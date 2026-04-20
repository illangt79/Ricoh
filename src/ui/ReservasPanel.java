package ui;

import manager.VideoClub;
import model.Cliente;
import model.Pelicula;
import model.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReservasPanel extends JPanel {
    private final VideoClub videoClub;
    private final DefaultTableModel modelo;
    private final JTable tabla;
    private final JCheckBox chkSoloActivas;

    public ReservasPanel(VideoClub videoClub) {
        this.videoClub = videoClub;
        this.modelo = new DefaultTableModel(new String[]{"ID", "Cliente", "Película", "Fecha", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        this.tabla = new JTable(modelo);
        this.chkSoloActivas = new JCheckBox("Mostrar solo reservas activas", true);
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
        tabla.getColumnModel().getColumn(4).setMaxWidth(90);

        JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelNorth.add(chkSoloActivas);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnCrear = new JButton("Crear reserva");
        JButton btnDevolver = new JButton("Devolver");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnCrear);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnEliminar);

        add(panelNorth, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        chkSoloActivas.addActionListener(e -> refrescar());
        btnCrear.addActionListener(e -> dialogoCrearReserva());
        btnDevolver.addActionListener(e -> devolver());
        btnEliminar.addActionListener(e -> eliminar());
    }

    public void refrescar() {
        modelo.setRowCount(0);
        List<Reserva> lista = chkSoloActivas.isSelected()
                ? videoClub.getReservasActivas()
                : videoClub.getReservas();
        for (Reserva r : lista) {
            modelo.addRow(new Object[]{
                r.getId(),
                r.getCliente().getNombre(),
                r.getPelicula().getTitulo(),
                r.getFecha().toString(),
                r.isActiva() ? "Activa" : "Devuelta"
            });
        }
    }

    private void dialogoCrearReserva() {
        List<Cliente> clientes = videoClub.getClientes();
        List<Pelicula> disponibles = videoClub.getPeliculas().stream()
                .filter(p -> p.getCopiasDisponibles() > 0)
                .collect(Collectors.toList());

        if (clientes.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay clientes registrados."); return; }
        if (disponibles.isEmpty()) { JOptionPane.showMessageDialog(this, "No hay películas disponibles."); return; }

        JComboBox<Cliente> cbCliente = new JComboBox<>(clientes.toArray(new Cliente[0]));
        JComboBox<Pelicula> cbPelicula = new JComboBox<>(disponibles.toArray(new Pelicula[0]));

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 8));
        form.add(new JLabel("Cliente:")); form.add(cbCliente);
        form.add(new JLabel("Película:")); form.add(cbPelicula);

        if (JOptionPane.showConfirmDialog(this, form, "Crear reserva",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        Cliente cliente = (Cliente) cbCliente.getSelectedItem();
        Pelicula pelicula = (Pelicula) cbPelicula.getSelectedItem();
        JOptionPane.showMessageDialog(this, videoClub.crearReserva(cliente.getId(), pelicula.getId()));
        refrescar();
    }

    private void devolver() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una reserva."); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        JOptionPane.showMessageDialog(this, videoClub.devolverPelicula(id));
        refrescar();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una reserva."); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar esta reserva?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        JOptionPane.showMessageDialog(this, videoClub.eliminarReserva(id));
        refrescar();
    }
}
