package models.quest;

import models.core.User;
import models.enums.Chapter;
import models.enums.PlantFamily;
import models.enums.PlantType;
import models.quest.reward.*;
import models.quest.types.DailyQuest;
import models.quest.types.MainQuest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class QuestManager {
    private List<Quest> activeQuests = new ArrayList<>();
    private List<Quest> completedQuests = new ArrayList<>();
    private QuestStats stats = new QuestStats();

    public void checkAllQuests(User user) {
        Iterator<Quest> iterator = activeQuests.iterator();
        while (iterator.hasNext()) {
            Quest quest = iterator.next();
            if (quest.checkCompletion(stats)) {
                quest.complete(user);
                completedQuests.add(quest);
                iterator.remove();
            }
        }
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

    public QuestStats getStats() { return stats; }
    public void addQuest(Quest quest) { this.activeQuests.add(quest); }

    public void resetDailyIfNeeded() {
        LocalDate today = LocalDate.now();
        if (!today.equals(stats.getLastResetDate())) {
            stats.resetDailyStats();
            activeQuests.removeIf(q -> q instanceof DailyQuest && !((DailyQuest) q).isAvailableToday());
        }
    }

    public List<Quest> getActiveQuests() {return activeQuests;}
}