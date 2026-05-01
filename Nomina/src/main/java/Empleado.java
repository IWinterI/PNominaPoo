/**
 Clase padre adstracta con la implementacion de la interfaz IPagable
 */
public abstract class Empleado {
    private int ID;
    private String Nombre;
    private String Puesto;

    public Empleado(int ID, String Nombre, String Puesto) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Puesto = Puesto;
    }
    /*Getters genericos de los atributos*/
    public int getId() { return ID; }
    public String getNombre() { return Nombre; }
    public String getPuesto() { return Puesto; }
    public void setNombre(String nombre) { this.Nombre = nombre; }
    public void setPuesto(String puesto) { this.Puesto = puesto; }
}