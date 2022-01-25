package com.example.labyrinth;

public class main {

    public static void affichageTableau(String tableau[][]) {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(tableau[i][j] + " " );
            }
            System.out.println("");
        }
    }
    //⬛
    //⬜
    // Permet de créer un tableau 'vide' avec les positions de départ des 2 joueurs
    public static String[][] tableZero() {
        String[][] tableau = new String[10][11];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 11; j++) {
                tableau[i][j] = "⬛";
            }
        }
        return tableau;
    }


    public static void walk(String[][] tab){

    }

}
