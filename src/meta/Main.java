package meta;

import java.io.*;
import java.util.Random;

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
    static Aeropuerto aeropuertoActual;

    private static String[] nombres_archivos;
    private static Aeropuerto[] aeropuertos;
    private static String ficheroLogNombre;
    static Random random;

    /**
     * Lectura del archivo de configuración y almacenamiento de parámetros en el vector de parámetros (variable declarada
     * en el Main)
     */
    private static void lecturaParametrosConfiguracion() {
        File archivo;
        FileReader fr = null;
        BufferedReader br;

        try {
            String direccion_parametros = "_data/parametros.csv";
            archivo = new File(direccion_parametros);
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
    }

    /**
     * Los datos se encuentran en archivos de texto con extensión .dat, dentro de la carpeta /_data
     */
    private static void lecturaNombresArchivosDatos(){
        File folder = new File("_data/");
        File[] listOfFiles = folder.listFiles(((dir, name) -> name.toLowerCase().endsWith(".dat")));
        int numArchivos;
        if(listOfFiles!=null) {
            numArchivos = listOfFiles.length;
            nombres_archivos = new String[numArchivos];

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    nombres_archivos[i] = listOfFiles[i].getName();
                }
            }
        }
    }

    /**
     * Devuelve un Array con los archivos seleccionados en el archivo de parámetros
     */
    private static String[] seleccionarArchivos(){
        String[] archivosSeleccionados;
        if (parametros[INPUT].equals(TODOS_ARCHIVOS)) {
            archivosSeleccionados = nombres_archivos;
        } else {
            String[] linea_troceada = parametros[INPUT].split(" ");
            archivosSeleccionados = new String[linea_troceada.length];

            for (int i = 0; i < linea_troceada.length; i++)
                archivosSeleccionados[i] = linea_troceada[i];
        }
        return archivosSeleccionados;
    }

    private static void crearAeropuertos(String[] archivosSeleccionados){
        String direccionDeUnAeropuerto;
        aeropuertos = new Aeropuerto[archivosSeleccionados.length];
        for (int i = 0; i < archivosSeleccionados.length; i++) {
            direccionDeUnAeropuerto = "_data/" + archivosSeleccionados[i];
            aeropuertos[i] = new Aeropuerto(direccionDeUnAeropuerto);
        }
    }

    private static void escribirSolucionYTiempos(Solucion solucion, long tiempoEjecucionMilisegundos){
        contenidoLog += "Solución: ";
        for (int j = 0; j < solucion.solucion.length; j++) {
            contenidoLog += solucion.solucion[j] + " ";
        }
        contenidoLog += "\nCoste: " + solucion.coste;
        contenidoLog += "\nTiempo: " + tiempoEjecucionMilisegundos/1000 +" s";
    }

    /**
     * Crea el nombre del archivo log a partir del archivo de datos (forma: archivo.dat)
     */
    private static void crearFicheroLog(String nombreArchivoDatos){
        String[] nombreSinFormato = nombreArchivoDatos.split("\\."); // Eliminamos .dat
        ficheroLogNombre = "_logs/log" + nombreSinFormato[0] + ".txt";
    }

    public static void main(String[] args) {
        lecturaParametrosConfiguracion();
        lecturaNombresArchivosDatos();

        String[] archivos_seleccionados = seleccionarArchivos();

        long T_INICIO, T_FIN , T_EJECUCION; //Variables para determinar el tiempo de ejecución

        crearAeropuertos(archivos_seleccionados);

        Solucion solucion = new Solucion();

        if (parametros[ALGORITHM].toLowerCase().equals(GREEDY)) {
            Greedy[] greedy = new Greedy[archivos_seleccionados.length];

            for (int i = 0; i < archivos_seleccionados.length; i++) {
                aeropuertoActual = aeropuertos[i];

                greedy[i] = new Greedy();
                T_INICIO = System.currentTimeMillis();
                greedy[i].algoritmoGreedy();
                T_FIN = System.currentTimeMillis();
                T_EJECUCION = T_FIN - T_INICIO;

                escribirSolucionYTiempos(solucion,T_EJECUCION );
                crearFicheroLog(archivos_seleccionados[i]);
                Utils.escribirFichero(ficheroLogNombre, contenidoLog);
            }
        }
        if (parametros[ALGORITHM].toLowerCase().equals(BL)) {
            random= new Random(Integer.parseInt(parametros[SEED]));
            String fichero_log;
            File directorio = new File("_logs");
            directorio.mkdir();

            BusquedaLocal[] busqueda_local = new BusquedaLocal[archivos_seleccionados.length];
            for (int i = 0; i < archivos_seleccionados.length; i++) {
                busqueda_local[i] = new BusquedaLocal(aeropuertos[i].numPuertas);
                aeropuertoActual = aeropuertos[i];
                T_INICIO = System.currentTimeMillis();
                busqueda_local[i].algoritmoBusquedaLocal();
                T_FIN = System.currentTimeMillis();
                T_EJECUCION = T_FIN - T_INICIO;
                solucion.copiar(busqueda_local[i].getSolucion());

                //Escribimos en archivo log
                String[] nombre_sin_formato = archivos_seleccionados[i].split("\\.");
                fichero_log = "_logs/log" + nombre_sin_formato[0] + ".txt";
                Utils.escribirFichero(fichero_log, contenidoLog);

                contenidoLog += "\nSolución: ";
                for (int j = 0; j < aeropuertos[i].numPuertas; j++) {
                    contenidoLog+=solucion.solucion[j]+" ";
                }
                contenidoLog += "\nCoste: " + busqueda_local[i].getCosteSolucion();
                contenidoLog += "\nTiempo: " + T_EJECUCION/1000 +" s";
                Utils.escribirFichero(fichero_log, contenidoLog);
            }
        }
        if (parametros[ALGORITHM].toLowerCase().equals(TABU)) {
            random= new Random(Integer.parseInt(parametros[SEED]));
            String fichero_log;
            File directorio = new File("_logs");
            directorio.mkdir();

            BusquedaTabu[] busquedaTabu = new BusquedaTabu[archivos_seleccionados.length];
            for (int i = 0; i < archivos_seleccionados.length; i++) {
                busquedaTabu[i] = new BusquedaTabu(aeropuertos[i].numPuertas);
                aeropuertoActual = aeropuertos[i];
                T_INICIO = System.currentTimeMillis();
                busquedaTabu[i].algoritmoTabu();
                T_FIN = System.currentTimeMillis();
                T_EJECUCION = T_FIN - T_INICIO;
                solucion = busquedaTabu[i].getSolucion();
                //Escribimos en archivo log
                String[] nombre_sin_formato = archivos_seleccionados[i].split("\\.");
                fichero_log = "_logs/log" + nombre_sin_formato[0] + ".txt";

                contenidoLog += "\nSolución: ";
                for (int j = 0; j < aeropuertos[i].numPuertas; j++) {
                    contenidoLog+=solucion.solucion[j]+" ";
                }
                contenidoLog += "\nCoste: " + busquedaTabu[i].getCosteSolucion();
                contenidoLog += "\nTiempo: " + T_EJECUCION/1000 +" s";
                Utils.escribirFichero(fichero_log, contenidoLog);
                //COmentario
            }
        }
    } // main()
} // Main()
