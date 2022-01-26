package com.example.labyrinth.classes;

import java.util.*;

public class Finder {

    /*Function that use the B.F.S algorithm ( Breadth-first search) to find every cell linked
    to the starting point and then find the path to the end point.
    This algorithm use 5 steps :
    */
    public static Box[][] bfs(Box[][] grid) {
        //Variable initializations
        int actualIndex = 0;
        int W = grid[0].length;
        int H = grid.length;
        boolean exitFound = false;


        //Object creations
        Box actualBox;
        Map<Integer, Box> dictBox = new HashMap<>();
        Map dictX = new HashMap();
        Map dictY = new HashMap();
        Map<Integer, Integer> P = new HashMap<>();
        ArrayList<String> list = new ArrayList<>(List.of("S", "E", "N", "W"));
        List<Integer> Discover = new ArrayList<>(List.of(actualIndex));
        List<Integer> Closed = new ArrayList<>();
        Queue<Integer> Q = new ArrayDeque<>();
        Stack pile = new Stack();


        //Dictionary filling
        dictY.put("S", -1);
        dictY.put("E", 0);
        dictY.put("W", 0);
        dictY.put("N", 1);
        dictX.put("S", 0);
        dictX.put("E", 1);
        dictX.put("W", -1);
        dictX.put("N", 0);


        //1 -- Use the grid to look for the starting point and register it as the "actualBox"
        for (int line = 1; line < H - 2; line++) {
            for (int column = 1; column < W - 2; column++) {
                if (grid[line][column].getColor().equals("▪ ")) {
                    actualBox = grid[line][column];
                    actualIndex = actualBox.getIndex();
                    column = W;
                    line = H;
                }
            }
        }
        P.put(actualIndex, 0);
        Q.add(actualIndex);

        //2 -- Put every "path" box in a dictionary with their index as key
        for (int line = 1; line < grid.length; line++) {
            for (int column = 1; column < grid[line].length; column++) {
                if (grid[line][column].compareType("path")) {
                    dictBox.put(grid[line][column].getIndex(), grid[line][column]);
                }
            }

        }

        //3 -- Check all the "sons" of the starting cell, then check all their "sons" and continue like that until
        //the ending point is found
        //This loop is the biggest of the BFS because it needs many checks (verify how many other
        // "path" cell one have/ if the border of the labyrinth isn't crossed/ if the neighbour cell is a "path" and not
        // a "wall"/ verify if the cell is the ending one or not)
        while (!Q.isEmpty() && !exitFound) {
            //variable initialization
            actualIndex = Q.peek();
            actualBox = dictBox.get(actualIndex);
            int line = actualBox.getLine();
            int column = actualBox.getColumn();

            //loop to find all the possible neighbour (4 possible) and register them if they are "path" cell
            for (int i = 0; i < 4; i++) {
                //variable initialization
                int dline = (int) dictX.get(list.get(i));
                int dcolumn = (int) dictY.get(list.get(i));
                //Check if this neighbour does exist (the cell can be on a border of the labyrinth
                if ((line + dline < grid.length && line + dline >= 1)
                        && (column + dcolumn < grid[line].length && column + dcolumn >= 1)) {
                    //Verify if the cell isn't already discovered and registered
                    if (!Discover.contains(grid[line + dline][column + dcolumn].getIndex())) {
                        //Verify if the cell is a "path" cell
                        if (grid[line + dline][column + dcolumn].compareType("path")) {
                            //Register the cell and add it to the queue so the loop can look for her neighbour later
                            P.put(grid[line + dline][column + dcolumn].getIndex(), actualIndex);
                            Q.add(grid[line + dline][column + dcolumn].getIndex());
                            Discover.add(grid[line + dline][column + dcolumn].getIndex());
                            //verify if the cell is the ending cell, if yes the loop will stop, this cell will be
                            // registered and the BFS continue
                            if (grid[line + dline][column + dcolumn].getColor().equals("▫ ")) {
                                actualIndex = grid[line + dline][column + dcolumn].getIndex();
                                exitFound = true;
                            }
                        }
                    }
                }
            }
            //Remove the checked cell from the list
            Closed.add(Q.poll());
        }

        //4 -- Put the path from the ending point to the starting point into a pile (the pile is used because when it will be
        //read the order will be reversed : starting point -> ending point)
        //The path is stocked with the index of all cell in order
        while (actualIndex != 0) {
            int parentIndex = P.get(actualIndex);
            pile.push(parentIndex);
            actualIndex = parentIndex;
        }

        //A Labyrinth is created and the grid is attributed to him
        Labyrinth labyrinth = new Labyrinth(0, 0);
        labyrinth.setGrid(grid);

        //The first element of the pile is popped out because it is the 0 (the father of the starting cell is registered as 0)
        pile.pop();

        //5 -- The last loop will draw the way on the console by changing a box, refreshing the labyrinth grid and
        // displaying it, with a 100ms interval et a clear
        while (!pile.isEmpty()) {
            int x = (int) pile.pop();
            actualBox = dictBox.get(x);
            //Stop before replacing the ending point
            if (actualBox.getColor().equals("▫ ")) {
                break;
            }
            int line = actualBox.getLine();
            int column = actualBox.getColumn();
            grid[line][column].setColor("▪ ");
            labyrinth.setGrid(grid);
            //Clear and redraw
            clear();
            labyrinth.display();
            //Pause between each draw
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return grid;
    }

    //Clear... WOW
    private static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}