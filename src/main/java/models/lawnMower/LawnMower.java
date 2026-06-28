package models.lawnMower;
import java.util.List;
import models.zombie.Zombie;

public class LawnMower {
    private int row;
    private boolean isActive;
    private boolean isSpent;
    private double positionX;
    private double speed = 5.0;

    public LawnMower(int row) {
        this.row = row;
        this.isActive = false;
        this.isSpent = false;
        this.positionX = 0.0; // مختصات ابتدای ردیف
    }

    public void activate() {
        if (!isActive && !isSpent) {
            this.isActive = true;
            System.out.println("The lawn mower in the row " + row + "is triggered and killed these zombies:");
        }
    }

    public void checkCollision(Zombie firstZombieInRow) { //برخورد
        if (firstZombieInRow != null && firstZombieInRow.getX() <= 0) {
            if (!isActive && !isSpent) {
                activate();
            }
        }
    }

    // متد نابود کردن زامبی‌ها هنگام عبور
    public void mowZombies(List<Zombie> zombiesInRow) {
        if (!isActive) return;

        for (Zombie z : zombiesInRow) {
            if (!z.isDead() && z.getX() <= this.positionX) {
                if (!z.isBoss()) {
                System.out.println("- " + z.getName().name());
                z.takeDamage(99999, "MOWER"); // دمیج بالا برای کشتن قطعی
                }
            }
        }
    }

    public void move() {
        if (isActive) {
            positionX += speed;
            if (isOutOfBounds()) {
                isActive = false;
                isSpent = true;
            }
        }
    }

    public boolean isOutOfBounds() {
        // فرض می‌کنیم عرض صفحه بازی ۱۰۰۰ پیکسل است
        return positionX > 1000.0;
    }

    public boolean isActive() { return isActive; }
    public int getRow() { return row; }
    public boolean isSpent() {
        return isSpent;
    }
}
