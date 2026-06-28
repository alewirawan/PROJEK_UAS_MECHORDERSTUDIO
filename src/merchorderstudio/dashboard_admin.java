/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package merchorderstudio;
import javax.swing.JTable;
import java.text.MessageFormat;
import java.awt.CardLayout;
import java.awt.Image;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import config.koneksi;

/**
 *
 * @author Ratih Nawang Wulan
 */
public class dashboard_admin extends javax.swing.JFrame {
    
    private final CardLayout card;
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(dashboard_admin.class.getName());

    /**
     * Creates new form dashboard_admin
     */
    public dashboard_admin() {
        initComponents();
        setLocationRelativeTo(null);
        
        card = new CardLayout();
    jPanel2.setLayout(card);

    jPanel2.add(HalDashboard, "dashboard");
    jPanel2.add(HalPesanan, "pesanan");
    jPanel2.add(HalProduk, "produk");
    jPanel2.add(HalPembayaran, "pembayaran");
    jPanel2.add(HalProduksi, "produksi");
    jPanel2.add(HalPelanggan, "pelanggan");
    jPanel2.add(HalLaporan, "Laporan");
    jPanel2.add(HalPengeluaran, "pengeluaran");
    
    card.show(jPanel2, "dashboard");
    
    tampilJumlahProduk();
    tampilJumlahPesanan();
    DataPesanan();
    DataProduk();
    setupTableRenderer();
    DataPembayaran();
    DataProduksi();
    DataPelanggan();
    DataLaporan();
    TotalPemasukan();
    }
    
    private void TotalPemasukan(){

    try{

        int bulan = cmbBulan.getSelectedIndex()+1;

        int tahun = Integer.parseInt(
            cmbTahun.getSelectedItem().toString()
        );

        String sql =
        "SELECT SUM(p.total_harga) AS total " +
        "FROM pesanan p " +
        "JOIN pembayaran pb " +
        "ON p.id_pesanan = pb.id_pesanan " +
        "WHERE pb.status_bayar='lunas' " +
        "AND MONTH(p.tanggal_pesan)=? " +
        "AND YEAR(p.tanggal_pesan)=?";

        Connection conn =
        koneksi.getConnection();

        PreparedStatement pst =
        conn.prepareStatement(sql);

        pst.setInt(1, bulan);
        pst.setInt(2, tahun);

        ResultSet rs = pst.executeQuery();

        if(rs.next()){

            lblTotalPemasukan.setText(
            "Total Pemasukan : Rp " +
            String.format("%,.0f",
            rs.getDouble("total")));

        }

    }catch(Exception e){

        JOptionPane.showMessageDialog(null,
        e.getMessage());

    }

}
    
