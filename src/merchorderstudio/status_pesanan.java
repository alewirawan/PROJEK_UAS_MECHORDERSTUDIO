/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package merchorderstudio;
import config.koneksi;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
/**
 *
 * @author Ratih Nawang Wulan
 */
public class status_pesanan extends javax.swing.JFrame {
    
    private final Color WARNA_AKTIF = new Color(40, 167, 69); // Hijau
    private final Color WARNA_MATI = new Color(180, 180, 180);  // Abu-abu
    
    private static final Logger logger = Logger.getLogger(status_pesanan.class.getName());
    /**
     * Creates new form status_pesanan
     */
public status_pesanan(int idPesanan) {
    initComponents();
    setLocationRelativeTo(null);

    eksekusiTrackingOtomatis(String.valueOf(idPesanan));
}
    private void eksekusiTrackingOtomatis(String idPesananAtauNoOrder) {

    System.out.println("Data yang dicari = " + idPesananAtauNoOrder);
        String sql = "SELECT status_pesanan, tanggal_pesan, total_harga FROM pesanan WHERE id_pesanan = ?"; 

        resetVisualTracking();

        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(idPesananAtauNoOrder.trim()));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Mengambil langsung menggunakan urutan kolom (Kolom 1 = status_pesanan)
                    String statusDatabase = rs.getString(1); 
                    Date tglPesan = rs.getTimestamp(2);
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");

