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

            opcion = leerEntero(sc);
            if (opcion < 0 || opcion > 9) {
                System.out.println("Opción inválida. Intente de nuevo.");
                continue;
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
            }
        } while (opcion != 0);
        sc.close();
    }

    // ---------- Métodos auxiliares de validación ----------

    /** Lee un entero positivo o 0, continuamente hasta que sea válido. */
    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Error: Debe ingresar un número entero. Intente de nuevo: ");
            }
        }
    }

    /** Lee un número decimal positivo (>0), repitiendo hasta que sea válido. */
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
                System.out.println("Error: Entrada no numérica.");
            }
        }
    }

    /** Lee un texto no vacío que no esté formado solo por dígitos. */
    private static String leerTextoNoVacio(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Este campo no puede estar vacío.");
                continue;
            }
            if (input.matches("\\d+")) {   // solo dígitos
                System.out.println("Error: El valor no puede ser solo números.");
                continue;
            }
            return input;
        }
    }
    /**
     * Para edición: muestra el valor actual entre corchetes.
     * Si el usuario solo presiona Enter, se mantiene el valorActual.
     * Si ingresa algo, valida que sea un número positivo (>0).
     */
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
                System.out.println("Error: Entrada no numérica.");
            }
        }
    }

    /**
    * Versión opcional para textos: si se ingresa Enter se mantiene el actual.
    * Si se ingresa algo nuevo, no puede estar vacío ni ser solo números.
    */
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

    /**
     * Lee un ID de empleado existente; si no existe, vuelve a pedir.
     * Devuelve el ID válido.
     */
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

    // ---------- Funcionalidad del sistema ----------

    private static int generarID() {
        return siguienteID++;
    }

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
        double horas = leerDoublePositivo(sc, "Horas trabajadas: ");
        double tarifa = leerDoublePositivo(sc, "Tarifa por hora: ");
        int id = generarID();
        Empleado e = new EmpleadoPorHoras(id, nombre, puesto, horas, tarifa);
        empleados.add(e);
        System.out.println("Empleado por horas agregado con ID " + id);
    }

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
                System.out.println("   Horas: " + eph.getHorasTrabajadas() + " | Tarifa: " + eph.getTarifaHora());
            }
        }

        int id = leerIDExistente(sc, "Ingrese ID del empleado a editar: ");
        Empleado emp = buscarEmpleadoPorID(id); // nunca null por la validación

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
            double nuevasHoras = leerDoublePositivoOpcional(sc, "Nuevas horas trabajadas", eph.getHorasTrabajadas());
            eph.setHorasTrabajadas(nuevasHoras);
            double nuevaTarifa = leerDoublePositivoOpcional(sc, "Nueva tarifa por hora", eph.getTarifaHora());
            eph.setTarifaHora(nuevaTarifa);
        }
        System.out.println("Empleado actualizado.");
    }

    private static void eliminarEmpleado(Scanner sc) {
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
        int id = leerIDExistente(sc, "Ingrese ID del empleado a eliminar: ");
        empleados.remove(buscarEmpleadoPorID(id));
        System.out.println("Empleado con ID " + id + " eliminado.");
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
        int idx = leerEntero(sc);
        registroNominas.Ver_Detalles(idx);
    }

    private static void eliminarNomina(Scanner sc) {
        System.out.print("Ingrese el índice de la nómina a eliminar: ");
        int idx = leerEntero(sc);
        boolean ok = registroNominas.Eliminar_nomina(idx);
        if (ok) {
            System.out.println("Nómina eliminada.");
        } else {
            System.out.println("No se pudo eliminar (índice fuera de rango).");
        }
    }
}