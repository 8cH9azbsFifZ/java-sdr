/*
 * Band.java
 *
 * Created on September 9, 2007, 5:16 PM
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

import java.awt.Color;

/**
 *
 *  Band
 */
public class Band extends javax.swing.JPanel implements BandInterface {
    
    /** Creates new form Band */
    public Band() {
        initComponents();
    }
    
    public void setEnabled(boolean state) {
        band1.setEnabled(state);
        band2.setEnabled(state);
        band3.setEnabled(state);
        band4.setEnabled(state);
        band5.setEnabled(state);
        band6.setEnabled(state);
        band7.setEnabled(state);
        band8.setEnabled(state);
        band9.setEnabled(state);
        band10.setEnabled(state);
        band11.setEnabled(state);
        band12.setEnabled(state);
        band13.setEnabled(state);
        band14.setEnabled(state);
        band15.setEnabled(state);
        
    }
    
    public void addListener(BandListener bandListener) {
        this.bandListener=bandListener;
    }
    
    public void select(Bands band) {
        javax.swing.JButton button=getButton(band);
        if(button!=null) {
            button.setForeground(Color.YELLOW);
        }
    }

    public void unselect(Bands band) {
        javax.swing.JButton button=getButton(band);
        if(button!=null) {
            button.setForeground(Color.BLACK);
        }
    }
    
    public void setTitles(String[] titles) {
        if(titles.length>0) {
            band1.setText(titles[0]);
            band1.setEnabled(titles[0].length()>0);
        }
        if(titles.length>1) {
            band2.setText(titles[1]);
            band2.setEnabled(titles[1].length()>0);
        }
        if(titles.length>2) {
            band3.setText(titles[2]);
            band3.setEnabled(titles[2].length()>0);
        }
        if(titles.length>3) {
            band4.setText(titles[3]);
            band4.setEnabled(titles[3].length()>0);
        }
        if(titles.length>4) {
            band5.setText(titles[4]);
            band5.setEnabled(titles[4].length()>0);
        }
        if(titles.length>5) {
            band6.setText(titles[5]);
            band6.setEnabled(titles[5].length()>0);
        }
        if(titles.length>6) {
            band7.setText(titles[6]);
            band7.setEnabled(titles[6].length()>0);
        }
        if(titles.length>7) {
            band8.setText(titles[7]);
            band8.setEnabled(titles[7].length()>0);
        }
        if(titles.length>8) {
            band9.setText(titles[8]);
            band9.setEnabled(titles[8].length()>0);
        }
        if(titles.length>9) {
            band10.setText(titles[9]);
            band10.setEnabled(titles[9].length()>0);
        }
        if(titles.length>10) {
            band11.setText(titles[10]);
            band11.setEnabled(titles[10].length()>0);
        }
        if(titles.length>11) {
            band12.setText(titles[11]);
            band12.setEnabled(titles[11].length()>0);
        }
        if(titles.length>12) {
            band13.setText(titles[12]);
            band13.setEnabled(titles[12].length()>0);
        }
        if(titles.length>13) {
            band14.setText(titles[13]);
            band14.setEnabled(titles[13].length()>0);
        }
        if(titles.length>14) {
            band15.setText(titles[14]);
            band15.setEnabled(titles[14].length()>0);
        }
    }
    
