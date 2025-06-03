/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.laberinto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class Ui extends JFrame {
    private LaberintoPanel laberintoPanel; 
    private LaberintoGrafo laberintoGrafo; 
    private GeneradorDeLaberinto generadorDeLaberinto;
    private Ordenamientos metodosOrdenamientos;
    private JPanel grafo;
    private JButton borrarBoton;
    private JButton ingresarCsvBoton;
    private JLabel tiempos;
    private JTextArea informacionTiempos;
    private JCheckBox bfsBoton;
    private JCheckBox aBoton;
    private JCheckBox dijBoton;
    private JButton seleccionar;
    private double tiempoSegAEstrella;
    private double tiempoSegBFS;
    private double tiempoSegDisjktra;

    public Ui(int dificultad) {
        initComponents();
        laberintoPanel = new LaberintoPanel();
        generadorDeLaberinto = new GeneradorDeLaberinto();
        metodosOrdenamientos = new Ordenamientos();
        JScrollPane scrollPane = new JScrollPane(laberintoPanel);
        
        grafo.setLayout(new BorderLayout());
        grafo.add(scrollPane, BorderLayout.CENTER);

        grafo.setPreferredSize(new Dimension(600, 600));

        generarYMostrarLaberintoAleatorio(dificultad);
        
        setTitle("Ventana de Laberintos");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

    //para cuando el usuario ingrese su laberinto
    public Ui(int[][] matriz) {
        initComponents();

        laberintoPanel = new LaberintoPanel();
        generadorDeLaberinto = new GeneradorDeLaberinto();
        metodosOrdenamientos = new Ordenamientos();

        JScrollPane scrollPane = new JScrollPane(laberintoPanel);
        grafo.setLayout(new BorderLayout());
        grafo.add(scrollPane, BorderLayout.CENTER);
        grafo.setPreferredSize(new Dimension(600, 600));

        generarYMostrarLaberintoUsuario(matriz);

        setTitle("Ventana de Laberintos");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void generarYMostrarLaberintoAleatorio(int dificultad) {
        int[][] matriz = generadorDeLaberinto.generarMatriz(dificultad);

        System.out.println("HOLA1");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println(); // Salto de línea al final de cada fila
        }
        System.out.println("ADIOS1");


        this.laberintoGrafo = generadorDeLaberinto.generarLaberinto(matriz);
        laberintoPanel.setLaberinto(this.laberintoGrafo);
        repaint();
        System.out.println("Laberinto generado:");
        generadorDeLaberinto.dibujarMatrizConsola(matriz, laberintoGrafo);
    }

    //para cuando el usuario ingrese un laberinto
    private void generarYMostrarLaberintoUsuario(int[][] matriz) {
        this.laberintoGrafo = generadorDeLaberinto.generarLaberinto(matriz);
        laberintoPanel.setLaberinto(this.laberintoGrafo);
        repaint();
        System.out.println("Laberinto generado:");
        generadorDeLaberinto.dibujarMatrizConsola(matriz, laberintoGrafo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grafo = new JPanel();
        borrarBoton = new JButton("Borrar");
        ingresarCsvBoton = new JButton("Generar con CSV");
        tiempos = new JLabel("Informacion tiempos");
        informacionTiempos = new JTextArea();
        bfsBoton = new JCheckBox("Algoritmo BFS");
        aBoton = new JCheckBox("Algoritmo A*");
        dijBoton = new JCheckBox("Algoritmo Dijkstra");
        seleccionar = new JButton("DAB"); //cambiar el nombre de esto

        //para quitarle la cosa feo del boton
        borrarBoton.setFocusPainted(false);
        seleccionar.setFocusPainted(false);
        informacionTiempos.setEditable(false);

        GroupLayout GrafoLayout = new GroupLayout(grafo);
        grafo.setLayout(GrafoLayout);
        GrafoLayout.setHorizontalGroup(
            GrafoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 617, Short.MAX_VALUE)
        );
        GrafoLayout.setVerticalGroup(
            GrafoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
        );

        ingresarCsvBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File archivo = LecturaArchivo.buscarArchivo();
                if (archivo != null) {
                    LecturaArchivo.leerArchivo(archivo);

                    //laberintoGrafo = LecturaArchivo.getGrafo();
                    laberintoPanel = new LaberintoPanel();
                    GeneradorDeLaberinto laberintos = new GeneradorDeLaberinto();
                    int[][] matriz = laberintos.generarMatrizDesdeListaAdyacenciaParedes(LecturaArchivo.getListaAdyacencia());

                    System.out.println("HOLA");
                    for (int i = 0; i < matriz.length; i++) {
                        for (int j = 0; j < matriz[i].length; j++) {
                            System.out.print(matriz[i][j] + " ");
                        }
                        System.out.println(); // Salto de línea al final de cada fila
                    }
                    System.out.println("ADIOS");

                    generarYMostrarLaberintoUsuario(matriz);
                    generadorDeLaberinto.dibujarMatrizConsola(LecturaArchivo.getMatriz(), laberintoGrafo);
                    repaint();
                } else {

                }
            }
        });

        borrarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                laberintoPanel.setIndicadorParaBorrarCamino(1);
                repaint();
                informacionTiempos.setText("");
                bfsBoton.setSelected(false);
                aBoton.setSelected(false);
                dijBoton.setSelected(false);
            }
        });

        //aqui estan todos los algoritmos, son como los botones
        seleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                informacionTiempos.setText("");
                int tiempoMenor = 0;
                ArrayList<Double> tiemposEjecutados = new ArrayList<>();

                if(bfsBoton.isSelected()){
                    laberintoPanel.setIndicadorParaBorrarCamino(0);
                    long tiempoInicio = System.nanoTime();
                    ArrayList<Nodo> lista = metodosOrdenamientos.bfs(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas());
                    laberintoPanel.setCaminoResuelto(lista);
                    long tiempoFinal = System.nanoTime();
                    tiempoSegBFS = (tiempoFinal - tiempoInicio) / 1_000_000_000.0;
                    informacionTiempos.append(String.format("BFS: %.6f segundos\n", tiempoSegBFS));
                    tiemposEjecutados.add(tiempoSegBFS);
                }

                if(aBoton.isSelected()){
                    for (ArrayList<Nodo> fila : laberintoGrafo.getNodos()) {
                        for (Nodo nodo : fila) {
                            nodo.setG(Integer.MAX_VALUE);
                            nodo.setF(0);
                            nodo.setPadre(null);
                            nodo.setVisitado(false);
                        }
                    }

                    laberintoPanel.setIndicadorParaBorrarCamino(0);
                    long tiempoInicio = System.nanoTime();
                    ArrayList<Nodo> lista = metodosOrdenamientos.aEstrella(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas(), laberintoGrafo.getNodos());
                    System.out.println(lista);
                    laberintoPanel.setCaminoResuelto(lista);
                    long tiempoFinal = System.nanoTime();
                    tiempoSegAEstrella = (tiempoFinal - tiempoInicio) / 1_000_000_000.0;
                    informacionTiempos.append(String.format("A*: %.6f segundos\n", tiempoSegAEstrella));
                    tiemposEjecutados.add(tiempoSegAEstrella);
                }

                if(dijBoton.isSelected()){
                    laberintoPanel.setIndicadorParaBorrarCamino(0);
                    long tiempoInicio = System.nanoTime();
                    ArrayList<Nodo> lista = metodosOrdenamientos.dijkstra(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas(), laberintoGrafo.getNodos());
                    System.out.println(lista);
                    laberintoPanel.setCaminoResuelto(lista);
                    System.out.println(lista);
                    long tiempoFinal = System.nanoTime();
                    tiempoSegDisjktra = (tiempoFinal - tiempoInicio) / 1_000_000_000.0;
                    informacionTiempos.append(String.format("Disjktra: %.6f segundos\n", tiempoSegDisjktra));
                    tiemposEjecutados.add(tiempoSegDisjktra);
                }

                if (!tiemposEjecutados.isEmpty()) {
                    int indiceMenorTiempo = 0;
                    double menorTiempo = tiemposEjecutados.get(0);

                    for (int i = 1; i < tiemposEjecutados.size(); i++) {
                        if (tiemposEjecutados.get(i) < menorTiempo) {
                            menorTiempo = tiemposEjecutados.get(i);
                            indiceMenorTiempo = i;
                        }
                    }
                    //resalta de verde el menor tiempo
                    Highlighter highlighter = informacionTiempos.getHighlighter();
                    Highlighter.HighlightPainter marcador = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
                    try {
                        int inicioInfoTiempo = informacionTiempos.getLineStartOffset(indiceMenorTiempo);
                        int finInfoTiempo = informacionTiempos.getLineEndOffset(indiceMenorTiempo);
                        highlighter.addHighlight(inicioInfoTiempo, finInfoTiempo, marcador);
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(grafo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(ingresarCsvBoton)
                                        .addComponent(bfsBoton)
                                        .addComponent(aBoton)
                                        .addComponent(dijBoton)
                                        .addComponent(borrarBoton)
                                        .addComponent(seleccionar)
                                        .addComponent(tiempos)
                                        .addComponent(informacionTiempos))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(51, 51, 51)
                                                .addComponent(ingresarCsvBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bfsBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(aBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(dijBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(borrarBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(seleccionar)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(tiempos)
                                                .addComponent(informacionTiempos))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(grafo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(32, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        //System.out.println("\nRecorrido del grafo en consola:");
        //generadorDeLaberinto.recorrerGrafoConsola(laberintoGrafo);
        //System.out.println("\nFin del programa");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Ui(1).setVisible(true);
            }
        });
    }
}
