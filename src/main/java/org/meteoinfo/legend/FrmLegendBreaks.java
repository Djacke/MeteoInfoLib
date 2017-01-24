/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.legend;

import org.meteoinfo.layer.FrmLayerProperty;
import org.meteoinfo.shape.ShapeTypes;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.meteoinfo.global.colors.ColorMap;
import org.meteoinfo.global.colors.ColorUtil;
import org.meteoinfo.global.util.BigDecimalUtil;
import org.meteoinfo.ui.ColorComboBoxModel;
import org.meteoinfo.ui.ColorListCellRender;

/**
 *
 * @author yaqiang
 */
public class FrmLegendBreaks extends javax.swing.JDialog {

    private final java.awt.Dialog _parent;
    private LegendScheme _legendScheme = null;
    private double _minContourValue, _maxContourValue, _interval;
    private boolean _isUniqueValue = false;

    /**
     * Constructor
     *
     * @param parent Parent dialog
     * @param modal Modal
     * @param isUniqueValue If is unique value legend scheme
     */
    public FrmLegendBreaks(java.awt.Dialog parent, boolean modal, boolean isUniqueValue) {
        super(parent, modal);
        initComponents();

        _parent = parent;
        _isUniqueValue = isUniqueValue;
    }

    private void initialize() {
        if (!this._isUniqueValue) {
            this.jLabel_Min.setEnabled(true);
            this.jLabel_Max.setEnabled(true);
            this.jLabel_From.setEnabled(true);
            this.jTextField_StartValue.setEnabled(true);
            this.jLabel_To.setEnabled(true);
            this.jTextField_EndValue.setEnabled(true);
            this.jLabel_Interval.setEnabled(true);
            this.jTextField_Interval.setEnabled(true);
            this.jButton_NewLegend.setEnabled(true);
            
            this.jLabel_Min.setText("Min: " + String.format("%1$E", _legendScheme.getMinValue()));
            this.jLabel_Max.setText("Max: " + String.format("%1$E", _legendScheme.getMaxValue()));
            int bnum = _legendScheme.getBreakNum();
            if (bnum > 2) {
                ColorBreak aCB = _legendScheme.getLegendBreaks().get(0);
                _minContourValue = Double.parseDouble(aCB.getEndValue().toString());
                if (_legendScheme.getHasNoData()) {
                    aCB = _legendScheme.getLegendBreaks().get(bnum - 2);
                    _maxContourValue = Double.parseDouble(aCB.getStartValue().toString());
                    _interval = BigDecimalUtil.div((BigDecimalUtil.sub(_maxContourValue, _minContourValue)), (bnum - 3));
                } else {
                    aCB = _legendScheme.getLegendBreaks().get(bnum - 1);
                    _maxContourValue = Double.parseDouble(aCB.getStartValue().toString());
                    switch (_legendScheme.getShapeType()) {
                        case Polyline:
                        case PolylineZ:
                            _interval = BigDecimalUtil.div((BigDecimalUtil.sub(_maxContourValue, _minContourValue)), (bnum - 1));
                            break;
                        default:
                            _interval = BigDecimalUtil.div((BigDecimalUtil.sub(_maxContourValue, _minContourValue)), (bnum - 2));
                            break;
                    }
                }

                this.jTextField_StartValue.setText(String.valueOf(_minContourValue));
                this.jTextField_EndValue.setText(String.valueOf(_maxContourValue));
                this.jTextField_Interval.setText(String.valueOf(_interval));
            }
        } else {
            this.jLabel_Min.setEnabled(false);
            this.jLabel_Max.setEnabled(false);
            this.jLabel_From.setEnabled(false);
            this.jTextField_StartValue.setEnabled(false);
            this.jLabel_To.setEnabled(false);
            this.jTextField_EndValue.setEnabled(false);
            this.jLabel_Interval.setEnabled(false);
            this.jTextField_Interval.setEnabled(false);
            this.jButton_NewLegend.setEnabled(false);
        }

        ColorMap[] colorTables;
        try {
            colorTables = ColorUtil.getColorTables();
            ColorListCellRender render = new ColorListCellRender();
            render.setPreferredSize(new Dimension(62, 21));
            this.jComboBox_ColorTable.setModel(new ColorComboBoxModel(colorTables));
            this.jComboBox_ColorTable.setRenderer(render);
            ColorMap ct = ColorUtil.findColorTable(colorTables, "grads_rainbow");
            if (ct != null)
                this.jComboBox_ColorTable.setSelectedItem(ct);
            else
                this.jComboBox_ColorTable.setSelectedIndex(0);
        } catch (IOException ex) {
            Logger.getLogger(FrmLegendBreaks.class.getName()).log(Level.SEVERE, null, ex);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel_Min = new javax.swing.JLabel();
        jLabel_Max = new javax.swing.JLabel();
        jLabel_From = new javax.swing.JLabel();
        jTextField_StartValue = new javax.swing.JTextField();
        jLabel_To = new javax.swing.JLabel();
        jTextField_EndValue = new javax.swing.JTextField();
        jLabel_Interval = new javax.swing.JLabel();
        jTextField_Interval = new javax.swing.JTextField();
        jButton_NewLegend = new javax.swing.JButton();
        jButton_NewColors = new javax.swing.JButton();
        jComboBox_ColorTable = new javax.swing.JComboBox();
        jLabel_ColorTable = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel_Min.setText("Min:");

        jLabel_Max.setText("Max:");

        jLabel_From.setText("from:");

        jTextField_StartValue.setPreferredSize(new java.awt.Dimension(89, 24));

        jLabel_To.setText("to:");

        jTextField_EndValue.setPreferredSize(new java.awt.Dimension(89, 24));

        jLabel_Interval.setText("Interval:");

        jTextField_Interval.setPreferredSize(new java.awt.Dimension(89, 24));

        jButton_NewLegend.setText("New Legend");
        jButton_NewLegend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NewLegendActionPerformed(evt);
            }
        });

        jButton_NewColors.setText("New Colors");
        jButton_NewColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NewColorsActionPerformed(evt);
            }
        });

        jComboBox_ColorTable.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel_ColorTable.setText("Color table:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel_ColorTable)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox_ColorTable, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel_Interval)
                                            .addComponent(jLabel_From))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField_StartValue, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField_Interval, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel_Min))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel_To)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField_EndValue, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel_Max))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton_NewLegend)
                        .addGap(56, 56, 56)
                        .addComponent(jButton_NewColors)
                        .addGap(45, 45, 45))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Max)
                    .addComponent(jLabel_Min))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_From)
                    .addComponent(jTextField_StartValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_To)
                    .addComponent(jTextField_EndValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Interval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Interval))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_ColorTable)
                    .addComponent(jComboBox_ColorTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_NewLegend)
                    .addComponent(jButton_NewColors))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.initialize();
    }//GEN-LAST:event_formWindowOpened

    private void jButton_NewLegendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NewLegendActionPerformed
        // TODO add your handling code here:
        _interval = Double.parseDouble(this.jTextField_Interval.getText());
        _minContourValue = Double.parseDouble(this.jTextField_StartValue.getText());
        _maxContourValue = Double.parseDouble(this.jTextField_EndValue.getText());

        if ((int) ((_maxContourValue - _minContourValue) / _interval) < 2) {
            JOptionPane.showMessageDialog(null, "Please reset the data!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double[] cValues;
        cValues = LegendManage.createContourValuesInterval(_minContourValue, _maxContourValue,
                _interval);

        Color[] colors = createColors(cValues.length + 1);

        LegendScheme aLS;
        if (_isUniqueValue) {
            aLS = LegendManage.createUniqValueLegendScheme(cValues, colors, _legendScheme.getShapeType(),
                    _legendScheme.getMinValue(), _legendScheme.getMaxValue(), _legendScheme.getHasNoData(), _legendScheme.getUndefValue());
        } else {
            aLS = LegendManage.createGraduatedLegendScheme(cValues, colors, _legendScheme.getShapeType(),
                    _legendScheme.getMinValue(), _legendScheme.getMaxValue(), _legendScheme.getHasNoData(),
                    _legendScheme.getUndefValue());
        }
        aLS.setFieldName(_legendScheme.getFieldName());
        //setLegendScheme(aLS);
        this._legendScheme = aLS;

        if (_parent.getClass() == FrmLegendSet.class) {
            ((FrmLegendSet) _parent).setLegendScheme(aLS);
        } else if (_parent.getClass() == FrmLayerProperty.class) {
            ((FrmLayerProperty) _parent).setLegendScheme(aLS);
        }
    }//GEN-LAST:event_jButton_NewLegendActionPerformed

    private void jButton_NewColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NewColorsActionPerformed
        // TODO add your handling code here:
        int colorNum = _legendScheme.getBreakNum();

        if (_legendScheme.getShapeType() == ShapeTypes.Polyline) {
            colorNum += 1;
        }

        Color[] colors = createColors(colorNum);

        int i;
        for (i = 0; i < _legendScheme.getBreakNum(); i++) {
            _legendScheme.getLegendBreaks().get(i).setColor(colors[i]);
        }

        if (_parent.getClass() == FrmLegendSet.class) {
            ((FrmLegendSet) _parent).setLegendScheme(_legendScheme);
        } else if (_parent.getClass() == FrmLayerProperty.class) {
            ((FrmLayerProperty) _parent).setLegendScheme(_legendScheme);
        }
    }//GEN-LAST:event_jButton_NewColorsActionPerformed

    /**
     * Set legend scheme
     *
     * @param aLS Legend scheme
     */
    public void setLegendScheme(LegendScheme aLS) {
        _legendScheme = (LegendScheme) aLS.clone();
        this.initialize();
    }

    private Color[] createColors(int colorNum) {        
        ColorComboBoxModel model = (ColorComboBoxModel)this.jComboBox_ColorTable.getModel();
        ColorMap ct = (ColorMap)model.getSelectedItem();
        return ct.getColors(colorNum);
    }    

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmLegendBreaks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLegendBreaks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLegendBreaks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLegendBreaks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLegendBreaks dialog = new FrmLegendBreaks(new javax.swing.JDialog(), true, false);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton_NewColors;
    private javax.swing.JButton jButton_NewLegend;
    private javax.swing.JComboBox jComboBox_ColorTable;
    private javax.swing.JLabel jLabel_ColorTable;
    private javax.swing.JLabel jLabel_From;
    private javax.swing.JLabel jLabel_Interval;
    private javax.swing.JLabel jLabel_Max;
    private javax.swing.JLabel jLabel_Min;
    private javax.swing.JLabel jLabel_To;
    private javax.swing.JTextField jTextField_EndValue;
    private javax.swing.JTextField jTextField_Interval;
    private javax.swing.JTextField jTextField_StartValue;
    // End of variables declaration//GEN-END:variables
}
