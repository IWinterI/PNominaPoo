import java.util.*;

public class ProcesarNomina {
    private List<Empleado> Empleados;
    private List<Nomina> Resultado = new ArrayList<>();
    private RegistroNomina Almacenamiento;

    public ProcesarNomina(List<Empleado> Empleados, RegistroNomina Almacenamiento) {
        this.Empleados = Empleados;
        this.Almacenamiento = Almacenamiento;
    }

    public void Procesar(){
        for (Empleado empleado : Empleados) {
            double sueldo = empleado.calcularSueldo();
            Nomina nomina = new Nomina(new Date(), empleado, sueldo);
            Resultado.add(nomina);
            Almacenamiento.Cargar_Nomina(nomina);
        }
    }

    public List<Nomina> getResultados() {
        return Resultado;
    }
}
