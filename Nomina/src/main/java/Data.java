import java.io.Serializable;
import java.util.List;

/**
 * Contenedor serializable que almacena la lista completa de empleados
 * y la lista completa de nóminas para persistir el estado del sistema.
 */
public class Data implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Empleado> empleados;
    private List<Nomina> nominas;

    public Data(List<Empleado> empleados, List<Nomina> nominas) {
        this.empleados = empleados;
        this.nominas = nominas;
    }

    public List<Empleado> getEmpleados() { return empleados; }
    public List<Nomina> getNominas() { return nominas; }
}