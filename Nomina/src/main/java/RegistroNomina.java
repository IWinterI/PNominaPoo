import java.util.*;

/*
 Clase encargada de almacenar y gestionar una coleccion de objetos Nomina.
 Permite agregar una nueva nomina, listar todas, ver detalle y eliminar por indice.
 Tipo de clase: clase contenedora / de gestión.
 */
public class RegistroNomina {

    private List<Nomina> registro;

    public RegistroNomina() {
        this.registro = new ArrayList<>();
    }

    /*
     Agrega una nomina al registro si no es nula.
     nomina Nomina a agregar.
     */
    public void Cargar_Nomina(Nomina nomina) {
        if (nomina != null) {
            registro.add(nomina);
        }
    }

    /*
     Muestra todas las nominas registradas con formato resumido.
     */
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

    /*
     Muestra el detalle completo de la nomina en la posición dada.
      i indice en el registro.
     */
    public void Ver_Detalles(int i) {
        if (i < 0 || i >= registro.size()) {
            System.out.println("Índice fuera de rango");
            return;
        }
        // Llama al toString() correspondiente (polimórfico)
        System.out.println(registro.get(i));
    }

    /*
     Elimina la nomina del indice especificado.
      i indice a eliminar.
     Retorna true si se elimino correctamente, false si el índice no es valido.
     */
    public Boolean Eliminar_nomina(int i) {
        if (i < 0 || i >= registro.size()) {
            System.out.println("Índice fuera de rango");
            return false;
        }
        registro.remove(i);
        return true;
    }
}