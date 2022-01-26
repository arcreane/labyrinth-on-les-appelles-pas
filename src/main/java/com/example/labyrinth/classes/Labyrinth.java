package com.example.labyrinth.classes;

import java.util.*;

public class Labyrinth {
    //⬛
    //⬜
    Random random = new Random();
    Box[][] grid = new Box[20][20];
    boolean created = false;


    public void display() {
        for (int i = 1; i < this.grid.length; i++) {
            for (int j = 1; j < this.grid[i].length; j++) {
                System.out.print(this.grid[i][j].getColor() + " ");
            }
            System.out.println("");
        }
    }

    public void create() {
        for (int line = 1; line < this.grid.length; line++) {
            for (int column = 1; column < this.grid[line].length; column++) {
                Box box = new Box(line, column);
                if (line % 2 == 0 || column % 2 == 0) {
                } else {
                    box.changeType();
                }
                this.grid[line][column] = box;
            }
        }

        while (!created) {
            Box actualBox = new Box(1, 1);
            walk(actualBox);
            actualBox = hunt();
        }

    }

    private void walk(Box actualBox) {
        actualBox = new Box(1, 1);
        for (int line = 1; line < this.grid.length; line++) {
            for (int column = 1; column < this.grid[line].length; column++) {
                if (grid[line][column].getType().equals("path")) {
                    actualBox = grid[line][column];
                }
            }
        }

        Map dictX = new HashMap();
        dictX.put("S", 0);
        dictX.put("E", 1);
        dictX.put("W", -1);
        dictX.put("N", 0);

        Map dictY = new HashMap();
        dictY.put("S", -1);
        dictY.put("E", 0);
        dictY.put("W", 0);
        dictY.put("N", 1);

        ArrayList<String> list = new ArrayList<String>(List.of("S", "E", "N", "W"));
        boolean blocked = false;
        while (!blocked) {
            boolean moved = false;
            Collections.shuffle(list);


            for (int i = 0; i < 4; i++) {
                int dline = (int) dictX.get(list.get(i));
                int dcolumn = (int) dictY.get(list.get(i));
                int line = actualBox.getLine();
                int column = actualBox.getColumn();
                int validSide = 0;
                if ((line + dline < grid[line].length && line + dline >= 1)
                        && (column + dcolumn < grid[line].length && column + dcolumn >= 1)
                        && (line + 2 * dline < grid.length && column + 2 * dcolumn < grid[line].length)
                        && (line + 2 * dline >= 1 && column + 2 * dcolumn >= 1)) {

                    if (grid[line + dline][column + dcolumn].compareType("wall")
                            && !grid[line + 2 * dline][column + 2 * dcolumn].isVisited()) {

                        grid[line + dline][column + dcolumn].setVisited(true);
                        grid[line + dline][column + dcolumn].changeType();

                        grid[line + 2 * dline][column + 2 * dcolumn].setVisited(true);
                        actualBox = grid[line + 2 * dline][column + 2 * dcolumn];
                        display();
                        System.out.println();
                        moved = true;
                    }
                }
            }
            if (!moved) {
                grid[actualBox.getLine()][actualBox.getColumn()].setBlocked(true);
                blocked = true;
            }
        }
    }


    private Box hunt() {
        for (int line = 1; line < this.grid.length; line++) {
            for (int column = 1; column < this.grid[line].length; column++) {
                if (grid[line][column].getType().equals("path") && grid[line][column].isVisited()) {
                    if (grid[line][column].isBlocked()) {
                        continue;
                    }
                }
            }
        }
        created = true;
        return null;
    }
}