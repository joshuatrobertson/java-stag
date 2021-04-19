package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameMain {

    HashMap<String, Location> locations;
    HashMap<String, Player> players;
    Player currentPlayer;
    List<Action> actions;
    DotParser dotParser;
    JsonParser jsonParser;
    String entityFilename, actionFilename;

    public GameMain(String entityFilename, String actionFilename) {
        this.entityFilename = entityFilename;
        this.actionFilename = actionFilename;
        dotParser = new DotParser(entityFilename);
        jsonParser = new JsonParser(actionFilename);
        locations = new HashMap<>();
        actions = new ArrayList<>();
    }

    public void load() {
        dotParser.parse(locations);
        jsonParser.parse(actions);
    }

    public String runCommand(String command) {
        parseName(command);
        return "The current player is " + currentPlayer;
    }

    private void parseName(String command) {
        String[] tokens = command.split(":");
        String name = tokens[0];
        if (players.containsKey(name)) {
            Player player = new Player(name);
            players.put(name, player);
        }
        currentPlayer = players.get(name);

    }
}
