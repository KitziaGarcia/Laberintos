package com.mycompany.laberinto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class GeneradorDeLaberinto {
    private ArrayList<ArrayList<Nodo>> listaAdyacencia;


    public GeneradorDeLaberinto() {
        listaAdyacencia = new ArrayList<>();
    }

    public int[][] generarMatriz(int dificultad) {
        int filas, columnas;
        switch (dificultad) {
            case 1: // Fácil
                filas = 7;
                columnas = 7;
                break;
            case 2: // Medio
                filas = 15;
                columnas = 15;
                break;
            case 3: // Difícil
                filas = 25;
                columnas = 25;
                break;
            case 4: // Muy Difícil
                filas = 35;
                columnas = 35;
                break;
            default:
                filas = 11;
                columnas = 11;
                break;
        }


        int[][] celdas = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = 1; 
            }
        }

        Stack<int[]> stack = new Stack<>();
        int startCol = 1; 
        int startRow = 1;

        celdas[startRow][startCol] = 0; 
        stack.push(new int[]{startCol, startRow});

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int currentCol = current[0];
            int currentRow = current[1];

            List<int[]> neighborsDirs = new ArrayList<>();
            
            int[][] directions = {{0, -2, 0, -1}, {0, 2, 0, 1}, {-2, 0, -1, 0}, {2, 0, 1, 0}};

            for (int[] dir : directions) {
                int nextCol = currentCol + dir[0];
                int nextRow = currentRow + dir[1];
                
                if (nextRow > 0 && nextRow < filas - 1 && nextCol > 0 && nextCol < columnas - 1 && celdas[nextRow][nextCol] == 1) {
                    neighborsDirs.add(dir);
                }
            }

            if (!neighborsDirs.isEmpty()) {
                Collections.shuffle(neighborsDirs); 
                int[] chosenDir = neighborsDirs.get(0);
                
                int wallCol = currentCol + chosenDir[2];
                int wallRow = currentRow + chosenDir[3];
                celdas[wallRow][wallCol] = 0; 

                int nextCol = currentCol + chosenDir[0];
                int nextRow = currentRow + chosenDir[1];
                celdas[nextRow][nextCol] = 0; 
                stack.push(new int[]{nextCol, nextRow});
            } else {
                stack.pop(); 
            }
        }
        return celdas;
    }


    public LaberintoGrafo generarLaberinto(int[][] matrizCeldas) {
        if (matrizCeldas == null || matrizCeldas.length == 0 || matrizCeldas[0].length == 0) {
            LaberintoGrafo grafoVacio = new LaberintoGrafo(1,1);
            grafoVacio.getNodo(0,0).setEsPared(false); 
            grafoVacio.setNodoInicio(grafoVacio.getNodo(0,0));
            grafoVacio.setNodoFin(grafoVacio.getNodo(0,0));
            return grafoVacio;
        }

        int filas = matrizCeldas.length;
        int columnas = matrizCeldas[0].length;
        LaberintoGrafo grafo = new LaberintoGrafo(filas, columnas);

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                //System.out.println("NODO: " + grafo.getNodo(i,j));
            }
        }

        int c1 = 0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Nodo actual = grafo.getNodo(i, j);
                if (matrizCeldas[i][j] == 1) { 
                    actual.setEsPared(true);
                } else { 
                    actual.setEsPared(false);
                    if (i > 0 && matrizCeldas[i - 1][j] == 0) {
                        Nodo vecinoArriba = grafo.getNodo(i - 1, j);
                        actual.conectarVecino(vecinoArriba);
                        vecinoArriba.conectarVecino(actual);
                    }
                    if (j > 0 && matrizCeldas[i][j - 1] == 0) {
                        Nodo vecinoIzquierda = grafo.getNodo(i, j - 1);
                        actual.conectarVecino(vecinoIzquierda);
                        vecinoIzquierda.conectarVecino(actual);
                    }

                    if (i < filas - 1 && matrizCeldas[i + 1][j] == 0) {
                        Nodo vecinoAbajo = grafo.getNodo(i + 1, j);
                        actual.conectarVecino(vecinoAbajo);
                        vecinoAbajo.conectarVecino(actual);
                    }

                    if (j < columnas - 1 && matrizCeldas[i][j + 1] == 0) {
                        Nodo vecinoDerecha = grafo.getNodo(i, j + 1);
                        actual.conectarVecino(vecinoDerecha);
                        vecinoDerecha.conectarVecino(actual);
                    }
                }
                listaAdyacencia.add(actual.getVecinos());
                System.out.println(actual + "           LISTA: " + actual.getVecinos());
            }
        }

        // Establecer inicio y fin
        Nodo nodoInicio = null;
        // Buscar primera celda no pared desde arriba-izquierda
        for(int r=0; r<filas; r++){
            for(int c=0; c<columnas; c++){
                if(!grafo.getNodo(r,c).isEsPared()){
                    nodoInicio = grafo.getNodo(r,c);
                    break;
                }
            }
            if(nodoInicio != null) break;
        }
        
        Nodo nodoFin = null;
        // Buscar primera celda no pared desde abajo-derecha
         for(int r=filas-1; r>=0; r--){
            for(int c=columnas-1; c>=0; c--){
                Nodo candidatoFin = grafo.getNodo(r,c);
                if(!candidatoFin.isEsPared() && candidatoFin != nodoInicio){
                    nodoFin = candidatoFin;
                    break;
                }
            }
            if(nodoFin != null) break;
        }

        if (nodoInicio == null) { 
            nodoInicio = grafo.getNodo(0,0); 
            if(nodoInicio != null) nodoInicio.setEsPared(false); 
        }
         if (nodoFin == null || nodoFin == nodoInicio) {
           
            if (nodoInicio != null && nodoInicio.getY() == 0 && nodoInicio.getX() == 0) {
                 nodoFin = grafo.getNodo(filas-1, columnas-1);
            } else {
                 nodoFin = grafo.getNodo(0,0);
            }
            if(nodoFin != null) {
                nodoFin.setEsPared(false); 
                if (nodoFin == nodoInicio && (filas > 1 || columnas > 1)) {
                    for(int r=filas-1; r>=0; r--){
                        for(int c=columnas-1; c>=0; c--){
                             Nodo candidatoAlternativo = grafo.getNodo(r,c);
                             if(!candidatoAlternativo.isEsPared() && candidatoAlternativo != nodoInicio){
                                 nodoFin = candidatoAlternativo;
                                 r = -1; 
                                 break;
                             }
                        }
                    }
                }
            }
        }
        if (filas == 1 && columnas == 1 && nodoInicio != null) {
            nodoFin = nodoInicio;
        }
        
        if(nodoInicio != null) grafo.setNodoInicio(nodoInicio);
        if(nodoFin != null) grafo.setNodoFin(nodoFin);
        else if (nodoInicio != null) grafo.setNodoFin(nodoInicio); 

        return grafo;
    }

   
    public void recorrerGrafoConsola(LaberintoGrafo grafo) {
        if (grafo == null || grafo.getNodoInicio() == null) {
            System.out.println("Error! El grafo es nulo o no tiene un nodo de inicio ");
            return;
        }

        System.out.println("Recorrido del grafo ");
        System.out.println("Nodo de inicio: (" + grafo.getNodoInicio().getX() + ", " + grafo.getNodoInicio().getY() + ")");
        if (grafo.getNodoFin() != null) {
            System.out.println("Nodo de fin: (" + grafo.getNodoFin().getX() + ", " + grafo.getNodoFin().getY() + ")");
        }
        System.out.println("-------------------------------------");

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        Nodo nodoActual = grafo.getNodoInicio();
   
        grafo.reiniciarNodosParaBusqueda(); 
        nodoActual.setVisitado(true);

        while (true) {
            System.out.println("\nEstás en el nodo: (" + nodoActual.getX() + ", " + nodoActual.getY() + ")");

            if (nodoActual.isEsFin()) {
                System.out.println("Felicidades! Has llegado al fin ");
                break;
            }

            List<Nodo> vecinos = nodoActual.getVecinos();
            
            if (vecinos.isEmpty()) {
                System.out.println("Este nodo no tiene caminos directos ");
                if (nodoActual.getPadre() != null) {
                    System.out.println("Volviendo al nodo anterior ");
                    nodoActual = nodoActual.getPadre();
                    continue;
                } else {
                    System.out.println("No hay nodo anterior al cual volver ");
                    break;
                }
            }

            System.out.println("Caminos disponibles:");
            for (int i = 0; i < vecinos.size(); i++) {
                Nodo vecino = vecinos.get(i);
                System.out.println((i + 1) + ": (" + vecino.getX() + ", " + vecino.getY() + ")" + (vecino.isVisitado() ? " [Ya Visitado]" : ""));
            }
            System.out.println("0: Salir del recorrido");
            System.out.print("Elige un camino para moverte o 0 para salir: ");

            int eleccion = -1;
            if (scanner.hasNextInt()) {
                eleccion = scanner.nextInt();
            } else {
                System.out.println("Entrada no válida ");
                scanner.next(); 
                continue;
            }

            if (eleccion == 0) {
                System.out.println("Saliste del recorrido");
                break;
            }

            if (eleccion > 0 && eleccion <= vecinos.size()) {
                Nodo siguienteNodo = vecinos.get(eleccion - 1);
                
                    siguienteNodo.setPadre(nodoActual); 
                    nodoActual = siguienteNodo;
                    nodoActual.setVisitado(true);
               
            } else {
                System.out.println("Opción no válida ");
            }
        }
    }

    public void dibujarMatrizConsola(int[][] matriz, LaberintoGrafo grafo) {
        if (matriz == null || matriz.length == 0) {
            System.out.println("Matriz vacía ");
            return;
        }
        System.out.println("\nRepresentación del laberinto en consola:");
        int filas = matriz.length;
        int columnas = matriz[0].length;

        Nodo nodoInicio = (grafo != null) ? grafo.getNodoInicio() : null;
        Nodo nodoFin = (grafo != null) ? grafo.getNodoFin() : null;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                boolean esInicio = false;
                boolean esFin = false;

                if (nodoInicio != null && nodoInicio.getY() == i && nodoInicio.getX() == j) {
                    esInicio = true;
                }
                if (nodoFin != null && nodoFin.getY() == i && nodoFin.getX() == j) {
                    esFin = true;
                }

                if (esInicio) {
                    System.out.print(" S "); // Inicio
                } else if (esFin) {
                    System.out.print(" E "); // Fin
                } else if (matriz[i][j] == 0) {
                    System.out.print("   "); // Camino
                } else {
                    System.out.print("[#]"); // Pared
                }
            }
            System.out.println();
        }
        System.out.println("Leyenda: [S] = Inicio, [E] = Fin, [ ] = Camino, [#] = Pared");
    }

    public ArrayList<ArrayList<Nodo>> getListaAdyacencia() {
        return listaAdyacencia;
    }
}
