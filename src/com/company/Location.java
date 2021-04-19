package com.company;

import java.util.ArrayList;
import java.util.List;

public class Location extends EntityMain {

    private List<String> nextLocations = new ArrayList<>();
    private List<Character> characters = new ArrayList<>();
    private List<Artefact> artefacts = new ArrayList<>();
    private List<Furniture> furniture = new ArrayList<>();
    private boolean startingLocation;

    public Location(String locationName) {
        super.setName(locationName);
    }

    public void addNextLocation(String locationName) {
        nextLocations.add(locationName);
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void addArtefact(Artefact artefact) {
        artefacts.add(artefact);
    }

    public void addFurniture(Furniture furniture) {
        this.furniture.add(furniture);
    }

    public void setStartingLocation() {
        startingLocation = true;
    }


}
