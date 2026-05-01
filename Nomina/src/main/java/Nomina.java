import java.util.Date;
import java.io.Serializable;

/*
 Clase abstracta que implementa IPagable y representa el concepto generico de una nomina.
 Contiene los atributos comunes de cualquier nomina (fecha, empleado, horas extra, tarifa de horas extra, total)
 y define los metodos abstractos para calcular el sueldo segun el tipo de empleado.
 Las subclases concretas (NominaAsalariado, NominaPorHoras) implementan estos calculos.
 Tipo de clase: clase abstracta (padre de la jerarquía de nóminas).
 */

public abstract class Nomina implements IPagable, Serializable{

    private static final long serialVersionUID = 1L;
    private Date Fecha;
    private Empleado Empleado;       // referencia al empleado (polimorfica, puede ser asalariado o por horas)
    private int HorasExtra;
    private double TarifaHoraExtra;
    private double HorasExtraPago;
    private double Total;

    /*
     Constructor base que inicializa fecha, empleado y tarifa de horas extra.
     Horas extra y total se inicializan en cero.
     */
    public Nomina(Date Fecha, Empleado Empleado, double TarifaHoraExtra) {
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

    /* Permite a las subclases fijar el total calculado. */
    protected void set_Total(double Total) { this.Total = Total; }

    // Métodos abstractos de la interfaz, que seran implementados por cada tipo de noómina.
    @Override
    public abstract double calcularSueldo();

    @Override
    public abstract double calcularSueldo(double bono);

    @Override
    public abstract double calcularSueldo(double bono, int horasExtra);

    /*
     Calcula el pago correspondiente a las horas extra segun la tarifa de hora extra.
     Se invoca desde las subclases cuando se requiera.
     horasExtra Numero de horas extra trabajadas.
     Monto total por horas extra.
     */
    protected abstract double calcularHorasExtra(int horasExtra);
}