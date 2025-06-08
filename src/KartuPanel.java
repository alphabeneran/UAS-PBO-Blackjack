// File: KartuPanel.java

import javax.swing.*;
import java.awt.*;

public class KartuPanel extends JPanel {
    private Kartu kartu;
    private boolean tersembunyi; // Untuk kartu tertutup (dealer)

    public KartuPanel(Kartu kartu, boolean tersembunyi) {
        this.kartu = kartu;
        this.tersembunyi = tersembunyi;
        setPreferredSize(new Dimension(72, 96)); // Ukuran standar kartu remi
        // Set background, border, dll. jika diperlukan
        // setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img;
        if (tersembunyi) {
            img = Kartu.getBackImageIcon().getImage(); // Ambil gambar belakang kartu
        } else {
            img = kartu.getImageIcon().getImage(); // Ambil gambar kartu asli
        }
        if (img != null) {
            // Gambar kartu agar sesuai dengan ukuran panel
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Gambar placeholder jika gambar tidak ditemukan
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("?", getWidth() / 2 - 5, getHeight() / 2 + 5);
        }
    }

    // Metode untuk mengubah status tersembunyi
    public void setTersembunyi(boolean tersembunyi) {
        this.tersembunyi = tersembunyi;
        repaint(); // Gambar ulang panel
    }
}