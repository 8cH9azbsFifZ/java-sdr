/*
 * Power.java
 *
 * Created on February 8, 2008, 9:32 PM
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

/**
 *
 * @author jm57878
 */
public class Power {
    
    /** Creates a new instance of PowerThread */
    public Power(java.io.File powerFifo) {
        System.err.println("Power");
        try {
            in=new java.io.FileInputStream(powerFifo);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("FIFO does not exist: "+powerFifo.getAbsolutePath());
        }
    }
    
    public int getPower() {
        int power=0;
        if(in!=null) {
            try {
                int BUFFER_SIZE=2;
                byte[] data=new byte[BUFFER_SIZE];
                int n=0;
                while(n<BUFFER_SIZE) {
                    n+=in.read(data,n,BUFFER_SIZE-n);
                }
                power=(int)data[1]&0xFF;
                
//System.err.println("Power.getPower: "+((int)data[0]&0xFF)+","+power);
                
            } catch (Exception e) {

                System.err.println("Power.getPower: "+e.toString());

                try {
                    in.close();
                } catch (Exception er) {
                }
            }
            
        }
        return power;
    }
    

    public void terminate() {
        if(in!=null) {
            try {
                in.close();
            } catch (Exception er) {
            }
        }
    }
    
    private java.io.FileInputStream in;
    
}
