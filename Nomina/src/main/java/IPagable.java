public interface IPagable {
    double calcularSueldo();

    double calcularSueldo(double bono);

    double calcularSueldo(double bono, int horasExtra);

    double calcularSueldo(int horasExtra);
}
