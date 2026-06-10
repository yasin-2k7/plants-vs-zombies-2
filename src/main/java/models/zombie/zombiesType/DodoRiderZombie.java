package models.zombie.zombiesType;

import models.zombie.Zombie;

public class DodoRiderZombie extends Zombie {
    private boolean isRiding; // آیا هنوز روی دودو سوار است؟

    public DodoRiderZombie(String name, int health, int speed, int damage) {
        super(name, health, speed, damage);
        this.isRiding = true; // در ابتدا سوار بر دودو است
    }

    // بررسی پرش از روی گیاه بر اساس نام/نوع گیاه
    public void encounterPlant(String plantName) {
        if (!isRiding) {
            return;
        }

        // بررسی گردوی بلند (Tall-nut) که نمی‌تواند از آن بپرد
        if (plantName.equalsIgnoreCase("Tall-nut")) {
            return;
        }

        // بررسی گیاهانی که دودو از روی آن‌ها می‌پرد (گردو، مین، و غیره)
        if (plantName.equalsIgnoreCase("Wall-nut") || plantName.equalsIgnoreCase("Potato Mine") /* + سایر موانع مجاز */) {
            // در اینجا منطق جابجایی (رد شدن) باید فراخوانی شود
        } else {
        }
    }

    @Override
    public void takeDamage(int damageAmount, String damageType) {
        // در فصل Frostbite Caves زامبی‌ها با پرتابه یخی یخ نمی‌زنند (فقط دمیج می‌خورند)
       // this.setHealth(this.getHealth() - damageAmount);

        // اگر جان پرنده تمام شود (مثلا زیر 50 درصد کل جان)، پیاده می‌شود
        if (this.getHealth() <= 50 && isRiding) {
            isRiding = false;
        }

        if (this.getHealth() <= 0) {
            die();
        }
    }

    @Override
    public void die() {
    }
}
