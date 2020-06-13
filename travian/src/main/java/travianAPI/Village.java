package travianAPI;

public class Village {
    private Location location;
    private String name;
    private String link;

    Village(String name, Location location, String link){
        this.name = name;
        this.location = location;
        this.link = link;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

}
