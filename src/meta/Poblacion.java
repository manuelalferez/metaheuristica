package meta;

public class Poblacion {
    Solucion[] individuos;
    private int tamFisico;
    private int tamLogico;
    int tamIndividuo;

    Poblacion(int tamFisico, int tamSolucion) {
        individuos = new Solucion[this.tamFisico = tamFisico];
        for (int i = 0; i < tamFisico; i++)
            individuos[i] = Utils.generarSolucionInicial(tamIndividuo= tamSolucion);
    }

    Poblacion (int tamFisico){
        individuos = new Solucion[this.tamFisico= tamFisico];
        this.tamLogico = 0;
    }

    public void evaluarPoblacion() {
        for (int i = 0; i < individuos.length; i++) {
            individuos[i].coste = Utils.calcularCoste(individuos[i].solucion);
            individuos[i].estaModificado = false;
        }
    }

    public int getTamPoblacion(){
        return tamFisico;
    }
    public void aniadirIndividuo(Solucion individuo){
            individuos[tamLogico++]= new Solucion (individuo);
    }

}

