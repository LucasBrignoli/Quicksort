package auxiliares;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Cronometro {
	private long inicio;
    private long duracion;
    private TimeUnit unidadTiempo;
    private int precisionDecimales;

    
    // CONSTRUCTORES
    public Cronometro() {
        this.inicio = 0;
        this.duracion = 0;
        this.unidadTiempo = TimeUnit.MILLISECONDS;
        this.precisionDecimales = 3;
    }
    
    public Cronometro(TimeUnit unidadTiempo, int precisionDecimales) {
        validarUnidadTiempo(unidadTiempo);
        validarPrecisionDecimales(precisionDecimales);
        this.unidadTiempo = unidadTiempo;
        this.precisionDecimales = precisionDecimales;
        this.inicio = 0;
        this.duracion = 0;
    }
    
    // CONFIGURACIÓN
    public void setUnidadTiempo(TimeUnit unidad) {
        validarUnidadTiempo(unidad);
        this.unidadTiempo = unidad;
    }

    public void setPrecisionDecimales(int precisionDecimales) {
        validarPrecisionDecimales(precisionDecimales);
        this.precisionDecimales = precisionDecimales;
    }

    // MÉTODOS PUBLICOS
    public void iniciar() {
        if (this.inicio == 0) {
            this.inicio = System.nanoTime();
        }
    }

    public void detener() {
        if (this.inicio != 0) {
            this.duracion += System.nanoTime() - this.inicio;
            this.inicio = 0;
        }
    }

    public void reiniciar() {
        this.inicio = 0;
        this.duracion = 0;
    }

    public String obtenerTiempo() {
        return convertirTiempo(this.duracion + (this.inicio != 0 ? System.nanoTime() - this.inicio : 0));
    }

    public String obtenerParcial() {
        if (this.inicio == 0) {
            return "No hay tiempo parcial disponible";
        }
        return convertirTiempo(System.nanoTime() - this.inicio);
    }
    
    public static String obtenerHoraPrecisa() {
		LocalTime time = Instant.now().atZone(ZoneId.systemDefault()).toLocalTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSSSS");
		return time.format(formatter);
	}

    
    // MÉTODOS PRIVADOS
    private String convertirTiempo(long tiempoEnNanosegundos) {
        double tiempoConvertido;
        switch (this.unidadTiempo) {
            case NANOSECONDS:
                tiempoConvertido = tiempoEnNanosegundos;
                break;
            case MICROSECONDS:
                tiempoConvertido = tiempoEnNanosegundos / 1000.0;
                break;
            case MILLISECONDS:
                tiempoConvertido = tiempoEnNanosegundos / 1_000_000.0;
                break;
            case SECONDS:
                tiempoConvertido = tiempoEnNanosegundos / 1_000_000_000.0;
                break;
            case MINUTES:
                tiempoConvertido = tiempoEnNanosegundos / 60_000_000_000.0;
                break;
            case HOURS:
                tiempoConvertido = tiempoEnNanosegundos / 3_600_000_000_000.0;
                break;
            default:
                throw new IllegalArgumentException("Unidad de tiempo no soportada: " + this.unidadTiempo);
        }
        String formato = "%." + this.precisionDecimales + "f"; // Formato con la precisión adecuada
        return String.format(Locale.US, formato, tiempoConvertido) + " " + abreviaturaUnidad(); // Se añade la abreviatura
    }
    
    private String abreviaturaUnidad() {
        switch (this.unidadTiempo) {
            case NANOSECONDS:
                return "ns";
            case MICROSECONDS:
                return "µs";
            case MILLISECONDS:
                return "ms";
            case SECONDS:
                return "s";
            case MINUTES:
                return "min";
            case HOURS:
                return "h";
            default:
                throw new IllegalArgumentException("Unidad de tiempo no soportada: " + this.unidadTiempo);
        }
    }
    
    
    // VALIDACIONES
    private void validarUnidadTiempo(TimeUnit unidad) {
        if (unidad != TimeUnit.NANOSECONDS && unidad != TimeUnit.MICROSECONDS && unidad != TimeUnit.MILLISECONDS &&
            unidad != TimeUnit.SECONDS && unidad != TimeUnit.MINUTES && unidad != TimeUnit.HOURS) {
            throw new IllegalArgumentException("Unidad de tiempo no permitida: " + unidad);
        }
    }

    private void validarPrecisionDecimales(int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException("La precisión de decimales no puede ser negativa");
        }
    }
	
	
	
}