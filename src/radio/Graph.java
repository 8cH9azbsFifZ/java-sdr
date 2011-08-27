/*
 * Graph.java
 *
 * Created on September 8, 2007, 5:31 PM
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

import java.awt.Image;

/**
 *
 *  Graph
 */
public class Graph extends javax.swing.JPanel {
    
    /** Creates new form Graph */
    public Graph() {
        initComponents();
        X=new int[WIDTH];
        Y=new int[WIDTH];
        Y1=new int[WIDTH];
        pixels=new int[WIDTH*HEIGHT];
        for(int i=0;i<pixels.length;i++) {
            pixels[i]=255<<24;
        }
    }
    
    public void addListener(DisplayListener displayListener) {
        this.displayListener=displayListener;
    }
    
    public void setRadio(Radios radio) {
        this.radio=radio;
    }
    
    public void setSoftrockCenterFrequency(double f) {
        softrockCenterFrequency=f;
        softrockLowFrequency=softrockCenterFrequency+((double)low/1000000.0);
        softrockHighFrequency=softrockCenterFrequency+((double)high/1000000.0);
    }
    
    public void setSampleRate(int sampleRate) {
        System.err.println("Graph.setSampleRate: "+sampleRate);
        this.sampleRate=sampleRate;
        low=-(sampleRate/2);
        high=sampleRate/2;
        softrockLowFrequency=softrockCenterFrequency+((double)low/1000000.0);
        softrockHighFrequency=softrockCenterFrequency+((double)high/1000000.0);
        switch(sampleRate) {
            case 24000:
                increment=25;
                break;
            case 44100:
                increment=50;
                break;
            case 48000:
                increment=50;
                break;
            case 96000:
                increment=100;
                break;
            case 192000:
                increment=200;
                break;
        }
        x=WIDTH*increment;
        //System.err.println("Graph.setSampleRate: x="+x);
        //System.err.println("Graph.setSampleRate: increment="+increment);
    }
    
    public void setFilter(int low,int high) {
        filterLow=low;
        filterHigh=high;
    }
    
    public void setOffset(double offset) {
        System.err.println("Graph.setOffset: "+offset);
        this.offset=offset;
    }
    
    public void setFrequency(double frequency) {
        this.frequency=frequency;
    }
    
    public void setSpectrumLow(int spectrumLow) {
        this.spectrumLow=spectrumLow;
        System.err.println("Grapg.setSpectrumLow: "+spectrumLow);
    }
    
    public void setSpectrumHigh(int spectrumHigh) {
        this.spectrumHigh=spectrumHigh;
        System.err.println("Grapg.setSpectrumHigh: "+spectrumHigh);
    }
    
    public void setWaterfallLowThreshold(float low) {
        this.waterfallLowThreshold=low;
    }
    
    public void setWaterfallHighThreshold(float high) {
        this.waterfallHighThreshold=high;
    }
    
    protected void paintComponent(java.awt.Graphics g) {

        if(display==Displays.NONE) {
            g.setColor(java.awt.Color.BLACK);
            g.fillRect(0,0,WIDTH,HEIGHT);
        } else if(display==Displays.WATERFALL) {
            java.awt.Image waterfallImage=java.awt.Toolkit.getDefaultToolkit().createImage(new java.awt.image.MemoryImageSource(WIDTH,HEIGHT,pixels,0,WIDTH));
            g.drawImage(waterfallImage,0,0,null);
        } else if(display==Displays.PANFALL) {
            g.drawImage(image,0,0,null);
            java.awt.Image panfallImage=java.awt.Toolkit.getDefaultToolkit().createImage(new java.awt.image.MemoryImageSource(WIDTH,HEIGHT/2,pixels,WIDTH*10,WIDTH));
            g.drawImage(panfallImage,0,HEIGHT/2,null);     
        } else {
            if(image!=null) {
                g.drawImage(image,0,0,null);
            } else {
                g.setColor(java.awt.Color.BLACK);
                g.fillRect(0,0,WIDTH,HEIGHT);
            }
        }
    }
    
