package com.mycompany.laberinto;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

// GUI en la cual se muestra el laberinto, se seleccionan los metodos de busqueda, se pinta el camino resuelto en el
// menor tiempo y se muestra cual fue el metodo de busqueda mas rapido
public class LaberintoPanel extends JPanel {
    private LaberintoGrafo grafo;
    private final int MARGEN = 20;
    private int anchoPanel;
    private int altoPanel;
    private int celdaAncho;
    private int celdaAlto;
    private int tamanoCelda;
    private int filas;
    private int columnas;
    private ArrayList<ArrayList<Nodo>> nodos;
    private ArrayList<Nodo> caminoResuelto;
    private int indicadorParaBorrarCamino;
    private int color;

    public LaberintoPanel() {
        setBackground(Color.GRAY);
    }

    public void setLaberinto(LaberintoGrafo grafo) {
        this.grafo = grafo;
        repaint();
        revalidate();
    }

    // Este método pinta el laberinto creado con el grafo, pintando las celdas con blanco representando donde hay camino.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (grafo == null || grafo.getNodos() == null || grafo.getFilas() == 0 || grafo.getColumnas() == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        anchoPanel = getWidth();
        altoPanel = getHeight();
        filas = grafo.getFilas();
        columnas = grafo.getColumnas();
        celdaAncho = (anchoPanel - 2 * MARGEN) / columnas;
        celdaAlto = (altoPanel - 2 * MARGEN) / filas;
        tamanoCelda = Math.min(celdaAncho, celdaAlto);
        nodos = grafo.getNodos();

        if (tamanoCelda <= 0) tamanoCelda = 1;

        int distanciaX = MARGEN + (anchoPanel - 2 * MARGEN - columnas * tamanoCelda) / 2;
        int distanciaY = MARGEN + (altoPanel - 2 * MARGEN - filas * tamanoCelda) / 2;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Nodo nodoActual = nodos.get(i).get(j);
                if (nodoActual == null) continue;

                int xRect = distanciaX + j * tamanoCelda;
                int yRect = distanciaY + i * tamanoCelda;

                if (nodoActual.isEsPared()) {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.fillRect(xRect, yRect, tamanoCelda, tamanoCelda);
                } else {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(xRect, yRect, tamanoCelda, tamanoCelda);
                }

                g2d.setColor(Color.BLACK);
                g2d.drawRect(xRect, yRect, tamanoCelda, tamanoCelda);

                g2d.setColor(Color.WHITE);

                List<Nodo> vecinos = nodoActual.getVecinos();

                for (Nodo vecino : vecinos) {
                    if (vecino.getY() == nodoActual.getY() + 1) {
                        g2d.drawLine(xRect + 1, yRect + tamanoCelda, xRect + tamanoCelda - 1, yRect + tamanoCelda);
                    }
                    if (vecino.getY() == nodoActual.getY() - 1) {
                        g2d.drawLine(xRect + 1, yRect, xRect + tamanoCelda - 1, yRect);
                    }
                    if (vecino.getX() == nodoActual.getX() + 1) {
                        g2d.drawLine(xRect + tamanoCelda, yRect + 1, xRect + tamanoCelda, yRect + tamanoCelda - 1);
                    }
                    if (vecino.getX() == nodoActual.getX() - 1) {
                        g2d.drawLine(xRect, yRect + 1, xRect, yRect + tamanoCelda - 1);
                    }
                }

                pintarInicioYFin(nodoActual, g2d, xRect, yRect);
            }
        }

        if (caminoResuelto != null) {
            pintarCamino(g2d);
        }
    }

    // Este método pinta un cuadro donde se encuentra el inicio y el fin del laberinto.
    public void pintarInicioYFin(Nodo nodoActual, Graphics2D g2d, int xRect, int yRect) {
        if (nodoActual.isEsInicio()) {
            g2d.setColor(new Color(144, 238, 144));
            g2d.fillRect(xRect + tamanoCelda / 4, yRect + tamanoCelda / 4, tamanoCelda / 2, tamanoCelda / 2);
            g2d.setColor(Color.BLACK);
            g2d.drawString("S", xRect + tamanoCelda / 2 - g2d.getFontMetrics().stringWidth("S") / 2, yRect + tamanoCelda / 2 + g2d.getFontMetrics().getAscent() / 2 - g2d.getFontMetrics().getDescent() / 2);
        } else if (nodoActual.isEsFin()) {
            g2d.setColor(new Color(255, 182, 193));
            g2d.fillRect(xRect + tamanoCelda / 4, yRect + tamanoCelda / 4, tamanoCelda / 2, tamanoCelda / 2);
            g2d.setColor(Color.BLACK);
            g2d.drawString("E", xRect + tamanoCelda / 2 - g2d.getFontMetrics().stringWidth("E") / 2, yRect + tamanoCelda / 2 + g2d.getFontMetrics().getAscent() / 2 - g2d.getFontMetrics().getDescent() / 2);
        }
    }

    // Este método pinta la solución del laberinto, seleccionando un color diferente dependiendo del algoritmo utilizado.
    private void pintarCamino(Graphics2D g2d) {
        anchoPanel = getWidth();
        altoPanel = getHeight();
        filas = grafo.getFilas();
        columnas = grafo.getColumnas();
        celdaAncho = (anchoPanel - 2 * MARGEN) / columnas;
        celdaAlto = (altoPanel - 2 * MARGEN) / filas;
        tamanoCelda = Math.min(celdaAncho, celdaAlto);
        nodos = grafo.getNodos();
        int distanciaX = MARGEN + (anchoPanel - 2 * MARGEN - columnas * tamanoCelda) / 2;
        int distanciaY = MARGEN + (altoPanel - 2 * MARGEN - filas * tamanoCelda) / 2;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Nodo nodoActual = nodos.get(i).get(j);
                if (nodoActual == null) continue;

                int xRect = distanciaX + j * tamanoCelda;
                int yRect = distanciaY + i * tamanoCelda;

                if (caminoResuelto.contains(nodoActual) && !nodoActual.isEsPared()) {
                    if (indicadorParaBorrarCamino == 1) {
                        g2d.setColor(Color.WHITE);
                    } else {
                        System.out.println("COLOR: " + getColor());
                        switch (getColor()) {
                            case 1:
                                // BSF
                                g2d.setColor(new Color(194, 84, 253));
                                break;
                                case 2:
                                    // A*
                                g2d.setColor(new Color(140, 143, 19));
                                break;
                            case 3:
                                // DIJKSTRA
                                g2d.setColor(new Color(53, 91, 227));
                                break;
                        }

                    }
                    g2d.fillRect(xRect, yRect, tamanoCelda, tamanoCelda);
                }
                pintarInicioYFin(nodoActual, g2d, xRect, yRect);
            }
        }
    }

    // Este método establece el camino obtenido a pintar para la solución.
    public void setCaminoResuelto(ArrayList<Nodo> camino) {
        this.caminoResuelto = camino;
        repaint();
    }

    // Este método establece el indicador de cuando se necesita borrar la solución pintada.
    public void setIndicadorParaBorrarCamino(int indicadorParaBorrarCamino) {
        this.indicadorParaBorrarCamino = indicadorParaBorrarCamino;
    }

    // Getter y setter del color.
    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}