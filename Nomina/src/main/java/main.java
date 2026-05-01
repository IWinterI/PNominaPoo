import java.util.*;

public class main {

    private static List<Empleado> empleados = new ArrayList<>();
    private static int siguienteID = 1;
    private static RegistroNomina registroNominas = new RegistroNomina();

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
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
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
                case 0: System.out.println("Saliendo del sistema..."); break;
                default: System.out.println("Opcion invalida. Intente de nuevo.");
            }
        } while (opcion != 0);
        sc.close();
    }

    // ---------- Métodos auxiliares ----------
    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Error: Debe ingresar un numero entero. Intente de nuevo: ");
            }
        }
    }

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

    private static int generarID() {
        return siguienteID++;
    }

    // ---------- Agregar empleados ----------
    private static void agregarAsalariado(Scanner sc) {
        String nombre = leerTextoNoVacio(sc, "Nombre: ");
        String puesto = leerTextoNoVacio(sc, "Puesto: ");
        double salario = leerDoublePositivo(sc, "Salario mensual: ");
        int id = generarID();
        Empleado e = new EmpleadoAsalariado(id, nombre, puesto, salario);
        empleados.add(e);
        System.out.println("Empleado asalariado agregado con ID " + id);
    }

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

    private static Empleado buscarEmpleadoPorID(int id) {
        for (Empleado e : empleados) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    // ---------- Listar y ver nominas ----------
    private static void listarNominas() {
        registroNominas.Listar_Nomina();
    }

    private static void verDetalleNomina(Scanner sc) {
        System.out.print("Ingrese el indice de la nomina a detallar: ");
        int idx = leerEntero(sc);
        registroNominas.Ver_Detalles(idx);
    }

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
}