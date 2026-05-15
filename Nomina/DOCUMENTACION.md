# 🏢 Sistema de Gestión de Nóminas — Documentación

> **Versión**: 1.0-SNAPSHOT  
> **Stack**: Java 16 + Swing GUI + Maven  
> **Entry point**: `VentanaM` (interfaz gráfica)  
> **Archivo**: `DOCUMENTACION.md`

---

## 📑 Índice

1. [Visión General](#-visión-general)
2. [Arquitectura del Sistema](#-arquitectura-del-sistema)
3. [Jerarquía de Clases](#-jerarquía-de-clases)
4. [Diagrama de Flujo General](#-diagrama-de-flujo-general)
5. [Módulo: Empleados](#-módulo-empleados)
6. [Módulo: Nóminas](#-módulo-nóminas)
7. [Módulo: Interfaz Gráfica (GUI)](#-módulo-interfaz-gráfica-gui)
8. [Módulo: Persistencia](#-módulo-persistencia)
9. [Facade: main.java](#-facade-mainjava)
10. [Flujo de Procesamiento de Nómina](#-flujo-de-procesamiento-de-nómina)
11. [Glosario](#-glosario)

---

## 🏗️ Visión General

Sistema de escritorio para la gestión de empleados y cálculo de nóminas. Permite:

- **Administrar empleados**: altas, bajas, edición de asalariados y por horas.
- **Procesar nóminas**: cálculo de sueldos con soporte para bonos y horas extra.
- **Persistencia**: guardado y carga del estado del sistema mediante serialización.
- **Interfaz gráfica**: completamente construida con Swing, con tablas, botones y diálogos.

```
┌──────────────────────────────────────────────────────┐
│              SISTEMA DE GESTIÓN DE NÓMINAS             │
│                                                        │
│  ┌───────────┐  ┌──────────────────────────────────┐  │
│  │  VENTANA  │  │        PANEL CENTRAL              │  │
│  │  LATERAL  │  │  ┌────────────────────────┐      │  │
│  │           │  │  │  VISTA EMPLEADOS       │      │  │
│  │ Empleados │  │  │  ┌────┬────┬────┬────┐ │      │  │
│  │  [Lista]  │  │  │  │Nombre│ID│Tipo│...│ │      │  │
│  │           │  │  │  └────┴────┴────┴────┘ │      │  │
│  │ Nóminas   │  │  └────────────────────────┘      │  │
│  │ [Procesar]│  │  ┌────────────────────────┐      │  │
│  │ [Lista]   │  │  │  VISTA NÓMINAS         │      │  │
│  │           │  │  │  ┌────┬────┬────┬────┐ │      │  │
│  │ Datos     │  │  │  │Fecha│Empl│Total│...│ │      │  │
│  │ [Cargar]  │  │  │  └────┴────┴────┴────┘ │      │  │
│  │ [Guardar] │  │  └────────────────────────┘      │  │
│  └───────────┘  └──────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

---

## 🏛️ Arquitectura del Sistema

### Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────────┐
│                        main.java (FACADE)                            │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │  Estado global estático                                      │   │
│  │  - empleados: List<Empleado>                                  │   │
│  │  - siguienteID: int                                           │   │
│  │  - registroNominas: RegistroNomina                            │   │
│  │  - ARCHIVO_DATOS: String ("datos_sistema.ser")                │   │
│  │                                                               │   │
│  │  Métodos privados:                                            │   │
│  │  - generarID()                                                │   │
│  │  - buscarEmpleadoPorID(id)                                    │   │
│  │  - guardarDatos()                                             │   │
│  │  - cargarDatos()                                              │   │
│  │                                                               │   │
│  │  Métodos públicos GUI:                                        │   │
│  │  - agregarEmpleadoAsalariadoGUI(...)                          │   │
│  │  - agregarEmpleadoPorHorasGUI(...)                            │   │
│  │  - getEmpleados()                                             │   │
│  │  - editarEmpleadoGUI(...)                                     │   │
│  │  - eliminarEmpleadoGUI(id)                                    │   │
│  │  - procesarNominasGUI(parent)                                 │   │
│  │  - eliminarNominaGUI(index)                                   │   │
│  │  - guardarDatosGUI() / cargarDatosGUI()                       │   │
│  │  - ... más                                                    │   │
│  └──────────────────────────────────────────────────────────────┘   │
└──────────┬────────────────────────────────────────┬──────────────────┘
           │                                        │
           ▼                                        ▼
┌──────────────────────┐              ┌──────────────────────────────┐
│   DOMINIO (Modelo)   │              │      REGISTRO (Colección)     │
│                      │              │                              │
│  ┌──────────┐        │              │  ┌────────────────────────┐  │
│  │ Empleado │◄───────│──hereda      │  │   RegistroNomina       │  │
│  │(abstract)│        │              │  │                        │  │
│  └────┬─────┘        │              │  │  - CopyOnWriteArrayList│  │
│       │              │              │  │                        │  │
│  ┌────▼─────────┐    │              │  │  + Cargar_Nomina()     │  │
│  │ Empleado     │    │              │  │  + Eliminar_nomina()   │  │
│  │ Asalariado   │    │              │  │  + getNominas()        │  │
│  └──────────────┘    │              │  │  + reemplazarNominas() │  │
│                      │              │  └────────────────────────┘  │
│  ┌──────────────┐    │              └──────────────────────────────┘
│  │ Empleado     │    │
│  │ PorHoras     │    │              ┌──────────────────────────────┐
│  └──────────────┘    │              │       PERSISTENCIA           │
│                      │              │                              │
│  ┌──────────┐        │              │  ┌────────────────────────┐  │
│  │  Nomina  │◄───────│──hereda      │  │   Data (Serializable)  │  │
│  │(abstract)│        │              │  │                        │  │
│  └────┬─────┘        │              │  │  - List<Empleado>      │  │
│       │              │              │  │  - List<Nomina>        │  │
│  ┌────▼─────────┐    │              │  └────────────────────────┘  │
│  │ Nomina       │    │              └──────────────────────────────┘
│  │ Asalariado   │    │
│  └──────────────┘    │
│                      │              ┌──────────────────────────────┐
│  ┌──────────────┐    │              │       INTERFAZ               │
│  │ Nomina       │    │              │                              │
│  │ PorHoras     │    │              │  ┌────────────────────────┐  │
│  └──────────────┘    │              │  │ <<interface>>          │  │
│                      │              │  │    IPagable            │  │
│  ┌──────────────┐    │              │  │                        │  │
│  │  IPagable    │◄───│──implementa  │  │  + calcularSueldo()    │  │
│  │ (interface)  │    │              │  │  + calcularSueldo(×4)  │  │
│  └──────────────┘    │              │  └────────────────────────┘  │
└──────────────────────┘              └──────────────────────────────┘

                    ┌──────────────────────────────┐
                    │    VentanaM (VISTA) Swing     │
                    │                              │
                    │  - JFrame + CardLayout        │
                    │  - Tabla empleados (JTable)   │
                    │  - Tabla nóminas (JTable)     │
                    │  - Diálogos (JOptionPane)     │
                    │  - Botones acción inline       │
                    └──────────────────────────────┘
```

### Patrón de Diseño

El sistema utiliza una variante liviana de **MVC** (Modelo-Vista-Controlador):

| Capa | Rol | Clases |
|------|-----|--------|
| **Modelo** | Lógica de negocio y datos | `Empleado`, `Nomina`, `IPagable`, `Data` |
| **Vista** | Interfaz de usuario | `VentanaM` (Swing) |
| **Controlador/Facade** | Orquestación y estado global | `main` (facade estática) |
| **Colección** | Contenedor thread-safe | `RegistroNomina` |

---

## 🧬 Jerarquía de Clases

### Jerarquía de Empleados

```
Empleado (abstract)                        ← implements Serializable
 ├── EmpleadoAsalariado                    ← + salarioMensual: double
 └── EmpleadoPorHoras                      ← + tarifaHora: double
```

### Jerarquía de Nóminas (implementan IPagable)

```
<<interface>>
   IPagable
      ↑
      │
  Nomina (abstract)                         ← implements IPagable, Serializable
   ├── NominaAsalariado                     ← calcula sobre salario mensual
   └── NominaPorHoras                       ← calcula sobre horas × tarifa
```

---

## 📖 Descripción de Clases

### `Empleado.java` — Clase Abstracta Base (40 líneas)

Representa un empleado genérico. Contiene los atributos comunes a todo empleado.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `ID` | `int` | Identificador único del empleado |
| `Nombre` | `String` | Nombre completo |
| `Puesto` | `String` | Cargo en la empresa |

| Método | Visibilidad | Retorno | Descripción |
|--------|-------------|---------|-------------|
| `Empleado(ID, Nombre, Puesto)` | `public` | — | Constructor |
| `getId()` | `public` | `int` | Obtiene el ID |
| `getNombre()` | `public` | `String` | Obtiene el nombre |
| `getPuesto()` | `public` | `String` | Obtiene el puesto |
| `setNombre(nombre)` | `public` | `void` | Actualiza el nombre |
| `setPuesto(puesto)` | `public` | `void` | Actualiza el puesto |

---

### `EmpleadoAsalariado.java` — Subclase Concreta (31 líneas)

Empleado con **salario mensual fijo**. Extiende `Empleado`.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `salarioMensual` | `double` | Salario fijo mensual |

| Método | Retorno | Descripción |
|--------|---------|-------------|
| `EmpleadoAsalariado(ID, Nombre, Puesto, salarioMensual)` | — | Constructor |
| `getSalarioMensual()` | `double` | Obtiene el salario |
| `setSalarioMensual(salario)` | `void` | Actualiza el salario |

---

### `EmpleadoPorHoras.java` — Subclase Concreta (33 líneas)

Empleado que **cobra por hora trabajada**. Extiende `Empleado`.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `tarifaHora` | `double` | Precio por hora ordinaria |

| Método | Retorno | Descripción |
|--------|---------|-------------|
| `EmpleadoPorHoras(ID, Nombre, Puesto, tarifaHora)` | — | Constructor |
| `getTarifaHora()` | `double` | Obtiene la tarifa por hora |
| `setTarifaHora(tarifa)` | `void` | Actualiza la tarifa |

---

### `IPagable.java` — Interfaz (34 líneas)

Define el **contrato de cálculo de sueldos** con 4 sobrecargas de `calcularSueldo()`.

| Método | Descripción |
|--------|-------------|
| `calcularSueldo()` | Sueldo base, sin extras |
| `calcularSueldo(double bono)` | Sueldo base + bono |
| `calcularSueldo(int horasExtra)` | Sueldo base + pago horas extra |
| `calcularSueldo(double bono, int horasExtra)` | Sueldo base + bono + horas extra |

---

### `Nomina.java` — Clase Abstracta (65 líneas)

Representa una **nómina genérica**. Contiene los atributos comunes.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `Fecha` | `Date` | Fecha de emisión |
| `Empleado` | `Empleado` | Referencia polimórfica al empleado |
| `HorasExtra` | `int` | Cantidad de horas extra |
| `TarifaHoraExtra` | `double` | Tarifa por hora extra |
| `HorasExtraPago` | `double` | Monto total por horas extra |
| `Total` | `double` | Total calculado de la nómina |

| Método | Visibilidad | Descripción |
|--------|-------------|-------------|
| `calcularHorasExtra(int)` | `protected abstract` | Calcula el pago por horas extra |
| `set_Total(double)` | `protected` | Fija el total calculado |

Declara como **abstractos** los 4 métodos de `calcularSueldo()` de la interfaz `IPagable`.

---

### `NominaAsalariado.java` — Subclase Concreta (114 líneas)

Nómina para empleados asalariados. El sueldo base es el **salario mensual**.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `Sueldo` | `double` | Sueldo base mensual |
| `Bono` | `double` | Bono aplicado |

**Fórmulas de cálculo**:

| Sobrecarga | Fórmula |
|------------|---------|
| `calcularSueldo()` | `Total = salarioMensual` |
| `calcularSueldo(bono)` | `Total = salarioMensual + bono` |
| `calcularSueldo(horasExtra)` | `Total = salarioMensual + (horasExtra × tarifaHoraExtra)` |
| `calcularSueldo(bono, horasExtra)` | `Total = salarioMensual + bono + (horasExtra × tarifaHoraExtra)` |

---

### `NominaPorHoras.java` — Subclase Concreta (119 líneas)

Nómina para empleados por horas. El sueldo base es **horas trabajadas × tarifa por hora**.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `HorasTrabajadas` | `double` | Horas trabajadas en el período |
| `TarifaHora` | `double` | Tarifa por hora ordinaria |
| `Bono` | `double` | Bono aplicado |

**Fórmulas de cálculo**:

| Sobrecarga | Fórmula |
|------------|---------|
| `calcularSueldo()` | `Total = horasTrabajadas × tarifaHora` |
| `calcularSueldo(bono)` | `Total = (horas × tarifa) + bono` |
| `calcularSueldo(horasExtra)` | `Total = (horas × tarifa) + (horasExtra × tarifaHoraExtra)` |
| `calcularSueldo(bono, horasExtra)` | `Total = (horas × tarifa) + bono + (horasExtra × tarifaHoraExtra)` |

---

### `RegistroNomina.java` — Contenedor Thread-Safe (62 líneas)

Almacena y gestiona nóminas usando una **`CopyOnWriteArrayList`** para seguridad en concurrencia.

| Método | Retorno | Descripción |
|--------|---------|-------------|
| `Cargar_Nomina(Nomina)` | `void` | Agrega una nómina (si no es nula) |
| `Listar_Nomina()` | `void` | Mantenido por compatibilidad (no operativo) |
| `Ver_Detalles(int)` | `void` | Mantenido por compatibilidad (no operativo) |
| `Eliminar_nomina(int)` | `Boolean` | Elimina por índice; retorna `true` si éxito |
| `reemplazarNominas(List<Nomina>)` | `void` | Reemplaza toda la lista (usado en carga) |
| `getNominas()` | `List<Nomina>` | Retorna copia de la lista de nóminas |

---

### `Data.java` — DTO de Persistencia (21 líneas)

Contenedor serializable que almacena el estado completo del sistema.

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `empleados` | `List<Empleado>` | Todos los empleados |
| `nominas` | `List<Nomina>` | Todas las nóminas |

---

### `main.java` — Fachada Estática / Controlador (251 líneas)

Clase principal que actúa como **facade** entre la GUI y el modelo. Mantiene el estado global del sistema.

| Atributo Estático | Tipo | Descripción |
|-------------------|------|-------------|
| `empleados` | `List<Empleado>` | Lista global de empleados |
| `siguienteID` | `int` | Contador auto-incremental de IDs |
| `registroNominas` | `RegistroNomina` | Registro de nóminas |
| `ARCHIVO_DATOS` | `String` | Nombre del archivo de persistencia |

#### Métodos Privados (Helpers)

| Método | Retorno | Descripción |
|--------|---------|-------------|
| `generarID()` | `int` | Genera y retorna un nuevo ID secuencial |
| `buscarEmpleadoPorID(int)` | `Empleado` | Busca empleado por ID; null si no existe |
| `guardarDatos()` | `void` | Serializa empleados + nóminas a archivo |
| `cargarDatos()` | `void` | Deserializa y restaura estado desde archivo |

#### Métodos Públicos (Facade para GUI)

| Método | Retorno | Descripción |
|--------|---------|-------------|
| `agregarEmpleadoAsalariadoGUI(nombre, puesto, salario)` | `EmpleadoAsalariado` | Crea y agrega un asalariado |
| `agregarEmpleadoPorHorasGUI(nombre, puesto, tarifa)` | `EmpleadoPorHoras` | Crea y agrega un empleado por horas |
| `getEmpleados()` | `List<Empleado>` | Retorna la lista global de empleados |
| `editarEmpleadoGUI(id, nombre, puesto, salario, tarifa)` | `boolean` | Edita un empleado; retorna éxito |
| `eliminarEmpleadoGUI(id)` | `boolean` | Elimina un empleado por ID |
| `recalcularSiguienteID()` | `void` | Recalcula el siguiente ID basado en empleados existentes |
| `getRegistroNominas()` | `RegistroNomina` | Retorna el registro de nóminas |
| `procesarNominasGUI(Component)` | `void` | Procesa nóminas con input vía JOptionPane |
| `eliminarNominaGUI(index)` | `boolean` | Elimina una nómina por índice |
| `getNominaDetails(index)` | `String` | Retorna detalle de nómina como String |
| `guardarDatosGUI()` | `void` | Wrapper público que llama a `guardarDatos()` |
| `cargarDatosGUI()` | `void` | Wrapper público que llama a `cargarDatos()` |

---

### `VentanaM.java` — Interfaz Gráfica (Swing) (816 líneas)

Ventana principal del sistema. Utiliza **`CardLayout`** para alternar entre vistas.

#### Estructura de la Ventana

```
┌───────────────────────────────────────────────────────────┐
│  Sistema de Gestión de Nóminas                            │
├──────────┬────────────────────────────────────────────────┤
│          │  [Agregar empleado]                            │
│  Menú    ├────────────────────────────────────────────────┤
│  Lateral │                                                │
│          │   ┌──────┬──────┬──────┬──────┐                │
│ [Lista   │   │Nombre│ ID  │ Tipo │  ... │                │
│  empl.]  │   ├──────┼──────┼──────┼──────┤                │
│          │   │ Juan │  1   │Asal. │50000 │ [Editar][Elim]│
│ ──────── │   ├──────┼──────┼──────┼──────┤                │
│ Nóminas  │   │ María│  2   │x Hora│ 250  │ [Editar][Elim]│
│ [Procesar]│   └──────┴──────┴──────┴──────┘                │
│ [Lista]  │                                                │
│ ──────── │              ─ O ─                             │
│ Datos    │   ┌──────┬──────────┬───────┬──────┐          │
│ [Cargar] │   │ Fecha│ Empleado │ Total │  ... │          │
│ [Guardar]│   ├──────┼──────────┼───────┼──────┤          │
│          │   │15/05 │ Juan P.  │55000  │[Det] [Elim]│    │
│          │   └──────┴──────────┴───────┴──────┘          │
└──────────┴────────────────────────────────────────────────┘
```

#### Componentes Principales

| Componente | Tipo | Propósito |
|------------|------|-----------|
| `panelCentral` | `JPanel` + `CardLayout` | Contenedor que alterna entre vistas |
| `panelEmpleados` | `JPanel` | Vista con tabla de empleados |
| `panelNominas` | `JPanel` | Vista con tabla de nóminas |
| `tablaEmpleados` | `JTable` | Tabla de empleados con botones de acción |
| `tablaNominas` | `JTable` | Tabla de nóminas con botones de acción |
| `jPanel1` | `JPanel` | Menú lateral izquierdo |
| `jPanel2` | `JPanel` | Barra superior con botón "Agregar empleado" |

#### Botones del Menú Lateral

| Botón | Acción |
|-------|--------|
| **Lista de empleados** | Muestra vista de empleados |
| **Procesar nómina** | Ejecuta `main.procesarNominasGUI()` y muestra vista de nóminas |
| **Lista de nóminas** | Muestra vista de nóminas |
| **Cargar Datos** | Ejecuta `main.cargarDatosGUI()` y refresca ambas tablas |
| **Guardar Datos** | Ejecuta `main.guardarDatosGUI()` |

#### Clases Internas (Renderers/Editors)

| Clase | Propósito |
|-------|-----------|
| `BotonesEmpleadosRenderer` | Renderiza botones Editar/Eliminar en tabla de empleados |
| `BotonesEmpleadosEditor` | Maneja eventos de botones en tabla de empleados |
| `BotonesNominasRenderer` | Renderiza botones Detalle/Eliminar en tabla de nóminas |
| `BotonesNominasEditor` | Maneja eventos de botones en tabla de nóminas |

#### Métodos Clave de VentanaM

| Método | Visibilidad | Descripción |
|--------|-------------|-------------|
| `configurarVistaEmpleados()` | `private` | Configura columnas, renderers y editores de la tabla de empleados |
| `configurarVistaNominas()` | `private` | Configura columnas, renderers y editores de la tabla de nóminas |
| `cargarEmpleadosEnGUI()` | `private` | Refresca la tabla de empleados desde la lista global |
| `cargarNominasEnGUI()` | `private` | Refresca la tabla de nóminas desde el registro |
| `mostrarDialogoAgregarEmpleado()` | `private` | Dialógo step-by-step para agregar empleado (tipo → nombre → puesto → valor) |
| `editarEmpleadoGUI(int)` | `private` | Panel de edición con campos precargados |
| `eliminarEmpleadoGUI(int)` | `private` | Confirmación y eliminación de empleado |
| `verDetalleNomina(int)` | `private` | Muestra detalle completo en JOptionPane con JTextArea |
| `eliminarNomina(int)` | `private` | Confirmación y eliminación de nómina |
| `buscarEmpleadoPorID(int)` | `private` | Búsqueda lineal sobre lista global |
| `mostrarVistaEmpleados()` | `private` | Cambia CardLayout a vista de empleados |
| `mostrarVistaNominas()` | `private` | Cambia CardLayout a vista de nóminas |
| `main(String[])` | `public static` | Entry point: configura LookAndFeel y lanza VentanaM |

---

## 🔄 Flujo de Procesamiento de Nómina

Este es el flujo más complejo del sistema. Se ejecuta cuando el usuario hace clic en **"Procesar nómina"** en el menú lateral.

```
USUARIO                    VENTANA M                     main.java                  MODELO
  │                           │                             │                         │
  │  click "Procesar"         │                             │                         │
  │ ─────────────────────►    │                             │                         │
  │                           │                             │                         │
  │                           │  procesarNominasGUI(this)   │                         │
  │                           │ ──────────────────────────► │                         │
  │                           │                             │                         │
  │                           │   ¿Hay empleados?           │                         │
  │                           │   │                         │                         │
  │                           │   ├── NO → JOptionPane      │                         │
  │                           │   │        "No hay empleados"                        │
  │                           │   │        return                                    │
  │                           │   │                         │                         │
  │                           │   └── SÍ → [por cada empleado]                       │
  │                           │                             │                         │
  │  ┌── BUCLE por cada ──────┐                             │                         │
  │  │  empleado              │                             │                         │
  │  │                        │                             │                         │
  │  │  JOptionPane:          │                             │                         │
  │  │  "Tarifa hora extra?"  │                             │                         │
  │  │ ◄─────────────────────►│                             │                         │
  │  │                        │                             │                         │
  │  │  JOptionPane:          │                             │                         │
  │  │  "¿Aplica bono?"       │                             │                         │
  │  │ ◄─────────────────────►│                             │                         │
  │  │  └─ SÍ → "Monto bono?" │                             │                         │
  │  │                        │                             │                         │
  │  │  JOptionPane:          │                             │                         │
  │  │  "¿Horas extra?"       │                             │                         │
  │  │ ◄─────────────────────►│                             │                         │
  │  │  └─ SÍ → "Cantidad?"   │                             │                         │
  │  │                        │                             │                         │
  │  │  ┌─ ¿Es Asalariado?    │                             │                         │
  │  │  │  ├── SÍ: new NominaAsalariado(fecha, emp, tarifaHE)                        │
  │  │  │  └── NO: pedir horasTrabajadas → new NominaPorHoras(...)                   │
  │  │  │                    │                             │                         │
  │  │  │  calcularSueldo() según bono y horasExtra       │                         │
  │  │  │                    │                             │                         │
  │  │  │                    │              Cargar_Nomina(nomina)                    │
  │  │  │                    │ ───────────────────────────────────────────────────► │
  │  │  └── ── ── ── ── ── ┘                             │                   │      │
  │  │                                                    │                   │      │
  │  └── FIN BUCLE ────────────────────────────────────── ┘                   │      │
  │                                                                           │      │
  │                           JOptionPane: "Nóminas procesadas"                      │
  │                           ◄────────────────────────────────────────────────      │
  │                                                                                   │
  │                           mostrarVistaNominas()                                    │
  │                           ───────────────► refresca tabla                         │
  │                                                                                   │
```

### Ejemplo de Cálculo Concreto

**Escenario**: Empleado asalariado con salario $50,000, bono $5,000, 10 horas extra a $150/h

```
calcularSueldo(5000, 10)
  → Sueldo      = 50,000
  → Bono        = 5,000
  → HorasExtra  = 10
  → PagoHE      = 10 × 150 = 1,500
  → TOTAL       = 50,000 + 5,000 + 1,500 = 56,500
```

---

## 💾 Flujo de Persistencia

### Guardar Datos

```
main.guardarDatosGUI()
  └── main.guardarDatos()
        └── Data(empleados, nominas)
              └── ObjectOutputStream → datos_sistema.ser
```

### Cargar Datos

```
main.cargarDatosGUI()
  └── main.cargarDatos()
        └── ObjectInputStream ← datos_sistema.ser
              └── Data.getEmpleados() → main.empleados
              └── Data.getNominas()   → registroNominas.reemplazarNominas()
              └── Recalcula siguienteID
```

---

## 📊 Diagrama de Flujo de la Aplicación

```
INICIO: VentanM.main()
  │
  ├── Configurar LookAndFeel (Nimbus / Sistema)
  │
  └── new VentanaM()
        │
        ├── initComponents()         ← Código generado por NetBeans
        ├── configurarVistaEmpleados()
        ├── configurarVistaNominas()
        ├── cargarEmpleadosEnGUI()
        ├── cargarNominasEnGUI()
        ├── cardLayout.show("empleados")
        └── pack() + setVisible(true)
              │
              ▼
        ┌─────┴──────────────────────────────┐
        │       ESPERANDO ACCIÓN             │
        └─────┬──────────────────────────────┘
              │
        ┌─────┴──────────────────────────────┐
        │                                     │
   ┌────▼────┐   ┌────▼────┐   ┌────▼────┐  ┌──▼──────────┐
   │ Lista   │   │Procesar │   │ Cargar  │  │  Guardar    │
   │ Empl.   │   │ Nómina  │   │ Datos   │  │  Datos      │
   └────┬────┘   └────┬────┘   └────┬────┘  └──┬──────────┘
        │             │             │           │
        ▼             ▼             ▼           ▼
   ┌──────────┐  ┌──────────┐  ┌────────┐  ┌────────┐
   │ Mostrar  │  │ Iniciar  │  │ Cargar │  │Guardar │
   │ vista    │  │ flujo de │  │ datos  │  │ datos  │
   │ empleados│  │ nómina   │  │ del    │  │ a      │
   │          │  │ (ver     │  │ archivo│  │archivo │
   │          │  │ diagrama │  │        │  │        │
   │          │  │ anterior)│  │        │  │        │
   └──────────┘  └──────────┘  └────────┘  └────────┘
              │                    │
              ▼                    ▼
         ┌──────────┐        ┌──────────┐
         │ Botones  │        │ Refrescar│
         │ en tabla │        │ tablas   │
         │ [Editar] │        │ y mostrar│
         │ [Eliminar]        │ vista    │
         └──────────┘        └──────────┘
```

---

## 🧪 Resumen de Funcionalidades por Módulo

| Módulo | Funcionalidad | Método Clave |
|--------|---------------|--------------|
| **Empleados** | Crear asalariado | `main.agregarEmpleadoAsalariadoGUI()` |
| | Crear por horas | `main.agregarEmpleadoPorHorasGUI()` |
| | Listar todos | `main.getEmpleados()` |
| | Editar | `main.editarEmpleadoGUI()` |
| | Eliminar | `main.eliminarEmpleadoGUI()` |
| **Nóminas** | Procesar (con opciones) | `main.procesarNominasGUI()` |
| | Ver detalle | `main.getNominaDetails()` |
| | Eliminar | `main.eliminarNominaGUI()` |
| **Persistencia** | Guardar estado | `main.guardarDatosGUI()` |
| | Cargar estado | `main.cargarDatosGUI()` |
| **Interfaz** | Iniciar GUI | `VentanaM.main()` |
| | Agregar empleado (diálogo) | `VentanaM.mostrarDialogoAgregarEmpleado()` |
| | Editar empleado (panel) | `VentanaM.editarEmpleadoGUI()` |

---

## 📁 Estructura del Proyecto

```
Nomina/
├── pom.xml                         ← Configuración Maven (entry point: VentanaM)
├── DOCUMENTACION.md                ← Este archivo
├── datos_sistema.ser               ← Archivo de persistencia (serialización)
├── .atl/
│   └── skill-registry.md           ← Registro de skills (infraestructura SDD)
└── src/main/java/
    ├── Empleado.java               ← Clase abstracta base
    ├── EmpleadoAsalariado.java     ← Empleado con salario fijo
    ├── EmpleadoPorHoras.java       ← Empleado por hora
    ├── IPagable.java               ← Interfaz de cálculo de sueldos
    ├── Nomina.java                 ← Clase abstracta de nómina
    ├── NominaAsalariado.java       ← Nómina para asalariados
    ├── NominaPorHoras.java         ← Nómina para por horas
    ├── RegistroNomina.java         ← Contenedor thread-safe de nóminas
    ├── Data.java                   ← DTO serializable para persistencia
    ├── main.java                   ← Fachada estática (controlador)
    ├── VentanaM.java               ← Interfaz gráfica Swing (vista)
    └── VentanaM.form               ← Diseñador visual de NetBeans (no editar)
```
