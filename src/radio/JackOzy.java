/*
 * JackOzy.java
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


public class JackOzy extends ProcessThread {
    
public JackOzy(String id){
        super(id);
  }

public void setSampleRate(int sampleRate){
    this.sampleRate=sampleRate;
}

public void setBufferSize(int bufferSize){
    this.bufferSize=bufferSize;
}

public void run(){
    
    String[] execCommand=new String[3];
    execCommand[0]=connectOzy;
    execCommand[1]=Integer.toString(sampleRate);
    execCommand[2]=Integer.toString(bufferSize);
 
try{      
    System.err.print ("connectOzy."+id+":exec");
    for (int i=0;i<execCommand.length;i++){
        System.err.print(" "+execCommand[i]);
    }
    System.err.println();
    process=Runtime.getRuntime().exec(execCommand);
    ProcessOutputThread stdout=new ProcessOutputThread("connectOzy.stdout",new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream())));
    stdout.start();
    ProcessOutputThread stderr=new ProcessOutputThread("connectOzy.stderr",new java.io.BufferedReader(new java.io.InputStreamReader(process.getErrorStream())));
    stderr.start();
    result=process.waitFor();
    System.err.println("connectOzy:"+id+":result="+result);
 }catch(Exception e){
        
    }
 }


private String connectOzy ="bin/ozy/ozyjack";

private int sampleRate;
private int bufferSize;
}
