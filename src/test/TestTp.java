package test;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import algoritmos.Secuencial;
import algoritmos.Concurrente; // Importa tu clase Concurrente

import auxiliares.Cronometro; 
import auxiliares.Funciones;   
import auxiliares.Casos;      
import auxiliares.VolumenesDeDatos;

public class TestTp {

    public static void main(String[] args) {
        Cronometro cron = new Cronometro(TimeUnit.MILLISECONDS,4);
        
        int[] arrS = null; // Array para la versión Secuencial
        int[] arrC = null; // Array para la versión Concurrente

        // ==========================================================================
        // Elegir el caso y el volumen de datos para probar
        Casos casoSeleccionado = Casos.ELEMENTOS_IDENTICOS; //
        VolumenesDeDatos volumenSeleccionado = VolumenesDeDatos.DIEZ; //
        // ==========================================================================

        int n = volumenSeleccionado.getN(); // Obtener valor del num
        
        //=================== Generacion de array segun el caso ===================
        switch (casoSeleccionado) {
            case ALEATORIO:
                arrS = Funciones.generarAleatorio(n, -1000, 1000);
                break;
            case ORDENADO:
                arrS = Funciones.generarOrdenado(n);
                break;
            case ORDENADO_INVERSO:
                arrS = Funciones.generarOrdenadoInverso(n);
                break;
            case ELEMENTOS_IDENTICOS:
                arrS = Funciones.generarElementosIdenticos(n, 7);
                break;
        }

        //arrC = Arrays.copyOf(arrS, arrS.length); 

        System.out.println("=================== CASO ===================");
        System.out.println(casoSeleccionado.getNombre() +
                           "\nn = " + (new DecimalFormat("#,###").format(volumenSeleccionado.getN())) +
                           " (" + volumenSeleccionado.getDescripcion() + ")");

        System.out.println("\n------------ Secuencial ------------");
        cron.iniciar();
        Secuencial.sort(arrS);
        cron.detener();
        System.out.println("Demoró: " + cron.obtenerTiempo());
        
        cron.reiniciar();
        
        
        System.out.println("\n------------ Concurrente ------------");
        
        System.out.println("\n=================== PRUEBA FINALIZADA ==================="); //
    }
}