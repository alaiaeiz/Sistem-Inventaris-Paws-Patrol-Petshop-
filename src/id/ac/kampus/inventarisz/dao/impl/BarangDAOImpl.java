package id.ac.kampus.inventarisz.dao.impl;

import id.ac.kampus.inventarisz.config.DatabaseConfig;
import id.ac.kampus.inventarisz.dao.BarangDAO;
import id.ac.kampus.inventarisz.entity.Barang;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BarangDAOImpl implements BarangDAO {

    @Override
    public void insert(Barang barang) {
        String sql = "INSERT INTO barang (kode_barang, nama_barang, stok, harga, tanggal_kedaluwarsa) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, barang.getKodeBarang());
            stmt.setString(2, barang.getNamaBarang());
            stmt.setInt(3, barang.getStok());
            stmt.setDouble(4, barang.getHarga());
            
            if (barang.getTanggalKedaluwarsa() != null) {
                stmt.setDate(5, Date.valueOf(barang.getTanggalKedaluwarsa()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            
            stmt.executeUpdate();
            System.out.println("Data barang berhasil ditambahkan ke database!");
            
        } catch (SQLException e) {
            System.err.println("Gagal menambah data: " + e.getMessage());
        }
    }

    @Override
    public List<Barang> findAll() {
        List<Barang> listBarang = new ArrayList<>();
        String sql = "SELECT * FROM barang";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                listBarang.add(mapResultSetToBarang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data: " + e.getMessage());
        }
        return listBarang;
    }

    @Override
    public void update(Barang barang) {
        String sql = "UPDATE barang SET kode_barang = ?, nama_barang = ?, stok = ?, harga = ?, tanggal_kedaluwarsa = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, barang.getKodeBarang());
            stmt.setString(2, barang.getNamaBarang());
            stmt.setInt(3, barang.getStok());
            stmt.setDouble(4, barang.getHarga());
            
            if (barang.getTanggalKedaluwarsa() != null) {
                stmt.setDate(5, Date.valueOf(barang.getTanggalKedaluwarsa()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            stmt.setInt(6, barang.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Data barang berhasil diperbarui!");
            } else {
                System.out.println("Barang dengan ID tersebut tidak ditemukan.");
            }
            
        } catch (SQLException e) {
            System.err.println("Gagal memperbarui data: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM barang WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Data barang berhasil dihapus!");
        } catch (SQLException e) {
            System.err.println("Gagal menghapus data: " + e.getMessage());
        }
    }

    @Override
    public List<Barang> searchByNama(String keyword) {
        List<Barang> listBarang = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE nama_barang LIKE ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    listBarang.add(mapResultSetToBarang(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari data: " + e.getMessage());
        }
        return listBarang;
    }

    @Override
    public List<Barang> findExpiredBarang() {
        List<Barang> listBarang = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE tanggal_kedaluwarsa IS NOT NULL AND tanggal_kedaluwarsa <= CURRENT_DATE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                listBarang.add(mapResultSetToBarang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data kedaluwarsa: " + e.getMessage());
        }
        return listBarang;
    }

    @Override
    public Barang findById(int id) {
        String sql = "SELECT * FROM barang WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBarang(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data barang: " + e.getMessage());
        }
        return null;
    }

    private Barang mapResultSetToBarang(ResultSet rs) throws SQLException {
        Date sqlDate = rs.getDate("tanggal_kedaluwarsa");
        LocalDate expiredDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
        
        return new Barang(
            rs.getInt("id"),
            rs.getString("kode_barang"),
            rs.getString("nama_barang"),
            rs.getInt("stok"),
            rs.getDouble("harga"),
            expiredDate
        );
    }
}