package app;

import adt.StackRiwayat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import model.Bengkel;
import model.Kendaraan;
import model.Mekanik;
import model.Servis;
import model.SukuCadang;

/**
 * Titik masuk program: menu console Sistem Manajemen Bengkel.
 * Semua input pengguna dibaca dan divalidasi di sini, sedangkan
 * aturan bisnis ada di class model.
 */
public class Main {
    private static final int KAPASITAS_RIWAYAT = 5;
    private static final Scanner INPUT = new Scanner(System.in);
    private static Bengkel bengkel;

    public static void main(String[] args) {
        bengkel = new Bengkel("Bengkel Sinar Motor", "Jl. Mekar Raya No. 12, Kendari", KAPASITAS_RIWAYAT);
        System.out.println("=== SISTEM MANAJEMEN " + bengkel.getNama().toUpperCase() + " ===");

        boolean jalan = true;
        try {
            while (jalan) {
                tampilkanMenu();
                int pilihan = bacaInt("Pilih menu: ");
                System.out.println();
                try {
                    switch (pilihan) {
                        case 1: menuTambahKendaraan(); break;
                        case 2: bengkel.tampilkanSemuaKendaraan(); break;
                        case 3: menuTambahMekanik(); break;
                        case 4: bengkel.tampilkanSemuaMekanik(); break;
                        case 5: menuBuatServis(); break;
                        case 6: menuTambahSukuCadang(); break;
                        case 7: menuSelesaikanServis(); break;
                        case 8: bengkel.tampilkanSemuaServis(); break;
                        case 9: menuCariData(); break;
                        case 10: menuRiwayat(); break;
                        case 11: tampilkanLaporan(); break;
                        case 0:
                            jalan = false;
                            System.out.println("Terima kasih, program selesai.");
                            break;
                        default:
                            System.out.println("[GAGAL] Menu " + pilihan + " tidak tersedia. Pilih angka 0-11.");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    // pelanggaran validasi/aturan bisnis ditampilkan, program tetap jalan
                    System.out.println("[GAGAL] " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            // input habis (misalnya stdin ditutup), keluar dengan rapi
            System.out.println("\nInput berakhir, program dihentikan.");
        }
    }

    private static void tampilkanMenu() {
        System.out.println();
        System.out.println("================ MENU UTAMA ================");
        System.out.println(" 1. Tambah kendaraan");
        System.out.println(" 2. Tampilkan semua kendaraan");
        System.out.println(" 3. Tambah mekanik");
        System.out.println(" 4. Tampilkan semua mekanik");
        System.out.println(" 5. Buat servis baru");
        System.out.println(" 6. Tambah suku cadang ke servis");
        System.out.println(" 7. Selesaikan servis");
        System.out.println(" 8. Tampilkan semua servis");
        System.out.println(" 9. Cari data");
        System.out.println("10. Riwayat servis (Stack)");
        System.out.println("11. Laporan ringkasan");
        System.out.println(" 0. Keluar");
        System.out.println("============================================");
    }

    // ---------- menu-menu ----------

    private static void menuTambahKendaraan() {
        System.out.println("-- Tambah Kendaraan --");
        String plat = bacaString("Plat nomor   : ");
        String merek = bacaString("Merek        : ");
        String model = bacaString("Model        : ");
        int tahun = bacaInt("Tahun        : ");
        String pemilik = bacaString("Nama pemilik : ");
        bengkel.tambahKendaraan(new Kendaraan(plat, merek, model, tahun, pemilik));
        System.out.println("[OK] Kendaraan berhasil ditambahkan.");
    }

    private static void menuTambahMekanik() {
        System.out.println("-- Tambah Mekanik --");
        String id = bacaString("ID mekanik   : ");
        String nama = bacaString("Nama         : ");
        String spesialisasi = bacaString("Spesialisasi : ");
        bengkel.tambahMekanik(new Mekanik(id, nama, spesialisasi));
        System.out.println("[OK] Mekanik berhasil ditambahkan.");
    }

    private static void menuBuatServis() {
        System.out.println("-- Buat Servis Baru --");
        String plat = bacaString("Plat kendaraan : ");
        String idMekanik = bacaString("ID mekanik     : ");
        String keluhan = bacaString("Keluhan        : ");
        double biayaJasa = bacaDouble("Biaya jasa (Rp): ");
        Servis servis = bengkel.buatServis(plat, idMekanik, keluhan, biayaJasa);
        System.out.println("[OK] Servis dibuat dan dicatat ke riwayat.");
        servis.tampilkanInfo();
    }

    private static void menuTambahSukuCadang() {
        System.out.println("-- Tambah Suku Cadang --");
        String kodeServis = bacaString("Kode servis      : ");
        String kode = bacaString("Kode suku cadang : ");
        String nama = bacaString("Nama suku cadang : ");
        double harga = bacaDouble("Harga satuan (Rp): ");
        int jumlah = bacaInt("Jumlah           : ");
        bengkel.tambahSukuCadangKeServis(kodeServis, new SukuCadang(kode, nama, harga, jumlah));
        System.out.println("[OK] Suku cadang ditambahkan ke servis " + kodeServis.toUpperCase() + ".");
    }

    private static void menuSelesaikanServis() {
        System.out.println("-- Selesaikan Servis --");
        String kodeServis = bacaString("Kode servis: ");
        Servis servis = bengkel.selesaikanServis(kodeServis);
        System.out.println("[OK] Servis selesai.");
        servis.tampilkanInfo();
    }

    private static void menuCariData() {
        System.out.println("-- Cari Data --");
        System.out.println("1. Kendaraan (plat)");
        System.out.println("2. Mekanik (nama)");
        System.out.println("3. Servis (kode)");
        int jenis = bacaInt("Pilih jenis pencarian: ");
        switch (jenis) {
            case 1: {
                Kendaraan k = bengkel.cariKendaraan(bacaString("Plat: "));
                if (k == null) {
                    System.out.println("Data kendaraan tidak ditemukan.");
                } else {
                    k.tampilkanInfo();
                }
                break;
            }
            case 2: {
                ArrayList<Mekanik> hasil = bengkel.cariMekanikByNama(bacaString("Nama (boleh sebagian): "));
                if (hasil.isEmpty()) {
                    System.out.println("Data mekanik tidak ditemukan.");
                } else {
                    for (Mekanik m : hasil) {
                        System.out.println("-----");
                        m.tampilkanInfo();
                    }
                }
                break;
            }
            case 3: {
                Servis s = bengkel.cariServis(bacaString("Kode servis: "));
                if (s == null) {
                    System.out.println("Data servis tidak ditemukan.");
                } else {
                    s.tampilkanInfo();
                }
                break;
            }
            default:
                System.out.println("[GAGAL] Jenis pencarian tidak dikenal.");
        }
    }

    /** Submenu untuk operasi ADT Stack. Push terjadi otomatis saat servis dibuat. */
    private static void menuRiwayat() {
        StackRiwayat riwayat = bengkel.getRiwayat();
        boolean kembali = false;
        while (!kembali) {
            System.out.println("---- Riwayat Servis (Stack, kapasitas " + riwayat.getKapasitas() + ") ----");
            System.out.println("1. Tampilkan isi riwayat");
            System.out.println("2. Lihat servis terakhir (peek)");
            System.out.println("3. Batalkan servis terakhir (pop)");
            System.out.println("4. Status stack (size, kosong, penuh)");
            System.out.println("0. Kembali");
            int pilihan = bacaInt("Pilih: ");
            try {
                switch (pilihan) {
                    case 1:
                        riwayat.tampilkan();
                        break;
                    case 2: {
                        Servis teratas = riwayat.peek();
                        if (teratas == null) {
                            System.out.println("Riwayat kosong, tidak ada servis untuk dilihat.");
                        } else {
                            teratas.tampilkanInfo();
                        }
                        break;
                    }
                    case 3: {
                        Servis batal = bengkel.batalkanServisTerakhir();
                        System.out.println("[OK] Servis " + batal.getKode() + " dibatalkan dan dikeluarkan dari riwayat.");
                        break;
                    }
                    case 4:
                        System.out.println("Ukuran   : " + riwayat.size() + "/" + riwayat.getKapasitas());
                        System.out.println("Kosong?  : " + (riwayat.isEmpty() ? "ya" : "tidak"));
                        System.out.println("Penuh?   : " + (riwayat.isFull() ? "ya" : "tidak"));
                        break;
                    case 0:
                        kembali = true;
                        break;
                    default:
                        System.out.println("[GAGAL] Pilihan tidak tersedia.");
                }
            } catch (IllegalStateException e) {
                System.out.println("[GAGAL] " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void tampilkanLaporan() {
        System.out.println("========= LAPORAN RINGKASAN =========");
        System.out.println("Bengkel          : " + bengkel.getNama());
        System.out.println("Alamat           : " + bengkel.getAlamat());
        System.out.println("Jumlah kendaraan : " + bengkel.getDaftarKendaraan().size());
        System.out.println("Jumlah mekanik   : " + bengkel.getDaftarMekanik().size());
        System.out.println("Servis aktif     : " + bengkel.hitungServisAktif());
        System.out.println("Servis selesai   : " + bengkel.hitungServisSelesai());
        System.out.println("Servis dibatalkan: " + bengkel.hitungServisDibatalkan());
        System.out.println("Isi riwayat      : " + bengkel.getRiwayat().size() + "/" + bengkel.getRiwayat().getKapasitas());
        System.out.println("Total pendapatan : " + String.format("Rp %,.0f", bengkel.hitungTotalPendapatan()));
        System.out.println("=====================================");
    }

    // ---------- pembaca input yang aman ----------

    /** Membaca teks, mengulang sampai tidak kosong. */
    private static String bacaString(String pesan) {
        while (true) {
            System.out.print(pesan);
            String teks = INPUT.nextLine().trim();
            if (!teks.isEmpty()) {
                return teks;
            }
            System.out.println("[INPUT SALAH] Input tidak boleh kosong, coba lagi.");
        }
    }

    /** Membaca bilangan bulat, mengulang kalau yang dimasukkan bukan angka. */
    private static int bacaInt(String pesan) {
        while (true) {
            String teks = bacaString(pesan);
            try {
                return Integer.parseInt(teks);
            } catch (NumberFormatException e) {
                System.out.println("[INPUT SALAH] Harus berupa angka bulat, coba lagi.");
            }
        }
    }

    /** Membaca bilangan desimal (tanpa pemisah ribuan), koma dianggap titik desimal. */
    private static double bacaDouble(String pesan) {
        while (true) {
            String teks = bacaString(pesan).replace(',', '.');
            try {
                double nilai = Double.parseDouble(teks);
                if (Double.isNaN(nilai) || Double.isInfinite(nilai)) {
                    throw new NumberFormatException();
                }
                return nilai;
            } catch (NumberFormatException e) {
                System.out.println("[INPUT SALAH] Harus berupa angka (contoh: 150000), coba lagi.");
            }
        }
    }
}
