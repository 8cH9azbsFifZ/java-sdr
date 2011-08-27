/*
 * RemoteIQSource.java
 *
 * Created on November 14, 2007, 9:01 PM
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
 *  RemoteIQSource
 */
import java.io.OutputStream;
import java.net.Socket;

public class RemoteIQSource extends IQSource {

    public RemoteIQSource(int sampleRate,int bufferSize,OutputStream dttspStdIn,String host,int port) {
System.err.println("RemoteIQSource: "+sampleRate+","+bufferSize+","+host+":"+port);
        this.sampleRate=sampleRate;
        this.bufferSize=bufferSize;
        this.dttspStdIn=dttspStdIn;
        this.host=host;
        this.port=port;
    }

    public void run() {
System.err.println("RemoteIQSource.run");
        Socket socket;
        try {
            socket=new Socket(host,port);
            byte[] buffer=new byte[bufferSize*BYTES_PER_SAMPLE*CHANNELS];
            int bytes;
            while(true) {
                bytes=0;
                while(bytes<buffer.length) {
                    bytes+=socket.getInputStream().read(buffer,bytes,buffer.length-bytes);
                }
//System.err.println("RemoteIQSource: "+(++count)+" got "+bytes+" bytes");
                dttspStdIn.write(buffer);
                dttspStdIn.flush();
            }
        } catch (Exception e) {
            System.err.println("DttSP: "+e.toString());
        }
System.err.println("RemoteIQSource.exit");
    }

    private int sampleRate;
    private int bufferSize;
    private OutputStream dttspStdIn;
    private String host;
    private int port;

    private int count=0;
}

