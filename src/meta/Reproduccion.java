package meta;

/**
 * Clase que representa los elementos básicos de reproducción de dos Individuos, así como la
 * forma en la que pueden reproducirse
 */
public class Reproduccion {
    int[][] progenitores;
    int TAM_PADRES;
    int NUM_PROGENITORES = 2;

    // Variables de soporte
    int puntoDeCorte;

    boolean[] estanEnHijos;
    int POS_PADRE = 0, POS_MADRE = 1;

    int[] cotaInferior;

    Reproduccion(int[] padre, int[] madre) {
        this.TAM_PADRES = padre.length;
        this.progenitores = new int[NUM_PROGENITORES][TAM_PADRES;]

        for (int i = 0; i < TAM_PADRES; i++) {
            this.progenitores[POS_PADRE] = padre[i];
            this.progenitores[POS_MADRE] = madre[i];
        }
    }

    private void cruceMOC() {
        puntoDeCorte = Main.random.nextInt(TAM_PADRES);
        eliminacionCruzada();
        rellenarValoresCruzados();
    }

    private void eliminacionCruzada() {
        this.estanEnHijos = new boolean[2];
        for (int i = 0; i < TAM_PADRES; i++) {
            estaAntesDePuntoDeCorte(POS_PADRE, i);
            estaAntesDePuntoDeCorte(POS_MADRE, i);
        }
    }

    /**
     * Chequea si la posicionEvaluada esta dentro de los valores anteriores al punto de corte
     */
    private void estaAntesDePuntoDeCorte(int progenitor, int posicionEvaluada) {
        int elOtroProgenitor;
        if (progenitor == POS_PADRE) elOtroProgenitor = POS_MADRE;
        else elOtroProgenitor = POS_PADRE;
        for (int i = 0; i < puntoDeCorte; i++) {
            if (this.progenitores[progenitor][posicionEvaluada] == this.progenitores[elOtroProgenitor][i]) {
                estanEnHijos[progenitor][posicionEvaluada] = true;
                break;
            }
        }
    }

    private void rellenarValoresCruzados() {
        int[][] copiaProgenitores = new int[NUM_PROGENITORES][TAM_PADRES];
        copiaProgenitores[POS_PADRE] = progenitores[POS_PADRE].clone();
        copiaProgenitores[POS_MADRE] = progenitores[POS_MADRE].clone();
        cotaInferior = new int[NUM_PROGENITORES];
        for (int i = this.puntoDeCorte; i < this.TAM_PADRES; i++) {
            rellenarValor(POS_MADRE, copiaProgenitores[POS_PADRE][i]);
            rellenarValor(POS_PADRE, copiaProgenitores[POS_MADRE][i]);
        }
    }

    private void rellenarValor(int progenitorDestino, int valor){
        while(!estanEnHijos[progenitorDestino][cotaInferior[progenitorDestino]]){
            if(this.estanEnHijos[progenitorDestino][cotaInferior[progenitorDestino]]==false){
                progenitores[progenitorDestino][cotaInferior[progenitorDestino]]=valor;
                estanEnHijos[progenitorDestino][cotaInferior[progenitorDestino]]=true;
                cotaInferior[progenitorDestino]++;
            }else cotaInferior[progenitorDestino]++;
        }
    }

    private void cruceOX2(int posPadre) {

    }
}
