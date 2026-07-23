package models.world.mechanics;

import models.plant.card.PlantCard;
import models.world.GameWorld;

import java.util.List;
import java.util.Random;

public class ConveyorMechanic implements Mechanic{
    private long lastSpawnTick = 0;
    private int spawnInterval = 50;
    private Random random = new Random();
    private List<PlantCard> availablePlants;

    public ConveyorMechanic(List<PlantCard> availablePlants){
        this.availablePlants = availablePlants;
    }

    private PlantCard getRandomUnlokedPlant(){
        return availablePlants.get(random.nextInt(availablePlants.size()));
    }

    @Override
    public void applyMechanic(GameWorld world) {
        long now = world.getCurrentTick();

        if(now - lastSpawnTick >= spawnInterval){
            List<PlantCard> conveyor = world.getConveyorBelt();
            if (conveyor != null) {
                PlantCard newCard = getRandomUnlokedPlant();
                conveyor.add(newCard);
                lastSpawnTick = now;

                System.out.println(" 🛒 [Conveyor Belt] New card added: " + newCard.getType());
            }
        }


    }
}
