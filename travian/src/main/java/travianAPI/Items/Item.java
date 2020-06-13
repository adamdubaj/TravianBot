package travianAPI.Items;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Item {
    private final String id;
    private String name;
    private String description;
    private Path picture;
    private int amount;

    public Item(String id, String name, String description, Path picture, int amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Path getPicture() {
        return picture;
    }

    public int getAmount() {
        return amount;
    }
}
