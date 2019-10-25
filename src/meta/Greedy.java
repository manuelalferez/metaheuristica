package meta;

class Greedy {
    private int[] sumatorioFlujos;
    private int[] sumatorioDistancias;
    private int tamSolucion;
    private Solucion solucion;

    private static final int MARCA = -1; // Posición añadida a la solución

    // i=0 -> Posición del mejor flujo
    // i=1 -> Posición de la mejor distancia
    private static int[] mejoresPosiciones = new int[2];;

    Greedy() {
        tamSolucion = Main.aeropuertoActual.numPuertas;
        sumatorioFlujos = new int[tamSolucion];
        sumatorioDistancias = new int[tamSolucion];

        // Creación de los vectores suma de flujos y distancias
        for (int i = 0; i < tamSolucion; i++)
            for (int j = 0; j < tamSolucion; j++) {
                sumatorioFlujos[i] += Main.aeropuertoActual.flujos[i][j];
                sumatorioDistancias[i] += Main.aeropuertoActual.distancias[i][j];
            }
        solucion = new Solucion(tamSolucion);
    }

    void algoritmoGreedy() {
        int tamLogicoSolucion = 0;
        do {
            seleccionarMejoresPosiciones();
            eliminarMejores();
            solucion.solucion[mejoresPosiciones[0]]=mejoresPosiciones[1];
            tamLogicoSolucion++;
        }while (tamLogicoSolucion< tamSolucion);

        solucion.coste = Utils.calcularCoste(solucion.solucion);
    }

    /**
     * Selecciona el mejor flujo y la mejor distancia
     */
    private void seleccionarMejoresPosiciones() {
        int val_mejor_flujo = Integer.MIN_VALUE;
        int val_mejor_dist = Integer.MAX_VALUE;

        for (int i = 0; i < tamSolucion; i++) {
            if (sumatorioFlujos[i] != MARCA) {
                if (sumatorioFlujos[i] > val_mejor_flujo) {
                    val_mejor_flujo = sumatorioFlujos[i];
                    mejoresPosiciones[0] = i;
                }
            }
            if (sumatorioDistancias[i] != MARCA) {
                if (sumatorioDistancias[i] < val_mejor_dist) {
                    val_mejor_dist = sumatorioDistancias[i];
                    mejoresPosiciones[1] = i;
                }
            }
        }
    }

    /**
     * Marcamos las posiciones de los mejores dentro de cada matriz para no volver a sacarlos
     */
    private void eliminarMejores(){
        sumatorioFlujos[mejoresPosiciones[0]]= MARCA;
        sumatorioDistancias[mejoresPosiciones[1]]= MARCA;
    }

    Solucion getSolucion() {
        return this.solucion;
    }
} // class Greedy
