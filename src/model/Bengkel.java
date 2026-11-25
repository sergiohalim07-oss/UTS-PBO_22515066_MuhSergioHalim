package model;

import adt.StackRiwayat;
import java.util.ArrayList;

/**
 * Class pusat yang mengelola kendaraan, mekanik, dan servis.
 * Semua aturan bisnis (satu mekanik satu servis aktif, kendaraan tidak boleh
 * diservis ganda, riwayat tidak boleh melebihi kapasitas) dijaga di sini.
 */
public class Bengkel {
    private String nama;
    private String alamat;
    private ArrayList<Kendaraan> daftarKendaraan;
    private ArrayList<Mekanik> daftarMekanik;
    private ArrayList<Servis> daftarServis;
    private StackRiwayat riwayat;
    private int penghitungServis;

    /** Konstruktor default. */
    public Bengkel() {
        this.nama = "Bengkel";
        this.alamat = "-";
        this.daftarKendaraan = new ArrayList<>();
        this.daftarMekanik = new ArrayList<>();
        this.daftarServis = new ArrayList<>();
        this.riwayat = new StackRiwayat();
        this.penghitungServis = 0;
    }

    /** Konstruktor berparameter. kapasitasRiwayat menentukan ukuran ADT Stack. */
    public Bengkel(String nama, String alamat, int kapasitasRiwayat) {
        this.setNama(nama);
        this.setAlamat(alamat);
        this.daftarKendaraan = new ArrayList<>();
        this.daftarMekanik = new ArrayList<>();
        this.daftarServis = new ArrayList<>();
        this.riwayat = new StackRiwayat(kapasitasRiwayat);
        this.penghitungServis = 0;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama bengkel tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat bengkel tidak boleh kosong.");
        }
        this.alamat = alamat.trim();
    }

    public ArrayList<Kendaraan> getDaftarKendaraan() {
        return new ArrayList<>(daftarKendaraan);
    }

    public ArrayList<Mekanik> getDaftarMekanik() {
        return new ArrayList<>(daftarMekanik);
    }

    public ArrayList<Servis> getDaftarServis() {
        return new ArrayList<>(daftarServis);
    }

    public StackRiwayat getRiwayat() {
        return riwayat;
    }

    // ---------- data master ----------

    /** Mendaftarkan kendaraan. Plat tidak boleh kembar. */
    public void tambahKendaraan(Kendaraan kendaraan) {
        if (kendaraan == null) {
            throw new IllegalArgumentException("Data kendaraan tidak boleh kosong.");
        }
        if (cariKendaraan(kendaraan.getPlat()) != null) {
            throw new IllegalArgumentException("Kendaraan dengan plat " + kendaraan.getPlat() + " sudah terdaftar.");
        }
        daftarKendaraan.add(kendaraan);
    }

    /** Menambah mekanik. ID tidak boleh kembar. */
    public void tambahMekanik(Mekanik mekanik) {
        if (mekanik == null) {
            throw new IllegalArgumentException("Data mekanik tidak boleh kosong.");
        }
        if (cariMekanik(mekanik.getId()) != null) {
            throw new IllegalArgumentException("Mekanik dengan ID " + mekanik.getId() + " sudah terdaftar.");
        }
        daftarMekanik.add(mekanik);
    }

    // ---------- pencarian ----------

    /** Cari kendaraan berdasarkan plat. Null jika tidak ada. */
    public Kendaraan cariKendaraan(String plat) {
        if (plat == null) {
            return null;
        }
        for (Kendaraan k : daftarKendaraan) {
            if (k.getPlat().equalsIgnoreCase(plat.trim())) {
                return k;
            }
        }
        return null;
    }

    /** Cari mekanik berdasarkan ID. Null jika tidak ada. */
    public Mekanik cariMekanik(String id) {
        if (id == null) {
            return null;
        }
        for (Mekanik m : daftarMekanik) {
            if (m.getId().equalsIgnoreCase(id.trim())) {
                return m;
            }
        }
        return null;
    }

    /** Cari mekanik berdasarkan potongan nama (boleh lebih dari satu hasil). */
    public ArrayList<Mekanik> cariMekanikByNama(String nama) {
        ArrayList<Mekanik> hasil = new ArrayList<>();
        if (nama == null || nama.trim().isEmpty()) {
            return hasil;
        }
        String kunci = nama.trim().toLowerCase();
        for (Mekanik m : daftarMekanik) {
            if (m.getNama().toLowerCase().contains(kunci)) {
                hasil.add(m);
            }
        }
        return hasil;
    }

    /** Cari servis berdasarkan kode. Null jika tidak ada. */
    public Servis cariServis(String kode) {
        if (kode == null) {
            return null;
        }
        for (Servis s : daftarServis) {
            if (s.getKode().equalsIgnoreCase(kode.trim())) {
                return s;
            }
        }
        return null;
    }

    // ---------- transaksi servis ----------

    /**
     * Membuat servis baru yang menghubungkan kendaraan dan mekanik,
     * lalu mencatatnya ke stack riwayat.
     * Semua pengecekan dilakukan sebelum ada objek yang diubah, jadi kalau
     * ada aturan yang dilanggar tidak ada data yang setengah berubah.
     */
    public Servis buatServis(String plat, String idMekanik, String keluhan, double biayaJasa) {
        Kendaraan kendaraan = cariKendaraan(plat);
        if (kendaraan == null) {
            throw new IllegalArgumentException("Kendaraan dengan plat " + plat + " tidak ditemukan.");
        }
        Mekanik mekanik = cariMekanik(idMekanik);
        if (mekanik == null) {
            throw new IllegalArgumentException("Mekanik dengan ID " + idMekanik + " tidak ditemukan.");
        }
        if (riwayat.isFull()) {
            throw new IllegalStateException("Riwayat servis penuh (kapasitas " + riwayat.getKapasitas()
                    + "). Batalkan servis terakhir lewat menu riwayat untuk mengosongkan tempat.");
        }
        if (kendaraan.isSedangDiservis()) {
            throw new IllegalStateException("Kendaraan " + kendaraan.getPlat() + " masih dalam servis aktif.");
        }
        if (mekanik.isSibuk()) {
            throw new IllegalStateException("Mekanik " + mekanik.getNama() + " masih menangani satu servis aktif.");
        }

        // kalau keluhan atau biaya tidak valid, exception muncul di sini sebelum status diubah
        String kode = String.format("SRV-%03d", penghitungServis + 1);
        Servis servis = new Servis(kode, kendaraan, mekanik, keluhan, biayaJasa);

        kendaraan.mulaiServis();
        mekanik.mulaiTugas();
        riwayat.push(servis);
        daftarServis.add(servis);
        penghitungServis++;
        return servis;
    }

    /** Menambahkan suku cadang ke servis yang masih aktif. */
    public void tambahSukuCadangKeServis(String kodeServis, SukuCadang sukuCadang) {
        Servis servis = cariServis(kodeServis);
        if (servis == null) {
            throw new IllegalArgumentException("Servis dengan kode " + kodeServis + " tidak ditemukan.");
        }
        servis.tambahSukuCadang(sukuCadang);
    }

    /** Menyelesaikan servis dan membebaskan kendaraan serta mekanik. */
    public Servis selesaikanServis(String kodeServis) {
        Servis servis = cariServis(kodeServis);
        if (servis == null) {
            throw new IllegalArgumentException("Servis dengan kode " + kodeServis + " tidak ditemukan.");
        }
        servis.selesaikan();
        return servis;
    }

    /** Operasi pop pada stack: membatalkan servis yang paling baru dibuat. */
    public Servis batalkanServisTerakhir() {
        if (riwayat.isEmpty()) {
            throw new IllegalStateException("Riwayat servis kosong, tidak ada servis yang bisa dibatalkan.");
        }
        Servis servis = riwayat.pop();
        servis.batalkan();
        return servis;
    }

    // ---------- laporan ----------

    /** Pendapatan hanya dihitung dari servis yang berstatus SELESAI. */
    public double hitungTotalPendapatan() {
        double total = 0;
        for (Servis s : daftarServis) {
            if (s.isSelesai()) {
                total += s.hitungTotalBiaya();
            }
        }
        return total;
    }

    public int hitungServisAktif() {
        int n = 0;
        for (Servis s : daftarServis) {
            if (s.isAktif()) {
                n++;
            }
        }
        return n;
    }

    public int hitungServisSelesai() {
        int n = 0;
        for (Servis s : daftarServis) {
            if (s.isSelesai()) {
                n++;
            }
        }
        return n;
    }

    public int hitungServisDibatalkan() {
        int n = 0;
        for (Servis s : daftarServis) {
            if (s.isDibatalkan()) {
                n++;
            }
        }
        return n;
    }

    // ---------- tampilan ----------

    public void tampilkanSemuaKendaraan() {
        if (daftarKendaraan.isEmpty()) {
            System.out.println("Belum ada data kendaraan.");
            return;
        }
        int no = 1;
        for (Kendaraan k : daftarKendaraan) {
            System.out.println("--- Kendaraan #" + no++ + " ---");
            k.tampilkanInfo();
        }
    }

    public void tampilkanSemuaMekanik() {
        if (daftarMekanik.isEmpty()) {
            System.out.println("Belum ada data mekanik.");
            return;
        }
        int no = 1;
        for (Mekanik m : daftarMekanik) {
            System.out.println("--- Mekanik #" + no++ + " ---");
            m.tampilkanInfo();
        }
    }

    public void tampilkanSemuaServis() {
        if (daftarServis.isEmpty()) {
            System.out.println("Belum ada data servis.");
            return;
        }
        for (Servis s : daftarServis) {
            System.out.println("--------------------------");
            s.tampilkanInfo();
        }
    }
}
