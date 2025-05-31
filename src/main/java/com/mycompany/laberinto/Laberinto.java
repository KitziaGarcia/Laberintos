package com.mycompany.laberinto;


public class Laberinto {

    public static void main(String[] args) {
        GeneradorDeLaberinto generador = new GeneradorDeLaberinto();
        int dificultad = 1;

        int[][] matriz = generador.generarMatriz(dificultad);
        
        LaberintoGrafo grafoLaberinto = generador.generarLaberinto(matriz);

        System.out.println("Laberinto generado:");
        generador.dibujarMatrizConsola(matriz, grafoLaberinto);

        System.out.println("\nRecorrido del grafo en consola:");
        generador.recorrerGrafoConsola(grafoLaberinto);

        System.out.println("\nFin del programa");
    }
}
