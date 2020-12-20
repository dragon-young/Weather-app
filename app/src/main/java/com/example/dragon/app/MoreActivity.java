package com.example.dragon.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragon.app.db.DBManger;
import com.example.dragon.app.game.Game2048;
import com.example.dragon.app.game.GameSnack;

import static java.util.ResourceBundle.clearCache;


public class MoreActivity extends AppCompatActivity implements View.OnClickListener{

    TextView bgTv,cacheTv,versionTv,shareTv,snackTv;
    RadioGroup exbgRg;
    ImageView backIv;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        /* 改变壁纸的按钮 */
        bgTv = (TextView) findViewById(R.id.more_tv_exchangebg);
        /* 清除缓存的按钮 */
        cacheTv = (TextView) findViewById(R.id.more_tv_cache);
        /* 当前版本的按钮 */
        versionTv = (TextView) findViewById(R.id.more_tv_version);
        /* 分享软件的按钮 */
        shareTv = (TextView) findViewById(R.id.more_tv_share);
        /* 返回首页的按钮 */
        backIv = (ImageView) findViewById(R.id.more_iv_back);
        /* 改变壁纸的按钮选项 */
        exbgRg = (RadioGroup) findViewById(R.id.more_rg);
        /* 进入贪吃蛇游戏界面的选项 */
        snackTv = (TextView) findViewById(R.id.more_tv_snack);

        /* 为他们都设置监听按钮 */
        bgTv.setOnClickListener(this);
        cacheTv.setOnClickListener(this);
        shareTv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        snackTv.setOnClickListener(this);

        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        String versionName = getVersionName();
        versionTv.setText("当前版本:    v"+versionName);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.more_iv_back:
                finish();
                break;
            case R.id.more_tv_cache:
                clearCache();
                break;
            case R.id.more_tv_share:
                shareSoftwareMsg("艺简天气app是一款超萌超可爱的天气预报软件，画面简约，播报天气情况非常精准，快来下载吧！");
                break;
            case R.id.more_tv_exchangebg:
                intent.setClass(this, Game2048.class);
                break;
            case R.id.more_tv_snack:
                intent.setClass(this, GameSnack.class);
        }
        startActivity(intent);
    }

    private void shareSoftwareMsg(String s) {
        /* 分享软件的函数*/

        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(Intent.createChooser(shareIntent,"艺简天气"));
    }

    private void clearCache() {
        /* 清除缓存的函数*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("确定要删除所有缓存么？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBManger.deleteAllInfo();
                Toast.makeText(MoreActivity.this,"已清除全部缓存！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setNegativeButton("取消",null);
        builder.create().show();
    }

    public String getVersionName() {
        /* 获取应用的版本名称*/
        PackageManager manager = getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