    private javax.swing.JButton getButton(Bands band) {
        javax.swing.JButton button=null;
        switch(band) {
            case BAND_1:
                button=band1;
                break;
            case BAND_2:
                button=band2;
                break;
            case BAND_3:
                button=band3;
                break;
            case BAND_4:
                button=band4;
                break;
            case BAND_5:
                button=band5;
                break;
            case BAND_6:
                button=band6;
                break;
            case BAND_7:
                button=band7;
                break;
            case BAND_8:
                button=band8;
                break;
            case BAND_9:
                button=band9;
                break;
            case BAND_10:
                button=band10;
                break;
            case BAND_11:
                button=band11;
                break;
            case BAND_12:
                button=band12;
                break;
            case BAND_13:
                button=band13;
                break;
            case BAND_14:
                button=band14;
                break;
            case BAND_15:
                button=band15;
                break;
        }
        return button;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        band1 = new javax.swing.JButton();
        band2 = new javax.swing.JButton();
        band3 = new javax.swing.JButton();
        band4 = new javax.swing.JButton();
        band5 = new javax.swing.JButton();
        band6 = new javax.swing.JButton();
        band7 = new javax.swing.JButton();
        band8 = new javax.swing.JButton();
        band9 = new javax.swing.JButton();
        band10 = new javax.swing.JButton();
        band11 = new javax.swing.JButton();
        band12 = new javax.swing.JButton();
        band13 = new javax.swing.JButton();
        band14 = new javax.swing.JButton();
        band15 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 153, 153));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        band1.setBackground(new java.awt.Color(153, 153, 153));
        band1.setText("160");
        band1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band1ActionPerformed(evt);
            }
        });
        add(band1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 50, -1));

        band2.setBackground(new java.awt.Color(153, 153, 153));
        band2.setText("80");
        band2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band2ActionPerformed(evt);
            }
        });
        add(band2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 50, -1));

        band3.setBackground(new java.awt.Color(153, 153, 153));
        band3.setText("60");
        band3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band3ActionPerformed(evt);
            }
        });
        add(band3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 50, -1));

        band4.setBackground(new java.awt.Color(153, 153, 153));
        band4.setText("40");
        band4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band4ActionPerformed(evt);
            }
        });
        add(band4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, -1));

        band5.setBackground(new java.awt.Color(153, 153, 153));
        band5.setText("30");
        band5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band5ActionPerformed(evt);
            }
        });
        add(band5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, -1));

        band6.setBackground(new java.awt.Color(153, 153, 153));
        band6.setText("20");
        band6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band6ActionPerformed(evt);
            }
        });
        add(band6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 50, -1));

        band7.setBackground(new java.awt.Color(153, 153, 153));
        band7.setText("17");
        band7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band7.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band7ActionPerformed(evt);
            }
        });
        add(band7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 50, -1));

        band8.setBackground(new java.awt.Color(153, 153, 153));
        band8.setText("15");
        band8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band8.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band8ActionPerformed(evt);
            }
        });
        add(band8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 50, -1));

        band9.setBackground(new java.awt.Color(153, 153, 153));
        band9.setText("12");
        band9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band9.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band9ActionPerformed(evt);
            }
        });
        add(band9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 50, -1));

        band10.setBackground(new java.awt.Color(153, 153, 153));
        band10.setText("10");
        band10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band10.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band10ActionPerformed(evt);
            }
        });
        add(band10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 50, -1));

        band11.setBackground(new java.awt.Color(153, 153, 153));
        band11.setText("6");
        band11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band11.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band11ActionPerformed(evt);
            }
        });
        add(band11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 50, -1));

        band12.setBackground(new java.awt.Color(153, 153, 153));
        band12.setText("2");
        band12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band12.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band12ActionPerformed(evt);
            }
        });
        add(band12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 50, -1));

        band13.setBackground(new java.awt.Color(153, 153, 153));
        band13.setText("VHF+");
        band13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band13.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band13ActionPerformed(evt);
            }
        });
        add(band13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 50, -1));

        band14.setBackground(new java.awt.Color(153, 153, 153));
        band14.setText("WWV");
        band14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band14.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band14ActionPerformed(evt);
            }
        });
        add(band14, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 50, -1));

        band15.setBackground(new java.awt.Color(153, 153, 153));
        band15.setText("Gen");
        band15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        band15.setMargin(new java.awt.Insets(2, 2, 2, 2));
        band15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                band15ActionPerformed(evt);
            }
        });
        add(band15, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 50, -1));

        jLabel1.setText("Band");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void band15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band15ActionPerformed
        bandListener.bandSelected(Bands.BAND_15);
    }//GEN-LAST:event_band15ActionPerformed

    private void band14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band14ActionPerformed
        bandListener.bandSelected(Bands.BAND_14);
    }//GEN-LAST:event_band14ActionPerformed

    private void band13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band13ActionPerformed
        bandListener.bandSelected(Bands.BAND_13);
    }//GEN-LAST:event_band13ActionPerformed

    private void band12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band12ActionPerformed
        bandListener.bandSelected(Bands.BAND_12);
    }//GEN-LAST:event_band12ActionPerformed

    private void band11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band11ActionPerformed
        bandListener.bandSelected(Bands.BAND_11);
    }//GEN-LAST:event_band11ActionPerformed

    private void band10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band10ActionPerformed
        bandListener.bandSelected(Bands.BAND_10);
    }//GEN-LAST:event_band10ActionPerformed

    private void band9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band9ActionPerformed
        bandListener.bandSelected(Bands.BAND_9);
    }//GEN-LAST:event_band9ActionPerformed

    private void band8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band8ActionPerformed
        bandListener.bandSelected(Bands.BAND_8);
    }//GEN-LAST:event_band8ActionPerformed

    private void band7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band7ActionPerformed
        bandListener.bandSelected(Bands.BAND_7);
    }//GEN-LAST:event_band7ActionPerformed

    private void band6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band6ActionPerformed
        bandListener.bandSelected(Bands.BAND_6);
    }//GEN-LAST:event_band6ActionPerformed

    private void band5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band5ActionPerformed
        bandListener.bandSelected(Bands.BAND_5);
    }//GEN-LAST:event_band5ActionPerformed

    private void band4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band4ActionPerformed
        bandListener.bandSelected(Bands.BAND_4);
    }//GEN-LAST:event_band4ActionPerformed

    private void band3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band3ActionPerformed
        bandListener.bandSelected(Bands.BAND_3);
    }//GEN-LAST:event_band3ActionPerformed

    private void band2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band2ActionPerformed
        bandListener.bandSelected(Bands.BAND_2);
    }//GEN-LAST:event_band2ActionPerformed

    private void band1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_band1ActionPerformed
        bandListener.bandSelected(Bands.BAND_1);
    }//GEN-LAST:event_band1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton band1;
    private javax.swing.JButton band10;
    private javax.swing.JButton band11;
    private javax.swing.JButton band12;
    private javax.swing.JButton band13;
    private javax.swing.JButton band14;
    private javax.swing.JButton band15;
    private javax.swing.JButton band2;
    private javax.swing.JButton band3;
    private javax.swing.JButton band4;
    private javax.swing.JButton band5;
    private javax.swing.JButton band6;
    private javax.swing.JButton band7;
    private javax.swing.JButton band8;
    private javax.swing.JButton band9;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    
    private BandListener bandListener;
}
