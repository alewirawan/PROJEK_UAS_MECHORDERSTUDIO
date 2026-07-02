/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package merchorderstudio;
import java.sql.*;
import java.awt.*;
import javax.swing.*;
import utility.sessions;

/**
 *
 * @author Mahesa
 */
public class detail_produk extends javax.swing.JDialog {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(detail_produk.class.getName());
 
    /**
     * Creates new form detail_produk
     */
    private int idProduk;
    private String ukuranTerpilih = "";
 
    // Constructor bawaan Netbeans (biarin aja buat jaga-jaga)
    public detail_produk() {
        initComponents();
    }
 
    // Constructor baru yang nerima ID Produk dari Dashboard
    public detail_produk(java.awt.Frame parent, boolean modal, int idProduk) {
        super(parent, modal);
        initComponents();
        this.idProduk = idProduk;
        loadDataProduk();
 
        // --- TAMBAHKAN INI UNTUK MEMASTIKAN TOMBOL BACK TERHUBUNG ---
        btnBack.addActionListener(e -> {
            this.dispose(); // Perintah untuk menutup JDialog
        });
    }
    
    // Fungsi utama buat ngambil data dan bikin tombol dinamis
    private void loadDataProduk() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db_merchorderstudio", "root", "");
            String sql = "SELECT * FROM produk WHERE id_produk = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, this.idProduk);
            ResultSet rs = pst.executeQuery();
 
            if (rs.next()) {
                // 1. Set Nama
                lblNama.setText(rs.getString("nama_produk"));
                
                // 2. Set Harga (Format biar rapi)
                double harga = rs.getDouble("harga");
                pnlHarga.setText(String.format("Rp %,.0f", harga)); 
 
                // 3. Set Gambar (Perbaikan ngambil kolom foto_produk bertipe BLOB)
                byte[] imgBytes = rs.getBytes("foto_produk");
                if (imgBytes != null) {
                    ImageIcon iconAsli = new ImageIcon(imgBytes);
 
                    
                    int imgWidth = iconAsli.getIconWidth();
                    int imgHeight = iconAsli.getIconHeight();
 
                    double rasio = Math.min((double) 360 / imgWidth, (double) 300 / imgHeight);
                    int lebarBaru = (int) (imgWidth * rasio);
                    int tinggiBaru = (int) (imgHeight * rasio);
 
                    Image gambarFit = iconAsli.getImage().getScaledInstance(lebarBaru, tinggiBaru, Image.SCALE_SMOOTH);
                    lblGambar.setIcon(new ImageIcon(gambarFit));
                    lblGambar.setText(""); // Hilangin teks bawaan
                }
 
                // 4. Bikin Tombol Ukuran Dinamis
                pnlUkuranList.removeAll(); 
                String dataUkuran = rs.getString("ukuran"); 
                
                if (dataUkuran != null && !dataUkuran.isEmpty()) {
                    String[] listUkuran = dataUkuran.split(",");
                    ButtonGroup grupUkuran = new ButtonGroup();
 
                    for (String ukuran : listUkuran) {
                        JToggleButton btnUkuran = new JToggleButton(ukuran.trim());
                        btnUkuran.setPreferredSize(new Dimension(60, 35));
                        btnUkuran.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        
                        btnUkuran.addActionListener(e -> {
                            ukuranTerpilih = e.getActionCommand();
                        });
 
                        grupUkuran.add(btnUkuran); 
                        pnlUkuranList.add(btnUkuran); 
                    }
                }
                
                pnlUkuranList.revalidate();
                pnlUkuranList.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data produk: " + e.getMessage());
        }
        
