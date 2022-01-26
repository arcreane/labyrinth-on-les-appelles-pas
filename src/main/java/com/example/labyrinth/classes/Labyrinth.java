package com.example.labyrinth.classes;

import java.util.*;

public class Labyrinth {
    Random random = new Random();
    Box[][] grid;
    boolean created = false;

    private final static String HORIZONTAL  =  "\u2501";
    private final static String VERTICAL    ="\u2503";
    private final static String CORNER_TL   ="\u250F";
    private final static String CORNER_TR   ="\u2513";
    private final static String CORNER_BL   ="\u2517";
    private final static String CORNER_BR  = "\u251B";
    private final static String TEE_TOP     ="\u2533";
    private final static String TEE_BOTTOM  ="\u253B";
    private final static String TEE_LEFT    ="\u2523";
    private final static String TEE_RIGHT   ="\u252B";
    private final static String CROSS       ="\u254B";

    public Labyrinth(int length, int witdh) {
        this.grid = new Box[length][witdh];

    }

    private String displayWall(Box wallBox){
        int line = wallBox.getLine();
        int column = wallBox.getColumn();
        boolean leftBox = false;
        boolean upBox = false;
        boolean rightBox = false;
        boolean downBox = false;


        try {
             leftBox = grid[line][column-1].compareType("wall");
        }
        catch (Exception e){}
        try {
            rightBox = grid[line][column+1].compareType("wall");
        }
        catch (Exception e){}
        try {
            upBox = grid[line-1][column].compareType("wall");
        }
        catch (Exception e){}
        try {
            downBox = grid[line+1][column].compareType("wall");
        }
        catch (Exception e){}


        if (!upBox && !downBox){
            if (!rightBox){
                return HORIZONTAL + " ";
            }

            return HORIZONTAL + HORIZONTAL;
        }
        else if (!leftBox && !rightBox){
            return  VERTICAL + " ";
        }

        else if ((rightBox && downBox) && (!upBox && !leftBox)){
            return CORNER_TL + HORIZONTAL;
        }

        else if ((rightBox && upBox) && (!downBox && !leftBox)){
            return CORNER_BL+HORIZONTAL;
        }

        else if ((leftBox && downBox) && (!upBox && !rightBox)){
            return CORNER_TR + " ";
        }

        else if ((upBox && leftBox) && (!rightBox && !downBox)){
            return CORNER_BR + " ";
        }

       else if (!leftBox){
            return  TEE_LEFT+HORIZONTAL;
        }
        else  if (!rightBox){
            return TEE_RIGHT + " ";
        }
        else if (!downBox){
            return TEE_BOTTOM + HORIZONTAL;
        }
        else if (!upBox){
            return TEE_TOP + HORIZONTAL;

        }

        else {
            return  CROSS + HORIZONTAL;
        }

    }


    public void display() {
        for (int line = 1; line < this.grid.length; line++) {
            for (int column = 1; column < this.grid[line].length; column++) {
                if (this.grid[line][column].compareType("path")) {
                    System.out.print(this.grid[line][column].getColor() + " ");
                }
                else {
                    System.out.print( displayWall(this.grid[line][column]));
                }
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
        Box actualBox = new Box(1, 1);

        while (!created) {
            walk(actualBox);
            actualBox = hunt();
        }
    }

    private void walk(Box actualBox) {
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
                            && (!grid[line + 2 * dline][column + 2 * dcolumn].isVisited()
                            && grid[line + 2 * dline][column + 2 * dcolumn].compareType("path"))) {

                        grid[line + dline][column + dcolumn].setVisited(true);
                        grid[line + dline][column + dcolumn].changeType();

                        grid[line + 2 * dline][column + 2 * dcolumn].setVisited(true);
                        actualBox = grid[line + 2 * dline][column + 2 * dcolumn];
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
                    else
                        return grid[line][column];

                }
            }
        }
        created = true;
        return null;
    }
}