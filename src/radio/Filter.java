/*
 * Filter.java
 *
 * Created on September 9, 2007, 5:34 PM
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
 *  Filter
 */
public class Filter extends javax.swing.JPanel implements FilterInterface {
    
    /** Creates new form Filter */
    public Filter() {
        initComponents();
        settingValues=true;
        widthSlider.setMinimum(25);
        widthSlider.setMaximum(12000);
        offsetSlider.setMinimum(-12000);
        offsetSlider.setMaximum(12000);
        settingValues=false;
    }
    

    public void addListener(FilterListener filterListener) {
        this.filterListener=filterListener;
    }
    
    public void select(Filters filter) {
        javax.swing.JButton button=getButton(filter);
        if(button!=null) {
            button.setForeground(Color.YELLOW);
        }
    }
    
    public void unselect(Filters filter) {
        javax.swing.JButton button=getButton(filter);
        if(button!=null) {
            button.setForeground(Color.BLACK);
        }
    }
    
    public void set(int low,int high) {
        settingValues=true;
        this.low.setValue(new Integer(low));
        this.high.setValue(new Integer(high));
        this.offset=low;
        this.width=high-low;
        // set the width and offset
        offsetSlider.setValue(low);
        widthSlider.setValue(width);  
        settingValues=false;
    }
    
    public void setTitles(String[] titles) {
        if(titles.length>0) {
            jButton1.setText(titles[0]);
            jButton1.setEnabled(titles[0].length()>0);
        }
        if(titles.length>1) {
            jButton2.setText(titles[1]);
            jButton2.setEnabled(titles[1].length()>0);
        }
        if(titles.length>2) {
            jButton3.setText(titles[2]);
            jButton3.setEnabled(titles[2].length()>0);
        }
        if(titles.length>3) {
            jButton4.setText(titles[3]);
            jButton4.setEnabled(titles[3].length()>0);
        }
        if(titles.length>4) {
            jButton5.setText(titles[4]);
            jButton5.setEnabled(titles[4].length()>0);
        }
        if(titles.length>5) {
            jButton6.setText(titles[5]);
            jButton6.setEnabled(titles[5].length()>0);
        }
        if(titles.length>6) {
            jButton7.setText(titles[6]);
            jButton7.setEnabled(titles[6].length()>0);
        }
        if(titles.length>7) {
            jButton8.setText(titles[7]);
            jButton8.setEnabled(titles[7].length()>0);
        }
        if(titles.length>8) {
            jButton9.setText(titles[8]);
            jButton9.setEnabled(titles[8].length()>0);
        }
        if(titles.length>9) {
            jButton10.setText(titles[9]);
            jButton10.setEnabled(titles[9].length()>0);
        }
        if(titles.length>10) {
            jButton11.setText(titles[10]);
            jButton11.setEnabled(titles[10].length()>0);
        }
        if(titles.length>11) {
            jButton12.setText(titles[11]);
            jButton12.setEnabled(titles[11].length()>0);
        }
    }



