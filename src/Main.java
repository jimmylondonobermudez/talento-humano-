import Controlador.EmpleadoControlador;
import Vista.VentanaEmpleados;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EmpleadoControlador controlador = new EmpleadoControlador();

            VentanaEmpleados ventana = new VentanaEmpleados(controlador);

            ventana.setVisible(true);
        });
    }
}