    // Tampilkan
    private void tampilJumlahProduk() {

    try {

        Connection conn = koneksi.getConnection();

        String sql = "SELECT COUNT(*) AS total_produk FROM produk";

        PreparedStatement pst = conn.prepareStatement(sql);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {

            Produk.setText(
                String.valueOf(
                    rs.getInt("total_produk")
                )
            );

        }

        rs.close();
        pst.close();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            null,
            "Gagal menampilkan jumlah produk : "
            + e.getMessage()
        );

    }

}
    
    private void tampilJumlahPesanan() {

    try {

        Connection conn = koneksi.getConnection();

        String sql = "SELECT COUNT(*) AS total_pesanan FROM pesanan";

        PreparedStatement pst = conn.prepareStatement(sql);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {

            Pesanan.setText(
                String.valueOf(
                    rs.getInt("total_pesanan")
                )
            );

        }

        rs.close();
        pst.close();

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
            null,
            "Gagal menampilkan jumlah pesanan : "
            + e.getMessage()
        );

    }

}
    
    private void setupTableRenderer() {

    TabelProduk.setRowHeight(70);

    TabelProduk.getColumnModel().getColumn(1)
    .setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            if (value instanceof javax.swing.ImageIcon icon) {

                javax.swing.JLabel label = new javax.swing.JLabel();
                label.setIcon(icon);
                label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

                return label;
            }

            return super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
        }
    });
}
    
    // Menampilkan Data Tabel
    private void DataPesanan() {

    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("ID Pesanan");
    model.addColumn("Pelanggan");
    model.addColumn("Produk");
    model.addColumn("Jumlah");
    model.addColumn("Total Harga");
    model.addColumn("Status");
    model.addColumn("Tanggal");

    try {

        String keyword = caripesanan.getText();

        String sql =
        "SELECT p.id_pesanan, u.nama, pr.nama_produk, dp.jumlah, " +
        "p.total_harga, p.status_pesanan, p.tanggal_pesan " +
        "FROM pesanan p " +
        "INNER JOIN users u ON p.id_user = u.id_user " +
        "INNER JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan " +
        "INNER JOIN produk pr ON dp.id_produk = pr.id_produk " +
        "WHERE p.id_pesanan LIKE ? " +
        "OR u.nama LIKE ? " +
        "OR pr.nama_produk LIKE ?";

        Connection conn = koneksi.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");
        pst.setString(3, "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {

            model.addRow(new Object[]{
                rs.getInt("id_pesanan"),
                rs.getString("nama"),
                rs.getString("nama_produk"),
                rs.getInt("jumlah"),
                rs.getDouble("total_harga"),
                rs.getString("status_pesanan"),
                rs.getDate("tanggal_pesan")
            });
        }

        TabelPesanan.setModel(model);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    
    private void DataProduk() {

    try {

        Connection conn = koneksi.getConnection();

        String cari = cariproduk.getText().trim();

        String kategori =
                cmbKategoriProduk
                .getSelectedItem()
                .toString();

        String sql =
        "SELECT * FROM produk " +
        "WHERE nama_produk LIKE ? ";

        // Jika bukan Semua Kategori
        if(!kategori.equalsIgnoreCase("Semua Kategori")){

            sql += "AND kategori = ? ";

        }

        PreparedStatement pst =
        conn.prepareStatement(sql);

        pst.setString(
                1,
                "%" + cari + "%"
        );

        if(!kategori.equalsIgnoreCase("Semua Kategori")){

            pst.setString(
                    2,
                    kategori
            );

        }

        ResultSet rs = pst.executeQuery();

        DefaultTableModel model =
        (DefaultTableModel)
        TabelProduk.getModel();

        model.setRowCount(0);

        while(rs.next()){

            int id =
            rs.getInt("id_produk");

            String nama =
            rs.getString("nama_produk");

            String kategoriProduk =
            rs.getString("kategori");

            double harga =
            rs.getDouble("harga");

            int stok =
            rs.getInt("stok");

            ImageIcon imageIcon = null;

            byte[] imageData =
            rs.getBytes("foto_produk");

            if(imageData != null){

                ImageIcon icon =
                new ImageIcon(imageData);

                Image img =
                icon.getImage()
                .getScaledInstance(
                        80,
                        80,
                        Image.SCALE_SMOOTH
                );

                imageIcon =
                new ImageIcon(img);

            }

            model.addRow(new Object[]{
                id,
                imageIcon,
                nama,
                kategoriProduk,
                harga,
                stok
            });

        }

        TabelProduk.setRowHeight(80);

        TabelProduk.getColumnModel()
        .getColumn(0)
        .setMinWidth(0);

        TabelProduk.getColumnModel()
        .getColumn(0)
        .setMaxWidth(0);

        TabelProduk.getColumnModel()
        .getColumn(0)
        .setPreferredWidth(0);

        rs.close();
        pst.close();
        conn.close();

    } catch(Exception e){

        JOptionPane.showMessageDialog(
                null,
                e.getMessage()
        );

    }

}
    
    private void DataPembayaran() {

    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("ID Pembayaran");
    model.addColumn("ID Pesanan");
    model.addColumn("Metode");
    model.addColumn("Total");
    model.addColumn("Status");
    model.addColumn("Tanggal");

    try {

        String keyword = caripemasukan.getText();

        String sql =
        "SELECT pb.id_pembayaran, pb.id_pesanan, pb.metode_bayar, " +
        "p.total_harga, pb.status_bayar, pb.tanggal_bayar " +
        "FROM pembayaran pb " +
        "INNER JOIN pesanan p ON pb.id_pesanan = p.id_pesanan " +
        "WHERE CAST(pb.id_pembayaran AS CHAR) LIKE ? " +
        "OR CAST(pb.id_pesanan AS CHAR) LIKE ? " +
        "OR pb.metode_bayar LIKE ? " +
        "OR pb.status_bayar LIKE ?";

        Connection conn = koneksi.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);

        for(int i=1; i<=4; i++){
            pst.setString(i, "%" + keyword + "%");
        }

        ResultSet rs = pst.executeQuery();

        while(rs.next()) {

            model.addRow(new Object[]{
                rs.getInt("id_pembayaran"),
                rs.getInt("id_pesanan"),
                rs.getString("metode_bayar"),
                rs.getDouble("total_harga"),
                rs.getString("status_bayar"),
                rs.getDate("tanggal_bayar")
            });

        }

        TabelPemasukan.setModel(model);

    } catch(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    
    private void DataProduksi() {

    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("ID Pesanan");
    model.addColumn("Pelanggan");
    model.addColumn("Produk");
    model.addColumn("Jumlah");
    model.addColumn("Desain");
    model.addColumn("Catatan");
    model.addColumn("Status");

    try {

        String keyword = cariproduksi.getText();

        String sql =
        "SELECT p.id_pesanan, u.nama, pr.nama_produk, dp.jumlah, " +
        "dp.upload_desain, dp.catatan, p.status_pesanan " +
        "FROM pesanan p " +
        "INNER JOIN users u ON p.id_user = u.id_user " +
        "INNER JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan " +
        "INNER JOIN produk pr ON dp.id_produk = pr.id_produk " +
        "INNER JOIN pembayaran pb ON p.id_pesanan = pb.id_pesanan " +
        "WHERE pb.status_bayar='lunas' " +
        "AND (CAST(p.id_pesanan AS CHAR) LIKE ? " +
        "OR u.nama LIKE ? " +
        "OR pr.nama_produk LIKE ? " +
        "OR p.status_pesanan LIKE ?)";

        Connection conn = koneksi.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);

        for(int i = 1; i <= 4; i++){
            pst.setString(i, "%" + keyword + "%");
        }

        ResultSet rs = pst.executeQuery();

        while(rs.next()) {

            model.addRow(new Object[]{
                rs.getInt("id_pesanan"),
                rs.getString("nama"),
                rs.getString("nama_produk"),
                rs.getInt("jumlah"),
                rs.getString("upload_desain"),
                rs.getString("catatan"),
                rs.getString("status_pesanan")
            });

        }

        TabelProduksi.setModel(model);

    } catch(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    
   private void DataPelanggan() {

    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("ID User");
    model.addColumn("Nama");
    model.addColumn("Email");
    model.addColumn("No Telepon");
    model.addColumn("Alamat");

    try {

        String keyword = caripelanggan.getText();

        String sql =
        "SELECT * FROM users " +
        "WHERE CAST(id_user AS CHAR) LIKE ? " +
        "OR nama LIKE ? " +
        "OR email LIKE ? " +
        "OR no_telp LIKE ?";

        Connection conn = koneksi.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);

        for(int i=1; i<=4; i++){
            pst.setString(i, "%" + keyword + "%");
        }

        ResultSet rs = pst.executeQuery();

        while(rs.next()) {

            model.addRow(new Object[]{
                rs.getInt("id_user"),
                rs.getString("nama"),
                rs.getString("email"),
                rs.getString("no_telp"),
                rs.getString("alamat")
            });

        }

        TabelPelanggan.setModel(model);

    } catch(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
   
   private void DataLaporan() {

    DefaultTableModel model = new DefaultTableModel();

    model.addColumn("ID Pesanan");
    model.addColumn("Pelanggan");
    model.addColumn("Produk");
    model.addColumn("Jumlah");
    model.addColumn("Total Harga");
    model.addColumn("Tanggal");

    double totalPemasukan = 0;

    try {

        String keyword = carilaporan.getText();

        int bulan = cmbBulan.getSelectedIndex() + 1;
        int tahun = Integer.parseInt(
                cmbTahun.getSelectedItem().toString()
        );

        String sql =
        "SELECT p.id_pesanan, u.nama, pr.nama_produk, " +
        "dp.jumlah, p.total_harga, p.tanggal_pesan " +
        "FROM pesanan p " +
        "INNER JOIN users u ON p.id_user = u.id_user " +
        "INNER JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan " +
        "INNER JOIN produk pr ON dp.id_produk = pr.id_produk " +
        "INNER JOIN pembayaran pb ON p.id_pesanan = pb.id_pesanan " +
        "WHERE pb.status_bayar='lunas' " +
        "AND MONTH(p.tanggal_pesan)=? " +
        "AND YEAR(p.tanggal_pesan)=? " +
        "AND (CAST(p.id_pesanan AS CHAR) LIKE ? " +
        "OR u.nama LIKE ? " +
        "OR pr.nama_produk LIKE ?)";

        Connection conn = koneksi.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setInt(1, bulan);
        pst.setInt(2, tahun);

        pst.setString(3, "%" + keyword + "%");
        pst.setString(4, "%" + keyword + "%");
        pst.setString(5, "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();

        while(rs.next()) {

            double total = rs.getDouble("total_harga");

            model.addRow(new Object[]{
                rs.getInt("id_pesanan"),
                rs.getString("nama"),
                rs.getString("nama_produk"),
                rs.getInt("jumlah"),
                total,
                rs.getDate("tanggal_pesan")
            });

            totalPemasukan += total;
        }

        TabelLaporan.setModel(model);

        lblTotalPemasukan.setText(
            "Total Pemasukan : Rp " +
            String.format("%,.0f", totalPemasukan)
        );

    } catch(Exception e) {

        JOptionPane.showMessageDialog(
            null,
            "Error : " + e.getMessage()
        );

        e.printStackTrace();
    }
}
   
   
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jSlider1 = new javax.swing.JSlider();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        HalDashboard = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        Produk = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        Pesanan = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        Produk1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        Produk3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelPesanan1 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        HalPesanan = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelPesanan = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        caripesanan = new javax.swing.JTextField();
        btnPesanan = new javax.swing.JButton();
        HalProduk = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelProduk = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        cariproduk = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        cmbKategoriProduk = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        HalPembayaran = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        Produk4 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelPemasukan = new javax.swing.JTable();
        caripemasukan = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        Produk7 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        Produk8 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        Produk9 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        HalProduksi = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TabelProduksi = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        btnSelesaiProduksi = new javax.swing.JButton();
        btnProsesProduksi = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        cariproduksi = new javax.swing.JTextField();
        btnPesanan1 = new javax.swing.JButton();
        HalPelanggan = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelPelanggan = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        caripelanggan = new javax.swing.JTextField();
        HalLaporan = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        btnCetakLaporan = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        carilaporan = new javax.swing.JTextField();
        btnLaporan = new javax.swing.JButton();
        cmbTahun = new javax.swing.JComboBox<>();
        cmbBulan = new javax.swing.JComboBox<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        TabelLaporan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lblTotalPemasukan = new javax.swing.JLabel();
        HalPengeluaran = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jButton1.setBackground(new java.awt.Color(255, 102, 0));
        jButton1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Dashboard");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setBackground(new java.awt.Color(255, 102, 0));
        jButton2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Pesanan");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setBackground(new java.awt.Color(255, 102, 0));
        jButton3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Produk");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setBackground(new java.awt.Color(255, 102, 0));
        jButton4.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Pembayaran");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton5.setBackground(new java.awt.Color(255, 102, 0));
        jButton5.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Produksi");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setBackground(new java.awt.Color(255, 102, 0));
        jButton6.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Pelanggan");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton7.setBackground(new java.awt.Color(255, 102, 0));
        jButton7.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Laporan");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton8.setBackground(new java.awt.Color(255, 102, 0));
        jButton8.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Logout");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MerchOrder ");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 102, 0));
        jLabel2.setText("Studio");

        jButton9.setBackground(new java.awt.Color(255, 102, 0));
        jButton9.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Pengeluaran");
        jButton9.addActionListener(this::jButton9ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addGap(23, 23, 23))
        );

        jPanel2.setLayout(new java.awt.CardLayout());

        HalDashboard.setBackground(new java.awt.Color(241, 241, 241));
        HalDashboard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        HalDashboard.setPreferredSize(new java.awt.Dimension(780, 489));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel11.setPreferredSize(new java.awt.Dimension(165, 101));

        Produk.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Produk.setForeground(new java.awt.Color(255, 102, 0));
        Produk.setText("Nilai");

        jLabel10.setText("Total Produk");

        jLabel11.setText("Pesanan");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(Produk))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(10, 10, 10)
                .addComponent(Produk)
                .addGap(12, 12, 12)
                .addComponent(jLabel11)
                .addGap(18, 18, 18))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setPreferredSize(new java.awt.Dimension(165, 101));

        Pesanan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Pesanan.setForeground(new java.awt.Color(255, 102, 0));
        Pesanan.setText("Nilai");

        jLabel12.setText("Total Pesanan");

        jLabel13.setText("Pesanan");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(Pesanan))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Pesanan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addGap(15, 15, 15))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.setPreferredSize(new java.awt.Dimension(165, 101));

        Produk1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Produk1.setForeground(new java.awt.Color(255, 102, 0));
        Produk1.setText("Nilai");

        jLabel15.setText("Pesanan");

        jLabel16.setText("Total Pesanan");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(Produk1))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Produk1)
                .addGap(12, 12, 12)
                .addComponent(jLabel15)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.setPreferredSize(new java.awt.Dimension(165, 101));

        Produk3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Produk3.setForeground(new java.awt.Color(255, 102, 0));
        Produk3.setText("Nilai");

        jLabel14.setText("Total Pesanan");

        jLabel17.setText("Pesanan");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel14)
                    .addComponent(Produk3))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Produk3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addGap(15, 15, 15))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Dashboard Admin");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Selamat Datang Kembali, Admin");

        jLabel5.setText("Admin");

        jLabel6.setText("Nama User");

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TabelPesanan1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Pelanggan", "Nama", "Email", "No_Telp", "Alamat"
            }
        ));
        jScrollPane1.setViewportView(TabelPesanan1);

        jLabel7.setBackground(new java.awt.Color(255, 153, 102));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText("Pesanan Terbaru");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout HalDashboardLayout = new javax.swing.GroupLayout(HalDashboard);
        HalDashboard.setLayout(HalDashboardLayout);
        HalDashboardLayout.setHorizontalGroup(
            HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalDashboardLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HalDashboardLayout.createSequentialGroup()
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(HalDashboardLayout.createSequentialGroup()
                        .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(HalDashboardLayout.createSequentialGroup()
                                .addComponent(jPanel11, 161, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(HalDashboardLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))))
                        .addGap(18, 18, 18)
                        .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(HalDashboardLayout.createSequentialGroup()
                                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(41, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalDashboardLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalDashboardLayout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(10, 10, 10)))
                                .addGap(88, 88, 88))))))
        );
        HalDashboardLayout.setVerticalGroup(
            HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalDashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addGap(12, 12, 12)
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.add(HalDashboard, "card9");

        HalPesanan.setBackground(new java.awt.Color(255, 255, 255));
        HalPesanan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TabelPesanan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Pesanan", "ID User", "Tanggal Pesan", "Total Harga", "Status Pesanan"
            }
        ));
        TabelPesanan.setGridColor(new java.awt.Color(255, 255, 255));
        TabelPesanan.setRowHeight(80);
        TabelPesanan.setRowMargin(5);
        jScrollPane6.setViewportView(TabelPesanan);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Aksi :");

        jButton13.setBackground(new java.awt.Color(102, 153, 255));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("EDIT");
        jButton13.addActionListener(this::jButton13ActionPerformed);

        jButton14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton14.setText("DELETE");
        jButton14.addActionListener(this::jButton14ActionPerformed);

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setText("Cari Pesanan :");

        caripesanan.addActionListener(this::caripesananActionPerformed);
        caripesanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                caripesananKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                caripesananKeyReleased(evt);
            }
        });

        btnPesanan.setText("Cari");
        btnPesanan.addActionListener(this::btnPesananActionPerformed);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(caripesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 241, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(caripesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout HalPesananLayout = new javax.swing.GroupLayout(HalPesanan);
        HalPesanan.setLayout(HalPesananLayout);
        HalPesananLayout.setHorizontalGroup(
            HalPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalPesananLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        HalPesananLayout.setVerticalGroup(
            HalPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalPesananLayout.createSequentialGroup()
                .addContainerGap(131, Short.MAX_VALUE)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel2.add(HalPesanan, "card8");

        HalProduk.setBackground(new java.awt.Color(255, 255, 255));
        HalProduk.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TabelProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Produk", "Foto", "Nama Produk", "Kategori", "Harga", "Stok"
            }
        ));
        TabelProduk.setGridColor(new java.awt.Color(255, 255, 255));
        TabelProduk.setRowHeight(80);
        TabelProduk.setRowMargin(5);
        jScrollPane2.setViewportView(TabelProduk);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Aksi :");

        jButton11.setBackground(new java.awt.Color(102, 153, 255));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("EDIT");
        jButton11.addActionListener(this::jButton11ActionPerformed);

        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton12.setText("DELETE");
        jButton12.addActionListener(this::jButton12ActionPerformed);

        cariproduk.addActionListener(this::cariprodukActionPerformed);
        cariproduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariprodukKeyReleased(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(this::btnCariActionPerformed);

        cmbKategoriProduk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Kategori", "Kaos", "Hoodie", "Mug", "Sticker", "Totebag" }));
        cmbKategoriProduk.addActionListener(this::cmbKategoriProdukActionPerformed);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(cariproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbKategoriProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKategoriProduk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setText("PRODUK");

        jButton10.setBackground(new java.awt.Color(255, 102, 0));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("+ Tambah Produk");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 102, 51));
        jLabel30.setText("Kelola Produksi");

        javax.swing.GroupLayout HalProdukLayout = new javax.swing.GroupLayout(HalProduk);
        HalProduk.setLayout(HalProdukLayout);
        HalProdukLayout.setHorizontalGroup(
            HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalProdukLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HalProdukLayout.createSequentialGroup()
                        .addGroup(HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel30))
                        .addGap(442, 442, 442)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        HalProdukLayout.setVerticalGroup(
            HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalProdukLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel2.add(HalProduk, "card7");

        HalPembayaran.setBackground(new java.awt.Color(255, 255, 255));
        HalPembayaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Produk4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Produk4.setText("jLabel10");

        jLabel18.setText("Total Pesanan");

        jLabel19.setText("Total Pesanan");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18)
                    .addComponent(Produk4))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Produk4)
                .addGap(12, 12, 12)
                .addComponent(jLabel19)
                .addGap(12, 12, 12))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TabelPemasukan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Pembayaran", "No Pesanan", "Bukti", "Status", "Tanggal Bayar"
            }
        ));
        jScrollPane5.setViewportView(TabelPemasukan);

        caripemasukan.addActionListener(this::caripemasukanActionPerformed);
        caripemasukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                caripemasukanKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(caripemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(caripemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Produk7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Produk7.setText("jLabel10");

        jLabel24.setText("Total Pesanan");

        jLabel25.setText("Total Pesanan");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24)
                    .addComponent(Produk7))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Produk7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel25)
                .addGap(12, 12, 12))
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Produk8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Produk8.setText("jLabel10");

        jLabel26.setText("Total Pesanan");

        jLabel27.setText("Total Pesanan");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel26)
                    .addComponent(Produk8))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Produk8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addGap(12, 12, 12))
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Produk9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Produk9.setText("jLabel10");

        jLabel28.setText("Total Pesanan");

        jLabel29.setText("Total Pesanan");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel28)
                    .addComponent(Produk9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Produk9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel29)
                .addGap(12, 12, 12))
        );

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 102, 51));
        jLabel20.setText("Riwayat Pembayaran");

        javax.swing.GroupLayout HalPembayaranLayout = new javax.swing.GroupLayout(HalPembayaran);
        HalPembayaran.setLayout(HalPembayaranLayout);
        HalPembayaranLayout.setHorizontalGroup(
            HalPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalPembayaranLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(HalPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addGroup(HalPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(HalPembayaranLayout.createSequentialGroup()
                            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        HalPembayaranLayout.setVerticalGroup(
            HalPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalPembayaranLayout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(HalPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel2.add(HalPembayaran, "card6");

        HalProduksi.setBackground(new java.awt.Color(255, 255, 255));
        HalProduksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TabelProduksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Pesanan", "ID User", "Tanggal Pesan", "Total Harga", "Status Pesanan"
            }
        ));
        TabelProduksi.setGridColor(new java.awt.Color(255, 255, 255));
        TabelProduksi.setRowHeight(80);
        TabelProduksi.setRowMargin(5);
        jScrollPane7.setViewportView(TabelProduksi);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Aksi :");

        btnSelesaiProduksi.setBackground(new java.awt.Color(102, 153, 255));
        btnSelesaiProduksi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSelesaiProduksi.setForeground(new java.awt.Color(255, 255, 255));
        btnSelesaiProduksi.setText("PROSES");
        btnSelesaiProduksi.addActionListener(this::btnSelesaiProduksiActionPerformed);

        btnProsesProduksi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProsesProduksi.setForeground(new java.awt.Color(51, 153, 255));
        btnProsesProduksi.setText("SELESAI");
        btnProsesProduksi.addActionListener(this::btnProsesProduksiActionPerformed);

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Cari Pesanan :");

        cariproduksi.addActionListener(this::cariproduksiActionPerformed);
        cariproduksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cariproduksiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariproduksiKeyReleased(evt);
            }
        });

        btnPesanan1.setText("Cari");
        btnPesanan1.addActionListener(this::btnPesanan1ActionPerformed);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSelesaiProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnProsesProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(jLabel33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cariproduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPesanan1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 241, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariproduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesanan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(btnSelesaiProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProsesProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout HalProduksiLayout = new javax.swing.GroupLayout(HalProduksi);
        HalProduksi.setLayout(HalProduksiLayout);
        HalProduksiLayout.setHorizontalGroup(
            HalProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalProduksiLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        HalProduksiLayout.setVerticalGroup(
            HalProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalProduksiLayout.createSequentialGroup()
                .addContainerGap(131, Short.MAX_VALUE)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel2.add(HalProduksi, "card5");

        HalPelanggan.setBackground(new java.awt.Color(255, 255, 255));
        HalPelanggan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        TabelPelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Pelanggan", "Nama", "Email", "No_Telp", "Alamat"
            }
        ));
        jScrollPane3.setViewportView(TabelPelanggan);

        jLabel21.setBackground(new java.awt.Color(255, 153, 102));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 102, 0));
        jLabel21.setText("Daftar Pelanggan");

        caripelanggan.addActionListener(this::caripelangganActionPerformed);
        caripelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                caripelangganKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                caripelangganKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(caripelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(caripelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout HalPelangganLayout = new javax.swing.GroupLayout(HalPelanggan);
        HalPelanggan.setLayout(HalPelangganLayout);
        HalPelangganLayout.setHorizontalGroup(
            HalPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalPelangganLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        HalPelangganLayout.setVerticalGroup(
            HalPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalPelangganLayout.createSequentialGroup()
                .addContainerGap(154, Short.MAX_VALUE)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jPanel2.add(HalPelanggan, "card4");

        HalLaporan.setBackground(new java.awt.Color(255, 255, 255));
        HalLaporan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnCetakLaporan.setBackground(new java.awt.Color(102, 153, 255));
        btnCetakLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCetakLaporan.setForeground(new java.awt.Color(255, 255, 255));
        btnCetakLaporan.setText("CETAK LAPORAN");
        btnCetakLaporan.addActionListener(this::btnCetakLaporanActionPerformed);

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setText("Cari Pesanan :");

        carilaporan.addActionListener(this::carilaporanActionPerformed);
        carilaporan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                carilaporanKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                carilaporanKeyReleased(evt);
            }
        });

        btnLaporan.setText("Cari");
        btnLaporan.addActionListener(this::btnLaporanActionPerformed);

        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2024", "2025", "2026", " " }));
        cmbTahun.addActionListener(this::cmbTahunActionPerformed);

        cmbBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember", " " }));
        cmbBulan.addActionListener(this::cmbBulanActionPerformed);

        TabelLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Pesanan", "ID User", "Tanggal Pesan", "Total Harga", "Status Pesanan"
            }
        ));
        TabelLaporan.setGridColor(new java.awt.Color(255, 255, 255));
        TabelLaporan.setRowHeight(80);
        TabelLaporan.setRowMargin(5);
        jScrollPane9.setViewportView(TabelLaporan);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblTotalPemasukan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalPemasukan.setText("TOTAL PEMASUKAN");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalPemasukan)
                .addGap(301, 301, 301))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalPemasukan)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCetakLaporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(carilaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 7, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carilaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(cmbBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTahun))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btnCetakLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout HalLaporanLayout = new javax.swing.GroupLayout(HalLaporan);
        HalLaporan.setLayout(HalLaporanLayout);
        HalLaporanLayout.setHorizontalGroup(
            HalLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalLaporanLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        HalLaporanLayout.setVerticalGroup(
            HalLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalLaporanLayout.createSequentialGroup()
                .addContainerGap(160, Short.MAX_VALUE)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jPanel2.add(HalLaporan, "card3");

        HalPengeluaran.setBackground(new java.awt.Color(255, 255, 255));
        HalPengeluaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout HalPengeluaranLayout = new javax.swing.GroupLayout(HalPengeluaran);
        HalPengeluaran.setLayout(HalPengeluaranLayout);
        HalPengeluaranLayout.setHorizontalGroup(
            HalPengeluaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        HalPengeluaranLayout.setVerticalGroup(
            HalPengeluaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 557, Short.MAX_VALUE)
        );

        jPanel2.add(HalPengeluaran, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "produk");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "dashboard");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        loginForm login = new loginForm();
    login.setVisible(true);

    this.dispose(); // menutup Dashboard_Admin
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "pesanan");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "pembayaran");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "produksi");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "pelanggan");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "Laporan");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        kelolaproduk_admin tambah = new kelolaproduk_admin();
    tambah.setVisible(true);

    this.dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
{

    DataProduk();

}
    }//GEN-LAST:event_btnCariActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        int row = TabelProduk.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }

        // ambil ID dari kolom pertama (WAJIB ada id_produk di table model)
        int id = Integer.parseInt(TabelProduk.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin hapus data ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                Connection conn = koneksi.getConnection();

                String sql = "DELETE FROM produk WHERE id_produk=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");

                DataProduk();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal hapus: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        int row = TabelProduk.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }

        // ambil data dari JTable
        int id = Integer.parseInt(TabelProduk.getValueAt(row, 0).toString());
        String nama = TabelProduk.getValueAt(row, 2).toString();
        String kategori = TabelProduk.getValueAt(row, 3).toString();
        String harga = TabelProduk.getValueAt(row, 4).toString();
        String stok = TabelProduk.getValueAt(row, 5).toString();

        // buka form kelola produk
        kelolaproduk_admin form = new kelolaproduk_admin();

        // kirim data ke form (INI PENTING)
        form.setDataEdit(id, nama, kategori, harga, stok);

        form.setVisible(true);
        this.dispose(); // optional
    }//GEN-LAST:event_jButton11ActionPerformed

    private void cariprodukKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariprodukKeyReleased
        // TODO add your handling code here:
 {

    if(cariproduk.getText().trim().isEmpty()) {

        tampilJumlahProduk(); // method load semua data

    } else {

        DataProduk();

    }

}
    }//GEN-LAST:event_cariprodukKeyReleased

    private void cariprodukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariprodukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariprodukActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void caripesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caripesananActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_caripesananActionPerformed

    private void caripesananKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caripesananKeyReleased
        // TODO add your handling code here:
        DataPesanan();
    }//GEN-LAST:event_caripesananKeyReleased

    private void btnPesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesananActionPerformed
        // TODO add your handling code here:
        
        DataProduk();
    }//GEN-LAST:event_btnPesananActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        card.show(jPanel2, "pengeluaran");
    }//GEN-LAST:event_jButton9ActionPerformed

    private void caripesananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caripesananKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_caripesananKeyPressed

    private void btnSelesaiProduksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelesaiProduksiActionPerformed
        // TODO add your handling code here:
            try {

            int row = TabelProduksi.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(null,
                "Pilih pesanan terlebih dahulu");
                return;
            }

            int idPesanan = Integer.parseInt(
                TabelProduksi.getValueAt(row, 0).toString()
            );

            String sql =
            "UPDATE pesanan " +
            "SET status_pesanan='produksi' " +
            "WHERE id_pesanan=?";

            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, idPesanan);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,
            "Pesanan masuk proses produksi");

            DataProduksi();

        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btnSelesaiProduksiActionPerformed

    private void btnProsesProduksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesProduksiActionPerformed
        // TODO add your handling code here:
            try {

            int row = TabelProduksi.getSelectedRow();

            if(row == -1){
                JOptionPane.showMessageDialog(null,
                "Pilih pesanan terlebih dahulu");
                return;
            }

            int idPesanan = Integer.parseInt(
                TabelProduksi.getValueAt(row, 0).toString()
            );

            String sql =
            "UPDATE pesanan " +
            "SET status_pesanan='selesai' " +
            "WHERE id_pesanan=?";

            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, idPesanan);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,
            "Produksi selesai");

            DataProduksi();

        } catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btnProsesProduksiActionPerformed

    private void cariproduksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariproduksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariproduksiActionPerformed

    private void cariproduksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariproduksiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariproduksiKeyPressed

    private void cariproduksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariproduksiKeyReleased
        // TODO add your handling code here:
        DataProduksi();
    }//GEN-LAST:event_cariproduksiKeyReleased

    private void btnPesanan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesanan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPesanan1ActionPerformed

    private void btnCetakLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakLaporanActionPerformed
        // TODO add your handling code here:
             try {

                String bulan = cmbBulan.getSelectedItem().toString();
                String tahun = cmbTahun.getSelectedItem().toString();

                MessageFormat header = new MessageFormat(
                        "LAPORAN PENJUALAN\n" +
                        "Periode : " + bulan + " " + tahun
                );

                MessageFormat footer = new MessageFormat(
                        lblTotalPemasukan.getText() +
                        " | Halaman {0}"
                );

                boolean complete = TabelLaporan.print(
                        JTable.PrintMode.FIT_WIDTH,
                        header,
                        footer
                );

                if (complete) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Laporan berhasil dicetak"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            null,
                            "Pencetakan dibatalkan"
                    );

                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        null,
                        "Gagal mencetak : " + e.getMessage()
                );

                e.printStackTrace();
    }
        
    }//GEN-LAST:event_btnCetakLaporanActionPerformed

    private void carilaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_carilaporanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_carilaporanActionPerformed

    private void carilaporanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carilaporanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_carilaporanKeyPressed

    private void carilaporanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_carilaporanKeyReleased
        // TODO add your handling code here:
        DataLaporan();
        TotalPemasukan();
    }//GEN-LAST:event_carilaporanKeyReleased

    private void btnLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanActionPerformed
        // TODO add your handling code here:
        DataPembayaran();
        TotalPemasukan();
    }//GEN-LAST:event_btnLaporanActionPerformed

    private void caripelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caripelangganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_caripelangganActionPerformed

    private void caripelangganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caripelangganKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_caripelangganKeyPressed

    private void caripelangganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caripelangganKeyReleased
        // TODO add your handling code here:
        DataPelanggan();
    }//GEN-LAST:event_caripelangganKeyReleased

    private void caripemasukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_caripemasukanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_caripemasukanActionPerformed

    private void caripemasukanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_caripemasukanKeyReleased
        // TODO add your handling code here:
        DataPembayaran();
        TotalPemasukan();
    }//GEN-LAST:event_caripemasukanKeyReleased

    private void cmbBulanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBulanActionPerformed
        // TODO add your handling code here:
        DataLaporan();
        TotalPemasukan();
    }//GEN-LAST:event_cmbBulanActionPerformed

    private void cmbTahunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTahunActionPerformed
        // TODO add your handling code here:
        DataLaporan();
        TotalPemasukan();
    }//GEN-LAST:event_cmbTahunActionPerformed

    private void cmbKategoriProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKategoriProdukActionPerformed
        // TODO add your handling code here:
        DataProduk();
    }//GEN-LAST:event_cmbKategoriProdukActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new dashboard_admin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HalDashboard;
    private javax.swing.JPanel HalLaporan;
    private javax.swing.JPanel HalPelanggan;
    private javax.swing.JPanel HalPembayaran;
    private javax.swing.JPanel HalPengeluaran;
    private javax.swing.JPanel HalPesanan;
    private javax.swing.JPanel HalProduk;
    private javax.swing.JPanel HalProduksi;
    private javax.swing.JLabel Pesanan;
    private javax.swing.JLabel Produk;
    private javax.swing.JLabel Produk1;
    private javax.swing.JLabel Produk3;
    private javax.swing.JLabel Produk4;
    private javax.swing.JLabel Produk7;
    private javax.swing.JLabel Produk8;
    private javax.swing.JLabel Produk9;
    private javax.swing.JTable TabelLaporan;
    private javax.swing.JTable TabelPelanggan;
    private javax.swing.JTable TabelPemasukan;
    private javax.swing.JTable TabelPesanan;
    private javax.swing.JTable TabelPesanan1;
    private javax.swing.JTable TabelProduk;
    private javax.swing.JTable TabelProduksi;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCetakLaporan;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnPesanan;
    private javax.swing.JButton btnPesanan1;
    private javax.swing.JButton btnProsesProduksi;
    private javax.swing.JButton btnSelesaiProduksi;
    private javax.swing.JTextField carilaporan;
    private javax.swing.JTextField caripelanggan;
    private javax.swing.JTextField caripemasukan;
    private javax.swing.JTextField caripesanan;
    private javax.swing.JTextField cariproduk;
    private javax.swing.JTextField cariproduksi;
    private javax.swing.JComboBox<String> cmbBulan;
    private javax.swing.JComboBox<String> cmbKategoriProduk;
    private javax.swing.JComboBox<String> cmbTahun;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JLabel lblTotalPemasukan;
    // End of variables declaration//GEN-END:variables
}
