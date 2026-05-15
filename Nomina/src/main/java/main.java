import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;

/*
 * Clase principal del sistema de nómina empresarial con interfaz gráfica (GUI).
 * Gestiona empleados (altas, bajas, ediciones) y nóminas (procesamiento, consulta, eliminación).
 * Tipo de clase: clase principal (punto de entrada para la GUI).
 */

public class main {

    private static List<Empleado> empleados = new ArrayList<>();
    private static int siguienteID = 1;
    private static RegistroNomina registroNominas = new RegistroNomina();
    private static final String ARCHIVO_DATOS = "datos_sistema.ser";

    /* Genera y retorna un nuevo ID secuencial. */
    private static int generarID() {
        return siguienteID++;
    }

    /* Busca un empleado por ID; retorna null si no existe. */
    private static Empleado buscarEmpleadoPorID(int id) {
        for (Empleado e : empleados) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    private static void guardarDatos() {
        List<Nomina> nominas = registroNominas.getNominas();
        Data data = new Data(empleados, nominas);
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_DATOS))) {
            out.writeObject(data);
        } catch (IOException e) {
            // Error silencioso, la GUI maneja la notificación
        }
    }

    private static void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ARCHIVO_DATOS))) {
            Data data = (Data) in.readObject();
            empleados = data.getEmpleados();
            registroNominas.reemplazarNominas(data.getNominas());
            siguienteID = 1;
            for (Empleado e : empleados) {
                if (e.getId() >= siguienteID) {
                    siguienteID = e.getId() + 1;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Error silencioso, la GUI maneja la notificación
        }
    }

    // ========== MÉTODOS PÚBLICOS PARA LA INTERFAZ GRÁFICA ==========
    public static EmpleadoAsalariado agregarEmpleadoAsalariadoGUI(String nombre, String puesto, double salarioMensual) {
        int id = generarID();
        EmpleadoAsalariado emp = new EmpleadoAsalariado(id, nombre, puesto, salarioMensual);
        empleados.add(emp);
        return emp;
    }

    public static EmpleadoPorHoras agregarEmpleadoPorHorasGUI(String nombre, String puesto, double tarifaHora) {
        int id = generarID();
        EmpleadoPorHoras emp = new EmpleadoPorHoras(id, nombre, puesto, tarifaHora);
        empleados.add(emp);
        return emp;
    }

    public static List<Empleado> getEmpleados() {
        return empleados;
    }

    public static boolean editarEmpleadoGUI(int id, String nuevoNombre, String nuevoPuesto,
                                             Double nuevoSalario, Double nuevaTarifa) {
        Empleado emp = buscarEmpleadoPorID(id);
        if (emp == null) return false;
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) emp.setNombre(nuevoNombre.trim());
        if (nuevoPuesto != null && !nuevoPuesto.trim().isEmpty()) emp.setPuesto(nuevoPuesto.trim());
        if (emp instanceof EmpleadoAsalariado && nuevoSalario != null && nuevoSalario > 0)
            ((EmpleadoAsalariado) emp).setSalarioMensual(nuevoSalario);
        else if (emp instanceof EmpleadoPorHoras && nuevaTarifa != null && nuevaTarifa > 0)
            ((EmpleadoPorHoras) emp).setTarifaHora(nuevaTarifa);
        return true;
    }

    public static boolean eliminarEmpleadoGUI(int id) {
        Empleado emp = buscarEmpleadoPorID(id);
        if (emp == null) return false;
        return empleados.remove(emp);
    }

    public static void recalcularSiguienteID() {
        int max = 0;
        for (Empleado e : empleados) if (e.getId() > max) max = e.getId();
        siguienteID = max + 1;
    }

    // --- Métodos para nóminas desde GUI ---
    public static RegistroNomina getRegistroNominas() {
        return registroNominas;
    }

    public static void procesarNominasGUI(Component parent) {
        if (empleados.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay empleados registrados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Empleado emp : empleados) {
            // 1. Tarifa hora extra (positiva)
            double tarifaHoraExtra = 0;
            while (true) {
                String input = JOptionPane.showInputDialog(parent,
                        "Empleado: " + emp.getNombre() + " (ID " + emp.getId() + ")\nIngrese la tarifa por hora extra (valor positivo):",
                        "Procesar nómina", JOptionPane.QUESTION_MESSAGE);
                if (input == null) return;
                try {
                    tarifaHoraExtra = Double.parseDouble(input);
                    if (tarifaHoraExtra <= 0) {
                        JOptionPane.showMessageDialog(parent, "La tarifa debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Entrada inválida. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // 2. Bono (opcional, pero si se aplica debe ser positivo)
            double bono = 0;
            int respBono = JOptionPane.showConfirmDialog(parent, "¿Aplica bono para " + emp.getNombre() + "?", "Bono", JOptionPane.YES_NO_OPTION);
            if (respBono == JOptionPane.YES_OPTION) {
                while (true) {
                    String input = JOptionPane.showInputDialog(parent, "Monto del bono (positivo):");
                    if (input == null) return;
                    try {
                        bono = Double.parseDouble(input);
                        if (bono <= 0) {
                            JOptionPane.showMessageDialog(parent, "El bono debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(parent, "Entrada inválida. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            // 3. Horas extra (opcional, entero positivo)
            int horasExtra = 0;
            int respHE = JOptionPane.showConfirmDialog(parent, "¿Aplica horas extra para " + emp.getNombre() + "?", "Horas extra", JOptionPane.YES_NO_OPTION);
            if (respHE == JOptionPane.YES_OPTION) {
                while (true) {
                    String input = JOptionPane.showInputDialog(parent, "Cantidad de horas extra (entero positivo):");
                    if (input == null) return;
                    try {
                        horasExtra = Integer.parseInt(input);
                        if (horasExtra <= 0) {
                            JOptionPane.showMessageDialog(parent, "Las horas extra deben ser un número entero positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(parent, "Entrada inválida. Debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            // 4. Crear nómina según tipo
            Nomina nomina;
            if (emp instanceof EmpleadoAsalariado) {
                nomina = new NominaAsalariado(new Date(), emp, tarifaHoraExtra);
                if (bono > 0 && horasExtra > 0)
                    nomina.calcularSueldo(bono, horasExtra);
                else if (bono > 0)
                    nomina.calcularSueldo(bono);
                else if (horasExtra > 0)
                    nomina.calcularSueldo(horasExtra);
                else
                    nomina.calcularSueldo();
            } else { // EmpleadoPorHoras
                double horasTrabajadas = 0;
                while (true) {
                    String input = JOptionPane.showInputDialog(parent,
                            "Empleado por horas: " + emp.getNombre() + "\nHoras trabajadas en el periodo (positivo):");
                    if (input == null) return;
                    try {
                        horasTrabajadas = Double.parseDouble(input);
                        if (horasTrabajadas <= 0) {
                            JOptionPane.showMessageDialog(parent, "Las horas trabajadas deben ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                            continue;
                        }
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(parent, "Entrada inválida. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                nomina = new NominaPorHoras(new Date(), emp, tarifaHoraExtra, horasTrabajadas);
                if (bono > 0 && horasExtra > 0)
                    nomina.calcularSueldo(bono, horasExtra);
                else if (bono > 0)
                    nomina.calcularSueldo(bono);
                else if (horasExtra > 0)
                    nomina.calcularSueldo(horasExtra);
                else
                    nomina.calcularSueldo();
            }

            registroNominas.Cargar_Nomina(nomina);
        }

        JOptionPane.showMessageDialog(parent, "Nóminas procesadas correctamente.\nTotal de nóminas registradas: " + registroNominas.getNominas().size(),
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean eliminarNominaGUI(int index) {
        return registroNominas.Eliminar_nomina(index);
    }

    public static String getNominaDetails(int index) {
        List<Nomina> nominas = registroNominas.getNominas();
        if (index < 0 || index >= nominas.size()) return null;
        Nomina n = nominas.get(index);
        return n.toString();
    }

    public static void guardarDatosGUI() {
        guardarDatos();
    }

    public static void cargarDatosGUI() {
        cargarDatos();
    }
}
