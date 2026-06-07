package models.plant;

public class PlantFactory {
//        // متد ساخت اکنون سطح (level) را هم می‌پذیرد
//        public Plant createPlant(String type, int level) {
//            switch (type.toUpperCase()) {
//                case "PEASHOOTER":
//                    return buildPeashooter(level);
//                case "SUNFLOWER":
//                    return buildSunflower(level);
//                default:
//                    return null;
//            }
//        }
//
//        private Plant buildPeashooter(int level) {
//            // جان گیاه با افزایش سطح بیشتر می‌شود (مثلاً سطح ۱: ۳۰۰، سطح ۲: ۳۵۰ و...)
//            int baseHealth = 300;
//            int finalHealth = baseHealth + (level - 1) * 50;
//
//            Plant p = new Plant("Peashooter", finalHealth);
//
//            // سرعت شلیک با افزایش سطح بالاتر می‌رود (فاصله زمانی کمتر می‌شود)
//            int baseCooldown = 2000; // ۲ ثانیه
//            int finalCooldown = baseCooldown - (level - 1) * 150; // در لول‌های بالاتر سریع‌تر شلیک می‌کند
//
//            p.addComponent(new ShooterComponent(finalCooldown));
//            return p;
//        }
//
//        private Plant buildSunflower(int level) {
//            Plant p = new Plant("Sunflower", 300);
//
//            // مقدار تولید آفتاب با لول‌آپ بیشتر می‌شود
//            int sunValue = 25 + (level - 1) * 5;
//            p.addComponent(new SunProducerComponent(5000, sunValue)); // فرستادن مقدار سکه به کامپوننت
//            return p;
//        }

}
