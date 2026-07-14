package view.terminalView;

import models.core.App;
import view.View;

import java.util.Scanner;

public class AppView {
    static View currentScreen = SignupMenuView.getInstance();

    public static void setCurrentScreen(View screen) {
        currentScreen = screen;
    }

    public static void run(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            String command = scanner.nextLine();
            if (command.equals("exit")){
                System.exit(0);
            }
            currentScreen.processCommand(command);
        }
    }
}
