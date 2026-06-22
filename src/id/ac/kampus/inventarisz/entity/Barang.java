package id.ac.kampus.inventarisz.entity;

import java.time.LocalDate;

public class Barang {
    
    private int id;
    private String kodeBarang;
    private String namaBarang;
    private int stok;
    private double harga;
    private LocalDate tanggalKedaluwarsa; 

    public Barang() {
    }

    public Barang(String kodeBarang, String namaBarang, int stok, double harga, LocalDate tanggalKedaluwarsa) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.stok = stok;
        this.harga = harga;
        this.tanggalKedaluwarsa = tanggalKedaluwarsa;
    }

    public Barang(int id, String kodeBarang, String namaBarang, int stok, double harga, LocalDate tanggalKedaluwarsa) {
        this.id = id;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.stok = stok;
        this.harga = harga;
        this.tanggalKedaluwarsa = tanggalKedaluwarsa;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh kurang dari 0!");
        }
        this.stok = stok;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public LocalDate getTanggalKedaluwarsa() {
        return tanggalKedaluwarsa;
    }

    public void setTanggalKedaluwarsa(LocalDate tanggalKedaluwarsa) {
        this.tanggalKedaluwarsa = tanggalKedaluwarsa;
    }

    @Override
    public String toString() {
        return String.format("Barang [ID=%d, Kode=%s, Nama=%s, Stok=%d, Harga=Rp%.2f, Expired=%s]", 
                id, kodeBarang, namaBarang, stok, harga, 
                (tanggalKedaluwarsa != null ? tanggalKedaluwarsa.toString() : "Tidak ada"));
    }
}