import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 Clase contenedora de nóminas que almacena y gestiona objetos Nomina.
 Se ha hecho segura para uso concurrente utilizando una lista concurrente (CopyOnWriteArrayList) que permite lecturas e iteraciones sin bloqueos.
 Tipo de clase: contenedor / colección segura para hilos.
 */
public class RegistroNomina {

    // Lista segura para entornos concurrentes.
    private final List<Nomina> registro = new CopyOnWriteArrayList<>();

    /*
    Agrega una nómina al registro. Puede ser llamada desde múltiples hilos sin problemas gracias a CopyOnWriteArrayList.
    nomina Nómina a agregar (no nula).
     */
    public void Cargar_Nomina(Nomina nomina) {
        if (nomina != null) {
            registro.add(nomina);
        }
    }

    /*
    Muestra todas las nóminas registradas en orden.
    La iteración sobre CopyOnWriteArrayList es segura aunque ocurran modificaciones concurrentes.
     */
    public void Listar_Nomina() {
        if (registro.isEmpty()) {
            System.out.println("El registro está vacío");
            return;
        }
        int i = 0;
        for (Nomina n : registro) {
            System.out.println("[" + i + "] Fecha: " + n.get_Fecha() +
                                " - Empleado: " + n.get_Empleado().getNombre() +
                                " - Total: " + n.get_Total());
            i++;
        }
    }

    /*
    Muestra el detalle completo de la nómina situada en el indice dado.
    i indice dentro del registro.
     */
    public void Ver_Detalles(int i) {
        if (i < 0 || i >= registro.size()) {
            System.out.println("Índice fuera de rango");
            return;
        }
        System.out.println(registro.get(i));
    }

    /*
    Elimina la nómina del índice especificado.
    i Índice a eliminar.
    Retorna true si se eliminó correctamente; false si el índice es inválido.
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