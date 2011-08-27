/*
 * InitOzy.java
 *
 * Created on March 12, 2008, 5:16 PM
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
 * roland.etienne@free.fr
 *
 * roland f8chk
 */


package radio;

public class InitOzy extends ProcessThread {
    
    /** Creates a new instance of InitOzy */
    public InitOzy(String id, Radio radio) {
        super(id);
        this.radio=radio;
    }
    
    public void run() {
  
      loadFw(vid,pid,fwFile);       // load initial firmware
      loadFpga(vid,pid,fpgaFile);   // load fpga software
      confJanus(vid,pid,confJanus1);// configure Janus
      confJanus(vid,pid,confJanus2);
      confJanus(vid,pid,confJanus3);
      confJanus(vid,pid,confJanus4);
      confJanus(vid,pid,confJanus5);
      confJanus(vid,pid,confJanus6);
      confJanus(vid,pid,confJanus7);
      radio.initOzyCompleted(result);
  
   }
   
    public void loadFw(String vid, String pid, String file){    
     String[] execCommand=new String[4];
        execCommand[0]=commandFw;
        execCommand[1]=vid;
        execCommand[2]=pid;
        execCommand[3]=file;
                
        try {
            System.err.print("loadFw."+id+":exec:");
            for(int i=0;i<execCommand.length;i++) {
                System.err.print(" "+execCommand[i]);
            }
            System.err.println();
            process=Runtime.getRuntime().exec(execCommand);
            ProcessOutputThread stdout=new ProcessOutputThread("loadFw.stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
            stdout.start();
            ProcessOutputThread stderr=new ProcessOutputThread("loadFw.stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
            stderr.start();
            result=process.waitFor();
            System.err.println("loadFw"+id+":result="+result);
        } catch (Exception e) {
            System.err.println("loadFw:"+id+":"+e.getMessage());
            if(process!=null) {
                process.destroy();
            }
        }
        System.err.println("Sleeping ...");
               try {
                    Thread.sleep(3000);
                } catch(Exception e) {
                }
    }
    
    public void loadFpga(String vid, String pid, String file){
      String[] execCommand=new String[4];
        execCommand[0]=commandFpga;
        execCommand[1]=vid;
        execCommand[2]=pid;
        execCommand[3]=file;
                
        try {
            System.err.print("loadFpga."+id+":exec:");
            for(int i=0;i<execCommand.length;i++) {
                System.err.print(" "+execCommand[i]);
            }
            System.err.println();
            process=Runtime.getRuntime().exec(execCommand);
            ProcessOutputThread stdout=new ProcessOutputThread("loadFpga.stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
            stdout.start();
            ProcessOutputThread stderr=new ProcessOutputThread("loadFpga.stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
            stderr.start();
            result=process.waitFor();
            System.err.println("loadFpga"+id+":result="+result);
        } catch (Exception e) {
            System.err.println("loadFpga:"+id+":"+e.getMessage());
            if(process!=null) {
                process.destroy();
            }
        }
            System.err.println("Sleeping ...");
               try {
                    Thread.sleep(3000);
                } catch(Exception e) {
                }
    }
    
    public void confJanus(String vid, String pid, String[] confJanus){
      String[] execCommand=new String[8];
        execCommand[0]=commandJanus;
        execCommand[1]=vid;
        execCommand[2]=pid;
        execCommand[3]=confJanus[0];
        execCommand[4]=confJanus[1];
        execCommand[5]=confJanus[2];
        execCommand[6]=confJanus[3];
        execCommand[7]=confJanus[4];
        
        try {
            System.err.print("confJanus."+id+":exec:");
            for(int i=0;i<execCommand.length;i++) {
                System.err.print(" "+execCommand[i]);
            }
            System.err.println();
            process=Runtime.getRuntime().exec(execCommand);
            ProcessOutputThread stdout=new ProcessOutputThread("confJanus.stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
            stdout.start();
            ProcessOutputThread stderr=new ProcessOutputThread("confJanus.stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
            stderr.start();
            result=process.waitFor();
            System.err.println("confJanus"+id+":result="+result);
        } catch (Exception e) {
            System.err.println("confJanus:"+id+":"+e.getMessage());
            if(process!=null) {
                process.destroy();
            }
          
        }  
    }
    private Radio radio;
       
    private String vid="0xfffe";
    private String pid="0x7";
    
    private String fwFile="bin/ozy/ozyfw-sdr1k.hex";
    private String fpgaFile="bin/ozy/Ozy_Janus.rbf";
    
    private String[] confJanus1={"0x1a","0x1e","0x00","0x00","2"};
    private String[] confJanus2={"0x1a","0x12","0x01","0x00","2"};
    private String[] confJanus3={"0x1a","0x08","0x15","0x00","2"};
    private String[] confJanus4={"0x1a","0x0c","0x00","0x00","2"};
    private String[] confJanus5={"0x1a","0x0e","0x02","0x00","2"};
    private String[] confJanus6={"0x1a","0x10","0x00","0x00","2"};
    private String[] confJanus7={"0x1a","0x0a","0x00","0x00","2"};
          
    private String commandFw="bin/ozy/loadFW";
    private String commandFpga="bin/ozy/loadFPGA";
    private String commandJanus="bin/ozy/write_i2c";
}
