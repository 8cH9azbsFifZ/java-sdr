/*
 * Bandstacks.java
 *
 * Created on November 3, 2007, 4:52 PM
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
 *  Bandstacks
 */
public class Bandstacks {
    
    /** Creates a new instance of Bandstacks */
    public Bandstacks() {
    }
    
    public Bandstacks(int length) {
        bandstacks=new Bandstack[length];
    }
    
    public Bandstack[] getBandstacks() {
        return bandstacks;
    }
    
    public Bandstack getBandstack(Bands band) {
        return bandstacks[band.ordinal()];
    }
    
    public BandstackEntry getBandstackEntry(Bands band) {
        
        System.err.println("Bandstacks.getBandstackEntry: "+band+","+band.ordinal());
        return bandstacks[band.ordinal()].getBandstackEntry();
    }
    
    public BandstackEntry getNextBandstackEntry(Bands band) {
        return bandstacks[band.ordinal()].getNextBandstackEntry();
    }
    
    public void set(int i,int length) {
        bandstacks[i]=new Bandstack(length);
    }
    
    public void setEntry(int i, int j, BandstackEntry entry) {
        bandstacks[i].set(j,entry);
    }
    
    public void setIndex(int i,int index) {
        bandstacks[i].setBandstackIndex(index);
    }
    