    public void setDisplayCalibrationOffset(double displayCalibrationOffset) {
        this.displayCalibrationOffset=(float)displayCalibrationOffset;
    }
    
    public void setPreampOffset(double preampOffset) {
        this.preampOffset=(float)preampOffset;
    }
    
    public void setFilterSizeOffset(int offset) {
        this.filterSizeOffset=offset;
    }
    
    public void updateGraph(float[] samples,Displays display) {
        if(image==null) image=this.createImage(WIDTH,HEIGHT);
        if((this.display!=display) && (display==Displays.WATERFALL)) {
            java.awt.Graphics graphics=image.getGraphics();
            graphics.setColor(java.awt.Color.BLACK);
            graphics.fillRect(0,0,WIDTH,HEIGHT);
        }
        this.display=display;
        
        switch(display) {
            case SPECTRUM:
                plotSpectrum(samples);
                drawSpectrum();
            case PANADAPTER:
                plotSpectrum(samples);
                drawSpectrum();
                break;
            case SCOPE:
                plotScope(samples);
                drawScope();
                break;
            case WATERFALL:
                plotSpectrum(samples);
                drawWaterfall();
                break;
            case PHASE:
                plotPhase(samples);
                drawPhase();
                break;
            case PANFALL:
                // combine panadapter and waterfall
                plotPanfall(samples);
                drawPanfall();
                drawWaterfall();
                break;
        }
        this.repaint(new java.awt.Rectangle(WIDTH,HEIGHT));
    }
    
    private void plotSpectrum(float[] samples) {
        int num_samples=x*samples.length/sampleRate;
        float slope=(float)num_samples/(float)WIDTH;
        int spectrum_max_x=0;
        float spectrum_max_y=-1000000.0F;
        int yRange=spectrumHigh-spectrumLow;
        
        if(radio==Radios.Softrock) {
            fixupSamplesForSoftrock(samples);
        }
        
        for(int i=0;i<WIDTH;i++) {
            float max = -1000000.0F;
            float dval = i*slope;
            int lindex = (int)Math.floor(dval);
            int rindex = (int)Math.floor(dval + slope);
            if (rindex > samples.length) rindex = samples.length;

            for(int j=lindex;j<rindex;j++) {
                if (samples[j] > max) max=samples[j];
            }

            max = max + displayCalibrationOffset + preampOffset + filterSizeOffset;

            if(max > spectrum_max_y)
            {
                spectrum_max_y = max;
                spectrum_max_x = i;
            }
            X[i] = i;
            Y1[i]=(int)max;
            Y[i] = (int)(Math.floor(((float)spectrumHigh - max)*(float)HEIGHT/(float)yRange));
        }
    }
    
    private void drawSpectrum() {
        java.awt.Graphics graphics=image.getGraphics();
        graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0,0,WIDTH,HEIGHT);

        // plot filter
        graphics.setColor(java.awt.Color.GRAY);
        int filterLeftX=(filterLow-low)*WIDTH/x;
        int filterRightX=(filterHigh-low)*WIDTH/x;
        if(filterLeftX==filterRightX) filterRightX++;
        graphics.fillRect(filterLeftX,20,filterRightX-filterLeftX,HEIGHT);

        int f=0;
        f=(int)(frequency*1000000.0)-(sampleRate/2); //-((int)(offset*1000000.0)*2);
        
        // plot frequency markers
        int startFrequency=f;
        for(int i=0;i<WIDTH;i++) {
            if((f%20000) < increment) {
                graphics.setColor(gridColor);
                graphics.drawLine(i,20,i,HEIGHT);
                graphics.setColor(java.awt.Color.GREEN);
                graphics.drawString(frequencyFormat.format((double)f/1000000.0),i-17,15);
            }
            f+=increment;
        }
        
        int endFrequency=f;
        
