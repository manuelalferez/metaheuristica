package meta;

public class Poblacion {
    private Solucion[] individuos;

    Poblacion(int tamPoblacion, int tamSolucion) {
        individuos = new Solucion[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++)
            individuos[i] = Utils.generarSolucionInicial(tamSolucion);
    }

    public void evaluarPoblacion() {
        for (int i = 0; i < individuos.length; i++) {
            individuos[i].coste = Utils.calcularCoste(individuos[i].solucion);
            individuos[i].estaModificado = false;
        }
    }

    public void seleccionTorneo(int kIndividuos){

        int seleccionados = 0;
        while (seleccionados != kIndividuos){
            Main.random.nextInt();
            int number = Main.random.nextInt(individuos.length);

        }
    }


}

