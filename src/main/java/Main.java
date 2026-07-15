import models.core.GameInitializer;
import view.terminalView.AppView;

public class Main {
    static void main() {
        GameInitializer.loadPlantUpgrades();
        AppView.run();
    }
}
