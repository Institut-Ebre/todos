package com.iesebre.dam2.sergi.todos;

import com.google.gson.Gson;

/**
 * Created by sergi on 13/11/15.
 */
public class TodoItem {

    private String name;
    private boolean done;
    private int priority;

    public TodoItem() {
        this.name = "";
        this.done = false;
        this.priority = 1;
    }

    public TodoItem(String name, boolean done, int priority) {
        this.name = name;
        this.done = done;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "{ name: " + name + ", done: " + done + ", priority: "
                +  priority +"}";
    }

    public String serialize(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
