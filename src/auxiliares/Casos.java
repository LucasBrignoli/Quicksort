package auxiliares;

public enum Casos {
	ALEATORIO("Aleatorio"),
	ORDENADO("Ordenado"),
	ORDENADO_INVERSO("Ordenado inverso"),
	ELEMENTOS_IDENTICOS("Elementos identicos");
	
	String nombre;
	
	Casos(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
}