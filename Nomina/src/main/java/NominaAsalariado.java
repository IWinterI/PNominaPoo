import java.util.Date;
import java.io.Serializable;

public class NominaAsalariado extends Nomina implements Serializable{

    private static final long serialVersionUID = 1L;
    private double Bono;
    private double Sueldo;

    public NominaAsalariado(Date Fecha, Empleado Empleado, double TarifaHorasExtra) {
        super(Fecha, Empleado, TarifaHorasExtra);
    }

    public double get_Bono() { return Bono; }
    public double get_Sueldo() { return Sueldo; }

    private EmpleadoAsalariado TEmpleado() {
        return (EmpleadoAsalariado) get_Empleado();
    }

    @Override
    protected double calcularHorasExtra(int horasExtra) {
        set_HorasExtra(horasExtra);
        double Total = horasExtra * get_TarifaHoraExtra(); 
        set_HorasExtraPago(Total);
        return Total;
    }

    @Override
    public double calcularSueldo() {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = 0;
        double Total = TEmpleado().getSalarioMensual();
        set_Total(Total);
        return Total;
    }

    @Override
    public double calcularSueldo(double Bono) {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = Bono;
        double Total = TEmpleado().getSalarioMensual() + Bono;
        set_Total(Total);
        return Total;
    }

    @Override
    public double calcularSueldo(int HorasExtra) {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = 0;
        double Total = TEmpleado().getSalarioMensual() + calcularHorasExtra(HorasExtra);
        set_Total(Total);
        return Total;
    }

    @Override
    public double calcularSueldo(double Bono, int HorasExtra) {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = Bono;
        double Total = TEmpleado().getSalarioMensual() + Bono + calcularHorasExtra(HorasExtra);
        set_Total(Total);
        return Total;
    }

    @Override
    public String toString() {
        return "----- Detalles de la Nómina (Asalariado) -----\n" +
                "Empleado: " + get_Empleado().getNombre() + "\n" +
                "ID: " + get_Empleado().getId() + "\n" +
                "Puesto: " + get_Empleado().getPuesto() + "\n" +
                "Fecha: " + get_Fecha() + "\n" +
                "--- Desglose ---\n" +
                "Sueldo base: " + Sueldo + "\n" +
                "Bono: " + Bono + "\n" +
                "Horas extra trabajadas: " + get_HorasExtra() + "\n" +
                "Pago por horas extra: " + get_HorasExtraPago() + "\n" +
                "TOTAL: " + get_Total();
    }
}
