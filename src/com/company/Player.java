package com.company;

import java.util.HashMap;

public class Player {

    private Location currentLocation;
    private HashMap<String, Artefact> artefacts = new HashMap<>();
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public void addArtefact(Artefact artefact) {
        artefacts.put(artefact.getName(), artefact);
    }

    public void removeArtefact(Artefact artefact) {
        artefacts.remove(artefact.getName());
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
    }
}
