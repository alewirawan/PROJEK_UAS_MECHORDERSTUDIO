/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package merchorderstudio;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import config.koneksi;
import utility.sessions;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.awt.Component;
import java.util.ArrayList;
import checkout.Checkoutsession;
import javax.swing.JSeparator;
import javax.swing.ImageIcon;
import java.awt.Image;





/**
 *
 * @author Gunawan
 */

public class keranjang extends javax.swing.JFrame {
    

            
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(keranjang.class.getName());

    /**
     * Creates new form keranjang
     */
    public keranjang() {
        initComponents();
        
        DataKeranjang();
        LoadTotal();
       
        setResizable(false);
        
    }
    
    private void UpdateCheckAll(){

    boolean semua=true;

    for(Component c : panelkeranjang.getComponents()){

        JPanel panel=(JPanel)c;

        for(Component item : panel.getComponents()){

            if(item instanceof JCheckBox){

                JCheckBox chk=(JCheckBox)item;

                if(!chk.isSelected()){

                    semua=false;
                    break;

                }

            }

        }

    }

    chkSemua.setSelected(semua);

}
    
    private void PilihSemua(boolean pilih){

    for(Component c : panelkeranjang.getComponents()){

        JPanel panel=(JPanel)c;

        for(Component item : panel.getComponents()){

            if(item instanceof JCheckBox){

                JCheckBox chk=(JCheckBox)item;

                chk.setSelected(pilih);

            }

        }

    }

    LoadTotal();

}
    
    private void HapusKeranjang(){

    try{

        Connection conn=koneksi.getConnection();

        for(Component c : panelkeranjang.getComponents()){

            JPanel panel=(JPanel)c;

            JCheckBox chk=null;

            for(Component item : panel.getComponents()){

                if(item instanceof JCheckBox){

                    chk=(JCheckBox)item;
                    break;

                }

            }

            if(chk!=null && chk.isSelected()){

                int idKeranjang=
                (int)chk.getClientProperty("idKeranjang");

                String sql=
                "DELETE FROM keranjang WHERE id_keranjang=?";

                PreparedStatement pst=
                conn.prepareStatement(sql);

                pst.setInt(1,idKeranjang);

                pst.executeUpdate();

            }

        }

        DataKeranjang();

    }catch(Exception e){

        JOptionPane.showMessageDialog(
            this,
            e.getMessage()
        );

    }

}
    
    private void TambahJumlah(int idKeranjang,int idProduk){

    try{

        Connection conn=koneksi.getConnection();

        String sqlStok=
        "SELECT stok FROM produk WHERE id_produk=?";

        PreparedStatement pstStok=
        conn.prepareStatement(sqlStok);

        pstStok.setInt(1,idProduk);

        ResultSet rsStok=pstStok.executeQuery();

        int stok=0;

        if(rsStok.next()){
            stok=rsStok.getInt("stok");
        }

        String sqlJumlah=
        "SELECT jumlah FROM keranjang WHERE id_keranjang=?";

        PreparedStatement pstJumlah=
        conn.prepareStatement(sqlJumlah);

        pstJumlah.setInt(1,idKeranjang);

        ResultSet rsJumlah=
        pstJumlah.executeQuery();

        int jumlah=0;

        if(rsJumlah.next()){
            jumlah=rsJumlah.getInt("jumlah");
        }

        if(jumlah>=stok){

            JOptionPane.showMessageDialog(
                this,
                "Stok produk tidak mencukupi!"
            );

            return;

        }

        String sqlUpdate=
        "UPDATE keranjang SET jumlah=jumlah+1 WHERE id_keranjang=?";

        PreparedStatement pstUpdate=
        conn.prepareStatement(sqlUpdate);

        pstUpdate.setInt(1,idKeranjang);

        pstUpdate.executeUpdate();

        DataKeranjang();

    }catch(Exception e){

        JOptionPane.showMessageDialog(
            this,
            e.getMessage()
        );

    }

}
    
    private void KurangJumlah(int idKeranjang){

    try{

        Connection conn=koneksi.getConnection();

        String sqlJumlah=
        "SELECT jumlah FROM keranjang WHERE id_keranjang=?";

        PreparedStatement pstJumlah=
        conn.prepareStatement(sqlJumlah);

        pstJumlah.setInt(1,idKeranjang);

        ResultSet rsJumlah=
        pstJumlah.executeQuery();

        int jumlah=0;

        if(rsJumlah.next()){
            jumlah=rsJumlah.getInt("jumlah");
        }

        if(jumlah<=1){

            JOptionPane.showMessageDialog(
                this,
                "Minimal pembelian adalah 1 produk."
            );

            return;

        }

        String sqlUpdate=
        "UPDATE keranjang SET jumlah=jumlah-1 WHERE id_keranjang=?";

        PreparedStatement pstUpdate=
        conn.prepareStatement(sqlUpdate);

        pstUpdate.setInt(1,idKeranjang);

        pstUpdate.executeUpdate();

        DataKeranjang();

    }catch(Exception e){

        JOptionPane.showMessageDialog(
            this,
            e.getMessage()
        );

    }

}
    
