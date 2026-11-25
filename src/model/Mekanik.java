package model;

/**
 * Mekanik bengkel. Aturan utama: satu mekanik hanya boleh menangani
 * satu servis aktif pada satu waktu (dijaga lewat atribut sibuk).
 */
public class Mekanik {
    private String id;
    private String nama;
    private String spesialisasi;
    private boolean sibuk;
    private int jumlahServisSelesai;

    /** Konstruktor default. */
    public Mekanik() {
        this.id = "-";
        this.nama = "-";
        this.spesialisasi = "Umum";
        this.sibuk = false;
        this.jumlahServisSelesai = 0;
    }

    /** Konstruktor berparameter. Mekanik baru selalu tersedia dan belum punya riwayat servis. */
    public Mekanik(String id, String nama, String spesialisasi) {
        this.setId(id);
        this.setNama(nama);
        this.setSpesialisasi(spesialisasi);
        this.sibuk = false;
        this.jumlahServisSelesai = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID mekanik tidak boleh kosong.");
        }
        this.id = id.trim().toUpperCase();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama mekanik tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        if (spesialisasi == null || spesialisasi.trim().isEmpty()) {
            throw new IllegalArgumentException("Spesialisasi mekanik tidak boleh kosong.");
        }
        this.spesialisasi = spesialisasi.trim();
    }

    public boolean isSibuk() {
        return sibuk;
    }

    public int getJumlahServisSelesai() {
        return jumlahServisSelesai;
    }

    public void setJumlahServisSelesai(int jumlahServisSelesai) {
        if (jumlahServisSelesai < 0) {
            throw new IllegalArgumentException("Jumlah servis selesai tidak boleh negatif.");
        }
        this.jumlahServisSelesai = jumlahServisSelesai;
    }

    // ---------- perilaku ----------

    /** Mekanik mulai menangani servis. Ditolak jika masih ada servis aktif. */
    public void mulaiTugas() {
        if (sibuk) {
            throw new IllegalStateException("Mekanik " + nama + " masih menangani satu servis aktif.");
        }
        sibuk = true;
    }

    /** Servis selesai: mekanik kembali tersedia dan jumlah servis selesainya bertambah. */
    public void selesaiTugas() {
        sibuk = false;
        jumlahServisSelesai++;
    }

    /** Servis dibatalkan saat masih aktif: mekanik bebas lagi, tanpa menambah hitungan selesai. */
    public void batalkanTugas() {
        sibuk = false;
    }

    /** Dipakai saat servis yang sudah selesai dibatalkan, supaya hitungan selesai tetap benar. */
    public void koreksiServisSelesai() {
        if (jumlahServisSelesai > 0) {
            jumlahServisSelesai--;
        }
    }

    public void tampilkanInfo() {
        System.out.println("ID       : " + id);
        System.out.println("Nama     : " + nama);
        System.out.println("Keahlian : " + spesialisasi);
        System.out.println("Status   : " + (sibuk ? "Sibuk (ada servis aktif)" : "Tersedia"));
        System.out.println("Servis selesai: " + jumlahServisSelesai);
    }
}
