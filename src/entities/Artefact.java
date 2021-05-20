package entities;

public class Artefact extends Entity {

    public Artefact(String name, String description) {
        super(name, description);
    }

    String getEntityType() {
        return "artefact";
    }

}
