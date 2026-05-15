import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RegistroNomina {

    private final List<Nomina> registro = new CopyOnWriteArrayList<>();

    public void Cargar_Nomina(Nomina nomina) {
        if (nomina != null) {
            registro.add(nomina);
        }
    }

    public void Listar_Nomina() {
    }

    public void Ver_Detalles(int i) {
        if (i < 0 || i >= registro.size()) {
            return;
        }
    }

    public Boolean Eliminar_nomina(int i) {
        if (i < 0 || i >= registro.size()) {
            return false;
        }
        registro.remove(i);
        return true;
    }

    public void reemplazarNominas(List<Nomina> nuevas) {
        registro.clear();
        registro.addAll(nuevas);
    }
    
    public List<Nomina> getNominas() { return new ArrayList<>(registro); }
}
