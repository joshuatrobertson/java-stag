package stag;

import entities.Location;
import entities.Player;

import java.util.*;

public class GameMain {

    private final HashMap<String, Player> playersGroup;
    private String currentPlayerName;
    private final List<Action> actions;
    private final DotParser dotParser;
    private final JsonParser jsonParser;
    private final List<String> triggers;
    private final HashMap<String, Location> locations;

    public GameMain(String entityFilename, String actionFilename) {
        dotParser = new DotParser(entityFilename);
        jsonParser = new JsonParser(actionFilename);
        locations = new HashMap<>();
        actions = new ArrayList<>();
        playersGroup = new HashMap<>();
        triggers = new ArrayList<>();
    }

    // Parse the data from the JSON and dot files
    public void loadGame() {
        dotParser.parseDot(locations);
        jsonParser.parseJson(actions, triggers);
    }

    // Run the incoming command
    public String runCommand(String command) {
        parseName(command);
        var incomingCommand = new Commands(command, playersGroup.get(currentPlayerName));
        return incomingCommand.runCommand();
    }

    // Create a new player (if it does not already exist)
    private void parseName(String command) {
        String[] tokens = command.split(":");
        String name = tokens[0];
        // If the player does not exist create a new one and set the starting location
        var player = new Player(locations, triggers, actions);
        playersGroup.putIfAbsent(name, player);
        currentPlayerName = name;
    }

}