    private void LoadTotal(){

    double total=0;

    try{

        for(java.awt.Component comp : panelkeranjang.getComponents()){

            JPanel panel=(JPanel)comp;

            JCheckBox chk=null;

            for(java.awt.Component c : panel.getComponents()){

                if(c instanceof JCheckBox){

                    chk=(JCheckBox)c;
                    break;

                }

            }

            if(chk!=null && chk.isSelected()){

                JLabel lblSubtotal=null;

                for(java.awt.Component c : panel.getComponents()){

                    if(c instanceof JLabel){

                        JLabel lbl=(JLabel)c;

                        if(lbl.getText().toLowerCase().contains("subtotal")){

                            lblSubtotal=lbl;
                            break;

                        }

                    }

                }

                if(lblSubtotal!=null){

                    String angka=lblSubtotal.getText()
                    .replace("Subtotal : ","")
                    .replace("Rp","")
                    .replace(".","")
                    .replace(",","")
                    .trim();

                    total+=Double.parseDouble(angka);

                }

            }

        }

        lblTotal.setText("Rp "+FormatRupiah.format(total));

    }catch(Exception e){

        e.printStackTrace();

    }

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
    
    public void DataKeranjang() {

    panelkeranjang.removeAll();

    try {

        String sql =
        "SELECT k.id_keranjang,p.id_produk,p.nama_produk," +
        "k.ukuran,k.jumlah,p.harga,p.foto_produk " +
        "FROM keranjang k " +
        "INNER JOIN produk p ON k.id_produk=p.id_produk " +
        "WHERE k.id_user=? " +
        "ORDER BY k.tanggal_tambah DESC";

        Connection conn = koneksi.getConnection();

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, sessions.idUser);   // Menggunakan ID user yang sedang login

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            panelkeranjang.add(CreatePanelItem(rs));
        }

        panelkeranjang.revalidate();
        panelkeranjang.repaint();

        UpdateCheckAll();
        LoadTotal();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}
    
    private JPanel CreatePanelItem(ResultSet rs){

    try{

        int idKeranjang=rs.getInt("id_keranjang");
        int idProduk=rs.getInt("id_produk");

        double harga=rs.getDouble("harga");
        int jumlah=rs.getInt("jumlah");

        double subtotal=harga*jumlah;

        JPanel panelItem=new JPanel();
        panelItem.setBackground(Color.WHITE);
        panelItem.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        panelItem.setPreferredSize(new Dimension(480,160));
        panelItem.setMinimumSize(new Dimension(480,160));
        panelItem.setMaximumSize(new Dimension(480,160));

        panelItem.setLayout(null);
        panelItem.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox chkPilih=new JCheckBox();
        chkPilih.setBounds(10,40,40,40);

        chkPilih.putClientProperty("subtotal",subtotal);
        chkPilih.putClientProperty("idKeranjang",idKeranjang);
        chkPilih.putClientProperty("idProduk",idProduk);

        chkPilih.addActionListener(e->{
            UpdateCheckAll();
            LoadTotal();
        });

        panelItem.add(chkPilih);

        JLabel lblFoto=new JLabel();
        lblFoto.setBounds(40,20,70,70);

        byte[] img=rs.getBytes("foto_produk");

        if(img!=null){

            ImageIcon icon=new ImageIcon(img);

            Image image=icon.getImage().getScaledInstance(
                    70,
                    70,
                    Image.SCALE_SMOOTH
            );

            lblFoto.setIcon(new ImageIcon(image));

        }

        panelItem.add(lblFoto);

        JLabel lblNama=new JLabel(rs.getString("nama_produk"));
        lblNama.setFont(new Font("Segoe UI",Font.BOLD,15));
        lblNama.setBounds(130,15,250,20);
        panelItem.add(lblNama);

        String ukuran=rs.getString("ukuran");

        if(ukuran==null){
            ukuran="-";
        }

        JLabel lblUkuran=new JLabel("Ukuran : "+ukuran);
        lblUkuran.setBounds(130,40,180,18);
        panelItem.add(lblUkuran);

        JLabel lblHarga=new JLabel(
                "Harga : Rp"+FormatRupiah.format(harga)
        );
        lblHarga.setBounds(130,62,180,18);
        panelItem.add(lblHarga);

        JLabel lblSubtotal=new JLabel(
                "Subtotal : Rp"+FormatRupiah.format(subtotal)
        );
        lblSubtotal.setFont(new Font("Segoe UI",Font.BOLD,14));
        lblSubtotal.setForeground(new Color(255, 140, 0));
        lblSubtotal.setBounds(130,84,220,18);
        panelItem.add(lblSubtotal);

        JPanel panelJumlah=new JPanel();
        panelJumlah.setBounds(130,115,120,28);
        panelJumlah.setLayout(null);
        panelJumlah.setBackground(Color.WHITE);
        panelJumlah.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));

