/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.util.Date;

public class Nomina {

    private Date Fecha;
    private Empleado Empleado;
    private double Sueldo;

    public Nomina(Date Fecha, Empleado Empleado, double Sueldo){
        this.Fecha = Fecha;
        this.Empleado = Empleado;
        this.Sueldo = Sueldo;
    }

    public Date get_Fecha() { return Fecha; }
    public Empleado get_Empleado() { return Empleado; }
    public double get_Sueldo() { return Sueldo; }
}
