package models.greenhouse;

import models.core.App;
import models.core.User;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.factory.PlantFactory;
import models.world.Cell;
import models.world.cellTerrains.LandTerrain;

import java.util.List;
import java.util.Random;

public class GreenHouse {
    private static final int ROWS = 4;
    private static final int COLS = 5;
    private Pot[][] pots;
    private Random random;

    public GreenHouse() {
        this.pots = new Pot[ROWS][COLS];
        this.random = new Random();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                boolean isLocked = (r >= 1 && r <= 3);
                pots[r][c] = new Pot(c + 1, r + 1, isLocked);
            }
        }
    }

    public String plantPot(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) {
            return "Error: Invalid coordinates.";
        }
        Pot pot = pots[y - 1][x - 1];
        if (pot.isLocked()) {
            return "Error: This pot is locked. Unlock it first via shop.";
        }
        if (!pot.isEmpty()) {
            return "Error: This pot is not empty.";
        }

        User user = App.getCurrentUser();
        if (user == null) {
            return "Error: No user logged in.";
        }

        PlantType chosenType;
        if (random.nextDouble() < 0.5) {
            chosenType = PlantType.MARIGOLD;
        } else {
            List<PlantType> unlockedWithPlantFood = user.getUnlockedPlantTypesWithPlantFood();
            if (unlockedWithPlantFood.isEmpty()) {
                return "Error: No unlocked plant with Plant Food ability available.";
            }
            chosenType = unlockedWithPlantFood.get(random.nextInt(unlockedWithPlantFood.size()));
        }


        PlantFactory factory = App.getFactory();
        Plant newPlant = factory.createPlant(chosenType, x, y, new Cell(1, 1, new LandTerrain()));
        if (newPlant == null) {
            return "Error: Could not create plant.";
        }

        pot.plant(newPlant, chosenType);
        return "Planted " + chosenType.name() + " in pot (" + x + ", " + y + ").";
    }

    public String collect(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) {
            return "Error: Invalid coordinates.";
        }
        Pot pot = pots[y - 1][x - 1];
        if (pot.isLocked()) {
            return "Error: This pot is locked.";
        }
        if (pot.isEmpty()) {
            return "Error: This pot is empty.";
        }
        if (!pot.isReady()) {
            long remaining = pot.getRemainingHours();
            return "Error: Plant is not ready yet. " + remaining + " hours remaining.";
        }

        User user = App.getCurrentUser();
        if (user == null) {
            return "Error: No user logged in.";
        }

        String result;
        PlantType type = pot.getPlantType();
        if (type == PlantType.MARIGOLD) {
            user.addCoins(500);
            result = "Collected Marigold! +500 coins total.";
        } else {
            if (!user.hasBoost(type)) {
                user.addBoost(type);
                result = "Collected " + type.name() + "! Boost added.";
            } else {
                result = "Collected " + type.name() + ". Already have boost.";
            }
        }
        pot.clear();
        return result;
    }

    public String grow(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) {
            return "Error: Invalid coordinates.";
        }
        Pot pot = pots[y - 1][x - 1];
        if (pot.isLocked()) {
            return "Error: This pot is locked.";
        }
        if (pot.isEmpty()) {
            return "Error: This pot is empty.";
        }
        if (pot.isReady()) {
            return "Error: Plant is already ready for harvest.";
        }

        User user = App.getCurrentUser();
        if (user == null) {
            return "Error: No user logged in.";
        }

        long remainingHours = pot.getRemainingHours();
        if (remainingHours <= 0) {
            pot.setReady(true);
            return "Plant is already ready.";
        }

        int cost = (int) Math.ceil(remainingHours);
        if (!user.spendGems(cost)) {
            return "Error: Not enough gems. Need " + cost + " gems.";
        }

        pot.setReady(true);
        return "Growth accelerated! Plant is now ready for harvest. Spent " + cost + " gems.";
    }

    public String unlockFirstLockedPot() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (pots[r][c].isLocked()) {
                    pots[r][c].setLocked(false);
                    return "Pot (" + (c + 1) + ", " + (r + 1) + ") unlocked successfully!";
                }
            }
        }
        return "Error: All pots are already unlocked!";
    }

    public Pot getPot(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) return null;
        return pots[y - 1][x - 1];
    }
}