package meta;

public class Poblacion {
    Solucion[] individuos;
    private int tamFisico;
    private int tamLogico;
    int tamIndividuo;
    int[] posicionElites;

    Poblacion(int numIndividuos, int tamIndividuo) {
        individuos = new Solucion[this.tamFisico = numIndividuos];
        this.tamLogico = 0;
    }

    Poblacion(int numIndividuos) {
        individuos = new Solucion[this.tamFisico = numIndividuos];
        this.tamLogico = 0;
    }

    public void inicializar() {
        for (int i = 0; i < tamFisico; i++)
            individuos[i] = Utils.generarSolucionInicial(this.tamIndividuo);
        tamLogico = tamFisico;
    }

    void evaluarPoblacion() {
        for (int i = 0; i < individuos.length; i++) {
            individuos[i].coste = Utils.calcularCoste(individuos[i].solucion);
            individuos[i].estaModificado = false;
        }
    }

    public int getTam() {
        return tamFisico;
    }

    public void incluirIndividuo(Solucion individuo) {
        individuos[tamLogico++] = new Solucion(individuo);
    }

    public void inicializarElites(int[] posElites) {
        posicionElites = posElites.clone();
    }
}

