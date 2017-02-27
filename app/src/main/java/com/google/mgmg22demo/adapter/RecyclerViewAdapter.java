package com.google.mgmg22demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.mgmg22demo.R;
import com.google.mgmg22demo.bean.TestBean;
import com.google.mgmg22demo.util.Utility;
import com.google.mgmg22demo.widget.CornerLabelView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends Adapter<ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private int FOOT_FLAG = 0;
    private Context context;
    private List data;
    CornerLabelView mClv;
    float mText1Height = 15;
    //指定角标名称及颜色
    private String nodes[] = {"角标1", "角标2", "角标3", "角标4", "角标5", "角标6"};
    private int colors[] = {0xffCDDC39, 0xff009688, 0xff3F51B5, 0xff9C27B0, 0xffF44336, 0xff03A9F4};

    public RecyclerViewAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.card_base, parent, false);
            return new ItemViewHolder(view, new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Utility.showToast(context, "第" + position + "行被点击");
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    Utility.showToast(context, "第" + position + "行LongClick");
                }
            });
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).tv_first.setText(((TestBean) data.get(position)).getGH_ID());
            ((ItemViewHolder) holder).tv_second.setText("测试 test 123456");
            //设置角标
            mClv = ((ItemViewHolder) holder).label;
            String node_name = ((TestBean) data.get(position)).getNODE_NAME();
            mClv.setText1(node_name);
            int lp = 0;
            for (; lp < nodes.length; lp++) {
                if (node_name.equals(nodes[lp])) {
                    break;
                }
            }
            mClv.setFillColor(colors[lp]);
            mClv.setText1Height(mText1Height - 1);
            mClv.setPaddingTop(mText1Height);
            mClv.setPaddingCenter(mText1Height / 3);
            mClv.setPaddingBottom(mText1Height / 3);
            if (holder instanceof FootViewHolder) {
                if (FOOT_FLAG == 1) {
                    ((FootViewHolder) holder).itemView.setVisibility(View.GONE);
                } else {
                    ((FootViewHolder) holder).itemView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void hideFoot() {
        FOOT_FLAG = 1;
    }

    public void showFoot() {
        FOOT_FLAG = 0;
    }

    static class ItemViewHolder extends ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        OnItemClickListener mListener;
        @BindView(R.id.tv_first)
        TextView tv_first;
        @BindView(R.id.tv_second)
        TextView tv_second;
        @BindView(R.id.label)
        CornerLabelView label;

        public ItemViewHolder(View view, OnItemClickListener ItemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            mListener = ItemClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onItemLongClick(v, getLayoutPosition());
            return true;
        }
    }

    static class FootViewHolder extends ViewHolder {
        public FootViewHolder(View view) {
            super(view);
        }
    }
}