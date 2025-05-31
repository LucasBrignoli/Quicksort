package algoritmos;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Concurrente {
    // Estas líneas comentadas estaban en tu código original, pero no se usan
    // con la implementación de sortConcurrentForkJoin que estás llamando desde TestTp.java
    // private static final ForkJoinPool pool = new ForkJoinPool(); 
    // private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // NOTA IMPORTANTE: Si usas ForkJoinPool.commonPool(), NO hagas pool.shutdown().
    // Si creas un nuevo ForkJoinPool en sortConcurrentForkJoin, sí debes cerrarlo.
    // En tu última versión de Concurrente, estabas creando un nuevo pool en cada llamada a sortConcurrentForkJoin,
    // lo cual está bien si se cierra, y lo estabas haciendo con pool.shutdown().
    // Mantendremos esa estructura.

    // El método 'sort' y las relacionadas con ExecutorService no se usan en tu TestTp actual.
    // Las dejo comentadas para no generar errores si tu código las busca, pero el foco es sortConcurrentForkJoin.
    /*
    public static void sort(int[] arr) {
        QuickSortTask mainTask = new QuickSortTask(arr, 0, arr.length - 1);
        System.out.println("Cantidad de hilos del pool: " + pool.getParallelism());
        pool.invoke(mainTask);
    }

    public static void sortConcurrentExecutor(int[] v) {
        try {
            quickSortConcurrentExecutor(v, 0, v.length - 1);
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("El ordenamiento concurrente fue interrumpido: " + e.getMessage());
        }
    }

    private static void quickSortConcurrentExecutor(int v[], int low, int high) {
        if (low < high) {
            int pi = partition(v, low, high);
            Runnable leftTask = () -> quickSortConcurrentExecutor(v, low, pi - 1);
            Runnable rightTask = () -> quickSortConcurrentExecutor(v, pi + 1, high);
            executor.execute(leftTask);
            executor.execute(rightTask);
        }
    }
    */

    // Opción 2: Usando ForkJoinPool (ideal para algoritmos divide y vencerás)
    // Esta es la opción que TestTp.java está llamando.
    public static void sortConcurrentForkJoin(int[] v) {
        // Se crea un nuevo ForkJoinPool para esta llamada, y se cierra al finalizar.
        ForkJoinPool pool = new ForkJoinPool(); // Crea un pool nuevo por cada llamada a sortConcurrentForkJoin
        pool.invoke(new QuickSortTask(v, 0, v.length - 1));
        pool.shutdown(); // Cierra el pool después de que la tarea principal ha terminado
    }

    // Clase interna para la tarea RecursiveAction de ForkJoinPool
    private static class QuickSortTask extends RecursiveAction {
        // Esta es la única línea que agregamos para quitar el warning.
        private static final long serialVersionUID = 1L; 

        private final int[] array;
        private final int low;
        private final int high;

        public QuickSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            // REGRESO A TU LÓGICA ORIGINAL:
            // No hay umbral de paralelismo aquí, la recursión continúa hasta el caso base low < high.
            // Para arreglos muy grandes, esto puede llevar a StackOverflowError si la profundidad
            // de la recursión es demasiada o si el heap no es suficientemente grande,
            // pero es el comportamiento que tenías antes de mi sugerencia de umbral.
            if (low < high) {
                int pi = partition(array, low, high);

                // Crear tareas para las sub-listas
                QuickSortTask leftTask = new QuickSortTask(array, low, pi - 1);
                QuickSortTask rightTask = new QuickSortTask(array, pi + 1, high);

                // Ejecutar ambas tareas en paralelo y esperar a que terminen
                invokeAll(leftTask, rightTask);
            }
        }
    }

    // El método partition es el mismo
    public static int partition(int v[], int low, int high) {
        int pivot = v[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (v[j] <= pivot) {
                i++;
                int temp = v[i];
                v[i] = v[j];
                v[j] = temp;
            }
        }

        int temp = v[i + 1];
        v[i + 1] = v[high];
        v[high] = temp;

        return (i + 1);
    }
}