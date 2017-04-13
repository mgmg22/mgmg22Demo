package com.google.mgmg22demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.mgmg22demo.R;
import com.google.mgmg22demo.adapter.RecyclerViewAdapter;
import com.google.mgmg22demo.bean.TestBean;
import com.google.mgmg22demo.util.LogUtils;
import com.google.mgmg22demo.util.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by uniware on 2017/2/22.
 */

public class RecyclerViewActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<TestBean> data = new ArrayList<>();
    private RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, data);
    boolean isLoading;
    private int page;
    private int clearFlag = 0;

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_layout);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        toolbar.setTitle("标题");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.blueStatus);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        onSearch(skey.getSearchName());
//                    }
//                }, 1500);
                getData();
            }
        });
        swipeRefreshLayout.setRefreshing(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new MyItemDecoration());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Log.d("test", "StateChanged = " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.d("test", "onScrolled:" + dx + "," + dy);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                //表示当前底部
//                Log.d("test", "lastVisibleItemPosition:" + lastVisibleItemPosition);
                if (lastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    LogUtils.d("loading executed");
                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getData();
//                                Log.d("test", "load more completed");
//                                isLoading = false;
//                            }
//                        }, 2500);
                        getData();
                        LogUtils.d("load more completed");
                        isLoading = false;
                    }
                }
            }
        });
        //getData();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1000);
    }

    /**
     * 当查询条件改变时要重置page
     */
    private void clearData() {
        clearFlag = 1;
        page = 0;
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        /*
        String url = "http://";
        VolleyRequest.RequestGet(this, url, "Tag", new VolleyInterface(this,
                VolleyInterface.listener, VolleyInterface.errorListener) {
            @Override
            public void onMySuccess(String result) {
                if (clearFlag == 1) {
                    data.clear();
                    clearFlag = 0;
                    adapter.showFoot();
                }
                List<TestBean> temp = MyApplication.getGson().fromJson(result, $List(TestBean.class));
                if (temp.size() == 0) {
                    adapter.hideFoot();
                    Utility.showToast(getApplicationContext(), "没有更多数据");
                } else {
                    data.addAll(temp);
                    page++;
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyItemRemoved(adapter.getItemCount());
            }

            @Override
            public void onMyError(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e(getClass().getSimpleName(), "getData()请求失败：" + error.toString());
                Utility.noNetworkDataAlert(getApplicationContext());
            }
        });
        */

        //伪造的静态数据
        if (clearFlag == 1) {
            data.clear();
            clearFlag = 0;
            adapter.showFoot();
        }
        data.add(new TestBean("标题1", "角标1"));
        data.add(new TestBean("标题2", "角标2"));
        data.add(new TestBean("标题3", "角标3"));
        data.add(new TestBean("标题4", "角标4"));
        data.add(new TestBean("标题5", "角标5"));
        data.add(new TestBean("标题6", "角标6"));
        if (data.size() == 0) {
            adapter.hideFoot();
            Utility.showToast(getApplicationContext(), "没有更多数据");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyItemRemoved(adapter.getItemCount());
            }
        }, 1000);
    }

}
