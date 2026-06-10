package models.lawnMower;
import java.util.List;
import models.zombie.Zombie;

public class LawnMower {
    private int row;              // ردیفی که چمن‌زن در آن قرار دارد
    private boolean isActive;     // آیا فعال شده و در حال حرکت است؟
    private double positionX;     // موقعیت محور X برای حرکت در طول صفحه

    public LawnMower(int row) {
        this.row = row;
        this.isActive = false;
        this.positionX = 0.0; // مختصات ابتدای ردیف
    }

    // متد فعال‌سازی
    public void activate() {
        this.isActive = true;
        // منطق اینجا: تغییر وضعیت برای شروع حرکت چمن‌زن به سمت راست صفحه
    }

    // متد بررسی برخورد با زامبی
    public void checkCollision(Zombie firstZombieInRow) {

    }

    // متد نابود کردن زامبی‌ها هنگام عبور
    public void mowZombies(List<Zombie> zombiesInRow) {
       }

    public boolean isActive() { return isActive; }
    public int getRow() { return row; }

    private double speed = 5.0; // سرعت حرکت چمن‌زن

    public void move() {
        if (isActive) {
            positionX += speed;
        }
    }

    public boolean isOutOfBounds() {
        // فرض می‌کنیم عرض صفحه بازی ۱۰۰۰ پیکسل است
        return positionX > 1000.0;
    }
}
