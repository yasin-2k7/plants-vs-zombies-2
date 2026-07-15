package models.zombie.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ZombieData {
    @JsonProperty("Hitpoints") // نام اصلی در جی‌سان
    @JsonAlias("hitpoints")    // نام جایگزین
    private int hitpoints;

    @JsonProperty("EatDPS")
    @JsonAlias("eatDPS")
    private int eatDPS;

    @JsonProperty("Speed")
    @JsonAlias("speed")
    private double speed;

    @JsonProperty("WavePointCost")
    @JsonAlias("wavePointCost")
    private int wavePointCost;

    @JsonProperty("Weight")
    @JsonAlias("weight")
    private int weight;

    @JsonProperty("ZombieArmorProps")
    @JsonAlias("zombieArmorProps")
    private List<String> zombieArmorProps = new ArrayList<>();

    @JsonProperty("ImpTargetColumn")
    @JsonAlias("impTargetColumn")
    private Integer impTargetColumn;

    @JsonProperty("ImpType")
    @JsonAlias("impType")
    private String impType;

    @JsonProperty("HealthPercentThrowImp")
    @JsonAlias("healthPercentThrowImp")
    private Integer healthPercentThrowImp;
    // سایر فیلدهای خاص بر اساس objclass (مثل MaxTorchReach, MaxClaimedSunCurrency و ...)

    public int getHitpoints() { return hitpoints; }
    public int getEatDPS() { return eatDPS; }
    public double getSpeed() { return speed; }
    public int getWavePointCost() { return wavePointCost; }
    public int getWeight() { return weight; }
    public List<String> getZombieArmorProps() { return zombieArmorProps; }
    public Integer getImpTargetColumn() { return impTargetColumn; }
    public String getImpType() { return impType; }
    public Integer getHealthPercentThrowImp() { return healthPercentThrowImp; }
}
