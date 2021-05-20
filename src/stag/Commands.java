package stag;

import entities.Artefact;
import entities.Location;

import java.util.Map;

import static entities.EntityType.ALL_ENTITY_TYPES;
import static entities.EntityType.ARTEFACT;

public class Commands {
    public static final String HEALTH = "health";
    private final String[] userCommands;
    private final Player player;
    private final Map<String, Location> locations;

    public Commands(String command, Player player) {
        this.userCommands = removeWhitespace(command);
        this.player = player;
        this.locations = player.getLocations();
    }

    // Runs the command fetched from the user
    public String runCommand() {
        // Get the first command given by the user
        String firstCommand = userCommands[1].toLowerCase();

        switch (firstCommand) {
            case "inv":
            case "inventory":
                return printInventory();
            case "get":
                return getCommand();
            case "look":
                return lookCommand();
            case "goto":
                return goToCommand();
            case "drop":
                return dropCommand();
            case HEALTH:
                return healthCommand();
            default:
                return triggerCommands();
        }

    }

    // Uses StringBuilder to fetch and print out the users current inventory
    private String printInventory() {
        if (player.getInventory().size() != 0) {
            var str = new StringBuilder();
            for (Map.Entry<String, Artefact> artefact : player.getInventory().entrySet()) {
                str.append(artefact.getValue().getDescription()).append("\n");
            }
            return "In you inventory you currently have: \n" + str;
        }
        else {
            return "You currently have no artefacts in your inventory";
        }
    }

    // The look command returns a description of the users current location, the entities at the location
    // and the possible next locations
    private String lookCommand() {
        return "You are in " + player.getCurrentLocation().getDescription() + ". You can see:\n" +
                player.getCurrentLocation().getEntitiesToString() + "You can access from here:\n" +
                player.getCurrentLocation().getNextLocationsToString();
    }

    // The get command searches for the specified artefact at the current location
    // and places it into the users inventory if it exists, before removing it from
    // the location
    private String getCommand() {
        String entityName = userCommands[2];
        // Check that the art
        if (!player.getCurrentLocation().checkEntityExists(entityName, ARTEFACT)) {
            return "There is no " + entityName + " available";
        }
        else {
            player.addInventory((Artefact) player.getCurrentLocation().getEntity(entityName));
            locations.get(player.getCurrentLocation().getName()).removeEntity(entityName);
            return "You picked up a " + entityName;
        }
    }

    // Used to return the players health to them
    private String healthCommand() {
        return "Your current health is " + player.getHealth();
    }

    // The drop command allows the user to drop a currently held artefact, which is then
    // placed at the current location
    private String dropCommand() {
        String artefactName = userCommands[2];
        if (!player.checkCarryArtefact(artefactName)) {
            return "You do not currently have a " + artefactName;
        }

        var newArtefact = player.getInventory().get(artefactName);
        player.removeInventory(artefactName);
        player.getCurrentLocation().addEntity(newArtefact);
        return "You dropped a " + artefactName;

    }

    // The go to command checks whether the specified location is a possible route
    // and then places the user there
    private String goToCommand() {
        String locationName = userCommands[2];
        // Check that the location is part of the next location
        if (player.getCurrentLocation().checkNextLocation(locationName)) {
            // If it is move the player to the specified location
            player.setCurrentLocation(locations.get(locationName));
            // Print the details of the new location to the console
            return lookCommand();
        }
        else {
            return "You cannot go there from this location";
        }

    }

    private String[] removeWhitespace(String command) {
        return command.split("\\s+");
    }

    private String triggerCommands() {
        // Fetch the relevant Action
        var action = player.getCurrentAction(userCommands[1]);

        // Parse the command to see if it's valid and make sure the player currently has all of the subjects
        if (parseTriggerCommands() && checkSubjectsExist(action)) {
            // Remove the consumed items and place the items within the map or take the player to the location
            removeConsumed(action);
            produceEntities(action);
            if (player.getHealth() == 0) {
                // Remove the artefacts from the player and return to the starting point
                return action.getNarration() + "\n" + lostAllHealth();
            }
            return action.getNarration();

        }
        return "You cannot do that";
    }

