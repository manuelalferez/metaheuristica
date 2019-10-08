package meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

public class Main {
    /*
        Parámetros contenidos en una Array de Strings. Distribución dentro del Array:
        0: algorithm
        1: input
        2: seed
        3: output
     */
    protected static String[] parametros = new String[4];

    /**
     * @param solucion   Vector con la solución del problema
     * @param aeropuerto Fabrica con las matrices de piezas transferidas y las distancias entre unidades de producción
     * @return El peso de la solucion
     * @brief Calcula el peso de la solución
     */


    public static int calcularCoste(int[] solucion, Aeropuerto aeropuerto) {
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

    public static int calcularCosteParcial(int[] solucion, Aeropuerto aeropuerto, int r, int s){
        int coste = 0;

        if(aeropuerto.getEsSimetrica()){
            for (int i = 0; i < solucion.length; i++) {
                if (i != r) coste += 2* aeropuerto.flujos[r][i] * aeropuerto.distancias[solucion[r]][solucion[i]];
                if (i != s) coste += 2* aeropuerto.flujos[s][i] * aeropuerto.distancias[solucion[s]][solucion[i]];
            }
        }else{
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

    public static void intercambio(int[]v, int r, int s){
        int aux = v[r];
        v[r] = v[s];
        v[s] = aux;
    }

    public static int calcularCosteParametrizado(int[] permutacion, int coste, Aeropuerto aeropuerto, int r, int s) {
        int costeP_A = 0, costeP_D=0;
        costeP_A = calcularCosteParcial(permutacion,aeropuerto, r,s);
        intercambio(permutacion,r,s);
        costeP_D = calcularCosteParcial(permutacion,aeropuerto, r,s);
        // Deshacemos el intercambio
        intercambio(permutacion,r,s);

        return coste+costeP_D-costeP_A;
    } // calcularCoste()


    public static void lecturaArchivoConf(String dir) {
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
/*            System.out.printf(parametros[0]);
            System.out.printf("\n");
            System.out.printf(parametros[1]);
            System.out.printf("\n");
            System.out.println(parametros[2]);
            System.out.printf(parametros[3]);
            System.out.printf("\n");*/
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

        int semilla = Integer.parseInt(parametros[2]);

        BusquedaLocal busqueda_local = new BusquedaLocal(semilla,aeropuertos[5].num_puertas);
        busqueda_local.algoritmoBusquedaLocal(aeropuertos[5]);
        int[] solucion_BL = busqueda_local.getSolucion();

        System.out.printf("%s: \n",aeropuertos[5].nombre_archivo);
        for (int j = 0; j < aeropuertos[5].num_puertas; j++) {
            System.out.printf("%d ", solucion_BL[j]);
        }
        System.out.printf("\nCoste BL: %d\n", busqueda_local.getCosteSolucion());
    } // main()
} // Main()
