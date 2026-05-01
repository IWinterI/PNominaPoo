/*
 Clase concreta que extiende Empleado para representar un empleado que recibe un salario mensual fijo.
 Añade el atributo salarioMensual y sus metodos de acceso.
 Es utilizada por la clase NominaAsalariado para calcular los sueldos.
 Tipo de clase: subclase concreta (hereda de Empleado).
 */
public class EmpleadoAsalariado extends Empleado {
    private double salarioMensual;
    
    /*
     Construye un empleado asalariado.
     ID             Identificador del empleado.
     Nombre         Nombre del empleado.
     Puesto         Puesto del empleado.
     salarioMensual Salario fijo mensual.
     */
    public EmpleadoAsalariado(int ID, String Nombre, String Puesto, double salarioMensual) {
        super(ID, Nombre, Puesto);
        this.salarioMensual = salarioMensual;
    }
    
    /* Salario mensual actual. */
    public double getSalarioMensual() { return salarioMensual; }
    
    /* Establece un nuevo salario mensual. */
    public void setSalarioMensual(double salarioMensual) { this.salarioMensual = salarioMensual; }
}
