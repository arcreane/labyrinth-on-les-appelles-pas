package com.example.labyrinth;

import com.example.labyrinth.classes.Box;
import com.example.labyrinth.classes.Labyrinth;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Labyrinth labyrinth = new Labyrinth(30, 60);
        labyrinth.create();
        labyrinth.display();
    }

}
