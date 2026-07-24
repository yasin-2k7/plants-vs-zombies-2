package controller;

import models.core.PasswordHasher;
import models.core.User;
import models.core.UserDataManager;
import models.core.UserManager;
import view.terminalView.AppView;
import view.terminalView.GameMenuView;
import view.terminalView.MainMenuView;
import view.terminalView.SignupMenuView;

public class LoginMenuController implements MenuController {

    private User recoveringUser = null;
    private boolean isSQPassed = false;

    @Override
    public void changeMenu() {
        AppView.currentScreen = MainMenuView.getInstance();
    }

    @Override
    public void exitMenu() {
        AppView.currentScreen = SignupMenuView.getInstance();
    }

    public String loginUser(String username, String password, boolean stayLoggedIn) {
        String result = UserManager.login(username, password, stayLoggedIn);
        return result;
    }

    public String forgetPassword(String username, String email) {
        User user = UserDataManager.loadUser(username);
        if (user == null) {
            return "This username doesn't exist.";
        }
        if (!user.getEmail().equals(email)) {
            return "Email is not correct.";
        }
        this.recoveringUser = user;
        this.isSQPassed = false;
        return "Please answer security question: " + "\n"
                + user.getSecurityQ();

    }

    public String answerSQ(String answer) {
        if (recoveringUser == null) {
            return "Please enter your username and email first.";
        }

        if (!recoveringUser.checkSeqA(answer)) {
            isSQPassed = false;
            return "your answer is incorrect";
        }
        isSQPassed = true;
        return "Enter your new password:";

    }

    public String newPassword(String password) {
        if (recoveringUser == null) {
            return "Please enter your username and email first.";
        }

        if (!isSQPassed) {
            return "Security question has not been answered correctly yet.";
        }

        recoveringUser.setHashPassword(PasswordHasher.hashSHA256(password));

        boolean saveSuccess = UserDataManager.saveUser(recoveringUser);

        if (saveSuccess) {
            recoveringUser = null;
            isSQPassed = false;
            return "Your password changed successfully.";
        } else {
            return "Failed to save the new password. Please try again.";
        }
    }

    public void showCurrentMenu() {
        GameMenuView.getInstance().showResult("Current menu: login menu");
    }
}
