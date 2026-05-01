/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.util.Date;

public abstract class Nomina implements IPagable {

    private Date Fecha;
    private Empleado Empleado;
    private int HorasExtra;
    private double TarifaHoraExtra;
    private double HorasExtraPago;
    private double Total;

    public Nomina(Date Fecha, Empleado Empleado, double TarifaHoraExtra){
        this.Fecha = Fecha;
        this.Empleado = Empleado;
        this.TarifaHoraExtra = TarifaHoraExtra;
        this.HorasExtra = 0;
        this.HorasExtraPago = 0;
        this.Total = 0;
    }

    public Date get_Fecha() { return Fecha; }
    public Empleado get_Empleado() { return Empleado; }
    public double get_Total() { return Total; }
    public int get_HorasExtra() { return HorasExtra; }
    public double get_TarifaHoraExtra() { return TarifaHoraExtra; }
    public double get_HorasExtraPago() { return HorasExtraPago; } 

    public void set_HorasExtra(int HorasExtra) { this.HorasExtra = HorasExtra; }
    public void set_HorasExtraPago(double HorasExtraPago) { this.HorasExtraPago = HorasExtraPago; }
    protected void set_Total(double Total) { this.Total = Total; }

    @Override
    public abstract double calcularSueldo();

    @Override
    public abstract double calcularSueldo(double bono);

    @Override
    public abstract double calcularSueldo(double bono, int horasExtra);

    protected abstract double calcularHorasExtra(int horasExtra);
}
