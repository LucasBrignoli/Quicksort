package algoritmos;

public class Secuencial { 

    // Este es el método principal que llaman desde afuera para ordenar un array de forma secuencial
    public static void sort(int[] v) {
        // 	Si el array es muy grande y tiene patrones problemáticos,
    	//  mejor usamos el algoritmo híbrido de Java
        if (v.length > 500000 && (allElementsIdentical(v) || isAlreadySorted(v) || isReverseSorted(v))) {
            System.out.println("Detectado caso problemático - usando algoritmo híbrido optimizado");
            java.util.Arrays.sort(v); // Java sabe manejar estos casos mejor que nosotros
            return;
        }
        
        // Si todos los elementos son iguales, nos ahorramos todo el trabajo
        if (allElementsIdentical(v)) {
            System.out.println("Detectados elementos idénticos - ordenamiento innecesario");
            return; // No hay nada que ordenar
        }

        // Llamamos al metodo recursivo con todo el rango del array (desde 0 hasta length-1)
        quickSort(v, 0, v.length - 1);
    }
    
    // Metodo que verifica si el array ya esta ordenado de menor a mayor
    private static boolean isAlreadySorted(int[] arr) {
        // Recorremos comparando cada elemento con su anterior
        for (int i = 1; i < arr.length; i++) {
            // Si encontramos uno menor que el anterior, no está ordenado
            if (arr[i] < arr[i-1]) return false;
        }
        return true; // Si llegamos aqui, esta perfectamente ordenado
    }

    // Verifica si el array esta ordenado al reves (de mayor a menor)
    private static boolean isReverseSorted(int[] arr) {
        // Comparamos cada elemento con el anterior
        for (int i = 1; i < arr.length; i++) {
            // Si uno es mayor que el anterior, no esta en orden descendente
            if (arr[i] > arr[i-1]) return false;
        }
        return true; // Esta ordenado inversamente
    }

    // Comprueba si todos los elementos del array son exactamente iguales
    private static boolean allElementsIdentical(int[] arr) {
        // Arrays vacios o de un elemento se consideran "identicos"
        if (arr.length <= 1) return true;
        
        int first = arr[0]; // Usamos el primer elemento como referencia
        // Comparamos todos los demás elementos con el primero
        for (int i = 1; i < arr.length; i++) {
            // Si encontramos uno diferente, no son todos iguales
            if (arr[i] != first) return false;
        }
        return true; // Todos son iguales al primero
    }
      
    // Metodo que encuentra la posicion donde particionar el array
    public static int partition(int v[], int low, int high) {

        // Elegimos el ultimo elemento como pivote
        int pivot = v[high];

        // Este puntero nos ayuda a mantener los elementos menores al pivote a la izquierda
        int i = (low - 1);

        // Recorremos todos los elementos excepto el pivote
        for (int j = low; j < high; j++) {
            // Si el elemento actual es menor o igual al pivote
            if (v[j] <= pivot) { 

                // Encontramos un elemento que va a la izquierda, asi que
                // incrementamos el puntero de elementos menores
                i++; 

                // Intercambiamos el elemento actual con el que esta en la posicion i
                // Esto mantiene todos los elementos <= pivote a la izquierda
                int temp = v[i]; 
                v[i] = v[j]; 
                v[j] = temp; 
            } 
        } 

        // Ahora ponemos el pivote en su posicion correcta
        // Todos los elementos a su izquierda son menores o iguales
        // Todos los de la derecha son mayores
        int temp = v[i + 1]; 
        v[i + 1] = v[high]; // El pivote va en i+1
        v[high] = temp;
        
        // Retornamos la posicion donde quedo el pivote
        // Esta es la línea divisoria para las proximas llamadas recursivas
        return (i + 1); 
    } 

    // El metodo recursivo que implementa el algoritmo quicksort
    public static void quickSort(int v[], int low, int high) {
        // Condición de parada: si low >= high, significa que no hay elementos que ordenar
        // (el subarreglo tiene 0 o 1 elemento)
        if (low < high) {

            // Particionamos el array y obtenemos la posicion del pivote
            // Después de esto, el pivote estara en su posición final correcta
            int pi = partition(v, low, high);

            // Ahora aplicamos divide y vencerás:
            // Ordenamos recursivamente la parte izquierda (elementos menores al pivote)
            quickSort(v, low, pi - 1);

            // Y tambien ordenamos recursivamente la parte derecha (elementos mayores al pivote)
            quickSort(v, pi + 1, high);
        } 
        // Si low no es menor que high, la recursion termina aqui
    } 
}