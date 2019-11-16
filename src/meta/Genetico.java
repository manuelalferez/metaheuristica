package meta;

public class Genetico {
    Poblacion poblacion;
    Poblacion poblacionDescendiente;
    private int TAM_TORNEO = 2;
    private int NUM_INDIVIDUOS_CRUZADOS = 2;
    private int posPadre;
    private int posMadre = posPadre + 1;

    public void algoritmoGenetico() {
        int iteraciones = 0;
        inicializarPoblacion();

        while (iteraciones < TAM) {
            evaluarPoblacion();
            //Calcular elite
            seleccionar();
            recombinar();
            mutar();
            //Calcular el o los elites
            reemplazar();
        }
    }

    public void inicializarPoblacion() {
        poblacion = new Poblacion(numIndividuos, numPuertas);
    }

    public void evaluarPoblacion() {
        poblacion.evaluarPoblacion();
    }

    public void seleccionar() {

    }

    private void recombinar() {
        double probabilidad = Main.random.nextDouble();
        int posPadre = 0; //posMadre = posPadre +1
        while (posPadre < poblacion.getTamPoblacion()) {
            if (probabilidad < 0.7) {
                if (Main.esCruceMOC) cruceMOC(posPadre);
                else cruceOX2(posPadre);
            }
            posPadre += TAM_TORNEO;
        }
    }

    private void cruceMOC() {
        int puntoDeCorte = Main.random.nextInt(poblacion.tamIndividuo);
        boolean copiaPadre[ poblacionDescendiente.tamIndividuo];
        boolean copiaMadre[ poblacionDescendiente.tamIndividuo];
        for (int i = 0; i < poblacionDescendiente.tamIndividuo; i++) {

            poblacionDescendiente.individuos[posPadre]
        }
    }

    private boolean[] eliminacionCruzada(int puntoDeCorte) {
        boolean esValorAntesDeCorte[NUM_INDIVIDUOS_CRUZADOS][poblacionDescendiente.tamIndividuo];
        for (int i = 0; i < puntoDeCorte; i++) {
            for (int j = 0; j < poblacionDescendiente.tamIndividuo; j++) {
                if (esValorAntesDeCorte[2][i] != true)
                    if (poblacionDescendiente.individuos[posPadre + 1] =)
            }
        }
    }

    private void cruceOX2(int posPadre) {

    }

    public void TorneoBinario() {
        int kIndividuos = 2;
        int individuosSeleccionados = 0;
        while (seleccionados != kIndividuos) {
            int number = Main.random.nextInt(individuos.length);
            poblacion.getIndividuo(i);
        }
    }
}
