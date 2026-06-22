package id.ac.kampus.inventarisz.main;

import id.ac.kampus.inventarisz.dao.BarangDAO;
import id.ac.kampus.inventarisz.dao.impl.BarangDAOImpl;
import id.ac.kampus.inventarisz.entity.Barang;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Application {
    
    private static final BarangDAO barangDAO = new BarangDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);

    static class ItemKeranjang {
        Barang barang;
        int jumlahBeli;
        double subtotal;

        public ItemKeranjang(Barang barang, int jumlahBeli) {
            this.barang = barang;
            this.jumlahBeli = jumlahBeli;
            this.subtotal = barang.getHarga() * jumlahBeli;
        }
    }

    public static void main(String[] args) {
        boolean isRunning = true;



        while (isRunning) {
            clearScreen();
            System.out.println("======================================================");
            System.out.println("            SISTEM INVENTARIS PAWS PATROL             ");
            System.out.println("======================================================");
            System.out.println("\n--- MENU UTAMA ---");
            System.out.println("1. Tambah Stok Kebutuhan Anabul");
            System.out.println("2. Cek Etalase Barang Paws Patrol");
            System.out.println("3. Perbarui Data Makanan/Aksesoris");
            System.out.println("4. Hapus Produk Dari Etalase");
            System.out.println("5. Cari Kebutuhan Anabul (Berdasarkan Nama)");
            System.out.println("6. Laporan Barang Kedaluwarsa");
            System.out.println("7. Transaksi Penjualan");
            System.out.println("8. Tutup Toko");
            System.out.print("Pilih menu (1-8): ");

            int pilihan = -1;
            try {
                pilihan = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Harap masukkan angka yang valid!");
                scanner.nextLine();
                tekanEnterUntukLanjut();
                continue;
            }

            switch (pilihan) {
                case 1: menuTambahBarang(); tekanEnterUntukLanjut(); break;
                case 2: menuLihatBarang(); tekanEnterUntukLanjut(); break;
                case 3: menuUbahBarang(); tekanEnterUntukLanjut(); break;
                case 4: menuHapusBarang(); tekanEnterUntukLanjut(); break;
                case 5: menuCariBarang(); tekanEnterUntukLanjut(); break;
                case 6: menuLaporanKedaluwarsa(); tekanEnterUntukLanjut(); break;
                case 7: menuKasir(); tekanEnterUntukLanjut(); break;
                case 8:
                    System.out.println("Terima kasih atas kerja kerasnya hari ini, Paw-rents!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Pilihan tidak tersedia. Silakan pilih 1-8.");
                    tekanEnterUntukLanjut();
            }
        }
        scanner.close();
    }

    private static LocalDate ambilInputTanggal() {
        System.out.print("Masukkan Tanggal Kedaluwarsa (YYYY-MM-DD): ");
        String tgl = scanner.nextLine();
        if (tgl.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(tgl);
        } catch (DateTimeParseException e) {
            System.out.println("Format tanggal salah! Harap gunakan format YYYY-MM-DD.");
            return null;
        }
    }

    private static void menuTambahBarang() {
        System.out.println("\nTAMBAH KEBUTUHAN ANABUL");
        System.out.print("Kode Barang (Contoh: KUC001): ");
        String kode = scanner.nextLine();
        System.out.print("Nama Produk: ");
        String nama = scanner.nextLine();
        
        int stok = -1;
        while (stok < 0) {
            System.out.print("Jumlah Stok (>= 0): ");
            stok = scanner.nextInt();
            if (stok < 0) System.out.println("Stok tidak boleh minus!");
        }
        System.out.print("Harga (Rp): ");
        double harga = scanner.nextDouble();
        scanner.nextLine();

        LocalDate tglExpired = ambilInputTanggal();

        Barang barangBaru = new Barang(kode, nama, stok, harga, tglExpired);
        barangDAO.insert(barangBaru);
    }

    private static void cetakTabelBarang(List<Barang> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("Data barang tidak ditemukan atau kosong.");
            return;
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-12s | %-25s | %-8s | %-12s | %-15s\n", "ID", "KODE", "NAMA BARANG", "STOK", "HARGA", "EXPIRED DATE");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for (Barang b : daftar) {
            String tgl = (b.getTanggalKedaluwarsa() != null) ? b.getTanggalKedaluwarsa().toString() : "-";
            System.out.printf("%-5d | %-12s | %-25s | %-8d | Rp%-10.2f | %-15s\n", 
                    b.getId(), b.getKodeBarang(), b.getNamaBarang(), b.getStok(), b.getHarga(), tgl);
        }
        System.out.println("-------------------------------------------------------------------------------------------------");
    }

    private static void menuLihatBarang() {
        System.out.println("\nDAFTAR KEBUTUHAN ANABUL DI ETALASE PAWS PATROL");
        cetakTabelBarang(barangDAO.findAll());
    }

    private static void menuUbahBarang() {
        System.out.println("\nUBAH DATA KEBUTUHAN ANABUL");
        menuLihatBarang();
        System.out.print("\nMasukkan ID yang ingin diubah: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Kode Barang Baru : "); String kode = scanner.nextLine();
        System.out.print("Nama Barang Baru : "); String nama = scanner.nextLine();
        System.out.print("Stok Baru        : "); int stok = scanner.nextInt();
        System.out.print("Harga Baru (Rp)  : "); double harga = scanner.nextDouble();
        scanner.nextLine();
        
        LocalDate tglExpired = ambilInputTanggal();

        Barang barangUpdate = new Barang(id, kode, nama, stok, harga, tglExpired);
        barangDAO.update(barangUpdate);
    }

    private static void menuHapusBarang() {
        System.out.println("\nHAPUS DATA KEBUTUHAN ANABUL");
        menuLihatBarang();
        System.out.print("\nMasukkan ID yang ingin dihapus: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Yakin ingin menghapus ID " + id + "? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            barangDAO.delete(id);
        }
    }

    private static void menuCariBarang() {
        System.out.println("\nPENCARIAN KEBUTUHAN ANABUL BERDASARKAN NAMA");
        System.out.print("Masukkan kata kunci pencarian: ");
        String keyword = scanner.nextLine();
        cetakTabelBarang(barangDAO.searchByNama(keyword));
    }

    private static void menuLaporanKedaluwarsa() {
        System.out.println("\nLAPORAN KEBUTUHAN ANABUL YANG KEDALUWARSA");
        System.out.println("Tanggal Hari Ini: " + LocalDate.now());
        cetakTabelBarang(barangDAO.findExpiredBarang());
    }

    private static void menuKasir() {
        System.out.println("\n======================================================");
        System.out.println("                    KASIR PAWS PATROL                ");
        System.out.println("======================================================");
        
        List<ItemKeranjang> keranjang = new ArrayList<>();
        boolean tambahLagi = true;
        double grandTotal = 0;

        while (tambahLagi) {
            System.out.print("\nMasukkan ID Barang (atau ketik 0 untuk Selesai): ");
            int idBarang = scanner.nextInt();
            if (idBarang == 0) break;

            Barang barang = barangDAO.findById(idBarang);
            if (barang == null) {
                System.out.println("Barang tidak ditemukan!");
                continue;
            }

            System.out.println("Terpilih: " + barang.getNamaBarang() + " (Stok tersisa: " + barang.getStok() + " | Harga: Rp" + barang.getHarga() + ")");
            System.out.print("Masukkan Jumlah Beli: ");
            int qty = scanner.nextInt();

            if (qty > barang.getStok()) {
                System.out.println("Gagal! Stok tidak mencukupi.");
            } else if (qty > 0) {
                ItemKeranjang item = new ItemKeranjang(barang, qty);
                keranjang.add(item);
                grandTotal += item.subtotal;
                System.out.println("✅ " + qty + " " + barang.getNamaBarang() + " masuk ke keranjang.");
            }
        }

        if (keranjang.isEmpty()) {
            System.out.println("Transaksi dibatalkan (Keranjang kosong).");
            scanner.nextLine();
            return;
        }

        System.out.println("\n--- TOTAL BELANJA ---");
        System.out.printf("Total yang harus dibayar: Rp%.2f\n", grandTotal);
        
        double uangTunai = 0;
        while (uangTunai < grandTotal) {
            System.out.print("Masukkan Uang Tunai Pembeli: Rp");
            uangTunai = scanner.nextDouble();
            if (uangTunai < grandTotal) {
                System.out.println("Uang tunai kurang!");
            }
        }
        scanner.nextLine();

        double kembalian = uangTunai - grandTotal;
        System.out.printf("Kembalian: Rp%.2f\n", kembalian);

        for (ItemKeranjang item : keranjang) {
            Barang b = item.barang;
            b.setStok(b.getStok() - item.jumlahBeli);
            barangDAO.update(b);
        }
        System.out.println("Transaksi sukses! Stok barang di database telah otomatis diperbarui.");

        cetakStrukKeFile(keranjang, grandTotal, uangTunai, kembalian);
    }

    private static void cetakStrukKeFile(List<ItemKeranjang> keranjang, double total, double tunai, double kembalian) {
        String namaFile = "struk_belanja.txt";
        
        // Menggunakan Try-With-Resources untuk File Writer
        try (FileWriter writer = new FileWriter(namaFile)) {
            writer.write("========================================\n");
            writer.write("          PETSHOP PAWS PATROL           \n");
            writer.write("========================================\n");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            writer.write("Tanggal : " + LocalDateTime.now().format(formatter) + "\n");
            writer.write("----------------------------------------\n");
            
            for (ItemKeranjang item : keranjang) {
                writer.write(String.format("%-20s x%-3d Rp%.0f\n", 
                        item.barang.getNamaBarang(), item.jumlahBeli, item.subtotal));
            }
            
            writer.write("----------------------------------------\n");
            writer.write(String.format("TOTAL BELANJA : Rp%.0f\n", total));
            writer.write(String.format("TUNAI         : Rp%.0f\n", tunai));
            writer.write(String.format("KEMBALIAN     : Rp%.0f\n", kembalian));
            writer.write("========================================\n");
            writer.write("      Terima kasih atas kunjungan Anda  \n");
            
            System.out.println("✅ Struk berhasil dicetak ke file: " + namaFile);
            
        } catch (IOException e) {
            System.err.println("❌ Gagal mencetak struk file: " + e.getMessage());
        }
    }

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ex) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    private static void tekanEnterUntukLanjut() {
        System.out.println("\nTekan Enter untuk kembali ke menu utama!");
        scanner.nextLine();
    }
}