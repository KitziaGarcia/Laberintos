/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.laberinto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;


public class Ui extends JFrame {
    private LaberintoPanel laberintoPanel; 
    private LaberintoGrafo laberintoGrafo; 
    private GeneradorDeLaberinto generadorDeLaberinto;
    private Ordenamientos metodosOrdenamientos;
    private JPanel grafo;
    private JButton bfsBoton;
    private JButton aEstrellaBoton;
    private JButton borrarBoton;

    public Ui() {
        initComponents();
        laberintoPanel = new LaberintoPanel();
        generadorDeLaberinto = new GeneradorDeLaberinto();
        metodosOrdenamientos = new Ordenamientos();
        JScrollPane scrollPane = new JScrollPane(laberintoPanel);
        
        grafo.setLayout(new BorderLayout());
        grafo.add(scrollPane, BorderLayout.CENTER);

        grafo.setPreferredSize(new Dimension(600, 600));

        generarYMostrarLaberintoAleatorio(4);
        
        setTitle("Ventana de Laberintos");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

    private void generarYMostrarLaberintoAleatorio(int dificultad) {
        int[][] matrizLaberinto = generadorDeLaberinto.generarMatriz(dificultad);
        this.laberintoGrafo = generadorDeLaberinto.generarLaberinto(matrizLaberinto);
        laberintoPanel.setLaberinto(this.laberintoGrafo);
        repaint();
        System.out.println("Laberinto generado:");
        generadorDeLaberinto.dibujarMatrizConsola(matrizLaberinto, laberintoGrafo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grafo = new JPanel();
        bfsBoton = new JButton("Algoritmo BFS");
        aEstrellaBoton = new JButton("Algoritmo A*");
        borrarBoton = new JButton("Borrar");

        GroupLayout GtafoLayout = new GroupLayout(grafo);
        grafo.setLayout(GtafoLayout);
        GtafoLayout.setHorizontalGroup(
            GtafoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 617, Short.MAX_VALUE)
        );
        GtafoLayout.setVerticalGroup(
            GtafoLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
        );

        bfsBoton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //jButton1ActionPerformed(evt); Esto lo puso Ale creo que para ir resolviendo el grafo o algo asi.
                laberintoPanel.setIndicadorParaBorrarCamino(0);
                ArrayList<Nodo> lista = metodosOrdenamientos.bfs(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas());
                laberintoPanel.setCaminoResuelto(lista);
            }
        });

        aEstrellaBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //jButton1ActionPerformed(evt); Esto lo puso Ale creo que para ir resolviendo el grafo o algo asi.
                for (ArrayList<Nodo> fila : laberintoGrafo.getNodos()) {
                    for (Nodo nodo : fila) {
                        nodo.setG(Integer.MAX_VALUE);
                        nodo.setF(0);
                        nodo.setPadre(null);
                        nodo.setVisitado(false);
                    }
                }

                laberintoPanel.setIndicadorParaBorrarCamino(0);
                ArrayList<Nodo> lista = metodosOrdenamientos.aEstrella(laberintoGrafo.getNodoInicio(), laberintoGrafo.getNodoFin(), generadorDeLaberinto.getListaAdyacencia(), laberintoGrafo.getFilas(), laberintoGrafo.getColumnas(), laberintoGrafo.getNodos());
                System.out.println(lista);
                laberintoPanel.setCaminoResuelto(lista);
            }
        });

        borrarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                laberintoPanel.setIndicadorParaBorrarCamino(1);
                repaint();
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(grafo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                // Aquí todos los botones juntos en paralelo para que estén en una columna
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(bfsBoton)
                                        .addComponent(aEstrellaBoton)
                                        .addComponent(borrarBoton))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(51, 51, 51)
                                                // Aquí apilas los botones uno debajo de otro
                                                .addComponent(bfsBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(aEstrellaBoton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(borrarBoton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(grafo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(32, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("\nRecorrido del grafo en consola:");
        generadorDeLaberinto.recorrerGrafoConsola(laberintoGrafo);
        System.out.println("\nFin del programa");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ui().setVisible(true);
            }
        });
    }
}
