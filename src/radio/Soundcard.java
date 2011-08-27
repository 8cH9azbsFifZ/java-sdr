/*
 * Soundcard.java
 *
 * Created on August 30, 2007, 1:52 PM
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

public class Soundcard implements java.io.Serializable {
    
    /** Creates a new instance of SoundCard */
    public Soundcard() {    
    }
    
    public Soundcard(Soundcards soundcard) {
        this.soundcard=soundcard;
    }
    
    public void setSoundcard(Soundcards soundcard) {
        this.soundcard=soundcard;
    }
    
    public Soundcards getSoundcard() {
        return soundcard;
    } 
    
    public double getMeterCalibrationOffset() {
        double value=-52.43533;
        switch(soundcard) {
            case UNSUPPORTED:
                value = -52.43533;
                break;
            case OZY_JANUS:
                value = -26.39952;
                break;
            case AUDIGY_2_ZS:
                value = 1.024933;
                break;
            case MP3_PLUS:
                value = -33.40224;
                break;
            case EXTIGY:
                value = -29.30501;
                break;
            case DELTA_44:
                value = -25.13887;
                break;
            case FIREBOX:
                value = -27.94611;
                break;
            case EDIROL_FA_66:
                value = -46.82864;
                break;
        }    
        return value;
    }
    
    public double getDisplayCalibrationOffset() {
        double value=-82.62103;
        switch(soundcard) {
            case UNSUPPORTED:
                value = -82.62103;
                break;
            case OZY_JANUS:
                value = -56.56675;
                break;
            case AUDIGY_2_ZS:
                value = -29.20928;
                break;
            case MP3_PLUS:
                value = -62.84578;
                break;
            case EXTIGY:
                value = -62.099;
                break;
            case DELTA_44:
                value = -57.467;
                break;
            case FIREBOX:
                value = -80.019;
                break;
            case EDIROL_FA_66:
                value = -80.429;
                break;
        }    
        return value;
    }
    
    private Soundcards soundcard=Soundcards.EDIROL_FA_66;
    
    
}
