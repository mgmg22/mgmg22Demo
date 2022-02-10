package com.google.mgmg22demo.log;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.view.Choreographer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;

/**
 * Created by matt.shen on 2022/02/10.
 */

public class PerformanceDataManager {
    private static final int NORMAL_FRAME_RATE = 1;

    private int mLastFrameRate;
    private float mLastCpuRate;
    private float mLastMemoryInfo;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private float mMaxMemory;
    private ActivityManager mActivityManager;
    private RandomAccessFile mProcStatFile;
    private RandomAccessFile mAppStatFile;
    private Long mLastCpuTime;
    private Long mLastAppCpuTime;
    private boolean mAboveAndroidO; // 是否是8.0及其以上
    public static final int MSG_CPU = 1;
    public static final int MSG_MEMORY = 2;
    public static final int MSG_FRAME = 4;
    public static final int MSG_SAVE_LOCAL = 3;
    private int mFrameRendered = 0;//Frame渲染的次数
    private Handler mainHandler;
    private long mLastFrameStartTime;//上一次Frame渲染的时间

    private final Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {

            long currentTimeMillis = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos);
            if (mLastFrameStartTime > 0) {
                long timeSpace = currentTimeMillis - mLastFrameStartTime;
                mFrameRendered++;
                //Frame采样时间
                int INTERVAL = 300;
                if (timeSpace >= INTERVAL) {
                    mLastFrameRate = (int) (mFrameRendered * 1000 / timeSpace);
                    mLastFrameStartTime = currentTimeMillis;
                    mFrameRendered = 0;
                }
            } else {
                mLastFrameStartTime = currentTimeMillis;
            }

            Choreographer.getInstance().postFrameCallback(this);

