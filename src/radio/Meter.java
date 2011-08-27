/*
 * Meter.java
 *
 * Created on September 10, 2007, 6:32 PM
 *
 * Copyright (C) 2006, 2007 by John Melton, G0ORX/N6LYT
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * The author can be reached by email at
 *
 * john.melton@sun.com
 *
 */

package radio;

import java.text.DecimalFormat;

/**
 *
 *  Meter
 */
public class Meter extends javax.swing.JPanel implements MeterInterface, PowerInterface {
    
    /** Creates new form Meter */
    public Meter() {
        initComponents();
    }
    
    public void addListener(MeterListener meterListener) {
        this.meterListener=meterListener;
    }
    
    public void selectRxMeter(RxMeters rxMeter) {
        initializing=true;
        rxMeterComboBox.setSelectedIndex(rxMeter.ordinal());
        currentRxMeter=rxMeter;
        initializing=false;
        switch(currentRxMeter) {
            case SIGNAL_STRENGTH:
            case SIGNAL_AVERAGE:
                meter.setText("  1   3   5   7   9  +20 +40 +60");
                break;
            case ADC_L:
            case ADC_R:
                meter.setText("-100  -80   -60   -40   -20    0");
                break;
            case OFF:
                meter.setText("");
                break;
        }
    }
    
    public void selectTxMeter(TxMeters txMeter) {
        initializing=true;
        txMeterComboBox.setSelectedIndex(txMeter.ordinal());
        currentTxMeter=txMeter;
        initializing=false;
        switch(currentTxMeter) {
            case MIC:
            case EQ:
            case LEVELER:
            case COMP:
            case CPDR:
            case ALC:
                    meter.setText("-20    -10     -5      0   1   2   3");
                    break;
            case FORWARD_POWER:
            case REVERSE_POWER:
                    if(State.getInstance().isPa()) {
                        meter.setText("1      5     10    50   100  120+");
                    } else {
                        meter.setText("0      0.1     0.2     0.5        1.0");
                    }
                    break;
            case SWR:
                    meter.setText("1      1.5   2     3     5    10");
                    meter.setText("0             10              20");
                    break;
            case OFF:
                    meter.setText("");
                    break;
            case LVL_G:
            case ALC_G:
                    meter.setText("0       5       10      15      20");
                    break;
        }

    }
    
    public void updateMeter(int id,float[] samples) {
        double sample=0.0;
        int dbm=0;
        int db=0;
        if(id==RX_METER) {
            switch(currentRxMeter) {
                case SIGNAL_STRENGTH:
                    sample=(double)samples[0]+meterCalibrationOffset+preampOffset+filterSizeOffset;
                    break;
                case SIGNAL_AVERAGE:
                    sample=(double)samples[1]+meterCalibrationOffset+preampOffset+filterSizeOffset;
                    break;
                case ADC_L:
                    sample=(double)samples[2]+meterCalibrationOffset+preampOffset+filterSizeOffset;
                    break;
                case ADC_R:
                    sample=(double)samples[3]+meterCalibrationOffset+preampOffset+filterSizeOffset;
                    break;
                case OFF:
                    break;
            }
            if(currentRxMeter==RxMeters.OFF) {
                meterText.setText("");
            } else {
                dbm=(int)sample;
                meterText.setText(Integer.toString(dbm)+ " dBm"); 
            }
            meter.setSample(dbm);
        } else {
            /*
            switch(currentTxMeter) {
                case FORWARD_POWER:
                    sample=(double)samples[0];
                    break;
                case REVERSE_POWER:
                    sample=(double)samples[1];
                    break;
                case MIC:
                    sample=(double)samples[0];
                    break;
                case EQ:
                    sample=(double)samples[2];
                    break;
                case LVL_G:
                    sample=(double)samples[6];
                    break;
                case COMP:
                    sample=(double)samples[4];
                    break;
                case CPDR:
                    sample=(double)samples[5];
                    break;
                case ALC:
                    sample=(double)samples[7];
                    break;
                case ALC_G:
                    sample=(double)samples[8];
                    break;
                case SWR:
                    sample=(double)samples[0];
                    break;
            }
            if(currentTxMeter==TxMeters.OFF) {
                meterText.setText("");
            } else {
                db=(int)sample;
                meterText.setText(Integer.toString(db)+ " db"); 
            }
            meter.setSample(db);
            */
        }
    }
    
    
    public void updatePower(int fwdPower,int revPower) {
        double forwardPower=PAPower(fwdPower);
        double reversePower=PAPower(revPower);
        double swr=SWR(fwdPower,revPower);
        
        switch(currentTxMeter) {
            case FORWARD_POWER:
                meterText.setText(Integer.toString((int)forwardPower/1000)+" W");
                break;
            case REVERSE_POWER:
                meterText.setText(Integer.toString((int)reversePower/1000)+" W");
                break;
            case SWR:
                meterText.setText(swrFormat.format(swr)+":1 SWR");
                break;
        }
        
        //System.err.println("FWD="+forwardPower+" REV="+reversePower);
    }
    
     private double PAPower(int adc)
        {
            double v_out = scaledVoltage(adc);
            double pow = Math.pow(v_out, 2)/50;
            pow = Math.max(pow, 0.0);
            return pow;
        }

