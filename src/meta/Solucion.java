package meta;

class Solucion {
    private static int contador= 0;
     int [] solucion;
     int coste;
     boolean estaModificado;
     int id;//TODO
    Solucion(){
        solucion = new int[0];
        coste=0;
        estaModificado = false;
        id = contador++;
    }

    Solucion(int tam){
        solucion = new int[tam];
        coste=0;
        estaModificado = false;
        id = contador++;
    }

    Solucion(Solucion copia){
        solucion = new int[copia.solucion.length];
        for(int i=0;i<solucion.length;i++) {
            solucion[i]= copia.solucion[i];
        }
        coste=copia.coste;
        estaModificado=copia.estaModificado;
        id = contador++;
    }

    void copiar(Solucion copia){
        solucion = new int[copia.solucion.length];
        for(int i=0;i<solucion.length;i++) solucion[i]= copia.solucion[i];
        coste=copia.coste;
        estaModificado=copia.estaModificado;
    }
}
