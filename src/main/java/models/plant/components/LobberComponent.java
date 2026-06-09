package models.plant.components;

import models.plant.GameComponent;
import models.plant.Plant;

public class LobberComponent implements GameComponent {
    private int damage;

    public LobberComponent(int damage) {
        this.damage = damage;
    }
    @Override
    public void update(Plant owner) {
        // TODO: تایمر شلیک
        lobAttack();
    }

    private void lobAttack() {
        // TODO: ایجاد پرتابه‌ای که به صورت سهمی (منجنیق) حرکت می‌کند
        // این پرتابه باید موانع را نادیده بگیرد و مستقیم روی سر زامبی هدف بیفتد
    }
}
