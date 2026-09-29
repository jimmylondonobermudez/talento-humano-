package Controlador;

import Modelo.*;
import java.util.ArrayList;

public class EmpleadoControlador {

    public static final String[] TIPOS_EMPLEADO = {
            "Operativo", "Administrativo", "Comercial"
    };

    private final RepositorioEmpleados repositorio = new RepositorioEmpleados();
    private final ArrayList<String> historial = new ArrayList<>();

    public EmpleadoControlador() {
        repositorio.agregar(new EmpleadoBase("1001", "Ana Torres", 1800000));
        repositorio.agregar(new EmpleadoAdministrativo("1002", "Luis Gómez", 2500000, 300000));
        repositorio.agregar(new EmpleadoBase("1003", "Marta Ríos", 1750000));
        repositorio.agregar(new EmpleadoAdministrativo("1004", "Pedro Cano", 3200000, 300000));
    }

    private boolean numeroValido(String texto) {
        try {
            Double.parseDouble(texto.replace(".", ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String validar(String cedula, String nombre,
                           String salario, String tipo, String extra) {

        if (cedula.isEmpty() || nombre.isEmpty())
            return "La cédula y el nombre son obligatorios.";

        if (!numeroValido(salario))
            return "El salario no es válido.";

        if (tipo.equals("Administrativo") || tipo.equals("Comercial")) {

            if (!numeroValido(extra))
                return "El valor no es válido.";

            if (tipo.equals("Comercial") &&
                    Double.parseDouble(extra.replace(".", "")) > 50)
                return "La comisión no puede superar el 50%.";
        }

        return null;
    }

    private EmpleadoBase crear(String cedula, String nombre,
                               String salario, String tipo, String extra) {

        double sueldo = Double.parseDouble(salario.replace(".", ""));

        if (tipo.equals("Administrativo")) {
            return new EmpleadoAdministrativo(
                    cedula, nombre, sueldo,
                    Double.parseDouble(extra.replace(".", ""))
            );
        }

        if (tipo.equals("Comercial")) {
            return new EmpleadoComercial(
                    cedula, nombre, sueldo,
                    Double.parseDouble(extra.replace(".", ""))
            );
        }

        return new EmpleadoBase(cedula, nombre, sueldo);
    }

    public String agregarEmpleado(String cedula, String nombre,
                                  String salario, String tipo, String extra) {

        String error = validar(cedula, nombre, salario, tipo, extra);

        if (error != null) return error;

        if (repositorio.agregar(
                crear(cedula, nombre, salario, tipo, extra))) {

            historial.add("Agregado: " + cedula);
            return "Empleado agregado correctamente.";
        }

        return "La cédula ya existe.";
    }

    public EmpleadoBase buscarEmpleado(String cedula) {
        return repositorio.buscar(cedula);
    }

    public String actualizarEmpleado(String cedula, String nombre,
                                     String salario, String tipo, String extra) {

        String error = validar(cedula, nombre, salario, tipo, extra);

        if (error != null) return error;

        if (repositorio.actualizar(
                crear(cedula, nombre, salario, tipo, extra))) {

            historial.add("Actualizado: " + cedula);
            return "Empleado actualizado correctamente.";
        }

        return "El empleado no existe.";
    }

    public String eliminarEmpleado(String cedula) {

        if (repositorio.eliminar(cedula)) {
            historial.add("Eliminado: " + cedula);
            return "Empleado eliminado correctamente.";
        }

        return "El empleado no existe.";
    }

    public ArrayList<EmpleadoBase> obtenerEmpleados() {
        return repositorio.listarTodos();
    }

    public double calcularTotalNomina() {

        double total = 0;

        for (EmpleadoBase empleado : repositorio.listarTodos())
            total += empleado.calcularSalarioTotal();

        return total;
    }

    public ArrayList<String> obtenerHistorial() {
        return historial;
    }
}