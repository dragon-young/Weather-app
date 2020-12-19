package com.example.dragon.app.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.example.dragon.app.R;


public class Game2048 extends AppCompatActivity {
    private int score = 0;
    private TextView tvScore;



    public Game2048() {
        mainActivity = this;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);
        tvScore = (TextView) findViewById(R.id.tvScore);
    }


    public void clearScore(){
        score = 0;
        showScore();
    }

    private void showScore() {
        tvScore.setText(score+"");
    }

    public void addScore(int s){
        score+=s;
        showScore();
    }
    private static Game2048 mainActivity = null;

    public static Game2048 getMainActivity() {
        return mainActivity;
    }
}
