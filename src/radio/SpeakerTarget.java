/*
 * SpeakerTarget.java
 *
 * Created on November 14, 2007, 9:03 PM
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
 *  SpeakerTarget
 */
import java.io.InputStream;

public class SpeakerTarget extends Thread {

    public SpeakerTarget(int sampleRate,int bufferSize,InputStream dttspStdOut,String device) {
        System.err.println("SpeakerTarget "+sampleRate+","+bufferSize+","+device);
        this.sampleRate=sampleRate;
        this.bufferSize=bufferSize;
        this.dttspStdOut=dttspStdOut;
        this.device=device;
        audioFormat = new javax.sound.sampled.AudioFormat(
            (float)sampleRate,
            IQSource.SAMPLE_SIZE,
            IQSource.CHANNELS,
            IQSource.SIGNED,
            IQSource.LITTLE_ENDIAN);
        javax.sound.sampled.DataLine.Info audioInfo=
            new javax.sound.sampled.DataLine.Info(javax.sound.sampled.SourceDataLine.class,audioFormat);
        javax.sound.sampled.Mixer.Info[] mixerInfos = javax.sound.sampled.AudioSystem.getMixerInfo();
        javax.sound.sampled.Mixer mixer=null;
        for (int i = 0; i < mixerInfos.length; i++) {
            try {
                mixer = javax.sound.sampled.AudioSystem.getMixer(mixerInfos[i]);
                //if(mixerInfos[i].getName().startsWith(device)) {
                    speaker = (javax.sound.sampled.SourceDataLine)mixer.getLine(audioInfo);
                    if(speaker!=null) {
                        break;
                    }
                //}
            } catch (Exception e) {
                System.out.println("SpeakerTarget.setupAudio: Exception: "+e.toString());
            }
        }

        if(speaker==null) {
            System.err.println("cound not find a SourceDataLine");
        }

    }

    public void run() {
        System.err.println("SpeakerTarget.run");
        if(speaker!=null) {
            byte buffer[]=new byte[bufferSize*IQSource.BYTES_PER_SAMPLE*IQSource.CHANNELS];
            int bytes;
            try {
                speaker.open(audioFormat,bufferSize*IQSource.CHANNELS*IQSource.BYTES_PER_SAMPLE);
                speaker.start();
                while(!interrupted()) {
                    bytes=0;
                    while(bytes!=buffer.length) {
                        bytes+=dttspStdOut.read(buffer,bytes,buffer.length-bytes);
                    }
//System.err.println("SpeakerTarget: read "+bytes+" bytes");
                    speaker.write(buffer,0,buffer.length);
                }
//System.err.println("SpeakerTarget terminated");
            } catch (Exception e) {
                System.err.println("SpeakerTarget: "+e.toString());
            }
            speaker.stop();
            speaker.close();
        }
    }

    private int sampleRate;
    private int bufferSize;
    private InputStream dttspStdOut;
    private String device;

    private javax.sound.sampled.AudioFormat audioFormat;
    private javax.sound.sampled.SourceDataLine speaker;

}

