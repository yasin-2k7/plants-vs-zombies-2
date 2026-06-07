package models.world.winCondition;

import models.world.GameWorld;

public class NormalWin implements WinCondition{
    @Override
    public boolean checkWin(GameWorld game) {
        return false;
    }
}
