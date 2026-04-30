/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*Clase hija que extiende de la clase Empleado*/

public class EmpleadoAsalariado extends Empleado {
    private double salarioMensual;

    public EmpleadoAsalariado(int ID, String Nombre, String Puesto, double salarioMensual) {
        super(ID, Nombre, Puesto);
        this.salarioMensual = salarioMensual;
    }
    /*Getter del atributo*/
    public double getSalarioMensual() {
        return salarioMensual;
    }

    // Sueldo base = salario mensual
    @Override
    public double calcularSueldo() {
        return salarioMensual;
    }

    // Sueldo con bono
    @Override
    public double calcularSueldo(double bono) {
        return salarioMensual + bono;
    }

    // Sueldo con bono y horas extra (se suman como monto adicional)
    @Override
    public double calcularSueldo(double bono, double horasExtra) {
        return salarioMensual + bono + horasExtra;
    }
}
