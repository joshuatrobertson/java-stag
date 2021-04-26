package com.company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {

    private Location currentLocation;
    private HashMap<String, Artefact> inventory = new HashMap<>();
    private String name;
    private HashMap<String, Location> locations;
    private int health;
    List<String> triggers;
    List<Action> actions;



    public Player(String name, HashMap<String, Location> locations, String startingLocation, List<String> triggers, List<Action> actions) {
        this.name = name;
        this.locations = locations;
        this.setCurrentLocation(locations.get(startingLocation));
        this.health = 3;
        this.triggers = triggers;
        this.actions = actions;
    }

    public HashMap<String, Location> getLocations() {
        return locations;
    }

    public HashMap<String, Artefact> getInventory() {
        return inventory;
    }

    public boolean checkSubjectExists(String subject) {
        return currentLocation.checkArtifactExists(subject) || inventory.containsKey(subject);
    }

    public void increaseHealth() {
        this.health++;
    }

    public void decreaseHealth() {
        this.health--;
    }

    public int getHealth() {
        return this.health;
    }

    public void addInventory(Artefact artefact) {
        inventory.put(artefact.getName(), artefact);
    }

    public boolean checkCarryArtefact(String artefact) {
        return inventory.containsKey(artefact);
    }

    public void removeArtefactCurrentLocation(String artefact) {
        locations.get(currentLocation.getName()).removeArtifact(artefact);
    }


    public void removeInventory(String artefact) {
        inventory.remove(artefact);
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
    }



    public String getName() {
        return this.name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Action getCurrentAction(String trigger) {
        for (Action a : actions) {
            if (a.checkTriggerExists(trigger)) {
                return a;
            }
        }
        return null;
    }

    public boolean checkTriggerExists(String trigger) {
        return triggers.contains(trigger);
    }



}
