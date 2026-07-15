package models.zombie.data;

import java.util.ArrayList;
import java.util.List;

public class ArmorData {
    private String ArmorType;
    private int BaseHealth;
    private List<String> ArmorFlags = new ArrayList<>();

    public String getArmorType() { return ArmorType; }
    public int getBaseHealth() { return BaseHealth; }
    public List<String> getArmorFlags() { return ArmorFlags; }
}
