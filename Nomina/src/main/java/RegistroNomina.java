
import java.util.*;

public class RegistroNomina {

    private List<Nomina> Registro;
    
    public RegistroNomina() {
        this.Registro = new ArrayList<>();
    }

    public void Cargar_Nomina(Nomina nomina) {
        if (nomina != null) {
            Registro.add(nomina);
        } 
    }

    public void Listar_Nomina() {
        if (Registro.isEmpty()) {
            System.out.println("El registro esta vacio");
        }

        for (int i = 0; i < Registro.size(); i++){
            Nomina n = Registro.get(i);
                System.out.println("[" + i + "] Empleado: " + n.get_Fecha() + " - " +n.get_Empleado().getNombre());
        }
    }

    public void Ver_Detalles(int i) {
        if (i < 0 || i >= Registro.size()){
            System.out.println("Indice fuera de rango");
            return;
        }

        Nomina n = Registro.get(i);

        System.out.println("----- Detalles de la Nomina -----");
        System.out.println("Empleado: " + n.get_Empleado().getNombre());
        System.out.println("ID: " + n.get_Empleado().getId());
        System.out.println("Puesto: " + n.get_Empleado().getPuesto());
        System.out.println("Pago: " + n.get_Sueldo());
        System.out.println("Fecha: " + n.get_Fecha());
    }

    public Boolean Eliminar_nomina(int i) {
        if (i < 0 || i >= Registro.size()){
            System.out.println("Indice fuera de rango");
            return false;
        }

        Registro.remove(i);
        return true;
    }
}
