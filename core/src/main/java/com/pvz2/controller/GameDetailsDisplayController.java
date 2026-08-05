package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.enums.PlantLayer;
import com.pvz2.models.lawnMower.LawnMower;
import com.pvz2.models.lawnMower.LawnMowerManager;
import com.pvz2.models.miniGame.IZombie.Brain;
import com.pvz2.models.miniGame.IZombie.IZombieLevel;
import com.pvz2.models.miniGame.vaseBreaker.SeedPacket;
import com.pvz2.models.miniGame.vaseBreaker.Vase;
import com.pvz2.models.miniGame.vaseBreaker.VaseBreakerLevel;
import com.pvz2.models.plant.Plant;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.world.Cell;
import com.pvz2.models.world.mechanics.NormalMechanic;
import com.pvz2.models.world.obstacles.BarrelObstacle;
import com.pvz2.models.world.obstacles.Grave;
import com.pvz2.models.world.obstacles.Obstacle;
import com.pvz2.models.world.obstacles.OctopusObstacle;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.wave.WaveManager;
import com.pvz2.models.zombie.zombiesType.ArmoredZombie;


import java.util.List;

//needs edit
public class GameDetailsDisplayController {
    public static void showSunAmount() {
//        GameMenuView.getInstance().showResult("current sun amount: " + App.getCurrentGame().getSun());
    }

    public static void showPlantFoodsCount() {
//        GameMenuView.getInstance().showResult("plant foods count: " + App.getCurrentUser().getPlantFoods());
    }

    private static String getCellDetails(Cell cell, int x, int y) {
        boolean isVaseBreaker = App.getCurrentGame() instanceof VaseBreakerLevel;
        VaseBreakerLevel vbLevel = isVaseBreaker ? (VaseBreakerLevel) App.getCurrentGame() : null;
        String terrainSymbol = cell.getTerrain().getTerminalSymbol();
        if (cell.getSlippingDir() == 1) {
            terrainSymbol = "🧊👇";
        } else if (cell.getSlippingDir() == -1) {
            terrainSymbol = "🧊👆";
        }
        if (isVaseBreaker) {
            Vase vase = vbLevel.getVaseAt(y, x);
            SeedPacket seed = vbLevel.getSeedPacketAt(y, x);
            if (vase != null && !vase.isBroken()) {
                terrainSymbol = vase.getType().getSymbol();
            } else if (seed != null) {
                terrainSymbol = "🌱📦";
            }
        }
        if (cell.hasObstacle()) {
            Obstacle obs = cell.getObstacle();
            if (obs instanceof Grave grave) {
                switch (grave.getType()) {
                    case SUN -> terrainSymbol = "🪦☀";
                    case PLANT_FOOD -> terrainSymbol = "🪦⚡";
                    default -> terrainSymbol = "🪦";
                }
            } else if (obs instanceof OctopusObstacle) {
                terrainSymbol = "🐙";
            } else if (obs instanceof BarrelObstacle) {
                terrainSymbol = "🛢️";
            } else {
                terrainSymbol = "🪨";
            }
        }
        String plantSymbol = "    ";
        if (!cell.isEmpty()) {
            Plant plant = cell.getPlant();
            if (plant.isCat()) {
                plantSymbol = "🐱 ";
            } else {
                plantSymbol = plant.getType().getSymbol();
            }
        }
        String zombieString = "       ";
        List<Zombie> zombiesInCell = Cell.getZombiesInCell(cell);
        if (!zombiesInCell.isEmpty()) {
            Zombie firstZombie = zombiesInCell.getFirst();
            zombieString = String.format("Z(%.1f)", firstZombie.getX());
        }
        return String.format("[ %s | %-4s | %-7s ] | ", terrainSymbol, plantSymbol.trim(), zombieString.trim());
    }

    public static void showMap() {
        int currentWaveNum = 1;
        int totalWaves = 1;
        NormalMechanic normal = App.getCurrentGame().getMechanic(NormalMechanic.class);
        if (normal != null && normal.getWaveManager() != null) {
            WaveManager wm = normal.getWaveManager();
            if (wm.getCurrentWave() != null) {
                currentWaveNum = wm.getCurrentWave().getWaveNumber();
            } else {
                currentWaveNum = wm.getCurrentWaveIndex() + 1;
            }
            totalWaves = wm.getTotalWavesCount();
        }
//        GameMenuView.getInstance().showResult(
//                "==================================================================================================");
        String title = String.format(" WAVE: %d/%d  |  SUN: %d ☀️  |  PLANT FOOD: %d ⚡  |  STATUS: %s 🎮",
                currentWaveNum, totalWaves, App.getCurrentGame().getSun(),
                App.getCurrentGame().getPlantFoods(), App.getCurrentGame().getState());
//        GameMenuView.getInstance().showResult(title);
//        GameMenuView.getInstance().showResult(
//                "==================================================================================================");
        boolean isIZombie = App.getCurrentGame() instanceof IZombieLevel;
        IZombieLevel izLevel = isIZombie ? (IZombieLevel) App.getCurrentGame() : null;
        for (int y = 0; y < App.getCurrentGame().getGrid().length; y++) {
            StringBuilder rowBuilder = new StringBuilder();
            String mowerSymbol = "    "; // پیش‌فرض خالی
            if (isIZombie) {
                Brain brain = izLevel.getBrainAtRow(y);
                if (brain != null && !brain.isEaten()) {
                    mowerSymbol = "[🧠]"; // اگر مغز موجود بود
                }
            } else {
                LawnMowerManager lmManager = App.getCurrentGame().getLawnMowerManager();
                if (lmManager != null && lmManager.isEnabled() && y < lmManager.getMowers().size()) {
                    LawnMower mower = lmManager.getMowers().get(y);
                    mowerSymbol = mower.isAlive() ? "[🚜]" : "[❌]";
                }
            }
            rowBuilder.append(String.format("Row %d %s | ", y + 1, mowerSymbol));
            for (int x = 0; x < App.getCurrentGame().getGrid()[0].length; x++) {
                Cell cell = App.getCurrentGame().getGrid()[y][x];
                rowBuilder.append(getCellDetails(cell, x, y));
            }
//            GameMenuView.getInstance().showResult(rowBuilder.toString());
        }
//        GameMenuView.getInstance().showResult(
//                "==================================================================================================");
    }

