/*
 * AGCListener.java
 *
 * Created on January 15, 2008, 12:08 PM
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
 *  AGCListener
 */
public interface AGCListener {
    
    public void agcSelected(AGCs agc);
    public void configureAGC();
    public void setAgcSlope(int slope);
    public void setAgcMaxGain(int gain);
    public void setAgcAttack(int attack);
    public void setAgcDecay(int decay);
    public void setAgcHang(int hang);
    public void setAgcFixedGain(int fixedGain);
    public void setAgcHangThreshold(int threshold);
    
}
