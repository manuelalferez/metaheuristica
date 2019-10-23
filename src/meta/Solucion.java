package meta;

class Solucion {
     int [] solucion;
     int coste;

    Solucion(){
        solucion = new int[0];
        coste=0;
    }

    Solucion(int tam){
        solucion = new int[tam];
        coste=0;
    }

    Solucion(Solucion copia){
        solucion = new int[copia.solucion.length];
        for(int i=0;i<solucion.length;i++) solucion[i]= copia.solucion[i];
        coste=copia.coste;
    }

    void copiar(Solucion copia){
        solucion = new int[copia.solucion.length];
        for(int i=0;i<solucion.length;i++) solucion[i]= copia.solucion[i];
        coste=copia.coste;
    }
}
