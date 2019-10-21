package meta;

import java.io.*;

public class Main {
    /*
        Parámetros contenidos en una Array de Strings. Distribución dentro del Array:
        0: algorithm
        1: input
        2: seed
     */
    private static String[] parametros = new String[3];
    private static final int ALGORITHM = 0;
    private static final int INPUT = 1;
    private static final int SEED = 2;

    private static final String TODOS_ARCHIVOS = "all";
    private static final String GREEDY = "greedy";
    private static final String BL = "bl";
    private static final String TABU = "tabu";

    static String contenidoLog = "";

    public static void main(String[] args) {
        String archivo_conf = args[0];
        String direccion_parametros = "_data/" + archivo_conf;
        Utils.lecturaArchivoConf(direccion_parametros, parametros);

        // Datos
        File folder = new File("_data/");
        File[] listOfFiles = folder.listFiles(((dir, name) -> name.toLowerCase().endsWith(".dat")));
        final int NUM_ARCHIVOS = listOfFiles.length;
        String[] nombres_archivos = new String[NUM_ARCHIVOS];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                nombres_archivos[i] = listOfFiles[i].getName();
            }
        }

        String[] archivos_seleccionados;
        if (parametros[INPUT].equals(TODOS_ARCHIVOS)) {
            archivos_seleccionados = nombres_archivos;
        } else {
            String[] linea_troceada = parametros[INPUT].split(" ");
            archivos_seleccionados = new String[linea_troceada.length];

            for (int i = 0; i < linea_troceada.length; i++)
                archivos_seleccionados[i] = linea_troceada[i];
        }

        String direccion;
        Aeropuerto aeropuertos[] = new Aeropuerto[archivos_seleccionados.length];
        for (int i = 0; i < archivos_seleccionados.length; i++) {
            direccion = "_data/" + archivos_seleccionados[i];
            aeropuertos[i] = new Aeropuerto(direccion);
        }

        if (parametros[ALGORITHM].toLowerCase().equals(GREEDY)) {
            Greedy greedy[] = new Greedy[archivos_seleccionados.length];
            int solucion_greedy[];
            for (int i = 0; i < archivos_seleccionados.length; i++) {
                greedy[i] = new Greedy(aeropuertos[i]);
                solucion_greedy = greedy[i].algoritmoGreedy();

                System.out.printf("%s: \n", nombres_archivos[i]);
                for (int j = 0; j < greedy[i].tam; j++) {
                    System.out.printf("%d ", solucion_greedy[j]);
                }
                System.out.printf("\nCoste: %d \n\n", Utils.calcularCoste(solucion_greedy, aeropuertos[i]));
            }
        }
        if (parametros[ALGORITHM].toLowerCase().equals(BL)) {
            int semilla = Integer.parseInt(parametros[SEED]);
            int[] solucion_BL;
            String fichero_log;
            File directorio = new File("_logs");
            directorio.mkdir();

            BusquedaLocal[] busqueda_local = new BusquedaLocal[archivos_seleccionados.length];
            for (int i = 0; i < archivos_seleccionados.length; i++) {
                busqueda_local[i] = new BusquedaLocal(semilla, aeropuertos[i].numPuertas);
                busqueda_local[i].algoritmoBusquedaLocal(aeropuertos[i]);
                solucion_BL = busqueda_local[i].getSolucion();

                //Escribimos en archivo log
                String[] nombre_sin_formato = archivos_seleccionados[i].split("\\.");
                fichero_log = "_logs/log" + nombre_sin_formato[0] + ".txt";
                Utils.escribirFichero(fichero_log, contenidoLog);

                System.out.printf("\n%s: \n", aeropuertos[i].nombreArchivo);
                for (int j = 0; j < aeropuertos[i].numPuertas; j++) {
                    System.out.printf("%d ", solucion_BL[j]);
                }
                System.out.printf("\nCoste BL: %d\n", busqueda_local[i].getCosteSolucion());
            }
        }
        if (parametros[ALGORITHM].toLowerCase().equals(TABU)) {

        }
    } // main()
} // Main()
