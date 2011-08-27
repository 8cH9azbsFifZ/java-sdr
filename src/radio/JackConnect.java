/*
 * JackConnect.java
 *
 * Created on September 9, 2007, 5:16 PM
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

public class JackConnect extends ProcessThread {

    /** Creates a new instance of JackConnect */
    public JackConnect(String id,Radio radio,boolean startProcess) {
        super(id);
        this.radio=radio;
        this.startProcess=startProcess;
    }

    public void setCommand(String command) {
        this.command=command;
    }

    public void setSource(String source) {
        this.source=source;
    }

    public void setDestination(String destination) {
        this.destination=destination;
    }

    public void run() {
        String[] execCommand=new String[3];
        execCommand[0]=command;
        execCommand[1]=source;
        execCommand[2]=destination;
        try {
            if(startProcess) {
                System.err.println("JackConnect: "+execCommand[0]+" "+execCommand[1]+" "+execCommand[2]);
                process=Runtime.getRuntime().exec(execCommand);
                ProcessOutputThread stdout=new ProcessOutputThread("jack_connect.stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
                stdout.start();
                ProcessOutputThread stderr=new ProcessOutputThread("jack_connect.stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
                stderr.start();
                result=process.waitFor();
                System.err.println("JackConnect: result="+result);
            }
            radio.jackCompleted(result);
        } catch (Exception e) {
            System.err.println("JackConnect: "+e.toString());
            if(process!=null) {
                process.destroy();
            }
            radio.jackCompleted(-1);
        }
    }

    private Radio radio;
    private String command="bin/jack_connect";
    private String source;
    private String destination;
    
    private boolean startProcess=false;

}

