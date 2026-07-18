package models.miniGame.beghouled;

import models.enums.PlantLayer;
import models.enums.PlantType;
import models.plant.Plant;
import models.plant.PlantFactory;
import models.world.Cell;
import models.world.GameWorld;
import models.world.GridPosition;
import models.world.mechanics.Mechanic;

import java.util.*;

public class BeghouledMechanics implements Mechanic {
    private final List<PlantType> availablePlantTypes;
    private final List<PlantUpgrade> upgrades;
    private final Set<GridPosition> craters = new HashSet<>();
    private final Random random = new Random();

    private int score = 0;
    private final int targetScore;

    public BeghouledMechanics(List<PlantType> availablePlantTypes, List<PlantUpgrade> upgrades, int targetScore) {
        this.availablePlantTypes = availablePlantTypes;
        this.upgrades = upgrades;
        this.targetScore = targetScore;
    }

    @Override
    public void applyMechanic(GameWorld world) {
    }



    public void fillRandomPlants(GameWorld world) {
        for (int r = 0; r < world.getRows(); r++) {
            for (int c = 0; c < world.getCols(); c++) {
                if (craters.contains(new GridPosition(r, c))) continue;
                PlantType randomType = randomPlantType();
                placePlant(world, r, c, randomType);
            }
        }
    }

    private PlantType randomPlantType() {
        return availablePlantTypes.get(random.nextInt(availablePlantTypes.size()));
    }

    private void placePlant(GameWorld world, int row, int col, PlantType type) {
        Cell cell = world.getGrid()[row][col];
        Plant plant = PlantFactory.createPlant(type, cell.getRow(), cell.getCol(), cell);
        cell.setPlant(plant, PlantLayer.MAIN);
        world.getActivePlants().add(plant);
    }



    public String trySwap(GameWorld world, GridPosition a, GridPosition b) {
        if (!areAdjacent(a, b)) return "only neighbors";
        if (craters.contains(a) || craters.contains(b)) return "you cant swap craters";

        swapPlants(world, a, b);

        List<List<GridPosition>> matches = findAllMatches(world);
        if (matches.isEmpty()) {
            swapPlants(world, a, b);
            return "no combination";
        }

        processMatches(world, matches, false);
        return null;
    }

    private boolean areAdjacent(GridPosition a, GridPosition b) {
        int dr = Math.abs(a.row() - b.row());
        int dc = Math.abs(a.col() - b.col());
        return (dr + dc) == 1;
    }

    private void swapPlants(GameWorld world, GridPosition a, GridPosition b) {
        Cell cellA = world.getGrid()[a.row()][a.col()];
        Cell cellB = world.getGrid()[b.row()][b.col()];

        Plant plantA = cellA.getPlant();
        Plant plantB = cellB.getPlant();

        cellA.removePlant();
        cellB.removePlant();

        if (plantB != null) {
            cellA.setPlant(plantB, PlantLayer.MAIN);
            plantB.setX(a.row());
            plantB.setY(a.col());
        }
        if (plantA != null) {
            cellB.setPlant(plantA, PlantLayer.MAIN);
            plantA.setX(b.row());
            plantA.setY(b.col());
        }
    }



    private List<List<GridPosition>> findAllMatches(GameWorld world) {
        List<List<GridPosition>> matches = new ArrayList<>();

        for (int r = 0; r < world.getRows(); r++) {
            int c = 0;
            while (c < world.getCols()) {
                PlantType type = typeAt(world, r, c);
                if (type == null) { c++; continue; }
                int start = c;
                while (c < world.getCols() && typeAt(world, r, c) == type) c++;
                if (c - start >= 3) {
                    List<GridPosition> match = new ArrayList<>();
                    for (int k = start; k < c; k++) match.add(new GridPosition(r, k));
                    matches.add(match);
                }
            }
        }

        for (int c = 0; c < world.getCols(); c++) {
            int r = 0;
            while (r < world.getRows()) {
                PlantType type = typeAt(world, r, c);
                if (type == null) { r++; continue; }
                int start = r;
                while (r < world.getRows() && typeAt(world, r, c) == type) r++;
                if (r - start >= 3) {
                    List<GridPosition> match = new ArrayList<>();
                    for (int k = start; k < r; k++) match.add(new GridPosition(k, c));
                    matches.add(match);
                }
            }
        }

        return matches;
    }

    private PlantType typeAt(GameWorld world, int row, int col) {
        Plant plant = world.getGrid()[row][col].getPlant();
        return (plant == null) ? null : plant.getType();
    }



