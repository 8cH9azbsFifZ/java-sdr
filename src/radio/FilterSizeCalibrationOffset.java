/*
 * FilterSizeCalibrationOffset.java
 *
 * Created on October 5, 2007, 10:11 AM
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

public class FilterSizeCalibrationOffset {
    
    /** Creates a new instance of FilterSizeCalibrationOffset */
    public FilterSizeCalibrationOffset() {
    }
    
    public void setFilterSize(int filterSize) {
        this.filterSize=filterSize;
    }
    
    public int getFilterSizeOffset() {
        int offset=0;
        switch(filterSize) {
            case 4096: offset = 0; break;
            case 2048: offset = 3; break;
            case 1024: offset = 6; break;
            case 512:  offset = 9; break;
            case 256:  offset = 12; break;
        }
        
        return offset;
    }
    private int filterSize=1024;
    
}