    private double scaledVoltage(double adc) {
        double v_det = adc * 0.062963;
        double v_out = v_det * 10.39853;
        //return v_out*PABandOffset(CurrentBand);
        return v_out*40.0;
    }
    
    private double SWR(int adc_fwd, int adc_rev)
    {
            if(adc_fwd == 0 && adc_rev == 0)
                    return 1.0;
            else if(adc_rev > adc_fwd)
                    return 50.0;
            
            double Ef = scaledVoltage(adc_fwd);
            double Er = scaledVoltage(adc_rev);

            double swr = (Ef + Er)/(Ef - Er);

            return swr;
    }

    public void setMeterCalibrationOffset(double meterCalibrationOffset) {
        this.meterCalibrationOffset=meterCalibrationOffset;
    }
    
    public void setPreampOffset(float preampOffset) {
        this.preampOffset=preampOffset;
    }
    
    public void setFilterSizeOffset(int filterSizeOffset) {
        this.filterSizeOffset=filterSizeOffset;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        meterText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rxMeterComboBox = new javax.swing.JComboBox();
        txMeterComboBox = new javax.swing.JComboBox();
        meter = new radio.MeterPanel();
        jLabel3 = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(153, 153, 153));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMaximumSize(new java.awt.Dimension(280, 98));
        setMinimumSize(new java.awt.Dimension(280, 98));
        setPreferredSize(new java.awt.Dimension(280, 98));
        meterText.setBackground(new java.awt.Color(102, 102, 102));
        meterText.setFont(new java.awt.Font("Dialog", 0, 18));
        meterText.setForeground(new java.awt.Color(255, 255, 0));
        meterText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        meterText.setText("dBm");
        meterText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(meterText, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 30));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel1.setText("Rx");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel2.setText("Tx");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        rxMeterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Signal", "Sig Avg", "ADC L", "ADC R", "OFF" }));
        rxMeterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rxMeterComboBoxActionPerformed(evt);
            }
        });

        add(rxMeterComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 100, -1));

        txMeterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Forward Power", "Reverse Power", "Mic", "EQ", "Lev", "Lev Gain", "Comp", "CPDR", "ALC", "ALC Gain", "SWR", "Off" }));
        txMeterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txMeterComboBoxActionPerformed(evt);
            }
        });

        add(txMeterComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 100, -1));

        meter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add(meter, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 150, 30));

        jLabel3.setText("Meter");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

    }// </editor-fold>//GEN-END:initComponents

    private void txMeterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txMeterComboBoxActionPerformed
        if(!initializing) {
            TxMeters txMeter=TxMeters.OFF;
            switch(txMeterComboBox.getSelectedIndex()) {
                case 0:
                    txMeter=TxMeters.FORWARD_POWER;
                    break;
                case 1:
                    txMeter=TxMeters.REVERSE_POWER;
                    break;
                case 2:
                    txMeter=TxMeters.MIC;
                    break;
                case 3:
                    txMeter=TxMeters.EQ;
                    break;
                case 4:
                    txMeter=TxMeters.LEVELER;
                    break;
                case 5:
                    txMeter=TxMeters.LVL_G;
                    break;
                case 6:
                    txMeter=TxMeters.COMP;
                    break;
                case 7:
                    txMeter=TxMeters.CPDR;
                    break;
                case 8:
                    txMeter=TxMeters.ALC;
                    break;
                case 9:
                    txMeter=TxMeters.ALC_G;
                    break;
                case 10:
                    txMeter=TxMeters.SWR;
                    break;
                case 11:
                    txMeter=TxMeters.OFF;
                    break;
            }
            meterListener.txMeterSelected(txMeter);
        }
    }//GEN-LAST:event_txMeterComboBoxActionPerformed

    private void rxMeterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rxMeterComboBoxActionPerformed
        if(!initializing) {
            RxMeters rxMeter=RxMeters.OFF;
            switch(rxMeterComboBox.getSelectedIndex()) {
                case 0:
                    rxMeter=RxMeters.SIGNAL_STRENGTH;
                    break;
                case 1:
                    rxMeter=RxMeters.SIGNAL_AVERAGE;
                    break;
                case 2:
                    rxMeter=RxMeters.ADC_L;
                    break;
                case 3:
                    rxMeter=RxMeters.ADC_R;
                    break;
                case 4:
                    rxMeter=RxMeters.OFF;
                    break;
            }
            meterListener.rxMeterSelected(rxMeter);
        }
    }//GEN-LAST:event_rxMeterComboBoxActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private radio.MeterPanel meter;
    private javax.swing.JTextField meterText;
    private javax.swing.JComboBox rxMeterComboBox;
    private javax.swing.JComboBox txMeterComboBox;
    // End of variables declaration//GEN-END:variables
    
    private DecimalFormat swrFormat=new DecimalFormat("##0.00");
    
    private boolean initializing=false;
    private MeterListener meterListener;
    private RxMeters currentRxMeter=RxMeters.OFF;
    private TxMeters currentTxMeter=TxMeters.OFF;
    private double meterCalibrationOffset;
    private float preampOffset;
    private int filterSizeOffset=6;
    
    public static final int RX_METER=0;
    public static final int TX_METER=1;
}
