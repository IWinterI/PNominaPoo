import java.util.*;

public class RegistroNomina {

    private List<Nomina> registro;

    public RegistroNomina() {
        this.registro = new ArrayList<>();
    }

    public void Cargar_Nomina(Nomina nomina) {
        if (nomina != null) {
            registro.add(nomina);
        }
    }

    public void Listar_Nomina() {
        if (registro.isEmpty()) {
            System.out.println("El registro está vacío");
            return;
        }
        for (int i = 0; i < registro.size(); i++) {
            Nomina n = registro.get(i);
            System.out.println("[" + i + "] Fecha: " + n.get_Fecha() +
                                " - Empleado: " + n.get_Empleado().getNombre() +
                                " - Total: " + n.get_Total());
        }
    }

    public void Ver_Detalles(int i) {
        if (i < 0 || i >= registro.size()) {
            System.out.println("Índice fuera de rango");
            return;
        }
        // Simplemente imprime la nómina, que usará el toString() correspondiente
        System.out.println(registro.get(i));
    }

    public Boolean Eliminar_nomina(int i) {
        if (i < 0 || i >= registro.size()) {
            System.out.println("Índice fuera de rango");
            return false;
        }
        registro.remove(i);
        return true;
    }
}