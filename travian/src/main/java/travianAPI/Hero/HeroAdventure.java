package travianAPI.Hero;

import travianAPI.Location;

public class HeroAdventure {
    private final String id;
    private Location location;
    private int moveTime, timeLeft;
    private Difficulty difficulty;

    public HeroAdventure(String id, Location location, int moveTime, int timeLeft, Difficulty difficulty) {
        this.id = id;
        this.location = location;
        this.moveTime = moveTime;
        this.timeLeft = timeLeft;
        this.difficulty = difficulty;
    }
}
