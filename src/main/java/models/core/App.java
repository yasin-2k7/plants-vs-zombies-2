package models.core;

import models.projectile.strategy.FirePeaStrategy;
import models.projectile.strategy.HitStrategy;
import models.projectile.strategy.IcePeaStrategy;
import models.projectile.strategy.RegularPeaStrategy;

public class App {
    private static User currentUser;


    public static final HitStrategy FIRE_PEA = new FirePeaStrategy();
    public static final HitStrategy ICE_PEA = new IcePeaStrategy();
    public static final HitStrategy REGULAR_PEA = new RegularPeaStrategy();
    public static final HitStrategy ICE_MELON = new FirePeaStrategy();

    public static User getCurrentUser() {
        return currentUser;
    }
}
