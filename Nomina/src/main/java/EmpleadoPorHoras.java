/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*Clase hija que extiende de Empleado*/
public class EmpleadoPorHoras extends Empleado {
    private double horasTrabajadas;
    private double tarifaHora;

    public EmpleadoPorHoras(int ID, String Nombre, String Puesto, double horasTrabajadas, double tarifaHora) {
        super(ID, Nombre, Puesto);
        this.horasTrabajadas = horasTrabajadas;
        this.tarifaHora = tarifaHora;
    }
    /*Getter basicos de los atributos*/
    public double getHorasTrabajadas() { return horasTrabajadas; }
    public double getTarifaHora() { return tarifaHora; }

    public void setHorasTrabajadas(double horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }
    public void setTarifaHora(double tarifaHora) { this.tarifaHora = tarifaHora; }

    // Sueldo base = horas normales * tarifa
    @Override
    public double calcularSueldo() {
        return horasTrabajadas * tarifaHora;
    }

    // Sueldo con bono (sin horas extra específicas)
    @Override
    public double calcularSueldo(double bono) {
        return (horasTrabajadas * tarifaHora) + bono;
    }

    // Sueldo con bono y un monto adicional por horas extra
    @Override
    public double calcularSueldo(double bono, double horasExtra) {
        return (horasTrabajadas * tarifaHora) + bono + horasExtra;
    }
}
