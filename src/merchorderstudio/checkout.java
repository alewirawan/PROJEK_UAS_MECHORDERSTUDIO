/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package merchorderstudio;

import checkout.Checkoutsession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import config.koneksi;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import utility.sessions;

/**
 *
 * @author Ratih Nawang Wulan
 */
public class checkout extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(checkout.class.getName());

    /**
     * Creates new form checkout
     */
    public checkout() {
        initComponents();
        setLocationRelativeTo(null);
        loadDataPenerima();
        TampilkanDetailPesanan();
    }
    
    public void loadDataPenerima(){
        jTextField1.setText(sessions.nama);
        
        Connection conn = koneksi.getConnection();
        String sql = "SELECT no_telp, alamat FROM users WHERE id_user = ?";
        
        try(PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setInt(1, sessions.idUser);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
            jTextField2.setText(rs.getString("no_telp"));
            jTextField3.setText(rs.getString("alamat"));
            } 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data alamat: " + e.getMessage());
        }
    }
    
    public void TampilkanDetailPesanan() {
        panelCheckout.removeAll();
        
        if (Checkoutsession.listKeranjang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada produk yang dipilih.");
            return;
        }
        
        try {
            StringBuilder tandaTanya = new StringBuilder();
            for (int i = 0; i < Checkoutsession.listKeranjang.size(); i++) {
                tandaTanya.append("?");
                if (i < Checkoutsession.listKeranjang.size() - 1) {
                    tandaTanya.append(",");
                }
            }
            
            String sql = "SELECT k.id_keranjang, p.nama_produk, k.ukuran, k.jumlah, p.harga, p.foto_produk, k.upload_desain " 
                    + "FROM keranjang k " 
                    + "INNER JOIN produk p ON k.id_produk = p.id_produk " 
                    + "WHERE k.id_keranjang IN (" + tandaTanya.toString() + ")";
            
            Connection conn = koneksi.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            
            for (int i = 0; i < Checkoutsession.listKeranjang.size(); i++) {
                pst.setInt(i + 1, Checkoutsession.listKeranjang.get(i));
            }
            
            ResultSet rs = pst.executeQuery();
            double subtotalKeseluruhan = 0;
            
            while (rs.next()) {
                double harga = rs.getDouble("harga");
                int jumlah = rs.getInt("jumlah");
                double subtotalItem = harga * jumlah;
                subtotalKeseluruhan += subtotalItem;
                panelCheckout.add(CreatePanelItemCheckout(rs, subtotalItem));
            }
            
            jLabel16.setText("Rp " + keranjang.FormatRupiah.format(subtotalKeseluruhan));
            double ongkir = 15000; // Contoh tarif ongkir statis, atau set 0 jika belum ada logika ongkir
            jLabel17.setText("Rp " + keranjang.FormatRupiah.format(ongkir));
            
            double totalAkhir = subtotalKeseluruhan + ongkir;
            jLabel18.setText("Rp " + keranjang.FormatRupiah.format(totalAkhir));
            
            panelCheckout.revalidate();
            panelCheckout.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat detail pesanan: " + e.getMessage());
        }
    }
    
    private JPanel CreatePanelItemCheckout(ResultSet rs, double subtotalItem) {
        JPanel panelItem = new JPanel();
        
        try {
            panelItem.setBackground(Color.WHITE);
            panelItem.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
            panelItem.setPreferredSize(new Dimension(570, 90));
            panelItem.setMinimumSize(new Dimension(570, 90));
            panelItem.setMaximumSize(new Dimension(570, 90));
            panelItem.setLayout(null);
            panelItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel lblFoto = new JLabel();
            lblFoto.setBounds(15, 15, 80, 80);
            lblFoto.setHorizontalAlignment(JLabel.CENTER);
            lblFoto.setBorder(BorderFactory.createLineBorder(new Color(240,240,240)));
            byte[] img = rs.getBytes("foto_produk");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img);
                Image image = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(image));
            } else {
                lblFoto.setText("No Photo");
            }
            panelItem.add(lblFoto);

            JLabel lblNama = new JLabel(rs.getString("nama_produk"));
            lblNama.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblNama.setBounds(120, 12, 280, 20);
            panelItem.add(lblNama);

            String ukuran = rs.getString("ukuran");
            JLabel lblUkuran = new JLabel("Ukuran: " + (ukuran == null ? "-" : ukuran));
            lblUkuran.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblUkuran.setBounds(120, 35, 280, 18);
            panelItem.add(lblUkuran);
            
            String fileDesain = rs.getString("upload_desain");
            JLabel lblDesain = new JLabel("File Desain: " + (fileDesain == null || fileDesain.isEmpty() ? "Tidak ada" : fileDesain));
            lblDesain.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDesain.setForeground(Color.GRAY);
            lblDesain.setBounds(120, 55, 280, 18);
            panelItem.add(lblDesain);

            double harga = rs.getDouble("harga");
            JLabel lblHarga = new JLabel("Harga: Rp " + keranjang.FormatRupiah.format(harga));
            lblHarga.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblHarga.setHorizontalAlignment(JLabel.RIGHT);
            lblHarga.setBounds(420, 15, 200, 18);
            panelItem.add(lblHarga);

            int jumlah = rs.getInt("jumlah");
            JLabel lblJumlah = new JLabel("Jumlah Pembelian: " + jumlah);
            lblJumlah.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblJumlah.setHorizontalAlignment(JLabel.RIGHT);
            lblJumlah.setBounds(420, 38, 200, 18);
            panelItem.add(lblJumlah);

            JLabel lblSub = new JLabel("Subtotal: Rp " + keranjang.FormatRupiah.format(subtotalItem));
            lblSub.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblSub.setForeground(new Color(255, 102, 0));
            lblSub.setHorizontalAlignment(JLabel.RIGHT);
            lblSub.setBounds(420, 65, 200, 20);
            panelItem.add(lblSub);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return panelItem;
    }
    
    private void pilihMetodeBayar(javax.swing.JRadioButton yangDipilih) {
        javax.swing.JRadioButton[] semuaMetode = {jRadioButton1, jRadioButton2, jRadioButton3};
        
        for (javax.swing.JRadioButton rb : semuaMetode) {
            if (rb != yangDipilih) {
                rb.setSelected(false);
            }
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelCheckout = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 102, 0));
        jLabel1.setText("Checkout");

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 14)); // NOI18N
        jLabel2.setText("Alamat Pengiriman");

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 14)); // NOI18N
        jLabel3.setText("Detail Pesanan");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jButton1.setText("Ubah");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jTextField1.setText("Nama Pemesan");
        jTextField1.setBorder(null);
        jTextField1.setFocusable(false);

        jTextField2.setText("No HP");
        jTextField2.setBorder(null);
        jTextField2.setFocusable(false);

        jTextField3.setText("Alamat");
        jTextField3.setBorder(null);
        jTextField3.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                    .addComponent(jTextField3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jLabel13.setText("Subtotal ");

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jLabel14.setText("Ongkir");

        jLabel15.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jLabel15.setText("Total");

        jLabel16.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jLabel16.setText("Rp");

        jLabel17.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jLabel17.setText("Rp");

        jLabel18.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jLabel18.setText("Rp");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel19.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 14)); // NOI18N
        jLabel19.setText("Metode Pembayaran ");

        jRadioButton1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jRadioButton1.setText("Transfer Bank");
        jRadioButton1.addActionListener(this::jRadioButton1ActionPerformed);

        jRadioButton2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jRadioButton2.setText("E-Wallet (OVO/Dana/Gopay)");
        jRadioButton2.addActionListener(this::jRadioButton2ActionPerformed);

        jRadioButton3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jRadioButton3.setText("COD (Bayar ditempat)");
        jRadioButton3.addActionListener(this::jRadioButton3ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton3)
                    .addComponent(jLabel19)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton1))
                .addContainerGap(406, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jButton2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 14)); // NOI18N
        jButton2.setText("Bayar Sekarang");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        panelCheckout.setLayout(new javax.swing.BoxLayout(panelCheckout, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(panelCheckout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(268, 268, 268))
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))
                        .addGap(169, 169, 169))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(14, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (jButton1.getText().equals("Ubah")) {
        jButton1.setText("Save");
        
        jTextField1.setFocusable(true);
        jTextField2.setFocusable(true);
        jTextField3.setFocusable(true);
        
        jTextField1.setEditable(true);
        jTextField2.setEditable(true);
        jTextField3.setEditable(true);
        
        jTextField1.setOpaque(true);
        jTextField2.setOpaque(true);
        jTextField3.setOpaque(true);
        
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.LIGHT_GRAY));
        
        jTextField1.revalidate();
        jTextField2.revalidate();
        jTextField3.revalidate();
        
        jTextField1.requestFocus();
        } else {
        jButton1.setText("Ubah");
        
        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
        jTextField3.setEditable(false);
        
        jTextField1.setFocusable(false);
        jTextField2.setFocusable(false);
        jTextField3.setFocusable(false);
        
        jTextField1.setBorder(null);
        jTextField2.setBorder(null);
        jTextField3.setBorder(null);
        
        jTextField1.setOpaque(false);
        jTextField2.setOpaque(false);
        jTextField3.setOpaque(false);
        
        JOptionPane.showMessageDialog(this, "Data Penerima Berhasil Diperbarui");
        }
        this.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        pilihMetodeBayar(jRadioButton1);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        pilihMetodeBayar(jRadioButton2);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        pilihMetodeBayar(jRadioButton3);
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String metodePembayaran = "";
        if (jRadioButton1.isSelected()) {
            metodePembayaran = "Transfer Bank";
        } else if (jRadioButton2.isSelected()) {
            metodePembayaran = "E-Wallet";
        } else if (jRadioButton3.isSelected()) {
            metodePembayaran = "COD";
        } else {
            JOptionPane.showMessageDialog(this, "Pilih Metode Pembayaran!");
            return;
        }
    
        int idUser = sessions.idUser;
        String alamat = jTextField3.getText().trim();
    
        if (alamat.isEmpty() || alamat.equals("Alamat")) {
            JOptionPane.showMessageDialog(this, "Alamat pengiriman tidak boleh kosong!");
            return;
        }
    
        double totalHarga = 0;
        try {
            String cleanTotal = jLabel18.getText()
                    .replace("Rp", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();
            totalHarga = Double.parseDouble(cleanTotal);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Gagal memproses total harga");
            return;
        }
    
        String statusAwal = "Pending";
        Connection conn = koneksi.getConnection();
        
        try {
            conn.setAutoCommit(false);
        
            String queryPesanan = "INSERT INTO pesanan (id_user, alamat, total_harga, metode_pembayaran, status_pesanan, tanggal_pesan) VALUES (?, ?, ?, ?, ?, NOW())";
            int idPesananBaru = 0;
        
            try (PreparedStatement pstPesanan = conn.prepareStatement(queryPesanan, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstPesanan.setInt(1, idUser);
                pstPesanan.setString(2, alamat);
                pstPesanan.setDouble(3, totalHarga);
                pstPesanan.setString(4, metodePembayaran);
                pstPesanan.setString(5, statusAwal);
            
                int rowsInserted = pstPesanan.executeUpdate();
            
                if (rowsInserted > 0) {
                    try (java.sql.ResultSet rsKeys = pstPesanan.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idPesananBaru = rsKeys.getInt(1);
                        }
                    }
                }
            }
            if (idPesananBaru == 0) {
                throw new java.sql.SQLException("Gagal membuat data pesanan utama.");
            }
            
            String queryPindahkanDetail = "INSERT INTO detail_pesanan (id_pesanan, id_produk, jumlah, ukuran, upload_desain, catatan) "
                + "SELECT ?, id_produk, jumlah, ukuran, upload_desain, catatan FROM keranjang WHERE id_user = ?";
            
            try (PreparedStatement pstDetail = conn.prepareStatement(queryPindahkanDetail)) {
                pstDetail.setInt(1, idPesananBaru);
                pstDetail.setInt(2, idUser);
                pstDetail.executeUpdate();
            }
            
            String queryHapusKeranjang = "DELETE FROM keranjang WHERE id_user = ?";
            try (PreparedStatement pstHapus = conn.prepareStatement(queryHapusKeranjang)) {
                pstHapus.setInt(1, idUser);
                pstHapus.executeUpdate();
            }
            
            conn.commit();
            
            JOptionPane.showMessageDialog(this, "Pesanan Anda telah dibuat");
            
            new status_pesanan(idPesananBaru).setVisible(true);
            this.dispose();
        } catch (java.sql.SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (java.sql.SQLException ex) {
                java.util.logging.Logger.getLogger(this.getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            java.util.logging.Logger.getLogger(this.getClass().getName()).log(java.util.logging.Level.SEVERE, null, e);
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (java.sql.SQLException ex) {
                java.util.logging.Logger.getLogger(this.getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new checkout().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel panelCheckout;
    // End of variables declaration//GEN-END:variables
}
