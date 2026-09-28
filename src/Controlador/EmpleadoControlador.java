package Controlador;

import Modelo.EmpleadoAdministrativo;
import Modelo.EmpleadoBase;
import Modelo.RepositorioEmpleados;

import java.util.ArrayList;

public class EmpleadoControlador {

    public static final String[] TIPOS_EMPLEADO = {
            "Operativo",
            "Administrativo"
    };

    private final RepositorioEmpleados repositorio;
    private final ArrayList<String> historial;

    public EmpleadoControlador() {
        repositorio = new RepositorioEmpleados();
        historial = new ArrayList<>();

        cargarDatosDePrueba();
    }

    // Carga algunos empleados para probar el programa
    private void cargarDatosDePrueba() {

        String[] cedulas = {"1001", "1002", "1003", "1004"};

        String[] nombres = {
                "Ana Torres",
                "Luis Gómez",
                "Marta Ríos",
                "Pedro Cano"
        };

        double[] salarios = {
                1800000,
                2500000,
                1750000,
                3200000
        };

        for (int i = 0; i < cedulas.length; i++) {

            EmpleadoBase empleado;

            if (i % 2 == 0) {

                empleado = new EmpleadoBase(
                        cedulas[i],
                        nombres[i],
                        salarios[i]
                );

            } else {

                empleado = new EmpleadoAdministrativo(
                        cedulas[i],
                        nombres[i],
                        salarios[i],
                        300000
                );
            }

            repositorio.agregar(empleado);
        }
    }

    // Valida números permitiendo puntos para separar miles
    private boolean esNumeroValido(String texto) {

        if (texto.isEmpty()) {
            return false;
        }

        // Quitamos los puntos antes de revisar el número
        texto = texto.replace(".", "");

        for (int i = 0; i < texto.length(); i++) {

            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    // Valida los datos que vienen del formulario
    private String validar(String cedula,
                           String nombre,
                           String salario,
                           String tipo,
                           String bonificacion) {

        if (cedula.isEmpty() || nombre.isEmpty()) {
            return "La cédula y el nombre son obligatorios.";
        }

        if (!esNumeroValido(salario)) {
            return "El salario debe ser un número válido.";
        }

        if (tipo.equals("Administrativo")
                && !esNumeroValido(bonificacion)) {

            return "La bonificación debe ser un número válido.";
        }

        return null;
    }

    // Crea el tipo de empleado correspondiente
    private EmpleadoBase construirEmpleado(String cedula,
                                           String nombre,
                                           String salario,
                                           String tipo,
                                           String bonificacion) {

        // Quitamos los puntos antes de convertir a double
        double salarioBase = Double.parseDouble(
                salario.replace(".", "")
        );

        if (tipo.equals("Administrativo")) {

            double bono = Double.parseDouble(
                    bonificacion.replace(".", "")
            );

            return new EmpleadoAdministrativo(
                    cedula,
                    nombre,
                    salarioBase,
                    bono
            );
        }

        return new EmpleadoBase(
                cedula,
                nombre,
                salarioBase
        );
    }

    // ======================= CRUD =======================

    public String agregarEmpleado(String cedula,
                                  String nombre,
                                  String salario,
                                  String tipo,
                                  String bonificacion) {

        String error = validar(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        if (error != null) {
            return error;
        }

        EmpleadoBase nuevo = construirEmpleado(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        if (repositorio.agregar(nuevo)) {

            historial.add(
                    "AGREGADO: " + cedula + " - " + nombre
            );

            return "Empleado agregado correctamente.";
        }

        return "Ya existe un empleado con la cédula " + cedula + ".";
    }

    public EmpleadoBase buscarEmpleado(String cedula) {

        historial.add("BÚSQUEDA: " + cedula);

        return repositorio.buscar(cedula);
    }

    public String actualizarEmpleado(String cedula,
                                     String nombre,
                                     String salario,
                                     String tipo,
                                     String bonificacion) {

        String error = validar(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        if (error != null) {
            return error;
        }

        EmpleadoBase actualizado = construirEmpleado(
                cedula,
                nombre,
                salario,
                tipo,
                bonificacion
        );

        if (repositorio.actualizar(actualizado)) {

            historial.add(
                    "ACTUALIZADO: " + cedula + " - " + nombre
            );

            return "Empleado actualizado correctamente.";
        }

        return "No existe ningún empleado con la cédula "
                + cedula + ".";
    }

    public String eliminarEmpleado(String cedula) {

        if (repositorio.eliminar(cedula)) {

            historial.add("ELIMINADO: " + cedula);

            return "Empleado eliminado correctamente.";
        }

        return "No existe ningún empleado con la cédula "
                + cedula + ".";
    }

    public ArrayList<EmpleadoBase> obtenerEmpleados() {
        return repositorio.listarTodos();
    }

    // Calcula el total de todos los salarios
    public double calcularTotalNomina() {

        double total = 0;

        for (EmpleadoBase empleado :
                repositorio.listarTodos()) {

            total += empleado.calcularSalarioTotal();
        }

        return total;
    }

    public ArrayList<String> obtenerHistorial() {
        return historial;
    }
}