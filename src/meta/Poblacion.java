package meta;

public class Poblacion {
    private Solucion[] individuos;
    private int tamFisico;
    private int tamLogico;
    Poblacion(int tamFisico, int tamSolucion) {
        individuos = new Solucion[this.tamFisico = tamFisico];
        for (int i = 0; i < tamFisico; i++)
            individuos[i] = Utils.generarSolucionInicial(tamSolucion);
    }


    Poblacion (int tamFisico){
        individuos = new Solucion[tamFisico];
        this.tamFisico = 0;//TODO , aniadirIndividuo añade sin darle la posicion
    }

    public void evaluarPoblacion() {
        for (int i = 0; i < individuos.length; i++) {
            individuos[i].coste = Utils.calcularCoste(individuos[i].solucion);
            individuos[i].estaModificado = false;
        }
    }

    public Solucion getIndividuo(int posicion){
        return individuos[posicion];
    }

    public int getTamPoblacion(){
        return tamPoblacion;
    }
    public void aniadirIndividuo(Solucion individuo){
            individuos[tamFisico++]= new Solucion (individuo);
    }

}

