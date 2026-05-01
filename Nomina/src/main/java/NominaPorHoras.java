import java.util.Date;

/*
 Nomina concreta para empleados por horas.
 Extiende Nomina e implementa los calculos de sueldo usando las horas trabajadas, la tarifa normal,
 posibles bonos y horas extra.
 Tipo de clase: subclase concreta (hereda de Nomina).
 */
public class NominaPorHoras extends Nomina {
    private double HorasTrabajadas;
    private double TarifaHora;
    private double Bono;

    /*
     Construye una nomina para un empleado por horas.
     Fecha            Fecha de la nomina.
     Empleado         Empleado (debe ser EmpleadoPorHoras).
     TarifaHorasExtra Tarifa por hora extra.
     HorasTrabajadas  Total de horas trabajadas en el periodo.
     */
    public NominaPorHoras(Date Fecha, Empleado Empleado, double TarifaHorasExtra, double HorasTrabajadas) {
        super(Fecha, Empleado, TarifaHorasExtra);
        this.HorasTrabajadas = HorasTrabajadas;
    }

    public double get_HorasTrabajadas() { return HorasTrabajadas; }
    public double get_TarifaHora() { return TarifaHora; }
    public double get_Bono() { return Bono; }

    /*
     Convierte la referencia generica Empleado a EmpleadoPorHoras para obtener la tarifa.
     */
    private EmpleadoPorHoras TEmpleado() {
        return (EmpleadoPorHoras) get_Empleado();
    }

    /*
     Calcula el pago por horas extra.
     */
    @Override
    protected double calcularHorasExtra(int horasExtra) {
        set_HorasExtra(horasExtra);
        double Total = horasExtra * get_TarifaHoraExtra(); 
        set_HorasExtraPago(Total);
        return Total;
    }

    /*
     Sueldo base = horasTrabajadas * tarifaHora.
     */
    @Override
    public double calcularSueldo() {
        this.TarifaHora = TEmpleado().getTarifaHora();
        this.Bono = 0;
        double Total = HorasTrabajadas * TEmpleado().getTarifaHora();
        set_Total(Total);
        return Total;
    }

    /*
     Sueldo base mas bono.
     */
    @Override
    public double calcularSueldo(double Bono) {
        this.TarifaHora = TEmpleado().getTarifaHora();
        this.Bono = Bono;
        double Total = (HorasTrabajadas * TEmpleado().getTarifaHora() + Bono);
        set_Total(Total);
        return Total;
    }

    /*
     Sueldo base mas horas extra.
    */
    @Override
    public double calcularSueldo(int HorasExtra) {
        this.TarifaHora = TEmpleado().getTarifaHora();
        this.Bono = 0;
        double Total = (HorasTrabajadas * TEmpleado().getTarifaHora() + calcularHorasExtra(HorasExtra));
        set_Total(Total);
        return Total;
    }

    /*
     Sueldo base mas bono y horas extra.
     */
    @Override
    public double calcularSueldo(double Bono, int horasExtra) {
        this.TarifaHora = TEmpleado().getTarifaHora();
        this.Bono = Bono;
        double Total = (HorasTrabajadas * TEmpleado().getTarifaHora() + Bono + calcularHorasExtra(horasExtra));
        set_Total(Total);
        return Total;
    }

    /*
     Representacion detallada de la nomina.
     */
    @Override
    public String toString() {
        return "----- Detalles de la Nómina (Por Horas) -----\n" +
                "Empleado: " + get_Empleado().getNombre() + "\n" +
                "ID: " + get_Empleado().getId() + "\n" +
                "Puesto: " + get_Empleado().getPuesto() + "\n" +
                "Fecha: " + get_Fecha() + "\n" +
                "--- Desglose ---\n" +
                "Horas trabajadas: " + HorasTrabajadas + "\n" +
                "Tarifa por hora: " + TarifaHora + "\n" +
                "Sueldo base: " + (HorasTrabajadas * TarifaHora) + "\n" +
                "Bono: " + Bono + "\n" +
                "Horas extra trabajadas: " + get_HorasExtra() + "\n" +
                "Pago por horas extra: " + get_HorasExtraPago() + "\n" +
                "TOTAL: " + get_Total();
    }
}