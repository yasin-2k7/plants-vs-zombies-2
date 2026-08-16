package com.pvz2.models.plant;

import com.pvz2.models.enums.PlantType;

public class PlantAnimationClips {
    public static String getSpecialClip(PlantType type) {
        if (type.hasTag("Wramp-up") || type == PlantType.PUFF_SHROOM) {
            return type == PlantType.KIWIBEAST ? "attack_stage3" : "special_stage3";
        }
        if (type == PlantType.GOLD_BLOOM) return "attack";
        return "special";
    }

    public static String getUnarmedClip(PlantType type) {
        return "plant_idle";
    }

    public static String getTriggerClip(PlantType type) {
        if (type == PlantType.SQUASH) return "jump_down_right";
        if (type == PlantType.DOOM_SHROOM) return "stage2_explode";
        return "attack";
    }

    public static String getExplosionPamPath(PlantType type) {
        if (type == PlantType.POTATO_MINE) return "768/INITIAL/EFFECTS/POTATOMINE_EXPLOSION/POTATOMINE_EXPLOSION.PAM";
        if (type == PlantType.PRIMAL_POTATO_MINE) return
        "768/INITIAL/EFFECTS/PRIMAL_POTATOMINE_EXPLOSION/PRIMAL_POTATOMINE_EXPLOSION.PAM";
        if (type == PlantType.CHERRY_BOMB) return "768/FULL/EFFECTS/CHERRYBOMB_EXPLOSION_TOP" +
            "/CHERRYBOMB_EXPLOSION_TOP.PAM";
        if (type == PlantType.JALAPENO) return "768/INITIAL/EFFECTS/JALAPENO_FIRE/JALAPENO_FIRE.PAM";
        return null;
    }

    public static String getExplosionClip(PlantType type) {
        if (type == PlantType.POTATO_MINE || type == PlantType.PRIMAL_POTATO_MINE) return "animation";
        if (type == PlantType.CHERRY_BOMB) return "explosion";
        if (type == PlantType.JALAPENO) return "idle";
        return null;
    }
}
