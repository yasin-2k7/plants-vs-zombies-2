package models.lawnMower;

import models.zombie.Zombie;
import java.util.List;

public class LawnMowerManager {
    public LawnMowerManager() {
        // TODO: ساخت و جایگذاری اولیه چمن‌زن‌ها برای هر ردیف
    }

    public void updateMowers(List<Zombie> allZombies) {
        // TODO: فراخوانی متدهای بررسی فعال‌سازی، حرکت و حذف چمن‌زن‌ها
    }

    private void checkActivations(List<Zombie> allZombies) {
        // TODO: بررسی رسیدن زامبی به مرز و روشن کردن چمن‌زن
    }

    private List<Zombie> getZombiesInRow(List<Zombie> allZombies, int row) {
        // TODO: برگرداندن لیست زامبی‌های موجود در یک ردیف خاص
        return null;
    }

    private LawnMower getMowerByRow(int row) {
        // TODO: پیدا کردن چمن‌زن مربوط به یک ردیف
        return null;
    }
}
