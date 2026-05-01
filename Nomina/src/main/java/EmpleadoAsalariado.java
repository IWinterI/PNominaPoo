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

    public double getSalarioMensual() { return salarioMensual; }
    public void setSalarioMensual(double salarioMensual) { this.salarioMensual = salarioMensual; }
}