    private boolean checkSubjectsExist(Action action) {
        for (String subject : action.getSubjects()) {
            if (!player.checkSubjectExists(subject) &&
                    !player.getCurrentLocation().checkEntityExists(subject, ALL_ENTITY_TYPES)) {
                return false;
            }
        }
        return true;
    }

    private void removeConsumed(Action action) {
        for (String consumedItem : action.getConsumed()) {
            if (consumedItem.equals(HEALTH)) {
                player.decreaseHealth();
            }
            if (player.checkCarryArtefact(consumedItem)) {
                player.removeInventory(consumedItem);
            }
            if (player.getCurrentLocation().checkEntityExists(consumedItem, ALL_ENTITY_TYPES)) {
                player.getCurrentLocation().removeEntity(consumedItem);
            }
        }
    }

    // Produce the entities whether it is health, a location, furniture, a character or an artefact
    // and remove the items from 'unplaced items'
    private void produceEntities(Action action) {
        var unplacedItems = locations.get("unplaced");
        // Loop through each produced item and check whether it exists
        for (String producedItem : action.getProduced()) {
            checkProducedEntityType(unplacedItems, producedItem);
            // Remove the item
            unplacedItems.removeEntity(producedItem);
        }
    }

    // See what type the entity produced is and add it to the current location
    private void checkProducedEntityType(Location unplacedItems, String producedEntity) {
        // Check if it produces health
        if (producedEntity.equals(HEALTH) && player.getHealth() < 3) {
            player.increaseHealth();
        }
        // Is it a location?
        else if (locations.containsKey(producedEntity)) {
            var oldLocation = player.getCurrentLocation();
            // Therefore set the current location to the given new location
            player.setCurrentLocation(locations.get(producedEntity));
            // Add the location so it is possible to go back there
            oldLocation.addNextLocation(player.getCurrentLocation().getName());
            // Is it an entity?
        } else {
            // Add the character to the map and remove it from unplaced items
            player.getCurrentLocation().addEntity(unplacedItems.getEntity(producedEntity));
        }
    }

    // Here we parse the trigger by checking the first word is equal to a trigger word
    // i.e. chop, cut etc. Then we check the last word is equal to a subject i.e. tree.
    // This allows for commands such as 'chop tree with axe' or 'cut tree using the axe'.
    // Further improvements could be made, such as parsing the command using a sentence structure
    // to avoid garbage words within the command as a command such as 'cut word another word axe' will
    // parse Ok.
    private boolean parseTriggerCommands() {
        String triggerCommand = userCommands[1];
        // Take the last item in the array to account for determiners and adjectives
        String subjectCommand = userCommands[userCommands.length - 1];
        // Check both the trigger and subject exist for current player/ current location
            // If the trigger does not exist return false
            if (!player.checkTriggerExists(triggerCommand)) {
               return false;
            }
            // Either the player holds the item **
            if (player.checkSubjectExists(subjectCommand)) {
                return true;
            }
            // ** Or the map contains the item
        return player.getCurrentLocation().checkEntityExists(subjectCommand, ALL_ENTITY_TYPES);

    }

    private String lostAllHealth() {
        // Move the players current items from the inventory to the current location and
        // set the location to the beginning location
        var location = player.getCurrentLocation();
        Map<String, Artefact> artefacts = player.getInventory();

        for (Map.Entry<String, Artefact> a : artefacts.entrySet()) {
            location.addEntity(a.getValue());
            player.removeInventory(a.getValue().getName());
        }
        // Set the current location to the starting location and return the players health
        player.setCurrentLocation(locations.get(player.getStartingLocation()));
        player.setHealth(3);
        return "You have lost all of your items and are now back at the start";
    }

}
