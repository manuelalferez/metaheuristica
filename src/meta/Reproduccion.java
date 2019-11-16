package meta;

/**
 * Clase que representa los elementos básicos de reproducción de dos Individuos, así como la
 * forma en la que pueden reproducirse
 */
public class Reproduccion {
    private int[][] progenitores;
    private int TAM_PROGENITORES;

    // Variables de soporte
    private int puntoDeCorte;
    private boolean[][] valoresAgregados;
    private int POS_PADRE = 0, POS_MADRE = 1, NUM_PROGENITORES = 2;
    private int[] cotaInferior;

    private int progenitorOrigen;

    Reproduccion(int[] padre, int[] madre) {
        this.TAM_PROGENITORES = padre.length;
        this.progenitores = new int[NUM_PROGENITORES][TAM_PROGENITORES];

        for (int i = 0; i < TAM_PROGENITORES; i++) {
            this.progenitores[POS_PADRE][i] = padre[i];
            this.progenitores[POS_MADRE][i] = madre[i];
        }
    }

    private void cruceMOC() {
        puntoDeCorte = Main.random.nextInt(TAM_PROGENITORES);
        valoresAgregados = new boolean[NUM_PROGENITORES][TAM_PROGENITORES];
        eliminacionCruzada();
        rellenarValoresCruzados();
    }

    private void eliminacionCruzada() {
        for (int i = 0; i < TAM_PROGENITORES; i++) {
            estaAntesDePuntoDeCorte(POS_PADRE, i);
            estaAntesDePuntoDeCorte(POS_MADRE, i);
        }
    }

    /**
     * Chequea si la posicionEvaluada esta dentro de los valores anteriores al punto de corte
     */
    private void estaAntesDePuntoDeCorte(int progenitorEvaluado, int posicionEvaluada) {
        int elOtroProgenitor;
        if (progenitorEvaluado == POS_PADRE) elOtroProgenitor = POS_MADRE;
        else elOtroProgenitor = POS_PADRE;
        for (int i = 0; i < puntoDeCorte; i++) {
            if (progenitores[progenitorEvaluado][posicionEvaluada] == progenitores[elOtroProgenitor][i]) {
                valoresAgregados[progenitorEvaluado][posicionEvaluada] = true;
                break;
            }
        }
    }

    private void rellenarValoresCruzados() {
        int[][] copiaProgenitores = progenitores.clone();
        cotaInferior = new int[NUM_PROGENITORES];
        for (int i = puntoDeCorte; i < TAM_PROGENITORES; i++) {
            rellenarValor(POS_MADRE, copiaProgenitores[POS_PADRE][i]);
            rellenarValor(POS_PADRE, copiaProgenitores[POS_MADRE][i]);
        }
    }

    private void rellenarValor(int progenitorDestino, int valor) {
        while (!valoresAgregados[progenitorDestino][cotaInferior[progenitorDestino]]) {
            if (!valoresAgregados[progenitorDestino][cotaInferior[progenitorDestino]]) {
                progenitores[progenitorDestino][cotaInferior[progenitorDestino]] = valor;
                valoresAgregados[progenitorDestino][cotaInferior[progenitorDestino]] = true;
                cotaInferior[progenitorDestino]++;
            } else cotaInferior[progenitorDestino]++;
        }
    }

    private void cruceOX2() {
        progenitorOrigen = 0;
        while(progenitorOrigen<NUM_PROGENITORES){
            valoresAgregados = new boolean[NUM_PROGENITORES][TAM_PROGENITORES];
            seleccionAleatoria();
            eliminacionProgenitorDestino();
            rellenarValoresCruzados();
            progenitorOrigen++;
            //TODO comprobar
        }

    }

    private void seleccionAleatoria() {
        for (int i = 0; i < TAM_PROGENITORES; i++) {
            double aleatorio = Main.random.nextDouble();
            if (aleatorio > 0.5) {
                valoresAgregados[progenitorOrigen][i]=true;
            }
        }
    }

    private void eliminacionProgenitorDestino(){
        //TODO implementar
    }
}
