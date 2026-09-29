package Modelo;

public class EmpleadoComercial extends EmpleadoBase {

    private double porcentajeComision;

    public EmpleadoComercial(String cedula, String nombre,
                             double salarioBase, double porcentajeComision) {

        super(cedula, nombre, salarioBase);
        this.porcentajeComision = porcentajeComision;
    }

    public double getPorcentajeComision() {
        return porcentajeComision;
    }

    @Override
    public double calcularSalarioTotal() {

        double comision =
                getSalarioBase() * porcentajeComision / 100;

        return getSalarioBase() + comision;
    }

    @Override
    public String getTipo() {
        return "Comercial";
    }
}