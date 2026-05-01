/*
 Interfaz que define los metodos para calcular sueldos.
 Declara tres variantes de calculo: sin parametros, con bono, y con bono más horas extra.
 Las clases que implementen esta interfaz (como Nomina) deben proporcionar la logica concreta.
 Tipo: interfaz.
 */
public interface IPagable {
    /*
     Calcula el sueldo sin considerar bono ni horas extra.
     Total a pagar.
     */
    double calcularSueldo();

    /*
     Calcula el sueldo incluyendo un bono fijo.
     bono Monto adicional al sueldo base.
     Total a pagar.
     */
    double calcularSueldo(double bono);

    /*
     Calcula el sueldo incluyendo un bono y un número de horas extra.
     bono       Monto del bono.
     horasExtra Cantidad de horas extra trabajadas.
     Total a pagar.
     */
    double calcularSueldo(double bono, int horasExtra);
}