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
           // System.out.println(this.getName() + " is on foot and attacks " + plantName + " normally.");
            return;
        }

        // بررسی گردوی بلند (Tall-nut) که نمی‌تواند از آن بپرد
        if (plantName.equalsIgnoreCase("Tall-nut")) {
           // System.out.println(this.getName() + " cannot jump over Tall-nut! Attacking normally.");
            return;
        }

        // بررسی گیاهانی که دودو از روی آن‌ها می‌پرد (گردو، مین، و غیره)
        if (plantName.equalsIgnoreCase("Wall-nut") || plantName.equalsIgnoreCase("Potato Mine") /* + سایر موانع مجاز */) {
           // System.out.println(this.getName() + " jumped over the " + plantName + "!");
            // در اینجا منطق جابجایی (رد شدن) باید فراخوانی شود
        } else {
          // System.out.println(this.getName() + " does not jump over " + plantName + ". Attacking normally.");
        }
    }

    @Override
    public void takeDamage(int damageAmount, String damageType) {
        // در فصل Frostbite Caves زامبی‌ها با پرتابه یخی یخ نمی‌زنند (فقط دمیج می‌خورند)
       // this.setHealth(this.getHealth() - damageAmount);

        // اگر جان پرنده تمام شود (مثلا زیر 50 درصد کل جان)، پیاده می‌شود
       /* if (this.getHealth() <= 50 && isRiding) {
            isRiding = false;
            System.out.println("Dodo bird was defeated! " + this.getName() + " is now walking.");
        }

        if (this.getHealth() <= 0) {
            die();
        }*/
    }

    @Override
    public void die() {
        //System.out.println(this.getName() + " (Dodo Rider) has died.");
    }
}
