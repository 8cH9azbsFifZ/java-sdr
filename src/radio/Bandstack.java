/*
 * Bandstack.java
 *
 * Created on August 19, 2007, 10:07 PM
 *
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

public class Bandstack implements java.io.Serializable {
    
    /** Creates a new instance of Bandstack */
    public Bandstack(int entries) {
        bandstack=new BandstackEntry[entries];
    }

    public void set(int entry,BandstackEntry bandstack) {
        this.bandstack[entry]=bandstack;
    }
    
    public BandstackEntry getBandstackEntry() {
        return bandstack[bandstackIndex];
    }
    
    public BandstackEntry getNextBandstackEntry() {
        bandstackIndex++;
        if(bandstackIndex>=bandstack.length) {
            bandstackIndex=0;
        }
        return bandstack[bandstackIndex];
    }
    
    public int getBandstackIndex() {
        return bandstackIndex;
    }
    
    public void setBandstackIndex(int bandstackIndex) {
        this.bandstackIndex=bandstackIndex;
    }
    
    public BandstackEntry[] getBandstackEntries() {
        return bandstack;
    }
    
    public void setBandstackEntries(BandstackEntry[] bandstack) {
        this.bandstack=bandstack;
    }
    
    private BandstackEntry[] bandstack;
    
    private int bandstackIndex=0;

    
}
