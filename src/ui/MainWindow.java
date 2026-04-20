package ui;

import manager.VideoClub;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        VideoClub videoClub = new VideoClub();

        setTitle("VideoClub - Sistema de Reservas");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        PeliculasPanel panelPeliculas = new PeliculasPanel(videoClub);
        ClientesPanel panelClientes = new ClientesPanel(videoClub);
        ReservasPanel panelReservas = new ReservasPanel(videoClub);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Peliculas", panelPeliculas);
        tabs.addTab("Clientes", panelClientes);
        tabs.addTab("Reservas", panelReservas);

        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            if (i == 0) panelPeliculas.refrescar();
            else if (i == 1) panelClientes.refrescar();
            else panelReservas.refrescar();
        });

        add(tabs);
    }
}
