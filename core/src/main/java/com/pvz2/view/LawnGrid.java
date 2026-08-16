package com.pvz2.view;

public class LawnGrid {
    // نقطه شروع زمین روی صفحه (مبدأ خانه 0,0)
    public static final float ORIGIN_X = 532f;
    public static final float ORIGIN_Y = 175f;

    // ابعاد هر خانه از چمن
    public static final float CELL_WIDTH = 142f;
    public static final float CELL_HEIGHT = 125f;

    public static final int COLS = 9;
    public static final int ROWS = 5;

    // محاسبه X بر اساس ستون
    public static float getCellX(int col) {
        return ORIGIN_X + (col * CELL_WIDTH);
    }

    // محاسبه Y بر اساس سطر (ردیف)
    public static float getCellY(int row) {
        return ORIGIN_Y + (row * CELL_HEIGHT);
    }

    public static int getColFromX(float x) {
        float startX = ORIGIN_X - (CELL_WIDTH / 2f);
        if (x < startX) return -1;
        int col = (int) ((x - startX) / CELL_WIDTH);
        return (col >= 0 && col < COLS) ? col : -1;
    }

    // محاسبه اندیس سطر بر اساس مختصات Y موس
    public static int getRowFromY(float y) {
        float startY = ORIGIN_Y - (CELL_HEIGHT / 2f);
        if (y < startY) return -1;
        int row = (int) ((y - startY) / CELL_HEIGHT);
        return (row >= 0 && row < ROWS) ? row : -1;
    }
}
