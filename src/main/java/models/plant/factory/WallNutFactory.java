package models.plant.factory;

import models.core.App;
import models.enums.PlantLayer;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.components.*;
import models.plant.components.explosionRanges.CircularRange;
import models.plant.components.explosiveBehaviors.AreaDamageBehavior;
import models.plant.components.explosiveTriggers.InstantTrigger;
import models.plant.components.moveZombieStrategy.AttractStrategy;
import models.plant.components.moveZombieStrategy.EjectStrategy;
import models.world.Sun;
import models.zombie.Zombie;

import java.util.Map;
import java.util.function.Supplier;

public class WallNutFactory {
    static void register(Map<PlantType, Supplier<Plant>> registry) {
        registry.put(PlantType.WALL_NUT, WallNutFactory::buildWallNut);
        registry.put(PlantType.TALL_NUT, WallNutFactory::buildTallNut);
        registry.put(PlantType.ENDURIAN, WallNutFactory::buildEndurian);
        registry.put(PlantType.GARLIC, WallNutFactory::buildGarlic);
        registry.put(PlantType.SWEET_POTATO, WallNutFactory::buildSweetPotato);
        registry.put(PlantType.EXPLODE_O_NUT, WallNutFactory::buildExplodeONut);
        registry.put(PlantType.PUMPKIN, WallNutFactory::buildPumpkin);
        registry.put(PlantType.SUN_BEAN, WallNutFactory::buildSunBean);
    }

    private static Plant buildWallNut() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.WALL_NUT);
        int health = 4000;
        if (level >= 2) health += 1000;
        if (level >= 4) health += 1500;
        Plant p = new Plant(PlantType.WALL_NUT, health, 0);
        p.addComponent(new WallNutsComponent() {
            @Override
            public void activatePlantFood(Plant owner) {
                ArmorComponent armor = p.getComponent(ArmorComponent.class);
                if (armor == null) {
                    p.addComponent(new ArmorComponent(4000));
                } else {
                    armor.setArmorHp(4000);
                }
            }
        });
        return p;
    }

    private static Plant buildTallNut() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.TALL_NUT);
        int health = 8000;
        if (level >= 2) health += 2000;
        if (level >= 4) health += 3000;
        Plant p = new Plant(PlantType.TALL_NUT, health, 0);
        p.addComponent(new WallNutsComponent() {
            @Override
            public void activatePlantFood(Plant owner) {
                ArmorComponent armor = p.getComponent(ArmorComponent.class);
                if (armor == null) {
                    p.addComponent(new ArmorComponent(8000));
                } else {
                    armor.setArmorHp(8000);
                }
            }
        });
        return p;
    }

    private static Plant buildEndurian() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.ENDURIAN);
        int health = (level >= 3) ? 4000 : 3000;
        int damage = (level >= 2) ? 25 : 20;
        Plant p = new Plant(PlantType.ENDURIAN, health, damage);
        p.addComponent(new WallNutsComponent() {
            @Override
            public int onTakeDamage(Plant owner, int damageAmount, Zombie attacker) {
                attacker.takeDamage(owner.getDamage(), "NORMAL");
                return damageAmount;
            }

            @Override
            public void activatePlantFood(Plant owner) {
                ArmorComponent armor = p.getComponent(ArmorComponent.class);
                if (armor == null) {
                    p.addComponent(new ArmorComponent(4000));
                } else {
                    armor.setArmorHp(4000);
                }
                if (owner.getDamage() != damage) return;
                owner.setDamage(owner.getDamage() + 5);
            }
        });
        return p;
    }

    private static Plant buildGarlic() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.GARLIC);
        int health = 300;
        if (level >= 2) health += 150;
        if (level >= 4) health += 250;
        Plant p = new Plant(PlantType.GARLIC, health, 0);
        p.addComponent(new MoveZombieComponent(new EjectStrategy()));
        return p;
    }

    private static Plant buildSweetPotato() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SWEET_POTATO);
        int health = 3000;
        if (level >= 2) health += 1000;
        if (level >= 4) health += 1500;
        Plant p = new Plant(PlantType.SWEET_POTATO, health, 0);
        p.addComponent(new MoveZombieComponent(new AttractStrategy()));
        return p;
    }

    private static Plant buildExplodeONut() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.EXPLODE_O_NUT);
        int health = (level >= 2) ? 5000 : 4000;
        int damage = (level >= 3) ? 2000 : 1800;
        Plant p = new Plant(PlantType.EXPLODE_O_NUT, health, damage);
        p.addComponent(new WallNutsComponent() {
            @Override
            public void activatePlantFood(Plant owner) {
                ArmorComponent armor = p.getComponent(ArmorComponent.class);
                if (armor == null) {
                    p.addComponent(new ArmorComponent(4000) {
                        @Override
                        protected void onDestroy(Plant owner) {
                            ExplosivesComponent explosivesComponent = new ExplosivesComponent(InstantTrigger.INSTANCE,
                                    new AreaDamageBehavior(damage, new CircularRange(1)), 0);
                            explosivesComponent.setPostTriggerDelay(0);
                            explosivesComponent.update(owner);
                            explosivesComponent.update(owner);
                        }
                    });
                } else {
                    armor.setArmorHp(4000);
                }
            }

            @Override
            public void onDeath(Plant owner) {
                ExplosivesComponent explosivesComponent = new ExplosivesComponent(InstantTrigger.INSTANCE,
                        new AreaDamageBehavior(damage, new CircularRange(1)), 0);
                explosivesComponent.setPostTriggerDelay(0);
                explosivesComponent.update(owner);
                explosivesComponent.update(owner);
            }
        });
        return p;
    }

    private static Plant buildPumpkin() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.PUMPKIN);
        int health = 4000;
        if (level >= 2) health += 1000;
        if (level >= 4) health += 1500;
        Plant p = new Plant(PlantType.PUMPKIN, health, 0);
        p.addComponent(new WallNutsComponent() {
            @Override
            public void activatePlantFood(Plant owner) {
                ArmorComponent armor = p.getComponent(ArmorComponent.class);
                if (armor == null) {
                    p.addComponent(new ArmorComponent(4000));
                } else {
                    armor.setArmorHp(4000);
                }
            }
        });
        p.addComponent(new PlacementBehaviorComponent(PlantLayer.SHIELD, false, 0, false));
        return p;
    }

    private static Plant buildSunBean() {
        int level = App.getCurrentUser().getUnlockedPlantsLevels().get(PlantType.SUN_BEAN);
        int health = (level >= 3) ? 1000 : 1150;
        int sunSize = (level >= 2) ? 10 : 5;
        Plant p = new Plant(PlantType.SUN_BEAN, health, 0);
        p.addComponent(new WallNutsComponent() {
            @Override
            public void activatePlantFood(Plant owner) {
                ArmorComponent armor = p.getComponent(ArmorComponent.class);
                if (armor == null) {
                    p.addComponent(new ArmorComponent(1000));
                } else {
                    armor.setArmorHp(1000);
                }
            }

            @Override
            public int onTakeDamage(Plant owner, int damageAmount, Zombie attacker) {
                Sun sun = App.getCurrentGame().getSunsPool().acquire();
                sun.reset(owner.getX(), owner.getY(), sunSize, null);
                App.getCurrentGame().addSunToPlayer(sunSize);
                return damageAmount;
            }
        });
        return p;
    }


}
