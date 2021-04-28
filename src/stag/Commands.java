package stag;

import entities.*;
import java.util.Map;

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
        String artefact = userCommands[2];
        // Check that the art
        if (!player.getCurrentLocation().checkArtefactExists(artefact)) {
            return "There is no " + artefact + " available";
        }
        else {
            player.addInventory(player.getCurrentLocation().getArtefactName(artefact));
            locations.get(player.getCurrentLocation().getName()).removeArtefact(artefact);
            return "You picked up a " + artefact;
        }
    }

    // Used to return the players health to them
    private String healthCommand() {
        return "Your current health is " + player.getHealth();
    }

    // The drop command allows the user to drop a currently held artefact, which is then
    // placed at the current location
    private String dropCommand() {
        String artefact = userCommands[2];
        if (!player.checkCarryArtefact(artefact)) {
            return "You do not currently have a " + artefact;
        }

        var newArtefact = player.getInventory().get(artefact);
        player.removeInventory(artefact);
        player.getCurrentLocation().addArtefact(newArtefact);
        return "You dropped a " + artefact;

    }

    // The go to command checks whether the specified location is a possible route
    // and then places the user there
    private String goToCommand() {
        String location = userCommands[2];
        // Check that the location is part of the next location
        if (player.getCurrentLocation().checkNextLocation(location)) {
            // If it is move the player to the specified location
            player.setCurrentLocation(locations.get(location));
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
                    !player.getCurrentLocation().checkEntityExists(subject)) {
                return false;
            }
        }
        return true;
    }

    private void removeConsumed(Action action) {
        for (String consumed : action.getConsumed()) {
            if (consumed.equals(HEALTH)) {
                player.decreaseHealth();
            }
            if (player.checkCarryArtefact(consumed)) {
                player.removeInventory(consumed);
            }
            if (player.getCurrentLocation().checkEntityExists(consumed)) {
                player.getCurrentLocation().removeEntity(consumed);
            }
        }
    }

    // Produce the entities whether it is health, a location, furniture, a character or an artefact
    // and remove the items from 'unplaced items'
    private void produceEntities(Action action) {
        var unplacedItems = locations.get("unplaced");
        // Loop through each produced item and check whether it exists
        for (String item : action.getProduced()) {
            checkEachEntityType(unplacedItems, item);
            // Remove the item
            unplacedItems.removeEntity(item);
        }


    }

    private void checkEachEntityType(Location unplacedItems, String item) {
        // Check if it produces health
        checkHealthProduced(item);
        // Check if it is a location
        checkLocationProduced(item);
        //Check if it is an artefact
        checkFurnitureProduced(item, unplacedItems);
        // Check if it is a character
        checkCharacterProduced(item, unplacedItems);
        // Check if it is an artefact
        checkArtefactProduced(item, unplacedItems);
    }

    private void checkArtefactProduced(String artefact, Location unplacedItems) {
        if (unplacedItems.checkArtefactExists(artefact)) {
            // Add the character to the map and remove it from unplaced items
            player.getCurrentLocation().addArtefact(unplacedItems.getArtefactName(artefact));
        }
    }

    private void checkCharacterProduced(String character, Location unplacedItems) {
        if (unplacedItems.checkCharacterExists(character)) {
            // Add the character to the map and remove it from unplaced items
            player.getCurrentLocation().addCharacter(unplacedItems.getCharacterEntity(character));
        }
    }

    // Check if the players
    private void checkHealthProduced(String health) {
        if (health.equals(HEALTH) && player.getHealth() < 3) {
            player.increaseHealth();
        }
    }

    private void checkLocationProduced(String location) {
        if (locations.containsKey(location)) {
            var oldLocation = player.getCurrentLocation();
            player.setCurrentLocation(locations.get(location));
            oldLocation.addNextLocation(player.getCurrentLocation().getName());
        }
    }

    private void checkFurnitureProduced(String furniture, Location unplacedItems) {
        if (unplacedItems.checkFurnitureExists(furniture)) {
            // Add the furniture to the map and remove it from unplaced items
            player.getCurrentLocation().addFurniture(unplacedItems.getFurnitureEntity(furniture));
        }
    }

    // Here we parse the trigger by checking the first word is equal to a trigger word
    // i.e. chop, cut etc. Then we check the last word is equal to a subject i.e. tree.
    // This allows for commands such as 'chop tree with axe' or 'cut tree using the axe'.
    // Further improvements could be made, such as parsing the command using a sentence structure
    // to avoid garbage words within the command as a command such as 'cut word another word axe' will
    // parse Ok.
    private boolean parseTriggerCommands() {
        String trigger = userCommands[1];
        // Take the last item in the array to account for determiners and adjectives
        String subject = userCommands[userCommands.length - 1];
        // Check both the trigger and subject exist for current player/ current location
            // If the trigger does not exist return false
            if (!player.checkTriggerExists(trigger)) {
               return false;
            }
            // Either the player holds the item **
            if (player.checkSubjectExists(subject)) {
                return true;
            }
            // ** Or the map contains the item
        return player.getCurrentLocation().checkEntityExists(subject);

    }

    private String lostAllHealth() {
        // Move the players current items from the inventory to the current location and
        // set the location to the beginning location
        var location = player.getCurrentLocation();
        Map<String, Artefact> artefacts = player.getInventory();

        for (Map.Entry<String, Artefact> a : artefacts.entrySet()) {
            location.addArtefact(a.getValue());
            player.removeInventory(a.getValue().getName());
        }
        // Set the current location to the starting location and return the players health
        player.setCurrentLocation(locations.get(player.getStartingLocation()));
        player.setHealth(3);
        return "You have lost all of your items and are now back at the start";
    }

}
