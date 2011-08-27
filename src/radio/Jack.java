/*
 * Jack.java
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

public class Jack extends ProcessThread {

    /** Creates a new instance of Jack */
    public Jack(String id,boolean startProcess) {
        super(id);
        this.startProcess=startProcess;
    }

    public void setCommand(String command) {
        this.command=command;
    }

    public void setDriver(String driver) {
        this.driver=driver;
    }
    
    public void setRealtime(boolean state) {
        this.realtime=state;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate=sampleRate;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize=bufferSize;
    }

    public void setOptions(String[] options) {
        this.options=options;
    }

    public void run() {

        int rt=(realtime?1:0);
        String[] execCommand=new String[4+rt+options.length];
        execCommand[0]=command;
        if(realtime) {
            execCommand[1]="-R";
        }
        execCommand[1+rt]="-d"+driver;
        execCommand[2+rt]="-r"+sampleRate;
        execCommand[3+rt]="-p"+bufferSize;
        for(int i=0;i<options.length;i++) {
           execCommand[4+rt+i]=options[i];
        }
        
        try {        
            if(startProcess) {
                System.err.print("Jack:");
                for(int i=0;i<execCommand.length;i++) {
                    System.err.print(" "+execCommand[i]);
                }
                System.err.println();

                process=Runtime.getRuntime().exec(execCommand);
                ProcessOutputThread stdout=new ProcessOutputThread("jack.stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
                stdout.start();
                ProcessOutputThread stderr=new ProcessOutputThread("jack.stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
                stderr.start();
                result=process.waitFor();
                System.err.println("Jack: result="+result);
                }
        } catch (Exception e) {
            System.err.println("Jack: "+e.getMessage());
            if(process!=null) {
                process.destroy();
            }
        }
    }
    
    private String command="bin/jackd";
    private String driver="coreaudio";
    private boolean realtime;
    private String[] options={"-i4","-o4","-n2"};
    private int sampleRate=96000;
    private int bufferSize=1024;
    
    private boolean startProcess=false;
    
}
