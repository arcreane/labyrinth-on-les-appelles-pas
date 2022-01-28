package com.example.labyrinth;

import com.example.labyrinth.classes.Box;
import com.example.labyrinth.classes.Finder;
import com.example.labyrinth.classes.Labyrinth;

import java.io.*;
import java.util.*;

public class Functions {
    private static int animationSpeed = 50;
    public static Scanner sc = new Scanner(System.in);
    private static boolean animation = false;
    private static Labyrinth labyrinth = new Labyrinth(0, 0, animation, animationSpeed);
    private static int turn = 0;
    private static boolean exitFund = false;
    private static boolean automated = false;
    private static boolean labyrinthImported = false;

    //Principal Menu, from here the player can start a game, modify his options, see the rules or the score
    public static int Menu(){
        clear();
        labyrinth = new Labyrinth(12, 30, animation, animationSpeed);
        labyrinth.create();
        clear();
        labyrinth.display();
        System.out.println("""
                MENU :\s
                1 : Labyrinth Generation\s
                2 : Rules\s
                3 : Options\s
                4 : Scores\s
                5 : Leave\s
                """);

        return sc.nextInt();
    }

    //Functions that let the player play a Maze and play again at the end if he wants
    public static void Play() {
        int height = 0;
        int width = 0;
        exitFund = false;
        if (labyrinthImported){
            height = labyrinth.getGrid().length;
            width = labyrinth.getGrid()[1].length;
            labyrinthImported = false;
            labyrinth.display();
        }
        int choice;
        boolean resolved = false;
        while (!resolved) {
            if (exitFund){
                break;
            }
            System.out.println("Turn : " + turn + "\nUse z-q-s-d to move and press enter | press 0 to resolve");
            String Strchoice = sc.next();
            if (Strchoice.equals("0")) {
                labyrinth.setGrid(Finder.bfs(labyrinth.getGrid(), animation, animationSpeed));
                labyrinth.display();
                resolved = true;
                automated = true;

            } else if (Strchoice.equals("z") || Strchoice.equals("q") || Strchoice.equals("s") || Strchoice.equals("d")) {
                Move(labyrinth.getGrid(), Strchoice);
            }
        }
        while (true) {
            String score = "";
            if (!automated){
                score = "4 : Save this Maze";
            }
            System.out.println("""
                    Play again ?\s
                    1 : With the same size\s
                    2 : With a different size\s
                    3 : Menu\s""" +
                    score);
            choice = sc.nextInt();
            if (choice == 3) {
                break;
            } else if (choice == 1) {
                labyrinth = new Labyrinth(height, width, animation, animationSpeed);
                labyrinth.create();
                labyrinth.gameStart();
                labyrinth.display();
            }
            else if (!automated && choice==4){
                System.out.println("Please enter a Pseudo");
                String pseudo = sc.next();
                write(pseudo + "-t" + turn + "-l" + labyrinth.getGrid().length + "-c" + labyrinth.getGrid()[1].length);
                automated = true;
                continue;
            } else {
                MenuMap();
                Play();
                return;
            }
            resolved = false;
            while (!resolved) {
                System.out.println("Use z-q-s-d to move and press enter; press 0 to resolve");
                String Strchoice = sc.next();
                if (Strchoice.equals("0")) {
                    labyrinth.setGrid(Finder.bfs(labyrinth.getGrid(), animation, animationSpeed));
                    labyrinth.display();
                    resolved = true;
                } else if (Strchoice.equals("z") || Strchoice.equals("q") || Strchoice.equals("s") || Strchoice.equals("d")) {
                    Move(labyrinth.getGrid(), Strchoice);
                }
            }
        }
    }

