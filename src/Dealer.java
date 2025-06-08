// File: Dealer.java (Sama seperti sebelumnya)

public class Dealer extends EntitasPermainan {
    public Dealer() {
        super("Dealer");
    }

    @Override
    public boolean harusHit() {
        return hitungTotalPoin() < 17;
    }
}