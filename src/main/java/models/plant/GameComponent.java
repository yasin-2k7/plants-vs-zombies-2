package models.plant;

public interface GameComponent {
    void update(Plant owner);
    void activatePlantFood(Plant owner);
}