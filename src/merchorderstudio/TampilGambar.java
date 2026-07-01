/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package merchorderstudio;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import config.koneksi;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.CardLayout;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author Gunawan
 */
public class TampilGambar extends javax.swing.JFrame {
    
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TampilGambar.class.getName());

    /**
     * Creates new form TampilGambar
     */
    public TampilGambar() {
        initComponents();
        
    CardLayout card = new CardLayout();
    jPanel2.setLayout(card);

    jPanel2.add(Semua, "semua");
    jPanel2.add(Kaos, "kaos");
    jPanel2.add(Hoodie, "hoodie");
    jPanel2.add(Mug, "mug");
    jPanel2.add(Sticker, "sticker");
    jPanel2.add(Totebag, "totebag");
    
    card.show(jPanel2, "semua");
        
        Semua.setLayout(new FlowLayout(FlowLayout.LEFT, 10,10));
        Kaos.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        Hoodie.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        Mug.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        Sticker.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
        Totebag.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));

        
        loadProduk();
        showCard("semua");
        
    }
    
    
    public void showCard(String namaCard) {

        CardLayout card =
            (CardLayout) jPanel2.getLayout();

        card.show(jPanel2, namaCard);
    }
    
   private void loadProduk() {

    System.out.println("=== loadProduk() dipanggil ===");

    Semua.removeAll();
    Kaos.removeAll();
    Hoodie.removeAll();
    Mug.removeAll();
    Sticker.removeAll();
    Totebag.removeAll();

    String sql =
        "SELECT MIN(id_produk) AS id_produk, " +
        "nama_produk, kategori, " +
        "MIN(harga) AS harga, " +
        "foto_produk " +
        "FROM produk " +
        "GROUP BY nama_produk, kategori";

    try (
        Connection conn = koneksi.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {

        int jumlahProduk = 0;

        while (rs.next()) {

            jumlahProduk++;

            System.out.println("Produk : "
                    + rs.getString("nama_produk"));

            String kategori =
                    rs.getString("kategori");

            JPanel cardSemua =
                    buatCardProduk(rs);

            Semua.add(cardSemua);

            JPanel cardKategori =
                    buatCardProduk(rs);

            switch (kategori.toLowerCase()) {

                case "kaos":
                    Kaos.add(cardKategori);
                    break;

                case "hoodie":
                    Hoodie.add(cardKategori);
                    break;

                case "mug":
                    Mug.add(cardKategori);
                    break;

                case "sticker":
                    Sticker.add(cardKategori);
                    break;

                case "totebag":
                    Totebag.add(cardKategori);
                    break;
            }
        }

        System.out.println(
                "Total Produk : "
                + jumlahProduk);

        Semua.revalidate();
        Semua.repaint();
        
        jPanel2.revalidate();
        jPanel2.repaint();

        Kaos.revalidate();
        Kaos.repaint();

        Hoodie.revalidate();
        Hoodie.repaint();

        Mug.revalidate();
        Mug.repaint();

        Sticker.revalidate();
        Sticker.repaint();

        Totebag.revalidate();
        Totebag.repaint();

    } catch (Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                e.getMessage()
        );
    }
}
    
    private JPanel buatCardProduk(ResultSet rs) throws Exception {

    JPanel card = new JPanel();
    card.setPreferredSize(new Dimension(110, 150));
    card.setLayout(new BorderLayout());
    card.setBackground(Color.WHITE);

    JLabel lblFoto = new JLabel();
    JLabel lblNama = new JLabel();
    JLabel lblHarga = new JLabel();

    JButton btnPesan = new JButton("+");

    lblFoto.setHorizontalAlignment(JLabel.CENTER);
    lblHarga.setHorizontalAlignment(JLabel.CENTER);
    lblHarga.setFont(new Font("Segoe UI", Font.BOLD, 9));
    lblNama.setHorizontalAlignment(JLabel.CENTER);
    lblNama.setFont(new Font("Segoe UI", Font.BOLD, 11));

    btnPesan.setPreferredSize(new Dimension(35, 25));
    btnPesan.setFocusable(false);

    lblNama.setText(rs.getString("nama_produk"));
    lblHarga.setText("Rp " + rs.getString("harga"));

    // FOTO PRODUK
    byte[] foto = rs.getBytes("foto_produk");

    if (foto != null) {

        ImageIcon icon = new ImageIcon(foto);

        Image img = icon.getImage().getScaledInstance(
                70,
                70,
                Image.SCALE_SMOOTH);

        lblFoto.setIcon(new ImageIcon(img));
    }

    // ID PRODUK
    final int idProduk = rs.getInt("id_produk");


    card.add(lblFoto, BorderLayout.CENTER);
    
        lblFoto.setBorder(
        BorderFactory.createLineBorder(Color.BLACK, 1)
    );

   JPanel info = new JPanel(new GridLayout(2,1,0,3));
    info.setBackground(Color.LIGHT_GRAY);

    info.add(lblNama);
    info.add(lblHarga);

    JPanel bawah = new JPanel(new BorderLayout());
    bawah.setBackground(Color.LIGHT_GRAY);

    bawah.add(info, BorderLayout.CENTER);
    bawah.add(btnPesan, BorderLayout.EAST);

    card.add(bawah, BorderLayout.SOUTH);

    card.setBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        )
    );

    return card;
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        Semua = new javax.swing.JPanel();
        Kaos = new javax.swing.JPanel();
        Hoodie = new javax.swing.JPanel();
        Mug = new javax.swing.JPanel();
        Sticker = new javax.swing.JPanel();
        Totebag = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Semua");
        jButton1.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Kaos");
        jButton2.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setText("Hoodie");
        jButton3.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setText("Mug");
        jButton4.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton5.setText("Sticker");
        jButton5.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton6.setText("Totebag");
        jButton6.setPreferredSize(new java.awt.Dimension(75, 30));
        jButton6.addActionListener(this::jButton6ActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 449, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 183, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setLayout(new java.awt.CardLayout());

        Semua.setBackground(new java.awt.Color(255, 255, 255));
        Semua.setForeground(new java.awt.Color(255, 153, 102));

        javax.swing.GroupLayout SemuaLayout = new javax.swing.GroupLayout(Semua);
        Semua.setLayout(SemuaLayout);
        SemuaLayout.setHorizontalGroup(
            SemuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        SemuaLayout.setVerticalGroup(
            SemuaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        jPanel2.add(Semua, "card7");

        Kaos.setBackground(new java.awt.Color(255, 153, 153));
        Kaos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout KaosLayout = new javax.swing.GroupLayout(Kaos);
        Kaos.setLayout(KaosLayout);
        KaosLayout.setHorizontalGroup(
            KaosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );
        KaosLayout.setVerticalGroup(
            KaosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 342, Short.MAX_VALUE)
        );

        jPanel2.add(Kaos, "card6");

        Hoodie.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout HoodieLayout = new javax.swing.GroupLayout(Hoodie);
        Hoodie.setLayout(HoodieLayout);
        HoodieLayout.setHorizontalGroup(
            HoodieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        HoodieLayout.setVerticalGroup(
            HoodieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        jPanel2.add(Hoodie, "card5");

        Mug.setBackground(new java.awt.Color(204, 255, 255));

        javax.swing.GroupLayout MugLayout = new javax.swing.GroupLayout(Mug);
        Mug.setLayout(MugLayout);
        MugLayout.setHorizontalGroup(
            MugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        MugLayout.setVerticalGroup(
            MugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        jPanel2.add(Mug, "card4");

        Sticker.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout StickerLayout = new javax.swing.GroupLayout(Sticker);
        Sticker.setLayout(StickerLayout);
        StickerLayout.setHorizontalGroup(
            StickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        StickerLayout.setVerticalGroup(
            StickerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        jPanel2.add(Sticker, "card3");

        Totebag.setBackground(new java.awt.Color(255, 204, 255));
        Totebag.setPreferredSize(new java.awt.Dimension(550, 325));

        javax.swing.GroupLayout TotebagLayout = new javax.swing.GroupLayout(Totebag);
        Totebag.setLayout(TotebagLayout);
        TotebagLayout.setHorizontalGroup(
            TotebagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );
        TotebagLayout.setVerticalGroup(
            TotebagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );

        jPanel2.add(Totebag, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        showCard("semua");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        showCard("kaos");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        showCard("hoodie");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        showCard("mug");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        showCard("sticker");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        showCard("totebag");
    }//GEN-LAST:event_jButton6ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new TampilGambar().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Hoodie;
    private javax.swing.JPanel Kaos;
    private javax.swing.JPanel Mug;
    private javax.swing.JPanel Semua;
    private javax.swing.JPanel Sticker;
    private javax.swing.JPanel Totebag;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
