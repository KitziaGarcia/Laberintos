package com.mycompany.laberinto;

import java.util.*;

// Esta clase cuenta con los tres métodos de búsqueda implementados en las soluciones del laberinto.
// Cuenta con la lógica de cada uno dependiendo de sus características.
public class MetodosDeBusqueda {

    // Búsqueda por anchura (BFS).
    public ArrayList<Nodo> bfs(Nodo origen, Nodo destino, ArrayList<ArrayList<Nodo>> listaGrafo, int filas, int columnas) {
        Queue<Nodo> cola = new LinkedList<>();
        boolean[] visitados = new boolean[filas * columnas];
        Map<Nodo, Nodo> paresDeVecinos = new HashMap<>();
        int indexOrigen = origen.posicion(columnas);
        visitados[indexOrigen] = true;
        cola.add(origen);

        // Este algoritmo maneja una cola donde va ingresando los nodos visitados, se repite la lógica mientras haya alguno
        // en la cola.
        while(!cola.isEmpty()){
            Nodo actual = cola.poll();

            if(actual.equals(destino)){
                break;
            }

            int indexActual = actual.posicion(columnas);

            for(Nodo vecino : listaGrafo.get(indexActual)){
                int indexVecino = vecino.posicion(columnas);
                if(!visitados[indexVecino]){
                    visitados[indexVecino] = true;
                    paresDeVecinos.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        /* Esta parte fue implementada con IA, se utilizó para recorrer el camino generado y ordenarlo al reves,
         * ya que en el BFS se guarda en sentido opuesto
         */
        ArrayList<Nodo> camino = new ArrayList<>();
        Nodo actual = destino;
        while(actual != null && !actual.equals(origen)){
            camino.add(actual);
            actual = paresDeVecinos.get(actual);
        }

        if(actual != null){
            camino.add(origen);
        }

        Collections.reverse(camino);

        return camino;
    }

    // A*.
    public ArrayList<Nodo> aEstrella(Nodo origen, Nodo destino, ArrayList<ArrayList<Nodo>> listaGrafo, int filas, int columnas, ArrayList<ArrayList<Nodo>> nodos) {
        PriorityQueue<Nodo> noVisitados = new PriorityQueue<>();
        boolean[] visitados = new boolean[filas * columnas];
        int fila = origen.getY();
        int columna = origen.getX();
        Nodo inicio = nodos.get(fila).get(columna);
        fila = destino.getY();
        columna = destino.getX();
        Nodo meta = nodos.get(fila).get(columna);
        inicio.setG(0);
        inicio.setF(calcularH(inicio, meta));
        noVisitados.add(inicio);

        // En este algoritmo se manejó una cola de prioridad de nodos no visitados, en la cual se iban removiendo los
        // nodos conforme se visitaran y se agregaban al arreglo de visitados.
        while(!noVisitados.isEmpty()){
            Nodo actual = noVisitados.poll();
            int indexActual = actual.posicion(columnas);
            int indexMeta = meta.posicion(columnas);

            if(indexActual == indexMeta){
                ArrayList<Nodo> camino = new ArrayList<>();
                while(actual != null){
                    camino.add(actual);
                    actual = actual.getPadre();
                }
                Collections.reverse(camino);
                return camino;
            }

            visitados[indexActual] = true;

            for(Nodo vecino : listaGrafo.get(actual.posicion(columnas))){
                if(visitados[vecino.posicion(columnas)]) continue;

                fila = vecino.getY();
                columna = vecino.getX();
                Nodo vecinoNuevo = nodos.get(fila).get(columna);
                int salidaG = actual.getG() + 1;

                if(vecinoNuevo.getPadre() == null || salidaG < vecinoNuevo.getG()){
                    vecinoNuevo.setPadre(actual);
                    vecinoNuevo.setG(salidaG);
                    vecinoNuevo.setF(salidaG + calcularH(vecinoNuevo, meta));

                    if(!noVisitados.contains(vecinoNuevo)){
                        noVisitados.add(vecinoNuevo);
                    } else {
                        noVisitados.remove(vecinoNuevo);
                        noVisitados.add(vecinoNuevo);
                    }
                }
            }
        }
        return null;
    }

    // Este mpetodo calcula la h necesaria para el algoritmo A estrella.
    private int calcularH(Nodo nodo1, Nodo nodo2) {
        return Math.abs(nodo1.getX() - nodo2.getX()) + Math.abs(nodo1.getY() - nodo2.getY());
    }

    // Dijkstra.
    public ArrayList<Nodo> dijkstra(Nodo origen, Nodo destino, ArrayList<ArrayList<Nodo>> listaGrafo, int filas, int columnas, ArrayList<ArrayList<Nodo>> nodos) {
        PriorityQueue<Nodo> noVisitados = new PriorityQueue<>();
        boolean[] visitados = new boolean[filas * columnas];

        for(int i = 0; i < filas; i++){
            for(int j = 0; j < columnas; j++){
                nodos.get(i).get(j).setG(Integer.MAX_VALUE);
                nodos.get(i).get(j).setPadre(null);
            }
        }

        Nodo nodoOrigen = nodos.get(origen.getY()).get(origen.getX());
        nodoOrigen.setG(0);
        noVisitados.add(nodoOrigen);
        Nodo nodoDestino = nodos.get(destino.getY()).get(destino.getX());

        while(!noVisitados.isEmpty()){
            Nodo actual = noVisitados.poll();
            int indexActual = actual.posicion(columnas);

            if(visitados[indexActual]) continue;
            visitados[indexActual] = true;

            // Este fragmento fue corregido con IA para permitir que funcionara la lógica de manera correcta.
            if(actual.equals(nodoDestino)){
                ArrayList<Nodo> caminoRecorrido = new ArrayList<>();
                Nodo nodoActual = actual;
                while(nodoActual != null){
                    caminoRecorrido.add(nodoActual);
                    nodoActual = nodoActual.getPadre();
                }
                Collections.reverse(caminoRecorrido);
                return caminoRecorrido;
            }

            for(Nodo vecino : listaGrafo.get(indexActual)){
                int indexVecino = vecino.posicion(columnas);
                if(visitados[indexVecino]) continue;

                Nodo vecinoNodo = nodos.get(vecino.getY()).get(vecino.getX());
                int nuevaDistancia = actual.getG() + 1;

                if(nuevaDistancia < vecinoNodo.getG()){
                    vecinoNodo.setG(nuevaDistancia);
                    vecinoNodo.setPadre(actual);
                    noVisitados.add(vecinoNodo);
                }
            }
        }
        return null;
    }
}