    public void init() {
        bandstacks=new Bandstack[30];
        bandstacks[0]=new Bandstack(3);
        bandstacks[0].set(0,new BandstackEntry(Bands.BAND_1,1.81,Modes.CWL,Filters.FILTER_5));
        bandstacks[0].set(1,new BandstackEntry(Bands.BAND_1,1.835,Modes.CWU,Filters.FILTER_1));
        bandstacks[0].set(2,new BandstackEntry(Bands.BAND_1,1.845,Modes.USB,Filters.FILTER_6));
        bandstacks[1]=new Bandstack(3);
        bandstacks[1].set(0,new BandstackEntry(Bands.BAND_2,3.501,Modes.CWL,Filters.FILTER_1));
        bandstacks[1].set(1,new BandstackEntry(Bands.BAND_2,3.751,Modes.LSB,Filters.FILTER_6));
        bandstacks[1].set(2,new BandstackEntry(Bands.BAND_2,3.850,Modes.LSB,Filters.FILTER_6));
        bandstacks[2]=new Bandstack(5);
        bandstacks[2].set(0,new BandstackEntry(Bands.BAND_3,5.3305,Modes.USB,Filters.FILTER_6));
        bandstacks[2].set(1,new BandstackEntry(Bands.BAND_3,5.3465,Modes.USB,Filters.FILTER_6));
        bandstacks[2].set(2,new BandstackEntry(Bands.BAND_3,5.3665,Modes.USB,Filters.FILTER_6));
        bandstacks[2].set(3,new BandstackEntry(Bands.BAND_3,5.3715,Modes.USB,Filters.FILTER_6));
        bandstacks[2].set(4,new BandstackEntry(Bands.BAND_3,5.4035,Modes.USB,Filters.FILTER_6));
        bandstacks[3]=new Bandstack(3);
        bandstacks[3].set(0,new BandstackEntry(Bands.BAND_4,7.001,Modes.CWL,Filters.FILTER_1));
        bandstacks[3].set(1,new BandstackEntry(Bands.BAND_4,7.152,Modes.LSB,Filters.FILTER_6));
        bandstacks[3].set(2,new BandstackEntry(Bands.BAND_4,7.255,Modes.LSB,Filters.FILTER_6));
        bandstacks[4]=new Bandstack(3);
        bandstacks[4].set(0,new BandstackEntry(Bands.BAND_5,10.12,Modes.CWU,Filters.FILTER_1));
        bandstacks[4].set(1,new BandstackEntry(Bands.BAND_5,10.13,Modes.CWU,Filters.FILTER_1));
        bandstacks[4].set(2,new BandstackEntry(Bands.BAND_5,10.14,Modes.CWU,Filters.FILTER_5));
        bandstacks[5]=new Bandstack(3);
        bandstacks[5].set(0,new BandstackEntry(Bands.BAND_6,14.010,Modes.CWU,Filters.FILTER_1));
        bandstacks[5].set(1,new BandstackEntry(Bands.BAND_6,14.230,Modes.USB,Filters.FILTER_6));
        bandstacks[5].set(2,new BandstackEntry(Bands.BAND_6,14.336,Modes.USB,Filters.FILTER_6));
        bandstacks[6]=new Bandstack(3);
        bandstacks[6].set(0,new BandstackEntry(Bands.BAND_7,18.0686,Modes.CWU,Filters.FILTER_1));
        bandstacks[6].set(1,new BandstackEntry(Bands.BAND_7,18.125,Modes.USB,Filters.FILTER_6));
        bandstacks[6].set(2,new BandstackEntry(Bands.BAND_7,18.140,Modes.USB,Filters.FILTER_6));
        bandstacks[7]=new Bandstack(3);
        bandstacks[7].set(0,new BandstackEntry(Bands.BAND_8,21.001,Modes.CWU,Filters.FILTER_1));
        bandstacks[7].set(1,new BandstackEntry(Bands.BAND_8,21.255,Modes.USB,Filters.FILTER_6));
        bandstacks[7].set(2,new BandstackEntry(Bands.BAND_8,21.300,Modes.USB,Filters.FILTER_6));
        bandstacks[8]=new Bandstack(3);
        bandstacks[8].set(0,new BandstackEntry(Bands.BAND_9,24.895,Modes.CWU,Filters.FILTER_1));
        bandstacks[8].set(1,new BandstackEntry(Bands.BAND_9,24.900,Modes.USB,Filters.FILTER_6));
        bandstacks[8].set(2,new BandstackEntry(Bands.BAND_9,24.910,Modes.USB,Filters.FILTER_6));
        bandstacks[9]=new Bandstack(3);
        bandstacks[9].set(0,new BandstackEntry(Bands.BAND_10,28.010,Modes.CWU,Filters.FILTER_1));
        bandstacks[9].set(1,new BandstackEntry(Bands.BAND_10,28.300,Modes.USB,Filters.FILTER_6));
        bandstacks[9].set(2,new BandstackEntry(Bands.BAND_10,28.400,Modes.USB,Filters.FILTER_6));
        bandstacks[10]=new Bandstack(3);
        bandstacks[10].set(0,new BandstackEntry(Bands.BAND_11,50.010,Modes.CWU,Filters.FILTER_1));
        bandstacks[10].set(1,new BandstackEntry(Bands.BAND_11,50.125,Modes.USB,Filters.FILTER_6));
        bandstacks[10].set(2,new BandstackEntry(Bands.BAND_11,50.200,Modes.USB,Filters.FILTER_6));
        bandstacks[11]=new Bandstack(3);
        bandstacks[11].set(0,new BandstackEntry(Bands.BAND_12,144.010,Modes.CWU,Filters.FILTER_1));
        bandstacks[11].set(1,new BandstackEntry(Bands.BAND_12,144.200,Modes.USB,Filters.FILTER_6));
        bandstacks[11].set(2,new BandstackEntry(Bands.BAND_12,144.210,Modes.USB,Filters.FILTER_6));
        bandstacks[13]=new Bandstack(5);
        bandstacks[13].set(0,new BandstackEntry(Bands.BAND_14,2.5,Modes.SAM,Filters.FILTER_7));
        bandstacks[13].set(1,new BandstackEntry(Bands.BAND_14,5.0,Modes.SAM,Filters.FILTER_7));
        bandstacks[13].set(2,new BandstackEntry(Bands.BAND_14,10.0,Modes.SAM,Filters.FILTER_7));
        bandstacks[13].set(3,new BandstackEntry(Bands.BAND_14,15.0,Modes.SAM,Filters.FILTER_7));
        bandstacks[13].set(4,new BandstackEntry(Bands.BAND_14,20.0,Modes.SAM,Filters.FILTER_7));
        bandstacks[14]=new Bandstack(3);
        bandstacks[14].set(0,new BandstackEntry(Bands.BAND_15,13.845,Modes.SAM,Filters.FILTER_6));
        bandstacks[14].set(1,new BandstackEntry(Bands.BAND_15,5.975,Modes.SAM,Filters.FILTER_7));
        bandstacks[14].set(2,new BandstackEntry(Bands.BAND_15,0.909,Modes.SAM,Filters.FILTER_4));
    }
    
    private Bandstack[] bandstacks;
    
}
