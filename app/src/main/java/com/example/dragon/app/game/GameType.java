package com.example.dragon.app.game;

/**
 * Created by yl177 on 2020/12/19.
 */

public interface GameType {
    /* 格子类型 */
    int GRID = 0;
    /* 食物类型 */
    int FOOD = 1;
    /* 蛇的类型 */
    int SNAKE = 2;

    int LEFT = 1;
    int TOP = 2;
    int RIGHT = 3;
    int BOTTOM = 4;
}
