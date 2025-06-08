// File: Pemain.java (KEMBALI KE VERSI AWAL, HANYA TAMBAH ATRIBUT SKOR)

// Tidak perlu lagi import java.io.Serializable
import java.util.ArrayList;
import java.util.List;

public class Pemain extends EntitasPermainan {
    private int jumlahKemenangan;
    private int jumlahKekalahan;

    public Pemain(String nama) {
        super(nama);
        this.jumlahKemenangan = 0;
        this.jumlahKekalahan = 0;
    }

    // Constructor tambahan untuk memuat pemain dari database
    public Pemain(String nama, int kemenangan, int kekalahan) {
        super(nama);
        this.jumlahKemenangan = kemenangan;
        this.jumlahKekalahan = kekalahan;
    }

    @Override
    public boolean harusHit() {
        return false;
    }

    public int getJumlahKemenangan() {
        return jumlahKemenangan;
    }

    public void tambahKemenangan() {
        this.jumlahKemenangan++;
    }

    public int getJumlahKekalahan() {
        return jumlahKekalahan;
    }

    public void tambahKekalahan() {
        this.jumlahKekalahan++;
    }

    public void setJumlahKemenangan(int jumlahKemenangan) { // Tambahkan setter untuk inisialisasi dari DB
        this.jumlahKemenangan = jumlahKemenangan;
    }

    public void setJumlahKekalahan(int jumlahKekalahan) { // Tambahkan setter untuk inisialisasi dari DB
        this.jumlahKekalahan = jumlahKekalahan;
    }

    public void resetSkor() {
        this.jumlahKemenangan = 0;
        this.jumlahKekalahan = 0;
    }
}