import java.io.Serializable;

public abstract class Empleado implements Serializable{
    private static final long serialVersionUID = 1L;
    private int ID;
    private String Nombre;
    private String Puesto;

    public Empleado(int ID, String Nombre, String Puesto) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Puesto = Puesto;
    }

    public int getId() { return ID; }

    public String getNombre() { return Nombre; }

    public String getPuesto() { return Puesto; }

    public void setNombre(String nombre) { this.Nombre = nombre; }

    public void setPuesto(String puesto) { this.Puesto = puesto; }
}
