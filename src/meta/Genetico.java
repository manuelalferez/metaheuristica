package meta;

public class Genetico {
    Poblacion poblacion;
    Poblacion poblacionDescendiente;

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
            posPadre += 2;
        }
    }

    private void cruceMOC(int posPadre) {
        double puntoDeCorte = Main.random.nextInt(poblacion.tamIndividuo);

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
