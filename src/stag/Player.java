package stag;

import entities.Artefact;
import entities.EntityType;
import entities.Location;
import stag.Action;
import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static entities.EntityType.ARTEFACT;

public class Player {

    private static final int STARTING_HEALTH = 3;
    private Location currentLocation;
    private String startingLocation;
    private final Map<String, Artefact> inventory = new HashMap<>();
    private final Map<String, Location> locations;
    private int health;
    private final List<String> triggers;
    private final List<Action> actions;

    public Player(Map<String, Location> locations, List<String> triggers, List<Action> actions) {
        this.locations = locations;
        this.health = STARTING_HEALTH;
        this.triggers = triggers;
        this.actions = actions;
        setStartingLocation();
        this.setCurrentLocation(locations.get(startingLocation));
    }

    private void setStartingLocation() {
        for (Map.Entry<String, Location> l : locations.entrySet()) {
            if (l.getValue().isStartingLocation()) {
                this.startingLocation = l.getValue().getName();
            }
        }
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public Map<String, Artefact> getInventory() {
        return inventory;
    }

    public boolean checkSubjectExists(String subject) {
        return currentLocation.checkEntityExists(subject, ARTEFACT) || inventory.containsKey(subject);
    }

    public void setHealth(int health) {
        this.health = health;
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

    public String getStartingLocation() {
        return startingLocation;
    }

    public void removeInventory(String artefact) {
        inventory.remove(artefact);
    }

    public void setCurrentLocation(Location location) {
        currentLocation = location;
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
