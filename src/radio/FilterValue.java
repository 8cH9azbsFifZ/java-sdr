/*
 * FilterValue.java
 *
 * Created on August 29, 2007, 2:39 PM
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
 *
 */

package radio;

public class FilterValue implements java.io.Serializable {
    
    /**
     * Creates a new instance of FilterValue
     */
    public FilterValue() {    
    }
    
    public FilterValue(String title,int low,int high) {
        this.title=title;
        this.low=low;
        this.high=high;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getLow() {
        return low;
    }
    
    public int getHigh() {
        return high;
    }
    
    public void setTitle(String title) {
        this.title=title;
    }
    
    public void setLow(int low) {
        this.low=low;
    }
    
    public void setHigh(int high) {
        this.high=high;
    }
    
    public void setValues(int low,int high) {
        this.low=low;
        this.high=high;
    }
    
    private String title;
    private int low;
    private int high;
    
}
