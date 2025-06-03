package com.mycompany.laberinto;

import java.util.*;

public class Ordenamientos {

    public ArrayList<Nodo> bfs(Nodo origen, Nodo destino, ArrayList<ArrayList<Nodo>> listaGrafo, int filas, int columnas) {
        Queue<Nodo> cola = new LinkedList<>();
        boolean[] visitados = new boolean[filas * columnas];
        Map<Nodo, Nodo> paresDeVecinos = new HashMap<>();
        int indexOrigen = origen.posicion(columnas);
        visitados[indexOrigen] = true;
        cola.add(origen);

        while (!cola.isEmpty()) {
            Nodo actual = cola.poll();

            if (actual.equals(destino)) {
                break;
            }

            int indexActual = actual.posicion(columnas);

            for (Nodo vecino : listaGrafo.get(indexActual)) {
                int indexVecino = vecino.posicion(columnas);
                if (!visitados[indexVecino]) {
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
        while (actual != null && !actual.equals(origen)) {
            camino.add(actual);
            actual = paresDeVecinos.get(actual);
        }

        if (actual != null) {
            camino.add(origen);
        }

        Collections.reverse(camino);

        return camino;
    }

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

        while (!noVisitados.isEmpty()) {
            Nodo actual = noVisitados.poll();
            int indexActual = actual.posicion(columnas);
            int indexMeta = meta.posicion(columnas);

            if (indexActual == indexMeta) {
                ArrayList<Nodo> camino = new ArrayList<>();
                while (actual != null) {
                    camino.add(actual);
                    actual = actual.getPadre();
                }
                Collections.reverse(camino);
                return camino;
            }

            visitados[indexActual] = true;

            for (Nodo vecino : listaGrafo.get(actual.posicion(columnas))) {
                if (visitados[vecino.posicion(columnas)]) continue;

                fila = vecino.getY();
                columna = vecino.getX();
                Nodo vecinoNuevo = nodos.get(fila).get(columna);
                int salidaG = actual.getG() + 1;

                if (vecinoNuevo.getPadre() == null || salidaG < vecinoNuevo.getG()) {
                    vecinoNuevo.setPadre(actual);
                    vecinoNuevo.setG(salidaG);
                    vecinoNuevo.setF(salidaG + calcularH(vecinoNuevo, meta));

                    if (!noVisitados.contains(vecinoNuevo)) {
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

    private int calcularH(Nodo nodo1, Nodo nodo2) {
        return Math.abs(nodo1.getX() - nodo2.getX()) + Math.abs(nodo1.getY() - nodo2.getY());
    }

    public ArrayList<Nodo> dijkstra(Nodo origen, Nodo destino, ArrayList<ArrayList<Nodo>> listaGrafo, int filas, int columnas, ArrayList<ArrayList<Nodo>> nodos) {
        // Cola de prioridad que ordena los nodos por su valor G (Distancia)
        PriorityQueue<Nodo> noVisitados = new PriorityQueue<>((a, b) -> Integer.compare(a.getG(), b.getG()));
        boolean[] visitados = new boolean[filas * columnas];
        Nodo actual = null; // valor arbitrario
        int indexActual = 0; // Valor arbitrario
        // Como en Dijkstra no se conocen las distancias entre los nodos a excepcion del origen
        // establecemos el valor de G (distancia) a todos los nodos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                nodos.get(i).get(j).setG(Integer.MAX_VALUE);
                nodos.get(i).get(j).setPadre(null);
            }
        }
        // Encontramos el nodo de origen y se le asigna el valor G (distancia) a 0
        int filaDeOrigen = origen.getY();
        int columnaDeOrigen = origen.getX();
        Nodo nodoDeOrigen = nodos.get(filaDeOrigen).get(columnaDeOrigen);
        nodoDeOrigen.setG(0);
        noVisitados.add(nodoDeOrigen);
        // Ubicamos el nodo de destino y utilizamos get() para obtener la ubicacion del nodo de destino
        int filaDeDestino = destino.getY();
        int columnaDeDestino = destino.getX();
        Nodo nodoDestino = nodos.get(filaDeDestino).get(columnaDeDestino);
        // Inicio del bucle principal del algoritmo en donde seguira recorriendo los nodos sin repetir ninguno
        // hasta encontrar el nodo de destino
        while (!noVisitados.isEmpty()) {
            actual = noVisitados.poll();
            indexActual = actual.posicion(columnaDeDestino);
            if (visitados[indexActual]) {
                continue;
            }
            visitados[indexActual] = true;
        }
        // Verificamos que se halla llegado al nodo deseado
        if (indexActual == nodoDestino.posicion(columnas)) {
            // Recorremos el camino desde el nodo de origen hasta el nodo de destino y lo guardamos en un arraylist
            ArrayList<Nodo> caminoRecorrido = new ArrayList<>();
            Nodo nodoActual = actual;
            while (nodoActual != null) {
                caminoRecorrido.add(nodoActual);
                nodoActual = nodoActual.getPadre();
            }
            // Hacemos un reverse ya que los padres recorren del destino al origen, no del origen al destino como lo
            // requerimos
            Collections.reverse(caminoRecorrido);
            return caminoRecorrido;
        }
            // Exploramos a los nodos vecinos del nodo en el que nos encontramos
        for (Nodo vecino : listaGrafo.get(indexActual)) {
            int indexVecino = vecino.posicion(columnas);
            // Si ya lo recorrimos, se salta
            if (visitados[indexVecino]) {
                continue;
            }

            // Calculamos la distancia entre los nodos, cada nodo tiene un peso de 1
            int filaVecino = vecino.getY();
            int columnaVecino = vecino.getX();
            Nodo vecinoNodo = nodos.get(filaVecino).get(columnaVecino);
            int nuevaDistancia = actual.getG() + 1;

            // Si se encuentra un camino con menor peso que el actual, lo cambiamos
            if (nuevaDistancia < vecinoNodo.getG()) {
                vecinoNodo.setG(nuevaDistancia);
                vecinoNodo.setPadre(actual);
                noVisitados.add(vecinoNodo);
            }
        }
            // Si no se encuentra ningun camino
                return null;
        }

    }

