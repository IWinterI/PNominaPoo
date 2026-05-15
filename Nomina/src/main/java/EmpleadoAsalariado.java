import java.io.Serializable;

public class EmpleadoAsalariado extends Empleado implements Serializable{
    private static final long serialVersionUID = 1L;
    private double salarioMensual;
    
    public EmpleadoAsalariado(int ID, String Nombre, String Puesto, double salarioMensual) {
        super(ID, Nombre, Puesto);
        this.salarioMensual = salarioMensual;
    }
    
    public double getSalarioMensual() { return salarioMensual; }
    
    public void setSalarioMensual(double salarioMensual) { this.salarioMensual = salarioMensual; }
}
