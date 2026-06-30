package models.greenhouse;

import models.core.App;
import models.core.User;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.PlantFactory;

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

    public void showGreenhouse() {
        System.out.println("===== Greenhouse =====");
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Pot pot = pots[r][c];
                String status;
                if (pot.isLocked()) {
                    status = "[LOCKED]";
                } else if (pot.isEmpty()) {
                    status = "[EMPTY ]";
                } else if (pot.isReady()) {
                    status = "[READY  ] (" + pot.getPlantType().name() + ")";
                } else {
                    long remaining = pot.getRemainingHours();
                    status = "[GROWING] (" + pot.getPlantType().name() + " - " + remaining + "h left)";
                }
                System.out.print(status + " ");
            }
            System.out.println();
        }
        System.out.println("=========================");
    }

    public void plantPot(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) {
            System.out.println("Error: Invalid coordinates.");
            return;
        }
        Pot pot = pots[y - 1][x - 1];
        if (pot.isLocked()) {
            System.out.println("Error: This pot is locked. Unlock it first via shop.");
            return;
        }
        if (!pot.isEmpty()) {
            System.out.println("Error: This pot is not empty.");
            return;
        }

        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        PlantType chosenType;
        if (random.nextDouble() < 0.5) {
            chosenType = PlantType.MARIGOLD;
        } else {
            List<PlantType> unlockedWithPlantFood = user.getUnlockedPlantTypesWithPlantFood();
            if (unlockedWithPlantFood.isEmpty()) {
                System.out.println("Error: No unlocked plant with Plant Food ability available.");
                return;
            }
            chosenType = unlockedWithPlantFood.get(random.nextInt(unlockedWithPlantFood.size()));
        }

        PlantFactory factory = App.getFactory();
        Plant newPlant = factory.createPlant(chosenType, x, y);
        if (newPlant == null) {
            System.out.println("Error: Could not create plant.");
            return;
        }

        pot.plant(newPlant, chosenType);
        System.out.println("Planted " + chosenType.name() + " in pot (" + x + ", " + y + ").");
    }

    public void collect(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) {
            System.out.println("Error: Invalid coordinates.");
            return;
        }
        Pot pot = pots[y - 1][x - 1];
        if (pot.isLocked()) {
            System.out.println("Error: This pot is locked.");
            return;
        }
        if (pot.isEmpty()) {
            System.out.println("Error: This pot is empty.");
            return;
        }
        if (!pot.isReady()) {
            long remaining = pot.getRemainingHours();
            System.out.println("Error: Plant is not ready yet. " + remaining + " hours remaining.");
            return;
        }

        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        user.addCoins(100);

        PlantType type = pot.getPlantType();
        if (type == PlantType.MARIGOLD) {
            user.addCoins(400); // مجموعا 500
            System.out.println("Collected Marigold! +500 coins total.");
        } else {
            if (!user.hasBoost(type)) {
                user.addBoost(type);
                System.out.println("Collected " + type.name() + "! Boost added.");
            } else {
                System.out.println("Collected " + type.name() + ". Already have boost.");
            }
        }
        pot.clear();
    }

    public void grow(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) {
            System.out.println("Error: Invalid coordinates.");
            return;
        }
        Pot pot = pots[y - 1][x - 1];
        if (pot.isLocked()) {
            System.out.println("Error: This pot is locked.");
            return;
        }
        if (pot.isEmpty()) {
            System.out.println("Error: This pot is empty.");
            return;
        }
        if (pot.isReady()) {
            System.out.println("Error: Plant is already ready for harvest.");
            return;
        }

        User user = App.getCurrentUser();
        if (user == null) {
            System.out.println("Error: No user logged in.");
            return;
        }

        long remainingHours = pot.getRemainingHours();
        if (remainingHours <= 0) {
            pot.setReady(true);
            System.out.println("Plant is already ready.");
            return;
        }

        int cost = (int) Math.ceil(remainingHours); // سقف ساعت‌ها
        if (!user.spendGems(cost)) {
            System.out.println("Error: Not enough gems. Need " + cost + " gems.");
            return;
        }

        pot.setReady(true);
        System.out.println("Growth accelerated! Plant is now ready for harvest. Spent " + cost + " gems.");
    }

    void unlockPot(int x, int y) { //توی فروشکاه
        if (x < 1 || x > COLS || y < 1 || y > ROWS) {
            System.out.println("Error: Invalid coordinates.");
            return;
        }
        Pot pot = pots[y - 1][x - 1];
        if (!pot.isLocked()) {
            System.out.println("Error: This pot is already unlocked.");
            return;
        }
        pot.setLocked(false);
        System.out.println("Pot (" + x + ", " + y + ") unlocked.");
    }

    public Pot getPot(int x, int y) {
        if (x < 1 || x > COLS || y < 1 || y > ROWS) return null;
        return pots[y - 1][x - 1];
    }

    public boolean unlockFirstLockedPot() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (pots[r][c].isLocked()) {
                    pots[r][c].setLocked(false);
                    return true;
                }
            }
        }
        System.out.println("All pots are already unlocked!");
        return false;
    }

}