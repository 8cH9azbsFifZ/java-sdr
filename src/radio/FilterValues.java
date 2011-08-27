/*
 * FilterValues.java
 *
 * Created on August 29, 2007, 3:01 PM
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

public class FilterValues implements java.io.Serializable {
    
    /** Creates a new instance of FilterValues */
    public FilterValues() {
        filterValues=new FilterValue[12];
    }
    
    public String[] getTitles() {
        String titles[]=new String[12];
        for(int i=0;i<12;i++)
            titles[i]=filterValues[i].getTitle();
        return titles;
    }
    
    public Filters getDefaultFilter() {
        return Filters.FILTER_1;
    }
    
    public int getLow(Filters filter) {
        int low=0;
        switch(filter) {
            case FILTER_1:
                low=filterValues[0].getLow();
                break;
            case FILTER_2:
                low=filterValues[1].getLow();
                break;
            case FILTER_3:
                low=filterValues[2].getLow();
                break;
            case FILTER_4:
                low=filterValues[3].getLow();
                break;
            case FILTER_5:
                low=filterValues[4].getLow();
                break;
            case FILTER_6:
                low=filterValues[5].getLow();
                break;
            case FILTER_7:
                low=filterValues[6].getLow();
                break;
            case FILTER_8:
                low=filterValues[7].getLow();
                break;
            case FILTER_9:
                low=filterValues[8].getLow();
                break;
            case FILTER_10:
                low=filterValues[9].getLow();
                break;
            case FILTER_11:
                low=filterValues[10].getLow();
                break;
            case FILTER_12:
                low=filterValues[11].getLow();
                break;
        }
        return low;
    }
    
        public int getHigh(Filters filter) {
        int high=0;
        switch(filter) {
            case FILTER_1:
                high=filterValues[0].getHigh();
                break;
            case FILTER_2:
                high=filterValues[1].getHigh();
                break;
            case FILTER_3:
                high=filterValues[2].getHigh();
                break;
            case FILTER_4:
                high=filterValues[3].getHigh();
                break;
            case FILTER_5:
                high=filterValues[4].getHigh();
                break;
            case FILTER_6:
                high=filterValues[5].getHigh();
                break;
            case FILTER_7:
                high=filterValues[6].getHigh();
                break;
            case FILTER_8:
                high=filterValues[7].getHigh();
                break;
            case FILTER_9:
                high=filterValues[8].getHigh();
                break;
            case FILTER_10:
                high=filterValues[9].getHigh();
                break;
            case FILTER_11:
                high=filterValues[10].getHigh();
                break;
            case FILTER_12:
                high=filterValues[11].getHigh();
                break;
        }
        return high;
    }
    
    public void setVarValues(Filters filter,int low,int high) {
        if(filter==Filters.FILTER_11) {
            filterValues[10].setValues(low,high);
        } else if(filter==Filters.FILTER_12) {
            filterValues[11].setValues(low,high);
        }
    }
    
    public FilterValue[] getFilterValues() {
        return filterValues;
    }
    
    public void setFilterValues(FilterValue[] filterValues) {
        this.filterValues=filterValues;
    }
    
    FilterValue[] filterValues;
    
}
