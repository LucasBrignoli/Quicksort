package test;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import algoritmos.Secuencial;
import algoritmos.Concurrente; // Importa tu clase Concurrente

import auxiliares.Cronometro; 
import auxiliares.Funciones;   

public class TestTp {

    // Definimos los tamaños de los arreglos
    private static final int[] TAMANIOS_ELEMENTOS = {10, 1000, 100000, 1000000};
    
    // Definimos los rangos/valores para la generación de arreglos
    private static final int MIN_VALOR_ALEATORIO = -1000;
    private static final int MAX_VALOR_ALEATORIO = 1000;
    private static final int VALOR_IDENTICO = 7;

    public static void main(String[] args) {
        Cronometro cron = new Cronometro(TimeUnit.MILLISECONDS, 4);
        Scanner scanner = new Scanner(System.in); 

        System.out.println("=================================================");
        System.out.println("======== INICIANDO PRUEBAS DE ORDENAMIENTO =======");
        System.out.println("=================================================");

        System.out.println("\nSeleccione el tipo de caso a probar:");
        System.out.println("1. Aleatorio");
        System.out.println("2. Ordenado");
        System.out.println("3. Ordenado Inverso");
        System.out.println("4. Elementos Identicos"); 
        System.out.print("Ingrese el número de su opción: ");

        int opcion = -1;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            scanner.close();
            return;
        }

        String tipoCasoSeleccionado = "";
        
        // Asignar el nombre del caso seleccionado para usarlo en la impresión
        switch(opcion) {
            case 1: tipoCasoSeleccionado = "Aleatorio"; break;
            case 2: tipoCasoSeleccionado = "Ordenado"; break;
            case 3: tipoCasoSeleccionado = "Ordenado Inverso"; break;
            case 4: tipoCasoSeleccionado = "Elementos Identicos"; break;
            default:
                System.out.println("Opción no válida. Reiniciando...");
                scanner.close();
                return;
        }

        // Obtener la cantidad de hilos una vez para la impresión concurrente
        int numProcesadoresDisponibles = Runtime.getRuntime().availableProcessors();

        // El bucle principal itera sobre cada tamaño de elemento
        for (int n : TAMANIOS_ELEMENTOS) {
            System.out.println("\n---------- CASO ----------");
            System.out.println(tipoCasoSeleccionado); // Imprime el tipo de caso seleccionado
            
            String descripcionN = "";
            if (n == 10) descripcionN = " (diez)";
            System.out.println("n = " + new DecimalFormat("#,###").format(n) + descripcionN);
            
            int[] arrOriginal; // Declarar el arreglo original aquí
            
            // Generar el arreglo original según el tipo de caso seleccionado
            switch (tipoCasoSeleccionado) {
                case "Aleatorio":
                    arrOriginal = Funciones.generarAleatorio(n, MIN_VALOR_ALEATORIO, MAX_VALOR_ALEATORIO);
                    break;
                case "Ordenado":
                    arrOriginal = Funciones.generarOrdenado(n);
                    break;
                case "Ordenado Inverso":
                    arrOriginal = Funciones.generarOrdenadoInverso(n);
                    break;
                case "Elementos Identicos": 
                    arrOriginal = Funciones.generarElementosIdenticos(n, VALOR_IDENTICO);
                    break;
                default: // Esto no debería pasar si la validación inicial es correcta
                    System.out.println("Error interno al generar el arreglo.");
                    continue; // Saltar a la siguiente iteración del bucle for
            }

            // --- PRUEBA SECUENCIAL ---
            int[] arrS = Arrays.copyOf(arrOriginal, arrOriginal.length); // Copia para secuencial

            System.out.println("\n---------- Secuencial ----------");
            cron.reiniciar(); 
            cron.iniciar();
            Secuencial.sort(arrS);
            cron.detener();
            System.out.println("Demoró: " + cron.obtenerTiempo());

            // --- PRUEBA CONCURRENTE ---
            int[] arrC = Arrays.copyOf(arrOriginal, arrOriginal.length); // Copia para concurrente

            System.out.println("\n---------- Concurrente ----------");
            System.out.println("Cantidad de hilos del pool: " + numProcesadoresDisponibles);
            cron.reiniciar(); 
            cron.iniciar();
            Concurrente.sortConcurrentForkJoin(arrC); 
            cron.detener();
            System.out.println("Demoró: " + cron.obtenerTiempo());
        } // Fin del bucle for de tamaños

        scanner.close(); 

        System.out.println("\n=================================================");
        System.out.println("======== PRUEBAS DE ORDENAMIENTO FINALIZADAS ======");
        System.out.println("=================================================");
    } // Fin del método main
}