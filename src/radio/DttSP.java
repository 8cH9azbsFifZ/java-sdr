package radio;

/**
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

public class DttSP extends ProcessThread {
 
    /** Creates a new instance of DttSP */
    public DttSP(String id,boolean startProcess) {
        super(id);
        this.startProcess=startProcess;
    }
    
    public void setFifoPath(String fifoPath) {
        this.fifoPath=fifoPath;
    }
 
    public void setCommandPath(String commandPath) {
        this.commandPath=commandPath;
    }
 
    public void setMeterPath(String meterPath) {
        this.meterPath=meterPath;
    }
 
    public void setSpectrumPath(String spectrumPath) {
        this.spectrumPath=spectrumPath;
    }

    public void setCommand(String command) {
        this.command=command;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate=sampleRate;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize=bufferSize;
    }
    
    public void setDevice(String device) {
        this.device=device;
    }
    
    public void setLocal(boolean local) {
        this.local=local;
    }

    public void setHost(String host) {
        this.host=host;
    }

    public void setPort(int port) {
        this.port=port;
    }

    public void setJack(boolean jack) {
        this.jack=jack;
    }
    
    // commands
    public void sendCommand(String command) {
        //if(command.startsWith("setOsc")) {
        //    System.err.println("DttSP.sendCommand: "+id+": "+command);
        //}
        if(commandFifo!=null) {
            commandFifo.write(command);
        }
    }
    
    public void setRxOscillator(double frequency) {
        if(commandFifo!=null) {
            commandFifo.write("setOsc"+" "+frequencyFormat.format(frequency)+" "+RX);
        }
    }
    
    public void setTxOscillator(double frequency) {
        if(commandFifo!=null) {
            commandFifo.write("setOsc"+" "+frequencyFormat.format(frequency)+" "+TX);
        }
    }
    
    

    public void run() {
        if(jack) {
            usesJack();
        } else {
            noJack();
        }
    }
    
    public void usesJack() {
        String execCommand=command+" -s -m "+"--client-name="+id+
                " --buffsize="+bufferSize+
                " --ringmult="+ringMult+
                " --command-path="+fifoPath+"/"+commandPath+
                " --meter-path="+fifoPath+"/"+meterPath+
                " --spectrum-path="+fifoPath+"/"+spectrumPath;
        String[] env=new String[2];
        env[0]="SDR_DEFRATE="+sampleRate;
        env[1]="HOME="+System.getProperty("user.home");
        try {
            if(startProcess) {
                System.err.println("DttSP:"+id+":exec="+execCommand);
                System.err.println("DttSP:"+id+":env[0]="+env[0]);
                System.err.println("DttSP:"+id+":env[1]="+env[1]);
           
                process=Runtime.getRuntime().exec(execCommand,env);
                ProcessOutputThread stdout=new ProcessOutputThread(id+".stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
                stdout.start();
                ProcessOutputThread stderr=new ProcessOutputThread(id+".stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
                stderr.start();
            }
            
            commandFifo=new CommandFifo(new java.io.File(fifoPath,commandPath));
            
            if(startProcess) {
                result=process.waitFor();
                System.err.println("DttSP:"+id+":result="+result);
            }
        } catch (Exception e) {
            System.err.println("DttSP:"+id+":"+e.getMessage());
            if(process!=null) {
                process.destroy();
            }
        }
    }

    public void noJack() {
        String execCommand=command+" -s -m "+
                " --buffsize="+bufferSize+
                " --ringmult="+ringMult+
                " --command-path="+fifoPath+"/"+commandPath+
                " --meter-path="+fifoPath+"/"+meterPath+
                " --spectrum-path="+fifoPath+"/"+spectrumPath;
        String[] env=new String[2];
        env[0]="SDR_DEFRATE="+sampleRate;
        env[1]="HOME="+System.getProperty("user.home");
        try {
            System.err.println("DttSP:"+id+":exec="+execCommand);
            System.err.println("DttSP:"+id+":env[0]="+env[0]);
            System.err.println("DttSP:"+id+":env[1]="+env[1]);
            process=Runtime.getRuntime().exec(execCommand,env);
            if(local) {
                iqSource=(IQSource)new LocalIQSource(sampleRate,bufferSize,process.getOutputStream(),device);
            } else {
                iqSource=(IQSource)new RemoteIQSource(sampleRate,bufferSize,process.getOutputStream(),host,port);
            }
            speakerTarget=new SpeakerTarget(sampleRate,bufferSize,process.getInputStream(),device);

            ProcessOutputThread stderr=new ProcessOutputThread(id+".stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
            stderr.start();
            iqSource.start();
            speakerTarget.start();

            //commandFifo=new CommandFifo(new java.io.File(fifoPath,commandPath));
            
            int result=process.waitFor();
            System.err.println("DttSP:"+id+":result="+result);
        } catch (Exception e) {
            System.err.println("DttSP:"+id+":"+e.getMessage());
            if(process!=null) {
                process.destroy();
            }
        }
    }
    
    private static final int RX=0;
    private static final int TX=1;
    
    private java.text.DecimalFormat frequencyFormat=new java.text.DecimalFormat("###0.000000",new java.text.DecimalFormatSymbols(java.util.Locale.ENGLISH));
    private String fifoPath="IPC";
    private String commandPath="SDRcommands";
    private String meterPath="SDRmeter";
    private String spectrumPath="SDRspectrum";
    private CommandFifo commandFifo;
    
    private String command="bin/sdr-core";

    private int bufferSize=1024;
    private int ringMult=4;
    private int sampleRate=96000;
    
    private boolean jack=true;
    private boolean startProcess=false;
    
    private String device;
    private IQSource iqSource;
    private SpeakerTarget speakerTarget;
    
    private boolean local=true;
    private String host="217.34.193.201";
    private int port=8811;

}
