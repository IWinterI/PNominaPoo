import java.util.*;

public class ProcesarNomina {

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