package models.enums;

public enum ProjectileType {
    PEA("STRAIGHT"),
    ICE_PEA("STRAIGHT"),
    FIRE_PEA("STRAIGHT"),
    GIANT_PEA("STRAIGHT"),
    PLASMA("STRAIGHT"),
    CITRON("STRAIGHT"),
    ROTOBAGA_PROJECTILE("STRAIGHT"),
    CACTUS("STRAIGHT"),
    CACTUS_SPECIAL("STRAIGHT"),
    STAR("STAR"),
    GOO("STRAIGHT"),
    GOO_SPECIAL("STRAIGHT"),
    SMALL_SHROOM("STRAIGHT"),
    FUME("STRAIGHT"),
    FUME_SPECIAL("STRAIGHT"),
    SMALL_BULB("STRAIGHT"),
    MEDIUM_BULB("STRAIGHT"),
    LARGE_BULB("STRAIGHT"),
    SPECIAL_BULB("STRAIGHT"),
    KERNEL("LOBBED"),
    MELON("LOBBED"),
    ICE_MELON("LOBBED"),
    CABBAGE("LOBBED");

    public final String movement;
    ProjectileType(String movement) {this.movement = movement;}
}
