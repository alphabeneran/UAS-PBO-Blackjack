// File: LoginGUI.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginGUI() {
        super("Blackjack Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Pusatkan jendela

        setupUI();
    }

    private void setupUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nama Pemain:"), gbc);

        // Username Field
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Mengisi ruang horizontal
        usernameField = new JTextField(15); // Lebar field
        panel.add(usernameField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Membentang 2 kolom
        gbc.fill = GridBagConstraints.NONE; // Tidak mengisi ruang
        loginButton = new JButton("Masuk / Daftar");
        panel.add(loginButton, gbc);

        // Status Label
        gbc.gridy = 2;
        statusLabel = new JLabel("Masukkan nama pemain Anda.", SwingConstants.CENTER);
        statusLabel.setForeground(Color.BLUE);
        panel.add(statusLabel, gbc);

        add(panel, BorderLayout.CENTER);

        // Tambahkan aksi untuk tombol Login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                if (username.isEmpty()) {
                    statusLabel.setText("Nama pemain tidak boleh kosong!");
                    statusLabel.setForeground(Color.RED);
                } else {
                    statusLabel.setText("Memuat pemain...");
                    statusLabel.setForeground(Color.BLACK);
                    // Panggil DatabaseManager untuk memuat/mendaftar pemain
                    Pemain pemain = DatabaseManager.muatPemain(username);
                    if (pemain != null) {
                        statusLabel.setText("Berhasil masuk sebagai " + pemain.getNama() + "!");
                        statusLabel.setForeground(Color.GREEN.darker());

                        // Sembunyikan jendela login dan tampilkan jendela permainan
                        dispose(); // Tutup jendela login
                        SwingUtilities.invokeLater(() -> {
                            new GameBlackjackGUI(pemain).setVisible(true); // Kirim objek Pemain ke game
                        });
                    } else {
                        statusLabel.setText("Gagal memuat/mendaftar pemain. Coba lagi.");
                        statusLabel.setForeground(Color.RED);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        // Jalankan GUI di Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginGUI().setVisible(true);
            }
        });
    }
}