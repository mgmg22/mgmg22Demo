package com.google.mgmg22demo.activity;

import android.os.Bundle;
import android.os.Handler;

import com.google.mgmg22demo.R;
import com.google.mgmg22demo.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
//    @BindView(R.id.btn_01)
//    Button btn_01;

    @OnClick(R.id.btn_01)
    void start() {
        intent2Activity(RecyclerViewActivity.class);
    }

    @OnClick(R.id.btn_02)
    void start2() {
        intent2Activity(TestTvActivity.class);
    }

    @OnClick(R.id.btn_03)
    void start3() {
        showProgressDialog(true, "正在加载");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                cancelProgressDialog();
//                stopProgressDialog();
            }
        }, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