            Message obtain = Message.obtain();
            obtain.what = MSG_FRAME;
            obtain.obj = mLastFrameRate;
            mainHandler.sendMessageDelayed(obtain, NORMAL_FRAME_RATE * 1000);
        }
    };

    private void executeCpuData() {
        if (mAboveAndroidO) {
            mLastCpuRate = getCpuDataForO();
//            writeCpuDataIntoFile();
        } else {
            mLastCpuRate = getCPUData();
//            writeCpuDataIntoFile();
        }
    }

    private float getCpuDataForO() {
        java.lang.Process process = null;
        try {
            process = Runtime.getRuntime().exec("top -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int cpuIndex = -1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (TextUtils.isEmpty(line)) {
                    continue;
                }
                int tempIndex = getCPUIndex(line);
                if (tempIndex != -1) {
                    cpuIndex = tempIndex;
                    continue;
                }
                if (line.startsWith(String.valueOf(Process.myPid()))) {
                    if (cpuIndex == -1) {
                        continue;
                    }
                    String[] param = line.split("\\s+");
                    if (param.length <= cpuIndex) {
                        continue;
                    }
                    String cpu = param[cpuIndex];
                    if (cpu.endsWith("%")) {
                        cpu = cpu.substring(0, cpu.lastIndexOf("%"));
                    }
                    return Float.parseFloat(cpu) / Runtime.getRuntime().availableProcessors();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return 0;
    }

    private int getCPUIndex(String line) {
        if (line.contains("CPU")) {
            String[] titles = line.split("\\s+");
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].contains("CPU")) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void executeMemoryData() {
        mLastMemoryInfo = getMemoryData();
    }

    private static class Holder {
        private static final PerformanceDataManager INSTANCE = new PerformanceDataManager();
    }

    private PerformanceDataManager() {
    }

    public static PerformanceDataManager getInstance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAboveAndroidO = true;
        }
        mHandlerThread = new HandlerThread("handler-thread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Message obtain = Message.obtain();
                obtain.what = msg.what;
                switch (msg.what) {
                    case MSG_CPU:
                        executeCpuData();
                        obtain.obj = mLastCpuRate;
                        mHandler.sendEmptyMessageDelayed(MSG_CPU, NORMAL_FRAME_RATE * 1000);
                        break;
                    case MSG_MEMORY:
                        executeMemoryData();
                        obtain.obj = String.format("%.1f", mLastMemoryInfo);
                        mHandler.sendEmptyMessageDelayed(MSG_MEMORY, NORMAL_FRAME_RATE * 1000);
                        break;
                    case MSG_SAVE_LOCAL:
                        mHandler.sendEmptyMessageDelayed(MSG_SAVE_LOCAL, NORMAL_FRAME_RATE * 1000);
                        break;
                }
                mainHandler.sendMessage(obtain);
            }
        };
    }

    public void initMainThread(Handler handler) {
        mainHandler = handler;
    }

    public void startMonitorFrameInfo() {
        Choreographer.getInstance().postFrameCallback(mFrameCallback);
    }

    public void stopMonitorFrameInfo() {
        Choreographer.getInstance().removeFrameCallback(mFrameCallback);
    }

    public void startMonitorCPUInfo() {
        mHandler.sendEmptyMessageDelayed(MSG_CPU, NORMAL_FRAME_RATE * 1000);
    }

    public void stopMonitorCPUInfo() {
        mHandler.removeMessages(MSG_CPU);
    }

    public void start() {
        startMonitorCPUInfo();
        startMonitorFrameInfo();
        startMonitorMemoryInfo();
    }

    public void destroy() {
        stopMonitorMemoryInfo();
        stopMonitorCPUInfo();
        stopMonitorFrameInfo();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
        mHandlerThread = null;
    }

    public void startMonitorMemoryInfo() {
        if (mMaxMemory == 0) {
            mMaxMemory = mActivityManager.getMemoryClass();
        }
        mHandler.sendEmptyMessageDelayed(MSG_MEMORY, NORMAL_FRAME_RATE * 1000);
    }

    public void stopMonitorMemoryInfo() {
        mHandler.removeMessages(MSG_MEMORY);
    }

    private float getCPUData() {
        long cpuTime;
        long appTime;
        float value = 0.0f;
        try {
            if (mProcStatFile == null || mAppStatFile == null) {
                mProcStatFile = new RandomAccessFile("/proc/stat", "r");
                mAppStatFile = new RandomAccessFile("/proc/" + Process.myPid() + "/stat", "r");
            } else {
                mProcStatFile.seek(0L);
                mAppStatFile.seek(0L);
            }
            String procStatString = mProcStatFile.readLine();
            String appStatString = mAppStatFile.readLine();
            String[] procStats = procStatString.split(" ");
            String[] appStats = appStatString.split(" ");
            cpuTime = Long.parseLong(procStats[2]) + Long.parseLong(procStats[3]) + Long.parseLong(procStats[4]) + Long.parseLong(procStats[5]) + Long.parseLong(procStats[6]) + Long.parseLong(procStats[7]) + Long.parseLong(procStats[8]);
            appTime = Long.parseLong(appStats[13]) + Long.parseLong(appStats[14]);
            if (mLastCpuTime == null && mLastAppCpuTime == null) {
                mLastCpuTime = cpuTime;
                mLastAppCpuTime = appTime;
                return value;
            }
            if (mLastCpuTime != null) {
                value = ((float) (appTime - mLastAppCpuTime) / (float) (cpuTime - mLastCpuTime)) * 100f;
                mLastCpuTime = cpuTime;
                mLastAppCpuTime = appTime;
            }
        } catch (Exception e) {
//            LogHelper.e(TAG,"getCPUData fail: "+e.toString());
        }
        return value;
    }

    private float getMemoryData() {
        float mem = 0.0F;
        try {
            // 统计进程的内存信息 totalPss
            final Debug.MemoryInfo[] memInfo = mActivityManager.getProcessMemoryInfo(new int[]{Process.myPid()});
            if (memInfo.length > 0) {
                // TotalPss = dalvikPss + nativePss + otherPss, in KB
                final int totalPss = memInfo[0].getTotalPss();
                if (totalPss >= 0) {
                    // Mem in MB
                    mem = totalPss / 1024.0F;
                }
            }
        } catch (Exception e) {
//            LogHelper.e(TAG,"getMemoryData fail: "+e.toString());
        }
        return mem;
    }

}
