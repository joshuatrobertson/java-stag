package entities;

public abstract class Entity {

    private String name;
    private String description;

    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    abstract String getEntityType();

}
