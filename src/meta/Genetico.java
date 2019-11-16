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
            posPadre += 2;
        }
    }

    private void cruceMOC(int posPadre) {
        double puntoDeCorte = Main.random.nextInt(poblacion.tamIndividuo);

    }

    private void cruceOX2(int posPadre) {

    }

    public Solucion torneoBinario() {
        int posPrimerIndividuo = Main.random.nextInt(individuos.length);
        int posSegundoIndividuo;

        do {
            posSegundoIndividuo= Main.random.nextInt(individuos.length);
        }while(posPrimerIndividuo == posSegundoIndividuo);

        if (poblacion.individuos[posPrimerIndividuo].coste < poblacion.individuos[posSegundoIndividuo].coste)
            return poblacion.individuos[posPrimerIndividuo];
        else
            return poblacion.individuos[posSegundoIndividuo];
    }

    public void calcularElite( int numElites){

        int posElite[] = new int[numElites];
        int costeElite[] = new int[numElites];

        for (int i = 0; i< numElites; i++) {
            posElite[i]= i;
            costeElite[i]= poblacion.individuos[i].coste;
        }

        for (int i = numElites; i< poblacion.getTamPoblacion(); i++){
            int posMayorCoste = calcularPosicionMaximoCoste(posElite,costeElite);
            if (poblacion.individuos[i].coste < costeElite[posMayorCoste]){
                posElite[posMayorCoste] = i;
                costeElite[posMayorCoste] = poblacion.individuos[i].coste;
            }
        }
        poblacion.inicializarElites(posElite);
    }

    private int calcularPosicionMaximoCoste(int []posElite, int [] costeElite){
        int posicionMaximo = 0;
        int costeMaximo= costeElite[0];

        for (int i = 1; i < posElite.length; i++){
            if (costeMaximo<costeElite[i]){
                posicionMaximo = i;
                costeMaximo = costeElite[i];
            }
        }
        return posicionMaximo;
    }

}