    public static void showPlantsStatus() {
        List<PlantCard> cards = App.getCurrentGame().isConveyorMode()?
                App.getCurrentGame().getConveyorBelt() : App.getCurrentGame().getPlantLists();
        for (PlantCard card : cards) {
            String ticksRemaining = card.isReady() ?
                    "" : " | ticks remaining: " + (card.getMaxCooldownTicks() - card.getCurrentCooldownTicks());
//            GameMenuView.getInstance().showResult(card.getType().name() +
//                    " | Cost: " + card.getSunCost() + " | is ready: " + card.isReady() + ticksRemaining);
        }
    }

    public static void showTileStatus(float x, float y) {
        Cell selectedCell = null;
        for (Cell[] cells : App.getCurrentGame().getGrid()) {
            if (!(cells[0].getY() + App.getCellHeight() / 2 > y && cells[0].getY() - App.getCellHeight() / 2 < y))
                continue;
            for (Cell cell : cells) {
                if ((cell.getX() + App.getCellWidth() / 2 > x && cell.getX() - App.getCellWidth() / 2 < x)) {
                    selectedCell = cell;
                    break;
                }
            }
        }
        if (selectedCell == null) {
//            GameMenuView.getInstance().showResult("there is no tile in that place!");
            return;
        }
//        GameMenuView.getInstance().showResult("plants in this tile:");
        for (PlantLayer layer : PlantLayer.values()) {
            Plant p = selectedCell.getPlant(layer);
            if (p != null) {
//                GameMenuView.getInstance().showResult(p.getType().name() +
//                        " | health: " + p.getHealth() + " | damage: " + p.getDamage());
//                if (p.isFreeze()) GameMenuView.getInstance().showResult("ICE health: " + p.getIceHealth());
            }
        }
//        GameMenuView.getInstance().showResult("zombies in this tile:");
        for (Zombie zombie : Cell.getZombiesInCells(List.of(selectedCell))) {
//            GameMenuView.getInstance().showResult(
//                    App.getArmoredZombieName(zombie.getSpecificName()) +
//                            " | health: " + zombie.getHealth() + " | damage: " + zombie.getDamage());
        }
    }

    public static void zombieInfo() {
        for (Zombie zombie : App.getCurrentGame().getActiveZombies()) {
//            GameMenuView.getInstance().showResult(App.getArmoredZombieName(zombie.getSpecificName()) + ":");
//            GameMenuView.getInstance().showResult("    position: (" + zombie.getX() + ", " + zombie.getY() + ")");
//            GameMenuView.getInstance().showResult("    health: " + zombie.getHealth());
            if (zombie instanceof ArmoredZombie armoredZombie) {
//                GameMenuView.getInstance().showResult("    armor health: " + handleArmor(armoredZombie));
            } else {
//                GameMenuView.getInstance().showResult("    armor health: none");
            }
//            GameMenuView.getInstance().showResult("    effects:");
            if (zombie.getDisabledTicksRemaining() > 0)
//                GameMenuView.getInstance().showResult("        stunned " + zombie.getDisabledTicksRemaining());
            if (zombie.getFreezedTicksRemaining() > 0)
//                GameMenuView.getInstance().showResult("        frozen " + zombie.getFreezedTicksRemaining());
            if (zombie.getSlowTicksRemaining() > 0)
//                GameMenuView.getInstance().showResult("        slowed " + zombie.getSlowTicksRemaining());
            if (zombie.getIceHealth() > 0)
//                GameMenuView.getInstance().showResult("        ice health " + zombie.getIceHealth());
            if (zombie.getOnPoisonTicksRemaining() > 0){}
//                GameMenuView.getInstance().showResult("        poisoned " + zombie.getOnPoisonTicksRemaining());
//            GameMenuView.getInstance().showResult("");
        }
    }

    private static String handleArmor(ArmoredZombie armoredZombie) {
        if (armoredZombie.getArmorHealth() <= 0) {
            return "0 (broken)";
        }
        if (armoredZombie.getSpecificName().equalsIgnoreCase("ZombieDarkArmor3")) {
            if (armoredZombie.getArmorHealth() > 1600) {
                return "crown: " + (armoredZombie.getArmorHealth() - 1600) + ", shoulderArmor: 1600";
            } else {
                return "shoulderArmor: " + armoredZombie.getArmorHealth();
            }
        }
        String type = armoredZombie.getArmorTypes().isEmpty() ? "unknown" : armoredZombie.getArmorTypes().get(0);
        return type + ": " + armoredZombie.getArmorHealth();
    }
}
