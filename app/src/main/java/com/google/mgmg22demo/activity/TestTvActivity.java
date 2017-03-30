package com.google.mgmg22demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.mgmg22demo.R;
import com.google.mgmg22demo.widget.TestTv;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by uniware on 2017/2/27.
 */


public class TestTvActivity extends AppCompatActivity {
    @BindView(R.id.test_tv)
    TestTv tv;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_tv_layout);
        ButterKnife.bind(this);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestTvActivity.this, "aaaaa", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
