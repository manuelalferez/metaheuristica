package meta;

import java.util.Random;

public class BusquedaTabu {
    static int NUM_VECINOS = 10;
    static int NUM_EVALUACIONES = 50000;
    String contenido_log = "";
    Random random;
    int[][] lista_tabu;

    int[] s_act;
    int coste_s_act;
    int tam;

    public BusquedaTabu(int _seed, int _tam) {
        random = new Random(_seed);
        tam = _tam;
        lista_tabu = new int[tam][tam];
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
                lista_tabu[i][j] = 0;
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
        } while (lista_tabu[rs[0]][rs[1]] == 1 || rs[0] == rs[1]);
        lista_tabu[rs[0]][rs[1]] = 1;
        return rs;
    }

    public void BusquedaTabu(Aeropuerto aeropuerto) {
        generar_solucion_inicial();
        coste_s_act = Main.calcularCoste(s_act, aeropuerto);

        int coste_mejor_vecino = coste_s_act;
        int coste_vecino;
        int evaluaciones = 0, intentos = 0, entorno = 0;

        // r: 0
        // s: 1
        int[] mejor_vecino = new int[2];
        int[] rs = new int[2];
        escribirSolucionInicial(evaluaciones);
        do {
            for (int i = 0; i < NUM_VECINOS; i++) {
                rs = generarVecino();
                coste_vecino = Main.calcularCosteParametrizado(s_act, coste_s_act, aeropuerto, rs[0], rs[1]);

                if (coste_vecino < coste_mejor_vecino) {
                    mejor_vecino[0] = rs[0];
                    mejor_vecino[1] = rs[1];
                    coste_mejor_vecino = coste_vecino;
                }
            }
            if (coste_mejor_vecino < coste_s_act) {
                Main.realizarMovimiento(s_act, mejor_vecino[0], mejor_vecino[1]);
                coste_s_act = coste_mejor_vecino;
                evaluaciones++;
                intentos = 0;
                escribirMovimiento(entorno, rs, coste_s_act, evaluaciones);
            } else {
                intentos += 1;
            }
            entorno++;
            limpiarMatriz();
        } while (evaluaciones < 50000 && intentos != 100);
    }

    public int[] getSolucion() {
        return this.s_act;
    }

    public int getCosteSolucion() {
        return this.coste_s_act;
    }


    public void escribirMovimiento(int _entorno, int[] _movimiento, int _coste, int _iteracion) {
        contenido_log += "\nEntorno:             " + _entorno;
        contenido_log += "\nMovimiento:          " + _movimiento[0] + " " + _movimiento[1];
        contenido_log += "\nCoste:               " + _coste;
        contenido_log += "\nIteración:           " + _iteracion;
        contenido_log += "\n----------------------------------------";
    }

    public void escribirSolucionInicial(int _iteracion) {
        contenido_log += "\n-------------------------------------------------------------------------------";
        contenido_log += "\nSolución inicial:    ";
        for (int j = 0; j < s_act.length; j++) {
            contenido_log += s_act[j] + " ";
        }

        contenido_log += "\nCoste:               " + coste_s_act;
        contenido_log += "\nIteración:           " + _iteracion;
        contenido_log += "\n-------------------------------------------------------------------------------";
    }
}
