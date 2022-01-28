package com.example.labyrinth;

public class Game {

    public static void main(String[] args) {

        int choice = 0;
        while (choice != 5) {
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
            else if (choice == 4){
                Functions.Score();
            }
        }
    }
}
