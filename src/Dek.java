// File: Dek.java

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dek {
    private List<Kartu> kartu;

    public Dek() {
        this.kartu = new ArrayList<>();
        String[] jenisKartu = {"Hati", "Wajik", "Keriting", "Sekop"};
        String[] nilaiKartu = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "As"};

        for (String jenis : jenisKartu) {
            for (String nilai : nilaiKartu) {
                this.kartu.add(new Kartu(jenis, nilai));
            }
        }
        acakDek();
    }

    public void acakDek() {
        Collections.shuffle(this.kartu);
        System.out.println("Dek telah dikocok.");
    }

    public Kartu ambilKartuTeratas() {
        if (kartu.isEmpty()) {
            // Untuk GUI, kita bisa memutuskan untuk membuat dek baru atau menangani skenario ini.
            // Untuk sementara, kita buat dek baru jika habis.
            System.out.println("Dek kosong! Membuat dek baru dan mengocok.");
            this.kartu = new Dek().kartu; // Buat dek baru secara rekursif (bukan cara terbaik untuk game sungguhan)
            acakDek();
        }
        return kartu.remove(0);
    }

    public int getJumlahKartu() {
        return kartu.size();
    }
}