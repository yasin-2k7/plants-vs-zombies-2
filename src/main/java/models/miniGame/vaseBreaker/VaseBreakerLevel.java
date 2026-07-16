package models.miniGame.vaseBreaker;

import models.plant.card.PlantCard;
import models.world.GameWorld;
import models.world.levelSetup.LevelSetup;
import models.world.loseCondition.LoseCondition;
import models.world.mechanics.Mechanic;
import models.world.winCondition.WinCondition;

import java.util.ArrayList;
import java.util.List;

public class VaseBreakerLevel extends GameWorld {
    private List<Vase> vases;
    private List<SeedPacket> droppedSeeds;

    public VaseBreakerLevel(LevelSetup levelSetup,
                            ArrayList<LoseCondition> loseConditions,
                            WinCondition winCondition,
                            ArrayList<Mechanic> mechanics){
        super(levelSetup, loseConditions, winCondition, mechanics);
        this.vases = new ArrayList<>();
        this.droppedSeeds = new ArrayList<>();
    }

    @Override
    protected void applyChapterRules() {

    }

    @Override
    public void tick() {
        super.tick();

        droppedSeeds.removeIf(SeedPacket::isCollected);
    }

    public void breakVaseAt(int row, int col){
        Vase vase = getVaseAt(row, col);
        if (vase != null && !vase.isBroken()) {
            vase.breakVase(this);
        }
    }

    public Vase getVaseAt(int row, int col) {
        for (Vase vase : vases) {
            if (vase.getRow() == row && vase.getCol() == col) {
                return vase;
            }
        }
        return null;
    }

    public void collectSeedPacket(SeedPacket packet){
        if (packet != null && !packet.isCollected()) {
            packet.collect();
            PlantCard card = new PlantCard(packet.getPlantType(), 0, 0);
            getConveyorBelt().add(card);
        }
    }

    public List<Vase> getVases() { return vases; }
    public List<SeedPacket> getDroppedSeeds() { return droppedSeeds; }
    public void addVase(Vase vase) { this.vases.add(vase); }
}
