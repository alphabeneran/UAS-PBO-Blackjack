// File: DatabaseManager.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    // Konfigurasi Database (Ganti sesuai konfigurasi Anda)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/blackjack_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // Metode untuk mendapatkan koneksi ke database
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Memuat data pemain dari database berdasarkan nama pemain.
     * Jika pemain tidak ada, akan membuat objek Pemain baru.
     * @param namaPemain Nama pemain yang akan dicari.
     * @return Objek Pemain dengan data dari DB, atau Pemain baru jika tidak ditemukan.
     */
    public static Pemain muatPemain(String namaPemain) {
        Pemain pemain = null;
        String sql = "SELECT nama, jumlah_kemenangan, jumlah_kekalahan FROM pemain WHERE nama = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, namaPemain);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Pemain ditemukan di database
                String nama = rs.getString("nama");
                int kemenangan = rs.getInt("jumlah_kemenangan");
                int kekalahan = rs.getInt("jumlah_kekalahan");
                pemain = new Pemain(nama, kemenangan, kekalahan);
                System.out.println("Pemain '" + namaPemain + "' dimuat dari database.");
            } else {
                // Pemain tidak ditemukan, buat objek Pemain baru
                pemain = new Pemain(namaPemain);
                System.out.println("Pemain '" + namaPemain + "' tidak ditemukan, membuat pemain baru.");
                // Dan simpan ke DB untuk pertama kalinya
                simpanAtauPerbaruiPemain(pemain);
            }
        } catch (SQLException e) {
            System.err.println("Error saat memuat pemain dari database: " + e.getMessage());
            e.printStackTrace();
            // Jika ada error DB, kembalikan pemain baru sebagai fallback
            pemain = new Pemain(namaPemain);
        }
        return pemain;
    }

    /**
     * Menyimpan data pemain ke database. Jika pemain sudah ada, akan diperbarui.
     * Jika belum ada, akan ditambahkan sebagai record baru.
     * @param pemain Objek Pemain yang akan disimpan/diperbarui.
     */
    public static void simpanAtauPerbaruiPemain(Pemain pemain) {
        // Cek apakah pemain sudah ada di DB
        String checkSql = "SELECT COUNT(*) FROM pemain WHERE nama = ?";
        String updateSql = "UPDATE pemain SET jumlah_kemenangan = ?, jumlah_kekalahan = ? WHERE nama = ?";
        String insertSql = "INSERT INTO pemain (nama, jumlah_kemenangan, jumlah_kekalahan) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, pemain.getNama());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                // Pemain sudah ada, lakukan UPDATE
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, pemain.getJumlahKemenangan());
                    updateStmt.setInt(2, pemain.getJumlahKekalahan());
                    updateStmt.setString(3, pemain.getNama());
                    updateStmt.executeUpdate();
                    System.out.println("Data pemain '" + pemain.getNama() + "' berhasil diperbarui.");
                }
            } else {
                // Pemain belum ada, lakukan INSERT
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, pemain.getNama());
                    insertStmt.setInt(2, pemain.getJumlahKemenangan());
                    insertStmt.setInt(3, pemain.getJumlahKekalahan());
                    insertStmt.executeUpdate();
                    System.out.println("Pemain '" + pemain.getNama() + "' baru ditambahkan ke database.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat menyimpan/memperbarui pemain: " + e.getMessage());
            e.printStackTrace();
        }
    }
}