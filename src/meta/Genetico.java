package meta;

public class Genetico {
    Poblacion poblacion;
    Poblacion poblacionDescendiente;
    private int TAM_TORNEO = 2;
    private static int TAM_POBLACION = 50;
    private static int NUM_EVALUACIONES = 50000;

    Reproduccion nuevaReproduccion;

    public void algoritmoGenetico() {
        int numElites = 3;
        int iteraciones = 0;
        inicializarPoblacion();
        crearPoblacionDescendientes();
        while (iteraciones < NUM_EVALUACIONES) {
            evaluarPoblacion();
            seleccionar();
            recombinar();
            mutar();
            //Calcular el o los elites
            poblacion.calcularElite(numElites);

            reemplazar();
        }
    }

    private void inicializarPoblacion() {
        poblacion = new Poblacion(TAM_POBLACION, Main.aeropuertoActual.numPuertas);
        poblacion.inicializar();
    }

    private void crearPoblacionDescendientes() {
        poblacionDescendiente = new Poblacion(TAM_POBLACION, Main.aeropuertoActual.numPuertas);
    }

    private void evaluarPoblacion() {
        poblacion.evaluarPoblacion();
    }

    private void seleccionar() {
        int posIndividuoGanador;
        for (int i = 0; i < poblacion.getTam(); i++) {
            posIndividuoGanador = torneoBinario();
            poblacionDescendiente.incluirIndividuo(poblacion.individuos[posIndividuoGanador]);
        }
    }

    private int torneoBinario() {
        int posPrimerIndividuo = Main.random.nextInt(poblacion.individuos.length);
        int posSegundoIndividuo = generarPosDistintaPrimerIndividuo(posPrimerIndividuo);
        return mejorIndividuo(posPrimerIndividuo, posSegundoIndividuo);
    }

    private int generarPosDistintaPrimerIndividuo(int posPrimerIndividuo) {
        int posSegundoIndividuo;
        do {
            posSegundoIndividuo = Main.random.nextInt(poblacion.individuos.length);
        } while (posPrimerIndividuo == posSegundoIndividuo);
        return posSegundoIndividuo;
    }

    private int mejorIndividuo(int primer, int segundo) {
        if (poblacion.individuos[primer].coste < poblacion.individuos[segundo].coste)
            return primer;
        else return segundo;
    }

    private void recombinar() {
        int i = 0;
        while (i < poblacion.getTam()) {
            double probabilidad = Main.random.nextDouble();
            if (probabilidad < 0.7) {
                realizarCruce(i);
                copiarIndividuosCruzados(i);
            }
            i += TAM_TORNEO;
        }
    }

    private void realizarCruce(int posPrimerProgenitor) {
        nuevaReproduccion = new Reproduccion(poblacionDescendiente.individuos[posPrimerProgenitor],
                poblacionDescendiente.individuos[posPrimerProgenitor + 1]);
        if (Main.esCruceMOC) nuevaReproduccion.cruceMOC();
        else nuevaReproduccion.cruceOX2();
    }

    private void copiarIndividuosCruzados(int posPrimerProgenitor) {
        poblacionDescendiente.individuos[posPrimerProgenitor].copiar(nuevaReproduccion.getPrimerProgenitor());
        poblacionDescendiente.individuos[posPrimerProgenitor + 1].copiar(nuevaReproduccion.getSegundoProgenitor());
    }

    private void mutar() {
        for (Solucion individuo : poblacionDescendiente.individuos) {
                for(int i=0;i<individuo.solucion.length;i++){
                    double probabilidad = Main.random.nextDouble();
                    if (probabilidad < 0.05) {
                        realizarCruce(i);//TODO
                        copiarIndividuosCruzados(i);
                    }
                }
        }
    }

    private void calcularElite(int numElites) {
        int posElite[] = new int[numElites];
        int costeElite[] = new int[numElites];

        for (int i = 0; i < numElites; i++) {
            posElite[i] = i;
            costeElite[i] = poblacion.individuos[i].coste;
        }
    public void reemplazar(){
        int posicionElites [] = poblacion.getPosicionElites();
        int idElites[] = new int [posicionElites.length];
        boolean estaElites[]= new boolean[posicionElites.length];

        for (int i = numElites; i < poblacion.getTam(); i++) {
            int posMayorCoste = calcularPosicionMaximoCoste(posElite, costeElite);
            if (poblacion.individuos[i].coste < costeElite[posMayorCoste]) {
                posElite[posMayorCoste] = i;
                costeElite[posMayorCoste] = poblacion.individuos[i].coste;
            }
        for (int i = 0; i< estaElites.length; i++){
            estaElites[i] = false;
        }
        //Sacamos los id
        for (int i = 0; i< posicionElites.length; i++){
            idElites[i]= poblacion.individuos[posicionElites[i]].id;
        }
        poblacion.inicializarElites(posElite);
    }

    private int calcularPosicionMaximoCoste(int[] posElite, int[] costeElite) {
        int posicionMaximo = 0;
        int costeMaximo = costeElite[0];

        for (int i = 0; i < poblacion.individuos.length; i++){
            for (int j = 0; j< posicionElites.length; j++){
                if (poblacion.individuos[i].id == idElites[j])
                    estaElites[j] = true;
        for (int i = 1; i < posElite.length; i++) {
            if (costeMaximo < costeElite[i]) {
                posicionMaximo = i;
                costeMaximo = costeElite[i];
            }
        }


        poblacion = poblacionDescendiente;
    }
}
