package adt;

import model.Servis;

/**
 * ADT Stack (LIFO) untuk riwayat servis terakhir di bengkel.
 * Dipakai untuk fitur "batalkan servis terakhir".
 * Datanya disimpan di array berkapasitas tetap; atribut internal private,
 * dan satu-satunya akses lewat operasi publik di bawah.
 */
public class StackRiwayat {
    public static final int KAPASITAS_DEFAULT = 5;

    private Servis[] data;
    private int top;        // indeks elemen teratas, -1 artinya kosong
    private int kapasitas;

    /** Konstruktor default dengan kapasitas bawaan. */
    public StackRiwayat() {
        this(KAPASITAS_DEFAULT);
    }

    /** Konstruktor berparameter. Kapasitas harus lebih dari 0. */
    public StackRiwayat(int kapasitas) {
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas stack harus lebih dari 0.");
        }
        this.kapasitas = kapasitas;
        this.data = new Servis[kapasitas];
        this.top = -1;
    }

    /**
     * Menaruh servis di puncak stack.
     * @return true jika berhasil, false jika stack penuh atau servis null
     */
    public boolean push(Servis servis) {
        if (servis == null || isFull()) {
            return false;
        }
        top++;
        data[top] = servis;
        return true;
    }

    /**
     * Mengambil dan menghapus servis di puncak stack.
     * @return servis teratas, atau null jika stack kosong
     */
    public Servis pop() {
        if (isEmpty()) {
            return null;
        }
        Servis hasil = data[top];
        data[top] = null; // lepas referensi supaya tidak menggantung
        top--;
        return hasil;
    }

    /** Melihat servis teratas tanpa menghapusnya. Null jika kosong. */
    public Servis peek() {
        if (isEmpty()) {
            return null;
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == kapasitas - 1;
    }

    public int size() {
        return top + 1;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    /** Menampilkan isi stack dari yang terbaru (puncak) sampai yang terlama. */
    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("Riwayat servis masih kosong.");
            return;
        }
        System.out.println("Isi riwayat (atas = terbaru), terisi " + size() + "/" + kapasitas + ":");
        for (int i = top; i >= 0; i--) {
            Servis s = data[i];
            String label = (i == top) ? "TOP" : String.valueOf(i + 1);
            System.out.println("  [" + label + "] " + s.getKode() + " | "
                    + s.getKendaraan().getPlat() + " | " + s.getStatus());
        }
    }
}
