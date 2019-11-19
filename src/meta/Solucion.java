package meta;

class Solucion {
    private static int contador = 0;
    int[] solucion;
    int coste;
    boolean estaModificado;

    Solucion() {
        solucion = new int[0];
        coste = 0;
        estaModificado = false;
    }

    Solucion(int tam) {
        solucion = new int[tam];
        coste = 0;
        estaModificado = false;
    }

    Solucion(Solucion copia) {
        solucion = new int[copia.solucion.length];
        System.arraycopy(copia.solucion, 0, solucion, 0, solucion.length);
        coste = copia.coste;
        estaModificado = copia.estaModificado;
    }

    void marcarComoModificado() {
        estaModificado = true;
    }


    void copiar(Solucion copia) {
        solucion = new int[copia.solucion.length];
        System.arraycopy(copia.solucion, 0, solucion, 0, solucion.length);
        coste = copia.coste;
        estaModificado = copia.estaModificado;
    }

    boolean sonIguales(Solucion otraSolucion) {
        if (this.coste == otraSolucion.coste) {
            for (int i = 0; i < solucion.length; i++)
                if (solucion[i] != otraSolucion.solucion[i])
                    return false;
            return true;
        } else
            return false;
    }
}
