/*
 * LocalIQSource.java
 *
 * Created on November 14, 2007, 9:00 PM
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
 *  LocalIQSource
 */

import java.io.OutputStream;
import java.net.Socket;

public class LocalIQSource extends IQSource {

    public LocalIQSource(int sampleRate,int bufferSize,OutputStream dttspStdIn,String device) {
        this.sampleRate=sampleRate;
        this.bufferSize=bufferSize;
        this.dttspStdIn=dttspStdIn;
        this.device=device;
        audioFormat = new javax.sound.sampled.AudioFormat(
            (float)sampleRate,
            SAMPLE_SIZE,
            CHANNELS,
            SIGNED,
            LITTLE_ENDIAN);
        javax.sound.sampled.DataLine.Info audioInfo=
            new javax.sound.sampled.DataLine.Info(javax.sound.sampled.TargetDataLine.class,audioFormat);

        javax.sound.sampled.Mixer.Info[] mixerInfos = javax.sound.sampled.AudioSystem.getMixerInfo();
        javax.sound.sampled.Mixer mixer=null;
        for (int i = 0; i < mixerInfos.length; i++) {
            try {
                mixer = javax.sound.sampled.AudioSystem.getMixer(mixerInfos[i]);
System.err.println("LocalIQSource: mixer: "+mixer.toString());
                //if(mixerInfos[i].getName().startsWith(device)) {
                    javax.sound.sampled.Line.Info[] targets=mixer.getTargetLineInfo();
                    lineIn = (javax.sound.sampled.TargetDataLine)mixer.getLine(audioInfo);
                    if(lineIn!=null) {
                        break;
                    }
                //}
            } catch (Exception e) {
                System.out.println("LocalIQSource.setupAudio: Exception: "+e.toString());
            }
        }
        if(lineIn==null) {
            System.err.println("cound not find a TargetDataLine");
        }

    }

    public void run() {
            if(lineIn!=null) {
                byte buffer[]=new byte[bufferSize*BYTES_PER_SAMPLE*CHANNELS];
                int bytes;
                try {
                    lineIn.open(audioFormat,bufferSize*CHANNELS*BYTES_PER_SAMPLE);
                    lineIn.start();
                    lineIn.flush();
                    while(!interrupted()) {
                        bytes=0;
                        while(bytes!=buffer.length) {
                            bytes+=lineIn.read(buffer,bytes,buffer.length-bytes);
                        }
                        dttspStdIn.write(buffer);
                    }
                } catch (Exception e) {

                }
                lineIn.stop();
                lineIn.close();
            }
    }

    private int sampleRate;
    private int bufferSize;
    private OutputStream dttspStdIn;
    private String device;

    private javax.sound.sampled.AudioFormat audioFormat;
    private javax.sound.sampled.TargetDataLine lineIn;

}

