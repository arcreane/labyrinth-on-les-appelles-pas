package com.example.labyrinth;

import com.example.labyrinth.classes.Box;
import com.example.labyrinth.classes.Finder;
import com.example.labyrinth.classes.Labyrinth;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Functions {
    public static Scanner sc = new Scanner(System.in);
    private static boolean animation = false;
    private static Labyrinth labyrinth = new Labyrinth(0, 0, animation);
    private static int turn = 0;
    private static boolean exitFund = false;

    public static int Menu(){
        clear();
        labyrinth = new Labyrinth(11, 31, animation);
        labyrinth.create();
        labyrinth.display();
        System.out.println("""
                MENU :\s
                1 : Labyrinth Generation\s
                2 : Rules\s
                3 : Options\s
                4 : Leave\s
                """);
        return sc.nextInt();
    }

    public static void Play() {
        clear();
        System.out.println("Please choose the size of your labyrinth :");
        System.out.print("Height : ");
        int height = sc.nextInt();
        System.out.print("Width : ");
        int width = sc.nextInt();
        labyrinth = new Labyrinth(height, width, animation);
        labyrinth.create();
        labyrinth.gameStart();
        labyrinth.display();
        int choice;
        boolean resolved = false;
        while (!resolved) {
            if (exitFund){
                break;
            }
            System.out.println("Use z-q-s-d to move and press enter; press 0 to resolve");
            String Strchoice = sc.next();
            if (Strchoice.equals("0")) {
                labyrinth.setGrid(Finder.bfs(labyrinth.getGrid(), animation));
                labyrinth.display();
                resolved = true;

            } else if (Strchoice.equals("z") || Strchoice.equals("q") || Strchoice.equals("s") || Strchoice.equals("d")) {
                Move(labyrinth.getGrid(), Strchoice);
            }
        }
        while (true) {
            System.out.println("""
                    Play again ?\s
                    1 : With the same size\s
                    2 : With a different size
                    3 : Menu""");
            choice = sc.nextInt();
            if (choice == 3) {
                break;
            } else if (choice == 1) {
                labyrinth = new Labyrinth(height, width, animation);
                labyrinth.create();
                labyrinth.gameStart();
                labyrinth.display();
            } else {
                System.out.println("Please choose the size of your labyrinth :");
                System.out.print("Height : ");
                height = sc.nextInt();
                System.out.print("Width : ");
                width = sc.nextInt();
                labyrinth = new Labyrinth(height, width, animation);
                labyrinth.create();
                labyrinth.gameStart();
                labyrinth.display();
            }
            resolved = false;
            while (!resolved) {
                System.out.println("Use z-q-s-d to move and press enter; press 0 to resolve");
                String Strchoice = sc.next();
                if (Strchoice.equals("0")) {
                    labyrinth.setGrid(Finder.bfs(labyrinth.getGrid(), animation));
                    labyrinth.display();
                    resolved = true;
                } else if (Strchoice.equals("z") || Strchoice.equals("q") || Strchoice.equals("s") || Strchoice.equals("d")) {
                    Move(labyrinth.getGrid(), Strchoice);
                }
            }
        }
    }

    public static void Options(){
        String anim;
        if (animation)
            anim = "(Currently Enabled)";
        else
            anim = "(Currently Disabled)";
        clear();
        System.out.println("OPTIONS : \n" +
                "1 : Disable/Enable Labyrinth Animations " + anim + " \n" +
                "2 : Return to menu" +
                "");
        int choice = sc.nextInt();
        if (choice == 1){
            animation = !animation;
        }
    }

    public static void Move(Box[][] grid, String choice){
        Map dictLine = new HashMap();
        dictLine.put("S", 1);
        dictLine.put("E", 0);
        dictLine.put("W", 0);
        dictLine.put("N", -1);
        Map dictColumn = new HashMap();
        dictColumn.put("S", 0);
        dictColumn.put("E", 1);
        dictColumn.put("W", -1);
        dictColumn.put("N", 0);
        Map dictMove = new HashMap();
        dictMove.put("s", "S");
        dictMove.put("q", "W");
        dictMove.put("d", "E");
        dictMove.put("z", "N");

        int W = grid[0].length;
        int H = grid.length;
        Box actualBox = new Box(0,0,0);
        for (int line = 1; line < H - 2; line++) {
            for (int column = 1; column < W - 2; column++) {
                if (grid[line][column].getColor().equals("▪ ")) {
                    actualBox = grid[line][column];
                    column = W;
                    line = H;
                }
            }
        }
        int dline = (int) dictLine.get(dictMove.get(choice));
        int dcolumn = (int) dictColumn.get(dictMove.get(choice));
        int line = actualBox.getLine();
        int column = actualBox.getColumn();
        if ((line + dline < grid[line].length && line + dline >= 1)
                && (column + dcolumn < grid[line].length && column + dcolumn >= 1)) {
            if (grid[line + dline][column + dcolumn].compareType("wall")) {
                clear();
                labyrinth.display();
                System.out.println("You can't go in a wall");
            }
            else if (grid[line + dline][column + dcolumn].getColor().equals("▫ ")){
                clear();
                labyrinth.display();
                System.out.println("Wonderful You found the exit within " + turn + " turn !");
                exitFund = true;
            }
            else {
                clear();
                turn += 1;
                grid[line + dline][column + dcolumn].setColor("▪ ");
                grid[line][column].setColor("  ");
                labyrinth.setGrid(grid);
                labyrinth.display();
            }
        }
    }



    public static void Rules(){
        Labyrinth labyrinthRule = new Labyrinth(10, 30, false);
        labyrinthRule.create();
        labyrinthRule.gameStart();
        labyrinthRule.display();
        System.out.println("""
                    Rules :\s
                        You are the white dot (▪) and have to go to the empty dot (▫)\s
                        You will need to go through the Labyrinth and find your path\s
                        You need to use z-q-s-d (z - up / s - down / q - left / d - right)\s
                        If you can't find the exit you can surrender and let the computer finish it for you\s
                """);
        System.out.println("Press any key to continue");
        sc.next();
        Finder.bfs(labyrinthRule.getGrid(), true);
    }
    private static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
