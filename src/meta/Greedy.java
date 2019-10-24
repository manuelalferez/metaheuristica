package meta;

class Greedy {
    private int[] sumatorio_flujos;
    private int[] sumatorio_distancias;
    private int tam;
    private Solucion solucion;

    private static final int MARCA = -1; // Posición añadida a la solución

    // i=0 -> Posición del mejor flujo
    // i=1 -> Posición de la mejor distancia
    private static int[] mejoresPosiciones = new int[2];;

    Greedy() {
        tam = Main.aeropuertoActual.numPuertas;
        sumatorio_flujos = new int[tam];
        sumatorio_distancias = new int[tam];

        // Creación de los vectores suma de flujos y distancias
        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++) {
                sumatorio_flujos[i] += Main.aeropuertoActual.flujos[i][j];
                sumatorio_distancias[i] += Main.aeropuertoActual.distancias[i][j];
            }
        solucion = new Solucion(tam);
    }

    void algoritmoGreedy() {
        int tam_sol = 0;
        do {
            seleccionarMejoresPosiciones();
            eliminarMejores();
            solucion.solucion[mejoresPosiciones[0]]=mejoresPosiciones[1];
            tam_sol++;
        }while (tam_sol<tam);

        solucion.coste = Utils.calcularCoste(solucion.solucion);
    }

    /**
     * Selecciona el mejor flujo y la mejor distancia
     */
    private void seleccionarMejoresPosiciones() {
        int val_mejor_flujo = Integer.MIN_VALUE;
        int val_mejor_dist = Integer.MAX_VALUE;

        for (int i = 0; i < tam; i++) {
            if (sumatorio_flujos[i] != MARCA) {
                if (sumatorio_flujos[i] > val_mejor_flujo) {
                    val_mejor_flujo = sumatorio_flujos[i];
                    mejoresPosiciones[0] = i;
                }
            }
            if (sumatorio_distancias[i] != MARCA) {
                if (sumatorio_distancias[i] < val_mejor_dist) {
                    val_mejor_dist = sumatorio_distancias[i];
                    mejoresPosiciones[1] = i;
                }
            }
        }
    }

    /**
     * Marcamos las posiciones de los mejores dentro de cada matriz para no volver a sacarlos
     */
    private void eliminarMejores(){
        sumatorio_flujos[mejoresPosiciones[0]]= MARCA;
        sumatorio_distancias[mejoresPosiciones[1]]= MARCA;
    }

    Solucion getSolucion() {
        return this.solucion;
    }
} // class Greedy
