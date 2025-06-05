package com.mycompany.laberinto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

// GUI del menu principal, se tienen 3 opciones: generar un laberinto aleatorio, ingresar manualmente una lista de adyacencia
// o leer un csv con una lista de adyacencia
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

        //Se manda a llamar el tipo de laberinto que escogio el usuario y se muestra
        laberintoAleatorio.addActionListener(e ->{
            String[] dificultades = {"Facil", "Normal", "Dificil", "Super dificil"};
            int opcionUsuario = JOptionPane.showOptionDialog(this, "Selecciona la dificultad del laberinto", "Laberinto",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, dificultades, dificultades[0]);
            dispose();
            new Ui(opcionUsuario+1).setVisible(true);
        });

        //Le pide al usuario la lista de adyacencia para luego crear una matriz y
        // poder generar el laberinto a partir de la lista de adyacencia
        grafoUsuario.addActionListener(e -> {
            String listaAdyacenciaUsuario = JOptionPane.showInputDialog(null, "Ingrese la lista de adyacencia",
                    "Laberinto usuario", JOptionPane.QUESTION_MESSAGE);

            String[] nodos = listaAdyacenciaUsuario.split(" ");
            Map<Integer, List<Integer>> listaAdyacencia = new HashMap<>();
            int[][] matriz = null;

            try {
                for(int i=1; i<nodos.length; i++){
                    String[] partesNodo = nodos[i].split(":");
                    if(partesNodo.length == 2){
                        int nodoActual = Integer.parseInt(partesNodo[0]);
                        List<Integer> vecinos = new ArrayList<>();
                        String[] vecinosStr = partesNodo[1].split(",");
                        for(String vecino : vecinosStr){
                            vecinos.add(Integer.parseInt(vecino.trim()));
                        }
                        listaAdyacencia.put(nodoActual, vecinos);
                    } else {
                        JOptionPane.showMessageDialog(null, "Formato incorrecto en la lista de adyacencia");
                        return;
                    }
                }

                GeneradorDeLaberinto laberintos = new GeneradorDeLaberinto();
                matriz = laberintos.generarMatrizDesdeListaAdyacenciaParedes(listaAdyacencia, 1);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error");
            }

            dispose();
            new Ui(matriz).setVisible(true);
        });

        //El usuario ingresa el archivo que contiene la lista de adyacencia y a partir de esta crea el laberinto
        leerArchivo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File archivo = LecturaArchivo.buscarArchivo();
                if (archivo != null) {
                    LecturaArchivo.leerArchivo(archivo);
                    GeneradorDeLaberinto laberintos = new GeneradorDeLaberinto();
                    int[][] matriz = laberintos.generarMatrizDesdeListaAdyacenciaParedes(LecturaArchivo.getListaAdyacencia(), 1);
                    for (Integer nodo : LecturaArchivo.getListaAdyacencia().keySet()) {
                        System.out.println(nodo + " -> " + LecturaArchivo.getListaAdyacencia().get(nodo));
                    }

                    System.out.println("HOLA");
                    for (int i = 0; i < matriz.length; i++) {
                        for (int j = 0; j < matriz[i].length; j++) {
                            System.out.print(matriz[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("ADIOS");
                    new Ui(matriz).setVisible(true);
                } else {

                }
            }
        });

        //Inicializamos los componentes y los agregamos al panel
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
