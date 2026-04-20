package ui;

import manager.VideoClub;
import model.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PeliculasPanel extends JPanel {
    private final VideoClub videoClub;
    private final DefaultTableModel modelo;
    private final JTable tabla;

    public PeliculasPanel(VideoClub videoClub) {
        this.videoClub = videoClub;
        this.modelo = new DefaultTableModel(new String[]{"ID", "Título", "Género", "Copias disponibles"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        this.tabla = new JTable(modelo);
        initUI();
        refrescar();
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JTextField campoBusqueda = new JTextField(18);
        JButton btnTitulo = new JButton("Buscar por título");
        JButton btnGenero = new JButton("Buscar por género");
        JButton btnLimpiar = new JButton("Mostrar todas");
        panelBusqueda.add(new JLabel("Búsqueda:"));
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(btnTitulo);
        panelBusqueda.add(btnGenero);
        panelBusqueda.add(btnLimpiar);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(3).setMaxWidth(130);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnAnadir = new JButton("Añadir");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnAnadir);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(panelBusqueda, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnTitulo.addActionListener(e -> {
            String t = campoBusqueda.getText().trim();
            cargar(t.isEmpty() ? videoClub.getPeliculas() : videoClub.filtrarPorTitulo(t));
        });
        btnGenero.addActionListener(e -> {
            String t = campoBusqueda.getText().trim();
            cargar(t.isEmpty() ? videoClub.getPeliculas() : videoClub.filtrarPorGenero(t));
        });
        btnLimpiar.addActionListener(e -> { campoBusqueda.setText(""); refrescar(); });
        btnAnadir.addActionListener(e -> dialogoAnadir());
        btnModificar.addActionListener(e -> dialogoModificar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    public void refrescar() { cargar(videoClub.getPeliculas()); }

    private void cargar(List<Pelicula> lista) {
        modelo.setRowCount(0);
        for (Pelicula p : lista) {
            modelo.addRow(new Object[]{p.getId(), p.getTitulo(), p.getGenero(), p.getCopiasDisponibles()});
        }
    }

    private void dialogoAnadir() {
        JTextField fTitulo = new JTextField(20);
        JTextField fGenero = new JTextField(20);
        JSpinner fCopias = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 8));
        form.add(new JLabel("Título:")); form.add(fTitulo);
        form.add(new JLabel("Género:")); form.add(fGenero);
        form.add(new JLabel("Copias:")); form.add(fCopias);

        if (JOptionPane.showConfirmDialog(this, form, "Añadir película",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        String titulo = fTitulo.getText().trim();
        String genero = fGenero.getText().trim();
        if (titulo.isEmpty() || genero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título y género son obligatorios.");
            return;
        }
        JOptionPane.showMessageDialog(this, videoClub.añadirPelicula(titulo, genero, (int) fCopias.getValue()));
        refrescar();
    }

    private void dialogoModificar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una película."); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        Pelicula p = videoClub.buscarPeliculaPorId(id);
        if (p == null) return;

        JTextField fTitulo = new JTextField(p.getTitulo(), 20);
        JTextField fGenero = new JTextField(p.getGenero(), 20);
        JSpinner fCopias = new JSpinner(new SpinnerNumberModel(p.getCopiasDisponibles(), 0, 100, 1));

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 8));
        form.add(new JLabel("Título:")); form.add(fTitulo);
        form.add(new JLabel("Género:")); form.add(fGenero);
        form.add(new JLabel("Copias:")); form.add(fCopias);

        if (JOptionPane.showConfirmDialog(this, form, "Modificar película",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) return;

        String titulo = fTitulo.getText().trim();
        String genero = fGenero.getText().trim();
        if (titulo.isEmpty() || genero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título y género son obligatorios.");
            return;
        }
        JOptionPane.showMessageDialog(this, videoClub.modificarPelicula(id, titulo, genero, (int) fCopias.getValue()));
        refrescar();
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona una película."); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        String titulo = (String) modelo.getValueAt(fila, 1);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar \"" + titulo + "\"?", "Confirmar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        JOptionPane.showMessageDialog(this, videoClub.eliminarPelicula(id));
        refrescar();
    }
}
