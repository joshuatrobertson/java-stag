package com.company;

import java.util.ArrayList;
import java.util.List;

public class Action {

    private final List<String> triggers = new ArrayList<>();
    private final List<String> subjects = new ArrayList<>();
    private final List<String> consumed = new ArrayList<>();
    private final List<String> produced = new ArrayList<>();
    private String narration;

    public void addTrigger(String trigger) {
        triggers.add(trigger);
    }

    public void addSubject(String subject) {
        subjects.add(subject);
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<String> getConsumed() {
        return consumed;
    }

    public String getNarration() {
        return this.narration;
    }

    public List<String> getProduced() {
        return produced;
    }

    public void addConsumed(String consumed) {
        this.consumed.add(consumed);
    }

    public void addProduced(String produced) {
        this.produced.add(produced);
    }

    public void addNarration(String narration) {
        this.narration = narration;
    }

    public boolean checkTriggerExists(String trigger) {
        return triggers.contains(trigger);
    }

}
