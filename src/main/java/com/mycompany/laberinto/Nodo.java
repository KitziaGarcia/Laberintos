package com.mycompany.laberinto;

import java.util.ArrayList;

// Esta clase modela a un nodo que es parte de un grafo.
public class Nodo implements Comparable<Nodo> {
    private int x;
    private int y;
    private ArrayList<Nodo> vecinos;
    private boolean visitado;
    private boolean esInicio;
    private boolean esFin;
    private boolean esPared;
    private Nodo padre;
    private int f;
    private int g;

    // Constructor parametrizado.
    public Nodo(int x, int y, int f, int g) {
        this.x = x;
        this.y = y;
        this.f = f;
        this.g = g;
        this.vecinos = new ArrayList<>();
        this.visitado = false;
        this.esInicio = false;
        this.esFin = false;
        this.esPared = false;
        this.padre = null;
    }
    
    // Permite conectar otros nodos con el nodo actual.
    public void conectarVecino(Nodo vecino) {
        if(esVecinoAdyacente(vecino) && !this.vecinos.contains(vecino)){
            this.vecinos.add(vecino);
            
            if(!vecino.vecinos.contains(this)){
                vecino.vecinos.add(this);
            }
        }
    }

    // Obtiene el estatus de si otro nodo tiene conexión con el actual.
    private boolean esVecinoAdyacente(Nodo otro) {
        int deltaX = Math.abs(this.x - otro.x);
        int deltaY = Math.abs(this.y - otro.y);
        
        return (deltaX == 1 && deltaY == 0) || (deltaX == 0 && deltaY == 1);
    }

    // Getters y setters de los atributos.
    public int getGrado() {
        return vecinos.size();
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public ArrayList<Nodo> getVecinos() {
        return new ArrayList<>(vecinos);
    }
    
    public void setVisitado(boolean visitado) {
        this.visitado = visitado;
    }
    
    public boolean isEsInicio() {
        return esInicio;
    }
    
    public void setEsInicio(boolean esInicio) {
        this.esInicio = esInicio;
    }
    
    public boolean isEsFin() {
        return esFin;
    }
    
    public void setEsFin(boolean esFin) {
        this.esFin = esFin;
    }
    
    public boolean isEsPared() {
        return esPared;
    }
    
    public void setEsPared(boolean esPared) {
        this.esPared = esPared;
    }
    
    public Nodo getPadre() {
        return padre;
    }
    
    public void setPadre(Nodo padre) {
        this.padre = padre;
    }

    public int getG() {
        return g;
    }

    public void setF(int f) {
        this.f = f;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int posicion(int columnas) {
        return this.getY() * columnas + this.getX();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Nodo nodo = (Nodo) obj;
        return x == nodo.x && y == nodo.y;
    }
    
    @Override
    public int hashCode() {
        return x * 1000 + y; 
    }
    
    @Override
    public String toString() {
        return "Nodo(" + x + "," + y +")" + "- Grado: " + getGrado();
    }

    public int compareTo(Nodo otro) {
        return Integer.compare(this.f, otro.f);
    }
}