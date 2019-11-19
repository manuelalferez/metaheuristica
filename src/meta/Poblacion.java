package meta;

class Poblacion {
    Solucion[] individuos;
    private int tamFisico;
    private int tamLogico;
    int tamIndividuo;
    private int[] posElites;
    private int[] costeElites;
    int[] posPeores;
    private int[] costePeores;


    Poblacion(int numIndividuos, int tamIndividuo) {
        individuos = new Solucion[this.tamFisico = numIndividuos];
        this.tamLogico = 0;
    }

    Poblacion(int numIndividuos) {
        individuos = new Solucion[this.tamFisico = numIndividuos];
        this.tamLogico = 0;
    }

    void inicializar() {
        for (int i = 0; i < tamFisico; i++)
            individuos[i] = Utils.generarSolucionInicial(this.tamIndividuo);
        tamLogico = tamFisico;
    }

    void evaluar() {
        for (Solucion individuo : individuos) {
            if(individuo.estaModificado) {
                individuo.coste = Utils.calcularCoste(individuo.solucion);
                individuo.estaModificado = false;
            }
        }
    }

    int getTam() {
        return tamFisico;
    }

    void incluirIndividuo(Solucion individuo) {
        individuos[tamLogico++] = new Solucion(individuo);
    }

    void copiarPoblacion(Poblacion copia) {
        individuos = copia.individuos.clone();
    }

    int[] getElites() {
        return posElites;
    }



    void calcularElites() {
        inicializarElites();
        for (int i = Main.getNumElites(); i < getTam(); i++) {
            int posMayorCoste = calcularPosicionMaximoCoste();
            if (individuos[i].coste < costeElites[posMayorCoste]) {
                posElites[posMayorCoste] = i;
                costeElites[posMayorCoste] = individuos[i].coste;
            }
        }
    }

    private void inicializarElites() {
        posElites = new int[Main.getNumElites()];
        costeElites = new int[Main.getNumElites()];

        for (int i = 0; i < Main.getNumElites(); i++) {
            posElites[i] = i;
            costeElites[i] = individuos[i].coste;
        }
    }

    private int calcularPosicionMaximoCoste() {
        int posicionMaximo = 0;
        int costeMaximo = costeElites[0];

        for (int i = 1; i < posElites.length; i++)
            if (costeMaximo < costeElites[i]) {
                posicionMaximo = i;
                costeMaximo = costeElites[i];
            }
        return posicionMaximo;
    }



    void calcularPeoresIndividuos(int numIndividuosABuscar) {
        inicializarPeores(numIndividuosABuscar);
        for (int i = numIndividuosABuscar; i < getTam(); i++) {
            int posMenorCoste = calcularPosicionMinimoCoste();
            if (individuos[i].coste > costePeores[posMenorCoste]) {
                posPeores[posMenorCoste] = i;
                costePeores[posMenorCoste] = individuos[i].coste;
            }
        }
    }

    private void inicializarPeores(int numPeores) {
        posPeores = new int[numPeores];
        costePeores = new int[numPeores];

        for (int i = 0; i < numPeores; i++) {
            posPeores[i] = i;
            costePeores[i] = individuos[i].coste;
        }
    }

    private int calcularPosicionMinimoCoste() {
        int posicionMinimo = 0;
        int costeMinimo = costePeores[0];

        for (int i = 1; i < posPeores.length; i++) {
            if (costeMinimo > costePeores[i]) {
                posicionMinimo = i;
                costeMinimo = costePeores[i];
            }
        }
        return posicionMinimo;
    }
}

