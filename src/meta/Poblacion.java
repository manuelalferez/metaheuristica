package meta;

public class Poblacion {
    private Solucion[] individuos;

    Poblacion(int numIndividuos, int tam) {
        individuos = new Solucion[numIndividuos];
        for (int i = 0; i < numIndividuos; i++)
            individuos[i] = Utils.generarSolucionInicial(tam);
    }
}
