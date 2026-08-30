package com.pvz2.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pvz2.models.core.LocalDateAdapter;
import com.pvz2.models.plant.GameComponent;
import com.pvz2.models.world.cellTerrains.CellTerrain;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.world.loseCondition.LoseCondition;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.winCondition.WinCondition;
import com.pvz2.models.zombie.Zombie;
import com.pvz2.models.zombie.state.ZombieState;

import java.time.LocalDate;

public class NetworkGson {
    public static final Gson INSTANCE = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(Zombie.class, new PolymorphicTypeAdapter<Zombie>())
        .registerTypeAdapter(ZombieState.class, new PolymorphicTypeAdapter<ZombieState>())
        .registerTypeAdapter(GameComponent.class, new PolymorphicTypeAdapter<GameComponent>())
        .registerTypeAdapter(CellTerrain.class, new PolymorphicTypeAdapter<CellTerrain>())
        .registerTypeAdapter(LevelSetup.class, new PolymorphicTypeAdapter<LevelSetup>())
        .registerTypeAdapter(WinCondition.class, new PolymorphicTypeAdapter<WinCondition>())
        .registerTypeAdapter(LoseCondition.class, new PolymorphicTypeAdapter<LoseCondition>())
        .registerTypeAdapter(Mechanic.class, new PolymorphicTypeAdapter<Mechanic>())
        // setExclusionStrategies (not addSerializationExclusionStrategy) applies to BOTH
        // directions — serializing a field but skipping it on the way back in is exactly
        // what caused "Interfaces can't be instantiated" for the Supplier field: Gson wrote
        // something for it server-side, then tried to construct a new Supplier from that on
        // the client and can't, since it's an interface. Symmetric exclusion avoids that class
        // of bug entirely instead of only patching the one field that happened to crash first.
        .setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return isUnwireable(f.getDeclaredClass());
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return isUnwireable(clazz);
            }

            private boolean isUnwireable(Class<?> clazz) {
                if (clazz == java.util.Random.class) return true;
                // Supplier, Consumer, Function, Predicate, BiConsumer, etc. — a lambda/callback
                // has no meaningful wire representation, and being an interface, can never be
                // reconstructed on the way back in even if it did.
                Package pkg = clazz.getPackage();
                return pkg != null && pkg.getName().equals("java.util.function");
            }
        })
        .create();
}
