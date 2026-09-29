package Vista;

import Controlador.EmpleadoControlador;
import Modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaEmpleados extends JFrame {

    private final EmpleadoControlador controlador;

    private final JTextField txtCedula = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtSalario = new JTextField();
    private final JTextField txtExtra = new JTextField();

    private final JComboBox<String> cmbTipo =
            new JComboBox<>(EmpleadoControlador.TIPOS_EMPLEADO);

    private final DefaultTableModel modelo =
            new DefaultTableModel(
                    new String[]{"Cédula", "Nombre", "Tipo",
                            "Salario", "Total"}, 0);

    private final JTable tabla = new JTable(modelo);

    public VentanaEmpleados(EmpleadoControlador controlador) {

        this.controlador = controlador;

        setTitle("Sistema de Talento Humano");
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));

        formulario.add(new JLabel("Cédula:"));
        formulario.add(txtCedula);

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);

        formulario.add(new JLabel("Salario:"));
        formulario.add(txtSalario);

        formulario.add(new JLabel("Tipo:"));
        formulario.add(cmbTipo);

        formulario.add(new JLabel("Bonificación / Comisión %:"));
        formulario.add(txtExtra);

        JButton agregar = new JButton("Agregar");
        JButton buscar = new JButton("Buscar");
        JButton actualizar = new JButton("Actualizar");
        JButton eliminar = new JButton("Eliminar");
        JButton limpiar = new JButton("Limpiar");

        JPanel botones = new JPanel();

        botones.add(agregar);
        botones.add(buscar);
        botones.add(actualizar);
        botones.add(eliminar);
        botones.add(limpiar);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.add(formulario, BorderLayout.CENTER);
        arriba.add(botones, BorderLayout.SOUTH);

        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        actualizarCampo();

        cmbTipo.addActionListener(e -> actualizarCampo());

        agregar.addActionListener(e -> mensaje(
                controlador.agregarEmpleado(
                        txtCedula.getText(),
                        txtNombre.getText(),
                        txtSalario.getText(),
                        (String) cmbTipo.getSelectedItem(),
                        txtExtra.getText()
                )
        ));

        buscar.addActionListener(e -> buscar());

        actualizar.addActionListener(e -> mensaje(
                controlador.actualizarEmpleado(
                        txtCedula.getText(),
                        txtNombre.getText(),
                        txtSalario.getText(),
                        (String) cmbTipo.getSelectedItem(),
                        txtExtra.getText()
                )
        ));

        eliminar.addActionListener(e -> mensaje(
                controlador.eliminarEmpleado(txtCedula.getText())
        ));

        limpiar.addActionListener(e -> limpiar());

        cargarTabla();
    }

    private void actualizarCampo() {

        String tipo = (String) cmbTipo.getSelectedItem();

        txtExtra.setEnabled(
                tipo.equals("Administrativo") ||
                        tipo.equals("Comercial")
        );

        if (tipo.equals("Operativo"))
            txtExtra.setText("");
    }

    private void cargarTabla() {

        modelo.setRowCount(0);

        for (EmpleadoBase e : controlador.obtenerEmpleados()) {

            modelo.addRow(new Object[]{
                    e.getCedula(),
                    e.getNombre(),
                    e.getTipo(),
                    e.getSalarioBase(),
                    e.calcularSalarioTotal()
            });
        }
    }

    private void buscar() {

        EmpleadoBase e =
                controlador.buscarEmpleado(txtCedula.getText());

        if (e == null) {
            mensaje("Empleado no encontrado.");
            return;
        }

        txtNombre.setText(e.getNombre());
        txtSalario.setText(String.valueOf(e.getSalarioBase()));
        cmbTipo.setSelectedItem(e.getTipo());

        if (e instanceof EmpleadoAdministrativo) {

            txtExtra.setText(String.valueOf(
                    ((EmpleadoAdministrativo) e).getBonificacion()
            ));

        } else if (e instanceof EmpleadoComercial) {

            txtExtra.setText(String.valueOf(
                    ((EmpleadoComercial) e).getPorcentajeComision()
            ));
        }
    }

    private void limpiar() {

        txtCedula.setText("");
        txtNombre.setText("");
        txtSalario.setText("");
        txtExtra.setText("");
        cmbTipo.setSelectedIndex(0);

        actualizarCampo();
    }

    private void mensaje(String texto) {

        JOptionPane.showMessageDialog(this, texto);
        cargarTabla();
    }
}