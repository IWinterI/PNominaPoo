import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import javax.swing.*;
 
/*
 Clase principal que implementa el sistema de nomina empresarial mediante un menu interactivo.
 Gestiona empleados (altas, bajas, ediciones) y nominas (procesamiento, consulta, eliminacion).
 Tipo de clase: clase principal (contiene el metodo main).
 */

public class main {

    private static List<Empleado> empleados = new ArrayList<>();
    private static int siguienteID = 1;
    private static RegistroNomina registroNominas = new RegistroNomina();
    private static final String ARCHIVO_DATOS = "datos_sistema.ser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n===== SISTEMA DE NOMINA EMPRESARIAL =====");
            System.out.println("1. Agregar empleado asalariado");
            System.out.println("2. Agregar empleado por horas");
            System.out.println("3. Editar empleado");
            System.out.println("4. Eliminar empleado");
            System.out.println("5. Listar empleados");
            System.out.println("6. Procesar nomina (calcular sueldos y registrar)");
            System.out.println("7. Listar nominas registradas");
            System.out.println("8. Ver detalle de nomina");
            System.out.println("9. Eliminar nomina");
            System.out.println("10. Guardar datos");
            System.out.println("11. Cargar datos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero(sc);

            switch (opcion) {
                case 1: agregarAsalariado(sc); break;
                case 2: agregarPorHoras(sc); break;
                case 3: editarEmpleado(sc); break;
                case 4: eliminarEmpleado(sc); break;
                case 5: listarEmpleados(); break;
                case 6: ProcesarNomina.procesar(sc, empleados, registroNominas); break;
                case 7: listarNominas(); break;
                case 8: verDetalleNomina(sc); break;
                case 9: eliminarNomina(sc); break;
                case 10: guardarDatos(); break;
                case 11: cargarDatos(); break;
                case 0: System.out.println("Saliendo del sistema..."); break;
                default: System.out.println("Opcion invalida. Intente de nuevo.");
            }
        } while (opcion != 0);
        sc.close();
    }

    // ---------- Métodos auxiliares ----------
    /* Lee un entero desde el scanner, validando la entrada. */
    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Error: Debe ingresar un numero entero. Intente de nuevo: ");
            }
        }
    }

    /* Lee un double positivo mostrando un mensaje. */
    private static double leerDoublePositivo(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(sc.nextLine());
                if (valor <= 0) {
                    System.out.println("Error: El valor debe ser positivo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: Entrada no numerica.");
            }
        }
    }

    /* Lee un texto no vacio y que no sea solo numeros. */
    private static String leerTextoNoVacio(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Este campo no puede estar vacio.");
                continue;
            }
            if (input.matches("\\d+")) {  
                System.out.println("Error: El valor no puede ser solo numeros.");
                continue;
            }
            return input;
        }
    }

    /* Lee un double positivo con opcion de conservar el valor actual. */
    private static double leerDoublePositivoOpcional(Scanner sc, String mensaje, double valorActual) {
        while (true) {
            System.out.print(mensaje + " [" + valorActual + "]: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return valorActual;
            }
            try {
                double valor = Double.parseDouble(input);
                if (valor <= 0) {
                    System.out.println("Error: El valor debe ser positivo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: Entrada no numerica.");
            }
        }
    }

    /* Lee un texto con opcion de conservar el valor actual. */
    private static String leerTextoOpcional(Scanner sc, String mensaje, String valorActual) {
        while (true) {
            System.out.print(mensaje + " [" + valorActual + "]: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return valorActual;
            }
            if (input.matches("\\d+")) {
                System.out.println("Error: El valor no puede ser solo números.");
                continue;
            }
            return input;
        }
    }

    /* Solicita un ID hasta que exista un empleado con ese ID. */
    private static int leerIDExistente(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            int id = leerEntero(sc);
            if (buscarEmpleadoPorID(id) == null) {
                System.out.println("No existe un empleado con ID " + id + ". Intente de nuevo.");
                continue;
            }
            return id;
        }
    }

    /* Genera y retorna un nuevo ID secuencial. */
    private static int generarID() {
        return siguienteID++;
    }

    // ---------- Agregar empleados ----------
    
    /* Agrega un nuevo empleado asalariado. */
    private static void agregarAsalariado(Scanner sc) {
        String nombre = leerTextoNoVacio(sc, "Nombre: ");
        String puesto = leerTextoNoVacio(sc, "Puesto: ");
        double salario = leerDoublePositivo(sc, "Salario mensual: ");
        int id = generarID();
        Empleado e = new EmpleadoAsalariado(id, nombre, puesto, salario);
        empleados.add(e);
        System.out.println("Empleado asalariado agregado con ID " + id);
    }

     /* Agrega un nuevo empleado por horas. */
    private static void agregarPorHoras(Scanner sc) {
        String nombre = leerTextoNoVacio(sc, "Nombre: ");
        String puesto = leerTextoNoVacio(sc, "Puesto: ");
        double tarifa = leerDoublePositivo(sc, "Tarifa por hora: ");
        int id = generarID();
        Empleado e = new EmpleadoPorHoras(id, nombre, puesto, tarifa);
        empleados.add(e);
        System.out.println("Empleado por horas agregado con ID " + id + " (tarifa: " + tarifa + ")");
    }

    // ---------- Editar empleado ----------
    
    /* Permite editar los datos de un empleado existente. */
    private static void editarEmpleado(Scanner sc) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        System.out.println("--- Lista de empleados ---");
        for (Empleado e : empleados) {
            String tipo = (e instanceof EmpleadoAsalariado) ? "Asalariado" : "Por Horas";
            System.out.println("ID: " + e.getId() + " | " + e.getNombre() + " | " + e.getPuesto() + " | Tipo: " + tipo);
            if (e instanceof EmpleadoAsalariado) {
                System.out.println("   Salario Mensual: " + ((EmpleadoAsalariado) e).getSalarioMensual());
            } else {
                EmpleadoPorHoras eph = (EmpleadoPorHoras) e;
                System.out.println("   Tarifa por hora: " + eph.getTarifaHora());
            }
        }

        int id = leerIDExistente(sc, "Ingrese ID del empleado a editar: ");
        Empleado emp = buscarEmpleadoPorID(id); 

        String nombre = leerTextoOpcional(sc, "Nuevo nombre", emp.getNombre());
        emp.setNombre(nombre);
        String puesto = leerTextoOpcional(sc, "Nuevo puesto", emp.getPuesto());
        emp.setPuesto(puesto);

        if (emp instanceof EmpleadoAsalariado) {
            EmpleadoAsalariado ea = (EmpleadoAsalariado) emp;
            double nuevoSalario = leerDoublePositivoOpcional(sc, "Nuevo salario mensual", ea.getSalarioMensual());
            ea.setSalarioMensual(nuevoSalario);
        } else if (emp instanceof EmpleadoPorHoras) {
            EmpleadoPorHoras eph = (EmpleadoPorHoras) emp;
            double nuevaTarifa = leerDoublePositivoOpcional(sc, "Nueva tarifa por hora", eph.getTarifaHora());
            eph.setTarifaHora(nuevaTarifa);
        }
        System.out.println("Empleado actualizado.");
    }

    // ---------- Eliminar empleado ----------
    
    /* Elimina un empleado tras confirmar su ID. */
    private static void eliminarEmpleado(Scanner sc) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        System.out.println("--- Lista de empleados ---");
        for (Empleado e : empleados) {
            String tipo = (e instanceof EmpleadoAsalariado) ? "Asalariado" : "Por Horas";
            System.out.println("ID: " + e.getId() + " | " + e.getNombre() + " | " + e.getPuesto() + " | Tipo: " + tipo);
        }
        int id = leerIDExistente(sc, "Ingrese ID del empleado a eliminar: ");
        empleados.remove(buscarEmpleadoPorID(id));
        System.out.println("Empleado con ID " + id + " eliminado.");
    }

    // ---------- Listar empleados ----------
    
    /* Muestra la lista completa de empleados con sus detalles. */
    private static void listarEmpleados() {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        System.out.println("--- Lista de empleados ---");
        for (Empleado e : empleados) {
            String tipo = (e instanceof EmpleadoAsalariado) ? "Asalariado" : "Por Horas";
            System.out.println("ID: " + e.getId() + " | " + e.getNombre() + " | " + e.getPuesto() + " | Tipo: " + tipo);
            if (e instanceof EmpleadoAsalariado) {
                System.out.println("   Salario Mensual: " + ((EmpleadoAsalariado) e).getSalarioMensual());
            } else {
                EmpleadoPorHoras eph = (EmpleadoPorHoras) e;
                System.out.println("   Tarifa por hora: " + eph.getTarifaHora());
            }
        }
    }

    /* Busca un empleado por ID; retorna null si no existe. */
    private static Empleado buscarEmpleadoPorID(int id) {
        for (Empleado e : empleados) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    // ---------- Listar y ver nominas ----------
    
    /* Lista todas las nominas almacenadas. */
    private static void listarNominas() {
        registroNominas.Listar_Nomina();
    }

    /* Muestra el detalle de una nomina segun un indice. */
    private static void verDetalleNomina(Scanner sc) {
        System.out.print("Ingrese el indice de la nomina a detallar: ");
        int idx = leerEntero(sc);
        registroNominas.Ver_Detalles(idx);
    }

    /* Elimina una nomina por indice. */
    private static void eliminarNomina(Scanner sc) {
        System.out.print("Ingrese el indice de la nomina a eliminar: ");
        int idx = leerEntero(sc);
        boolean ok = registroNominas.Eliminar_nomina(idx);
        if (ok) {
            System.out.println("Nomina eliminada.");
        } else {
            System.out.println("No se pudo eliminar (indice fuera de rango).");
        }
    }

    // 

    private static void guardarDatos() {
        // Recogemos las listas actuales
        List<Nomina> nominas = registroNominas.getNominas();
        Data data = new Data(empleados, nominas);
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_DATOS))) {
            out.writeObject(data);
            System.out.println("Datos guardados correctamente en " + ARCHIVO_DATOS);
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    private static void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            System.out.println("No se encontró el archivo " + ARCHIVO_DATOS);
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ARCHIVO_DATOS))) {
            Data data = (Data) in.readObject();
            empleados = data.getEmpleados();
            // Reemplazar la lista interna del registro (requiere un metodo setNominas o limpiar y agregar)
            registroNominas.reemplazarNominas(data.getNominas());
            // Recalcular el siguienteID
            siguienteID = 1;
            for (Empleado e : empleados) {
                if (e.getId() >= siguienteID) {
                    siguienteID = e.getId() + 1;
                }
            }
            System.out.println("Datos cargados correctamente. Empleados: " + empleados.size() +
                                ", Nóminas: " + registroNominas.getNominas().size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
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
            // 1. Tarifa hora extra
            double tarifaHoraExtra;
            while (true) {
                String input = JOptionPane.showInputDialog(parent,
                        "Empleado: " + emp.getNombre() + " (ID " + emp.getId() + ")\nIngrese la tarifa por hora extra:",
                        "Procesar nómina", JOptionPane.QUESTION_MESSAGE);
                if (input == null) return; // cancelar todo el proceso
                try {
                    tarifaHoraExtra = Double.parseDouble(input);
                    if (tarifaHoraExtra <= 0) throw new NumberFormatException();
                    break;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(parent, "Valor inválido. Debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            // 2. Bono
            double bono = 0;
            int respBono = JOptionPane.showConfirmDialog(parent, "¿Aplica bono para " + emp.getNombre() + "?", "Bono", JOptionPane.YES_NO_OPTION);
            if (respBono == JOptionPane.YES_OPTION) {
                while (true) {
                    String input = JOptionPane.showInputDialog(parent, "Monto del bono:");
                    if (input == null) return;
                    try {
                        bono = Double.parseDouble(input);
                        if (bono <= 0) throw new NumberFormatException();
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(parent, "Monto inválido. Debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            // 3. Horas extra
            int horasExtra = 0;
            int respHE = JOptionPane.showConfirmDialog(parent, "¿Aplica horas extra para " + emp.getNombre() + "?", "Horas extra", JOptionPane.YES_NO_OPTION);
            if (respHE == JOptionPane.YES_OPTION) {
                while (true) {
                    String input = JOptionPane.showInputDialog(parent, "Cantidad de horas extra:");
                    if (input == null) return;
                    try {
                        horasExtra = Integer.parseInt(input);
                        if (horasExtra <= 0) throw new NumberFormatException();
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(parent, "Cantidad inválida. Debe ser entero positivo.", "Error", JOptionPane.ERROR_MESSAGE);
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
                double horasTrabajadas;
                while (true) {
                    String input = JOptionPane.showInputDialog(parent,
                            "Empleado por horas: " + emp.getNombre() + "\nHoras trabajadas en el periodo:");
                    if (input == null) return;
                    try {
                        horasTrabajadas = Double.parseDouble(input);
                        if (horasTrabajadas <= 0) throw new NumberFormatException();
                        break;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(parent, "Horas trabajadas inválidas. Debe ser número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
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
        return n.toString(); // Usamos el toString ya implementado en las subclases
    }
    
    public static void guardarDatosGUI() {
        guardarDatos();
    }

    public static void cargarDatosGUI() {
        cargarDatos();
    }
}