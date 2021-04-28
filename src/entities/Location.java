package entities;

import java.util.*;

public class Location extends EntityMain {

    private final List<String> nextLocations = new ArrayList<>();
    private final Map<String, Character> characters = new HashMap<>();
    private final Map<String, Artefact> artefacts = new HashMap<>();
    private final Map<String, Furniture> furniture = new HashMap<>();
    private boolean isStartingLocation;

    public Location(String locationName) {
        super.setName(locationName);
    }

    public void setStartingLocation() {
        this.isStartingLocation = true;
    }

    public void removeArtefact(String artefact) {
        artefacts.remove(artefact);
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

    public void addCharacter(Character character) {
        characters.put(character.getName(), character);
    }

    public void addArtefact(Artefact artefact) {
        artefacts.put(artefact.getName(), artefact);
    }

    public Artefact getArtefactName(String artefactName) {
        return artefacts.get(artefactName);
    }

    public boolean checkArtefactExists(String artifactName) {
        return artefacts.containsKey(artifactName);
    }

    public void removeEntity(String entityName) {
        if (artefacts.containsKey(entityName)) {
            artefacts.remove(entityName);
        }
        else if (characters.containsKey(entityName)) {
            characters.remove(entityName);
        }
        else furniture.remove(entityName);
    }
    public boolean checkEntityExists(String entityName) {
        if (artefacts.containsKey(entityName) || characters.containsKey(entityName)) {
            return true;
        }
        return furniture.containsKey(entityName);
    }

    public boolean checkFurnitureExists(String furniture) {
        return this.furniture.containsKey(furniture);
    }

    public boolean checkCharacterExists(String character) {
        return this.characters.containsKey(character);
    }

    public Furniture getFurnitureEntity(String furniture) {
        return this.furniture.get(furniture);
    }

    public Character getCharacterEntity(String character) {
        return this.characters.get(character);
    }

    public void addFurniture(Furniture furniture) {
        this.furniture.put(furniture.getName(), furniture);
    }

    public String getEntitiesToString() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Artefact> item : artefacts.entrySet()) {
            str.append(item.getValue().getDescription()).append("\n");
        }
        for (Map.Entry<String, Furniture> item : furniture.entrySet()) {
            str.append(item.getValue().getDescription()).append("\n");
        }
        for (Map.Entry<String, Character> item : characters.entrySet()) {
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
