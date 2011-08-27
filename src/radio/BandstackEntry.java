/*
 * BandstackEntry.java
 *
 * Created on August 19, 2007, 9:55 PM
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

public class BandstackEntry implements java.io.Serializable {
    
    /** Creates a new instance of Bandstack */
    public BandstackEntry() {    
    }
    
    public BandstackEntry(Bands band,double aFrequency,Modes mode,Filters filter) {
        this.band=band;        
        this.aFrequency=aFrequency;
        this.mode=mode;
        this.filter=filter;
    }

    public void setAFrequency(double frequency) {
        aFrequency=frequency;
    }
    
    public void setBand(Bands band) {
        this.band=band;
    }
    
    public void setMode(Modes mode) {
        this.mode=mode;
    }
    
    public void setFilter(Filters filter) {
        this.filter=filter;
    }
    
    public double getAFrequency() {
        return aFrequency;
    }
    
    public Bands getBand() {
        return band;
    }
    
    public Modes getMode() {
        return mode;
    }
    
    public Filters getFilter() {
        return filter;
    }
   
    private double aFrequency;
    private Bands band;
    private Modes mode;
    private Filters filter;
    
}
