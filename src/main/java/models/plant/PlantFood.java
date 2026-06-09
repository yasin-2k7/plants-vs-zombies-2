package models.plant;
import models.plant.Plant;

public class PlantFood {
    private Plant plant;
    private long duration; // مدت زمان ماندگاری قابلیت ویژه
    private long startTime;

    public PlantFood(Plant plant, long duration) {
        this.plant = plant;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();
    }

    public void performAction() {
       //TODO:  مثال بررسی تمام شدن زمان غذای گیاه و برگشت به حالت عادی
       //* if (System.currentTimeMillis() - startTime > duration) {
          //plant.setState(new IdlePlantState(plant));
    //}
    }

    public void applyPlantFood() {
        //TODO:تغییر وضعیت گیاه به حالت استفاده از غذای گیاه برای مثلا 5 ثانیه مثال
        //this.setState(new PlantFood(this, 5000));
        //....
    }

    public void triggerSpecialAbility() {
        // این متد کامپوننت‌های گیاه را صدا می‌زند.
        // مثلاً اگر کامپوننت شلیک دارد، به جای ۱ تیر، در هر فریم ۳ تیر شلیک می‌کند.
    }
}
