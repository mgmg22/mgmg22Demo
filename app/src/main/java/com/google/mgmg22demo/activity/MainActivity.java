package com.google.mgmg22demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.mgmg22demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    Intent start_intent;
    @BindView(R.id.btn_01)
    Button btn_01;

    @OnClick(R.id.btn_01)
    void start() {
        start_intent = new Intent(getApplicationContext(), RecyclerViewActivity.class);
        startActivity(start_intent);
    }

    @OnClick(R.id.btn_02)
    void start2() {
        start_intent = new Intent(getApplicationContext(), TestTvActivity.class);
        startActivity(start_intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
