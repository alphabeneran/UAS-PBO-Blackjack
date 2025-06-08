// File: EntitasPermainan.java

import java.util.ArrayList;
import java.util.List;

public abstract class EntitasPermainan {
    protected String nama;
    protected List<Kartu> tanganKartu;

    public EntitasPermainan(String nama) {
        this.nama = nama;
        this.tanganKartu = new ArrayList<>();
    }

    public void tambahKartuKeTangan(Kartu kartu) {
        this.tanganKartu.add(kartu);
    }

    public int hitungTotalPoin() {
        int totalPoin = 0;
        int jumlahAs = 0;

        for (Kartu kartu : tanganKartu) {
            totalPoin += kartu.getPoin();
            if (kartu.getNilai().equals("As")) {
                jumlahAs++;
            }
        }

        while (totalPoin > 21 && jumlahAs > 0) {
            totalPoin -= 10;
            jumlahAs--;
        }

        return totalPoin;
    }

    // Untuk GUI, kita tidak akan lagi menggunakan System.out.print di sini
    // Public getter untuk mendapatkan tangan kartu
    public List<Kartu> getTanganKartu() {
        return tanganKartu;
    }

    public String getNama() {
        return nama;
    }

    public abstract boolean harusHit();

    // Metode untuk mengosongkan tangan kartu
    public void kosongkanTangan() {
        this.tanganKartu.clear();
    }
}