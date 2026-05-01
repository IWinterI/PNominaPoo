import java.util.*;

/*
 Clase de utilidad que contiene la lógica para procesar la nomina de todos los empleados.
 Recorre la lista de empleados, solicita los datos necesarios (tarifas de hora extra, bonos, horas trabajadas)
 y crea la nómina correspondiente (NominaAsalariado o NominaPorHoras), la calcula y la registra.
 Tipo de clase: clase de utilidad (métodos estáticos, no se instancia).
 */
public class ProcesarNomina {

    /*
     Procesa la nomina para cada empleado de la lista.
     sc         Scanner para entrada de usuario.
     empleados  Lista de empleados de la empresa.
     registro   Registro de nominas donde se almacenarán los resultados.
     */
    public static void procesar(Scanner sc, List<Empleado> empleados, RegistroNomina registro) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados para procesar.");
            return;
        }

        System.out.println("\n--- Procesar nomina ---");
        for (Empleado empleado : empleados) {
            System.out.println("\nEmpleado: " + empleado.getNombre() + " (ID: " + empleado.getId() + ")");

            if (empleado instanceof EmpleadoAsalariado) {
                procesarAsalariado(sc, (EmpleadoAsalariado) empleado, registro);
            } else if (empleado instanceof EmpleadoPorHoras) {
                procesarPorHoras(sc, (EmpleadoPorHoras) empleado, registro);
            }
        }
        System.out.println("\nNomina procesada exitosamente.");
    }

    /*
     Procesa la nomina de un empleado asalariado.
     */
    private static void procesarAsalariado(Scanner sc, EmpleadoAsalariado emp, RegistroNomina registro) {
        double tarifaHoraExtra = leerDoublePositivo(sc, "Ingrese la tarifa por hora extra: ");

        NominaAsalariado nomina = new NominaAsalariado(new Date(), emp, tarifaHoraExtra);

        System.out.print("¿Aplica bono? (s/n): ");
        String resp = sc.nextLine().trim();
        double bono = 0;
        if (resp.equalsIgnoreCase("s")) {
            bono = leerDoublePositivo(sc, "Monto del bono: ");
        }

        System.out.print("¿Aplica horas extra? (s/n): ");
        resp = sc.nextLine().trim();
        int horasExtra = 0;
        if (resp.equalsIgnoreCase("s")) {
            horasExtra = (int) leerDoublePositivo(sc, "Cantidad de horas extra: ");
        }

        if (bono > 0 && horasExtra > 0) {
            nomina.calcularSueldo(bono, horasExtra);
        } else if (bono > 0) {
            nomina.calcularSueldo(bono);
        } else {
            nomina.calcularSueldo();
        }

        registro.Cargar_Nomina(nomina);
        System.out.println("Nomina de " + emp.getNombre() + " registrada. Total: " + nomina.get_Total());
    }

    /*
     Procesa la nomina de un empleado por horas.
     */
    private static void procesarPorHoras(Scanner sc, EmpleadoPorHoras emp, RegistroNomina registro) {
        double horasTrabajadas = leerDoublePositivo(sc, "Horas trabajadas en este periodo: ");
        double tarifaHoraExtra = leerDoublePositivo(sc, "Ingrese la tarifa por hora extra: ");

        NominaPorHoras nomina = new NominaPorHoras(new Date(), emp, tarifaHoraExtra, horasTrabajadas);

        System.out.print("¿Aplica bono? (s/n): ");
        String resp = sc.nextLine().trim();
        double bono = 0;
        if (resp.equalsIgnoreCase("s")) {
            bono = leerDoublePositivo(sc, "Monto del bono: ");
        }

        System.out.print("¿Aplica horas extra? (s/n): ");
        resp = sc.nextLine().trim();
        int horasExtra = 0;
        if (resp.equalsIgnoreCase("s")) {
            horasExtra = (int) leerDoublePositivo(sc, "Cantidad de horas extra: ");
        }

        if (bono > 0 && horasExtra > 0) {
            nomina.calcularSueldo(bono, horasExtra);
        } else if (bono > 0) {
            nomina.calcularSueldo(bono);
        } else {
            nomina.calcularSueldo();
        }

        registro.Cargar_Nomina(nomina);
        System.out.println("Nomina de " + emp.getNombre() + " registrada. Total: " + nomina.get_Total());
    }

    /*
     Lee un valor double positivo desde el usuario.
     */
    private static double leerDoublePositivo(Scanner sc, String mensaje) {
        double valor;
        while (true) {
            System.out.print(mensaje);
            try {
                valor = Double.parseDouble(sc.nextLine());
                if (valor <= 0) {
                    System.out.println("Error: Debe ser un valor positivo.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero valido.");
            }
        }
    }
}