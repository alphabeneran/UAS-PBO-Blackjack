// File: TanganPanel.java

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TanganPanel extends JPanel {
    public TanganPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // Tata letak horizontal untuk kartu
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Sedikit padding
    }

    // Metode untuk memperbarui tampilan tangan kartu
    public void perbaruiTangan(List<Kartu> tangan, boolean isDealer, boolean sembunyikanKartuPertamaDealer) {
        removeAll(); // Hapus semua kartu yang ada
        for (int i = 0; i < tangan.size(); i++) {
            Kartu kartu = tangan.get(i);
            boolean tersembunyiUntukPanelIni = sembunyikanKartuPertamaDealer && isDealer && i == 0;
            KartuPanel kartuPanel = new KartuPanel(kartu, tersembunyiUntukPanelIni);
            add(kartuPanel);
        }
        revalidate(); // Validasi ulang layout
        repaint();    // Gambar ulang komponen
    }

    // Metode untuk mengosongkan panel tangan
    public void kosongkan() {
        removeAll();
        revalidate();
        repaint();
    }
}