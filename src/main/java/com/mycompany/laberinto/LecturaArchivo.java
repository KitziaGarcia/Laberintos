package com.mycompany.laberinto;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


public class LecturaArchivo {
    private static LaberintoGrafo grafo;
    private static int[][] matriz;
    private static Map<Integer, List<Integer>> listaAdyacencia;

    public static File buscarArchivo() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona el archivo CSV del laberinto");

        int resultado = fileChooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            System.out.println("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath());
            return archivoSeleccionado;
        } else {
            System.out.println("No se seleccionó ningún archivo.");
            return null;
        }
    }

    public static void leerArchivo(File archivo) {
        String linea;
        listaAdyacencia = new HashMap<>();
        int filas = 0;
        int columnas = 0;

        try (BufferedReader leer = new BufferedReader(new FileReader(archivo))) {
            while ((linea = leer.readLine()) != null) {
                if (linea.contains(":")) {
                    String[] partes = linea.split(":");
                    int nodoPrincipal = Integer.parseInt(partes[0].trim());

                    String[] adyacentesStr = partes[1].split(",");
                    List<Integer> adyacentes = new ArrayList<>();
                    for (String nodo : adyacentesStr) {
                        if (!nodo.trim().isEmpty()) {
                            adyacentes.add(Integer.parseInt(nodo.trim()));
                        }
                    }

                    listaAdyacencia.put(nodoPrincipal, adyacentes);
                } else {
                    String[] dimensiones = linea.split(",");
                    filas = Integer.parseInt(dimensiones[0].trim());
                    columnas = Integer.parseInt(dimensiones[1].trim());
                    System.out.println("FILAS: " + filas + "   COLUMNAS: " + columnas);
                }
            }

            // Ahora tienes el Map construido
            // grafo = new LaberintoGrafo(filas, columnas, listaAdyacencia);

        } catch (FileNotFoundException ex) {
            System.err.println("No encontró el archivo");
        } catch (IOException ex) {
            System.err.println("No se pudo leer el archivo");
        }
    }

    public static Map<Integer, List<Integer>> getListaAdyacencia() {
        return listaAdyacencia;
    }

    public static LaberintoGrafo getGrafo() {
        return grafo;
    }

    public static int[][] getMatriz() {
        return matriz;
    }


}
