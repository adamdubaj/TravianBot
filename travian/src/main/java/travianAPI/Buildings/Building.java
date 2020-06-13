package travianAPI.Buildings;

public class Building {

    private int level, nextLevelWoodAmount, nextLevelClayAmount, nextLevelSteelAmount, nextLevelWheatAmount;
    private String id, name;
    private int nextLevelCropConsumption;

    public Building(String id, String name, int level, int nextLevelWoodAmount, int nextLevelClayAmount, int nextLevelSteelAmount, int nextLevelWheatAmount){
        this.id = id;
        this.name = name;
        this.level = level;
        this.nextLevelWoodAmount = nextLevelWoodAmount;
        this.nextLevelClayAmount = nextLevelClayAmount;
        this.nextLevelSteelAmount = nextLevelSteelAmount;
        this.nextLevelWheatAmount = nextLevelWheatAmount;
    }
    public Building(String id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("BUILD = ID: %s NAME: %s LEVEL: %d nextLvlWood: %d nextLvlClay: %d nextLvlSteel: %d nextLvlWheat: %d",
                id, name, level, nextLevelWoodAmount, nextLevelClayAmount, nextLevelSteelAmount, nextLevelWheatAmount);
    }

    public int getLevel() {
        return level;
    }

    public int getNextLevelWoodAmount() {
        return nextLevelWoodAmount;
    }

    public int getNextLevelClayAmount() {
        return nextLevelClayAmount;
    }

    public int getNextLevelSteelAmount() {
        return nextLevelSteelAmount;
    }

    public int getNextLevelWheatAmount() {
        return nextLevelWheatAmount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
