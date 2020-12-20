package com.example.dragon.app.game;

/**
 * Created by yl177 on 2020/12/19.
 */

import android.graphics.Color;

/**
 * 定义格子元素
 */
public class GridSquare {
    private int mType; // 元素类型

    public GridSquare(int mType) {
        this.mType = mType;
    }

    public int getColor() {
        switch (mType) {
            case GameType.GRID:
                return Color.WHITE;
            case GameType.FOOD://食物
                return Color.BLUE;
            case GameType.SNAKE://蛇
                return Color.parseColor("#FF4081");
        }
        return Color.WHITE;
    }

    public void setType(int type) {
        mType = type;
    }

}
