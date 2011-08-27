/*
 * DSBFilters.java
 *
 * Created on August 29, 2007, 3:09 PM
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

public class DSBFilters extends FilterValues {
    
    /** Creates a new instance of DSBFilters */
    public DSBFilters() {
        super();
        filterValues[0]=new FilterValue("16k",-8000,8000);
        filterValues[1]=new FilterValue("12k",-6000,6000);
        filterValues[2]=new FilterValue("10k",-5000,5000);
        filterValues[3]=new FilterValue("8.0k",-4000,4000);
        filterValues[4]=new FilterValue("6.6k",-3300,3300);
        filterValues[5]=new FilterValue("5.2k",-2600,2600);
        filterValues[6]=new FilterValue("4.0k",-2000,2000);
        filterValues[7]=new FilterValue("3.1k",-1550,1550);
        filterValues[8]=new FilterValue("2.9k",-1450,1450);
        filterValues[9]=new FilterValue("2.4k",-1200,1200);
        filterValues[10]=new FilterValue("Var1",-3300,3300);
        filterValues[11]=new FilterValue("Var2",-3300,3300);
    }
    
    public Filters getDefaultFilter() {
        return Filters.FILTER_9;
    }
}
