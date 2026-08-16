package com.pvz2.models.world.mechanics;

import com.pvz2.models.core.App;
import com.pvz2.models.core.DifficultyCalculator;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.Sun;
import com.pvz2.models.world.SunType;

import java.util.Random;

public class SunSpawnMechanic implements Mechanic {
    private float lastSpawnTime = -8f; // خورشید اول خیلی زودتر (حدود ۴ ثانیه پس از شروع) می‌افتد
    private float spawnInterval = 12f; // فاصله پایه (ثانیه)

    @Override
    public void applyMechanic(GameWorld world) {
        float now = world.getElapsedTime();

        int difficulty = App.getCurrentUser().getGameDifficulty();
        double increaseFactor = DifficultyCalculator.increaseFactor(difficulty);

        // محاسبه فاصله اسپاون بر اساس سختی
        float adjustedInterval = (float) (spawnInterval * increaseFactor);

        if (now - lastSpawnTime >= adjustedInterval) {
            spawnRandomSun(world);

            // با گذشت زمان، خورشیدها کمی سریع‌تر می‌افتند (حداقل ۶ ثانیه)
            spawnInterval = Math.max(6f, 12f - 0.02f * world.getElapsedTime());
            lastSpawnTime = now;
        }
    }

    private void spawnRandomSun(GameWorld world) {
        // اطمینان از وجود سطر و ستون معتبر برای جلوگیری از کرش
        int rows = world.getRows() > 0 ? world.getRows() : 5;
        int cols = world.getCols() > 0 ? world.getCols() : 9;

        Random random = new Random();
        double r = random.nextDouble();
        SunType type;

        if (r < 0.75) {
            type = SunType.NORMAL;
        } else if (r < 0.90) {
            type = SunType.SPECIAL;
        } else {
            type = SunType.RADIOACTIVE;
        }

        int row = random.nextInt(rows);
        int col = random.nextInt(cols);

        Sun sun = world.getSunsPool().acquire();
        sun.setup(row, col, type);
        world.getActiveSuns().add(sun);
    }
}
