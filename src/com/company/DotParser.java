package com.company;

import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DotParser {

    private final String filename;
    private static boolean firstLocationBool = true;
    private Location firstLocation;

    public DotParser(String filename) {
        this.filename = filename;
    }

    public void parse(HashMap<String, Location> locations) {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(filename);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
            for (Graph g : subGraphs) {
                    ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                    for (Graph g1 : subGraphs1) {
                        ArrayList<Node> nodesLoc = g1.getNodes(false);
                        Node nLoc = nodesLoc.get(0);
                        // Create a new location and set the name
                        Location location = new Location(nLoc.getId().getId());
                        location.setDescription(nLoc.getAttribute("description"));
                        // If it is the first location encountered set it as the starting location
                        if (firstLocationBool) {
                            location.setStartingLocation(true);
                            firstLocation = location;
                        }
                        firstLocationBool = false;
                        ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
                        for (Graph g2 : subGraphs2) {
                            // Loop through to find which entity type it is and place in the correct array
                            String entityType = g2.getId().getId();
                            ArrayList<Node> nodesEnt = g2.getNodes(false);
                            switch (entityType) {
                                case "artefacts":
                                    for (Node nEnt : nodesEnt) {
                                        Artefact artefact = new Artefact(nEnt.getId().getId());
                                        artefact.setDescription(nEnt.getAttribute("description"));
                                        location.addArtefact(artefact);
                                    }
                                    break;
                                case "furniture":
                                    for (Node nEnt : nodesEnt) {
                                        Furniture furniture = new Furniture(nEnt.getId().getId());
                                        furniture.setDescription(nEnt.getAttribute("description"));
                                        location.addFurniture(furniture);
                                    }
                                    break;
                                case "characters":
                                    for (Node nEnt : nodesEnt) {
                                        Character character = new Character(nEnt.getId().getId());
                                        character.setDescription(nEnt.getAttribute("description"));
                                        location.addCharacter(character);
                                    }
                                    break;
                                default:
                            }
                        }
                        locations.put(location.getName(), location);
                    }
                ArrayList<Edge> edges = g.getEdges();
                // Add the possible paths from each location, by first searching for the initial value
                // in the hashmap, before setting the next path
                for (Edge e : edges) {
                    String firstLocation = e.getSource().getNode().getId().getId();
                    String secondLocation = e.getTarget().getNode().getId().getId();
                    locations.get(firstLocation).addNextLocation(secondLocation);
                }
            }
        } catch (FileNotFoundException | ParseException e) {
            System.out.println(e);
        }
    }

    public Location getFirstLocation() {
        return firstLocation;
    }
}
