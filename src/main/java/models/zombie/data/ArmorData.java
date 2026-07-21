package models.zombie.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class ArmorData {
    @JsonProperty("ArmorType")
    private String ArmorType;

    @JsonProperty("BaseHealth")
    private int BaseHealth;

    @JsonProperty("ArmorFlags")
    private List<String> ArmorFlags = new ArrayList<>();

    public String getArmorType() { return ArmorType; }
    public int getBaseHealth() { return BaseHealth; }
    public List<String> getArmorFlags() { return ArmorFlags; }
}