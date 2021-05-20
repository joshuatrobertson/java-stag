package entities;

public class Character extends Entity {

    public Character(String name, String description) {
        super(name, description);
    }

    String getEntityType() {
        return "character";
    }
}
