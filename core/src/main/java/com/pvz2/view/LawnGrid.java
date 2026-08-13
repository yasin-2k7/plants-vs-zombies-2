package com.pvz2.view;

public class LawnGrid {
    // نقطه شروع زمین روی صفحه (مبدأ خانه 0,0)
    public static final float ORIGIN_X = 530f;
    public static final float ORIGIN_Y = 220f;

    // ابعاد هر خانه از چمن
    public static final float CELL_WIDTH = 140f;
    public static final float CELL_HEIGHT = 125f;

    // محاسبه X بر اساس ستون
    public static float getCellX(int col) {
        return ORIGIN_X + (col * CELL_WIDTH);
    }

    // محاسبه Y بر اساس سطر (ردیف)
    public static float getCellY(int row) {
        return ORIGIN_Y + (row * CELL_HEIGHT);
    }
}
