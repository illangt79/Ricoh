package ui;

import manager.VideoClub;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientesPanel extends JPanel {
    private final VideoClub videoClub;
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public ClientesPanel(VideoClub videoClub) {
        this.videoClub = videoClub;
        this.modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Teléfono"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        this.tabla = new JTable(modelo);
        initUI();
        refrescar();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnAnadir = new JButton("Añadir");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnAnadir);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAnadir.addActionListener(e -> dialogoAnadir());
        btnModificar.addActionListener(e -> dialogoModificar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    public void refrescar() {
        modelo.setRowCount(0);
        for (Cliente c : videoClub.getClientes()) {
            modelo.addRow(new Object[]{c.getId(), c.getNombre(), c.getTelefono()});
        }
    }

    private void dialogoAnadir() {
        JTextField fNombre = new JTextField(20);
        JTextField fTelefono = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 8));
        form.add(new JLabel("Nombre:")); form.add(fNombre);
        form.add(new JLabel("Teléfono:")); form.add(fTelefono);

        if (JOptionPane.showConfirmDialog(this, form, "Añadir cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        String nombre = fNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return; }
        JOptionPane.showMessageDialog(this, videoClub.añadirCliente(nombre, fTelefono.getText().trim()));
        refrescar();
    }

    private void dialogoModificar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un cliente."); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        Cliente c = videoClub.buscarClientePorId(id);
        if (c == null) return;

        JTextField fNombre = new JTextField(c.getNombre(), 20);
        JTextField fTelefono = new JTextField(c.getTelefono(), 20);

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 8));
        form.add(new JLabel("Nombre:")); form.add(fNombre);
        form.add(new JLabel("Teléfono:")); form.add(fTelefono);

        if (JOptionPane.showConfirmDialog(this, form, "Modificar cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        String nombre = fNombre.getText().trim();
        if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return; }
        JOptionPane.showMessageDialog(this, videoClub.modificarCliente(id, nombre, fTelefono.getText().trim()));
        refrescar();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un cliente."); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = (String) modelo.getValueAt(fila, 1);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar a \"" + nombre + "\"?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        JOptionPane.showMessageDialog(this, videoClub.eliminarCliente(id));
        refrescar();
    }
}
