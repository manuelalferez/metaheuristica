package meta;

public class Genetico {

    Poblacion poblacion;
    Poblacion poblacionDescendiente;

    public void algoritmoGenetico(){
        int iteraciones = 0;
        inicializarPoblacion();

    while (iteraciones < TAM){
        evaluarPoblacion();
        //Calcular elite
        seleccionar();
        recombinar();
        mutar();
        //Calcular el o los elites
        reemplazar();
    }
    }

    public void inicializarPoblacion(){
        poblacion = new Poblacion (numIndividuos, numPuertas);
    }

    public void evaluarPoblacion(){
        poblacion.evaluarPoblacion();
    }

    public void seleccionar(){


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
