package com.pvz2.models.miniGame.vaseBreaker;

import com.pvz2.models.enums.PlantType;
import com.pvz2.models.world.GameWorld;
import com.pvz2.models.world.levelSetup.LevelSetup;
import com.pvz2.models.world.loseCondition.LoseCondition;
import com.pvz2.models.world.mechanics.Mechanic;
import com.pvz2.models.world.winCondition.WinCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class VaseBreakerLevel extends GameWorld {
    private List<Vase> vases;
    private List<SeedPacket> droppedSeeds;
    private Supplier<GameWorld> builder;

    public VaseBreakerLevel(LevelSetup levelSetup,
                            ArrayList<LoseCondition> loseConditions,
                            WinCondition winCondition,
                            ArrayList<Mechanic> mechanics, Supplier<GameWorld> builder) {
        super(levelSetup, loseConditions, winCondition, mechanics);
        this.builder = builder;
        if (this.droppedSeeds == null) {
            this.droppedSeeds = new ArrayList<>();
        }

    }

    @Override
    protected void applyChapterRules() {

    }

    @Override
    public void tick(float delta) {
        super.tick(delta);

        for (SeedPacket seed : droppedSeeds) {
            seed.tick();
        }

        droppedSeeds.removeIf(SeedPacket::isExpired);
    }

    public String breakVaseAt(int row, int col) {
        Vase vase = getVaseAt(row, col);
        if (vase == null) return "There is no vase at that location.";
        if (vase.isBroken()) return "This vase is already broken.";
        return vase.breakVase(this);
    }

    public Vase getVaseAt(int row, int col) {
        for (Vase vase : getVases()) {
            if (vase.getRow() == row && vase.getCol() == col) {
                return vase;
            }
        }
        return null;
    }
    public List<Vase> getVases() {
        if (vases == null) vases = new ArrayList<>();
        return vases;
    }

    public List<SeedPacket> getDroppedSeeds() {
        if (droppedSeeds == null) droppedSeeds = new ArrayList<>();
        return droppedSeeds;
    }

    public Supplier<GameWorld> getBuilder() {
        return builder;
    }

    public void addVase(Vase vase) {
        getVases().add(vase);
    }
}
