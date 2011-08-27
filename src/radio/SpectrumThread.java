/*
 * SpectrumThread.java
 *
 * Created on August 30, 2007, 1:26 PM
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

public class SpectrumThread extends Thread {
    
    /**
     * Creates a new instance of SpectrumThread
     */
    public SpectrumThread(java.io.File spectrumFifo,DisplayInterface display) {
        System.err.println("SpectrumThread:"+spectrumFifo.getAbsolutePath());
        this.display=display;
        try {
            in=new java.io.FileInputStream(spectrumFifo);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("FIFO does not exist: "+spectrumFifo.getAbsolutePath());
        }
    }
    
    public void run() {
        System.err.println("SpectrumThread.run");
        if(in!=null) {
            try {
                int BUFFER_SIZE=4+4+(4*4096); // int + int + float*4096
                byte[] data=new byte[BUFFER_SIZE];
                int label;
                int bufferCount;
                float[] samples=new float[4096];
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
                    bufferCount=din.readInt();
                    for(int i=0;i<4096;i++) {
                        samples[i]=din.readFloat();
                    }
                    din.close();
                    display.updateGraph(samples);
                }
                in.close();
                terminated=true;
                System.err.println("SpectrumThread.run: terminated");
            } catch (Exception e) {
                if(!terminate) {
                     System.err.println("SpectrumThread.run: "+e.toString());
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
    

    public static final int SPEC_SEMI_RAW=0;
    public static final int SPEC_PRE_FILT=1;
    public static final int SPEC_POST_FILT=2;
    public static final int SPEC_POST_AGC=3;
    public static final int SPEC_POST_DET=4;
    public static final int SPEC_PREMOD=4;

    public static final int SPEC_MAG=0;
    public static final int SPEC_PWR=1;

    private DisplayInterface display;
    private java.io.FileInputStream in;
    private boolean terminate=false;
    private boolean terminated=false;
    
}
