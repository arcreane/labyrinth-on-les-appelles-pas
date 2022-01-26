package com.example.labyrinth.classes;

import java.util.ArrayList;

public class Box {
    int line;
    int column;
    String color = "⬛";
    boolean discovered = false;
    String type = "wall";
    boolean visited = false;
    boolean blocked = false;

    public Box(int x, int y) {
        this.line = x;
        this.column = y;
    }


    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<Integer> getCord(){
        ArrayList<Integer> tab = new ArrayList<Integer>();
        tab.add(this.line, this.column);
        return tab;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }

    public void changeType(){
        if (this.color.equals("⬛")) {
            this.color = "▪";
            this.type = "path";
        }
        else {
            this.color = "⬛";
            this.type = "wall";
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean compareType(String type){
        return type.equals(this.type);
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}