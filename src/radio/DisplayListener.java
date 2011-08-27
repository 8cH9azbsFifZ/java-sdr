/*
 * DisplayListener.java
 *
 * Created on September 18, 2007, 3:12 PM
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

public interface DisplayListener {
    public void displaySelected(Displays display);
    public void dragFrequency(double increment);
    public void incrementFrequency(int increment);
    public void setFrequency(double frequency);
    public void configureDisplay();
    public void setSpectrumLow(int low);
    public void setSpectrumHigh(int high);
    public void setWaterfallLow(int low);
    public void setWaterfallHigh(int high);
}
