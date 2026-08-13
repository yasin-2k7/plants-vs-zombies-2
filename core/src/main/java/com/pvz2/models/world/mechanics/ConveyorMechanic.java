package com.pvz2.models.world.mechanics;

import com.pvz2.models.plant.card.PlantCard;
import com.pvz2.models.world.GameWorld;

import java.util.List;
import java.util.Random;

public class ConveyorMechanic implements Mechanic {
    private float lastSpawnTime = 0f;
    private float spawnInterval = 5.0f;
    private Random random = new Random();
    private List<PlantCard> availablePlants;

    public ConveyorMechanic(List<PlantCard> availablePlants) {
        this.availablePlants = availablePlants;
    }

    private PlantCard getRandomUnlokedPlant() {
        return availablePlants.get(random.nextInt(availablePlants.size()));
    }

    @Override
    public void applyMechanic(GameWorld world) {
        float now = world.getElapsedTime();
        now = now + 50;
        if (now - lastSpawnTime >= spawnInterval) {
            List<PlantCard> conveyor = world.getConveyorBelt();
            if (conveyor != null) {
                PlantCard newCard = getRandomUnlokedPlant();
                conveyor.add(newCard);
                lastSpawnTime = now;

                System.out.println(" 🛒 [Conveyor Belt] New card added: " + newCard.getType());
            }
        }


    }
}
