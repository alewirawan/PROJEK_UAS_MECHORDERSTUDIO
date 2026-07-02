/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package merchorderstudio;
import javax.swing.JTable;
import java.io.File;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
        
    setResizable(false);
    
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
    
    Tampilkan();
    DataDashboard();
    DataPesanan();
    DataProduk();
    setupTableRenderer();
    DataPembayaran();
    DataProduksi();
    DataPelanggan();
    DataLaporan();
    TotalPemasukan();
    showCard("");
    }
    
    public class FormatRupiah {

    public static String format(double harga) {

        DecimalFormatSymbols simbol = new DecimalFormatSymbols();
        simbol.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,###");
        df.setDecimalFormatSymbols(simbol);

        return df.format(harga);
    }
}
    
   private void TotalPemasukan(){

    try{

        String bulanDipilih =
        cmbBulan.getSelectedItem().toString();

        String tahunDipilih =
        cmbTahun.getSelectedItem().toString();

        String sql =
        "SELECT COALESCE(SUM(p.total_harga),0) AS total " +
        "FROM pesanan p " +
        "JOIN pembayaran pb " +
        "ON p.id_pesanan = pb.id_pesanan " +
        "WHERE pb.status_bayar='lunas' ";

        if(!bulanDipilih.equals("Pilih Bulan")){

            sql +=
            "AND MONTH(p.tanggal_pesan)=? ";

        }

        if(!tahunDipilih.equals("Pilih Tahun")){

            sql +=
            "AND YEAR(p.tanggal_pesan)=? ";

        }

        Connection conn =
        koneksi.getConnection();

        PreparedStatement pst =
        conn.prepareStatement(sql);

        int index = 1;

        if(!bulanDipilih.equals("Pilih Bulan")){

            pst.setInt(
            index++,
            cmbBulan.getSelectedIndex()
            );

        }

        if(!tahunDipilih.equals("Pilih Tahun")){

            pst.setInt(
            index++,
            Integer.parseInt(tahunDipilih)
            );

        }

        ResultSet rs =
        pst.executeQuery();

        if(rs.next()){

            lblTotalPemasukan.setText(
            "Total Pemasukan : Rp " +
            String.format(
            "%,.0f",
            rs.getDouble("total"))
            );

        }

    }catch(Exception e){

        JOptionPane.showMessageDialog(
        null,
        e.getMessage()
        );

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

            lblTotalProduk.setText(
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

            lblTotalPesanan.setText(
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
    
    private void DataDashboard(){

    DefaultTableModel model =
    new DefaultTableModel();

    model.addColumn("No Order");
    model.addColumn("Pelanggan");
    model.addColumn("Produk");
    model.addColumn("Total Harga");
    model.addColumn("Status");

    try{

        String sql =
        "SELECT p.id_pesanan, " +
        "u.nama, " +
        "pr.nama_produk, " +
        "p.total_harga, " +
        "p.status_pesanan " +
        "FROM pesanan p " +
        "INNER JOIN users u " +
        "ON p.id_user = u.id_user " +
        "INNER JOIN detail_pesanan dp " +
        "ON p.id_pesanan = dp.id_pesanan " +
        "INNER JOIN produk pr " +
        "ON dp.id_produk = pr.id_produk " +
        "ORDER BY p.id_pesanan DESC " +
        "LIMIT 10";

        Connection conn =
        koneksi.getConnection();

        PreparedStatement pst =
        conn.prepareStatement(sql);

        ResultSet rs =
        pst.executeQuery();

        while(rs.next()){

            double totalHarga =
            rs.getDouble("total_harga");

            model.addRow(new Object[]{

                rs.getInt("id_pesanan"),
                rs.getString("nama"),
                rs.getString("nama_produk"),
                FormatRupiah.format(totalHarga),
                rs.getString("status_pesanan")

            });

        }

        TabelDashboard.setModel(model);

        TabelDashboard.setRowHeight(45);

    }catch(Exception e){

        JOptionPane.showMessageDialog(
        null,
        e.getMessage());

    }

}
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

            double totalHarga = rs.getDouble("total_harga");

            model.addRow(new Object[]{
                rs.getInt("id_pesanan"),
                rs.getString("nama"),
                rs.getString("nama_produk"),
                rs.getInt("jumlah"),
                FormatRupiah.format(totalHarga),
                rs.getString("status_pesanan"),
                rs.getDate("tanggal_pesan")
            });
        }

        TabelPesanan.setModel(model);
        
        TabelPesanan.setRowHeight(45);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    
    public void DataProduk() {

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

        if (!kategori.equalsIgnoreCase("Semua Kategori")) {

            sql += "AND kategori = ? ";

        }

        PreparedStatement pst =
        conn.prepareStatement(sql);

        pst.setString(
                1,
                "%" + cari + "%"
        );

        if (!kategori.equalsIgnoreCase("Semua Kategori")) {

            pst.setString(
                    2,
                    kategori
            );

        }

        ResultSet rs = pst.executeQuery();

        DefaultTableModel model =
        (DefaultTableModel) TabelProduk.getModel();

        model.setRowCount(0);

        DecimalFormatSymbols simbol =
        new DecimalFormatSymbols();

        simbol.setGroupingSeparator('.');

        DecimalFormat formatHarga =
        new DecimalFormat("#,###");

        formatHarga.setDecimalFormatSymbols(simbol);

        while (rs.next()) {

            int id = rs.getInt("id_produk");
            String nama = rs.getString("nama_produk");
            String kategoriProduk = rs.getString("kategori");
            double harga = rs.getDouble("harga");
            String hargaFormat =formatHarga.format(harga);
            int stok = rs.getInt("stok");
            String ukuran = rs.getString("ukuran");
            String deskripsi = rs.getString("deskripsi");

            ImageIcon imageIcon = null;

            byte[] imageData =
            rs.getBytes("foto_produk");

            if (imageData != null) {

                ImageIcon icon =
                new ImageIcon(imageData);

                Image img =
                icon.getImage()
                .getScaledInstance(
                        60,
                        60,
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
                hargaFormat,
                stok,
                ukuran,
                deskripsi
            });

        }

        TabelProduk.setRowHeight(80);

        rs.close();
        pst.close();
        conn.close();

    } catch (Exception e) {

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
            
            double totalHarga = rs.getDouble("total_harga");

            model.addRow(new Object[]{
                rs.getInt("id_pembayaran"),
                rs.getInt("id_pesanan"),
                rs.getString("metode_bayar"),
                FormatRupiah.format(totalHarga),
                rs.getString("status_bayar"),
                rs.getDate("tanggal_bayar")
            });

        }

        TabelPembayaran.setModel(model);
        
        TabelPembayaran.setRowHeight(45);

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

        String keyword = cariproduksi.getText().trim();

        String status =
        cmbStatusProduksi.getSelectedItem().toString();

        String sql =
        "SELECT p.id_pesanan, u.nama, pr.nama_produk, " +
        "dp.jumlah, dp.upload_desain, dp.catatan, " +
        "p.status_pesanan " +
        "FROM pesanan p " +
        "INNER JOIN users u ON p.id_user = u.id_user " +
        "INNER JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan " +
        "INNER JOIN produk pr ON dp.id_produk = pr.id_produk " +
        "INNER JOIN pembayaran pb ON p.id_pesanan = pb.id_pesanan " +
        "WHERE pb.status_bayar='lunas' ";

        // Filter Status
        if(!status.equalsIgnoreCase("Semua")){

            sql += "AND p.status_pesanan=? ";

        }

        sql +=
        "AND (CAST(p.id_pesanan AS CHAR) LIKE ? " +
        "OR u.nama LIKE ? " +
        "OR pr.nama_produk LIKE ? " +
        "OR p.status_pesanan LIKE ?)";

        Connection conn = koneksi.getConnection();

        PreparedStatement pst =
        conn.prepareStatement(sql);

        int index = 1;

        if(!status.equalsIgnoreCase("Semua")){

            pst.setString(index, status.toLowerCase());
            index++;

        }

        pst.setString(index++, "%" + keyword + "%");
        pst.setString(index++, "%" + keyword + "%");
        pst.setString(index++, "%" + keyword + "%");
        pst.setString(index++, "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();

       while (rs.next()) {

            ImageIcon desainIcon = null;

            byte[] imageBytes = rs.getBytes("upload_desain");

            if (imageBytes != null && imageBytes.length > 0) {

                ImageIcon icon = new ImageIcon(imageBytes);

                Image img = icon.getImage().getScaledInstance(
                        70,
                        70,
                        Image.SCALE_SMOOTH
                );

                desainIcon = new ImageIcon(img);
            }

            model.addRow(new Object[]{
                rs.getInt("id_pesanan"),
                rs.getString("nama"),
                rs.getString("nama_produk"),
                rs.getInt("jumlah"),
                desainIcon,
                rs.getString("catatan"),
                rs.getString("status_pesanan")
            });
        }

                TabelProduksi.setModel(model);

                TabelProduksi.setRowHeight(70);

                setupTableRenderer();

            } catch(Exception e) {

                JOptionPane.showMessageDialog(
                    null,
                    e.getMessage()
                );

                e.printStackTrace();
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
        "SELECT DISTINCT u.id_user, u.nama, u.email, u.no_telp, u.alamat " +
        "FROM users u " +
        "INNER JOIN pesanan p ON u.id_user = p.id_user " +
        "WHERE CAST(u.id_user AS CHAR) LIKE ? " +
        "OR u.nama LIKE ? " +
        "OR u.email LIKE ? " +
        "OR u.no_telp LIKE ?";

        Connection conn = koneksi.getConnection();

        PreparedStatement pst = conn.prepareStatement(sql);

        for(int i = 1; i <= 4; i++){
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

        TabelPelanggan.setRowHeight(45);

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

        String keyword = carilaporan.getText().trim();

        String bulanDipilih =
                cmbBulan.getSelectedItem().toString();

        String tahunDipilih =
                cmbTahun.getSelectedItem().toString();

        String sql =
        "SELECT p.id_pesanan, u.nama, pr.nama_produk, " +
        "dp.jumlah, p.total_harga, p.tanggal_pesan " +
        "FROM pesanan p " +
        "INNER JOIN users u ON p.id_user = u.id_user " +
        "INNER JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan " +
        "INNER JOIN produk pr ON dp.id_produk = pr.id_produk " +
        "INNER JOIN pembayaran pb ON p.id_pesanan = pb.id_pesanan " +
        "WHERE pb.status_bayar='lunas' ";

        if(!bulanDipilih.equals("Pilih Bulan")){

            sql +=
            "AND MONTH(p.tanggal_pesan)=? ";

        }

        if(!tahunDipilih.equals("Pilih Tahun")){

            sql +=
            "AND YEAR(p.tanggal_pesan)=? ";

        }

        sql +=
        "AND (CAST(p.id_pesanan AS CHAR) LIKE ? " +
        "OR u.nama LIKE ? " +
        "OR pr.nama_produk LIKE ?)";

        Connection conn = koneksi.getConnection();

        PreparedStatement pst =
        conn.prepareStatement(sql);

        int index = 1;

        if(!bulanDipilih.equals("Pilih Bulan")){

            pst.setInt(
            index++,
            cmbBulan.getSelectedIndex()
            );

        }

        if(!tahunDipilih.equals("Pilih Tahun")){

            pst.setInt(
            index++,
            Integer.parseInt(tahunDipilih)
            );

        }

        pst.setString(
        index++,
        "%" + keyword + "%");

        pst.setString(
        index++,
        "%" + keyword + "%");

        pst.setString(
        index++,
        "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {

            double total =
            rs.getDouble("total_harga");

            model.addRow(new Object[]{
                rs.getInt("id_pesanan"),
                rs.getString("nama"),
                rs.getString("nama_produk"),
                rs.getInt("jumlah"),
                FormatRupiah.format(total), 
                rs.getDate("tanggal_pesan")
            });

            totalPemasukan += total;
        }

        TabelLaporan.setModel(model);

        TabelLaporan.setRowHeight(45);

        lblTotalPemasukan.setText(
            "Total Pemasukan : Rp " +
            String.format("%,.0f",
            totalPemasukan)
        );

    } catch(Exception e) {

        JOptionPane.showMessageDialog(
            null,
            "Error : " + e.getMessage()
        );

        e.printStackTrace();

    }
}
   
   private void Tampilkan() {

    try {

        String sql =
        "SELECT " +

        "(SELECT COUNT(*) FROM produk) AS jumlah_produk, " +
        "(SELECT COUNT(*) FROM pesanan) AS total_pesanan, " +
        "(SELECT COUNT(*) FROM pembayaran " +
        "WHERE status_bayar='pending') " +
        "AS menunggu_pembayaran, " +
        "(SELECT COALESCE(SUM(jumlah),0) " +
        "FROM detail_pesanan) " +
        "AS produk_terjual, " +
        "(SELECT COUNT(*) FROM pesanan " +
        "WHERE status_pesanan='pending') " +
        "AS pesanan_pending, " +
        "(SELECT COUNT(*) FROM pesanan " +
        "WHERE status_pesanan='diproses') " +
        "AS pesanan_diproses, " +
        "(SELECT COUNT(*) FROM pesanan " +
        "WHERE status_pesanan='produksi') " +
        "AS pesanan_produksi, " +
        "(SELECT COUNT(*) FROM pesanan " +
        "WHERE status_pesanan='selesai') " +
        "AS pesanan_selesai, " +
        "(SELECT COALESCE(SUM(p.total_harga),0) " +
        "FROM pesanan p " +
        "INNER JOIN pembayaran pb " +
        "ON p.id_pesanan = pb.id_pesanan " +
        "WHERE pb.status_bayar='lunas') " +
        "AS total_pemasukan";

        Connection conn =
        koneksi.getConnection();

        PreparedStatement pst =
        conn.prepareStatement(sql);

        ResultSet rs =
        pst.executeQuery();

        if(rs.next()){

            lblTotalProduk.setText(rs.getString("jumlah_produk"));
            lblTotalProdukLap.setText(rs.getString("jumlah_produk"));
            lblTotalPesanan.setText(rs.getString("total_pesanan"));
            lblTotalPesananLap.setText(rs.getString("total_pesanan"));
            lblMenungguPembayaran.setText(rs.getString("menunggu_pembayaran"));
            lblProdukTerjual.setText(rs.getString("produk_terjual"));
            lblProdukTerjualLap.setText(rs.getString("produk_terjual"));
            lblPending.setText(rs.getString("pesanan_pending"));
            lblProses.setText(rs.getString("pesanan_diproses"));
            lblProduksi.setText(rs.getString("pesanan_produksi"));
            lblSelesai.setText(rs.getString("pesanan_selesai"));
            
            lblTotalPemasukanLap.setText(
            "Rp " +
            String.format(
            "%,.0f",
            rs.getDouble("total_pemasukan"))
            );

        }

        rs.close();
        pst.close();
        conn.close();

    } catch(Exception e){

        JOptionPane.showMessageDialog(
        null,
        "Error : " + e.getMessage()
        );

        e.printStackTrace();

    }

}
    
  public void showCard(String namaPanel) {

    if(namaPanel.equals("dashboard")){

        Tampilkan();
        DataDashboard();

        }
        else if(namaPanel.equals("pesanan")){

            DataPesanan();

        }
        else if(namaPanel.equals("produk")){

            DataProduk();

        }
        else if(namaPanel.equals("pembayaran")){

            DataPembayaran();

        }
        else if(namaPanel.equals("produksi")){

            DataProduksi();

        }
        else if(namaPanel.equals("pelanggan")){

            DataPelanggan();

        }
        else if(namaPanel.equals("Laporan")){

            DataLaporan();
            TotalPemasukan();

        }

        CardLayout card =
        (CardLayout) jPanel2.getLayout();

        card.show(jPanel2, namaPanel);
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
        lblTotalProduk = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        lblTotalPesanan = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        lblMenungguPembayaran = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        lblProdukTerjual = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelDashboard = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        HalPesanan = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelPesanan = new javax.swing.JTable();
        caripesanan = new javax.swing.JTextField();
        btnPesanan = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        lblPending = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        lblProses = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        lblProduksi = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        lblSelesai = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        HalProduk = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TabelProduk = new javax.swing.JTable();
        cariproduk = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        cmbKategoriProduk = new javax.swing.JComboBox<>();
        btnEditProduk = new javax.swing.JButton();
        btnDeleteProduk = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        HalPembayaran = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TabelPembayaran = new javax.swing.JTable();
        caripemasukan = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        btnPesanan2 = new javax.swing.JButton();
        HalProduksi = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        TabelProduksi = new javax.swing.JTable();
        btnProses = new javax.swing.JButton();
        btnSelesai = new javax.swing.JButton();
        cariproduksi = new javax.swing.JTextField();
        btnPesanan1 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        cmbStatusProduksi = new javax.swing.JComboBox<>();
        HalPelanggan = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TabelPelanggan = new javax.swing.JTable();
        caripelanggan = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        btnPesanan3 = new javax.swing.JButton();
        HalLaporan = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        btnCetakLaporan = new javax.swing.JButton();
        carilaporan = new javax.swing.JTextField();
        btnLaporan = new javax.swing.JButton();
        cmbTahun = new javax.swing.JComboBox<>();
        cmbBulan = new javax.swing.JComboBox<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        TabelLaporan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lblTotalPemasukan = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        lblTotalPemasukanLap = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        lblTotalProdukLap = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        lblTotalPesananLap = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        lblProdukTerjualLap = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
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

        HalDashboard.setBackground(new java.awt.Color(255, 255, 255));
        HalDashboard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        HalDashboard.setPreferredSize(new java.awt.Dimension(780, 489));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel11.setPreferredSize(new java.awt.Dimension(165, 101));

        lblTotalProduk.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalProduk.setForeground(new java.awt.Color(255, 102, 0));
        lblTotalProduk.setText("Nilai");

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
                    .addComponent(lblTotalProduk))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(10, 10, 10)
                .addComponent(lblTotalProduk)
                .addGap(12, 12, 12)
                .addComponent(jLabel11)
                .addGap(18, 18, 18))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setPreferredSize(new java.awt.Dimension(165, 101));

        lblTotalPesanan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalPesanan.setForeground(new java.awt.Color(255, 102, 0));
        lblTotalPesanan.setText("Nilai");

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
                    .addComponent(lblTotalPesanan))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalPesanan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addGap(15, 15, 15))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.setPreferredSize(new java.awt.Dimension(165, 101));

        lblMenungguPembayaran.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblMenungguPembayaran.setForeground(new java.awt.Color(255, 102, 0));
        lblMenungguPembayaran.setText("Nilai");

        jLabel15.setText("Pesanan");

        jLabel16.setText("Menunggu Pembayaran");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(lblMenungguPembayaran))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMenungguPembayaran)
                .addGap(12, 12, 12)
                .addComponent(jLabel15)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.setPreferredSize(new java.awt.Dimension(165, 101));

        lblProdukTerjual.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblProdukTerjual.setForeground(new java.awt.Color(255, 102, 0));
        lblProdukTerjual.setText("Nilai");

        jLabel14.setText("Produk Terjual");

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
                    .addComponent(lblProdukTerjual))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProdukTerjual)
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

        jPanel22.setBackground(new java.awt.Color(51, 51, 51));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel22.setPreferredSize(new java.awt.Dimension(740, 340));

        TabelDashboard.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(TabelDashboard);

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout HalDashboardLayout = new javax.swing.GroupLayout(HalDashboard);
        HalDashboard.setLayout(HalDashboardLayout);
        HalDashboardLayout.setHorizontalGroup(
            HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalDashboardLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HalDashboardLayout.createSequentialGroup()
                        .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(HalDashboardLayout.createSequentialGroup()
                                .addComponent(jPanel11, 161, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(HalDashboardLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalDashboardLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(58, 58, 58))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalDashboardLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(71, 71, 71))
                            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        HalDashboardLayout.setVerticalGroup(
            HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalDashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HalDashboardLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalDashboardLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(HalDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jPanel2.add(HalDashboard, "card9");

        HalPesanan.setBackground(new java.awt.Color(255, 255, 255));
        HalPesanan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel25.setBackground(new java.awt.Color(51, 51, 51));
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

        jPanel8.setBackground(new java.awt.Color(255, 102, 51));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("DAFTAR PESANAN");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(296, 296, 296)
                .addComponent(jLabel9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel9)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(caripesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(283, 283, 283))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jScrollPane6)
                        .addContainerGap())))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(caripesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel16.setPreferredSize(new java.awt.Dimension(165, 101));

        lblPending.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPending.setForeground(new java.awt.Color(255, 102, 0));
        lblPending.setText("Nilai");

        jLabel18.setText("Pending");

        jLabel19.setText("Pesanan");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18)
                    .addComponent(lblPending))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addGap(10, 10, 10)
                .addComponent(lblPending)
                .addGap(12, 12, 12)
                .addComponent(jLabel19)
                .addGap(18, 18, 18))
        );

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel17.setPreferredSize(new java.awt.Dimension(165, 101));

        lblProses.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblProses.setForeground(new java.awt.Color(255, 102, 0));
        lblProses.setText("Nilai");

        jLabel22.setText("Proses");

        jLabel23.setText("Pesanan");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel22)
                    .addComponent(lblProses))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addGap(10, 10, 10)
                .addComponent(lblProses)
                .addGap(12, 12, 12)
                .addComponent(jLabel23)
                .addGap(18, 18, 18))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel18.setPreferredSize(new java.awt.Dimension(165, 101));

        lblProduksi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblProduksi.setForeground(new java.awt.Color(255, 102, 0));
        lblProduksi.setText("Nilai");

        jLabel24.setText("Produksi");

        jLabel25.setText("Pesanan");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel24)
                    .addComponent(lblProduksi))
                .addContainerGap(95, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addGap(10, 10, 10)
                .addComponent(lblProduksi)
                .addGap(12, 12, 12)
                .addComponent(jLabel25)
                .addGap(18, 18, 18))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel19.setPreferredSize(new java.awt.Dimension(165, 101));

        lblSelesai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblSelesai.setForeground(new java.awt.Color(255, 102, 0));
        lblSelesai.setText("Nilai");

        jLabel26.setText("Selesai");

        jLabel27.setText("Pesanan");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel26)
                    .addComponent(lblSelesai))
                .addContainerGap(97, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addGap(10, 10, 10)
                .addComponent(lblSelesai)
                .addGap(12, 12, 12)
                .addComponent(jLabel27)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout HalPesananLayout = new javax.swing.GroupLayout(HalPesanan);
        HalPesanan.setLayout(HalPesananLayout);
        HalPesananLayout.setHorizontalGroup(
            HalPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalPesananLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(HalPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(HalPesananLayout.createSequentialGroup()
                        .addComponent(jPanel16, 161, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel17, 161, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel18, 161, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jPanel19, 161, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18))
        );
        HalPesananLayout.setVerticalGroup(
            HalPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalPesananLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(HalPesananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel2.add(HalPesanan, "card8");

        HalProduk.setBackground(new java.awt.Color(255, 255, 255));
        HalProduk.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel23.setBackground(new java.awt.Color(51, 51, 51));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel23.setPreferredSize(new java.awt.Dimension(740, 410));

        TabelProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Produk", "Foto", "Nama Produk", "Kategori", "Harga", "Stok", "Ukuran", "Deskripsi"
            }
        ));
        TabelProduk.setGridColor(new java.awt.Color(255, 255, 255));
        TabelProduk.setRowHeight(100);
        TabelProduk.setRowMargin(5);
        jScrollPane2.setViewportView(TabelProduk);

        cariproduk.addActionListener(this::cariprodukActionPerformed);
        cariproduk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariprodukKeyReleased(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(this::btnCariActionPerformed);

        cmbKategoriProduk.setBackground(new java.awt.Color(51, 102, 255));
        cmbKategoriProduk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbKategoriProduk.setForeground(new java.awt.Color(255, 255, 255));
        cmbKategoriProduk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Kategori", "Kaos", "Hoodie", "Mug", "Sticker", "Totebag" }));
        cmbKategoriProduk.addActionListener(this::cmbKategoriProdukActionPerformed);

        btnEditProduk.setBackground(new java.awt.Color(102, 153, 255));
        btnEditProduk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEditProduk.setForeground(new java.awt.Color(255, 255, 255));
        btnEditProduk.setText("EDIT");
        btnEditProduk.addActionListener(this::btnEditProdukActionPerformed);

        btnDeleteProduk.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteProduk.setText("DELETE");
        btnDeleteProduk.addActionListener(this::btnDeleteProdukActionPerformed);

        jPanel9.setBackground(new java.awt.Color(255, 102, 51));
        jPanel9.setForeground(new java.awt.Color(255, 255, 255));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("DAFTAR PRODUK");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(282, 282, 282)
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel28)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(cariproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                        .addComponent(cmbKategoriProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(btnEditProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariproduk, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKategoriProduk))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteProduk, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jButton10.setBackground(new java.awt.Color(255, 102, 0));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("+ Tambah Produk");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 102, 51));
        jLabel30.setText("Kelola Produk");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setText("PRODUK");

        javax.swing.GroupLayout HalProdukLayout = new javax.swing.GroupLayout(HalProduk);
        HalProduk.setLayout(HalProdukLayout);
        HalProdukLayout.setHorizontalGroup(
            HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalProdukLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(HalProdukLayout.createSequentialGroup()
                        .addGroup(HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        HalProdukLayout.setVerticalGroup(
            HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalProdukLayout.createSequentialGroup()
                .addGroup(HalProdukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HalProdukLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HalProdukLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(HalProduk, "card7");

        HalPembayaran.setBackground(new java.awt.Color(255, 255, 255));
        HalPembayaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel15.setBackground(new java.awt.Color(0, 0, 0));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel15.setPreferredSize(new java.awt.Dimension(740, 410));

        TabelPembayaran.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(TabelPembayaran);

        caripemasukan.addActionListener(this::caripemasukanActionPerformed);
        caripemasukan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                caripemasukanKeyReleased(evt);
            }
        });

        jPanel20.setBackground(new java.awt.Color(255, 102, 51));
        jPanel20.setForeground(new java.awt.Color(255, 255, 255));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("RIWAYAT PEMBAYARAN");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(269, Short.MAX_VALUE)
                .addComponent(jLabel32)
                .addGap(254, 254, 254))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel32)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        btnPesanan2.setText("Cari");
        btnPesanan2.addActionListener(this::btnPesanan2ActionPerformed);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(caripemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPesanan2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(caripemasukan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesanan2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout HalPembayaranLayout = new javax.swing.GroupLayout(HalPembayaran);
        HalPembayaran.setLayout(HalPembayaranLayout);
        HalPembayaranLayout.setHorizontalGroup(
            HalPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalPembayaranLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        HalPembayaranLayout.setVerticalGroup(
            HalPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalPembayaranLayout.createSequentialGroup()
                .addContainerGap(78, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel2.add(HalPembayaran, "card6");

        HalProduksi.setBackground(new java.awt.Color(255, 255, 255));
        HalProduksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel26.setBackground(new java.awt.Color(51, 51, 51));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel26.setForeground(new java.awt.Color(51, 51, 51));
        jPanel26.setPreferredSize(new java.awt.Dimension(740, 410));

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

        btnProses.setBackground(new java.awt.Color(102, 153, 255));
        btnProses.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProses.setForeground(new java.awt.Color(255, 255, 255));
        btnProses.setText("PROSES");
        btnProses.addActionListener(this::btnProsesActionPerformed);

        btnSelesai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSelesai.setText("SELESAI");
        btnSelesai.addActionListener(this::btnSelesaiActionPerformed);

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

        jPanel21.setBackground(new java.awt.Color(255, 102, 51));
        jPanel21.setForeground(new java.awt.Color(255, 255, 255));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("DAFTAR PRODUKSI");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(jLabel33)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel33)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        cmbStatusProduksi.setBackground(new java.awt.Color(51, 102, 255));
        cmbStatusProduksi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbStatusProduksi.setForeground(new java.awt.Color(255, 255, 255));
        cmbStatusProduksi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Pending", "Diproses", "Produksi", "Selesai" }));
        cmbStatusProduksi.addActionListener(this::cmbStatusProduksiActionPerformed);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel26Layout.createSequentialGroup()
                                .addComponent(cariproduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnPesanan1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cmbStatusProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPesanan1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cariproduksi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStatusProduksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelesai, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout HalProduksiLayout = new javax.swing.GroupLayout(HalProduksi);
        HalProduksi.setLayout(HalProduksiLayout);
        HalProduksiLayout.setHorizontalGroup(
            HalProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalProduksiLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        HalProduksiLayout.setVerticalGroup(
            HalProduksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalProduksiLayout.createSequentialGroup()
                .addContainerGap(79, Short.MAX_VALUE)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel2.add(HalProduksi, "card5");

        HalPelanggan.setBackground(new java.awt.Color(255, 255, 255));
        HalPelanggan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel24.setBackground(new java.awt.Color(51, 51, 51));
        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel24.setPreferredSize(new java.awt.Dimension(740, 350));

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

        caripelanggan.addActionListener(this::caripelangganActionPerformed);
        caripelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                caripelangganKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                caripelangganKeyReleased(evt);
            }
        });

        jPanel28.setBackground(new java.awt.Color(255, 102, 51));
        jPanel28.setForeground(new java.awt.Color(255, 255, 255));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("DAFTAR PELANGGAN");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(jLabel34)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel34)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        btnPesanan3.setText("Cari");
        btnPesanan3.addActionListener(this::btnPesanan3ActionPerformed);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(caripelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPesanan3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(caripelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesanan3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout HalPelangganLayout = new javax.swing.GroupLayout(HalPelanggan);
        HalPelanggan.setLayout(HalPelangganLayout);
        HalPelangganLayout.setHorizontalGroup(
            HalPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalPelangganLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        HalPelangganLayout.setVerticalGroup(
            HalPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalPelangganLayout.createSequentialGroup()
                .addContainerGap(83, Short.MAX_VALUE)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jPanel2.add(HalPelanggan, "card4");

        HalLaporan.setBackground(new java.awt.Color(255, 255, 255));
        HalLaporan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel27.setBackground(new java.awt.Color(51, 51, 51));
        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.setPreferredSize(new java.awt.Dimension(740, 350));

        btnCetakLaporan.setBackground(new java.awt.Color(102, 153, 255));
        btnCetakLaporan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCetakLaporan.setForeground(new java.awt.Color(255, 255, 255));
        btnCetakLaporan.setText("CETAK LAPORAN");
        btnCetakLaporan.addActionListener(this::btnCetakLaporanActionPerformed);

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

        cmbTahun.setBackground(new java.awt.Color(51, 153, 255));
        cmbTahun.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbTahun.setForeground(new java.awt.Color(255, 255, 255));
        cmbTahun.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Tahun", "2024", "2025", "2026", " " }));
        cmbTahun.addActionListener(this::cmbTahunActionPerformed);

        cmbBulan.setBackground(new java.awt.Color(0, 153, 255));
        cmbBulan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmbBulan.setForeground(new java.awt.Color(255, 255, 255));
        cmbBulan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember", " " }));
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

        jPanel3.setBackground(java.awt.SystemColor.controlHighlight);
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblTotalPemasukan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(carilaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68)
                        .addComponent(cmbTahun, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(cmbBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCetakLaporan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carilaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTahun)
                    .addComponent(cmbBulan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCetakLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setPreferredSize(new java.awt.Dimension(165, 100));

        jLabel31.setText("Total Pemasukan");

        lblTotalPemasukanLap.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalPemasukanLap.setForeground(new java.awt.Color(255, 102, 0));
        lblTotalPemasukanLap.setText("Nilai");

        jLabel36.setText("Pemasukan");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(lblTotalPemasukanLap)
                    .addComponent(jLabel31))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalPemasukanLap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel36)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(165, 100));

        jLabel37.setText("Total Produk");

        lblTotalProdukLap.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalProdukLap.setForeground(new java.awt.Color(255, 102, 0));
        lblTotalProdukLap.setText("Nilai");

        jLabel39.setText("Pesanan");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(lblTotalProdukLap)
                    .addComponent(jLabel37))
                .addContainerGap(78, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalProdukLap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel39)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setPreferredSize(new java.awt.Dimension(165, 100));

        jLabel40.setText("Total Pesanan");

        lblTotalPesananLap.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalPesananLap.setForeground(new java.awt.Color(255, 102, 0));
        lblTotalPesananLap.setText("Nilai");

        jLabel42.setText("Pesanan");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42)
                    .addComponent(lblTotalPesananLap)
                    .addComponent(jLabel40))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalPesananLap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel42)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setPreferredSize(new java.awt.Dimension(165, 100));

        jLabel43.setText("Produk Terjual");

        lblProdukTerjualLap.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblProdukTerjualLap.setForeground(new java.awt.Color(255, 102, 0));
        lblProdukTerjualLap.setText("Nilai");

        jLabel45.setText("Pesanan");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45)
                    .addComponent(lblProdukTerjualLap)
                    .addComponent(jLabel43))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblProdukTerjualLap)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel45)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 102, 0));
        jLabel46.setText("Laporan Pemasukan");

        javax.swing.GroupLayout HalLaporanLayout = new javax.swing.GroupLayout(HalLaporan);
        HalLaporan.setLayout(HalLaporanLayout);
        HalLaporanLayout.setHorizontalGroup(
            HalLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HalLaporanLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(HalLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addGroup(HalLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(HalLaporanLayout.createSequentialGroup()
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(28, 28, 28)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(27, 27, 27)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        HalLaporanLayout.setVerticalGroup(
            HalLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HalLaporanLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(HalLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.add(HalLaporan, "card3");

        HalPengeluaran.setBackground(new java.awt.Color(255, 255, 255));
        HalPengeluaran.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout HalPengeluaranLayout = new javax.swing.GroupLayout(HalPengeluaran);
        HalPengeluaran.setLayout(HalPengeluaranLayout);
        HalPengeluaranLayout.setHorizontalGroup(
            HalPengeluaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 771, Short.MAX_VALUE)
        );
        HalPengeluaranLayout.setVerticalGroup(
            HalPengeluaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 559, Short.MAX_VALUE)
        );

        jPanel2.add(HalPengeluaran, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE))
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
                int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {

            loginForm login = new loginForm();
            login.setVisible(true);

            this.dispose();
        }
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

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
        // TODO add your handling code here:
            try {

                int row = TabelProduksi.getSelectedRow();

                if(row == -1){

                    JOptionPane.showMessageDialog(
                        null,
                        "Pilih pesanan terlebih dahulu"
                    );

                    return;
                }

                int idPesanan = Integer.parseInt(
                    TabelProduksi.getValueAt(row, 0).toString()
                );

                String statusSekarang =
                TabelProduksi.getValueAt(row, 6)
                .toString();

                String statusBaru = "";

                Connection conn = koneksi.getConnection();

                conn.setAutoCommit(false);

                try {

                    // Pending → Diproses
                    if(statusSekarang.equalsIgnoreCase("pending")){

                        statusBaru = "diproses";

                    }

                    // Diproses → Produksi
                    else if(statusSekarang.equalsIgnoreCase("diproses")){

                        statusBaru = "produksi";

                        String sqlProduk =
                        "SELECT id_produk, jumlah " +
                        "FROM detail_pesanan " +
                        "WHERE id_pesanan=?";

                        PreparedStatement pstProduk =
                        conn.prepareStatement(sqlProduk);

                        pstProduk.setInt(1, idPesanan);

                        ResultSet rsProduk =
                        pstProduk.executeQuery();

                        while(rsProduk.next()){

                            int idProduk =
                            rsProduk.getInt("id_produk");

                            int jumlah =
                            rsProduk.getInt("jumlah");

                            // CEK STOK
                            String sqlCek =
                            "SELECT stok " +
                            "FROM produk " +
                            "WHERE id_produk=?";

                            PreparedStatement pstCek =
                            conn.prepareStatement(sqlCek);

                            pstCek.setInt(1, idProduk);

                            ResultSet rsCek =
                            pstCek.executeQuery();

                            if(rsCek.next()){

                                int stok =
                                rsCek.getInt("stok");

                                if(stok < jumlah){

                                    JOptionPane.showMessageDialog(
                                        null,
                                        "Stok produk tidak mencukupi!"
                                    );

                                    conn.rollback();
                                    return;
                                }
                            }

                            // KURANGI STOK
                            String sqlUpdateStok =
                            "UPDATE produk " +
                            "SET stok = stok - ? " +
                            "WHERE id_produk=?";

                            PreparedStatement pstUpdate =
                            conn.prepareStatement(sqlUpdateStok);

                            pstUpdate.setInt(1, jumlah);
                            pstUpdate.setInt(2, idProduk);

                            pstUpdate.executeUpdate();
                        }
                    }

                    // Sudah Produksi
                    else if(statusSekarang.equalsIgnoreCase("produksi")){

                        JOptionPane.showMessageDialog(
                            null,
                            "Pesanan sudah berada di tahap produksi"
                        );

                        conn.rollback();
                        return;
                    }

                    // Sudah Selesai
                    else if(statusSekarang.equalsIgnoreCase("selesai")){

                        JOptionPane.showMessageDialog(
                            null,
                            "Pesanan sudah selesai"
                        );

                        conn.rollback();
                        return;
                    }

                    // UPDATE STATUS PESANAN
                    String sql =
                    "UPDATE pesanan " +
                    "SET status_pesanan=? " +
                    "WHERE id_pesanan=?";

                    PreparedStatement pst =
                    conn.prepareStatement(sql);

                    pst.setString(1, statusBaru);
                    pst.setInt(2, idPesanan);

                    pst.executeUpdate();

                    conn.commit();

                    JOptionPane.showMessageDialog(
                        null,
                        "Status berhasil diubah menjadi " +
                        statusBaru
                    );

                    DataProduksi();

                } catch(Exception e){

                    conn.rollback();

                    JOptionPane.showMessageDialog(
                        null,
                        "Error : " + e.getMessage()
                    );
                }

                conn.setAutoCommit(true);

            } catch(Exception e){

                JOptionPane.showMessageDialog(
                    null,
                    e.getMessage()
                );

                e.printStackTrace();
            }
    }//GEN-LAST:event_btnProsesActionPerformed

    private void btnSelesaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelesaiActionPerformed
        // TODO add your handling code here:
            try {

                int row = TabelProduksi.getSelectedRow();

                if(row == -1){

                    JOptionPane.showMessageDialog(
                        null,
                        "Pilih pesanan terlebih dahulu"
                    );

                    return;
                }

                int idPesanan = Integer.parseInt(
                    TabelProduksi.getValueAt(row, 0).toString()
                );

                String status = TabelProduksi
                        .getValueAt(row, 6)
                        .toString();

                // Hanya boleh selesai jika status produksi
                if(!status.equalsIgnoreCase("produksi")){

                    JOptionPane.showMessageDialog(
                        null,
                        "Pesanan harus berada pada status PRODUKSI terlebih dahulu!"
                    );

                    return;
                }

                String sql =
                "UPDATE pesanan " +
                "SET status_pesanan='selesai' " +
                "WHERE id_pesanan=?";

                Connection conn =
                koneksi.getConnection();

                PreparedStatement pst =
                conn.prepareStatement(sql);

                pst.setInt(1, idPesanan);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(
                    null,
                    "Produksi selesai"
                );

                DataProduksi();

            } catch(Exception e){

                JOptionPane.showMessageDialog(
                    null,
                    e.getMessage()
                );

            }
    }//GEN-LAST:event_btnSelesaiActionPerformed

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
        DataProduksi();
    }//GEN-LAST:event_btnPesanan1ActionPerformed

    private void btnCetakLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakLaporanActionPerformed
        // TODO add your handling code here:
             try {

                String bulan = cmbBulan.getSelectedItem().toString();
                String tahun = cmbTahun.getSelectedItem().toString();

                MessageFormat header = new MessageFormat(
                        "LAPORAN PENJUALAN" 
                );

                MessageFormat footer = new MessageFormat(
                        lblTotalPemasukan.getText() 
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

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:

        kelolaproduk_admin tambah = new kelolaproduk_admin(this);
        tambah.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void btnDeleteProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProdukActionPerformed
        // TODO add your handling code here:
        int row = TabelProduk.getSelectedRow();

            if(row == -1){

                JOptionPane.showMessageDialog(
                    null,
                    "Pilih produk terlebih dahulu!"
                );

                return;
            }

            int idProduk = Integer.parseInt(
                TabelProduk.getValueAt(row, 0).toString()
            );

            try {

                Connection conn = koneksi.getConnection();

                String cekSql =
                "SELECT COUNT(*) AS total " +
                "FROM detail_pesanan " +
                "WHERE id_produk=?";

                PreparedStatement cekPst =
                conn.prepareStatement(cekSql);

                cekPst.setInt(1, idProduk);

                ResultSet rs = cekPst.executeQuery();

                if(rs.next()){

                    int total = rs.getInt("total");

                    if(total > 0){

                        JOptionPane.showMessageDialog(
                            null,
                            "Produk tidak dapat dihapus karena sudah pernah dipesan!"
                        );

                        return;
                    }
                }

                int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin ingin menghapus produk ini?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
                );

                if(confirm == JOptionPane.YES_OPTION){

                    String sql =
                    "DELETE FROM produk WHERE id_produk=?";

                    PreparedStatement pst =
                    conn.prepareStatement(sql);

                    pst.setInt(1, idProduk);

                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(
                        null,
                        "Produk berhasil dihapus!"
                    );

                    DataProduk();
                }

            } catch(Exception e){

                JOptionPane.showMessageDialog(
                    null,
                    "Error: " + e.getMessage()
                );
            }
    }//GEN-LAST:event_btnDeleteProdukActionPerformed

    private void btnEditProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditProdukActionPerformed
        // TODO add your handling code here:
        int row = TabelProduk.getSelectedRow();

        if(row == -1){

            JOptionPane.showMessageDialog(
                null,
                "Pilih produk terlebih dahulu!");

            return;
        }

        int id = Integer.parseInt(TabelProduk.getValueAt(row,0).toString());

        String nama = TabelProduk.getValueAt(row,2).toString();
        String kategori = TabelProduk.getValueAt(row,3).toString();
        String harga = TabelProduk.getValueAt(row,4).toString();
        String stok = TabelProduk.getValueAt(row,5).toString();
        String ukuran = TabelProduk.getValueAt(row,6).toString();
        String deskripsi = TabelProduk.getValueAt(row,7).toString();

        kelolaproduk_admin form = new kelolaproduk_admin(this);

        form.setDataEdit(
            id,
            nama,
            kategori,
            harga,
            stok,
            ukuran,
            deskripsi
        );

        form.setVisible(true);

        dispose();
    }//GEN-LAST:event_btnEditProdukActionPerformed

    private void cmbKategoriProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKategoriProdukActionPerformed
        // TODO add your handling code here:
        DataProduk();
    }//GEN-LAST:event_cmbKategoriProdukActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        {

            DataProduk();

        }
    }//GEN-LAST:event_btnCariActionPerformed

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

    private void btnPesanan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesanan2ActionPerformed
        // TODO add your handling code here:
        
        DataPembayaran();
        
    }//GEN-LAST:event_btnPesanan2ActionPerformed

    private void btnPesanan3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesanan3ActionPerformed
        // TODO add your handling code here:
        
        DataPelanggan();
    }//GEN-LAST:event_btnPesanan3ActionPerformed

    private void cmbStatusProduksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStatusProduksiActionPerformed
        // TODO add your handling code here:
        DataProduksi();
    }//GEN-LAST:event_cmbStatusProduksiActionPerformed

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
    private javax.swing.JTable TabelDashboard;
    private javax.swing.JTable TabelLaporan;
    private javax.swing.JTable TabelPelanggan;
    private javax.swing.JTable TabelPembayaran;
    private javax.swing.JTable TabelPesanan;
    private javax.swing.JTable TabelProduk;
    private javax.swing.JTable TabelProduksi;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnCetakLaporan;
    private javax.swing.JButton btnDeleteProduk;
    private javax.swing.JButton btnEditProduk;
    private javax.swing.JButton btnLaporan;
    private javax.swing.JButton btnPesanan;
    private javax.swing.JButton btnPesanan1;
    private javax.swing.JButton btnPesanan2;
    private javax.swing.JButton btnPesanan3;
    private javax.swing.JButton btnProses;
    private javax.swing.JButton btnSelesai;
    private javax.swing.JTextField carilaporan;
    private javax.swing.JTextField caripelanggan;
    private javax.swing.JTextField caripemasukan;
    private javax.swing.JTextField caripesanan;
    private javax.swing.JTextField cariproduk;
    private javax.swing.JTextField cariproduksi;
    private javax.swing.JComboBox<String> cmbBulan;
    private javax.swing.JComboBox<String> cmbKategoriProduk;
    private javax.swing.JComboBox<String> cmbStatusProduksi;
    private javax.swing.JComboBox<String> cmbTahun;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
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
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
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
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JLabel lblMenungguPembayaran;
    private javax.swing.JLabel lblPending;
    private javax.swing.JLabel lblProdukTerjual;
    private javax.swing.JLabel lblProdukTerjualLap;
    private javax.swing.JLabel lblProduksi;
    private javax.swing.JLabel lblProses;
    private javax.swing.JLabel lblSelesai;
    private javax.swing.JLabel lblTotalPemasukan;
    private javax.swing.JLabel lblTotalPemasukanLap;
    private javax.swing.JLabel lblTotalPesanan;
    private javax.swing.JLabel lblTotalPesananLap;
    private javax.swing.JLabel lblTotalProduk;
    private javax.swing.JLabel lblTotalProdukLap;
    // End of variables declaration//GEN-END:variables
}
