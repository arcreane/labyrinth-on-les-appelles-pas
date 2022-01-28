package com.example.labyrinth.classes;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

//Labyrinth class, the labyrinth can create itself, put the player and the ending point on the maze and
//display itself
public class Labyrinth implements Serializable {
    @Serial
    private static final long serialVersionUID = 975142525840047392L;
    Box[][] grid;
    boolean created = false;
    boolean animations;
    int lineStart;
    int columnStart;
    int animSpeed;

    //Variables initialization, these variables are used to display the walls
    private final static String HORIZONTAL = "\u2501";
    private final static String VERTICAL = "\u2503";
    private final static String CORNER_TL = "\u250F";
    private final static String CORNER_TR = "\u2513";
    private final static String CORNER_BL = "\u2517";
    private final static String CORNER_BR = "\u251B";
    private final static String TEE_TOP = "\u2533";
    private final static String TEE_BOTTOM = "\u253B";
    private final static String TEE_LEFT = "\u2523";
    private final static String TEE_RIGHT = "\u252B";
    private final static String CROSS = "\u254B";

    //Constructor, need the number of lines and columns and if the animation is enabled or not
    public Labyrinth(int line, int column, boolean animation, int animSpeed) {
        this.grid = new Box[line][column];
        this.animations = animation;
        this.animSpeed = animSpeed;
    }

    //Used to check what type is the wall and to change his corresponding character
    private String displayWall(Box wallBox) {
        int line = wallBox.getLine();
        int column = wallBox.getColumn();
        boolean leftBox = false;
        boolean upBox = false;
        boolean rightBox = false;
        boolean downBox = false;
        try {
            leftBox = grid[line][column - 1].compareType("wall");
        } catch (Exception e) {
        }
        try {
            rightBox = grid[line][column + 1].compareType("wall");
        } catch (Exception e) {}
        try {
            upBox = grid[line - 1][column].compareType("wall");
        } catch (Exception e) {}
        try {
            downBox = grid[line + 1][column].compareType("wall");
        } catch (Exception e) {}
        if (!upBox && !downBox) {
            if (!rightBox) {
                return HORIZONTAL + " ";
            }
            return HORIZONTAL + HORIZONTAL;
        } else if (!leftBox && !rightBox) {
            return VERTICAL + " ";
        } else if ((rightBox && downBox) && (!upBox && !leftBox)) {
            return CORNER_TL + HORIZONTAL;
        } else if ((rightBox && upBox) && (!downBox && !leftBox)) {
            return CORNER_BL + HORIZONTAL;
        } else if ((leftBox && downBox) && (!upBox && !rightBox)) {
            return CORNER_TR + " ";
        } else if ((upBox && leftBox) && (!rightBox && !downBox)) {
            return CORNER_BR + " ";
        } else if (!leftBox) {
            return TEE_LEFT + HORIZONTAL;
        } else if (!rightBox) {
            return TEE_RIGHT + " ";
        } else if (!downBox) {
            return TEE_BOTTOM + HORIZONTAL;
        } else if (!upBox) {
            return TEE_TOP + HORIZONTAL;
        } else {
            return CROSS + HORIZONTAL;
        }
}


    //Display the maze, each Box contains information about his "color", this functions display all box in order and add
    //a border all around the maze
    public void display() {
        clear();
        System.out.println("\n\n\n");
        System.out.print(CORNER_TL);
        for (int i = 1; i < grid[0].length; i++) {
            System.out.print(HORIZONTAL + HORIZONTAL);
        }
        System.out.print(CORNER_TR);
        System.out.println();
        for (int line = 1; line < this.grid.length; line++) {
            System.out.print(VERTICAL);
            for (int column = 1; column < this.grid[line].length; column++) {
                if (this.grid[line][column].compareType("path")) {
                    System.out.print(this.grid[line][column].getColor());
                }
                else {
                    System.out.print(displayWall(this.grid[line][column]));
                }
            }
            System.out.print(VERTICAL);
            System.out.println();
        }
        System.out.print(CORNER_BL);
        for (int i = 1; i < grid[0].length; i++) {
            System.out.print(HORIZONTAL + HORIZONTAL);
        }
        System.out.print(CORNER_BR);

        System.out.println();
    }


