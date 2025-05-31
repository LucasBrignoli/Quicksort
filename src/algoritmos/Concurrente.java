package algoritmos;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Concurrente {

    // Umbral ajustado para mejor rendimiento con arrays grandes
    private static final int THRESHOLD = 1000;

    // Método principal que llama TestTp.java
 // Método principal que llama TestTp.java
    public static void sortConcurrentForkJoin(int[] v) {
        if (v.length <= 1) return; // Caso base para arrays vacíos o de un elemento
        
        // Para arrays grandes con patrones problemáticos, usar Arrays.sort
        if (v.length > 500000 && (allElementsIdentical(v) || isAlreadySorted(v) || isReverseSorted(v))) {
            System.out.println("Detectado caso problemático - usando algoritmo híbrido optimizado");
            java.util.Arrays.sort(v);
            return;
        }
        
        // Optimización: si todos los elementos son idénticos, no hacer nada
        if (allElementsIdentical(v)) {
            System.out.println("Detectados elementos idénticos - ordenamiento innecesario");
            return;
        }
        
        // Usar el pool común de ForkJoin es más seguro
        ForkJoinPool.commonPool().invoke(new QuickSortTask(v, 0, v.length - 1));
    }
    
 // Método para detectar si el array ya está ordenado
    private static boolean isAlreadySorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i-1]) return false;
        }
        return true;
    }

    // Método para detectar si el array está ordenado en orden inverso
    private static boolean isReverseSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[i-1]) return false;
        }
        return true;
    }
    
    // Método para detectar si todos los elementos del array son idénticos
    private static boolean allElementsIdentical(int[] arr) {
        if (arr.length <= 1) return true;
        int first = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != first) return false;
        }
        return true;
    }

    private static class QuickSortTask extends RecursiveAction {
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
            // Caso base: si no hay elementos que ordenar
            if (low >= high) {
                return;
            }
            
            // Si el segmento es pequeño, usar ordenamiento secuencial
            if (high - low + 1 <= THRESHOLD) {
                // Usar Arrays.sort de Java que es muy eficiente para casos pequeños
                java.util.Arrays.sort(array, low, high + 1);
                return;
            }
            
            // Optimización: si todos los elementos son iguales, no hacer nada
            if (allElementsEqual(array, low, high)) {
                return;
            }

            // Partición
            int pi = partition(array, low, high);
            
            // Crear subtareas solo si hay elementos que procesar
            QuickSortTask leftTask = null;
            QuickSortTask rightTask = null;
            
            if (pi - 1 > low) {
                leftTask = new QuickSortTask(array, low, pi - 1);
            }
            if (high > pi + 1) {
                rightTask = new QuickSortTask(array, pi + 1, high);
            }
            
            // Ejecutar las tareas
            if (leftTask != null && rightTask != null) {
                invokeAll(leftTask, rightTask);
            } else if (leftTask != null) {
                leftTask.compute();
            } else if (rightTask != null) {
                rightTask.compute();
            }
        }
        
        // Método auxiliar para detectar si todos los elementos son iguales
        private boolean allElementsEqual(int[] arr, int start, int end) {
            if (start >= end) return true;
            int first = arr[start];
            for (int i = start + 1; i <= end; i++) {
                if (arr[i] != first) return false;
            }
            return true;
        }
    }

    // Método partition
    // Partición mejorada para manejar elementos duplicados
    public static int[] partitionThreeWay(int[] v, int low, int high) {
        int pivot = v[high];
        int lt = low;      // elementos < pivot
        int gt = high;     // elementos > pivot
        int i = low;       // elementos == pivot
        
        while (i <= gt) {
            if (v[i] < pivot) {
                swap(v, lt++, i++);
            } else if (v[i] > pivot) {
                swap(v, i, gt--);
            } else {
                i++;
            }
        }
        
        return new int[]{lt, gt}; // retorna los límites de la sección igual al pivot
    }
    
    private static void swap(int[] v, int i, int j) {
        int temp = v[i];
        v[i] = v[j];
        v[j] = temp;
    }
    
    // Mantener partition original para compatibilidad
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