/*
 * CommandFifo.java
 *
 * Created on August 30, 2007, 1:22 PM
 *
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

public class CommandFifo {
    
    /** Creates a new instance of CommandFifo */
    public CommandFifo(java.io.File path) {
        System.err.println("CommandFifo: "+path.getAbsolutePath());
        this.path=path;
        try {
            fifo=new java.io.FileOutputStream(path,true);
        } catch(Exception e) {
            System.err.println("CommandFifo: "+e.getMessage());
        }
    }
    
    public synchronized void write(String command) {
        try {
            if(fifo!=null) {
                
                fifo.write(command.getBytes());
                fifo.write(newline);
                fifo.flush();
            }
        } catch (Exception e) {
            System.err.println("CommandFifo.write: "+path.getAbsolutePath()+": "+e.getMessage());
        }
    }
    
    private java.io.File path;
    private java.io.FileOutputStream fifo;
    private byte[] newline="\n".getBytes();
    
}
