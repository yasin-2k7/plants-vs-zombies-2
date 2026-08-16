package com.pvz2.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pvz2.Main;
import com.pvz2.models.core.App;
import com.pvz2.models.world.ChapterWorld.DarkAgesWorld;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.obstacles.Grave;
import pvz.libpvz.pam.PamPlayer;

public class GraveGraphic {
    private final Grave grave;
    private float animTime = 0f;
    private boolean breakStarted = false;
    private boolean breakFinished = false;

    private static final String EGYPT_PAM_PATH = "768/INITIAL/GRAVESTONES/EGYPT_HIEROGLYPH/EGYPT_HIEROGLYPH.PAM";

    private static final String DARK_NORMAL_PAM_PATH = "768/FULL/GRAVESTONES/DARK_NOOP/DARK_NOOP.PAM";
    private static final String DARK_SUN_PAM_PATH = "768/FULL/GRAVESTONES/DARK_SUN/DARK_SUN.PAM";
    private static final String DARK_PLANT_FOOD_PAM_PATH = "768/FULL/GRAVESTONES/DARK_PLANTFOOD/DARK_PLANTFOOD.PAM";

    private final String pamPath;

    public GraveGraphic(Grave grave) {
        this(grave, App.getCurrentGame());
    }

    public GraveGraphic(Grave grave, GameWorld world) {
        this.grave = grave;
        this.pamPath = resolvePamPath(grave, world);
    }

    private String resolvePamPath(Grave grave, GameWorld world) {
        // اگر فصل Dark Ages بود
        if (world instanceof DarkAgesWorld) {
            Grave.GraveType type = grave.getType(); // یا متد دریافت نوع قبر در مدل شما

            if (type == Grave.GraveType.SUN) {
                return DARK_SUN_PAM_PATH;
            } else if (type == Grave.GraveType.PLANT_FOOD) {
                return DARK_PLANT_FOOD_PAM_PATH;
            } else {
                return DARK_NORMAL_PAM_PATH;
            }
        }

        // در غیر این صورت (مصر باستان)
        return EGYPT_PAM_PATH;
    }

    public void  update(float delta) {
        animTime += delta;

        // اگر قبر نابود شده باشد، انیمیشن شکستن آغاز می‌شود
        if (grave.isDying() && !breakStarted) {
            breakStarted = true;
            animTime = 0f;
        }

        // بعد از پایان انیمیشن شکستن (حدود 0.5 ثانیه)، متد markDestroyed اجرا می‌شود
        if (breakStarted && animTime >= 0.5f) {
            breakFinished = true;
            grave.markDestroyed();
        }
    }

    public void draw(SpriteBatch batch, PamPlayer pamPlayer, Main game){
        if (breakFinished) return;

        String animState;
        if (breakStarted) {
            animState = "damage1";
        } else {
            switch (grave.getDamageStage()) {
                case 1:  animState = "damage2"; break;
                case 2:  animState = "damage3"; break;
                default: animState = "undamaged"; break;
            }
        }

        boolean drawn = false;

        try {
            // رندر انیمیشن PAM مصر باستان
            pamPlayer.draw(
                batch,
                pamPath,
                animState,
                animTime,
                grave.getX(),
                grave.getY(),
                1.0f,
                1.0f,
                true
            );
            drawn = true;
        } catch (Exception e) {
            e.printStackTrace();
            // در صورت نبود فریم یا فایل PAM، فال‌بک تکسچر اجرا می‌شود
        }

        // رندر رزرو (Fallback) با Sprite در صورت عدم بارگذاری PAM
    }

    public boolean isBreakFinished() { return breakFinished; }
    public Grave getGrave() { return grave; }
}
