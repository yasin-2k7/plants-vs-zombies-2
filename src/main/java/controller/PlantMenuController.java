package controller;

import models.core.App;
import models.core.User;
import models.enums.PlantType;
import models.plant.card.PlantCard;
import models.plant.card.PlantCardFactory;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;

import java.util.*;

public class PlantMenuController implements MenuController {
    private Set<PlantType> selectedPlants = new HashSet<>();
    private int maxSlots = 8;
    private Map<PlantType, Boolean> boosts = new HashMap<>();
    private PlantType imitatorTarget = null;

    @Override
    public void changeMenu() {
    }

    public void reset(){
        maxSlots = 8-App.getCurrentGame().getPlantLists().size();
        numberOfLockedPlantsInList = App.getCurrentGame().getPlantLists().size();
        this.imitatorTarget = null;
    }

    public String showAllPlants() {
        StringBuilder sb = new StringBuilder("All plants:\n");
        for (PlantType type : PlantType.values()) {
            sb.append(" - ").append(type.name()).append("\n");
        }
        return sb.toString();
    }

    public String showAvailablePlants() {
        User user = App.getCurrentUser();
        if (user == null) return "Error: No user logged in.";

        StringBuilder sb = new StringBuilder("Available plants (unlocked):\n");
        for (PlantType type : user.getUnlockedPlantsLevels().keySet()) {
            sb.append(" - ").append(type.name()).append("\n");
        }
        return sb.toString();
    }

    public String addPlant(String typeName) {
        User user = App.getCurrentUser();
        if (user == null) return "Error: No user logged in.";

        PlantType type;
        try {
            type = PlantType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error: Invalid plant type.";
        }

        if (type == PlantType.IMITATOR) {
            return "Error: Please specify target plant for Imitator (e.g., add plant imitator peashooter).";
        }

        if (!user.getUnlockedPlantsLevels().containsKey(type)) {
            return "Error: Plant is locked.";
        }
        if (selectedPlants.size() >= maxSlots) {
            return "Error: Selection is full (max " + maxSlots + " plants).";
        }

        boolean gameHasThisCard = false;
        for (PlantCard card : App.getCurrentGame().getPlantLists()){
            if (card.getType() == type) {
                gameHasThisCard = true;
                break;
            }
        }

        if (selectedPlants.contains(type) || gameHasThisCard) {
            return "Error: Plant already selected.";
        }

        selectedPlants.add(type);
        return "Plant " + type.name() + " added to selection.";
    }

    public String addPlant(String typeName, String targetTypeName) {
        User user = App.getCurrentUser();
        if (user == null) return "Error: No user logged in.";

        PlantType type;
        PlantType targetType;
        try {
            type = PlantType.valueOf(typeName.toUpperCase());
            targetType = PlantType.valueOf(targetTypeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error: Invalid plant type.";
        }

        if (type != PlantType.IMITATOR) {
            return "Error: Target plant can only be specified for IMITATOR.";
        }
        if (targetType == PlantType.IMITATOR) {
            return "Error: Imitator cannot imitate itself!";
        }
        if (!user.getUnlockedPlantsLevels().containsKey(targetType)) {
            return "Error: Target plant is locked.";
        }
        if (selectedPlants.size() >= maxSlots) {
            return "Error: Selection is full (max " + maxSlots + " plants).";
        }

        if (selectedPlants.contains(type)) {
            return "Error: Imitator is already selected.";
        }

        selectedPlants.add(type);
        this.imitatorTarget = targetType;

        return "Plant IMITATOR added to selection as " + targetType.name() + ".";
    }

    public String removePlant(String typeName) {
        PlantType type;
        try {
            type = PlantType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error: Invalid plant type.";
        }

        boolean gameHasThisCard = false;
        for (PlantCard card : App.getCurrentGame().getPlantLists()){
            if (card.getType() == type) {
                gameHasThisCard = true;
                break;
            }
        }

        if (!selectedPlants.contains(type)) {
            if (gameHasThisCard){
                return "Error: This plant can't be removed!";
            }
            return "Error: Plant is not selected.";
        }


        selectedPlants.remove(type);
        boosts.remove(type);

        if (type == PlantType.IMITATOR) {
            this.imitatorTarget = null;
        }
        return "Plant " + type.name() + " removed from selection.";
    }

    public String boostPlant(String typeName) {
        User user = App.getCurrentUser();
        if (user == null) return "Error: No user logged in.";

        PlantType type;
        try {
            type = PlantType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Error: Invalid plant type.";
        }

        if (!user.getUnlockedPlantsLevels().containsKey(type)) {
            return "Error: Plant is locked.";
        }

        boolean gameHasThisCard = false;
        for (PlantCard card : App.getCurrentGame().getPlantLists()){
            if (card.getType() == type) {
                gameHasThisCard = true;
                break;
            }
        }
        if (!selectedPlants.contains(type) && !gameHasThisCard) {
            return "Error: Plant is not selected, cannot boost.";
        }
        if (boosts.getOrDefault(type, false)) {
            return "Error: Plant is already boosted.";
        }

        if (!user.spendGems(2)) {
            return "Error: Not enough gems (need 2).";
        }

        boosts.put(type, true);
        return "Plant " + type.name() + " boosted for this level (2 gems spent).";
    }

    public String startGame() {
        User user = App.getCurrentUser();
        if (user == null) return "Error: No user logged in.";

        if (selectedPlants.size() < maxSlots) {
            return "Error: Please select " + (maxSlots-selectedPlants.size()) + " more plants";
        }

        for (PlantType type : selectedPlants) {
            if (type == PlantType.IMITATOR) {
                int targetLevel = user.getUnlockedPlantsLevels().getOrDefault(imitatorTarget, 1);
                int imitatorLevel = user.getUnlockedPlantsLevels().getOrDefault(PlantType.IMITATOR, 1);

                App.getCurrentGame().getPlantLists().add(
                        PlantCardFactory.createImitatorCard(imitatorTarget, targetLevel, imitatorLevel)
                );
            } else {
                int plantLevel = user.getUnlockedPlantsLevels().getOrDefault(type, 1);
                App.getCurrentGame().getPlantLists().add(
                        PlantCardFactory.createCard(type, plantLevel)
                );
            }
        }

        AppView.setCurrentScreen(GameMenuView.getInstance());
        return "Starting game with selected plants...";
    }

    public Set<PlantType> getSelectedPlants() {
        return Collections.unmodifiableSet(selectedPlants);
    }

    public boolean isBoosted(PlantType type) {
        return boosts.getOrDefault(type, false);
    }

    @Override
    public void exitMenu() {
    }

    public void showCurrentMenu(){
        GameMenuView.getInstance().showResult("Current menu: plant menu");
    }
}