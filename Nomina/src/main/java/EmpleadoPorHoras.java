import java.io.Serializable;

/*
 Clase concreta que extiende Empleado para representar un empleado que cobra por horas trabajadas.
 Añade el atributo tarifaHora (precio por hora normal) y sus metodos de acceso.
 Es utilizada por la clase NominaPorHoras para calcular los sueldos.
 Tipo de clase: subclase concreta (hereda de Empleado).
 */

public class EmpleadoPorHoras extends Empleado implements Serializable{
    private static final long serialVersionUID = 1L;
    private double tarifaHora;

    
    /*
     Construye un empleado por horas.
     ID         Identificador del empleado.
     Nombre     Nombre del empleado.
     Puesto     Puesto del empleado.
     tarifaHora Tarifa por hora ordinaria.
     */
    public EmpleadoPorHoras(int ID, String Nombre, String Puesto, double tarifaHora) {
        super(ID, Nombre, Puesto);
        this.tarifaHora = tarifaHora;
    }

    /* Tarifa por hora ordinaria. */
    public double getTarifaHora() { return tarifaHora; }

    /* Establece una nueva tarifa por hora. */
    public void setTarifaHora(double tarifaHora) { this.tarifaHora = tarifaHora; }

}
