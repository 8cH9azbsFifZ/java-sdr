/*
 * MeterThread.java
 *
 * Created on September 28, 2007, 11:32 AM
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

public class MeterThread extends Thread {
    
    /** Creates a new instance of MeterThread */
    public MeterThread(java.io.File meterFifo,MeterInterface meter,int id) {
        System.err.println("Meter");
        this.meter=meter;
        this.id=id;
        try {
            in=new java.io.FileInputStream(meterFifo);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("FIFO does not exist: "+meterFifo.getAbsolutePath());
        }
    }
    
    public void run() {
        System.err.println("MeterThread.run");
        if(in!=null) {
            try {
                int BUFFER_SIZE;
                if(id==Meter.RX_METER) {
                    BUFFER_SIZE=4+(4*MAX_RX*RX_SAMPLES);
                } else {
                    BUFFER_SIZE=4+(4*TX_SAMPLES);
                }
                byte[] data=new byte[BUFFER_SIZE];
                int label;

                float[] samples;
                if(id==Meter.RX_METER) {
                    samples=new float[MAX_RX*RX_SAMPLES];
                } else {
                    samples=new float[TX_SAMPLES];
                }
                int n;
                while(!terminate) {
                    n=0;
                    while(n<BUFFER_SIZE) {
                        n+=in.read(data,n,BUFFER_SIZE-n);
                    }
                    // change the byte order so readInt and readFloat work
                    data=byteSwap(data);

                    //
                    java.io.DataInputStream din=new java.io.DataInputStream(new java.io.ByteArrayInputStream(data));
                    label=din.readInt();
                    for(int i=0;i<samples.length;i++) {
                        samples[i]=din.readFloat();
                    }
                    din.close();
                    meter.updateMeter(id,samples);
                }
                in.close();
                terminated=true;
                System.err.println("MeterThread.run: terminated");
            } catch (Exception e) {
                if(!terminate) {
                    System.err.println("MeterThread.run: "+e.toString());
                }
                try {
                    in.close();
                } catch (Exception er) {
                }
                terminated=true;
            }
            
        }
    }

    public void terminate() {
        this.terminate=true;
    }
    
    private byte[] byteSwap(byte[] in) {
        byte[] out=new byte[in.length];
        for(int i=0;i<in.length;i+=4) {
            out[i]=in[i+3];
            out[i+1]=in[i+2];
            out[i+2]=in[i+1];
            out[i+3]=in[i];
        }
        return out;
    }
    

    public static final int MAX_RX=4;
    public static final int RX_SAMPLES=5;
    public static final int TX_SAMPLES=9;
    
    public static final int SPEC_SEMI_RAW=0;
    public static final int SPEC_PRE_FILT=1;
    public static final int SPEC_POST_FILT=2;
    public static final int SPEC_POST_AGC=3;
    public static final int SPEC_POST_DET=4;
    public static final int SPEC_PREMOD=4;

    public static final int SPEC_MAG=0;
    public static final int SPEC_PWR=1;

    private MeterInterface meter;
    private java.io.FileInputStream in;
    private boolean terminate=false;
    private boolean terminated=false;
    
    private int id;
}
