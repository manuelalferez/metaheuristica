package meta;

public class Main {
    // Datos
    static final int NUM_ARCHIVOS = 8;
    static final String[] nombres_archivos = {"madrid01.dat", "madrid02.dat", "madrid03.dat",
            "madrid04.dat", "malaga01.dat", "malaga02.dat", "malaga03.dat", "malaga04.dat"};

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
