package models.zombie.wave;

public class WaveSpawnEntry {
    private String zombieAlias;   // نام مستعار
    private int wavePointCost;    // هزینه این زامبی

    public WaveSpawnEntry(String zombieAlias, int wavePointCost) {
        this.zombieAlias = zombieAlias;
        this.wavePointCost = wavePointCost;
    }

    public String getZombieAlias() {
        return zombieAlias;
    }

    public int getWavePointCost() {
        return wavePointCost;
    }
}
