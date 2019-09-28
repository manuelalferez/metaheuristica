package meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Aeropuerto {
    protected int[][] flujos;
    protected int[][] distancias;
    protected int num_puertas;
    protected String nombre_archivo;

    public Aeropuerto(String _direccionArchivo) {
        File archivo;
        FileReader fr = null;
        BufferedReader br;

        try {
            archivo = new File(nombre_archivo = _direccionArchivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;

            // Lectura de la dimensión
            linea = br.readLine().replaceAll(" +", " ").trim();
            num_puertas = Integer.parseInt(linea);

            // Creación de las matrices
            flujos = new int[num_puertas][num_puertas];
            distancias = new int[num_puertas][num_puertas];

            // Inicialización de las matrices
            for (int i = 0; i < num_puertas; i++)
                for (int j = 0; j < num_puertas; j++) {
                    flujos[i][j] = 0;
                    distancias[i][j] = 0;
                }

            br.readLine(); // Eliminamos línea separatoria

            int fila = 0;
            int col = 0;
            String[] linea_troceada;
            // Leemos matriz de flujos
            while ((fila < num_puertas)) {
                linea = br.readLine().replaceAll(" +", " ").trim();
                linea_troceada = linea.split(" ");

                for (int p = 0; p < linea_troceada.length; p++) {
                    flujos[fila][col] = Integer.parseInt(linea_troceada[p]);
                    col++;
                }

                if (col == num_puertas) {
                    fila++;
                    col = 0;
                }
            }
  /*          for (int i = 0; i < num_puertas; i++) {
                System.out.printf("\n");
                for (int j = 0; j < num_puertas; j++) {
                    System.out.printf("%d ", flujos[i][j]);
                }
            }*/
            fila = 0;
            col = 0;
            br.readLine();

            // Leemos matriz de distancias
            while ((fila < num_puertas)) {
                linea = br.readLine().replaceAll(" +", " ").trim();
                linea_troceada = linea.split(" ");

                for (int p = 0; p < linea_troceada.length; p++) {
                    distancias[fila][col] = Integer.parseInt(linea_troceada[p]);
                    col++;
                }

                if (col == num_puertas) {
                    fila++;
                    col = 0;
                }
            }

 /*           System.out.printf("\n");
            for (int i = 0; i < num_puertas; i++) {
                System.out.printf("\n");
                for (int j = 0; j < num_puertas; j++) {
                    System.out.printf("%d ", distancias[i][j]);
                }
            }*/

        } catch (Exception e) {
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
}
