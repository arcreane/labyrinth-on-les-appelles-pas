package com.example.labyrinth;

import com.example.labyrinth.classes.Labyrinth;
import com.example.labyrinth.classes.Finder;

import java.util.Scanner;

public class Game {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Labyrinth labyrinth = new Labyrinth(11, 31);
        labyrinth.create();
        labyrinth.display();
        System.out.println("\n MENU : \n" +
                "1 : Labyrinth Generation \n" +
                "2 : Score tab \n" +
                "3 : Leave \n");
        int choice = sc.nextInt();
        if (choice == 1) {
            System.out.println("Please choose the size of your labyrinth :");
            System.out.print("Height : ");
            int height = sc.nextInt();
            System.out.print("Width : ");
            int width = sc.nextInt();
            labyrinth = new Labyrinth(height, width);
            labyrinth.create();
            labyrinth.gameStart();
            labyrinth.display();

            System.out.println("Résoudre ? 0 : oui");
            choice = sc.nextInt();
            if (choice == 0){
                labyrinth.setGrid(Finder.bfs(labyrinth.getGrid()));
                labyrinth.display();

            }

        }


    }
}