        // plot band edges
        for(int i=0;i<bandEdge.length;i++) {
            if(bandEdge[i]>startFrequency && bandEdge[i]<endFrequency) {
                int loc=((bandEdge[i]-startFrequency)/(int)increment)+((startFrequency%200)==0?0:1);
                
                graphics.setColor(bandMarker);
                graphics.drawLine(loc,20,loc,HEIGHT);
                graphics.setColor(java.awt.Color.RED);
                graphics.drawString(frequencyFormat.format((double)bandEdge[i]/1000000.0),loc-17,15);
            }
        }
        
        // plot horizontal grid
        graphics.setColor(gridColor);
        int V = spectrumHigh - spectrumLow;
        int numSteps = V/STEP;
        int pixelStepSize = HEIGHT/numSteps;
        for(int i=1; i<numSteps; i++)
        {
            int num = spectrumHigh - i*STEP;
            int y = (int)Math.floor((spectrumHigh - num)*HEIGHT/V);
            
            graphics.setColor(gridColor);
            graphics.drawLine(0, y, WIDTH, y);
            
            graphics.setColor(java.awt.Color.GREEN);
            graphics.drawString(Integer.toString(num),3,y);
        }

        // plot cursor
        if(radio==Radios.SDR1000) {
            graphics.setColor(cursorColor);
            int cursorX=-low*WIDTH/(x);
            graphics.drawLine(cursorX,20,cursorX,HEIGHT);
        }
        
