package com.google.mgmg22demo.log

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.google.mgmg22demo.R

class MobileInfoView(context: Context) :
    IMonitorRecord {

    private var tvOne: TextView? = null
    private var tvTwo: TextView? = null
    private var tvThr: TextView? = null
    private var mWindowManager: WindowManager? = null
    private var mWindowParams: WindowManager.LayoutParams? = null
    private var handler: Handler? = null
    private var conteView: View? = null

    private var context: Context = context

    init {
        handler = Handler()
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        mWindowParams = WindowManager.LayoutParams()
        mWindowParams?.run {
            height = WindowManager.LayoutParams.WRAP_CONTENT
            width = WindowManager.LayoutParams.MATCH_PARENT
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP

            packageName = context.packageName
            flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)


            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //26及以上必须使用TYPE_APPLICATION_OVERLAY   @deprecated TYPE_PHONE
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
    }

    fun addView() {
        conteView = LayoutInflater.from(context).inflate(R.layout.common_mobile_top, null)
        tvOne = conteView?.findViewById(R.id.tv_one)
        tvTwo = conteView?.findViewById(R.id.tv_two)
        tvThr = conteView?.findViewById(R.id.tv_thr)
        mWindowManager?.addView(conteView, mWindowParams)
    }

    fun removeView() {
        conteView?.let {
            mWindowManager?.removeView(it)
        }
    }

    override fun addOneRecord(tvName: String?, tvNum: String?, isGoodValue: Boolean) {

        when (tvName) {
            "cpu" -> tvOne?.text = "CPU:${tvNum}%"
            "fps" -> tvTwo?.text = "FPS:$tvNum "
            "memory" -> tvThr?.text = "Memory:${tvNum}MB"
        }

    }
}