    //Create the maze as a grid first (each path is surrounded by 4 walls or by nothing)
    //Then use the Walk() and Hunt() functions, the walk functions start from a random point and will break walls around
    //him to create a way until he is blocked, then the Hunt() will find a "path" Box that's still not blocked and start to walk
    public void create() {
        int index = 1;
        for (int line = 1; line < this.grid.length; line++) {
            for (int column = 1; column < this.grid[line].length; column++) {
                Box box = new Box(line, column, index);
                index += 1;
                if (line % 2 != 0 && column % 2 != 0) {
                    box.changeType();
                }
                this.grid[line][column] = box;
            }
        }
        Box actualBox = grid[1][1];

        while (!created) {
            walk(actualBox);
            actualBox = hunt();
        }
        clear();
    }

    //This function will start from a given Box and then go around the maze while breaking walls while the Box isn't blocked
    //The dictionary dictX and dictY are used to determine the 4 possible directions from the Box and try them in a random way
    //Each walk will be different from another due to the randomization of the list("S", "E", "N", "W") that contains
    //the possibilities of direction
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

        ArrayList<String> list = new ArrayList<>(List.of("S", "E", "N", "W"));
        boolean blocked = false;
        while (!blocked) {
            boolean moved = false;
            Collections.shuffle(list);


            for (int i = 0; i < 4; i++) {
                int dline = (int) dictX.get(list.get(i));
                int dcolumn = (int) dictY.get(list.get(i));
                int line = actualBox.getLine();
                int column = actualBox.getColumn();
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
                        if (animations) {
                            clear();
                            display();
                            try {
                                Thread.sleep(animSpeed);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (!moved) {
                grid[actualBox.getLine()][actualBox.getColumn()].setBlocked(true);
                blocked = true;
            }
        }
    }

    //This function look for a visited Box that's still not blocked and then return it to the Walk()
    //If Hunt() can't find a visited cell that's not blocked he changes the global parameter created that will break the
    //construction loop
    private Box hunt() {
        for (int line = 1; line < this.grid.length; line++) {
            for (int column = 1; column < this.grid[line].length; column++) {
                if (grid[line][column].getType().equals("path") && grid[line][column].isVisited()) {
                    if (!grid[line][column].isBlocked()) {
                        return grid[line][column];
                    }
                }
            }
        }
        created = true;
        return null;
    }

    //Clear the screen...
    private void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    //Place the starting point and the ending point on the maze
    public void gameStart() {
        placeEmoji("▪ ");
        placeEmoji("▫ ");
    }

    //Place the "emoji" given in a random position
    private void placeEmoji(String emoji) {
        Random random = new Random();
        int line;
        int column;
        boolean chosed = false;
        while (!chosed) {
            line = random.nextInt(grid[0].length - 1) + 1;
            column = random.nextInt(grid.length - 1) + 1;

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == line && j == column) {
                        if (grid[line][column].compareType("path")) {
                            grid[line][column].setColor(emoji);
                            if (emoji.equals("▪ ")){
                                lineStart = line;
                                columnStart = column;
                            }
                            chosed = true;
                        }
                    }
                }
            }
        }
    }

    public Box[][] getGrid() {
        return grid;
    }

    public void setGrid(Box[][] grid) {
        this.grid = grid;
    }

    public void Reset(){
        int W = grid[0].length;
        int H = grid.length;
        for (int line = 1; line < H ; line++) {
            for (int column = 1; column < W ; column++) {
                if (grid[line][column].getColor().equals("▪ ")) {
                    grid[line][column].setColor("  ");
                    column = W;
                    line = H;
                }
            }
        }
        grid[lineStart][columnStart].setColor("▪ ");
    }

    public void setAnimSpeed(int animSpeed) {
        this.animSpeed = animSpeed;
    }
}