        JButton btnKurang=new JButton("-");
        btnKurang.addActionListener(e->{
            KurangJumlah(idKeranjang);
        });
        btnKurang.setBounds(0,0,35,28);
        btnKurang.setBorder(BorderFactory.createEmptyBorder());
        btnKurang.setContentAreaFilled(false);
        btnKurang.setFocusPainted(false);
        panelJumlah.add(btnKurang);

        JSeparator kiri=new JSeparator(SwingConstants.VERTICAL);
        kiri.setBounds(35,0,1,28);
        panelJumlah.add(kiri);

        JLabel lblJumlah=new JLabel(String.valueOf(jumlah));
        lblJumlah.setHorizontalAlignment(SwingConstants.CENTER);
        lblJumlah.setBounds(36,0,48,28);
        panelJumlah.add(lblJumlah);

        JSeparator kanan=new JSeparator(SwingConstants.VERTICAL);
        kanan.setBounds(84,0,1,28);
        panelJumlah.add(kanan);

        JButton btnTambah=new JButton("+");
        btnTambah.addActionListener(e->{
            TambahJumlah(idKeranjang,idProduk);
        });
        btnTambah.setBounds(85,0,35,28);
        btnTambah.setBorder(BorderFactory.createEmptyBorder());
        btnTambah.setContentAreaFilled(false);
        btnTambah.setFocusPainted(false);
        panelJumlah.add(btnTambah);

        panelItem.add(panelJumlah);

        return panelItem;

    }catch(Exception e){

        e.printStackTrace();
        return new JPanel();

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

        header = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelkeranjang = new javax.swing.JPanel();
        footer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        chkSemua = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        btnCheckout = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnHapus = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        header.setBackground(new java.awt.Color(255, 255, 255));
        header.setPreferredSize(new java.awt.Dimension(0, 50));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("HOME");
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelkeranjang.setBackground(new java.awt.Color(255, 255, 255));
        panelkeranjang.setPreferredSize(null);
        panelkeranjang.setLayout(new javax.swing.BoxLayout(panelkeranjang, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(panelkeranjang);

        footer.setBackground(new java.awt.Color(255, 255, 255));
        footer.setPreferredSize(new java.awt.Dimension(0, 50));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel1.setText("all");

        chkSemua.addActionListener(this::chkSemuaActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkSemua, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkSemua)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnCheckout.setText("Checkout");
        btnCheckout.setBorderPainted(false);
        btnCheckout.setContentAreaFilled(false);
        btnCheckout.addActionListener(this::btnCheckoutActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCheckout)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnHapus.setText("Hapus");
        btnHapus.setBorderPainted(false);
        btnHapus.setContentAreaFilled(false);
        btnHapus.addActionListener(this::btnHapusActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHapus)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(255, 102, 0));
        lblTotal.setText("Total");

        javax.swing.GroupLayout footerLayout = new javax.swing.GroupLayout(footer);
        footer.setLayout(footerLayout);
        footerLayout.setHorizontalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, footerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        footerLayout.setVerticalGroup(
            footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, footerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addGroup(footerLayout.createSequentialGroup()
                .addGroup(footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(footerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, footerLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(footerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
            .addComponent(footer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(footer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        btnHapus.addActionListener(e->{
            HapusKeranjang();
        });
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        // TODO add your handling code here:
                Checkoutsession.listKeranjang.clear();

           for(Component c : panelkeranjang.getComponents()){

               JPanel panel=(JPanel)c;

               for(Component item : panel.getComponents()){

                   if(item instanceof JCheckBox){

                       JCheckBox chk=(JCheckBox)item;

                       if(chk.isSelected()){

                           Checkoutsession.listKeranjang.add(
                               (Integer)chk.getClientProperty("idKeranjang")
                           );

                       }

                   }

               }

           }

           if(Checkoutsession.listKeranjang.isEmpty()){

               JOptionPane.showMessageDialog(
                   this,
                   "Pilih minimal satu produk."
               );

               return;

           }

           new checkout().setVisible(true);
           dispose();

    }//GEN-LAST:event_btnCheckoutActionPerformed

    private void chkSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSemuaActionPerformed
        // TODO add your handling code here:
        PilihSemua(chkSemua.isSelected());
    }//GEN-LAST:event_chkSemuaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dashboard_home d=new dashboard_home();
        d.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new keranjang().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnHapus;
    private javax.swing.JCheckBox chkSemua;
    private javax.swing.JPanel footer;
    private javax.swing.JPanel header;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel panelkeranjang;
    // End of variables declaration//GEN-END:variables
}

class CheckoutSession {
    public static ArrayList<Integer> listKeranjang = new ArrayList<>();
}
