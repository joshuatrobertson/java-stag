package stag;

import entities.Artefact;
import entities.Character;
import entities.Furniture;
import entities.Location;
import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.logging.*;

public class DotParser {

    private static final Logger logger = Logger.getLogger(DotParser.class.getName());
    private final String filename;
    private boolean firstLocationBool = true;
    private Location firstLocation;
    private Parser parser;
    private FileReader reader;
    private List<Graph> graphs;
    private List<Graph> subGraphs;
    private static final String DESCRIPTION = "description";

    // Set up the parser using the given filename
    public DotParser(String filename) {
        this.filename = filename;
        try {
            parser = new Parser();
            reader = new FileReader(filename);
            parser.parse(reader);
            graphs = parser.getGraphs();
            subGraphs = graphs.get(0).getSubgraphs();
        } catch (FileNotFoundException | ParseException e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }

    // Parse the file by traversing the graph and subgraphs
    public void parseDot(HashMap<String, Location> locations) {
            for (Graph g : subGraphs) {
                    List<Graph> subGraphs1 = g.getSubgraphs();
                    // Loop through the subgraphs and create a Location object for each
                    for (Graph graph : subGraphs1) {
                        var location = getLocation(graph);
                        // Set to false on the first loop through to get the first location
                        firstLocationBool = false;
                        addItemsToLocation(graph, location);
                        locations.put(location.getName(), location);
                    }
                getGraphEdges(locations, g);
            }
        }

    // Get the locations from the graph
    private Location getLocation(Graph graph) {
        List<Node> nodesLoc = graph.getNodes(false);
        var node = nodesLoc.get(0);
        // Create a new location and set the name
        var location = new Location(node.getId().getId());
        location.setDescription(node.getAttribute(DESCRIPTION));
        // If it is the first location encountered set it as the starting location
        if (firstLocationBool) {
            location.setStartingLocation();
        }
        return location;
    }

    // Fetch the artefacts, furniture and characters from the file and add to their respective
    // classes
    private void addItemsToLocation(Graph graph, Location location) {
        List<Graph> subGraphs2 = graph.getSubgraphs();
        for (Graph graph2 : subGraphs2) {
            // Loop through to find which entity type it is and place in the correct array
            String entityType = graph2.getId().getId();
            List<Node> nodesEnt = graph2.getNodes(false);
            for (Node node : nodesEnt) {
                switch (entityType) {
                    case "artefacts":
                        var artefact = new Artefact(node.getId().getId());
                        artefact.setDescription(node.getAttribute(DESCRIPTION));
                        location.addArtefact(artefact);
                        break;
                    case "furniture":
                        var furniture = new Furniture(node.getId().getId());
                        furniture.setDescription(node.getAttribute(DESCRIPTION));
                        location.addFurniture(furniture);
                        break;
                    case "characters":
                        var character = new Character(node.getId().getId());
                        character.setDescription(node.getAttribute(DESCRIPTION));
                        location.addCharacter(character);
                        break;
                }
            }
        }
    }

    private void getGraphEdges(HashMap<String, Location> locations, Graph g) {
        List<Edge> edges = g.getEdges();
        // Add the possible paths from each location, by first searching for the initial value
        // in the hashmap, before setting the next path
        for (Edge e : edges) {
            String firstLoc = e.getSource().getNode().getId().getId();
            String secondLoc = e.getTarget().getNode().getId().getId();
            locations.get(firstLoc).addNextLocation(secondLoc);
        }
    }
}
