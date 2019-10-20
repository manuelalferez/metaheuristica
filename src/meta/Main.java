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

    private static final String TODOS_ARCHIVOS = "all";
    private static final String GREEDY = "greedy";
    private static final String BL = "bl";
    private static final String TABU = "tabu";

    private static final int ALGORITHM = 0;
    private static final int INPUT = 1;
    private static final int SEED = 2;

    /**
     * @param mensaje Mensaje a escribir en el fichero
     * @brief Método que escribe en un fichero
     * @post Escribir un mensaje en un fichero llamado solucion.txt
     */
    private static void escribirFichero(String _nombre_fichero, String _mensaje) {
        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(_nombre_fichero, false);
            pw = new PrintWriter(fichero);
            pw.println(_mensaje);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Para asegurarnos que se cierra el fichero
                if (null != fichero)
                    fichero.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    } // escribirFichero()

    /**
     * @param solucion   Vector con la solución del problema
     * @param aeropuerto Fabrica con las matrices de piezas transferidas y las distancias entre unidades de producción
     * @return El peso de la solucion
     * @brief Calcula el peso de la solución
     */
    static int calcularCoste(int[] solucion, Aeropuerto aeropuerto) {
        int peso = 0;

        if (aeropuerto.getEsSimetrica()) {
            for (int i = 0; i < solucion.length; i++)
                for (int j = i + 1; j < solucion.length; j++)
                    peso += 2 * (aeropuerto.flujos[i][j] * aeropuerto.distancias[solucion[i]][solucion[j]]);
        } else {
            for (int i = 0; i < solucion.length; i++)
                for (int j = 0; j < solucion.length; j++)
                    if (i != j)
                        peso += aeropuerto.flujos[i][j] * aeropuerto.distancias[solucion[i]][solucion[j]];
        }
        return peso;
    } // calcularCoste()

    private static int calcularCosteParcial(int[] solucion, Aeropuerto aeropuerto, int r, int s) {
        int coste = 0;

        if (aeropuerto.getEsSimetrica()) {
            for (int i = 0; i < solucion.length; i++) {
                if (i != r) coste += 2 * aeropuerto.flujos[r][i] * aeropuerto.distancias[solucion[r]][solucion[i]];
                if (i != s) coste += 2 * aeropuerto.flujos[s][i] * aeropuerto.distancias[solucion[s]][solucion[i]];
            }
        } else {
            for (int i = 0; i < solucion.length; i++) {
                if (i != r) {
                    coste += aeropuerto.flujos[r][i] * aeropuerto.distancias[solucion[r]][solucion[i]];
                    coste += aeropuerto.flujos[i][r] * aeropuerto.distancias[solucion[i]][solucion[r]];
                }
                if (i != s) {
                    coste += aeropuerto.flujos[s][i] * aeropuerto.distancias[solucion[s]][solucion[i]];
                    coste += aeropuerto.flujos[i][s] * aeropuerto.distancias[solucion[i]][solucion[s]];
                }
            }
        }
        return coste;
    }

    static void intercambio(int[] v, int r, int s) {
        int aux = v[r];
        v[r] = v[s];
        v[s] = aux;
    }

    static int calcularCosteParametrizado(int[] permutacion, int coste, Aeropuerto aeropuerto, int r, int s) {
        int costeP_A = 0, costeP_D = 0;
        costeP_A = calcularCosteParcial(permutacion, aeropuerto, r, s);
        intercambio(permutacion, r, s);
        costeP_D = calcularCosteParcial(permutacion, aeropuerto, r, s);
        // Deshacemos el intercambio
        intercambio(permutacion, r, s);

        return coste + costeP_D - costeP_A;
    } // calcularCoste()


    private static void lecturaArchivoConf(String dir) {
        File archivo;
        FileReader fr = null;
        BufferedReader br;

        try {
            archivo = new File(dir);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;
            int i = 0;
            while ((linea = br.readLine()) != null) {
                String[] linea_troceada = linea.split(";");
                parametros[i++] = linea_troceada[1];
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos que se cierra
            // tanto como si va bien la lectura como si no
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    } //lecturaArchivoConf()

    public static void main(String[] args) {
        String archivo_conf = args[0];
        String direccion_parametros = "_data/" + archivo_conf;
        lecturaArchivoConf(direccion_parametros);

        // Datos
        File folder = new File("_data/");
        File[] listOfFiles = folder.listFiles((new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".dat");
            }
        }));
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
                System.out.printf("\nCoste: %d \n\n", calcularCoste(solucion_greedy, aeropuertos[i]));
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
                escribirFichero(fichero_log, busqueda_local[i].contenidoLog);

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
