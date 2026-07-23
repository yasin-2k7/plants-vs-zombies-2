package models.world.obstacles;

import controller.GameMenuController;
import models.core.App;
import models.enums.CollectableType;
import models.world.Collectable;
import models.world.GameWorld;

public class Grave extends Obstacle{
    public enum GraveType {
        NORMAL,
        SUN,
        PLANT_FOOD
    }

    private GraveType type;
    private boolean isCollected = false;

    private int row;
    private int col;

    public Grave(float x, float y, int row, int col, GraveType type) {
        super(x, y, 700);
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public GraveType getType() { return type; }
    public boolean isCollected() { return isCollected; }
    public void setCollected(boolean collected) { isCollected = collected; }

    public boolean blocksProjectiles() {
        return !isDestroyed;
    }
    @Override
    public void takeDamage(int amount, String type) {
        if (isDestroyed) return;
        super.takeDamage(amount, type);
        GameMenuController.updateState("grave in (" + x + ", " + y + ") health: " + health);
        if (isDestroyed) {
            releaseContent();
        }
    }

    public void releaseContent() {
        if (isCollected) return;

        GameWorld game = App.getCurrentGame();
        if (game == null) return;

        if (type == GraveType.SUN) {
            game.setSun(game.getSun() + 50);
            GameMenuController.updateState("A grave released 50 suns!");
        } else if (type == GraveType.PLANT_FOOD) {
            if (game.getPlantFoods() < 3) {
                game.getActiveCollectables().add(new Collectable(x, y, CollectableType.PLANT_FOOD));
                GameMenuController.updateState("A grave released a plant food!");
            }
        }
        isCollected = true;
    }

}
