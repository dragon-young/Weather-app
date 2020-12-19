package com.example.dragon.app;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.dragon.app.city_manage.CityManagerActivity;
import com.example.dragon.app.db.DBManger;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView addCityIv,moreIv;
    LinearLayout pointLayout;
    ViewPager mainVp;

    // ViewPager 的数据源
    List<Fragment> fragmentList;

    // 选中城市的集合
    List<String> cityList;

    // 表示ViewPager 的个数，指示器显示的内容
    List<ImageView> imgList;

    // 定义城市页面的适配器
    /**
     * 说一下适配器的作用
     * 1. 适配器就像显示器，把复杂的东西按人可以接受的方式来展现
     */
    private CityFragmentPagerAdapter adapter;

    /**
     * onCreate 主活动一旦加载，就会执行该活动
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 添加测试的按钮
        addCityIv = (ImageView) findViewById(R.id.main_iv_add);
        // 更多的按钮
        moreIv = (ImageView) findViewById(R.id.main_iv_more);

        // 显示相关城市的界面 ViewPager
        mainVp = (ViewPager) findViewById(R.id.main_vp);
        pointLayout = (LinearLayout) findViewById(R.id.main_layout_point);

        // 为 添加城市按钮 设置 监听
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);

        fragmentList = new ArrayList<>();

//        cityList = new ArrayList<>();

        // 从数据库中拿到数据给集合里面
        cityList = DBManger.queryAllCityName();

        // 指示器。 就相当于按钮
        imgList = new ArrayList<>();

        if (cityList.size() == 0) {
//            cityList.add("成都");
//            cityList.add("沈阳");
            cityList.add("四川省 绵阳");
//            cityList.add("四川省 成都");
//            cityList.add("广东省 广州");
//            cityList.add("广东省 深圳");
        }

        /* 因为可能搜索界面点击跳转此界面，会传值，所以此处获取一下*/
        try {
            Intent intent = getIntent();
            String city = intent.getStringExtra("city");
            // 判断数据库里面的数据是否包含该城市
            if (!cityList.contains(city)&&!TextUtils.isEmpty(city)) {
                cityList.add(city);
            }
        }catch (Exception e){
            Log.i("dr","程序出现问题了！！");
        }

        // 初始化 ViewPager页面的方法
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);
        // 创建小圆点指示器
        initPoint();
        // 设置最后一个城市信息
        mainVp.setCurrentItem(fragmentList.size()-1);

        // 设置监听页面
        setPagerListener();
    }

    private void setPagerListener() {
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            /**
             * 代表视图被滑动的过程中不断的调用该方法
             * @param position
             * @param positionOffset
             * @param positionOffsetPixels
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 代表哪个页面被选中，当手指滑动页面的时候，如果滑动成功的话，就会执行这个方法
             * @param position 就是当前滑动到的页面
             */
            @Override
            public void onPageSelected(int position) {
                // 1. 先将所有页面的设置为一样的
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.mipmap.a1);
                }
                // 2. 在设置滑动的页面的图片
                imgList.get(position).setImageResource(R.mipmap.a2);
            }

            /**
             * 这个方法在手指操作屏幕的时候发生变化触发该方法
             * @param state end-0， press-1， up-2
             */
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        //  创建小圆点 ViewPager页面指示器的函数
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(pIv);
            pointLayout.addView(pIv);
        }
        imgList.get(imgList.size()-1).setImageResource(R.mipmap.a2);
    }

    private void initPager() {
        // 创建 fragment 对象， 添加到 ViewPager数据源中
        for (int i = 0; i < cityList.size(); i++) {
            CityWheatherFrgment cwFrag = new CityWheatherFrgment();
            Bundle bundle = new Bundle();
            bundle.putString("city", cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case  R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                break;
            case  R.id.main_iv_more:
                intent.setClass(this,MoreActivity.class);
                break;
        }
        startActivity(intent);
    }

    /* 当页面重写加载时会调用的函数，这个函数在页面获取焦点之前进行调用，此处完成ViewPager页数的更新*/
    @Override
    protected void onRestart() {
        super.onRestart();
        //  获取数据库当中还剩下的城市集合
        List<String> list = DBManger.queryAllCityName();
        if (list.size()==0) {
            list.add("北京");
        }
        cityList.clear();    //重写加载之前，清空原本数据源
        cityList.addAll(list);
        //  剩余城市也要创建对应的fragment页面
        fragmentList.clear();
        initPager();
        adapter.notifyDataSetChanged();
        //  页面数量发生改变，指示器的数量也会发生变化，重写设置添加指示器
        imgList.clear();
        pointLayout.removeAllViews();   //将布局当中所有元素全部移除
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);
    }
}
