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

public void copiarPoblacion ()




public int[] getPosicionElites(){
        return posicionElites;
}
    public void calcularElite( int numElites){

        int posElite[] = new int[numElites];
        int costeElite[] = new int[numElites];

        for (int i = 0; i< numElites; i++) {
            posElite[i]= i;
            costeElite[i]=individuos[i].coste;
        }

        for (int i = numElites; i< getTamPoblacion(); i++){
            int posMayorCoste = calcularPosicionMaximoCoste(posElite,costeElite);
            if (individuos[i].coste < costeElite[posMayorCoste]){
                posElite[posMayorCoste] = i;
                costeElite[posMayorCoste] = individuos[i].coste;
            }
        }
        inicializarElites(posElite);
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
}

