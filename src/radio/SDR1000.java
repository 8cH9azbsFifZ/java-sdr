/*
 * SDR1000.java
 *
 * Created on September 29, 2007, 5:30 PM
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

public class SDR1000 extends ProcessThread {
    
    /** Creates a new instance of SDR1000 */
    public SDR1000(String id,boolean startProcess) {
        super(id);    
        this.startProcess=startProcess;
    }
    
    public void setFifoPath(String fifoPath) {
        this.fifoPath=fifoPath;
    }
    
    public void setCommandPath(String commandPath) {
        this.commandPath=commandPath;
    }
    
    public void setStatusPath(String statusPath) {
        this.statusPath=statusPath;
    }
    
    public void setCommand(String command) {
        this.command=command;
    }
    
    public void setUsb(boolean usb) {
        this.usb=usb;
    }
    
    public void setPort(int port) {
        this.port=port;
    }
    
    public void setPa(boolean pa) {
        this.pa=pa;
    }
    
    public void setRfe(boolean rfe) {
        this.rfe=rfe;
    }
    
    public void sendCommand(String command) {
        //System.err.println("sdr1000.sendCommand: '"+command+"'");
        commandFifo.write(command);
    }
    
    public void run() {
        String[] execCommand=new String[8];
        execCommand[0]=command;
        execCommand[1]="-u"+(usb?"1":"0");
        execCommand[2]="-r"+(rfe?"1":"0");
        execCommand[3]="-a"+(pa?"1":"0");
        execCommand[4]="-p0x"+Integer.toHexString(port);
        execCommand[5]="-f"+fifoPath;
        execCommand[6]="-c"+commandPath;
        execCommand[7]="-s"+statusPath;
        
        try {
            if(startProcess) {
                System.err.print("SDR1000."+id+":exec:");
                for(int i=0;i<execCommand.length;i++) {
                    System.err.print(" "+execCommand[i]);
                }
                System.err.println();
            
                process=Runtime.getRuntime().exec(execCommand);
                ProcessOutputThread stdout=new ProcessOutputThread("sdr1000.stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
                stdout.start();
                ProcessOutputThread stderr=new ProcessOutputThread("sdr1000.stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
                stderr.start();
            }
            
            commandFifo=new CommandFifo(new java.io.File(fifoPath,commandPath));
            
            if(startProcess) {
                int result=process.waitFor();
            }
            
            System.err.println("SDR1000:"+id+":result="+result);
        } catch (Exception e) {
            System.err.println("SDR1000:"+id+":"+e.getMessage());
            if(process!=null) {
                process.destroy();
            }
        }
    }
    
    private String fifoPath="fifos";
    private String commandPath="HWcommands";
    private String statusPath="HWstatus";
    private CommandFifo commandFifo;
   
    private boolean pa;
    private boolean rfe;
    private boolean usb;
    private int port;

    private String command="bin/linux/sdr1000";
    
    private boolean startProcess=false;
    
}
