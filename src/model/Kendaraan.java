package model;

import java.time.Year;

/**
 * Kendaraan milik pelanggan yang masuk ke bengkel untuk diservis.
 * Semua atribut private; perubahan nilai hanya lewat setter yang memvalidasi.
 */
public class Kendaraan {
    private String plat;
    private String merek;
    private String model;
    private int tahun;
    private String pemilik;
    private boolean sedangDiservis;

    /** Konstruktor default: semua atribut diisi nilai awal yang aman. */
    public Kendaraan() {
        this.plat = "-";
        this.merek = "-";
        this.model = "-";
        this.tahun = Year.now().getValue();
        this.pemilik = "-";
        this.sedangDiservis = false;
    }

    /** Konstruktor berparameter. Nilai dikirim lewat setter supaya ikut divalidasi. */
    public Kendaraan(String plat, String merek, String model, int tahun, String pemilik) {
        this.setPlat(plat);
        this.setMerek(merek);
        this.setModel(model);
        this.setTahun(tahun);
        this.setPemilik(pemilik);
        this.sedangDiservis = false;
    }

    // ---------- getter & setter ----------

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        if (plat == null || plat.trim().isEmpty()) {
            throw new IllegalArgumentException("Plat nomor tidak boleh kosong.");
        }
        // plat disimpan huruf besar supaya pencarian tidak sensitif huruf besar/kecil
        this.plat = plat.trim().toUpperCase();
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        if (merek == null || merek.trim().isEmpty()) {
            throw new IllegalArgumentException("Merek kendaraan tidak boleh kosong.");
        }
        this.merek = merek.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model kendaraan tidak boleh kosong.");
        }
        this.model = model.trim();
    }

    public int getTahun() {
        return tahun;
    }

    public void setTahun(int tahun) {
        int tahunSekarang = Year.now().getValue();
        if (tahun < 1950 || tahun > tahunSekarang) {
            throw new IllegalArgumentException("Tahun kendaraan harus antara 1950 dan " + tahunSekarang + ".");
        }
        this.tahun = tahun;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        if (pemilik == null || pemilik.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama pemilik tidak boleh kosong.");
        }
        this.pemilik = pemilik.trim();
    }

    public boolean isSedangDiservis() {
        return sedangDiservis;
    }

    // ---------- perilaku ----------

    /** Menandai kendaraan masuk servis. Gagal kalau masih ada servis aktif. */
    public void mulaiServis() {
        if (sedangDiservis) {
            throw new IllegalStateException("Kendaraan " + plat + " masih dalam servis aktif.");
        }
        sedangDiservis = true;
    }

    /** Menandai kendaraan sudah keluar dari servis. */
    public void selesaiServis() {
        sedangDiservis = false;
    }

    /** Menghitung umur kendaraan (dalam tahun) dari tahun pembuatan. */
    public int hitungUsia() {
        return Year.now().getValue() - tahun;
    }

    public void tampilkanInfo() {
        System.out.println("Plat     : " + plat);
        System.out.println("Kendaraan: " + merek + " " + model + " (" + tahun + ", usia " + hitungUsia() + " th)");
        System.out.println("Pemilik  : " + pemilik);
        System.out.println("Status   : " + (sedangDiservis ? "Sedang diservis" : "Tidak dalam servis"));
    }
}
