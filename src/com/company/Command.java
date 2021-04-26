package com.company;

import java.util.HashMap;
import java.util.Map;

public class Command {
    private final String[] command;
    Player player;
    HashMap<String, Location> locations;

    public Command(String command, Player player) {
        this.command = parseText(command);
        this.player = player;
        this.locations = player.getLocations();
    }

    public String run() {
        String firstCommand = command[1].toLowerCase();
        if ((firstCommand.equals("inventory") | firstCommand.equals("inv")) && command.length == 2) {
            return inventoryCommand();
        }
        else if (firstCommand.equals("look") && command.length == 2) {
            return lookCommand();
        }
        else if (firstCommand.equals("get") && command.length == 3) {
            return getCommand();
        }
        else if (firstCommand.equals("goto") && command.length == 3) {
            return goToCommand();
        }
        else if (firstCommand.equals("drop") && command.length == 3) {
            return dropCommand();
        }
        else if (firstCommand.equals("health") && command.length == 2) {
            return healthCommand();
        }
        else return triggerCommands();
    }

    private String inventoryCommand() {
        if (player.getInventory().size() == 0) {
            return "You currently have no artefacts in your inventory";
        }
        else {
            StringBuilder str = new StringBuilder();
            for (Map.Entry<String, Artefact> artefact : player.getInventory().entrySet()) {
                str.append(artefact.getValue().getDescription()).append("\n");
            }
            return "In you inventory you currently have: \n" + str;
        }
    }

    private String lookCommand() {
        return "You are in " + player.getCurrentLocation().getDescription() + ". You can see:\n" +
                player.getCurrentLocation().getEntitiesToString() + "You can access from here:\n" +
                player.getCurrentLocation().getNextLocationsToString();
    }

    private String getCommand() {
        String artifact = command[2];
        if (!player.getCurrentLocation().checkArtifactExists(artifact)) {
            return "There is no " + artifact + " available";
        }
        else {
            player.addInventory(player.getCurrentLocation().getArtefactByName(artifact));
            player.removeArtefactCurrentLocation(artifact);
            return "You picked up a " + artifact;
        }
    }

    private String healthCommand() {
        return "Your current health is " + player.getHealth();
    }

    private String dropCommand() {
        String artefact = command[2];
        if (!player.checkCarryArtefact(artefact)) {
            return "You do not currently have a " + artefact;
        }
        else {
            Artefact newArtefact = player.getInventory().get(artefact);
            player.removeInventory(artefact);
            player.getCurrentLocation().addArtefact(newArtefact);
            return "You dropped a " + artefact;
        }
    }

    private String goToCommand() {
        if (player.getCurrentLocation().checkNextLocation(command[2])) {
            player.setCurrentLocation(locations.get(command[2]));
            return lookCommand();
        }
        else {
            return "You cannot go there from this location";
        }

    }

    private String[] parseText(String command) {
        return command.split("\\s+");
    }

    private String triggerCommands() {
        // Parsed Ok
        String errorCommand = "You cannot do that";
        if (parseTriggerCommands()) {
            String trigger = command[1];
            // Fetch the relevant Action
            Action action = player.getCurrentAction(trigger);
            // Make sure the player currently has all of the subjects
            for (String subject : action.getSubjects()) {
                if (!player.checkSubjectExists(subject) &&
                        !player.getCurrentLocation().checkEntityExists(subject)) {
                    return errorCommand;
                }
            }
            // Remove the consumed items and place the items within the map or take the player to the location
            return removeConsumed(action);
        }
        return errorCommand;
    }

    private String removeConsumed(Action action) {
        for (String consumed : action.getConsumed()) {
            if (consumed.equals("health")) {
                player.decreaseHealth();
                if (player.getHealth() == 0) {
                    // Remove the artefacts from the player and return to the starting point
                    return action.getNarration() + "\n" + lostHealth();
                }
            }
            if (player.checkCarryArtefact(consumed)) {
                player.removeInventory(consumed);
            }
            if (player.getCurrentLocation().checkEntityExists(consumed)) {
                player.getCurrentLocation().removeEntity(consumed);
            }
        }
        return getUnplacedItem(action);
    }

    private String getUnplacedItem(Action action) {
        Location unplacedItems = locations.get("unplaced");

        for (String produced : action.getProduced()) {
            // Check if it produces health
            if (produced.equals("health")) {
                if (player.getHealth() < 3) {
                    player.increaseHealth();
                }
            }
            // Check if it is a location
            else if (locations.containsKey(produced)) {
                Location oldLocation = player.getCurrentLocation();
                player.setCurrentLocation(locations.get(produced));
                oldLocation.addNextLocation(player.getCurrentLocation().getName());
            }
            //Check if it is an artefact
            else if (unplacedItems.checkFurnitureExists(produced)) {
                 // Add the furniture to the map and remove it from unplaced items
                 player.getCurrentLocation().addFurniture(unplacedItems.getFurnitureEntity(produced));
             }
            else if (unplacedItems.checkCharacterExists(produced)) {
                // Add the character to the map and remove it from unplaced items
                player.getCurrentLocation().addCharacter(unplacedItems.getCharacterEntity(produced));
            }
             else if (unplacedItems.checkArtifactExists(produced)) {
                 // Add the character to the map and remove it from unplaced items
                 player.getCurrentLocation().addArtefact(unplacedItems.getArtefactByName(produced));
             }
            // Remove the item
            unplacedItems.removeEntity(produced);
        }
        // Return the relevant narration
        return action.getNarration();
    }


    // Here we parse the trigger by checking the first word is equal to a trigger word
    // i.e. chop, cut etc. Then we check the last word is equal to a subject i.e. tree.
    // This allows for commands such as 'chop tree with axe' or 'cut tree using the axe'.
    // Further improvements could be made, such as parsing the command using a sentence structure
    // to avoid garbage words within the command as a command such as 'cut word another word axe' will
    // parse Ok.
    private boolean parseTriggerCommands() {
        String trigger = command[1];
        // Take the last item in the array to account for determiners and adjectives
        String subject = command[command.length - 1];
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

    private String lostHealth() {
        // Move the players current items from the inventory to the current location
        Location location = player.getCurrentLocation();
        HashMap<String, Artefact> artefacts = player.getInventory();


        for (Map.Entry<String, Artefact> a : artefacts.entrySet()) {
            location.addArtefact(a.getValue());
            player.removeInventory(a.getValue().getName());
        }
        return "You have lost all of your items and are now back at the start";
    }

}