    private void processMatches(GameWorld world, List<List<GridPosition>> matches, boolean isCascade) {
        Set<GridPosition> toRemove = new HashSet<>();
        int sunUnits = 0;

        for (List<GridPosition> match : matches) {
            toRemove.addAll(match);
            int size = match.size();
            int unitsForThisMatch = (size - 3) + 1;
            if (isCascade) unitsForThisMatch += 1;
            sunUnits += unitsForThisMatch;
        }

        for (GridPosition pos : toRemove) {
            world.getGrid()[pos.row()][pos.col()].findAndRemovePlant();
        }

        world.setSun(world.getSun() + sunUnits * 50);
        score += matches.size();

        applyGravityAndRefill(world);

        List<List<GridPosition>> cascadeMatches = findAllMatches(world);
        if (!cascadeMatches.isEmpty()) {
            processMatches(world, cascadeMatches, true);
        } else if (!hasAnyPossibleMove(world)) {
            resetBoard(world);
        }
    }

    private void applyGravityAndRefill(GameWorld world) {
        for (int c = 0; c < world.getCols(); c++) {
            List<Plant> column = new ArrayList<>();
            for (int r = world.getRows() - 1; r >= 0; r--) {
                if (craters.contains(new GridPosition(r, c))) continue;
                Plant p = world.getGrid()[r][c].getPlant();
                if (p != null) column.add(p);
            }

            int idx = 0;
            for (int r = world.getRows() - 1; r >= 0; r--) {
                if (craters.contains(new GridPosition(r, c))) continue;
                Cell cell = world.getGrid()[r][c];
                cell.removePlant();
                if (idx < column.size()) {
                    Plant p = column.get(idx++);
                    cell.setPlant(p, PlantLayer.MAIN);
                    p.setX(r);
                    p.setY(c);
                } else {
                    placePlant(world, r, c, randomPlantType());
                }
            }
        }
    }

    private void resetBoard(GameWorld world) {
        for (int r = 0; r < world.getRows(); r++) {
            for (int c = 0; c < world.getCols(); c++) {
                if (craters.contains(new GridPosition(r, c))) continue;
                world.getGrid()[r][c].findAndRemovePlant();
            }
        }
        fillRandomPlants(world);
        System.out.println("No more moves possible — board reset!");
    }

    private boolean hasAnyPossibleMove(GameWorld world) {
        for (int r = 0; r < world.getRows(); r++) {
            for (int c = 0; c < world.getCols(); c++) {
                GridPosition current = new GridPosition(r, c);
                if (craters.contains(current)) continue;

                if (c + 1 < world.getCols() && !craters.contains(new GridPosition(r, c + 1))
                        && wouldCreateMatch(world, current, new GridPosition(r, c + 1))) {
                    return true;
                }
                if (r + 1 < world.getRows() && !craters.contains(new GridPosition(r + 1, c))
                        && wouldCreateMatch(world, current, new GridPosition(r + 1, c))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean wouldCreateMatch(GameWorld world, GridPosition a, GridPosition b) {
        swapPlants(world, a, b);
        boolean result = !findAllMatches(world).isEmpty();
        swapPlants(world, a, b);
        return result;
    }



    public String upgradePlant(GameWorld world, PlantType from) {
        PlantUpgrade upgrade = upgrades.stream()
                .filter(u -> u.getFrom() == from)
                .findFirst()
                .orElse(null);

        if (upgrade == null) return "no upgrade for this plant";
        if (world.getSun() < upgrade.getCost()) return "not enough sun";

        int count = 0;
        for (Cell[] row : world.getGrid()) {
            for (Cell cell : row) {
                Plant plant = cell.getPlant();
                if (plant != null && plant.getType() == from) {
                    Plant upgraded = PlantFactory.createPlant(upgrade.getTo(), cell.getRow(), cell.getCol(), cell);
                    world.getActivePlants().remove(plant);
                    cell.removePlant();
                    cell.setPlant(upgraded, PlantLayer.MAIN);
                    world.getActivePlants().add(upgraded);
                    count++;
                }
            }
        }

        if (count == 0) return "u dont have this type of plant";

        world.setSun(world.getSun() - upgrade.getCost());
        return null;
    }



    public void createCrater(GameWorld world, int row, int col) {
        GridPosition pos = new GridPosition(row, col);
        craters.add(pos);
        Cell cell = world.getGrid()[row][col];
        cell.findAndRemovePlant();
        cell.setPlantable(false);
    }



    public int getScore() { return score; }
    public int getTargetScore() { return targetScore; }
    public Set<GridPosition> getCraters() { return craters; }
}