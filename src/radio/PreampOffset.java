package radio;
/*
 * PreampOffset.java
 *
 * Created on October 5, 2007, 9:55 AM
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

public class PreampOffset {
    
    /**
     * Creates a new instance of PreampOffset
     */
    public PreampOffset() {

        preampOffset = new float[4];
        preampOffset[Preamps.OFF.ordinal()] = +10.0f;
        preampOffset[Preamps.LOW.ordinal()] = 0.0f;
        preampOffset[Preamps.MEDIUM.ordinal()] = -16.0f;
        preampOffset[Preamps.HIGH.ordinal()] = -26.0f;

    }
    
    public float getPreampOffset(Preamps preamp) {
        return preampOffset[preamp.ordinal()];
    }
    
    float[] preampOffset;
}
