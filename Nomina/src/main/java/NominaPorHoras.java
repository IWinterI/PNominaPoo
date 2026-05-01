import java.util.Date;

public class NominaPorHoras extends Nomina{
    private double HorasTrabajadas;
    private double TarifaHora;
    private double Bono;

    public NominaPorHoras(Date Fecha, Empleado Empleado, double TarifaHorasExtra, double HorasTrabajadas) {
        super(Fecha, Empleado, TarifaHorasExtra);
        this.HorasTrabajadas = HorasTrabajadas;
    }

    public double get_HorasTrabajadas() { return HorasTrabajadas; }
    public double get_TarifaHora() { return TarifaHora; }
    public double get_Bono() { return Bono; }

    private EmpleadoPorHoras TEmpleado(){
        EmpleadoPorHoras PorHora = (EmpleadoPorHoras) get_Empleado();
        return PorHora;
    }

    @Override
    protected double calcularHorasExtra(int horasExtra) {
        set_HorasExtra(horasExtra);
        set_HorasExtraPago(horasExtra * get_TarifaHoraExtra());
        return get_HorasExtraPago();
    }

    @Override
    public double calcularSueldo() {
        this.TarifaHora = TEmpleado().getTarifaHora();
        this.Bono = 0;
        double Total = HorasTrabajadas * TEmpleado().getTarifaHora();
        set_Total(Total);
        return Total;
    }

    @Override
    public double calcularSueldo(double Bono) {
        this.TarifaHora = TEmpleado().getTarifaHora();
        this.Bono = Bono;
        double Total = (HorasTrabajadas * TEmpleado().getTarifaHora() + Bono);
        set_Total(Total);
        return Total;
    }

    @Override
    public double calcularSueldo(double Bono, int horasExtra) {
        this.TarifaHora = TEmpleado().getTarifaHora();
        this.Bono = Bono;
        double Total = (HorasTrabajadas * TEmpleado().getTarifaHora() + Bono + calcularHorasExtra(horasExtra));
        set_Total(Total);
        return Total;
    }

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
