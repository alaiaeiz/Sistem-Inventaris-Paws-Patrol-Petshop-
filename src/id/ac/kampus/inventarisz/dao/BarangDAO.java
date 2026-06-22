package id.ac.kampus.inventarisz.dao;

import id.ac.kampus.inventarisz.entity.Barang;
import java.util.List;

public interface BarangDAO {
    void insert(Barang barang);
    List<Barang> findAll();
    void update(Barang barang);
    void delete(int id);
    
    List<Barang> searchByNama(String keyword); 
    List<Barang> findExpiredBarang();          
    Barang findById(int id);                   
}