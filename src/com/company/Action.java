package com.company;

import java.util.ArrayList;
import java.util.List;

public class Action {

    private List<String> triggers = new ArrayList<>();
    private List<String> subjects = new ArrayList<>();
    private List<String> consumed = new ArrayList<>();
    private List<String> produced = new ArrayList<>();
    private String narration;

    public void addTrigger(String trigger) {
        triggers.add(trigger);
    }

    public void addSubject(String subject) {
        subjects.add(subject);
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



}
