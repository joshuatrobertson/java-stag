package entities;

public class Furniture extends Entity {

    public Furniture(String name, String description) {
        super(name, description);
    }

    String getEntityType() {
        return "furniture";
    }
}
