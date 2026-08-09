package com.pvz2.view;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.enums.PlantFamily;
import com.pvz2.models.enums.PlantType;
import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.plant.card.PlantCardFactory;

import java.util.HashMap;
import java.util.function.Consumer;

public class PlantsTable extends Table {
    private int column;
    private boolean upgradeBar, canFilter;
    private static HashMap<PlantType, String> plantsMap;
    private static HashMap<PlantFamily, String> plantsFamilyMap;
    private int cardWidth, cardHeight;
    private Consumer<PlantCardView> cardClickMethod;

    public PlantsTable(int column, int pad, boolean upgradeBar, boolean canFilter, int cardWidth,
                       int cardHeight, Consumer<PlantCardView> cardClickMethod) {
        this.column = column;
        this.cardClickMethod = cardClickMethod;
        this.defaults().pad(pad);
        this.upgradeBar = upgradeBar;
        this.canFilter = canFilter;
        this.cardWidth = cardWidth;
        this.cardHeight = cardHeight;
        build();
    }

    private void build(){
        this.clear();
        User user = App.getCurrentUser();
        if (user == null) return;
        int i = 1;
        for (PlantType plantType : PlantType.values()){
            if (plantType == PlantType.GIANT_WALLNUT || plantType == PlantType.MARIGOLD) continue;
            Table cardTable = new Table();
            int cardLevel = user.getUnlockedPlantsLevels().getOrDefault(plantType, 0);
            PlantCard card = PlantCardFactory.createCard(plantType, Math.max(1, cardLevel));
            boolean lock = cardLevel == 0;
            PlantCardView plantCardView = new PlantCardView(!lock, user.hasBoost(plantType), lock
                , cardLevel, card.getSunCost(), plantType);
            cardTable.add(plantCardView).size(cardWidth, cardHeight);
            plantCardView.setClickMethod(cardClickMethod);
            this.add(cardTable);
            i++;
            if (i > column){
                i = 1;
                this.row();
            }
        }
    }

    public static HashMap<PlantType, String> getPlantsMap(){
        if (plantsMap == null){
            plantsMap = new HashMap<>();
            for (PlantType type : PlantType.values()){
                String newName = type.name().toUpperCase().replaceAll("_", "");
                if (type == PlantType.CHERRY_BOMB) newName = "CHERRY_BOMB";
                String address = "IMAGE_UI_PACKETS_" + newName;
                plantsMap.put(type, address);
            }
        }
        return plantsMap;
    }

    public static HashMap<PlantFamily, String> getPlantsFamilyMap() {
        if (plantsFamilyMap == null){
            plantsFamilyMap = new HashMap<>();
            plantsFamilyMap.put(PlantFamily.SUN_PRODUCER, "IMAGE_UI_PACKETS_MINTFAM_SUN");
            plantsFamilyMap.put(PlantFamily.SHOOTER, "IMAGE_UI_PACKETS_MINTFAM_PEASHOOTER");
            plantsFamilyMap.put(PlantFamily.LOBBER, "IMAGE_UI_PACKETS_MINTFAM_LOBBER");
            plantsFamilyMap.put(PlantFamily.EXPLOSIVE, "IMAGE_UI_PACKETS_MINTFAM_EXPLOSIVE");
            plantsFamilyMap.put(PlantFamily.MELEE, "IMAGE_UI_PACKETS_MINTFAM_MELEE");
            plantsFamilyMap.put(PlantFamily.WALL_NUTS, "IMAGE_UI_PACKETS_MINTFAM_DEFENSE");
            plantsFamilyMap.put(PlantFamily.STRIKE_THROUGH, "IMAGE_UI_PACKETS_MINTFAM_SHARP");
            plantsFamilyMap.put(PlantFamily.MODIFIER, "IMAGE_UI_PACKETS_MINTFAM_MAGIC");
            plantsFamilyMap.put(PlantFamily.HOMING, "IMAGE_UI_PACKETS_MINTFAM_MAGIC");
        }
        return plantsFamilyMap;
    }
}
