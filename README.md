# UTS PBO - Sistem Manajemen Bengkel

Aplikasi console Java untuk mengelola bengkel mini.

- Nama  : Muh. Sergio Halim
- NIM   : 22515066
- Kelas : III/B (Sistem dan Teknologi Informasi)
- Topik : Bengkel (digit terakhir NIM = 6)
- ADT   : Stack (digit kedua dari belakang = 6, 6 mod 3 = 0)

## Struktur

```
src/
  app/Main.java              menu console
  adt/StackRiwayat.java      ADT Stack riwayat servis
  model/Kendaraan.java
  model/Mekanik.java
  model/SukuCadang.java
  model/Servis.java
  model/Bengkel.java
diagram/
  class-diagram.drawio       bisa dibuka dan diedit di draw.io
  class-diagram.svg
```

## Cara menjalankan

```
javac -d out src/adt/*.java src/model/*.java src/app/*.java
java -cp out app.Main
```

## Aturan bisnis

- Satu mekanik hanya boleh menangani satu servis aktif.
- Biaya servis = biaya jasa + total harga suku cadang.
- Kendaraan yang masih diservis tidak bisa masuk servis lagi.
- Riwayat servis (Stack) berkapasitas 5; kalau penuh, servis baru ditolak sampai ada yang dibatalkan.
