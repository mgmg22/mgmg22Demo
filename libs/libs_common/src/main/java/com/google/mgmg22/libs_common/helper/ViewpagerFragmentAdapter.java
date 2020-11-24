package com.google.mgmg22.libs_common.helper;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;


/**
 * @Description:
 * @Author: 沈晓顺
 * @CreateDate: 2020/3/11 4:57 PM
 */
public class ViewpagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> titles;

    public ViewpagerFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    public ViewpagerFragmentAdapter(FragmentManager fm, List<Fragment> fragmentLists, List<String> titles) {
        super(fm);
        this.mFragments = fragmentLists;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && position <= titles.size()) {
            return titles.get(position);
        }
        return super.getPageTitle(position);
    }

    // 动态设置我们标题的方法
    public void setPageTitle(int position, String title) {
        if (position >= 0 && position < titles.size()) {
            titles.set(position, title);
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        if (mFragments != null) {
            if (position < mFragments.size()) {
                return mFragments.get(position).hashCode();       //important
            }
        }
        return super.getItemId(position);
    }
}
