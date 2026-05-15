import java.io.Serializable;

public class EmpleadoPorHoras extends Empleado implements Serializable{
    private static final long serialVersionUID = 1L;
    private double tarifaHora;

    public EmpleadoPorHoras(int ID, String Nombre, String Puesto, double tarifaHora) {
        super(ID, Nombre, Puesto);
        this.tarifaHora = tarifaHora;
    }

    public double getTarifaHora() { return tarifaHora; }

    public void setTarifaHora(double tarifaHora) { this.tarifaHora = tarifaHora; }

}
