package models.plant.state;
import models.plant.GameComponent;
import models.plant.Plant;

public class ActionPlantState implements PlantState{
    @Override
    public void action(Plant plant) {
        // TODO: اجرای عملیات اصلی گیاه (شلیک، تولید خورشید و...)
        // اگر زامبی از بین رفت، وضعیت را به IdlePlantState برگردان
        //System.out.println("Plant is taking action!");

        // فراخوانی آپدیتِ کامپوننت‌های گیاه
        /*for (GameComponent component : plant.getComponents()) {
            component.update(plant);
        }*/
    }
}
