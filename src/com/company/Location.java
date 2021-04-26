package com.company;

import java.util.*;

public class Location extends EntityMain {

    private List<String> nextLocations = new ArrayList<>();
    private HashMap<String, Character> characters = new HashMap<>();
    private HashMap<String, Artefact> artefacts = new HashMap<>();
    private HashMap<String, Furniture> furniture = new HashMap<>();
    private boolean startingLocation;

    public Location(String locationName) {
        super.setName(locationName);
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

    public void removeArtifact(String artefactName) {
        artefacts.remove(artefactName);
    }

    public Artefact getArtefactByName(String artefactName) {
        return artefacts.get(artefactName);
    }

    public boolean checkArtifactExists(String artifactName) {
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
        if (artefacts.containsKey(entityName)) {
            return true;
        }
        else if (characters.containsKey(entityName)) {
            return true;
        }
        else if (furniture.containsKey(entityName)) {
            return true;
        }
        else {
            return false;
        }
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

    public void setStartingLocation(boolean startingLocation) {
        startingLocation = startingLocation;
    }


    public String getEntitiesToString() {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Artefact> item : artefacts.entrySet()) {
            str.append(item.getValue().getDescription() + "\n");
        }
        for (Map.Entry<String, Furniture> item : furniture.entrySet()) {
            str.append(item.getValue().getDescription() + "\n");
        }
        for (Map.Entry<String, Character> item : characters.entrySet()) {
            str.append(item.getValue().getDescription() + "\n");
        }
        return str.toString();
    }



    public String getNextLocationsToString() {
        StringBuilder str = new StringBuilder();
        for (String location : nextLocations) {
            str.append(location + "\n");
        }
        return str.toString();
    }

}
