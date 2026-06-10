package models.miniGame.IZombie;

public class Brain {
    private int row;
    private boolean isEaten;

    public void eat(){
        this.isEaten = true;
    }

    public boolean isEaten(){
        return isEaten;
    }
}
