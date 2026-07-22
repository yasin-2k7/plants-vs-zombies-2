import models.core.GameInitializer;
import models.core.UserManager;
import view.terminalView.AppView;

public class Main {
    static void main() {
        UserManager.init();
        GameInitializer.loadPlantUpgrades();
        AppView.run();
    }
}