        // Panggil fungsi buat ngaktifin tombol-tombol
        initTombolAksi(); 
    }
 
    // Fungsi buat ngidupin tombol +, -, dan Upload
    private void initTombolAksi() {
        // Tombol Plus
        btnPlus.addActionListener(e -> {
            int jumlah = Integer.parseInt(lblJumlah.getText());
            lblJumlah.setText(String.valueOf(jumlah + 1));
        });
        
        // Tombol Min (minimal pembelian 1)
        btnMin.addActionListener(e -> {
            int jumlah = Integer.parseInt(lblJumlah.getText());
            if (jumlah > 1) {
                lblJumlah.setText(String.valueOf(jumlah - 1));
            }
        });
        
        // Tombol Upload File
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File fileTerpilih = fileChooser.getSelectedFile();
                txtNamaFile.setText(fileTerpilih.getName());
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDetailContent = new javax.swing.JPanel();
        lblGambar = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblNama = new javax.swing.JLabel();
        pnlHarga = new javax.swing.JLabel();
        pnlUkuranSection = new javax.swing.JPanel();
        lblUkuranTitle = new javax.swing.JLabel();
        pnlUkuranList = new javax.swing.JPanel();
        pnlJumlahSection = new javax.swing.JPanel();
        lblJumlahTitle = new javax.swing.JLabel();
        pnlJumlahControl = new javax.swing.JPanel();
        btnMin = new javax.swing.JButton();
        lblJumlah = new javax.swing.JLabel();
        btnPlus = new javax.swing.JButton();
        pnlUploadSection = new javax.swing.JPanel();
        lblUploadTitle = new javax.swing.JLabel();
        pnlUploadFile = new javax.swing.JPanel();
        btnUpload = new javax.swing.JButton();
        txtNamaFile = new javax.swing.JTextField();
        lblUploadHint = new javax.swing.JLabel();
        btnTambahKeranjang = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        lblCart = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        pnlDetailContent.setBackground(new java.awt.Color(255, 255, 255));

        lblGambar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGambar.setText("Gambar Produk");
        lblGambar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblGambar.setPreferredSize(new java.awt.Dimension(360, 300));

        lblNama.setText("Nama Produk");

        pnlHarga.setText("Rp.45.000");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNama)
                    .addComponent(pnlHarga))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlHarga)
                .addContainerGap())
        );

        lblUkuranTitle.setText("Pilih Ukuran");

        pnlUkuranList.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        javax.swing.GroupLayout pnlUkuranSectionLayout = new javax.swing.GroupLayout(pnlUkuranSection);
        pnlUkuranSection.setLayout(pnlUkuranSectionLayout);
        pnlUkuranSectionLayout.setHorizontalGroup(
            pnlUkuranSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUkuranSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlUkuranSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlUkuranSectionLayout.createSequentialGroup()
                        .addComponent(lblUkuranTitle)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pnlUkuranList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlUkuranSectionLayout.setVerticalGroup(
            pnlUkuranSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUkuranSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUkuranTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlUkuranList, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblJumlahTitle.setText("Jumlah");

        pnlJumlahControl.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnMin.setText("-");
        btnMin.addActionListener(this::btnMinActionPerformed);
        pnlJumlahControl.add(btnMin);

        lblJumlah.setText("1");
        pnlJumlahControl.add(lblJumlah);

        btnPlus.setText("+");
        btnPlus.addActionListener(this::btnPlusActionPerformed);
        pnlJumlahControl.add(btnPlus);

        javax.swing.GroupLayout pnlJumlahSectionLayout = new javax.swing.GroupLayout(pnlJumlahSection);
        pnlJumlahSection.setLayout(pnlJumlahSectionLayout);
        pnlJumlahSectionLayout.setHorizontalGroup(
            pnlJumlahSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJumlahSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlJumlahSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblJumlahTitle)
                    .addComponent(pnlJumlahControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlJumlahSectionLayout.setVerticalGroup(
            pnlJumlahSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJumlahSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJumlahTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlJumlahControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblUploadTitle.setText("Upload Desain");

        pnlUploadFile.setLayout(new java.awt.BorderLayout());

        btnUpload.setText("Pilih File");
        btnUpload.addActionListener(this::btnUploadActionPerformed);
        pnlUploadFile.add(btnUpload, java.awt.BorderLayout.LINE_START);

        txtNamaFile.setEditable(false);
        txtNamaFile.setBorder(null);
        pnlUploadFile.add(txtNamaFile, java.awt.BorderLayout.CENTER);

        lblUploadHint.setText("Format PNG, JPG, PDF (maks 10MB)");

        javax.swing.GroupLayout pnlUploadSectionLayout = new javax.swing.GroupLayout(pnlUploadSection);
        pnlUploadSection.setLayout(pnlUploadSectionLayout);
        pnlUploadSectionLayout.setHorizontalGroup(
            pnlUploadSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUploadSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlUploadSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlUploadFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlUploadSectionLayout.createSequentialGroup()
                        .addGroup(pnlUploadSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUploadTitle)
                            .addComponent(lblUploadHint))
                        .addGap(0, 76, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlUploadSectionLayout.setVerticalGroup(
            pnlUploadSectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUploadSectionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUploadTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlUploadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUploadHint)
                .addGap(34, 34, 34))
        );

        btnTambahKeranjang.setBackground(new java.awt.Color(255, 104, 0));
        btnTambahKeranjang.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahKeranjang.setText("Tambah Ke Keranjang ");
        btnTambahKeranjang.addActionListener(this::btnTambahKeranjangActionPerformed);

        btnBack.setText("<");
        btnBack.addActionListener(this::btnBackActionPerformed);

        lblCart.setText("🛒");

        javax.swing.GroupLayout pnlDetailContentLayout = new javax.swing.GroupLayout(pnlDetailContent);
        pnlDetailContent.setLayout(pnlDetailContentLayout);
        pnlDetailContentLayout.setHorizontalGroup(
            pnlDetailContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetailContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDetailContentLayout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCart)
                        .addGap(9, 9, 9))
                    .addGroup(pnlDetailContentLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlDetailContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblGambar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlUkuranSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlJumlahSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlUploadSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTambahKeranjang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlDetailContentLayout.setVerticalGroup(
            pnlDetailContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetailContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(lblCart, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGambar, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlUkuranSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlJumlahSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlUploadSection, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTambahKeranjang, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDetailContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlDetailContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUploadActionPerformed

    private void btnMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMinActionPerformed

    private void btnPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPlusActionPerformed

    private void btnTambahKeranjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahKeranjangActionPerformed
        // TODO add your handling code here:
        // Cek dulu apakah produk ini punya pilihan ukuran dan apakah user udah milih
        if (pnlUkuranList.getComponentCount() > 0 && ukuranTerpilih.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jangan lupa pilih ukuran bajunya dulu ya!");
            return; // Berhentiin proses kalau belum pilih ukuran
        }
 
        // Ambil data jumlah dan file desain dari tampilan
        int jumlahPesanan = Integer.parseInt(lblJumlah.getText());
        String fileDesain = txtNamaFile.getText();
 
        // Simpan beneran ke tabel keranjang (sebelumnya cuma nampilin dialog doang,
        // makanya kalau balik ke dashboard jumlah keranjangnya nggak nambah)
        merchorderstudio.dao.KeranjangDAO keranjangDAO = new merchorderstudio.dao.KeranjangDAO();
        boolean sukses = keranjangDAO.tambahItem(
                sessions.idUser,
                idProduk,
                jumlahPesanan,
                ukuranTerpilih.isEmpty() ? null : ukuranTerpilih,
                fileDesain.isEmpty() ? null : fileDesain,
                null
        );
 
        if (!sukses) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan ke keranjang, coba lagi.");
            return;
        }
 
        String pesan = "Produk berhasil ditambahkan!\n\n"
                     + "ID Produk: " + idProduk + "\n"
                     + "Ukuran: " + (ukuranTerpilih.isEmpty() ? "Tanpa Ukuran" : ukuranTerpilih) + "\n"
                     + "Jumlah: " + jumlahPesanan + "\n"
                     + "Desain: " + (fileDesain.isEmpty() ? "Tanpa Desain" : fileDesain);
 
        JOptionPane.showMessageDialog(this, pesan);
 
        // Opsional: Tutup form detail setelah berhasil nambahin ke keranjang
        // this.dispose();
    }//GEN-LAST:event_btnTambahKeranjangActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new detail_produk().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnMin;
    private javax.swing.JButton btnPlus;
    private javax.swing.JButton btnTambahKeranjang;
    private javax.swing.JButton btnUpload;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCart;
    private javax.swing.JLabel lblGambar;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblJumlahTitle;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblUkuranTitle;
    private javax.swing.JLabel lblUploadHint;
    private javax.swing.JLabel lblUploadTitle;
    private javax.swing.JPanel pnlDetailContent;
    private javax.swing.JLabel pnlHarga;
    private javax.swing.JPanel pnlJumlahControl;
    private javax.swing.JPanel pnlJumlahSection;
    private javax.swing.JPanel pnlUkuranList;
    private javax.swing.JPanel pnlUkuranSection;
    private javax.swing.JPanel pnlUploadFile;
    private javax.swing.JPanel pnlUploadSection;
    private javax.swing.JTextField txtNamaFile;
    // End of variables declaration//GEN-END:variables
}
