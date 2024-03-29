package meta;

/**
 * Clase que representa los elementos básicos de reproducción de dos Individuos, así como la
 * forma en la que pueden reproducirse
 */
public class Reproduccion {
    private Solucion[] progenitores;
    private int TAM_PROGENITORES;

    private int POS_PADRE = 0, POS_MADRE = 1, NUM_PROGENITORES = 2;
    private Solucion[] copiaProgenitores;

    // Variables de soporte
    private int puntoDeCorte;
    private boolean[][] posMarcadasMOC;
    private int[] cotaInferior;

    private int progenitorOrigen, progenitorDestino;
    private int[][] posMarcadasOX2;
    private int[] tamPosMarcadasOX2;

    Reproduccion(Solucion padre, Solucion madre) {
        this.progenitores = new Solucion[NUM_PROGENITORES];
        this.TAM_PROGENITORES = padre.solucion.length;

        this.progenitores[POS_PADRE] = new Solucion(padre);
        this.progenitores[POS_MADRE] = new Solucion(madre);
    }

    void cruceMOC() {
        puntoDeCorte = Main.random.nextInt(TAM_PROGENITORES - 1) + 1;
        System.out.println(puntoDeCorte);
        posMarcadasMOC = new boolean[NUM_PROGENITORES][TAM_PROGENITORES];
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
    private void estaAntesDePuntoDeCorte(int progenitorEvaluado, int posEvaluada) {
        int elOtroProgenitor;
        if (progenitorEvaluado == POS_PADRE) elOtroProgenitor = POS_MADRE;
        else elOtroProgenitor = POS_PADRE;
        for (int i = 0; i < puntoDeCorte; i++) {
            if (progenitores[progenitorEvaluado].solucion[posEvaluada] == progenitores[elOtroProgenitor].solucion[i]) {
                posMarcadasMOC[progenitorEvaluado][posEvaluada] = true;
                break;
            }
        }
    }

    private void rellenarValoresCruzados() {
        copiaProgenitores = new Solucion[NUM_PROGENITORES];
        copiaProgenitores[POS_PADRE] = new Solucion(progenitores[POS_PADRE]);
        copiaProgenitores[POS_MADRE]= new Solucion(progenitores[POS_MADRE]);
        cotaInferior = new int[NUM_PROGENITORES];
        for (int i = puntoDeCorte; i < TAM_PROGENITORES; i++) {
            rellenarValor(POS_MADRE, copiaProgenitores[POS_PADRE].solucion[i]);
            rellenarValor(POS_PADRE, copiaProgenitores[POS_MADRE].solucion[i]);
        }
    }

    private void rellenarValor(int progenitorDestino, int valor) {
        while (posMarcadasMOC[progenitorDestino][cotaInferior[progenitorDestino]]) {
            cotaInferior[progenitorDestino]++;
        }
        progenitores[progenitorDestino].solucion[cotaInferior[progenitorDestino]] = valor;
        cotaInferior[progenitorDestino]++;
    }

    void cruceOX2() {
        progenitorOrigen = 0;
        crearCopiaProgenitores();
        while (progenitorOrigen < NUM_PROGENITORES) {
            inicializarVariables();
            seleccionAleatoria();
            eliminacionProgenitorDestino();
            rellenarValores();
            progenitorOrigen++;
        }
    }

    private void crearCopiaProgenitores() {
        copiaProgenitores = new Solucion[NUM_PROGENITORES];
        for (int i = 0; i < NUM_PROGENITORES; i++)
            copiaProgenitores[i] = new Solucion(progenitores[i]);
    }

    private void inicializarVariables() {
        posMarcadasOX2 = new int[NUM_PROGENITORES][TAM_PROGENITORES];
        tamPosMarcadasOX2 = new int[NUM_PROGENITORES];
        if (progenitorOrigen == POS_PADRE)
            progenitorDestino = POS_MADRE;
        else
            progenitorDestino = POS_PADRE;
    }

    private void seleccionAleatoria() {
        for (int i = 0; i < TAM_PROGENITORES; i++) {
            double aleatorio = Main.random.nextDouble();
            if (aleatorio > 0.5)
                posMarcadasOX2[progenitorOrigen][tamPosMarcadasOX2[progenitorOrigen]++] = i;
        }
    }

    private void eliminacionProgenitorDestino() {
        for (int i = 0; i < TAM_PROGENITORES; i++)
            marcarSiEsta(i);
    }

    private void marcarSiEsta(int posEvaluada) {
        for (int i = 0; i < tamPosMarcadasOX2[progenitorOrigen]; i++) {
            if (copiaProgenitores[progenitorDestino].solucion[posEvaluada] ==
                    copiaProgenitores[progenitorOrigen].solucion[posMarcadasOX2[progenitorOrigen][i]]) {
                posMarcadasOX2[progenitorDestino][tamPosMarcadasOX2[progenitorDestino]++] = posEvaluada;
                break;
            }
        }
    }

    private void rellenarValores() {
        for (int i = 0; i < tamPosMarcadasOX2[progenitorOrigen]; i++)
            progenitores[progenitorDestino].solucion[posMarcadasOX2[progenitorDestino][i]] =
                    copiaProgenitores[progenitorOrigen].solucion[posMarcadasOX2[progenitorOrigen][i]];
    }

    Solucion getPrimerProgenitor() {
        return progenitores[POS_PADRE];
    }

    Solucion getSegundoProgenitor() {
        return progenitores[POS_MADRE];
    }
}
