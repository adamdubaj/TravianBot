package travianAPI.Items;

import java.nio.file.Path;

public class Helmet extends Item {
    public Helmet(String id, String name, String description, Path picture, int amount) {
        super(id, name, description, picture, amount);
    }
}
