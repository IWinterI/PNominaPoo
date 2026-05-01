/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class EmpleadoPorHoras extends Empleado {
    private double tarifaHora;

    public EmpleadoPorHoras(int ID, String Nombre, String Puesto, double tarifaHora) {
        super(ID, Nombre, Puesto);
        this.tarifaHora = tarifaHora;
    }

    public double getTarifaHora() { return tarifaHora; }

    public void setTarifaHora(double tarifaHora) { this.tarifaHora = tarifaHora; }

}
