import java.util.*;

public class main {
    
    private static List<Empleado> empleados = new ArrayList<>();
    private static int siguienteID = 1;
    private static RegistroNomina registroNominas = new RegistroNomina();

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n===== SISTEMA DE NÓMINA EMPRESARIAL =====");
            System.out.println("1. Agregar empleado asalariado");
            System.out.println("2. Agregar empleado por horas");
            System.out.println("3. Editar empleado");
            System.out.println("4. Eliminar empleado");
            System.out.println("5. Listar empleados");
            System.out.println("6. Procesar nómina (calcular sueldos y registrar)");
            System.out.println("7. Listar nóminas registradas");
            System.out.println("8. Ver detalle de nómina");
            System.out.println("9. Eliminar nómina");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
            switch (opcion) {
                case 1: agregarAsalariado(sc); break;
                case 2: agregarPorHoras(sc); break;
                case 3: editarEmpleado(sc); break;
                case 4: eliminarEmpleado(sc); break;
                case 5: listarEmpleados(); break;
                case 6: procesarNomina(); break;
                case 7: listarNominas(); break;
                case 8: verDetalleNomina(sc); break;
                case 9: eliminarNomina(sc); break;
                case 0: System.out.println("Saliendo del sistema..."); break;
                default: System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);
        sc.close();
    }

    private static int generarID() {
        return siguienteID++;
    }

    private static void agregarAsalariado(Scanner sc) {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Puesto: ");
        String puesto = sc.nextLine();
        System.out.print("Salario mensual: ");
        double salario = Double.parseDouble(sc.nextLine());
        int id = generarID();
        Empleado e = new EmpleadoAsalariado(id, nombre, puesto, salario);
        empleados.add(e);
        System.out.println("Empleado asalariado agregado con ID " + id);
    }

    private static void agregarPorHoras(Scanner sc) {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Puesto: ");
        String puesto = sc.nextLine();
        System.out.print("Horas trabajadas: ");
        double horas = Double.parseDouble(sc.nextLine());
        System.out.print("Tarifa por hora: ");
        double tarifa = Double.parseDouble(sc.nextLine());
        int id = generarID();
        Empleado e = new EmpleadoPorHoras(id, nombre, puesto, horas, tarifa);
        empleados.add(e);
        System.out.println("Empleado por horas agregado con ID " + id);
    }

    private static void editarEmpleado(Scanner sc) {
        System.out.print("Ingrese ID del empleado a editar: ");
        int id = Integer.parseInt(sc.nextLine());
        Empleado emp = buscarEmpleadoPorID(id);
        if (emp == null) {
            System.out.println("No existe empleado con ID " + id);
            return;
        }
        System.out.print("Nuevo nombre (Enter para mantener [" + emp.getNombre() + "]): ");
        String nombre = sc.nextLine();
        if (!nombre.isEmpty()) {
            emp.setNombre(nombre);   // Requiere agregar setter en clase abstracta Empleado
        }
        System.out.print("Nuevo puesto (Enter para mantener [" + emp.getPuesto() + "]): ");
        String puesto = sc.nextLine();
        if (!puesto.isEmpty()) {
            emp.setPuesto(puesto);
        }

        if (emp instanceof EmpleadoAsalariado) {
            EmpleadoAsalariado ea = (EmpleadoAsalariado) emp;
            System.out.print("Nuevo salario mensual (Enter para mantener [" + ea.getSalarioMensual() + "]): ");
            String salarioStr = sc.nextLine();
            if (!salarioStr.isEmpty()) {
                ea.setSalarioMensual(Double.parseDouble(salarioStr));
            }
        } else if (emp instanceof EmpleadoPorHoras) {
            EmpleadoPorHoras eph = (EmpleadoPorHoras) emp;
            System.out.print("Nuevas horas trabajadas (Enter para mantener [" + eph.getHorasTrabajadas() + "]): ");
            String horasStr = sc.nextLine();
            if (!horasStr.isEmpty()) {
                eph.setHorasTrabajadas(Double.parseDouble(horasStr));
            }
            System.out.print("Nueva tarifa por hora (Enter para mantener [" + eph.getTarifaHora() + "]): ");
            String tarifaStr = sc.nextLine();
            if (!tarifaStr.isEmpty()) {
                eph.setTarifaHora(Double.parseDouble(tarifaStr));
            }
        }
        System.out.println("Empleado actualizado.");
    }

    private static void eliminarEmpleado(Scanner sc) {
        System.out.print("Ingrese ID del empleado a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());
        Empleado emp = buscarEmpleadoPorID(id);
        if (emp != null) {
            empleados.remove(emp);
            System.out.println("Empleado con ID " + id + " eliminado.");
        } else {
            System.out.println("No se encontró empleado con ese ID.");
        }
    }

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
                System.out.println("   Horas: " + eph.getHorasTrabajadas() + " | Tarifa: " + eph.getTarifaHora());
            }
        }
    }

    private static Empleado buscarEmpleadoPorID(int id) {
        for (Empleado e : empleados) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    private static void procesarNomina() {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados para procesar. Agregue empleados primero.");
            return;
        }
        ProcesarNomina procesador = new ProcesarNomina(empleados, registroNominas);
        procesador.Procesar();
        System.out.println("Nómina procesada. Se generaron " + procesador.getResultados().size() + " registros.");
    }

    private static void listarNominas() {
        registroNominas.Listar_Nomina();
    }

    private static void verDetalleNomina(Scanner sc) {
        System.out.print("Ingrese el índice de la nómina a detallar: ");
        int idx = Integer.parseInt(sc.nextLine());
        registroNominas.Ver_Detalles(idx);
    }

    private static void eliminarNomina(Scanner sc) {
        System.out.print("Ingrese el índice de la nómina a eliminar: ");
        int idx = Integer.parseInt(sc.nextLine());
        boolean ok = registroNominas.Eliminar_nomina(idx);
        if (ok) {
            System.out.println("Nómina eliminada.");
        } else {
            System.out.println("No se pudo eliminar (índice fuera de rango).");
        }
    }
}