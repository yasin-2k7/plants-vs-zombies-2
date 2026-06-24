package models.zombie.data;

import java.util.List;

public class ZombieData {
    private int Hitpoints;
    private int EatDPS;
    private double Speed;
    private int WavePointCost;
    private int Weight;
    private List<String> ZombieArmorProps;
    private Integer ImpTargetColumn;
    private String ImpType;
    private Integer HealthPercentThrowImp;
    // سایر فیلدهای خاص بر اساس objclass (مثل MaxTorchReach, MaxClaimedSunCurrency و ...)

    public int getHitpoints() { return Hitpoints; }
    public int getEatDPS() { return EatDPS; }
    public double getSpeed() { return Speed; }
    public int getWavePointCost() { return WavePointCost; }
    public int getWeight() { return Weight; }
    public List<String> getZombieArmorProps() { return ZombieArmorProps; }
    public Integer getImpTargetColumn() { return ImpTargetColumn; }
    public String getImpType() { return ImpType; }
    public Integer getHealthPercentThrowImp() { return HealthPercentThrowImp; }
}
