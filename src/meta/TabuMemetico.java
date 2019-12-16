package meta;

import java.util.ArrayList;
import java.util.List;

public class TabuMemetico {
    // Variables compartidas entre funciones de la clase
    private static final int MAX_ITERACIONES = Main.getIteracionesMaximas();
    private static final int MAX_INTENTOS = Main.getIntentosMaximos();
    private static final int NUM_VECINOS = Main.getVecinosMaximos();
    private static final int NUM_ENTORNOS = Main.getEntornosMaximos();

    private static Solucion solucionActual = new Solucion();
    private static Vecino mejorVecino = new Vecino();
    private static int costeMejorVecino = Integer.MAX_VALUE;
    private static int entornoMejorVecino;
    private static int entorno;

    // Atributos
    private int tamSolucion;

    private List<Vecino> listaTabues;
    private Solucion mejorSolucion;

    TabuMemetico() {
        tamSolucion = Main.aeropuertoActual.numPuertas;
    }

    void algoritmoTabu(Solucion solucion) {
        int iteraciones = 0, intentos = 0;
        entorno = 0;
        listaTabues = new ArrayList<>();
        solucionActual = new Solucion(tamSolucion);
        solucionActual.copiar(solucion);
        solucionActual.coste = Utils.calcularCoste(solucionActual.solucion);
        Utils.escribirSolucionInicial(solucionActual, iteraciones);
        mejorSolucion = new Solucion(solucionActual);
        do {
            generarEntornos();
            realizarMovimiento();
            anadirVecinoTabu(mejorVecino);

            Utils.escribirMovimientoEnFichero(entornoMejorVecino, mejorVecino, solucionActual.coste, iteraciones); // logs
            iteraciones++;

            if (solucionActual.coste < mejorSolucion.coste) {
                mejorSolucion.copiar(solucionActual);
                intentos = 0;
            } else intentos++;
        } while (iteraciones < MAX_ITERACIONES && intentos != MAX_INTENTOS);
    }

    private void generarEntornos() {
        int entornosLocales = 0;
        Vecino mejorEntornos = new Vecino();
        int costeMejorEntornos = Integer.MAX_VALUE;
        do {
            generarMejorVecino();
            if (costeMejorEntornos > costeMejorVecino) {
                mejorEntornos.copiarVecino(mejorVecino);
                costeMejorEntornos = costeMejorVecino;
                entornoMejorVecino = entorno;
            }
            entornosLocales++;
            entorno++;
        } while (entornosLocales < NUM_ENTORNOS && costeMejorVecino > solucionActual.coste);

        if (!mejorEntornos.sonIguales(mejorVecino))
            mejorVecino.copiarVecino(mejorEntornos);
    }

    private void generarMejorVecino() {
        Vecino vecinoActual;
        costeMejorVecino = Integer.MAX_VALUE;
        int costeVecinoActual;

        for (int i = 0; i < NUM_VECINOS; i++) {
            vecinoActual = generarVecino();
            costeVecinoActual = Utils.calcularCosteParametrizado(solucionActual, vecinoActual);

            if (costeVecinoActual < costeMejorVecino) {
                mejorVecino.copiarVecino(vecinoActual);
                costeMejorVecino = costeVecinoActual;
            }
        }
    }

    private Vecino generarVecino() {
        Vecino nuevoVecino = new Vecino();
        do {
            nuevoVecino.setPrimeraPosicion(Main.random.nextInt(tamSolucion));
            nuevoVecino.setSegundaPosicion(Main.random.nextInt(tamSolucion));
            if (nuevoVecino.getPrimeraPosicion() > nuevoVecino.getSegundaPosicion()) {
                nuevoVecino.intercambiarPosiciones();
            }
        } while (esVecinoTabu(nuevoVecino) || nuevoVecino.tienePosicionesIguales());

        return nuevoVecino;
    }

    private boolean esVecinoTabu(Vecino vecino) {
        if (listaTabues.size() == 0) return false;
        for (Vecino v : listaTabues) {
            if (v.sonIguales(vecino)) return true;
        }
        return false;
    }

    /**
     * Agrega un vecino a la lista. Si la lista está llena, se elimina por el principio , y siempre se añade
     * por el final
     */
    private void anadirVecinoTabu(Vecino vecino) {
        int tenenciaTabu = 5;
        if (listaTabues.size() == tenenciaTabu) {
            listaTabues.remove(0);
            listaTabues.add(vecino);
        } else {
            listaTabues.add(vecino);
        }
    }

    private void realizarMovimiento() {
        Utils.realizarMovimiento(solucionActual.solucion, mejorVecino);
        solucionActual.coste = costeMejorVecino;
    }

    /**
     * El entorno se va generando desde el inicio. La cotaSuperior mejora la eficiencia
     */
    private boolean valorNoEsta(int[] entorno, int cotaSuperior, int valor) {
        for (int i = 0; i <= cotaSuperior; i++)
            if (entorno[i] == valor)
                return false;
        return true;
    }

    Solucion getSolucion() {
        return this.mejorSolucion;
    }
}
