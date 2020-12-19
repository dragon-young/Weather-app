package com.example.dragon.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dragon.app.base.BaseFragment;
import com.example.dragon.app.bean.WeatherBean;
import com.example.dragon.app.db.DBManger;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;




/**
 *      Fragment是一种可以嵌入在活动中的UI片段，
 *      1. 能够让程序更加合理和充分地利用大屏幕的空间，
 *      2. 出现的初衷是为了适应大屏幕的平板电脑，
 *      3. 可以将其看成一个小型Activity
 *      4. 使用Fragment可以把屏幕划分成几块，然后进行分组，进行一个模块化管理
 *      5. Fragment不能够单独使用，需要嵌套在Activity中使用
 */
public class CityWheatherFrgment extends BaseFragment implements View.OnClickListener{

    TextView tempTv;
    TextView cityTv;
    TextView conditionTv;
    TextView windTv;
    TextView tempRangeTv;
    TextView dateTv;
    TextView clothIndexTv;
    TextView carIndexTv;
    TextView coldIndexTv;
    TextView sportIndexTv;
    TextView raysIndexTv;
    ImageView dayIv;
    LinearLayout futureLayout;

    String url1 = "https://wis.qq.com/weather/common?source=pc&weather_type=observe|index|rise|alarm|air|tips|forecast_24h&province=";
    String url2 = "&city=";
    String city;
    String provice;

    // 这个是json数据中的  json.data.index 的数据
    WeatherBean.DataBean.IndexBean index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate()的作用就是将一个用xml定义的布局文件查找出来
        // inflate是加载一个布局文件
        /**
         * 3 个参数
         * 1. 需要加载布局文件的id，意思是需要将这个布局文件中加载到Activity中来操作
         * 2. 需要附加到资源文件的根控件，如果第三个参数attachToRoot为true，就将这个root作为根对象返回
         *          否则仅仅将这个root对象的LayoutParams属性附加到对象的根布局对象上
         * 3. 是否将root附加到布局文件的根视图上
         */
        View view = inflater.inflate(R.layout.fragment_city_wheather_frgment, container, false);
        // 以通过activity传值获取到当前fragment加载的是那个地方的省份和天气情况
        /*
            activity向fragment传值的步骤
            1.创建一个fragment实例
            2.创建一个Bundle 对象（其为键值对存储，可以理解为一个Map<K,V>
            3.向bundle中传值：bundle.putString("str","这是MainActivity传来的值~");
            4.将bundle通过setArguments(bundle)方法设置进fragment01：fragment01.setArguments(bundle);
         */
        Bundle bundle = getArguments();
        String provice_city = bundle.getString("city");
        //   获取省份
        if(provice_city.split(" ").length > 1)
        {   provice = provice_city.split(" ")[0];
            city = provice_city.split(" ")[1];
        }
        else
        {
            city = provice_city.split(" ")[0];
            provice = provice_city.split(" ")[0];
        }
        String url = url1+provice+url2+city;
        loadData(url);

