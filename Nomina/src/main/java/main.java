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

public class main {

    private static List<Empleado> empleados = new ArrayList<>();
    private static int siguienteID = 1;
    private static RegistroNomina registroNominas = new RegistroNomina();
    private static final String ARCHIVO_DATOS = "datos_sistema.ser";

    private static int generarID() {
        return siguienteID++;
    }

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
        }
    }

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

    public static RegistroNomina getRegistroNominas() {
        return registroNominas;
    }

    public static void procesarNominasGUI(Component parent) {
        if (empleados.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No hay empleados registrados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Empleado emp : empleados) {
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
            } else {
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
