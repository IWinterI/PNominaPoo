import java.util.*;
import java.util.concurrent.*;

/*
 Clase de utilidad que procesa la nómina de todos los empleados.
 Recolecta primero los datos de cada empleado de forma secuencial (por la
 interacción con el usuario) y luego calcula las nóminas en paralelo utilizando un pool de hilos.
 Tipo de clase: utilidad (métodos estáticos).
 */
public class ProcesarNomina {

    /*
     Estructura auxiliar para almacenar los datos necesarios para generar una nómina después de la entrada interactiva.
     */
    private static class DatosNomina {
        final Empleado empleado;
        final double tarifaHoraExtra;
        final double bono;
        final int horasExtra;
        final double horasTrabajadas;   // solo se usa para empleado por horas

        DatosNomina(Empleado empleado, double tarifaHoraExtra, double bono,
                    int horasExtra, double horasTrabajadas) {
            this.empleado = empleado;
            this.tarifaHoraExtra = tarifaHoraExtra;
            this.bono = bono;
            this.horasExtra = horasExtra;
            this.horasTrabajadas = horasTrabajadas;
        }
    }

    /*
    Procesa la nómina de todos los empleados.
    Primero obtiene los datos necesarios mediante interacción con el usuario de forma secuencial) 
    y después lanza múltiples hilos para calcular las nóminas concurrentemente y registrarlas.
    sc        Scanner para entrada de usuario.
    empleados Lista de empleados.
    registro  Registro donde se almacenarán las nóminas (debe ser seguro para uso concurrente).
     */
    public static void procesar(Scanner sc, List<Empleado> empleados,
                                RegistroNomina registro) {
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados para procesar.");
            return;
        }

        System.out.println("\n--- Procesar nómina (concurrente) ---");

        // 1. FASE DE ENTRADA: recolección de datos (secuencial)
        List<DatosNomina> datosNominas = new ArrayList<>();

        for (Empleado emp : empleados) {
            System.out.println("\nDatos para: " + emp.getNombre() +
                               " (ID: " + emp.getId() + ")");

            if (emp instanceof EmpleadoAsalariado) {
                double tarifaHoraExtra = leerDoublePositivo(sc,
                        "Ingrese la tarifa por hora extra: ");
                double bono = preguntarBono(sc);
                int horasExtra = preguntarHorasExtra(sc);
                datosNominas.add(new DatosNomina(emp, tarifaHoraExtra,
                                                 bono, horasExtra, 0));
            } else if (emp instanceof EmpleadoPorHoras) {
                double horasTrabajadas = leerDoublePositivo(sc,
                        "Horas trabajadas en este periodo: ");
                double tarifaHoraExtra = leerDoublePositivo(sc,
                        "Ingrese la tarifa por hora extra: ");
                double bono = preguntarBono(sc);
                int horasExtra = preguntarHorasExtra(sc);
                datosNominas.add(new DatosNomina(emp, tarifaHoraExtra,
                                    bono, horasExtra, horasTrabajadas));
            }
        }

        // 2. FASE DE CÁLCULO CONCURRENTE
        // Se crea un pool con tantos hilos como procesadores virtuales.
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        // Para sincronizar la finalización de todas las tareas.
        CountDownLatch latch = new CountDownLatch(datosNominas.size());

        for (DatosNomina datos : datosNominas) {
            executor.execute(() -> {
                try {
                    Nomina nomina = crearYCalcularNomina(datos);
                    registro.Cargar_Nomina(nomina);
                    System.out.println("Nómina de " +
                            datos.empleado.getNombre() +
                            " registrada. Total: " + nomina.get_Total());
                } catch (Exception e) {
                    System.err.println("Error al procesar a " +
                            datos.empleado.getNombre() + ": " +
                            e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        // Se espera a que todos los hilos finalicen.
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Proceso interrumpido.");
        }
        executor.shutdown();

        System.out.println("\nNómina procesada exitosamente.");
    }

    /*
     Crea el objeto Nomina adecuado según el tipo de empleado y ejecuta la variante correcta de calcularSueldo() de acuerdo con los datos recogidos.
     */
    private static Nomina crearYCalcularNomina(DatosNomina datos) {
        Nomina nomina;
        if (datos.empleado instanceof EmpleadoAsalariado) {
            nomina = new NominaAsalariado(new Date(), datos.empleado,
                                        datos.tarifaHoraExtra);
        } else {
            nomina = new NominaPorHoras(new Date(), datos.empleado,
                                        datos.tarifaHoraExtra,
                                        datos.horasTrabajadas);
        }

        // Logica idéntica a la original para elegir el método de calculo.
        if (datos.bono > 0 && datos.horasExtra > 0) {
            nomina.calcularSueldo(datos.bono, datos.horasExtra);
        } else if (datos.bono > 0) {
            nomina.calcularSueldo(datos.bono);
        } else if (datos.horasExtra > 0) {
            nomina.calcularSueldo(datos.horasExtra);
        }else {
            nomina.calcularSueldo();
        }
        return nomina;
    }

    // ------------------- metodos auxiliares de entrada -------------------

    private static double preguntarBono(Scanner sc) {
        System.out.print("¿Aplica bono? (s/n): ");
        String resp = sc.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            return leerDoublePositivo(sc, "Monto del bono: ");
        }
        return 0;
    }

    private static int preguntarHorasExtra(Scanner sc) {
        System.out.print("¿Aplica horas extra? (s/n): ");
        String resp = sc.nextLine().trim();
        if (resp.equalsIgnoreCase("s")) {
            return (int) leerDoublePositivo(sc, "Cantidad de horas extra: ");
        }
        return 0;
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
                System.out.println("Error: Ingrese un número válido.");
            }
        }
    }
}