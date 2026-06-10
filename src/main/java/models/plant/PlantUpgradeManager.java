package models.plant;

import models.core.User;

public class PlantUpgradeManager {

    private static PlantUpgradeManager instance;
    private PlantUpgradeManager() {}

    public static PlantUpgradeManager getInstance() {
        if (instance == null) {
            instance = new PlantUpgradeManager();
        }
        return instance;
    }

    // TODO: متدهایی برای محاسبه هزینه‌های ارتقا (فرمول‌ها اینجا)

    /// فرمول محاسبه سکه لازم برای رفتن به لول بعدی رو اینجا
    public int calculateRequiredCoins(int currentLevel) {
        return 0;
    }

    /// : فرمول محاسبه Seed Packet لازم برای رفتن به لول بعدی رو اینجا
    public int calculateRequiredSeedPackets(int currentLevel) {
        return 0;
    }

    // TODO: متدهای اصلی بررسی و اعمال ارتقا

    /// چک میکنه آیا کاربر سکه و بزر کافی برای آپگرید این گیاه رو داره یا نه
    public boolean canUpgrade(User user, String plantName) {

        return false;
    }


    /// اگر canUpgrade ترو بود، این متد رو صدا میزنیم تا سکه/بزر رو کم کنه و لول رو ببره بالا
    public void performUpgrade(User user, String plantName) {

    }
}
