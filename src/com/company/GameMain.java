package com.company;

import java.util.*;

public class GameMain {

    Location startingLocation;
    HashMap<String, Player> players;
    String currentPlayerName;
    List<Action> actions;
    DotParser dotParser;
    JsonParser jsonParser;
    String entityFilename, actionFilename;
    List<String> triggers;
    HashMap<String, Location> locations;


    public GameMain(String entityFilename, String actionFilename) {
        this.entityFilename = entityFilename;
        this.actionFilename = actionFilename;
        dotParser = new DotParser(entityFilename);
        jsonParser = new JsonParser(actionFilename);
        locations = new HashMap<>();
        actions = new ArrayList<>();
        players = new HashMap<>();
        triggers = new ArrayList<>();
        startingLocation = new Location("location");
    }

    // Parse the data from the JSON and dot files
    public void load() {
        dotParser.parse(locations);
        jsonParser.parse(actions, triggers);
    }

    // Run the incoming command
    public String runCommand(String command) {
        parseName(command);
        Command incomingCommand = new Command(command, players.get(currentPlayerName));
        return incomingCommand.run();
    }

    // Create a new player (if it does not already exist)
    private void parseName(String command) {
        String[] tokens = command.split(":");
        String name = tokens[0];
        // If the player does not exist create a new one and set the starting location
        if (!players.containsKey(name)) {
            Player player = new Player(name, locations, dotParser.getFirstLocation().getName(), triggers, actions);
            players.put(name, player);
        }
        currentPlayerName = name;
    }

}
