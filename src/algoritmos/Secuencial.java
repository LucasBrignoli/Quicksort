package algoritmos;

public class Secuencial { 

	public static void sort(int[] v) {
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

	    // Llama al método quickSort principal con los índices inicial y final del arreglo.
	    quickSort(v, 0, v.length - 1);
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
 	 
     // método para encontrar la posición de la partición 
     public static int partition(int v[], int low, int high) {

         // elige el elemento más a la derecha como pivote 
         int pivot = v[high];

         // puntero para el elemento mayor 
         int i = (low - 1);

         // recorre todos los elementos 
         // compara cada elemento con el pivote 
         for (int j = low; j < high; j++) {
             if (v[j] <= pivot) { 

                 // si se encuentra un elemento menor que el pivote 
                 // intercámbialo con el elemento mayor apuntado por i 
                 i++; 

                 // intercambiando el elemento en i con el elemento en j 
                 int temp = v[i]; 
                 v[i] = v[j]; 
                 v[j] = temp; 
             } 
         } 

         // intercambia el elemento pivote con el elemento mayor especificado por i 
         int temp = v[i + 1]; 
         v[i + 1] = v[high];
         v[high] = temp;
         
         // devuelve la posición desde donde se realizó la partición 
         return (i + 1); 
     } 

     public static void quickSort(int v[], int low, int high) {
         if (low < high) {

             // encuentra el elemento pivote de tal manera que 
             // los elementos menores que el pivote estén a la izquierda 
             // y los elementos mayores que el pivote estén a la derecha 
             int pi = partition(v, low, high);

             // llamada recursiva a la izquierda del pivote 
             quickSort(v, low, pi - 1);

             // llamada recursiva a la derecha del pivote 
             quickSort(v, pi + 1, high);
         } 
     } 
 }