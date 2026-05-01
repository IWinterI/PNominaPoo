/**
 Clase interfaz para el calculo de los sueldos
 */
public interface IPagable {

    double calcularSueldo();

    double calcularSueldo(double bono);

    double calcularSueldo(double bono, int horasExtra);
}