                    if (statusDatabase != null) {
                        lblStatusBesar.setText(statusDatabase.toUpperCase());
                        
                        int levelStatus = 0;
                        switch (statusDatabase.toLowerCase().trim()) {
                            case "pesanan berhasil":    levelStatus = 1; break;
                            case "pembayaran berhasil": levelStatus = 2; break;
                            case "diproses":            levelStatus = 3; break;
                            case "sedang dicetak":      levelStatus = 4; break;
                            case "packing":             levelStatus = 5; break;
                            case "selesai":             levelStatus = 6; break;
                        }

                        if (levelStatus >= 1 && tglPesan != null) {
                            lblIconBerhasil.setForeground(WARNA_AKTIF);
                            lblTglBerhasil.setText(sdf.format(tglPesan));
                        }
                        if (levelStatus >= 2) {
                            lblIconBayar.setForeground(WARNA_AKTIF);
                            lblTglBayar.setText(sdf.format(tglPesan));
                        }
                        if (levelStatus >= 3 && tglPesan != null) {
                            lblIconProses.setForeground(WARNA_AKTIF);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(tglPesan);
                            cal.add(Calendar.MINUTE, 30);
                            lblTglProses.setText(sdf.format(cal.getTime()));
                        }
                        if (levelStatus >= 4 && tglPesan != null) {
                            lblIconCetak.setForeground(WARNA_AKTIF);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(tglPesan);
                            cal.add(Calendar.DAY_OF_YEAR, 1);
                            lblTglCetak.setText(sdf.format(cal.getTime()));
                        }
                        if (levelStatus >= 5 && tglPesan != null) {
                            lblIconPacking.setForeground(WARNA_AKTIF);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(tglPesan);
                            cal.add(Calendar.DAY_OF_YEAR, 1);
                            cal.add(Calendar.HOUR_OF_DAY, 2);
                            lblTglPacking.setText(sdf.format(cal.getTime()));
                        }
                        if (levelStatus == 6 && tglPesan != null) {
                            lblIconSelesai.setForeground(WARNA_AKTIF);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(tglPesan);
                            cal.add(Calendar.DAY_OF_YEAR, 2);
                            lblTglSelesai.setText(sdf.format(cal.getTime()));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "ID Transaksi tidak ditemukan.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Eror Sistem: " + e.getMessage());
        }
    }

    private void resetVisualTracking() {
        lblIconBerhasil.setForeground(WARNA_MATI);
        lblIconBayar.setForeground(WARNA_MATI);
        lblIconProses.setForeground(WARNA_MATI);
        lblIconCetak.setForeground(WARNA_MATI);
        lblIconPacking.setForeground(WARNA_MATI);
        lblIconSelesai.setForeground(WARNA_MATI);
        
        // Komponen yang belum tercapai akan tetap menampilkan strip standar
        lblTglBerhasil.setText("-");
        lblTglBayar.setText("-");
        lblTglProses.setText("-");
        lblTglCetak.setText("-");
        lblTglPacking.setText("-");
        lblTglSelesai.setText("-");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblStatusBesar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblTglBerhasil = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        l = new javax.swing.JLabel();
        lblTglBayar = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblTglProses = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblTglCetak = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblTglPacking = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblTglSelesai = new javax.swing.JLabel();
        lblIconBerhasil = new javax.swing.JLabel();
        lblIconBayar = new javax.swing.JLabel();
        lblIconProses = new javax.swing.JLabel();
        lblIconCetak = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblIconPacking = new javax.swing.JLabel();
        lblIconSelesai = new javax.swing.JLabel();
        btnLihatDetail = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(255, 102, 0));
        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 102, 0));
        jLabel1.setText("Status Pesanan");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblStatusBesar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        lblStatusBesar.setText("Pesanan Berhasil !");

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jLabel3.setText("No Order :");

        lblTglBerhasil.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        lblTglBerhasil.setText("Tanggal :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTglBerhasil)
                    .addComponent(jLabel3)
                    .addComponent(lblStatusBesar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(330, 330, 330))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblStatusBesar, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTglBerhasil)
                .addGap(6, 6, 6))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        l.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        l.setText("Pembayaran Berhasil !");

        lblTglBayar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        lblTglBayar.setText("Tanggal / waktu");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTglBayar)
                    .addComponent(l))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(l)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTglBayar)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jLabel7.setText("Diproses");

        lblTglProses.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        lblTglProses.setText("Tanggal / waktu");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTglProses)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTglProses)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jLabel9.setText("Sedang dicetak");

        lblTglCetak.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        lblTglCetak.setText("Tanggal / waktu");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTglCetak)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTglCetak)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jLabel11.setText("Packing");

        jLabel12.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jLabel12.setText("-");

        lblTglPacking.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        lblTglPacking.setText("Tanggal / waktu");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTglPacking))
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(lblTglPacking))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jLabel13.setText("Selesai");

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jLabel14.setText("-");

        lblTglSelesai.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        lblTglSelesai.setText("Tanggal / waktu");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTglSelesai))
                    .addComponent(jLabel13))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(lblTglSelesai))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        lblIconBerhasil.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblIconBerhasil.setText("●");

        lblIconBayar.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblIconBayar.setText("●");

        lblIconProses.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblIconProses.setText("●");

        lblIconCetak.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblIconCetak.setText("●");

        jLabel19.setText(">");

        lblIconPacking.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblIconPacking.setText("●");

        lblIconSelesai.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblIconSelesai.setText("●");

        btnLihatDetail.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 14)); // NOI18N
        btnLihatDetail.setText("Lihat Detail Pesanan");
        btnLihatDetail.addActionListener(this::btnLihatDetailActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(221, 221, 221))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIconBerhasil, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblIconBayar, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIconCetak, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblIconProses, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblIconPacking, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblIconSelesai, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(52, Short.MAX_VALUE))
            .addComponent(btnLihatDetail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIconBerhasil))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIconBayar))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIconProses))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIconCetak))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIconPacking))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIconSelesai)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLihatDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLihatDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLihatDetailActionPerformed
        // TODO add your handling code here:
        detail_pesanan dp = new detail_pesanan();
        dp.setVisible(true);
        this.dispose(); // menutup halaman status pesanan
    }//GEN-LAST:event_btnLihatDetailActionPerformed

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
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new status_pesanan(1).setVisible(true);
});
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLihatDetail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel l;
    private javax.swing.JLabel lblIconBayar;
    private javax.swing.JLabel lblIconBerhasil;
    private javax.swing.JLabel lblIconCetak;
    private javax.swing.JLabel lblIconPacking;
    private javax.swing.JLabel lblIconProses;
    private javax.swing.JLabel lblIconSelesai;
    private javax.swing.JLabel lblStatusBesar;
    private javax.swing.JLabel lblTglBayar;
    private javax.swing.JLabel lblTglBerhasil;
    private javax.swing.JLabel lblTglCetak;
    private javax.swing.JLabel lblTglPacking;
    private javax.swing.JLabel lblTglProses;
    private javax.swing.JLabel lblTglSelesai;
    // End of variables declaration//GEN-END:variables
}
