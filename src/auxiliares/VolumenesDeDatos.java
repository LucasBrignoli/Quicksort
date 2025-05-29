package auxiliares;

public enum VolumenesDeDatos {
	DIEZ(10,"diez"),
	MIL(1000,"mil"),
	CIENMIL(100000,"cien mil"),
	MILLON(1000000,"1 millón");
	
	int n;
	String descripcion;
	
	VolumenesDeDatos(int n, String descripcion) {
		this.n = n;
		this.descripcion = descripcion;
	}
	
	public int getN() {
		return this.n;
	}
	
	public String getDescripcion() {
		return this.descripcion;
	}
}