// File: GameBlackjackGUI.java (MODIFIKASI)

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

public class GameBlackjackGUI extends JFrame {
    private Dek dek;
    private Pemain pemain; // Objek pemain ini akan diterima dari LoginGUI
    private Dealer dealer;

    // Komponen GUI
    private TanganPanel pemainTanganPanel;
    private TanganPanel dealerTanganPanel;
    private JLabel labelPemainScore;
    private JLabel labelDealerScore;
    private JLabel labelStatus;
    private JButton btnDeal;
    private JButton btnHit;
    private JButton btnStand;
    private JLabel labelWinLoss;

    // --- MODIFIKASI: Konstruktor menerima objek Pemain ---
    public GameBlackjackGUI(Pemain pemainYangLogin) {
        super("Blackjack Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        this.pemain = pemainYangLogin; // Gunakan objek Pemain yang diterima dari login
        // Tidak perlu lagi memanggil DatabaseManager.muatPemain di sini

        this.dek = new Dek();
        this.dealer = new Dealer();

        setupUI();
        aturTombolAwal();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Simpan data pemain ke database saat menutup aplikasi
                DatabaseManager.simpanAtauPerbaruiPemain(pemain);
                dispose();
                System.exit(0);
            }
        });
    }
    // --- AKHIR MODIFIKASI KONSTRUKTOR ---


    private void setupUI() {
        setLayout(new BorderLayout());

        // Panel untuk Tangan Dealer
        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setBorder(BorderFactory.createTitledBorder("Dealer"));
        dealerTanganPanel = new TanganPanel();
        dealerPanel.add(dealerTanganPanel, BorderLayout.CENTER);
        labelDealerScore = new JLabel("Total: ?", SwingConstants.CENTER);
        dealerPanel.add(labelDealerScore, BorderLayout.SOUTH);
        add(dealerPanel, BorderLayout.NORTH);

        // Panel untuk Pesan Status dan Info Skor
        JPanel centerPanel = new JPanel(new BorderLayout());
        labelStatus = new JLabel("Klik 'Deal' untuk memulai permainan!", SwingConstants.CENTER);
        labelStatus.setFont(new Font("Arial", Font.BOLD, 18));
        centerPanel.add(labelStatus, BorderLayout.CENTER);

        // Label win/loss harus diperbarui sesuai data pemain yang dimuat
        labelWinLoss = new JLabel("Kemenangan: " + pemain.getJumlahKemenangan() + " | Kekalahan: " + pemain.getJumlahKekalahan(), SwingConstants.CENTER);
        labelWinLoss.setFont(new Font("Arial", Font.PLAIN, 14));
        centerPanel.add(labelWinLoss, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);


        // Panel untuk Tangan Pemain
        JPanel pemainPanel = new JPanel(new BorderLayout());
        pemainPanel.setBorder(BorderFactory.createTitledBorder("Pemain"));
        pemainTanganPanel = new TanganPanel();
        pemainPanel.add(pemainTanganPanel, BorderLayout.CENTER);
        labelPemainScore = new JLabel("Total: 0", SwingConstants.CENTER);
        pemainPanel.add(labelPemainScore, BorderLayout.SOUTH);
        add(pemainPanel, BorderLayout.SOUTH);

        // Panel untuk Tombol Kontrol
        JPanel controlPanel = new JPanel();
        btnDeal = new JButton("Deal");
        btnHit = new JButton("Hit");
        btnStand = new JButton("Stand");

        controlPanel.add(btnDeal);
        controlPanel.add(btnHit);
        controlPanel.add(btnStand);

        add(controlPanel, BorderLayout.EAST);

        // Tambahkan ActionListener ke tombol
        btnDeal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mulaiPermainanBaru();
            }
        });

        btnHit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pemainHit();
            }
        });

        btnStand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pemainStand();
            }
        });
    }

    private void aturTombolAwal() {
        btnDeal.setEnabled(true);
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        labelStatus.setText("Klik 'Deal' untuk memulai permainan!");
        updateWinLossLabel();
    }

    private void mulaiPermainanBaru() {
        dek = new Dek();
        pemain.kosongkanTangan();
        dealer.kosongkanTangan();

        dek.acakDek();

        pemain.tambahKartuKeTangan(dek.ambilKartuTeratas());
        dealer.tambahKartuKeTangan(dek.ambilKartuTeratas());
        pemain.tambahKartuKeTangan(dek.ambilKartuTeratas());
        dealer.tambahKartuKeTangan(dek.ambilKartuTeratas());

        pemainTanganPanel.perbaruiTangan(pemain.getTanganKartu(), false, false);
        dealerTanganPanel.perbaruiTangan(dealer.getTanganKartu(), true, true);

        labelPemainScore.setText("Total: " + pemain.hitungTotalPoin());
        labelDealerScore.setText("Total: ?");
        labelStatus.setText("Giliran Pemain!");

        btnDeal.setEnabled(false);
        btnHit.setEnabled(true);
        btnStand.setEnabled(true);

        if (pemain.hitungTotalPoin() == 21) {
            labelStatus.setText("Blackjack! Giliran Dealer...");
            btnHit.setEnabled(false);
            btnStand.setEnabled(false);
            SwingUtilities.invokeLater(() -> {
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {Thread.currentThread().interrupt();}
                dealerMain();
            });
        }
    }

    private void pemainHit() {
        Kartu kartuBaru = dek.ambilKartuTeratas();
        pemain.tambahKartuKeTangan(kartuBaru);
        pemainTanganPanel.perbaruiTangan(pemain.getTanganKartu(), false, false);
        labelPemainScore.setText("Total: " + pemain.hitungTotalPoin());

        if (pemain.hitungTotalPoin() > 21) {
            labelStatus.setText("Anda BUST! Dealer menang.");
            pemain.tambahKekalahan();
            akhirPermainan();
        } else if (pemain.hitungTotalPoin() == 21) {
            labelStatus.setText("21! Giliran Dealer...");
            akhirGiliranPemain();
        }
    }

    private void pemainStand() {
        labelStatus.setText("Anda memilih 'Stand'. Giliran Dealer...");
        akhirGiliranPemain();
    }

    private void akhirGiliranPemain() {
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        SwingUtilities.invokeLater(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {Thread.currentThread().interrupt();}
            dealerMain();
        });
    }

    private void dealerMain() {
        dealerTanganPanel.perbaruiTangan(dealer.getTanganKartu(), true, false);
        labelDealerScore.setText("Total: " + dealer.hitungTotalPoin());

        while (dealer.harusHit()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Kartu kartuBaru = dek.ambilKartuTeratas();
            dealer.tambahKartuKeTangan(kartuBaru);
            dealerTanganPanel.perbaruiTangan(dealer.getTanganKartu(), true, false);
            labelDealerScore.setText("Total: " + dealer.hitungTotalPoin());
        }

        if (dealer.hitungTotalPoin() > 21) {
            labelStatus.setText("Dealer BUST! Anda menang!");
            pemain.tambahKemenangan();
        }
        akhirPermainan();
    }

    private void akhirPermainan() {
        int poinPemain = pemain.hitungTotalPoin();
        int poinDealer = dealer.hitungTotalPoin();

        if (poinPemain > 21) {
            // Sudah ditangani di pemainHit()
        } else if (poinDealer > 21) {
            // Sudah ditangani di dealerMain()
        } else if (poinPemain > poinDealer) {
            labelStatus.setText("Anda Menang! " + poinPemain + " vs " + poinDealer);
            pemain.tambahKemenangan();
        } else if (poinDealer > poinPemain) {
            labelStatus.setText("Dealer Menang! " + poinDealer + " vs " + poinPemain);
            pemain.tambahKekalahan();
        } else {
            labelStatus.setText("Seri! " + poinPemain + " vs " + poinDealer);
        }

        updateWinLossLabel();
        btnDeal.setEnabled(true);
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
    }

    private void updateWinLossLabel() {
        labelWinLoss.setText("Kemenangan: " + pemain.getJumlahKemenangan() + " | Kekalahan: " + pemain.getJumlahKekalahan());
    }

    // --- MODIFIKASI: Hapus main method dari sini ---
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(new Runnable() {
    //         public void run() {
    //             new GameBlackjackGUI().setVisible(true);
    //         }
    //     });
    // }
    // --- AKHIR MODIFIKASI main method ---
}