    private javax.swing.JButton getButton(Filters filter) {
        javax.swing.JButton button=null;
        switch(filter) {
            case FILTER_1:
                button=jButton1;
                break;
            case FILTER_2:
                button=jButton2;
                break;
            case FILTER_3:
                button=jButton3;
                break;
            case FILTER_4:
                button=jButton4;
                break;
            case FILTER_5:
                button=jButton5;
                break;
            case FILTER_6:
                button=jButton6;
                break;
            case FILTER_7:
                button=jButton7;
                break;
            case FILTER_8:
                button=jButton8;
                break;
            case FILTER_9:
                button=jButton9;
                break;
            case FILTER_10:
                button=jButton10;
                break;
            case FILTER_11:
                button=jButton11;
                break;
            case FILTER_12:
                button=jButton12;
                break;
        }
        return button;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        offsetSlider = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        widthSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        low = new javax.swing.JSpinner();
        high = new javax.swing.JSpinner();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(153, 153, 153));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setText("Filter");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButton1.setBackground(new java.awt.Color(153, 153, 153));
        jButton1.setText("jButton1");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 50, -1));

        jButton2.setBackground(new java.awt.Color(153, 153, 153));
        jButton2.setText("jButton1");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 50, -1));

        jButton3.setBackground(new java.awt.Color(153, 153, 153));
        jButton3.setText("jButton1");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 50, -1));

        jButton4.setBackground(new java.awt.Color(153, 153, 153));
        jButton4.setText("jButton1");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 50, -1));

        jButton5.setBackground(new java.awt.Color(153, 153, 153));
        jButton5.setText("jButton1");
        jButton5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 50, -1));

        jButton6.setBackground(new java.awt.Color(153, 153, 153));
        jButton6.setText("jButton1");
        jButton6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 50, -1));

        jButton7.setBackground(new java.awt.Color(153, 153, 153));
        jButton7.setText("jButton1");
        jButton7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton7.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 50, -1));

        jButton8.setBackground(new java.awt.Color(153, 153, 153));
        jButton8.setText("jButton1");
        jButton8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton8.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 50, -1));

        jButton9.setBackground(new java.awt.Color(153, 153, 153));
        jButton9.setText("jButton1");
        jButton9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton9.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 50, -1));

        jButton10.setBackground(new java.awt.Color(153, 153, 153));
        jButton10.setText("jButton1");
        jButton10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton10.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 50, -1));

        jButton11.setBackground(new java.awt.Color(153, 153, 153));
        jButton11.setText("jButton1");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 50, -1));

        jButton12.setBackground(new java.awt.Color(153, 153, 153));
        jButton12.setText("jButton1");
        jButton12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton12.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 50, -1));

        jLabel2.setText("Offset");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        offsetSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                offsetSliderStateChanged(evt);
            }
        });

        add(offsetSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 90, -1));

        jLabel3.setText("Width");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        widthSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                widthSliderStateChanged(evt);
            }
        });

        add(widthSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, 90, -1));

        jLabel4.setText("Low");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        jLabel5.setText("High");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        low.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                lowStateChanged(evt);
            }
        });

        add(low, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 70, -1));

        high.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                highStateChanged(evt);
            }
        });

        add(high, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 70, -1));

    }// </editor-fold>//GEN-END:initComponents

    private void highStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_highStateChanged
        if(!settingValues) {
            int high=((Integer)this.high.getValue()).intValue();
            filterListener.setFilterValues(((Integer)this.low.getValue()).intValue(),high);
        }
    }//GEN-LAST:event_highStateChanged

    private void lowStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_lowStateChanged
        if(!settingValues) {
            int low=((Integer)this.low.getValue()).intValue();
            filterListener.setFilterValues(low,((Integer)this.high.getValue()).intValue());
        }
    }//GEN-LAST:event_lowStateChanged

    private void widthSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_widthSliderStateChanged
        if(!settingValues) {
            int width=widthSlider.getValue();
            filterListener.setFilterValues(offset,offset+width);
        }
    }//GEN-LAST:event_widthSliderStateChanged

    private void offsetSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_offsetSliderStateChanged
        if(!settingValues) {
            int offset=offsetSlider.getValue();
            filterListener.setFilterValues(offset,offset+width);
        }
    }//GEN-LAST:event_offsetSliderStateChanged

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        filterListener.filterSelected(Filters.FILTER_12);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        filterListener.filterSelected(Filters.FILTER_11);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        filterListener.filterSelected(Filters.FILTER_10);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        filterListener.filterSelected(Filters.FILTER_9);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        filterListener.filterSelected(Filters.FILTER_8);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        filterListener.filterSelected(Filters.FILTER_7);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        filterListener.filterSelected(Filters.FILTER_6);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        filterListener.filterSelected(Filters.FILTER_5);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        filterListener.filterSelected(Filters.FILTER_4);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        filterListener.filterSelected(Filters.FILTER_3);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        filterListener.filterSelected(Filters.FILTER_2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        filterListener.filterSelected(Filters.FILTER_1);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner high;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSpinner low;
    private javax.swing.JSlider offsetSlider;
    private javax.swing.JSlider widthSlider;
    // End of variables declaration//GEN-END:variables
    
    private FilterListener filterListener;
    private boolean settingValues=false;
    private int width;
    private int offset;
    
}
