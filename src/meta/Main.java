package meta;

import java.io.File;
import java.io.FilenameFilter;

public class Main {
    /**
     * @param solucion   Vector con la solución del problema
     * @param aeropuerto Fabrica con las matrices de piezas transferidas y las distancias entre unidades de producción
     * @return El peso de la solucion
     * @brief Calcula el peso de la solución
     */
    public static int calcularCoste(int[] solucion, Aeropuerto aeropuerto) {
        int peso = 0;
        for (int i = 0; i < solucion.length; i++)
            for (int j = 0; j < solucion.length; j++)
                if (i != j)
                    peso += aeropuerto.flujos[i][j] * aeropuerto.distancias[solucion[i]][solucion[j]];

        return peso;
    } // calcularCoste()

    public static void main(String[] args) {
        // Datos
        File folder = new File("_data/");
        File[] listOfFiles = folder.listFiles((new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".dat");
            }
        }));
        final int NUM_ARCHIVOS = listOfFiles.length;
        String[] nombres_archivos = new String[NUM_ARCHIVOS];

        int tam_nombres_archivos = 0;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                nombres_archivos[tam_nombres_archivos] = listOfFiles[i].getName();
                tam_nombres_archivos++;
            }
        }
        // Declaraciones
        String direccion;
        Aeropuerto aeropuertos[] = new Aeropuerto[NUM_ARCHIVOS];
        Greedy greedy[] = new Greedy[NUM_ARCHIVOS];
        int solucion_greedy[];

        // Creación de aeropuertos y generación de soluciones
        for (int i = 0; i < NUM_ARCHIVOS; i++) {
            direccion = "_data/" + nombres_archivos[i];
            aeropuertos[i] = new Aeropuerto(direccion);
            greedy[i] = new Greedy(aeropuertos[i]);
            solucion_greedy = greedy[i].algoritmoGreedy();
            System.out.printf("%s: \n", nombres_archivos[i]);
            for (int j = 0; j < greedy[i].tam; j++) {
                System.out.printf("%d ", solucion_greedy[j]);
            }
            System.out.printf("\nCoste: %d \n\n", calcularCoste(solucion_greedy, aeropuertos[i]));
        }
    } // main()
} // Main()
