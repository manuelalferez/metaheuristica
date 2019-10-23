package meta;

public class Greedy {
    int[] sumatorio_flujos;
    int[] sumatorio_distancias;
    int tam;
    int[] sol;

    protected static final int MARCA = -1; // Posición añadida a la solución

    public Greedy() {
        tam = Main.aeropuertoActual.numPuertas;
        sumatorio_flujos = new int[tam];
        sumatorio_distancias = new int[tam];

        // Creación de los vectores suma de flujos y distancias
        for (int i = 0; i < tam; i++)
            for (int j = 0; j < tam; j++) {
                sumatorio_flujos[i] += Main.aeropuertoActual.flujos[i][j];
                sumatorio_distancias[i] += Main.aeropuertoActual.distancias[i][j];
            }
        sol = new int[tam];
    } // constructor()

    public int[] algoritmoGreedy() {
        // i=0 -> Posición del mejor flujo
        // i=1 -> Posición de la mejor distancia
        int[] pos_mejores = new int[2];
        int tam_sol = 0;
        do {
            seleccionar(pos_mejores);
            eliminar(pos_mejores);
            sol[pos_mejores[0]]=pos_mejores[1];
            tam_sol++;
        }while (tam_sol<tam);

        return sol;
    } // algoritmoGreedy()

    public void seleccionar(int[] _pos_mejores) {
        int val_mejor_flujo = Integer.MIN_VALUE;
        int val_mejor_dist = Integer.MAX_VALUE;

        for (int i = 0; i < tam; i++) {
            if (sumatorio_flujos[i] != MARCA) {
                if (sumatorio_flujos[i] > val_mejor_flujo) {
                    val_mejor_flujo = sumatorio_flujos[i];
                    _pos_mejores[0] = i;
                }
            }
            if (sumatorio_distancias[i] != MARCA) {
                if (sumatorio_distancias[i] < val_mejor_dist) {
                    val_mejor_dist = sumatorio_distancias[i];
                    _pos_mejores[1] = i;
                }
            }
        }
    } // seleccionar()

    public void eliminar(int[] _pos_mejores){
        sumatorio_flujos[_pos_mejores[0]]= MARCA;
        sumatorio_distancias[_pos_mejores[1]]= MARCA;
    } // eliminar()
} // class Greedy
