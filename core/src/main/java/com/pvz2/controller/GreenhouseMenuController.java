package com.pvz2.controller;

import com.pvz2.models.core.App;
import com.pvz2.models.core.User;
import com.pvz2.models.greenhouse.GreenHouse;
import com.pvz2.models.greenhouse.Pot;

import java.util.ArrayList;
import java.util.List;

public class GreenhouseMenuController implements MenuController {

    @Override
    public void changeMenu() {
    }

    @Override
    public void exitMenu() {
    }

    private GreenHouse getGreenHouse() {
        User user = App.getCurrentUser();
        return (user != null) ? user.getGreenhouse() : null;
    }

    public List<String> showGreenhouse() {
        List<String> output = new ArrayList<>();
        GreenHouse greenHouse = getGreenHouse();

        if (greenHouse == null) {
            output.add("Error: No user logged in.");
            return output;
        }

        output.add("===== Greenhouse =====");
        for (int r = 1; r <= 4; r++) {
            StringBuilder rowStr = new StringBuilder();
            for (int c = 1; c <= 5; c++) {
                Pot pot = greenHouse.getPot(c, r);
                if (pot == null) continue;

                String status;
                if (pot.isLocked()) {
                    status = "[LOCKED]";
                } else if (pot.isEmpty()) {
                    status = "[EMPTY ]";
                } else if (pot.isReady()) {
                    status = "[READY  ] (" + pot.getPlantType().name() + ")";
                } else {
                    long remaining = pot.getRemainingHours();
                    status = "[GROWING] (" + pot.getPlantType().name() + " - " + remaining + "h left)";
                }
                rowStr.append(status).append(" ");
            }
            output.add(rowStr.toString().trim());
        }
        output.add("=========================");
        return output;
    }

    public String plantPot(int x, int y) {
        GreenHouse greenHouse = getGreenHouse();
        return (greenHouse != null) ? greenHouse.plantPot(x, y) : "Error: No user logged in.";
    }

    public String collect(int x, int y) {
        GreenHouse greenHouse = getGreenHouse();
        return (greenHouse != null) ? greenHouse.collect(x, y) : "Error: No user logged in.";
    }

    public String grow(int x, int y) {
        GreenHouse greenHouse = getGreenHouse();
        return (greenHouse != null) ? greenHouse.grow(x, y) : "Error: No user logged in.";
    }
}
