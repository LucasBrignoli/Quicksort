package algoritmos;

public class Secuencial { 

 	public static void sort(int[] v) { 
         // Llama al método quickSort principal con los índices inicial y final del arreglo. 
         quickSort(v, 0, v.length - 1); 
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