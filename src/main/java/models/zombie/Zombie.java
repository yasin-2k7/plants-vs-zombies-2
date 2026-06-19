package models.zombie;

import models.enums.Zombies;

public abstract class Zombie {
    protected Zombies name;
    protected int health;
    protected int speed;
    protected int damage;
    protected boolean isDead = false;
    private float x, y;

    public Zombie(Zombies name, int health, int speed, int damage) {
        this.name = name;
        this.health = health;
        this.speed = speed;
        this.damage = damage;
    }

        // متد آپدیت که در هر فریم بازی صدا زده می‌شود
        public void update() {
            if (isDead) return;
            move(); // فراخوانی حرکت
    }

        // متد انتزاعی حرکت که هر زامبی می‌تواند آن را تغییر دهد
        public void move() {
        // منطق پیش‌فرض حرکت به سمت چپ
    }

        // متد مشترک دریافت آسیب
        public void takeDamage(int amount, String damageType) {
            if (isDead) return;

            this.health -= amount;
            if (this.health <= 0) {
                die();
            }
        }

        public abstract void damageToPlant();

        // متد مرگ زامبی
        public void die() {
            this.isDead = true;
            // TODO:  حذف از لیست زامبی‌های بازی
        }

        // Getters & Setters
        public int getSpeed() { return speed; }
        public void setSpeed(int speed) { this.speed = speed; }
        public int getHealth() { return health; }
        public void setHealth(int health) { this.health = health; }
        public String getName() { return name; }
}