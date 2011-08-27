/*
 * PowerUpdateThread.java
 *
 * Created on September 28, 2007, 11:28 AM
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
 * @author jm57878
 */
public class PowerUpdateThread extends Thread {
    
    private boolean terminate=false;
    private int powerFrequency;
    private SDR1000 sdr1000;
    private PowerInterface power;
    private Power powerThread;
    private String powerUpdateCommand;
    private int id;
    
    PowerUpdateThread(int powerFrequency,SDR1000 sdr1000,PowerInterface power,Power powerThread) {
        System.err.println("PowerUpdateThread");
        this.powerFrequency=powerFrequency;
        this.sdr1000=sdr1000;
        this.power=power;
        this.powerThread=powerThread;
    }

    public void run() {
        System.err.println("PowerUpdateThread.run");
        int forward;
        int reverse;
        while(!terminate) {
            try {
                this.sleep(1000/powerFrequency);
                /*
                if(!terminate) {
                    sdr1000.sendCommand("PA_ReadADC 0"); // FWD
                    forward=powerThread.getPower();
                    sdr1000.sendCommand("PA_ReadADC 1"); // REV
                    reverse=powerThread.getPower();
                    power.updatePower(forward, reverse);
                }
                */
            } catch(Exception e) {
            }
        }
        System.err.println("PowerUpdateThread.run: terminated");
    }

    public void terminate() {
        this.terminate=true;
    }
}
