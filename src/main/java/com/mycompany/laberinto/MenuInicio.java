package com.mycompany.laberinto;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MenuInicio extends JFrame {
    private JPanel panelMenu;
    private JButton laberintoAleatorio;
    private JButton grafoUsuario;
    private JButton leerArchivo;
    private JLabel titulo;

    public MenuInicio(){
        panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));

        laberintoAleatorio = new JButton("Laberinto aleatorio");
        grafoUsuario = new JButton("Ingresar un grafo");
        leerArchivo = new JButton("Agregar archivo");

        laberintoAleatorio.setFocusPainted(false);
        grafoUsuario.setFocusPainted(false);
        leerArchivo.setFocusPainted(false);

        laberintoAleatorio.setAlignmentX(Component.CENTER_ALIGNMENT);
        grafoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        leerArchivo.setAlignmentX(Component.CENTER_ALIGNMENT);

        laberintoAleatorio.addActionListener(e ->{
            String[] dificultades = {"Facil", "Normal", "Dificil", "Super dificil"};
            int opcionUsuario = JOptionPane.showOptionDialog(this, "Selecciona la dificultad del laberinto", "Laberinto",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, dificultades, dificultades[0]);
            dispose();
            new Ui(opcionUsuario+1).setVisible(true);
        });

        grafoUsuario.addActionListener(e -> {
            String listaAdyacenciaUsuario = JOptionPane.showInputDialog(null, "Ingrese la lista de adyacencia",
                    "Laberinto usuario", JOptionPane.QUESTION_MESSAGE);

            String[] nodos = listaAdyacenciaUsuario.split(" ");
            Map<Integer, List<Integer>> listaAdyacencia = new HashMap<>();
            int[][] matriz = null;

            try {
                for (String nodo : nodos) {
                    String[] partesNodo = nodo.split(":");
                    if (partesNodo.length == 2) {
                        int nodoActual = Integer.parseInt(partesNodo[0]);
                        List<Integer> vecinos = new ArrayList<>();
                        String[] vecinosStr = partesNodo[1].split(",");
                        for (String vecino : vecinosStr) {
                            vecinos.add(Integer.parseInt(vecino.trim()));
                        }
                        listaAdyacencia.put(nodoActual, vecinos);
                    } else {
                        JOptionPane.showMessageDialog(null, "Formato incorrecto en la lista de adyacencia.");
                        return;
                    }
                }

                //generar la matriz con la lista de adyacencia del usuario
                GeneradorDeLaberinto laberintos = new GeneradorDeLaberinto();
                matriz = laberintos.generarMatrizDesdeListaAdyacenciaParedes(listaAdyacencia);

                for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[i].length; j++) {
                        System.out.print(matriz[i][j] + " ");
                    }
                    System.out.println();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error");
            }

            dispose();
            new Ui(matriz).setVisible(true);
        });


        titulo = new JLabel("Laberintos con grafos");
        titulo = new JLabel("Laberintos con grafos");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 50, 0));

        panelMenu.add(titulo);
        panelMenu.add(laberintoAleatorio);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(grafoUsuario);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        panelMenu.add(leerArchivo);

        add(panelMenu);
        setTitle("Menu");
        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
