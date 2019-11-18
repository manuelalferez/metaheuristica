package meta;

public class Genetico {
    Poblacion poblacion;
    Poblacion poblacionDescendiente;
    private int TAM_TORNEO = 2;
    private int NUM_INDIVIDUOS_CRUZADOS = 2;
    private int posPadre;
    private int posMadre = posPadre + 1;

    public void algoritmoGenetico() {
        int numElites = 3;
        int iteraciones = 0;
        inicializarPoblacion();

        while (iteraciones < TAM) {
            evaluarPoblacion();
            //Calcular elite
            seleccionar();
            recombinar();
            mutar();
            //Calcular el o los elites
            poblacion.calcularElite(numElites);

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
        Solucion individuo ;
        for (int i= 0; i< poblacion.getTamPoblacion(); i++) {
            individuo = torneoBinario();
            poblacionDescendiente.incluirIndividuo(individuo);
        }
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

    public Solucion torneoBinario() {
        int posPrimerIndividuo = Main.random.nextInt(poblacion.individuos.length);
        int posSegundoIndividuo;

        do {
            posSegundoIndividuo= Main.random.nextInt(poblacion.individuos.length);
        }while(posPrimerIndividuo == posSegundoIndividuo);

        if (poblacion.individuos[posPrimerIndividuo].coste < poblacion.individuos[posSegundoIndividuo].coste)
            return poblacion.individuos[posPrimerIndividuo];
        else
            return poblacion.individuos[posSegundoIndividuo];
    }



    public void reemplazar(){
        int posicionElites [] = poblacion.getPosicionElites();
        int idElites[] = new int [posicionElites.length];
        boolean estaElites[]= new boolean[posicionElites.length];

        for (int i = 0; i< estaElites.length; i++){
            estaElites[i] = false;
        }
        //Sacamos los id
        for (int i = 0; i< posicionElites.length; i++){
            idElites[i]= poblacion.individuos[posicionElites[i]].id;
        }

        for (int i = 0; i < poblacion.individuos.length; i++){
            for (int j = 0; j< posicionElites.length; j++){
                if (poblacion.individuos[i].id == idElites[j])
                    estaElites[j] = true;
            }
        }


        poblacion = poblacionDescendiente;
    }
}
