package com.example.labyrinth;

import com.example.labyrinth.classes.Labyrinth;
import com.example.labyrinth.classes.Finder;

import java.util.Scanner;

public class Game {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice = 0;
        while (choice != 4) {
            choice = Functions.Menu();

            if (choice == 1) {
                Functions.Play();
            }
            else if (choice == 2){
                Functions.Rules();
            }
            else if (choice == 3) {
                Functions.Options();
            }
        }
    }
}
