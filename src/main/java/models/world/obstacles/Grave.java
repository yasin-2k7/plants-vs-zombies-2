package models.world.obstacles;

public class Grave extends Obstacle{
    private int row;
    private int col;

    public Grave(float x, float y, int row, int col) {
        super(x, y, 700);
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean blocksProjectiles() {
        return !isDestroyed;
    }
}
