package models.quest;

import controller.GameMenuController;
import models.core.User;
import models.enums.Chapter;
import models.enums.PlantFamily;
import models.enums.PlantType;
import models.quest.types.DailyQuest;
import models.quest.types.MainQuest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class QuestManager {
    private List<Quest> activeQuests = new ArrayList<>();
    private List<Quest> completedQuests = new ArrayList<>();

    public void checkAllQuests(User user, boolean isGameEnded) {
        QuestStats stats = user.getQuestStats();
        Iterator<Quest> iterator = activeQuests.iterator();
        while (iterator.hasNext()) {
            Quest quest = iterator.next();
            if (quest.isEndGameDependent() && !isGameEnded) {
                continue;
            }
            if (!quest.isCompleted() && quest.checkCompletion(stats)) {
                quest.complete(user);
                String msg = "🎉 Quest completed: " + quest.getDescription();
                GameMenuController.updateState(msg);
                user.addCompletedQuest(quest.getId());
                completedQuests.add(quest);
                iterator.remove();
            }
        }
    }

    public void checkAllQuests(User user) {
        checkAllQuests(user, false);
    }

    public void generateDailyQuests(User user) {
        // 1. آفتاب‌گیر روزانه (با مقدار تصادفی از 3000، 4000، 5000)
        int sunAmount = getRandomSunAmount();
        DailyQuest sunQuest = QuestFactory.createDailySunCollectorQuest(sunAmount);
        activeQuests.add(sunQuest);

        // 3. plant باز حرفه‌ای (یک گیاه تصادفی که قدرت کشتن دارد)
        PlantType randomKillerPlant = getRandomKillerPlant(user);
        if (randomKillerPlant != null) {
            DailyQuest plantKillerQuest = QuestFactory.createPlantKillerQuest(randomKillerPlant);
            activeQuests.add(plantKillerQuest);
        }

        // 4. only cactus (همیشه فعال)
        DailyQuest cactusQuest = QuestFactory.createCactusOnlyQuest();
        activeQuests.add(cactusQuest);

        // 8. تخریب‌گر حرفه‌ای
        activeQuests.add(QuestFactory.createExplosiveDestroyerQuest());

        // 9. تقارن
        activeQuests.add(QuestFactory.createSymmetryQuest());

        // 10. کشتار خانوادگی (برای هر خانواده به‌جز خانواده‌های خاص)
        for (PlantFamily family : PlantFamily.values()) {
            if (family != PlantFamily.SUN_PRODUCER && family != PlantFamily.MODIFIER) {
                activeQuests.add(QuestFactory.createFamilySlaughterQuest(family));
            }
        }

        // 11. شکوفایی در محدودیت‌ها (برای هر خانواده به جز خانواده‌های خاص)
        for (PlantFamily family : PlantFamily.values()) {
            if (family != PlantFamily.SUN_PRODUCER && family != PlantFamily.MODIFIER) {
                activeQuests.add(QuestFactory.createFlourishInRestrictionsQuest(family));
            }
        }

        // 13. برد پشت برد
        activeQuests.add(QuestFactory.createWinStreakQuest());

        // 14. تقریبا پیروز
        activeQuests.add(QuestFactory.createAlmostVictoryQuest());

        // 15. OCD نَمَنَ
        activeQuests.add(QuestFactory.createOCDQuest());
    }

    public void generateMainQuests(User user) {
        // 2. شکارچی فصل (برای هر فصلی که کاربر آن را باز کرده است)
        for (Chapter chapter : getAvailableChapters(user)) {
            MainQuest chapterQuest = QuestFactory.createChapterHunterQuest(chapter.name());
            activeQuests.add(chapterQuest);
        }

        // 5. گیاه‌خوار اقتصادی (برای n های 0 تا 5)
        for (int n = 0; n <= 5; n++) {
            MainQuest ecoQuest = QuestFactory.createEconomicVegetarianQuest(n);
            activeQuests.add(ecoQuest);
        }

        // 7. سرعت عمل
        activeQuests.add(QuestFactory.createSpeedQuest());

        // 16. روز ابری
        activeQuests.add(QuestFactory.createCloudyDayQuest());

        // 17. یه ستون کمتر (برای n=1 تا تعداد ستون‌های بازی، مثلاً 9)
        for (int n = 0; n < 9; n++) {
            activeQuests.add(QuestFactory.createOneLessColumnQuest(n));
        }

        // 18. سطر بی دفاع (برای n=0 تا تعداد سطرها، مثلاً 5)
        for (int n = 0; n < 5; n++) {
            activeQuests.add(QuestFactory.createDefenselessRowQuest(n));
        }

        // 19. صلیب بی دفاع (برای n های 0 تا min(سطرها, ستون‌ها))
        int minDim = Math.min(5, 9);
        for (int n = 0; n < minDim; n++) {
            activeQuests.add(QuestFactory.createCrossDefenselessQuest(n));
        }
    }

    // متد جدید برای تولید کوئست‌های Epic
    public void generateEpicQuests(User user) {
        // 6. استاد دفاع
        activeQuests.add(QuestFactory.createMasterDefenseQuest());
        // 12. شب یا صبح
        activeQuests.add(QuestFactory.createNightOrMorningQuest());

        // 20. وقت چمن‌زنی (برای n های 10، 20، 30، 40، 50)
        int[] options = {10, 20, 30, 40, 50};
        for (int n : options) {
            activeQuests.add(QuestFactory.createLawnmowerTimeQuest(n));
        }
    }

    private int getRandomSunAmount() {
        int[] options = {3000, 4000, 5000};
        return options[new Random().nextInt(options.length)];
    }

    private PlantType getRandomKillerPlant(User user) {
        List<PlantType> killerPlants = Arrays.stream(PlantType.values())
                .filter(pt -> pt.family != PlantFamily.SUN_PRODUCER && pt != PlantType.GRAVE_BUSTER)
                .collect(Collectors.toList());
        if (killerPlants.isEmpty()) return null;
        return killerPlants.get(new Random().nextInt(killerPlants.size()));
    }

    private List<Chapter> getAvailableChapters(User user) {
        return Arrays.asList(Chapter.values());
    }

    public List<Quest> getSortedActiveQuests() {
        Collections.sort(activeQuests);
        return activeQuests;
    }

    public void addQuest(Quest quest) {
        this.activeQuests.add(quest);
    }

    public void resetDailyIfNeeded(User user) {
        LocalDate today = LocalDate.now();
        QuestStats stats = user.getQuestStats();
        if (!today.equals(stats.getLastResetDate())) {
            stats.resetDailyStats();
            activeQuests.removeIf(q -> q instanceof DailyQuest && !((DailyQuest) q).isAvailableToday());
        }
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }

    public List<Quest> getCompletedQuests() {
        return completedQuests;
    }
}