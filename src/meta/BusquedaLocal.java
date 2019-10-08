package meta;

import java.util.Random;
import java.util.Vector;

public class BusquedaLocal {
    static int NUM_VECINOS = 10; //Preguntar
    static int NUM_EVALUACIONES = 50000;
    Random random;
    int[][] matriz_generar_vecinos;

    int[] s_act;
    int coste_s_act;
    int tam;

    public BusquedaLocal(int _seed, int _tam) {
        random = new Random(_seed);
        tam = _tam;
        matriz_generar_vecinos = new int[tam][tam];
    }

    public void generar_solucion_inicial() {
        int[] posiciones = new int[tam];
        int taml = tam;
        for (int i = 0; i < tam; i++) posiciones[i] = i;
        s_act = new int[tam];

        while (taml != 0) {
            int number = random.nextInt(taml);
            s_act[tam - taml] = posiciones[number];
            posiciones[number] = posiciones[taml - 1];
            taml--;
        }
    } //generar_solucion_inicial()

    private void limpiarMatriz() {
        for (int i = 0; i < tam; i++)
            for (int j = i + 1; j < tam; j++)
                matriz_generar_vecinos[i][j] = 0;
    }

    public int[] generarVecino() {
        int rs[] = new int[2];
        do {
            rs[0] = random.nextInt(tam);
            rs[1] = random.nextInt(tam);
            if (rs[0] > rs[1]) {
                int aux = rs[0];
                rs[0] = rs[1];
                rs[1] = aux;
            }
        } while (matriz_generar_vecinos[rs[0]][rs[1]] == 1 || rs[0] == rs[1]);
        matriz_generar_vecinos[rs[0]][rs[1]] = 1;
        return rs;
    }

    public void algoritmoBusquedaLocal(Aeropuerto aeropuerto) {
        generar_solucion_inicial();
        coste_s_act = Main.calcularCoste(s_act, aeropuerto);
        int coste_mejor_vecino = coste_s_act;
        int coste_vecino = 0;
        // r: 0
        // s: 1
        int[] mejor_vecino = new int[2];
        int[] rs;

        int evaluaciones = 0;
        int intentos = 0;

        do {
            for (int i = 0; i < NUM_VECINOS; i++) {
                rs = generarVecino();
                coste_vecino = Main.calcularCosteParametrizado(s_act, coste_s_act, aeropuerto, rs[0], rs[1]);
                evaluaciones++; //Preguntar
                if (coste_vecino < coste_mejor_vecino) {
                    mejor_vecino[0] = rs[0];
                    mejor_vecino[1] = rs[1];
                    coste_mejor_vecino = coste_vecino;
                }
            }
            if (coste_mejor_vecino < coste_s_act) {
                System.out.print("Solución actual: \n");
                for (int j = 0; j < aeropuerto.num_puertas; j++) {
                    System.out.printf("%d ", s_act[j]);
                }
                System.out.printf("Coste solución actual: %d \n", coste_s_act);
                Main.intercambio(s_act, mejor_vecino[0], mejor_vecino[1]);
                System.out.print("Solución vecina: \n");
                for (int j = 0; j < aeropuerto.num_puertas; j++) {
                    System.out.printf("%d ", s_act[j]);
                }
                System.out.printf("Coste solución vecina: %d \n", coste_mejor_vecino);
                limpiarMatriz();
                coste_s_act= coste_mejor_vecino;
                intentos = 0;
            } else {
                intentos += 10;
            }
        } while (evaluaciones < 50000 && intentos != 100);
    }

    public int[] getSolucion() {
        return this.s_act;
    }

    public int getCosteSolucion() {
        return this.coste_s_act;
    }
}
