/*
 * CWLFilters.java
 *
 * Created on August 29, 2007, 3:12 PM
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

public class CWLFilters extends FilterValues implements java.io.Serializable {
    
    /** Creates a new instance of CWLFilters */
    public CWLFilters() {
        super();
        filterValues[0]=new FilterValue("1k",-1100,-100);
        filterValues[1]=new FilterValue("800",-1000,-200);
        filterValues[2]=new FilterValue("750",-975,-225);
        filterValues[3]=new FilterValue("600",-900,-300);
        filterValues[4]=new FilterValue("500",-850,-350);
        filterValues[5]=new FilterValue("400",-800,-400);
        filterValues[6]=new FilterValue("250",-725,-475);
        filterValues[7]=new FilterValue("100",-650,-550);
        filterValues[8]=new FilterValue("50",-625,-575);
        filterValues[9]=new FilterValue("25",-613,-587);
        filterValues[10]=new FilterValue("Var1",-850,-350);
        filterValues[11]=new FilterValue("Var2",-850,-350);
    }
    
    
    public Filters getDefaultFilter() {
        return Filters.FILTER_5;
    }
}
