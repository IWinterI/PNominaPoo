import java.util.Date;
import java.io.Serializable;

/*
 Nomina concreta para empleados asalariados.
 Extiende Nomina e implementa los calculos de sueldo usando el salario mensual del empleado,
 mas posibles bonos y horas extra.
 Tipo de clase: subclase concreta (hereda de Nomina).
 */

public class NominaAsalariado extends Nomina implements Serializable{

    private static final long serialVersionUID = 1L;
    private double Bono;
    private double Sueldo;   // sueldo base mensual

    /*
     Construye una nómina para un empleado asalariado.
     Fecha            Fecha de emision de la nomina.
     Empleado         Referencia al empleado (debe ser EmpleadoAsalariado).
     TarifaHorasExtra Tarifa aplicada a cada hora extra.
     */
    public NominaAsalariado(Date Fecha, Empleado Empleado, double TarifaHorasExtra) {
        super(Fecha, Empleado, TarifaHorasExtra);
    }

    public double get_Bono() { return Bono; }
    public double get_Sueldo() { return Sueldo; }

    /*
     Convierte la referencia genérica Empleado a EmpleadoAsalariado para acceder a su salario.
     Retorna el empleado visto como EmpleadoAsalariado.
     */
    private EmpleadoAsalariado TEmpleado() {
        return (EmpleadoAsalariado) get_Empleado();
    }

    /*
     Calcula el pago por horas extra y actualiza los atributos correspondientes.
     */
    @Override
    protected double calcularHorasExtra(int horasExtra) {
        set_HorasExtra(horasExtra);
        double Total = horasExtra * get_TarifaHoraExtra(); 
        set_HorasExtraPago(Total);
        return Total;
    }

    /*
     Calcula sueldo sin bono ni horas extra. El total es el salario mensual.
     */
    @Override
    public double calcularSueldo() {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = 0;
        double Total = TEmpleado().getSalarioMensual();
        set_Total(Total);
        return Total;
    }

    /*
     Calcula sueldo añadiendo un bono.
     */
    @Override
    public double calcularSueldo(double Bono) {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = Bono;
        double Total = TEmpleado().getSalarioMensual() + Bono;
        set_Total(Total);
        return Total;
    }

    /*
    Calcula sueldo añadiendo horas extra.
    */
    @Override
    public double calcularSueldo(int HorasExtra) {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = 0;
        double Total = TEmpleado().getSalarioMensual() + calcularHorasExtra(HorasExtra);
        set_Total(Total);
        return Total;
    }

    /*
    Calcula sueldo añadiendo bono y horas extra.
     */
    @Override
    public double calcularSueldo(double Bono, int HorasExtra) {
        this.Sueldo = TEmpleado().getSalarioMensual();
        this.Bono = Bono;
        double Total = TEmpleado().getSalarioMensual() + Bono + calcularHorasExtra(HorasExtra);
        set_Total(Total);
        return Total;
    }

    /*
    Devuelve una representación detallada de la nómina.
     */
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