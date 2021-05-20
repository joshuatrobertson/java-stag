package entities;

import java.util.*;

public class Location extends Entity {

    private final List<String> nextLocations = new ArrayList<>();
    private final Map<String, Entity> entities = new HashMap<>();
    private boolean isStartingLocation;

    public Location(String name, String description) {
        super(name, description);
    }

    String getEntityType() {
        return "location";
    }

    public void setStartingLocation() {
        this.isStartingLocation = true;
    }

    public void removeEntity(String entity) {
        entities.remove(entity);
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getName(), entity);
    }

    public boolean isStartingLocation() {
        return this.isStartingLocation;
    }

    public boolean checkNextLocation(String nextLocation) {
        return nextLocations.contains(nextLocation);
    }

    public void addNextLocation(String locationName) {
        nextLocations.add(locationName);
    }

    public String getEntityName(String entity) {
        return entities.get(entity).getName();
    }
    
    // Check whether the entity name exists and matches the given type
    public boolean checkEntityExists(String name, EntityType entityType) {
        // Does the entity exist?
        if (entities.containsKey(name)) {
            // Is it the specified type?
            switch(entityType) {
                case ARTEFACT: return entities.get(name).getEntityType().equals("artefact");
                case CHARACTER: return entities.get(name).getEntityType().equals("character");
                case FURNITURE: return entities.get(name).getEntityType().equals("furniture");
                case ALL_ENTITY_TYPES: return true;
            }
        }
        return false;
    }

    public Entity getEntity(String name) {
        return entities.get(name);
    }

    public String getEntitiesToString() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Entity> item : entities.entrySet()) {
            str.append(item.getValue().getDescription()).append("\n");
        }
        return str.toString();
    }

    public String getNextLocationsToString() {
        StringBuilder str = new StringBuilder();
        for (String location : nextLocations) {
            str.append(location).append("\n");
        }
        return str.toString();
    }

}
