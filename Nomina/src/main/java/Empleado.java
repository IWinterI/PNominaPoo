/*
 Clase abstracta que representa a un empleado generico de la empresa.
 Sirve como clase base para los distintos tipos de empleado (asalariados, por horas).
 Tipo de clase: clase abstracta (padre de la jerarquía de empleados).
 */
public abstract class Empleado {
    private int ID;
    private String Nombre;
    private String Puesto;

    /*
     Constructor que inicializa los datos basicos de un empleado.
     ID      Identificador unico del empleado.
     Nombre  Nombre completo del empleado.
     Puesto  Cargo o puesto que ocupa en la empresa.
     */
    public Empleado(int ID, String Nombre, String Puesto) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Puesto = Puesto;
    }
    /* Identificador del empleado. */
    public int getId() { return ID; }

    /* Nombre del empleado. */
    public String getNombre() { return Nombre; }

    /* Puesto del empleado. */
    public String getPuesto() { return Puesto; }

    /* Establece un nuevo nombre para el empleado. */
    public void setNombre(String nombre) { this.Nombre = nombre; }

    /* Establece un nuevo puesto para el empleado. */
    public void setPuesto(String puesto) { this.Puesto = puesto; }
}