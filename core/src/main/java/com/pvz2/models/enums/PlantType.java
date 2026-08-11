package com.pvz2.models.enums;

import java.util.List;

import static com.pvz2.models.enums.PlantFamily.*;

public enum PlantType {
    SUNFLOWER(SUN_PRODUCER, 50, 50, "Day"),
    TWIN_SUNFLOWER(SUN_PRODUCER, 125, 150, "Day"),
    SUN_SHROOM(SUN_PRODUCER, 25, 50, "Shroom", "Wramp-up", "Night"),
    PRIMAL_SUNFLOWER(SUN_PRODUCER, 75, 50),
    GOLD_BLOOM(SUN_PRODUCER, 0, 750),
    PEASHOOTER(SHOOTER, 100, 50, "Pea"),
    REPEATER(SHOOTER, 200, 50, "Pea"),
    THREEPEATER(SHOOTER, 300, 50, "Pea"),
    SNOW_PEA(SHOOTER, 150, 50, "Pea", "Ice"),
    XSHOT(SHOOTER, 150, 50),
    PEA_POD(SHOOTER, 125, 50, "Pea", "Stack"),
    SPLIT_PEA(SHOOTER, 125, 50, "Pea"),
    CITRON(SHOOTER, 350, 50, "Charge"),
    BOWLING_BULB(SHOOTER, 200, 50, "Charge"),
    CACTUS(STRIKE_THROUGH, 175, 50),
    FIRE_PEASHOOTER(SHOOTER, 175, 50, "Fire", "Pea"),
    STARFRUIT(SHOOTER, 150, 50),
    POISON_PEASHOOTER(SHOOTER, 125, 50, "Poison"),
    MEGA_GATLING(SHOOTER, 400, 50, "Pea"),
    SEA_SHROOM(SHOOTER, 0, 150, "Shroom", "Water"),
    PUFF_SHROOM(SHOOTER, 0, 50, "Shroom"),
    FUME_SHROOM(STRIKE_THROUGH, 125, 50, "Shroom"),
    CABBAGE_PULT(LOBBER, 100, 50),
    KERNEL_PULT(LOBBER, 100, 50),
    MELON_PULT(LOBBER, 325, 50, "AoE"),
    WINTER_MELON(LOBBER, 500, 50, "AoE", "Ice"),
    PEPPER_PULT(LOBBER, 200, 50, "AoE", "Fire"),
    POTATO_MINE(EXPLOSIVE, 25, 250, "Trap", "Charge"),
    PRIMAL_POTATO_MINE(EXPLOSIVE, 50, 50, "Trap", "Charge"),
    CHERRY_BOMB(EXPLOSIVE, 150, 350),
    SQUASH(EXPLOSIVE, 50, 200, "Trap"),
    GRAPESHOT(EXPLOSIVE, 150, 350),
    JALAPENO(EXPLOSIVE, 125, 350, "Fire"),
    DOOM_SHROOM(EXPLOSIVE, 125, 150, "Shroom"),
    TANGLE_KELP(EXPLOSIVE, 25, 150, "Trap"),
    ICEBURG(EXPLOSIVE, 0, 200, "Trap", "Ice"),
    BONK_CHOY(MELEE, 150, 50),
    PHAT_BEET(MELEE, 150, 50, "AoE"),
    CHOMPER(MELEE, 150, 50),
    WASABI_WHIP(MELEE, 150, 50, "Fire"),
    KIWIBEAST(MELEE, 175, 50, "AoE", "Wramp-up"),
    WALL_NUT(WALL_NUTS, 50, 200),
    TALL_NUT(WALL_NUTS, 125, 200),
    ENDURIAN(WALL_NUTS, 100, 150),
    GARLIC(WALL_NUTS, 50, 200, "Move Zombies"),
    SWEET_POTATO(WALL_NUTS, 150, 200, "Move Zombies"),
    EXPLODE_O_NUT(WALL_NUTS, 50, 200, "Explosive"),
    GIANT_WALLNUT(WALL_NUTS, 0, 0),
    PUMPKIN(WALL_NUTS, 150, 200, "Stack"),
    SUN_BEAN(WALL_NUTS, 50, 200, "Sun"),
    TORCHWOOD(MODIFIER, 175, 50, "Fire"),
    MAGNET_SHROOM(HOMING, 100, 150, "Shroom", "Magic"),
    IMITATER(MODIFIER, 0, 0),
    ICE_SHROOM(EXPLOSIVE, 75, 500, "Shroom", "Ice"),
    LILY_PAD(MODIFIER, 25, 50, "Water", "Stack"),
    HOT_POTATO(EXPLOSIVE, 0, 50, "Fire"),
    GRAVE_BUSTER(EXPLOSIVE, 0, 100),
    ENLIGHTEN_MINT(SUN_PRODUCER, 0, 850),
    APPEASE_MINT(SHOOTER, 0, 850),
    ARMA_MINT(LOBBER, 0, 850),
    BOMBARD_MINT(EXPLOSIVE, 0, 850),
    ENFORCE_MINT(MELEE, 0, 850),
    REINFORCE_MINT(WALL_NUTS, 0, 850),
    ENCHANT_MINT(MODIFIER, 0, 850),
    SPEAR_MINT(STRIKE_THROUGH, 0, 850),
    CONTAIN_MINT(HOMING, 0, 850),
    MARIGOLD(WALL_NUTS, 0, 0); // گل معمولی گلخانه;

    public final PlantFamily family;
    public final int baseSunCost;
    public final int baseCoolDown;
    public final List<String> tags;

    PlantType(PlantFamily family, int baseSunCost, int baseCoolDown, String... tags) {
        this.family = family;
        this.baseSunCost = baseSunCost;
        this.baseCoolDown = baseCoolDown;
        this.tags = List.of(tags);
    }

    public boolean hasAnyTag() {
        return !tags.isEmpty();
    }

    public boolean hasTag(String tag){
        return this.tags.contains(tag);
    }
}
