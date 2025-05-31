package com.mycompany.laberinto;

import java.util.ArrayList;

public class LaberintoGrafo {
    private ArrayList<ArrayList<Nodo>> nodos;
    private int filas;
    private int columnas;
    private Nodo nodoInicio;
    private Nodo nodoFin;

    public LaberintoGrafo(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.nodos = new ArrayList<>();

        for (int i = 0; i < filas; i++) {
            ArrayList<Nodo> fila = new ArrayList<>();
            for (int j = 0; j < columnas; j++) {
                Nodo nodo = new Nodo(j, i, 0, 0);
                nodo.setG(Integer.MAX_VALUE);
                fila.add(nodo);
            }
            nodos.add(fila);
        }
    }

    public Nodo getNodo(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            return nodos.get(fila).get(columna);
        }
        return null;
    }

    public void setNodoInicio(Nodo nodoInicio) {
        if (this.nodoInicio != null) {
            this.nodoInicio.setEsInicio(false);
        }
        this.nodoInicio = nodoInicio;
        if (this.nodoInicio != null) {
            this.nodoInicio.setEsInicio(true);
            this.nodoInicio.setEsPared(false); 
        }
    }

    public Nodo getNodoInicio() {
        return nodoInicio;
    }

    public void setNodoFin(Nodo nodoFin) {
        if (this.nodoFin != null) {
            this.nodoFin.setEsFin(false);
        }
        this.nodoFin = nodoFin;
        if (this.nodoFin != null) {
            this.nodoFin.setEsFin(true);
            this.nodoFin.setEsPared(false); 
        }
    }

    public Nodo getNodoFin() {
        return nodoFin;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
    
    public ArrayList<ArrayList<Nodo>> getNodos() {
        return nodos;
    }

    public void reiniciarNodosParaBusqueda() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (nodos.get(i).get(j) != null) {
                    nodos.get(i).get(j).reiniciar();
                }
            }
        }
    }
}
