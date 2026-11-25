package model;

/**
 * Suku cadang yang dipakai pada sebuah servis.
 * Objek ini dimiliki oleh Servis (komposisi): kalau servisnya hilang, suku cadangnya ikut hilang.
 */
public class SukuCadang {
    private String kode;
    private String nama;
    private double hargaSatuan;
    private int jumlah;

    /** Konstruktor default. */
    public SukuCadang() {
        this.kode = "-";
        this.nama = "-";
        this.hargaSatuan = 1;
        this.jumlah = 1;
    }

    /** Konstruktor berparameter. */
    public SukuCadang(String kode, String nama, double hargaSatuan, int jumlah) {
        this.setKode(kode);
        this.setNama(nama);
        this.setHargaSatuan(hargaSatuan);
        this.setJumlah(jumlah);
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode suku cadang tidak boleh kosong.");
        }
        this.kode = kode.trim().toUpperCase();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama suku cadang tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(double hargaSatuan) {
        // !(x > 0) sekaligus menolak NaN
        if (!(hargaSatuan > 0)) {
            throw new IllegalArgumentException("Harga suku cadang harus lebih dari 0.");
        }
        this.hargaSatuan = hargaSatuan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah suku cadang harus lebih dari 0.");
        }
        this.jumlah = jumlah;
    }

    /** Subtotal = harga satuan x jumlah. */
    public double hitungSubtotal() {
        return hargaSatuan * jumlah;
    }

    /** Menambah jumlah (dipakai kalau suku cadang yang sama dimasukkan lagi ke servis). */
    public void tambahJumlah(int tambahan) {
        if (tambahan <= 0) {
            throw new IllegalArgumentException("Jumlah tambahan harus lebih dari 0.");
        }
        this.jumlah += tambahan;
    }

    public void tampilkanInfo() {
        System.out.println("  - [" + kode + "] " + nama + " | " + jumlah + " x "
                + String.format("Rp %,.0f", hargaSatuan) + " = " + String.format("Rp %,.0f", hitungSubtotal()));
    }
}
