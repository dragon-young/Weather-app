package com.example.dragon.app.city_manage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dragon.app.R;
import com.example.dragon.app.db.DBManger;
import com.example.dragon.app.db.DatabaseBean;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView addIv,backIv,deleteIv;
    ListView cityLv;
    List<DatabaseBean> mDatas;  //显示列表数据源
    private CityManagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        // 数据源
        addIv = (ImageView) findViewById(R.id.city_iv_add);
        backIv = (ImageView) findViewById(R.id.city_iv_back);
        deleteIv = (ImageView) findViewById(R.id.city_iv_delete);
        cityLv = (ListView) findViewById(R.id.city_lv);
        mDatas = new ArrayList<>();
        // 添加点击监听事件
        addIv.setOnClickListener(this);
        deleteIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        // 设置适配器
        adapter = new CityManagerAdapter(this, mDatas);
        cityLv.setAdapter(adapter);
    }

    /*  获取数据库当中真实数据源，添加到原有数据源当中，提示适配器更新*/
    @Override
    protected void onResume() {
        super.onResume();
        List<DatabaseBean> list = DBManger.queryAllInfo();
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_iv_add:
                int cityCount = DBManger.getCityCount();
                if (cityCount<5) {
                    Intent intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(this,"存储城市数量已达上限，请删除后再增加",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_back:
                finish();
                break;
            case R.id.city_iv_delete:
                Intent intent1 = new Intent(this, DeleteCityActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
