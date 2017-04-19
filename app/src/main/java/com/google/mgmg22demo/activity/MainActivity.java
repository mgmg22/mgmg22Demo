package com.google.mgmg22demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.google.mgmg22demo.R;
import com.google.mgmg22demo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    //    @BindView(R.id.btn_01)
//    Button btn_01;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView mBottomNavigationView;
    private int mCurrPosition;

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
        initView();
    }

    private void initView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mCurrPosition = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.item_home_page:
//                        showHideFragment(0);
                        break;
                    case R.id.item_category:
//                        showHideFragment(1);
                        break;
                    case R.id.item_mine:
//                        showHideFragment(2);
                        break;
                }
                return true;
            }
        });
    }

}
