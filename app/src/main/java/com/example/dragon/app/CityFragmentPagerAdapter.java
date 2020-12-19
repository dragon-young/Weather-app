package com.example.dragon.app;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by yl177 on 2020/12/15.
 */

/*
    1. PageAdapter是 ViewPager 的支持者
    2. ViewPager 将调用它来取得所需显示的页
    3. PageAdapter 也会在数据变化时，通知 ViewPager
 */
public class CityFragmentPagerAdapter extends FragmentStatePagerAdapter{

    List<Fragment> fragmentList;
    /*
        1. fragmentManger 是用来管理 fragment 的
     */
    public CityFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentLis) {
        super(fm);
        this.fragmentList = fragmentLis;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    int childCount = 0;   //表示ViewPager包含的页数
    //    当ViewPager的页数发生改变时，必须要重写两个函数
    @Override
    public void notifyDataSetChanged() {
        this.childCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (childCount>0) {
            childCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
