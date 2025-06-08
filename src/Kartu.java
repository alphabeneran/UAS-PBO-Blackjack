// File: Kartu.java

import javax.swing.ImageIcon;
import java.net.URL;

public class Kartu {
    private String jenis; // Contoh: "Sekop", "Hati", "Keriting", "Wajik"
    private String nilai; // Contoh: "2", "3", ..., "10", "Jack", "Queen", "King", "As"
    private int poin;     // Nilai poin Blackjack dari kartu tersebut

    public Kartu(String jenis, String nilai) {
        this.jenis = jenis;
        this.nilai = nilai;
        this.poin = hitungPoinKartu(nilai);
    }

    private int hitungPoinKartu(String nilaiKartu) {
        try {
            return Integer.parseInt(nilaiKartu);
        } catch (NumberFormatException e) {
            switch (nilaiKartu) {
                case "Jack":
                case "Queen":
                case "King":
                    return 10;
                case "As":
                    return 11; // Untuk sementara As selalu 11.
                default:
                    return 0;
            }
        }
    }

    public String getJenis() {
        return jenis;
    }

    public String getNilai() {
        return nilai;
    }

    public int getPoin() {
        return poin;
    }

    @Override
    public String toString() {
        return nilai + " " + jenis;
    }

    /**
     * Mengembalikan ImageIcon dari gambar kartu.
     * Asumsikan gambar berada di folder 'kartu_gambar/' di root classpath.
     * Nama file: nilai_jenis.png (contoh: 'as_sekop.png', '10_hati.png')
     * atau back.png untuk kartu tertutup.
     */
    public ImageIcon getImageIcon() {
        String namaFile = nilai.toLowerCase() + "_" + jenis.toLowerCase() + ".png";
        URL imgURL = getClass().getResource("/kartu_gambar/" + namaFile); // Path relatif dari classpath
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Gambar kartu tidak ditemukan: " + namaFile);
            return null; // Atau kembalikan placeholder
        }
    }

    /**
     * Mengembalikan ImageIcon untuk bagian belakang kartu.
     */
    public static ImageIcon getBackImageIcon() {
        URL imgURL = Kartu.class.getResource("/kartu_gambar/back_blue.png");
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Gambar belakang kartu tidak ditemukan: back.png");
            return null;
        }
    }
}