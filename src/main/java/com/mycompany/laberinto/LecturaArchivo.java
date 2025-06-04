package com.mycompany.laberinto;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

// Esta clase busca y lee un archivo csv con una lista de adyacencia para que pueda ser convertida a un laberinto
public class LecturaArchivo {
    private static LaberintoGrafo grafo;
    private static int[][] matriz;
    private static Map<Integer, List<Integer>> listaAdyacencia;

    public static File buscarArchivo() {

        // Se crea un fileChooser para que el usuario pueda buscar un archivo csv en cualquier directorio en su
        // computadora
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

    // Este metodo lee el archivo y obtiene la lista de adyacencia del laberinto
    // Lee primero la primea linea que determina las dimensiones del laberinto
    // El formato para la adyacencia de cada nodo es Nodo: nodoVecino, Vecino2, etc.
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

                    String[] adyacentes = partes[1].split(",");
                    List<Integer> vecinos = new ArrayList<>();
                    for (String nodo : adyacentes) {
                        if (!nodo.isEmpty()) {
                            vecinos.add(Integer.parseInt(nodo));
                        }
                    }
                    listaAdyacencia.put(nodoPrincipal, vecinos);
                } else {
                    String[] dimensiones = linea.split(",");
                    filas = Integer.parseInt(dimensiones[0].trim());
                    columnas = Integer.parseInt(dimensiones[1].trim());
                    System.out.println("FILAS: " + filas + "   COLUMNAS: " + columnas);
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("No encontró el archivo");
        } catch (IOException ex) {
            System.err.println("No se pudo leer el archivo");
        }
    }

    // GETTERS
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
