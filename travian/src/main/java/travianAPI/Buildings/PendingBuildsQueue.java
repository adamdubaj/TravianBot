package travianAPI.Buildings;

public class PendingBuildsQueue {
    private String cancelLink, name, finishNowLink;
    private int level, buildDuration;

    public PendingBuildsQueue(String name, int level, int buildDuration, String cancelLink, String finishNowLink){
        this.name = name;
        this.level = level;
        this.buildDuration = buildDuration;
        this.cancelLink = cancelLink;
        this.finishNowLink = finishNowLink;
    }

    @Override
    public String toString() {
        return String.format("PENDING = name: %s level: %d, buildDuration: %d cancelLink: %s finishNowLink: %s",
                name, level, buildDuration, cancelLink, finishNowLink);
    }
}
