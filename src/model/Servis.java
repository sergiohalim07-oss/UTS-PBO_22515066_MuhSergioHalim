package model;

import java.util.ArrayList;

/**
 * Satu transaksi servis: menghubungkan satu Kendaraan dengan satu Mekanik
 * dan menyimpan daftar suku cadang yang dipakai.
 * Biaya servis = biaya jasa + total harga suku cadang.
 */
public class Servis {
    private static final String STATUS_AKTIF = "AKTIF";
    private static final String STATUS_SELESAI = "SELESAI";
    private static final String STATUS_DIBATALKAN = "DIBATALKAN";

    private String kode;
    private Kendaraan kendaraan;
    private Mekanik mekanik;
    private String keluhan;
    private double biayaJasa;
    private String status;
    private ArrayList<SukuCadang> daftarSukuCadang;

    /** Konstruktor default: servis kosong tanpa kendaraan dan mekanik. */
    public Servis() {
        this.kode = "-";
        this.kendaraan = null;
        this.mekanik = null;
        this.keluhan = "-";
        this.biayaJasa = 0;
        this.status = STATUS_AKTIF;
        this.daftarSukuCadang = new ArrayList<>();
    }

    /** Konstruktor berparameter. Servis baru otomatis berstatus AKTIF. */
    public Servis(String kode, Kendaraan kendaraan, Mekanik mekanik, String keluhan, double biayaJasa) {
        this.setKode(kode);
        this.setKendaraan(kendaraan);
        this.setMekanik(mekanik);
        this.setKeluhan(keluhan);
        this.setBiayaJasa(biayaJasa);
        this.status = STATUS_AKTIF;
        this.daftarSukuCadang = new ArrayList<>();
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode servis tidak boleh kosong.");
        }
        this.kode = kode.trim().toUpperCase();
    }

    public Kendaraan getKendaraan() {
        return kendaraan;
    }

    public void setKendaraan(Kendaraan kendaraan) {
        if (kendaraan == null) {
            throw new IllegalArgumentException("Servis harus terhubung ke sebuah kendaraan.");
        }
        this.kendaraan = kendaraan;
    }

    public Mekanik getMekanik() {
        return mekanik;
    }

    public void setMekanik(Mekanik mekanik) {
        if (mekanik == null) {
            throw new IllegalArgumentException("Servis harus ditangani oleh seorang mekanik.");
        }
        this.mekanik = mekanik;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        if (keluhan == null || keluhan.trim().isEmpty()) {
            throw new IllegalArgumentException("Keluhan tidak boleh kosong.");
        }
        this.keluhan = keluhan.trim();
    }

    public double getBiayaJasa() {
        return biayaJasa;
    }

    public void setBiayaJasa(double biayaJasa) {
        if (!(biayaJasa >= 0)) {
            throw new IllegalArgumentException("Biaya jasa tidak boleh negatif.");
        }
        this.biayaJasa = biayaJasa;
    }

    public String getStatus() {
        return status;
    }

    /** Mengembalikan salinan list supaya list asli tidak bisa diubah dari luar. */
    public ArrayList<SukuCadang> getDaftarSukuCadang() {
        return new ArrayList<>(daftarSukuCadang);
    }

    public boolean isAktif() {
        return status.equals(STATUS_AKTIF);
    }

    public boolean isSelesai() {
        return status.equals(STATUS_SELESAI);
    }

    public boolean isDibatalkan() {
        return status.equals(STATUS_DIBATALKAN);
    }

    // ---------- perilaku ----------

    /** Menambah suku cadang. Kalau kodenya sudah ada, jumlahnya digabung. Hanya untuk servis aktif. */
    public void tambahSukuCadang(SukuCadang sukuCadang) {
        if (sukuCadang == null) {
            throw new IllegalArgumentException("Suku cadang tidak boleh kosong.");
        }
        if (!isAktif()) {
            throw new IllegalStateException("Servis " + kode + " berstatus " + status
                    + ", suku cadang tidak bisa ditambah.");
        }
        for (SukuCadang sc : daftarSukuCadang) {
            if (sc.getKode().equals(sukuCadang.getKode())) {
                sc.tambahJumlah(sukuCadang.getJumlah());
                return;
            }
        }
        daftarSukuCadang.add(sukuCadang);
    }

    /** Total harga semua suku cadang pada servis ini. */
    public double hitungBiayaSukuCadang() {
        double total = 0;
        for (SukuCadang sc : daftarSukuCadang) {
            total += sc.hitungSubtotal();
        }
        return total;
    }

    /** Aturan bisnis: biaya servis = jasa + suku cadang. */
    public double hitungTotalBiaya() {
        return biayaJasa + hitungBiayaSukuCadang();
    }

    /** Menyelesaikan servis, lalu membebaskan kendaraan dan mekanik. */
    public void selesaikan() {
        if (!isAktif()) {
            throw new IllegalStateException("Servis " + kode + " tidak bisa diselesaikan karena berstatus " + status + ".");
        }
        status = STATUS_SELESAI;
        kendaraan.selesaiServis();
        mekanik.selesaiTugas();
    }

    /** Membatalkan servis (dipakai oleh fitur batalkan servis terakhir). */
    public void batalkan() {
        if (isAktif()) {
            // masih berjalan: kendaraan dan mekanik dilepas
            kendaraan.selesaiServis();
            mekanik.batalkanTugas();
        } else if (isSelesai()) {
            // sudah selesai: resource sudah bebas, tinggal koreksi hitungan mekanik
            mekanik.koreksiServisSelesai();
        }
        status = STATUS_DIBATALKAN;
    }

    public void tampilkanInfo() {
        System.out.println("Kode servis : " + kode + " [" + status + "]");
        System.out.println("Kendaraan   : " + (kendaraan == null ? "-" : kendaraan.getPlat() + " (" + kendaraan.getMerek() + " " + kendaraan.getModel() + ")"));
        System.out.println("Mekanik     : " + (mekanik == null ? "-" : mekanik.getNama() + " (" + mekanik.getId() + ")"));
        System.out.println("Keluhan     : " + keluhan);
        System.out.println("Biaya jasa  : " + String.format("Rp %,.0f", biayaJasa));
        if (daftarSukuCadang.isEmpty()) {
            System.out.println("Suku cadang : (belum ada)");
        } else {
            System.out.println("Suku cadang :");
            for (SukuCadang sc : daftarSukuCadang) {
                sc.tampilkanInfo();
            }
        }
        System.out.println("Total biaya : " + String.format("Rp %,.0f", hitungTotalBiaya()));
    }
}
