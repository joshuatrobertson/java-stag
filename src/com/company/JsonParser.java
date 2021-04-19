package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JsonParser {

    private String filename;

    public JsonParser(String filename) {
        this.filename = filename;
    }

    public void parse(List<Action> actions) {

        JSONParser parser = new JSONParser();

        JSONObject jObj = null;
        try {
            jObj = (JSONObject) parser.parse(new FileReader(filename));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create an array of actions
        JSONArray array = (JSONArray) jObj.get("actions");

        // Loop through the actions and create separate Action classes
        for (int i = 0; i < array.size(); i++) {
            Action action = new Action();
            JSONObject obj2=(JSONObject)array.get(i);
            JSONArray triggers = (JSONArray) obj2.get("triggers");
            for (Object trigger : triggers) {
                action.addTrigger((String) trigger);
            }
            JSONArray subjects = (JSONArray) obj2.get("subjects");
            for (Object subject : subjects) {
                action.addSubject((String) subject);
            }
            JSONArray consumed = (JSONArray) obj2.get("consumed");
            for (Object o : consumed) {
                action.addConsumed((String) o);
            }
            JSONArray produced = (JSONArray) obj2.get("produced");
            for (Object o : produced) {
                action.addProduced((String) o);
            }
            action.addNarration((String) obj2.get("narration"));
            actions.add(action);
        }
    }
}
