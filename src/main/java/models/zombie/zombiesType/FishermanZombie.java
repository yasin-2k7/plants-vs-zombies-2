package models.zombie.zombiesType;

import models.core.App;
import models.enums.PlantLayer;
import models.enums.Zombies;
import models.plant.Plant;
import models.world.Cell;
import models.world.GameWorld;
import models.zombie.Zombie;

public class FishermanZombie extends Zombie {
    private static final int HOOK_INTERVAL = 45;
    private int hookCooldown;

    public FishermanZombie(int health, int damage) {
        super(Zombies.FISHERMAN, health, 0, damage);
        this.hookCooldown = 0;
    }

    @Override
    public void update() {
        if (isDead) return;

        // ماهیگیر حرکت نمی‌کند
        if (hookCooldown <= 0) {
            tryHook();
            hookCooldown = HOOK_INTERVAL;
        } else {
            hookCooldown--;
        }
    }

    private void tryHook() {
        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        // محاسبه شماره ردیف از موقعیت عمودی
        int row = (int) (this.y / App.getCellHeight());
        if (row < 0 || row >= game.getRows()) return;

        // پیدا کردن نزدیک‌ترین گیاه در همان سطر، سمت راست ماهیگیر
        // (چون ماهیگیر در ستون آخر است، گیاهان سمت چپ او قرار دارند)
        Plant target = game.getNearestPlantInRow(row, this.x + 10);
        if (target == null) return;

        float targetX = target.getX();
        float targetY = target.getY();

        // فاصله گیاه تا ماهیگیر
        float distance = Math.abs(targetX - this.x);

        // اگر گیاه در کنار ماهیگیر باشد (کمتر از عرض یک سلول)
        if (distance < App.getCellWidth()) {
            // نابود کردن گیاه
            target.die();
            System.out.println("Fisherman threw and destroyed plant at (" + targetX + ", " + targetY + ")");
            return;
        }

        // پیدا کردن سلول فعلی گیاه و سلول مقصد (یک خانه به راست)
        Cell currentCell = Cell.findCell(targetX, targetY, game.getGrid());
        if (currentCell == null) return;

        int targetCol = currentCell.getCol() + 1; // یک خانه به راست
        if (targetCol >= game.getCols()) return; // اگر خارج از محدوده بود، کاری نکن

        Cell targetCell = game.getGrid()[row][targetCol];
        if (targetCell == null || !targetCell.isEmpty()) {
            // سلول مقصد خالی نیست
            return;
        }

        // جابجایی گیاه به سلول مقصد
        // ۱. پیدا کردن لایه گیاه
        PlantLayer layer = null;
        for (PlantLayer l : PlantLayer.values()) {
            if (currentCell.getPlant(l) == target) {
                layer = l;
                break;
            }
        }
        if (layer == null) return;

        // ۲. حذف از سلول فعلی
        currentCell.setPlant(null, layer);

        // ۳. افزودن به سلول مقصد (با همان لایه)
        targetCell.setPlant(target, layer);

        // ۴. به‌روزرسانی مختصات گیاه
        target.setX((int) targetCell.getX());
        target.setY((int) targetCell.getY());
        target.setCell(targetCell); // به‌روزرسانی مرجع سلول

        System.out.println("Fisherman pulled plant from (" + currentCell.getX() + ", " + currentCell.getY() +
                ") to (" + targetCell.getX() + ", " + targetCell.getY() + ")");
    }

    @Override
    public void move() {
        // ماهیگیر حرکت نمی‌کند
    }
}