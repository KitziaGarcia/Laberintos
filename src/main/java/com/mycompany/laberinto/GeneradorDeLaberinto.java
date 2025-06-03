package com.mycompany.laberinto;

import java.lang.reflect.Array;
import java.util.*;

public class GeneradorDeLaberinto {
    private ArrayList<ArrayList<Nodo>> listaAdyacencia;


    public GeneradorDeLaberinto() {
        listaAdyacencia = new ArrayList<>();
    }

    public int[][] generarMatriz(int dificultad) {
        int filas, columnas;
        switch (dificultad) {
            case 1:
                filas = 7;
                columnas = 7;
                break;
            case 2:
                filas = 15;
                columnas = 15;
                break;
            case 3:
                filas = 25;
                columnas = 25;
                break;
            case 4:
                filas = 35;
                columnas = 35;
                break;
            default:
                filas = dificultad;
                columnas = dificultad;
                break;
        }

        int[][] celdas = new int[filas][columnas];
        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                celdas[i][j] = 1; 
            }
        }

        Stack<int[]> fila = new Stack<>();
        int inicioCol = 1;
        int inicioFil = 1;

        celdas[inicioFil][inicioCol] = 0;
        fila.push(new int[]{inicioCol, inicioFil});

        while(!fila.isEmpty()){
            int[] actual = fila.peek();
            int colActual = actual[0];
            int filaActual = actual[1];

            List<int[]> direccionesVecinos = new ArrayList<>();
            
            int[][] direcciones = {{0,-2,0,-1}, {0,2,0,1}, {-2,0,-1,0}, {2,0,1,0}};

            for(int[] dir : direcciones){
                int sigCol = colActual + dir[0];
                int sigFila = filaActual + dir[1];
                
                if(sigFila > 0 && sigFila < filas - 1 && sigCol > 0 && sigCol < columnas - 1 && celdas[sigFila][sigCol] == 1){
                    direccionesVecinos.add(dir);
                }
            }

            if(!direccionesVecinos.isEmpty()){
                Collections.shuffle(direccionesVecinos);
                int[] direccionElegida = direccionesVecinos.get(0);
                
                int paredCol = colActual + direccionElegida[2];
                int paredFila = filaActual + direccionElegida[3];
                celdas[paredFila][paredCol] = 0;

                int sigCol = colActual + direccionElegida[0];
                int sigFila = filaActual + direccionElegida[1];
                celdas[sigFila][sigCol] = 0;
                fila.push(new int[]{sigCol, sigFila});
            } else {
                fila.pop();
            }
        }
        return celdas;
    }

    public int[][] generarMatrizDesdeListaAdyacenciaParedes(Map<Integer, List<Integer>> listaAdyacencia, int indicador) {
        Set<Integer> nodos = new HashSet<>(listaAdyacencia.keySet());
        for(List<Integer> vecinos : listaAdyacencia.values()){
            if(vecinos != null){
                nodos.addAll(vecinos);
            }
        }

        if(!nodos.contains(0)){
            throw new IllegalArgumentException("La lista no tiene un nodo 0");
        }

        List<Integer> nodosOrdenados = new ArrayList<>(nodos);
        nodosOrdenados.remove(Integer.valueOf(0));
        Collections.sort(nodosOrdenados);
        nodosOrdenados.add(0, 0);

        Map<Integer, Integer> nodoAIndice = new HashMap<>();
        for(int i = 0; i < nodosOrdenados.size(); i++){
            nodoAIndice.put(nodosOrdenados.get(i), i);
        }

        int n = nodosOrdenados.size();
        int[][] matriz = new int[n][n];

        for(int i = 0; i < n; i++){
            Arrays.fill(matriz[i], 1);
        }

        for(Map.Entry<Integer, List<Integer>> i : listaAdyacencia.entrySet()){
            int nodo = i.getKey();
            Integer indiceNodo = nodoAIndice.get(nodo);

            if(indiceNodo == null) continue;

            List<Integer> vecinos = i.getValue();
            if(vecinos == null) continue;

            for(Integer vecino : vecinos){
                Integer indiceVecino = nodoAIndice.get(vecino);

                if(indiceVecino != null && indicador != 1){
                    matriz[indiceNodo][indiceVecino] = 0;
                    matriz[indiceVecino][indiceNodo] = 0;
                } else {
                    matriz[indiceNodo][indiceVecino] = 0;
                }
            }
        }
        return matriz;
    }

    public LaberintoGrafo generarLaberinto(int[][] matrizCeldas) {
        if(matrizCeldas == null || matrizCeldas.length == 0 || matrizCeldas[0].length == 0){
            LaberintoGrafo grafoVacio = new LaberintoGrafo(1,1);
            grafoVacio.getNodo(0,0).setEsPared(false); 
            grafoVacio.setNodoInicio(grafoVacio.getNodo(0,0));
            grafoVacio.setNodoFin(grafoVacio.getNodo(0,0));
            return grafoVacio;
        }

        int filas = matrizCeldas.length;
        int columnas = matrizCeldas[0].length;
        LaberintoGrafo grafo = new LaberintoGrafo(filas, columnas);

        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                System.out.println("NODOsssssssss: " + grafo.getNodo(i,j));
            }
        }

        int c1 = 0;
        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                Nodo actual = grafo.getNodo(i, j);
                if(matrizCeldas[i][j] == 1){
                    actual.setEsPared(true);
                } else { 
                    actual.setEsPared(false);
                    if(i > 0 && matrizCeldas[i - 1][j] == 0){
                        Nodo vecinoArriba = grafo.getNodo(i - 1, j);
                        actual.conectarVecino(vecinoArriba);
                        vecinoArriba.conectarVecino(actual);
                    }
                    if(j > 0 && matrizCeldas[i][j - 1] == 0){
                        Nodo vecinoIzquierda = grafo.getNodo(i, j - 1);
                        actual.conectarVecino(vecinoIzquierda);
                        vecinoIzquierda.conectarVecino(actual);
                    }

                    if(i < filas - 1 && matrizCeldas[i + 1][j] == 0){
                        Nodo vecinoAbajo = grafo.getNodo(i + 1, j);
                        actual.conectarVecino(vecinoAbajo);
                        vecinoAbajo.conectarVecino(actual);
                    }

                    if(j < columnas - 1 && matrizCeldas[i][j + 1] == 0){
                        Nodo vecinoDerecha = grafo.getNodo(i, j + 1);
                        actual.conectarVecino(vecinoDerecha);
                        vecinoDerecha.conectarVecino(actual);
                    }
                }
                listaAdyacencia.add(actual.getVecinos());
                System.out.println(actual + "           LISTA: " + actual.getVecinos());
            }
        }

        Nodo nodoInicio = null;
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

        if(nodoInicio == null){
            nodoInicio = grafo.getNodo(0,0); 
            if(nodoInicio != null) nodoInicio.setEsPared(false); 
        }
         if(nodoFin == null || nodoFin == nodoInicio){
           
            if(nodoInicio != null && nodoInicio.getY() == 0 && nodoInicio.getX() == 0){
                 nodoFin = grafo.getNodo(filas-1, columnas-1);
            } else {
                 nodoFin = grafo.getNodo(0,0);
            }
            if(nodoFin != null){
                nodoFin.setEsPared(false); 
                if(nodoFin == nodoInicio && (filas > 1 || columnas > 1)){
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
        if(filas == 1 && columnas == 1 && nodoInicio != null){
            nodoFin = nodoInicio;
        }
        
        if(nodoInicio != null) grafo.setNodoInicio(nodoInicio);
        if(nodoFin != null) grafo.setNodoFin(nodoFin);
        else if(nodoInicio != null) grafo.setNodoFin(nodoInicio);

        return grafo;
    }

    public void dibujarMatrizConsola(int[][] matriz, LaberintoGrafo grafo) {
        if(matriz == null || matriz.length == 0){
            System.out.println("Matriz vacia ");
            return;
        }
        System.out.println("\nRepresentacion del laberinto en consola:");
        int filas = matriz.length;
        int columnas = matriz[0].length;

        Nodo nodoInicio = (grafo != null) ? grafo.getNodoInicio() : null;
        Nodo nodoFin = (grafo != null) ? grafo.getNodoFin() : null;

        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                boolean esInicio = false;
                boolean esFin = false;

                if(nodoInicio != null && nodoInicio.getY() == i && nodoInicio.getX() == j){
                    esInicio = true;
                }
                if(nodoFin != null && nodoFin.getY() == i && nodoFin.getX() == j){
                    esFin = true;
                }

                if(esInicio){
                    System.out.print(" S "); // Inicio
                } else if(esFin){
                    System.out.print(" E "); // Fin
                } else if(matriz[i][j] == 0){
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