    public static void Options(){
        boolean optionDone = false;
        while (!optionDone) {
            StringBuilder anim;
            if (animation)
                anim = new StringBuilder("(Currently Enabled)");
            else
                anim = new StringBuilder("(Currently Disabled)");
            clear();
            int animationSpeedDisplay = animationSpeed;
            StringBuilder animSpeed = new StringBuilder("Current speed : ");
            for (int i = 0; i < 10; i++) {
                if (animationSpeedDisplay > 0) {
                    animationSpeedDisplay -= 10;
                    animSpeed.append("▪");
                } else {
                    animSpeed.append("▫");
                }
            }
            System.out.println("OPTIONS : \n" +
                    "1 : Disable/Enable Labyrinth Animations " + anim + " \n" +
                    "2 : Change the animation speed - " + animSpeed + "\n" +
                    "3 : Return to menu" +
                    "");
            int choice = sc.nextInt();
            if (choice == 1) {
                animation = !animation;
            } else if (choice == 2) {
                boolean choiceDone = false;
                while (!choiceDone) {
                    clear();
                    animationSpeedDisplay = animationSpeed;
                    System.out.print("Animation speed : ");
                    for (int i = 0; i < 10; i++) {
                        if (animationSpeedDisplay < 100) {
                            animationSpeedDisplay += 10;
                            System.out.print("▪");
                        } else {
                            System.out.print("▫");
                        }
                    }

                    System.out.println("\nPress a or d to change the speed and q to quit");
                    String strChoice = sc.next();
                    switch (strChoice) {
                        case "a":
                            if (animationSpeed < 100) {
                                animationSpeed += 10;
                            } else
                                System.out.println("You have already reached the minimum value");
                            break;
                        case "d":
                            if (animationSpeed > 10) {
                                animationSpeed -= 10;
                                if (animationSpeed < 10)
                                    animationSpeed = 1;
                            } else
                                System.out.println("You have already reached the maximum value");

                            break;
                        case "q":
                            choiceDone = true;
                            break;
                        default:
                            System.out.println("Please enter 'a', 'd' or 'q'");
                            break;
                    }
                }
            }
            else if (choice == 3){
                optionDone = true;
            }
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
        for (int line = 1; line < H; line++) {
            for (int column = 1; column < W; column++) {
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
        Labyrinth labyrinthRule = new Labyrinth(10, 30, false, animationSpeed);
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
        Finder.bfs(labyrinthRule.getGrid(), true, animationSpeed);
    }
    private static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }



    static Labyrinth read(String filePath){
        String path = "/Users/celian/IdeaProjects/labyrinth/src/main/java/com/example/labyrinth/mazeSave/";
        File file =  new File(path + filePath) ;

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Labyrinth m = null;
        try {
            m = (Labyrinth) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return m;
    }

    static void write(String filePath){

        String path = "/Users/celian/IdeaProjects/labyrinth/src/main/java/com/example/labyrinth/mazeSave/";
        File file =  new File(path + filePath) ;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));}
        catch (IOException e) {e.printStackTrace();}
        try {
            oos.writeObject(labyrinth) ;}
        catch (IOException e) {e.printStackTrace();}
    }


    public static void Score(){
        File dir  = new File("/Users/celian/IdeaProjects/labyrinth/src/main/java/com/example/labyrinth/mazeSave");
        File[] list = dir.listFiles();
        ArrayList<String> mazeList = new ArrayList<>();
        int i = 1;
        for(File item : list) {
            if (item.isFile()) {
                String turn = item.getName().split("-")[1].split("t")[1];
                String name = item.getName().split("-")[0];
                String line = item.getName().split("-")[2].split("l")[1];
                String column = item.getName().split("-")[3].split("c")[1];

                System.out.println(i + " : Maze of " + line + " lines on "+ column + " column solved by " + name + " within " + turn + " turn");
                mazeList.add(item.getName());
                i ++;
            }
        }

        System.out.println("Choose a Maze you want to try or press 0 to go back");
        int choice = sc.nextInt();
        if (choice!=0){
            if (choice <= mazeList.size()){
                labyrinth = read(mazeList.get(choice-1));
                labyrinthImported = true;
                labyrinth.Reset();
                Play();
            }
        }
    }


    public static void MenuMap(){
        boolean choiceDone = false;
        while(!choiceDone) {
            clear();
            System.out.println("Please choose the size of your labyrinth :");
            System.out.println("1 :small" +
                    "\n2 :medium" +
                    "\n3 :large" +
                    "\n4 :custom");
            int choice = sc.nextInt();
            int height = 0;
            int width = 0;
            switch (choice) {
                case 1:
                    height = 20;
                    width = 20;
                    choiceDone = true;
                    break;
                case 2:
                    height = 30;
                    width = 50;
                    choiceDone = true;
                    break;
                case 3:
                    height = 40;
                    width = 80;
                    choiceDone = true;
                    break;
                case 4:
                    System.out.println("Please choose the size of your labyrinth :");
                    System.out.print("Height : ");
                    height = sc.nextInt();
                    System.out.print("Width : ");
                    width = sc.nextInt();
                    choiceDone = true;
                    break;
                default:
                    System.out.println("Please choose a valid option");
            }
            labyrinth = new Labyrinth(height, width, animation, animationSpeed);
            labyrinth.create();
            labyrinth.gameStart();
            labyrinth.display();
        }
    }
}
