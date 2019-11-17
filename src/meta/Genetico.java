package meta;

public class Genetico {
    Poblacion poblacion;
    Poblacion poblacionDescendiente;
    private int TAM_TORNEO = 2;
    private static int TAM_POBLACION = 50;

    public void algoritmoGenetico() {
        int iteraciones = 0;
        inicializarPoblacion();
        crearPoblacionDescendientes();
        while (iteraciones < TAM) {
            evaluarPoblacion();
            seleccionar();
            recombinar();
            mutar();
            //Calcular el o los elites
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

    private int generarPosDistintaPrimerIndividuo(int posPrimerIndividuo){
        int posSegundoIndividuo;
        do {
            posSegundoIndividuo= Main.random.nextInt(poblacion.individuos.length);
        }while(posPrimerIndividuo == posSegundoIndividuo);
        return posSegundoIndividuo;
    }

    private int mejorIndividuo(int primer, int segundo) {
        if (poblacion.individuos[primer].coste < poblacion.individuos[segundo].coste)
            return primer;
        else return segundo;
    }

    private void recombinar() {
        int i=0;
        while (i < poblacion.getTam()) {
            double probabilidad = Main.random.nextDouble();
            if (probabilidad < 0.7) {
                Reproduccion nuevaReproduccion =
                        new Reproduccion(poblacionDescendiente.individuos[i], poblacionDescendiente.individuos[i+1]);
                if (Main.esCruceMOC) nuevaReproduccion.cruceMOC();
                else nuevaReproduccion.cruceOX2();
            }
            i += TAM_TORNEO;
        }
    }

    private void calcularElite(int numElites) {

        int posElite[] = new int[numElites];
        int costeElite[] = new int[numElites];

        for (int i = 0; i < numElites; i++) {
            posElite[i] = i;
            costeElite[i] = poblacion.individuos[i].coste;
        }

        for (int i = numElites; i < poblacion.getTam(); i++) {
            int posMayorCoste = calcularPosicionMaximoCoste(posElite, costeElite);
            if (poblacion.individuos[i].coste < costeElite[posMayorCoste]) {
                posElite[posMayorCoste] = i;
                costeElite[posMayorCoste] = poblacion.individuos[i].coste;
            }
        }
        poblacion.inicializarElites(posElite);
    }

    private int calcularPosicionMaximoCoste(int[] posElite, int[] costeElite) {
        int posicionMaximo = 0;
        int costeMaximo = costeElite[0];

        for (int i = 1; i < posElite.length; i++) {
            if (costeMaximo < costeElite[i]) {
                posicionMaximo = i;
                costeMaximo = costeElite[i];
            }
        }
        return posicionMaximo;
    }

}