        // 初始化组件
        initView(view);
        return view;
    }

    @Override
    public void onSuccess(String result) {
        // 解析并展示数据
        try {
            parseShowData(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 更新数据
        int i = DBManger.updateInfoByCity(city, result);
        if (i<=0) {
            //  更新数据库失败，说明没有这条城市信息，增加这个城市记录
            DBManger.addCityInfo(city,result);
        }

    }

    // 解析并展示数据
    private void parseShowData(String result) throws ParseException{
        // 使用geson解析数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        WeatherBean.DataBean resultsBean = weatherBean.getData();

        // 设置城市
        cityTv.setText(city);
        // 获取指数信息集合
        index = resultsBean.getIndex();
        // 设置TextView

        // 获取今日天气情况, 有 今天的温度, degree, 湿度humidity, 天气情况weather
        WeatherBean.DataBean.ObserveBean todayDataBean = resultsBean.getObserve();


        // 获取时间 202012170850， 所以我们需要解析时间 2020-12-17 08:57
        String time = changeTime(todayDataBean.getUpdate_time());
        dateTv.setText("发布时间  "+ time);

        //  todayDataBean.getHumidity()  ---  "97"
        windTv.setText("湿度 "+ todayDataBean.getHumidity() + "%");

        // todayDataBean.getPressure() ---  "969"
        tempRangeTv.setText("气压  "+ todayDataBean.getPressure() + "hPa");

        // todayDataBean.getWeather_short()  ----  "阵雨"
        conditionTv.setText(todayDataBean.getWeather_short());
        // 获取实时天气温度情况，需要处理字符串
        tempTv.setText(todayDataBean.getDegree()+"°C");


        //  获取未来三天的天气情况，加载到layout当中
        WeatherBean.DataBean.Forecast24hBean futureList = resultsBean.getForecast_24h();

        /**
         * LayoutInflater 是什么鬼？
         * 1. 一个用于加载布局的系统服务，就是实例化与Layout XML文件对应的View对象
         *
         * 实例化 LayoutInflatter，调用inflate加载布局
         * LayoutInflater.from 就是实例化 主活动
         *
         * inflate 接收2个参数
         * 第一个参数： 要加载的布局id
         * 第二个参数： 给该布局嵌套一层父布局， 不过不需要则为 null
         */
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);

        // 为其设置参数， 宽度设置为匹配父级宽度， 高度为自身高度
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        futureLayout.addView(itemView);

        //  idateTv 表示星期几
        TextView idateTv = (TextView) itemView.findViewById(R.id.item_center_tv_date);
        // iconTv 表示天气情况 -- 晴
        TextView iconTv = (TextView) itemView.findViewById(R.id.item_center_tv_con);
        // itemprangeTv 表示温度范围 -- 对应的是json数据中的maxDegree 和 minDegree
        TextView itemprangeTv = (TextView) itemView.findViewById(R.id.item_center_tv_temp);
        // wind 表示风向 -- 微风
        TextView wind = (TextView) itemView.findViewById(R.id.item_center_tv_winddirection);

        //  获取对应的位置的天气情况
        // futureList.get_$0()  是 昨天的天气情况
        // futureList.get_$1()  是 今天的天气情况
        // futureList.get_$2() 是 明天的天气情况
        // futureList.get_$3() 是 后天的天气情况
        // futureList.get_$4() 是 大后天的天气情况

        // getTime --  2020-12-17
        idateTv.setText(futureList.get_$2().getTime()+"   明天");
        // getDay_weather -- 小雨
        iconTv.setText(futureList.get_$2().getDay_weather());
        // getDay_wind_direction -- "东北风"
        wind.setText(futureList.get_$2().getDay_wind_direction());
        // getMin_degree 是 最小温度   getMax_degree 是 最大温度
        itemprangeTv.setText(futureList.get_$2().getMin_degree()+"~"+futureList.get_$2().getMax_degree()+"°C");


        View itemView3 = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        futureLayout.addView(itemView3);
        TextView idateTv3 = (TextView) itemView3.findViewById(R.id.item_center_tv_date);
        TextView iconTv3 = (TextView) itemView3.findViewById(R.id.item_center_tv_con);
        TextView itemprangeTv3 = (TextView) itemView3.findViewById(R.id.item_center_tv_temp);
        TextView wind3 = (TextView) itemView3.findViewById(R.id.item_center_tv_winddirection);
        //  获取对应的位置的天气情况
        idateTv3.setText(futureList.get_$3().getTime()+"   后天");
        iconTv3.setText(futureList.get_$3().getDay_weather());
        wind3.setText(futureList.get_$3().getDay_wind_direction());
        itemprangeTv3.setText(futureList.get_$3().getMin_degree()+"~"+futureList.get_$3().getMax_degree()+"°C");

        View itemView2 = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        futureLayout.addView(itemView2);
        TextView idateTv2 = (TextView) itemView2.findViewById(R.id.item_center_tv_date);
        TextView iconTv2 = (TextView) itemView2.findViewById(R.id.item_center_tv_con);
        TextView itemprangeTv2 = (TextView) itemView2.findViewById(R.id.item_center_tv_temp);
        TextView wind2 = (TextView) itemView2.findViewById(R.id.item_center_tv_winddirection);
        //   获取对应的位置的天气情况
        idateTv2.setText(futureList.get_$4().getTime()+" 大后天");
        iconTv2.setText(futureList.get_$4().getDay_weather());
        wind2.setText(futureList.get_$4().getDay_wind_direction());
        itemprangeTv2.setText(futureList.get_$4().getMin_degree()+"~"+futureList.get_$4().getMax_degree()+"°C");

    }

    // 时间格式化
    private String changeTime(String update_time) throws ParseException{
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sfstr = "";
        sfstr = sf2.format(sf1.parse(update_time));
        return sfstr;
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //  数据库当中查找上一次信息显示在Fragment当中
        String s = DBManger.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)) {
            try {
                parseShowData(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化组件的操作
     * @param view
     */
    private void initView(View view) {
        // 对应的是今天的温度
        tempTv = (TextView) view.findViewById(R.id.frag_tv_currenttemp);
        // 对应的所定位城市
        cityTv = (TextView) view.findViewById(R.id.frag_tv_city);
        // 对应的是 今天的天气情况
        conditionTv = (TextView) view.findViewById(R.id.frag_tv_condition);
        // 对应的是 东北风 3 ~ 4 级
        windTv = (TextView) view.findViewById(R.id.frag_tv_wind);
        // 对应的是今天的温度范围
        tempRangeTv = (TextView) view.findViewById(R.id.frag_tv_temprange);
        // 对应的是 今天的日期  2020-12-12
        dateTv = (TextView) view.findViewById(R.id.frag_tv_date);
        // 对应的是今天的穿衣指数
        clothIndexTv = (TextView) view.findViewById(R.id.frag_index_tv_dress);
        // 对应的是今天的洗车指数
        carIndexTv = (TextView) view.findViewById(R.id.frag_index_tv_washcar);
        // 对应的是今天的感冒指数
        coldIndexTv = (TextView) view.findViewById(R.id.frag_index_tv_cold);
        // 对应的是今天的运动指数
        sportIndexTv = (TextView) view.findViewById(R.id.frag_index_tv_sport);
        // 对应的是今天的紫外线指数
        raysIndexTv = (TextView) view.findViewById(R.id.frag_index_tv_rays);
        // 对应的是今天的 天气图片
        dayIv = (ImageView) view.findViewById(R.id.frag_iv_today);
        // 对应的是 未来三天的天气情况的布局
        futureLayout = (LinearLayout) view.findViewById(R.id.frag_center_layout);

        // 设置点击事件的监听
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /**
         *  AlertDialog 是一个对话框
         *  1. 该对话框置为所有界面元素之上
         *  2. 能够屏蔽掉其他控件的交互能力
         *
         *  如何创建一个对话框呢？
         *  1. 创建构造器AlertDialog.Builder的对象
         *  2. 通过构造器对象调用setTitle、setMessage、setIcon等方法构造对话框的标题、信息和图标等内容
         *  3. 根据需要调用setPositive/Negative/NeutralButton()方法设置正面按钮、负面按钮和中立按钮
         *  4. 调用构造器对象的create方法创建AlertDialog对象
         *  5. AlertDialog对象调用show方法，让对话框在界面上显示
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.frag_index_tv_dress:
                builder.setTitle("穿衣指数");
                // 获得衣服的提示语 index.(detail, info, name)
                WeatherBean.DataBean.IndexBean.ClothesBean cloth = index.getClothes();
                // msg = "天气寒冷，建议着厚羽绒服、毛皮大衣加厚毛衣等隆冬服装。年老体弱者尤其要注意保暖防冻。";
                String msg = cloth.getInfo()+"\n"+cloth.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_washcar:
                builder.setTitle("洗车指数");
                WeatherBean.DataBean.IndexBean.CarwashBean car = index.getCarwash();
                msg = car.getInfo()+"\n"+car.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_cold:
                builder.setTitle("感冒指数");
                WeatherBean.DataBean.IndexBean.ColdBean cold = index.getCold();
                msg = cold.getInfo()+"\n"+cold.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_sport:
                builder.setTitle("运动指数");
                WeatherBean.DataBean.IndexBean.SportsBean sport = index.getSports();
                msg = sport.getInfo()+"\n"+sport.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_rays:
                builder.setTitle("紫外线指数");
                WeatherBean.DataBean.IndexBean.UltravioletBean ultraviolet = index.getUltraviolet();
                msg = ultraviolet.getInfo()+"\n"+ultraviolet.getDetail();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
