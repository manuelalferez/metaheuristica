package meta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Aeropuerto {
    protected int[][] flujos;
    protected int[][] distancias;
    protected int numPuertas;
    protected String nombreArchivo;
    protected boolean esSimetrica;

    public Aeropuerto(String direccionArchivo) {
        File archivo;
        FileReader fr = null;
        BufferedReader br;

        try {
            archivo = new File(nombreArchivo = direccionArchivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;

            // Lectura de la dimensión
            linea = br.readLine().replaceAll(" +", " ").trim();
            numPuertas = Integer.parseInt(linea);

            // Creación de las matrices
            flujos = new int[numPuertas][numPuertas];
            distancias = new int[numPuertas][numPuertas];

            // Inicialización de las matrices
            for (int i = 0; i < numPuertas; i++)
                for (int j = 0; j < numPuertas; j++) {
                    flujos[i][j] = 0;
                    distancias[i][j] = 0;
                }

            br.readLine(); // Eliminamos línea separatoria

            int fila = 0;
            int col = 0;
            String[] linea_troceada;
            // Leemos matriz de flujos
            while ((fila < numPuertas)) {
                linea = br.readLine().replaceAll(" +", " ").trim();
                linea_troceada = linea.split(" ");

                for (int p = 0; p < linea_troceada.length; p++) {
                    flujos[fila][col] = Integer.parseInt(linea_troceada[p]);
                    col++;
                }

                if (col == numPuertas) {
                    fila++;
                    col = 0;
                }
            }

            fila = 0;
            col = 0;
            br.readLine();

            // Leemos matriz de distancias
            while ((fila < numPuertas)) {
                linea = br.readLine().replaceAll(" +", " ").trim();
                linea_troceada = linea.split(" ");

                for (int p = 0; p < linea_troceada.length; p++) {
                    distancias[fila][col] = Integer.parseInt(linea_troceada[p]);
                    col++;
                }

                if (col == numPuertas) {
                    fila++;
                    col = 0;
                }
            }
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
        esSimetrica = esSimetrica();
    }

    protected boolean esSimetrica() {
        for (int i = 0; i < this.numPuertas; i++)
            for (int j = i + 1; j < this.numPuertas; j++) {
                if (this.flujos[i][j] != this.flujos[j][i])
                    return false;
            }
        return true;
    }

    public boolean getEsSimetrica() {
        return this.esSimetrica;
    }
}
