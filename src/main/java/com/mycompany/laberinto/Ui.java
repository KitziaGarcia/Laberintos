/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.laberinto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class Ui extends JFrame {
    private LaberintoPanel laberintoPanel;
    private LaberintoGrafo laberintoGrafo;
    private GeneradorDeLaberinto generadorDeLaberinto;
    private MetodosDeBusqueda metodosMetodosDeBusqueda;
    private JPanel grafo;
    private JButton borrarBoton;
    private JButton regresarMenu;
    private JLabel tiempos;
    private JTextArea informacionTiempos;
    private JCheckBox bfsBoton;
    private JCheckBox aBoton;
    private JCheckBox dijBoton;
    private JButton seleccionar;
    private double tiempoSegAEstrella;
    private double tiempoSegBFS;
    private double tiempoSegDisjktra;
    private boolean indicador1;
    private boolean indicador2;
    private boolean indicador3;


    public Ui(int dificultad) {
        initComponents();
        tiempoSegAEstrella = 100;
        tiempoSegBFS = 100;
        laberintoPanel = new LaberintoPanel();
        generadorDeLaberinto = new GeneradorDeLaberinto();
        metodosMetodosDeBusqueda = new MetodosDeBusqueda();
        JScrollPane scrollPane = new JScrollPane(laberintoPanel);
        grafo.setLayout(new BorderLayout());
        grafo.add(scrollPane, BorderLayout.CENTER);
        grafo.setPreferredSize(new Dimension(600, 600));
        generarYMostrarLaberintoAleatorio(dificultad);
        setTitle("Ventana de Laberintos");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Ui(int[][] matriz) {
        initComponents();
        generadorDeLaberinto = new GeneradorDeLaberinto();
        laberintoPanel = new LaberintoPanel();
        generadorDeLaberinto.dibujarMatrizConsola(LecturaArchivo.getMatriz(), laberintoGrafo);
        generarYMostrarLaberintoUsuario(matriz);
        metodosMetodosDeBusqueda = new MetodosDeBusqueda();
        JScrollPane scrollPane = new JScrollPane(laberintoPanel);
        grafo.setLayout(new BorderLayout());
        grafo.add(scrollPane, BorderLayout.CENTER);
        grafo.setPreferredSize(new Dimension(600, 600));
        setTitle("Ventana de Laberintos");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Metodo para generar y mostrar el laberinto para cuando se genera un laberinto aleatorio
    private void generarYMostrarLaberintoAleatorio(int dificultad) {
        int[][] matriz = generadorDeLaberinto.generarMatriz(dificultad);
        this.laberintoGrafo = generadorDeLaberinto.generarLaberinto(matriz);
        laberintoPanel.setLaberinto(this.laberintoGrafo);
        repaint();
        generadorDeLaberinto.dibujarMatrizConsola(matriz, laberintoGrafo);
    }

    //Metodo para generar y mostrar el laberinto para cuando el usuario ingresa su laberinto
    public void generarYMostrarLaberintoUsuario(int[][] matriz) {
        this.laberintoGrafo = generadorDeLaberinto.generarLaberinto(matriz);
        laberintoPanel.setLaberinto(this.laberintoGrafo);
        repaint();
        generadorDeLaberinto.dibujarMatrizConsola(matriz, laberintoGrafo);
    }

    //Inicializamos todos los componentes de nuestra GUI y agregamos listeners
    private void initComponents() {
        grafo = new JPanel();
        borrarBoton = new JButton("Borrar");
        tiempos = new JLabel("Informacion tiempos");
        informacionTiempos = new JTextArea();
        bfsBoton = new JCheckBox("Algoritmo BFS");
        aBoton = new JCheckBox("Algoritmo A*");
        dijBoton = new JCheckBox("Algoritmo Dijkstra");
        seleccionar = new JButton("Enter");
        regresarMenu = new JButton("Regresar al menu");

        borrarBoton.setFocusPainted(false);
        seleccionar.setFocusPainted(false);
        regresarMenu.setFocusPainted(false);
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

        regresarMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuInicio().setVisible(true);
                dispose();
            }
        });

        //Gestiona la seleccion de los diferentes algoritmos, manda a llamar a los metodos para resolver el laberinto,
        //pinta los caminos de diferentes colores dependiendo del algoritmo y toma el tiempo que tarda cada uno en resolver el laberinto
        seleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                informacionTiempos.setText("");
                int tiempoMenor = 0;
                ArrayList<Double> tiemposEjecutados = new ArrayList<>();

                if(bfsBoton.isSelected()){
                    laberintoPanel.setIndicadorParaBorrarCamino(0);
                    long tiempoInicio = System.nanoTime();
                    ArrayList<Nodo> lista = metodosMetodosDeBusqueda.bfs(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas());
                    laberintoPanel.setColor(1);
                    laberintoPanel.setCaminoResuelto(lista);
                    indicador1 = true;
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
                    ArrayList<Nodo> lista = metodosMetodosDeBusqueda.aEstrella(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas(), laberintoGrafo.getNodos());
                    System.out.println(lista);
                    indicador2 = true;
                    laberintoPanel.setColor(2);
                    System.out.println("COLORIOT: " + laberintoPanel.getColor());
                    laberintoPanel.setCaminoResuelto(lista);
                    long tiempoFinal = System.nanoTime();
                    tiempoSegAEstrella = (tiempoFinal - tiempoInicio) / 1_000_000_000.0;
                    informacionTiempos.append(String.format("A*: %.6f segundos\n", tiempoSegAEstrella));
                    tiemposEjecutados.add(tiempoSegAEstrella);
                }

                if(dijBoton.isSelected()){
                    laberintoPanel.setIndicadorParaBorrarCamino(0);
                    long tiempoInicio = System.nanoTime();
                    ArrayList<Nodo> lista = metodosMetodosDeBusqueda.dijkstra(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas(), laberintoGrafo.getNodos());
                    System.out.println(lista);
                    laberintoPanel.setColor(3);
                    laberintoPanel.setCaminoResuelto(lista);
                    indicador3 = true;
                    System.out.println(lista);
                    long tiempoFinal = System.nanoTime();
                    tiempoSegDisjktra = (tiempoFinal - tiempoInicio) / 1_000_000_000.0;
                    informacionTiempos.append(String.format("Disjktra: %.6f segundos\n", tiempoSegDisjktra));
                    tiemposEjecutados.add(tiempoSegDisjktra);
                }

                //Marca de color verde el algoritmo que tardo menos tiempo en resolver el laberinto y
                // muestra el camino del algoritmo que tardo menos
                if (!tiemposEjecutados.isEmpty()) {
                    int indiceMenorTiempo = 0;
                    double menorTiempo = tiemposEjecutados.get(0);

                    for (int i = 1; i < tiemposEjecutados.size(); i++) {
                        if (tiemposEjecutados.get(i) < menorTiempo) {
                            menorTiempo = tiemposEjecutados.get(i);
                            indiceMenorTiempo = i;

                        }
                    }
                    Highlighter highlighter = informacionTiempos.getHighlighter();
                    Highlighter.HighlightPainter marcador = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
                    try {
                        int inicioInfoTiempo = informacionTiempos.getLineStartOffset(indiceMenorTiempo);
                        int finInfoTiempo = informacionTiempos.getLineEndOffset(indiceMenorTiempo);
                        highlighter.addHighlight(inicioInfoTiempo, finInfoTiempo, marcador);
                        //Cada algoritmo le corresponde un color, pintara el camino resulto del mas rapido
                        System.out.println("\n------------\n" + tiemposEjecutados);
                        switch (indiceMenorTiempo) {
                            case 0:
                                if (indicador1) {
                                    laberintoPanel.setColor(1);
                                }
                                break;
                            case 1:
                                if (indicador2) {
                                    laberintoPanel.setColor(2);
                                }
                                break;
                            case 2:
                                if (indicador3) {
                                    laberintoPanel.setColor(3);
                                }
                                break;
                        }
                        indicador1 = false;
                        indicador2 = false;
                        indicador3 = false;
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        //Acomodo de los componentes de la GUI en la para seleccionar el algoritmo y resolver junto con otras cosas
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(grafo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(bfsBoton)
                                        .addComponent(aBoton)
                                        .addComponent(dijBoton)
                                        .addComponent(seleccionar)
                                        .addComponent(borrarBoton)
                                        .addComponent(regresarMenu)
                                        .addComponent(tiempos)
                                        .addComponent(informacionTiempos))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //Centramos los botones y todos los componentes que permiten al usuario interactuar con la GUI
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(51, 51, 51)
                                                .addComponent(bfsBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(aBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(dijBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(seleccionar)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(borrarBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(regresarMenu)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(tiempos)
                                                .addComponent(informacionTiempos))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(grafo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(32, Short.MAX_VALUE))
        );
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Ui(1).setVisible(true);
            }
        });
    }
}