        // plot data
        if(X!=null) {
            graphics.setColor(plotColor);
            graphics.drawPolyline(X,Y,X.length);
        }

    }
    
    private void plotScope(float[] samples) {
        int y=0;
        int pixels=0;
        for(int i=0;i<WIDTH;i++) {
            pixels=(int)((float)(HEIGHT/2)*samples[i*2]);
            y = HEIGHT/2 -pixels;
            X[i] = i;
            Y[i] = y;
        }
    }
    
    private void drawScope() {
        java.awt.Graphics graphics=image.getGraphics();
        graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0,0,WIDTH,HEIGHT);

        // plot grid
        graphics.setColor(java.awt.Color.LIGHT_GRAY);
        graphics.drawLine(0,HEIGHT/2,WIDTH,HEIGHT/2);
        graphics.drawLine(WIDTH/2,0,WIDTH/2,HEIGHT);
        
        // plot data
        if(X!=null) {
            graphics.setColor(java.awt.Color.YELLOW);
            graphics.drawPolyline(X,Y,X.length);
        }
    }

    private void plotPhase(float[] samples) {
        int x;
        int y;

        for(int i=0;i<PHASE_POINTS;i++) {
            x = (int)(samples[i*2]*(float)HEIGHT/2.0F);   //re
            y = (int)(samples[i*2+1]*(float)HEIGHT/2.0F); //im
            X[i] = (WIDTH/2)+x;
            Y[i] = (HEIGHT/2)+y;
        }
    }
    
    private void drawPhase() {
        java.awt.Graphics graphics=image.getGraphics();
        graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0,0,WIDTH,HEIGHT);

        // plot grid
        graphics.setColor(java.awt.Color.LIGHT_GRAY);
        graphics.drawLine(0,HEIGHT/2,WIDTH,HEIGHT/2);
        graphics.drawLine(WIDTH/2,0,WIDTH/2,HEIGHT);
        
        // plot data
        if(X!=null) {
            graphics.setColor(java.awt.Color.YELLOW);
            for(int i=0;i<PHASE_POINTS;i++) {
                graphics.drawRect(X[i],Y[i],1,1);
            }
        }
        
    }
    
    private void plotPhase2(float[] samples) {
                int x;
        int y;

        for(int i=0;i<PHASE_POINTS;i++) {
            x = (int)(samples[i*2]*(float)HEIGHT/2.0F);   //re
            y = (int)(samples[i*2+1]*(float)HEIGHT/2.0F); //im
            X[i] = (WIDTH/2)+x;
            Y[i] = (HEIGHT/2)+y;
        }
    }
    
    private void drawPhase2() {
        
    }
    
    private void drawWaterfall() {
        // calculate the filter
        int filterLeftX=(filterLow-low)*WIDTH/x;
        int filterRightX=(filterHigh-low)*WIDTH/x;
        if(filterLeftX==filterRightX) filterRightX++;

        // calculate the cursor
        int cursorX=-low*WIDTH/(x);

        // first 10 lines are used to show the filter and cursor
        int R,G,B;                      
        for(int i=0;i<10;i++) {
            for(int j=0;j<WIDTH;j++) {
                if(j==cursorX) {
                     R=java.awt.Color.RED.getRed();
                     G=java.awt.Color.RED.getGreen();
                     B=java.awt.Color.RED.getBlue();
                } else if(j>=filterLeftX && j<= filterRightX) {
                     R=java.awt.Color.DARK_GRAY.getRed();
                     G=java.awt.Color.DARK_GRAY.getGreen();
                     B=java.awt.Color.DARK_GRAY.getBlue();
                } else {
                     R=java.awt.Color.BLACK.getRed();
                     G=java.awt.Color.BLACK.getGreen();
                     B=java.awt.Color.BLACK.getBlue();
                }
                pixels[(i*WIDTH)+j]=(255<<24)+(R<<16)+(G<<8)+B;
            }
        }
        
        // move the remaining lines down one
        System.arraycopy(pixels,WIDTH*10,pixels,WIDTH*11,(WIDTH*HEIGHT)-(WIDTH*11));
        
        // draw the new line
        for(int i=0;i<WIDTH;i++) {

            pixels[i+(WIDTH*10)]=calculatePixel(Y1[i]);
        }

    }
    
    private void plotPanfall(float[] samples) {
        // panadapter in top half
        int num_samples=x*samples.length/sampleRate;
        float slope=(float)num_samples/(float)WIDTH;
        int spectrum_max_x=0;
        float spectrum_max_y=-1000000.0F;
        int yRange=spectrumHigh-spectrumLow;
        
        if(radio==Radios.Softrock) {
            fixupSamplesForSoftrock(samples);
        }
        
        for(int i=0;i<WIDTH;i++) {
            float max = -1000000.0F;
            float dval = i*slope;
            int lindex = (int)Math.floor(dval);
            int rindex = (int)Math.floor(dval + slope);
            if (rindex > samples.length) rindex = samples.length;

            for(int j=lindex;j<rindex;j++) {
                if (samples[j] > max) max=samples[j];
            }

            max = max + displayCalibrationOffset + preampOffset + filterSizeOffset;

            if(max > spectrum_max_y)
            {
                spectrum_max_y = max;
                spectrum_max_x = i;
            }
            X[i] = i;
            Y1[i]=(int)max;
            Y[i] = (int)(Math.floor(((float)spectrumHigh - (float)max)*(float)(HEIGHT/2)/(float)yRange));
        }    
    }
    
    private void drawPanfall() {
        java.awt.Graphics graphics=image.getGraphics();
        graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0,0,WIDTH,HEIGHT/2);

        // plot filter
        graphics.setColor(java.awt.Color.GRAY);
        int filterLeftX=(filterLow-low)*WIDTH/x;
        int filterRightX=(filterHigh-low)*WIDTH/x;
        if(filterLeftX==filterRightX) filterRightX++;
        graphics.fillRect(filterLeftX,20,filterRightX-filterLeftX,HEIGHT/2);

        int f=(int)(frequency*1000000.0)-(sampleRate/2); //-((int)(offset*1000000.0)*2);
        
        // plot frequency markers
        int startFrequency=f;
        for(int i=0;i<WIDTH;i++) {
            if((f%20000) < increment) {
                graphics.setColor(gridColor);
                graphics.drawLine(i,20,i,HEIGHT);
                graphics.setColor(java.awt.Color.GREEN);
                graphics.drawString(frequencyFormat.format((double)f/1000000.0),i-17,15);
            }
            f+=increment;
        }
        
        int endFrequency=f;
        
        // plot band edges
        for(int i=0;i<bandEdge.length;i++) {
            if(bandEdge[i]>startFrequency && bandEdge[i]<endFrequency) {
                int loc=((bandEdge[i]-startFrequency)/(int)increment)+((startFrequency%200)==0?0:1);
                
                graphics.setColor(bandMarker);
                graphics.drawLine(loc,20,loc,HEIGHT/2);
                graphics.setColor(java.awt.Color.RED);
                graphics.drawString(frequencyFormat.format((double)bandEdge[i]/1000000.0),loc-17,15);
            }
        }
        // plot horizontal grid
        graphics.setColor(gridColor);
        int V = spectrumHigh - spectrumLow;
        int numSteps = V/STEP;
        int pixelStepSize = HEIGHT/2/numSteps;
        for(int i=1; i<numSteps; i++)
        {
            int num = spectrumHigh - i*STEP;
            int y = (int)Math.floor((spectrumHigh - num)*HEIGHT/2/V);
            
            graphics.setColor(gridColor);
            graphics.drawLine(0, y, WIDTH, y);
            
            graphics.setColor(java.awt.Color.GREEN);
            graphics.drawString(Integer.toString(num),3,y);
        }

        // plot cursor
        graphics.setColor(cursorColor);
        int cursorX=-low*WIDTH/(x);
        graphics.drawLine(cursorX,20,cursorX,HEIGHT/2);

        // plot data
        if(X!=null) {
            graphics.setColor(plotColor);
            graphics.drawPolyline(X,Y,X.length);
        }

    }
    
    private int calculatePixel(int value){
        int R,G,B;
        int pixel=0;
        if(value<=waterfallLowThreshold) {
            R=waterfallLowColor.getRed();
            G=waterfallLowColor.getGreen();
            B=waterfallLowColor.getBlue();
        } else if(value>=waterfallHighThreshold) {
            R=192;
            G=124;
            B=255;
        } else {
            float range=waterfallHighThreshold-waterfallLowThreshold;
            float percent=((float)value-waterfallLowThreshold)/range;
            if(percent < 2.0F/9.0F ) {
                float p=percent/(2.0F/9.0F);
                R = (int)((1.0-p)*(float)waterfallLowColor.getRed());
                G = (int)((1.0-p)*(float)waterfallLowColor.getGreen());
                B = (int)((float)waterfallLowColor.getBlue()+p*(float)(255-waterfallLowColor.getBlue()));
            } else if(percent < (3.0F/9.0F) ) {
                float p=(percent-2.0F/9.0F)/(1.0F/9.0F);
                R = 0;
                G = (int)(p*255.0F);
                B = 255;
            } else if(percent < (4.0F/9.0F) ) {
                float p=(percent-3.0F/9.0F)/(1.0F/9.0F);
                R = 0;
                G = 255;
                B = (int)((1.0F-p)*255.0F);
            } else if(percent < (5.0F/9.0F) ) {
                float p=(percent-4.0F/9.0F)/(1.0F/9.0F);
                R = (int)(p*255.0F);
                G = 255;
                B = 0;
            } else if(percent < (7.0F/9.0F) ) {
                float p=(percent-5.0F/9.0F)/(1.0F/9.0F);
                R = 255;
                G = (int)((1.0-p)*255.0F);
                B = 0;
            } else if(percent < (8.0F/9.0F) ) {
                float p=(percent-7.0F/9.0F)/(1.0F/9.0F);
                R = 255;
                G = 0;
                B = (int)(p*255.0F);
            } else {
                float p=(percent-8.0F/9.0F)/(1.0F/9.0F);
                R = (int)((0.75F+0.25F*(1.0F-p))*255.0F);
                G = (int)(p*255.0F*0.5);
                B = 255;
            }
        }
        pixel=(255<<24)+(R<<16)+(G<<8)+B;
        return pixel;
    }
    
    private void fixupSamplesForSoftrock(float[] samples) {
        double f=frequency+((double)low/1000000.0);
        double increment=(double)sampleRate/(double)samples.length/1000000.0;
        for(int i=0;i<samples.length;i++) {
            if(f<softrockLowFrequency) {
                samples[i]=-200.0f;
            } else if(f>softrockHighFrequency) {
                samples[i]=-200.0f;
            }
            f+=increment;
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(0, 0, 0));
        setBorder(null);
        setMaximumSize(new java.awt.Dimension(880, 300));
        setMinimumSize(new java.awt.Dimension(880, 300));
        setVerifyInputWhenFocusTarget(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 960, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 960, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if(display==Displays.PANADAPTER || display==Displays.WATERFALL || display==Displays.PANFALL) {
            if(evt.getButton()==java.awt.event.MouseEvent.BUTTON1) {
                // center in filter
                double f=frequency-(double)((sampleRate/2)/1000000.0);
                f=f+(double)((evt.getX()*increment)/1000000.0);
                f=f-((double)(filterLow+((filterHigh-filterLow)/2))/1000000.0);
                displayListener.setFrequency(f);
            } else {
                double f=frequency-(double)((sampleRate/2)/1000000.0);
                f=f+(double)((evt.getX()*increment)/1000000.0);
                displayListener.setFrequency(f);
            }
        }
       dragging=false;
    }//GEN-LAST:event_formMouseClicked

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        displayListener.incrementFrequency(evt.getWheelRotation());
    }//GEN-LAST:event_formMouseWheelMoved

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if(dragging) {
            displayListener.dragFrequency((double)((startDrag-evt.getX())*increment)/1000000.0);
            startDrag=evt.getX();
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased

    }//GEN-LAST:event_formMouseReleased

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if(display==Displays.PANADAPTER || display==Displays.WATERFALL || display==Displays.PANFALL) {
            if(evt.getButton()==java.awt.event.MouseEvent.BUTTON1) {
                startDrag=evt.getX();
                dragging=true;
            }
        }
    }//GEN-LAST:event_formMousePressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    private int WIDTH=880;
    private int HEIGHT=300;
    
    private java.text.DecimalFormat frequencyFormat=new java.text.DecimalFormat("####.00");
    private Image image;
    
    private Radios radio;
    private double softrockCenterFrequency=7.056000;
    private double softrockLowFrequency;
    private double softrockHighFrequency;
    private Displays display;
    private int sampleRate=192000;
    private int low=-96000;
    private int high=+96000;
    private int x=high-low;
    private int increment=100;
    private int spectrumLow=-70;
    private int spectrumHigh=-170;
    private int STEP=20;
    private float displayCalibrationOffset;
    private float preampOffset=-16F;
    private int filterSizeOffset;
    private int filterLow;
    private int filterHigh;
    private double frequency;
    private double offset;
    private int pixels[];
    private int X[];
    private int Y[];
    private int Y1[];
    
    private final int PHASE_POINTS=512;
    
    private DisplayListener displayListener;
    private int startDrag;
    private boolean dragging=false;
    
    private java.awt.Color plotColor=java.awt.Color.YELLOW;
    private java.awt.Color gridColor=java.awt.Color.LIGHT_GRAY;
    private java.awt.Color bandMarker=java.awt.Color.RED;
    private java.awt.Color cursorColor=java.awt.Color.RED;
    
    float waterfallLowThreshold=-160.0f; //-130.0f;
    float waterfallHighThreshold=-80.0f;
    java.awt.Color waterfallLowColor=java.awt.Color.BLACK;
    java.awt.Color waterfallMidColor=java.awt.Color.RED;
    java.awt.Color waterfallHighColor=java.awt.Color.YELLOW;

    
    private int[] bandEdge= {
                    1800000,
                    2000000,
                    3500000,
                    4000000, 
                    7000000,
                    7300000,
                    10100000,
                    10150000,
                    14000000,
                    14350000,
                    18068000,
                    18168000,
                    21000000,
                    21450000,
                    24890000,
                    24990000,
                    28000000,
                    29700000,
                    50000000,
                    54000000,
                    144000000,
                    148000000
                    };
}
