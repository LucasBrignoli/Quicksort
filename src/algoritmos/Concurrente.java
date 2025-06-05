package algoritmos;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Concurrente {

    // Definimos un umbral de 50,000 elementos
    private static final int THRESHOLD = 50000;

    // Este es el metodo principal que van a llamar desde afuera para ordenar un array
    public static void sortConcurrentForkJoin(int[] v) {
        // Si el array esta vacio o tiene un solo elemento, ya esta "ordenado"
        if (v.length <= 1) return; 

        // Aqui hacemos una optimizacion: si tenemos un array muy grande
        // y detectamos que tiene patrones problematicos (todos iguales, ya ordenado, o al reves),
        // mejor usamos el algoritmo hibrido de Java que esta super optimizado
        if (v.length > 500000 && (allElementsIdentical(v) || isAlreadySorted(v) || isReverseSorted(v))) {
            System.out.println("Detectado caso problematico - usando algoritmo hibrido optimizado");
            java.util.Arrays.sort(v); // Le damos el trabajo a Java, que lo hace mejor
            return;
        }

        // Otra optimizacion: si todos los elementos son iguales, no hay nada que ordenar
        if (allElementsIdentical(v)) {
            System.out.println("Detectados elementos identicos - ordenamiento innecesario");
            return; // Nos ahorramos todo el trabajo
        }

        // Aqui es donde empieza el paralelismo - usamos el pool comun de ForkJoin
        // que maneja automaticamente los hilos segun los procesadores disponibles
        ForkJoinPool.commonPool().invoke(new QuickSortTask(v, 0, v.length - 1));
    }

    // Metodo que recorre el array para ver si ya esta ordenado de menor a mayor
    private static boolean isAlreadySorted(int[] arr) {
        // Comparamos cada elemento con el anterior
        for (int i = 1; i < arr.length; i++) {
            // Si encontramos uno que es menor que el anterior, no esta ordenado
            if (arr[i] < arr[i-1]) return false;
        }
        return true; // Si llegamos hasta aca, esta perfectamente ordenado
    }

    // Similar al anterior, pero verifica si esta ordenado al reves (mayor a menor)
    private static boolean isReverseSorted(int[] arr) {
        // Recorremos comparando cada elemento con el anterior
        for (int i = 1; i < arr.length; i++) {
            // Si uno es mayor que el anterior, no esta en orden descendente
            if (arr[i] > arr[i-1]) return false;
        }
        return true; // Esta ordenado al reves
    }

    // Verifica si todos los elementos del array son exactamente iguales
    private static boolean allElementsIdentical(int[] arr) {
        // Arrays vacios o de un elemento tecnicamente tienen elementos "identicos"
        if (arr.length <= 1) return true;
        
        int first = arr[0]; // Tomamos el primer elemento como referencia
        // Comparamos todos los demas con el primer elemento
        for (int i = 1; i < arr.length; i++) {
            // Si encontramos uno diferente, no son todos iguales
            if (arr[i] != first) return false;
        }
        return true; // Son todos iguales
    }

    // Esta es la clase interna que extiende RecursiveAction - es lo que permite la paralelizacion
    private static class QuickSortTask extends RecursiveAction {
        private static final long serialVersionUID = 1L; // Necesario para la serializacion

        // Los datos que necesita cada tarea para trabajar
        private final int[] array; // El array que vamos a ordenar
        private final int low;     // Indice de inicio del segmento
        private final int high;    // Indice de fin del segmento

        // Constructor que inicializa los datos de la tarea
        public QuickSortTask(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        // Este metodo es el corazon de cada tarea - aqui es donde hacemos el trabajo
        @Override
        protected void compute() {
            // Si no hay elementos que ordenar (indices se cruzaron), terminamos
            if (low >= high) {
                return;
            }

            // Si el segmento es pequeño, mejor usamos ordenamiento secuencial
            // porque crear mas tareas paralelas seria contraproducente
            if (high - low + 1 <= THRESHOLD) {
                // Le pasamos el trabajo a Arrays.sort que es muy eficiente para casos pequeños
                java.util.Arrays.sort(array, low, high + 1);
                return;
            }

            // Otra optimizacion: si en este segmento todos los elementos son iguales, no hay nada que hacer
            if (allElementsEqual(array, low, high)) {
                return; // Nos ahorramos el trabajo
            }
            
            // Usamos particion de tres vias en lugar de la tradicional
            // Esto es mucho mejor cuando hay elementos duplicados
            int[] pivots = partitionThreeWay(array, low, high); // Particionamos en tres grupos
            int lt = pivots[0]; // Primer indice de elementos iguales al pivote
            int gt = pivots[1]; // Ultimo indice de elementos iguales al pivote

            // Preparamos las subtareas para los segmentos que necesitan mas trabajo
            QuickSortTask leftTask = null;  // Tarea para elementos menores al pivote
            QuickSortTask rightTask = null; // Tarea para elementos mayores al pivote

            // Solo creamos la tarea izquierda si hay elementos menores al pivote
            if (lt - 1 > low) { 
                leftTask = new QuickSortTask(array, low, lt - 1);
            }
            
            // Solo creamos la tarea derecha si hay elementos mayores al pivote
            if (high > gt + 1) { 
                rightTask = new QuickSortTask(array, gt + 1, high);
            }

            // Ahora ejecutamos las tareas segun lo que tengamos
            if (leftTask != null && rightTask != null) {
                // Si tenemos ambas tareas, las ejecutamos en paralelo
                invokeAll(leftTask, rightTask);
            } else if (leftTask != null) {
                // Si solo tenemos tarea izquierda, la ejecutamos directamente
                leftTask.compute();
            } else if (rightTask != null) {
                // Si solo tenemos tarea derecha, la ejecutamos directamente
                rightTask.compute();
            }
            // Si no hay ninguna tarea, significa que todo el segmento era igual al pivote
        }

        // Metodo auxiliar que verifica si todos los elementos en un rango son iguales
        private boolean allElementsEqual(int[] arr, int start, int end) {
            // Si el rango es invalido, consideramos que son "iguales"
            if (start >= end) return true;
            
            int first = arr[start]; // Tomamos el primer elemento como referencia
            // Comparamos todos los elementos del rango con el primero
            for (int i = start + 1; i <= end; i++) {
                // Si encontramos uno diferente, no son todos iguales
                if (arr[i] != first) return false;
            }
            return true; // Son todos iguales en este rango
        }
    }

    // Este es el metodo de particion de tres vias
    // Divide el array en tres grupos: menores, iguales y mayores al pivote
    public static int[] partitionThreeWay(int[] v, int low, int high) {
        int pivot = v[high]; // Usamos el ultimo elemento como pivote
        int lt = low;        // Puntero para elementos menores (less than)
        int gt = high;       // Puntero para elementos mayores (greater than)  
        int i = low;         // Puntero que recorre el array

        // Particion de tres vias
        while (i <= gt) {
            if (v[i] < pivot) {
                // Si el elemento es menor al pivote, lo mandamos a la zona izquierda
                swap(v, lt++, i++);
            } else if (v[i] > pivot) {
                // Si es mayor, lo mandamos a la zona derecha (y no avanzamos i porque
                // el elemento que trajimos desde gt todavia no lo examinamos)
                swap(v, i, gt--);
            } else {
                // Si es igual al pivote, avanzamos
                i++;
            }
        }

        // Retornamos los limites de la zona donde estan los elementos iguales al pivote
        return new int[]{lt, gt}; 
    }

    // Metodo simple para intercambiar dos elementos en el array
    private static void swap(int[] v, int i, int j) {
        int temp = v[i]; // Guardamos el valor de la posicion i
        v[i] = v[j];     // Ponemos el valor de j en la posicion i
        v[j] = temp;     // Ponemos el valor original de i en la posicion j
    }

}