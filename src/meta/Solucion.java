package meta;

class Solucion {
    private static int contador = 0;
    int[] solucion;
    int coste;
    boolean estaModificado;
    int id;

    Solucion() {
        solucion = new int[0];
        coste = 0;
        estaModificado = false;
        id = contador++;
    }

    Solucion(int tam) {
        solucion = new int[tam];
        coste = 0;
        estaModificado = false;
        id = contador++;
    }

    Solucion(Solucion copia) {
        solucion = new int[copia.solucion.length];
        System.arraycopy(copia.solucion, 0, solucion, 0, solucion.length);
        coste = copia.coste;
        estaModificado = copia.estaModificado;
        id = copia.id;
    }

    void modificarIdentificador(){
        id=contador++;
    }

    void copiar(Solucion copia) {
        solucion = new int[copia.solucion.length];
        System.arraycopy(copia.solucion, 0, solucion, 0, solucion.length);
        coste = copia.coste;
        estaModificado = copia.estaModificado;
        id = copia.id;
    }
}
