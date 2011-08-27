/*
 * State.java
 *
 * Created on September 20, 2007, 5:51 PM
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

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class State implements java.io.Serializable {
    
    /** Creates a new instance of State */
    public State() {
    }
    
    private void setDefaults() {
        System.err.println("State.setDefaults: Setting Default Values");
        
        defaults=true;
        radio=Radios.SDR1000;
        softrockCenterFrequency=7.056000;
        rfe=true;
        pa=true;
        usb=true;
        port=0x378;
        transmitEnabled=false;
        jack=true;
        jackStartProcess=true;
        jackConnectStartProcess=true;
        sdr1000StartProcess=true;
        dttspStartProcess=true;
        soundcard=new Soundcard();
        soundcard.setSoundcard(Soundcards.UNSUPPORTED);
        sampleRate=96000;
        bufferSize=1024;
        lsbFilters=new LSBFilters();
        usbFilters=new USBFilters();
        dsbFilters=new DSBFilters();
        amFilters=new AMFilters();
        fmnFilters=new FMNFilters();
        cwlFilters=new CWLFilters();
        cwuFilters=new CWUFilters();
        diguFilters=new DIGUFilters();
        diglFilters=new DIGLFilters();
        specFilters=new SPECFilters();
        samFilters=new SAMFilters();
        drmFilters=new DRMFilters();
        ;offset=-0.011025;
        offset=-0.009000;
        step=0.000100;
        bandstacks=new Bandstacks();
        bandstacks.init();
        band=Bands.BAND_4;
        bandstackEntry=bandstacks.getBandstackEntry(band);
        mode=bandstackEntry.getMode();
        filter=bandstackEntry.getFilter();
        aFrequency=bandstackEntry.getAFrequency();
        setDisplay(Displays.PANFALL);
        rxMeter=RxMeters.SIGNAL_STRENGTH;
        txMeter=TxMeters.MIC;
        
        waterfallLowThreshold=-130.0f;
        waterfallHighThreshold=-80.0f;
        
        spectrumHigh=-50;
        spectrumLow=-150;
        
        spectrumFrequency=15;
        meterFrequency=15;
        
        preamp=Preamps.HIGH;
        agc=AGCs.MEDIUM;
        agcSlope=0;
        agcMaxGain=60;
        agcAttack=2;
        agcDecay=2000;
        agcHang=750;
        agcFixedGain=20;
        agcHangThreshold=0;
        rxAfGain=1;
        rxIfGain=1;
        rxPan=0.5F;
        
        cwPitch=600;
        
        // make an educated guess to OS specific values
        String os=System.getProperty("os.name");
        if(os.equalsIgnoreCase("mac OS X")) {
            sdr1000Command="bin/mac/sdr1000";
            jackCommand="/usr/local/bin/jackd";
            jackDriver="coreaudio";
            jackDevice="coreaudio";
            jackRealtime=true;
            jackOptions=new String[3];
            jackOptions[0]="-o2";
            jackOptions[1]="-i2";
            jackOptions[2]="n2";
            jackConnectCommand="/usr/local/bin/jack_connect";
            
            dttspCommand="bin/mac/sdr-core";
            
            transmitEnabled=false;
            
            rxInputLeftSource="sdr1000:out1";
            rxInputRightSource="sdr1000:out2";
            rxOutputLeftDestination="sdr1000:in1";
            rxOutputRightDestination="sdr1000:in2";
            txInputLeftSource="";
            txInputRightSource="";
            txOutputLeftDestination="";
            txOutputRightDestination="";
        } else if(os.equalsIgnoreCase("linux")) {
            sdr1000Command="bin/linux/sdr1000";
            jackCommand="/usr/bin/jackd";
            jackDriver="alsa";
            jackDevice="alsa_pcm";
            jackRealtime=false;
            jackOptions=new String[1];
            jackOptions[0]="-dhw:0";
            jackConnectCommand="/usr/bin/jack_connect";
            dttspCommand="bin/linux/sdr-core";

            rxInputLeftSource="capture_1";
            rxInputRightSource="capture_2";
            rxOutputLeftDestination="playback_1";
            rxOutputRightDestination="playback_2";
            txInputLeftSource="";
            txInputRightSource="";
            txOutputLeftDestination="";
            txOutputRightDestination="";

        }
        dttspFifoPath="fifos";
        dttspRxId="sdr-rx";
        dttspRxCommandFifo="RXcommands";
        dttspRxSpectrumFifo="RXspectrum";
        dttspRxMeterFifo="RXmeter";
        dttspTxId="sdr-tx";
        dttspTxCommandFifo="TXcommands";
        dttspTxSpectrumFifo="TXspectrum";
        dttspTxMeterFifo="TXmeter";
        
        sdr1000FifoPath="fifos";
        sdr1000HwCommandFifo="HWcommands";
        sdr1000HwStatusFifo="HWstatus";
    }
    
    public boolean isDefaults() {
        return defaults;
    }
    
    public Radios getRadio() {
        return radio;
    }
    
    public void setRadio(Radios radio) {
        this.radio=radio;
    }
    
    public double getSoftrockCenterFrequency() {
        return softrockCenterFrequency;
    }
    
    public void setSoftrockCenterFrequency(double softrockCenterFrequency) {
        this.softrockCenterFrequency=softrockCenterFrequency;
    }
    
    public boolean isRfe(){
        return rfe;
    }
    
    public void setRfe(boolean rfe) {
        this.rfe=rfe;
    }
    
    public boolean isPa() {
        return pa;
    }

    public String getSdr1000FifoPath() {
        return sdr1000FifoPath;
    }
    
    public void setSdr1000FifoPath(String path) {
        this.sdr1000FifoPath=path;
    }
    
    public String getSdr1000HwCommandFifo() {
        return sdr1000HwCommandFifo;
    }
    
    public void setSdr1000HwCommandFifo(String fifo) {
        this.sdr1000HwCommandFifo=fifo;
    }
    
    public String getSdr1000HwStatusFifo() {
        return sdr1000HwStatusFifo;
    }
    
    public void setSdr1000HwStatusFifo(String fifo) {
        this.sdr1000HwStatusFifo=fifo;
    }
    
    public void setPa(boolean pa) {
        this.pa=pa;
    }
    
    public boolean isUsb() {
        return usb;
    }
    
    public void setUsb(boolean state) {
        usb=state;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port=port;
    }
    
    public boolean isTransmitEnabled() {
        return transmitEnabled;
    }
    
    public void setTransmitEnabled(boolean state) {
        transmitEnabled=state;
    }
    
    public Soundcard getSoundcard() {
        return soundcard;
    }
    
    public void setSoundcard(Soundcard soundcard) {
        this.soundcard=soundcard;
    }
    
    public void setSampleRate(int sampleRate) {
        this.sampleRate=sampleRate;
    }
    
    public int getSampleRate() {
        return sampleRate;
    }
    
    public void setBufferSize(int bufferSize) {
        this.bufferSize=bufferSize;
    }
    
    public int getBufferSize() {
        return bufferSize;
    }
    
    public double getStep() {
        return step;
    }
    
    public void setStep(double step) {
        this.step=step;
    }
    
    public Bands getBand() {
        return band;
    }
    
    public void setBand(Bands band) {
        this.band=band;
    }
    
    public Modes getMode() {
        return mode;
    }
    
    public void setMode(Modes mode) {
        this.mode=mode;
    }
    
    public Filters getFilter() {
        return filter;
    }
    
    public void setFilter(Filters filter) {
        this.filter=filter;
    }
    
    public Bandstacks getBandstacks() {
        return bandstacks;
    }
    
    public void setBandstacks(Bandstacks bandstacks) {
        this.bandstacks=bandstacks;
    }
    
    public BandstackEntry getBandstackEntry() {
        return bandstackEntry;
    }
    
    public void setBandstackEntry(BandstackEntry bandstackEntry) {
        this.bandstackEntry=bandstackEntry;
    }
    
    public double getAFrequency() {
        return aFrequency;
    }
    
    public void setAFrequency(double aFrequency) {
        this.aFrequency=aFrequency;
    }
    
    public double getBFrequency() {
         return bFrequency;    
    }
    
    public void setBFrequency(double bFrequency) {
        this.bFrequency=bFrequency;
    }
    
    public Displays getDisplay() {
        return display;
    }
    
    public void setDisplay(Displays display) {
        this.display=display;
    }
    
    public RxMeters getRxMeter() {
        return rxMeter;
    }
    
    public void setRxMeter(RxMeters rxMeter) {
        this.rxMeter=rxMeter;
    }
    
    public TxMeters getTxMeter() {
        return txMeter;
    }
    
    public void setTxMeter(TxMeters txMeter) {
        this.txMeter=txMeter;
    }
    
    public double getOffset() {
        return offset;
    }
    
    public void setOffset(double offset) {
        this.offset=offset;
    }
    
    public LSBFilters getLsbFilters() {
        return lsbFilters;
    }
    
    public USBFilters getUsbFilters() {
        return usbFilters;
    }
    
    public DSBFilters getDsbFilters() {
        return dsbFilters;
    }
    
    public AMFilters getAmFilters() {
        return amFilters;
    }
    
    public FMNFilters getFmnFilters() {
        return fmnFilters;
    }
    
    public CWLFilters getCwlFilters() {
        return cwlFilters;
    }
    
    public CWUFilters getCwuFilters() {
        return cwuFilters;
    }
    
    public DIGUFilters getDiguFilters() {
        return diguFilters;
    }
    
    public DIGLFilters getDiglFilters() {
        return diglFilters;
    }
    
    public SPECFilters getSpecFilters() {
        return specFilters;
    }
    
    public SAMFilters getSamFilters() {
        return samFilters;
    }
    
    public DRMFilters getDrmFilters() {
        return drmFilters;
    }
    
    public void setLsbFilters(LSBFilters lsbFilters) {
        this.lsbFilters=lsbFilters;
    }
    
    public void setUsbFilters(USBFilters usbFilters) {
        this.usbFilters=usbFilters;
    }

    public void setAmFilters(AMFilters amFilters) {
        this.amFilters=amFilters;
    }
    
    public void setFmnFilters(FMNFilters fmnFilters) {
        this.fmnFilters=fmnFilters;
    }
    
    public void setCwlFilters(CWLFilters cwlFilters) {
        this.cwlFilters=cwlFilters;
    }

    public void setCwuFilters(CWUFilters cwuFilters) {
        this.cwuFilters=cwuFilters;
    }
    
    public void setDiguFilters(DIGUFilters diguFilters) {
        this.diguFilters=diguFilters;
    }
    
    public void setDiglFilters(DIGLFilters diglFilters) {
        this.diglFilters=diglFilters;
    }
 
    public void setSpecFilters(SPECFilters specFilters) {
        this.specFilters=specFilters;
    }
 
    public void setSamFilters(SAMFilters samFilters) {
        this.samFilters=samFilters;
    }
    
    public void setDrmFilters(DRMFilters drmFilters) {
        this.drmFilters=drmFilters;
    }
    
    public void setJackCommand(String jackCommand) {
        this.jackCommand=jackCommand;
    }
    
    public String getJackCommand() {
        return jackCommand;
    }
    
    public void setJackDriver(String jackDriver) {
        this.jackDriver=jackDriver;
    }
 
    public String getJackDriver() {
        return jackDriver;
    }
    
    public void setJackDevice(String jackDevice) {
        this.jackDevice=jackDevice;
    }
    
    public String getJackDevice() {
        return jackDevice;
    }
    
    public boolean getJackRealtime() {
        return jackRealtime;
    }
    
    public void setJackRealtime(boolean state) {
        jackRealtime=state;
    }
    
    public void setJackOptions(String[] jackOptions) {
        this.jackOptions=jackOptions;
    }
 
    public String[] getJackOptions() {
        return jackOptions;
    }

    public void setJackConnectCommand(String jackConnectCommand) {
        this.jackConnectCommand=jackConnectCommand;
    }
    
    public String getJackConnectCommand() {
        return jackConnectCommand;
    }
    
    public void setDttspCommand(String dttspCommand) {
        this.dttspCommand=dttspCommand;
    }
    
    public String getDttspCommand() {
        return dttspCommand;
    }
    
    public void setDttspFifoPath(String dttspFifoPath) {
        this.dttspFifoPath=dttspFifoPath;
    }
    
    public String getDttspFifoPath() {
        return dttspFifoPath;
    }
    
    public void setDttspRxId(String dttspRxId) {
        this.dttspRxId=dttspRxId;
    }
    
    public String getDttspRxId() {
        return dttspRxId;
    }
    
    public void setDttspRxCommandFifo(String dttspRxCommandFifo) {
        this.dttspRxCommandFifo=dttspRxCommandFifo;
    }
    
    public String getDttspRxCommandFifo() {
        return dttspRxCommandFifo;
    }
    
    public void setDttspRxSpectrumFifo(String dttspRxSpectrumFifo) {
        this.dttspRxSpectrumFifo=dttspRxSpectrumFifo;
    }
    
    public String getDttspRxSpectrumFifo() {
        return dttspRxSpectrumFifo;
    }
    
    public void setDttspRxMeterFifo(String dttspRxMeterFifo) {
        this.dttspRxMeterFifo=dttspRxMeterFifo;
    }
    
    public String getDttspRxMeterFifo() {
        return dttspRxMeterFifo;
    }
    
    public void setDttspTxId(String dttspTxId) {
        this.dttspTxId=dttspTxId;
    }
    
    public String getDttspTxId() {
        return dttspTxId;
    }
    
    public void setDttspTxCommandFifo(String dttspTxCommandFifo) {
        this.dttspTxCommandFifo=dttspTxCommandFifo;
    }
    
    public String getDttspTxCommandFifo() {
        return dttspTxCommandFifo;
    }
    
    public void setDttspTxSpectrumFifo(String dttspTxSpectrumFifo) {
        this.dttspTxSpectrumFifo=dttspTxSpectrumFifo;
    }
    
    public String getDttspTxSpectrumFifo() {
        return dttspTxSpectrumFifo;
    }
    
    public void setDttspTxMeterFifo(String dttspTxMeterFifo) {
        this.dttspTxMeterFifo=dttspTxMeterFifo;
    }
    
    public String getDttspTxMeterFifo() {
        return dttspTxMeterFifo;
    }
    
    public void setSdr1000Command(String sdr1000Command) {
        this.sdr1000Command=sdr1000Command;
    }
    
    public String getSdr1000Command() {
        return sdr1000Command;
    }
    
    public String getRxInputLeftSource() {
        return rxInputLeftSource;
    }
    
    public void setRxInputLeftSource(String source) {
        this.rxInputLeftSource=source;
    }
    
    public String getRxInputRightSource() {
        return rxInputRightSource;
    }
    
    public void setRxInputRightSource(String source) {
        this.rxInputRightSource=source;
    }
    
    public String getTxInputLeftSource() {
        return txInputLeftSource;
    }
    
    public void setTxInputLeftSource(String source) {
        this.txInputLeftSource=source;
    }
    
    public String getTxInputRightSource() {
        return txInputRightSource;
    }
    
    public void setTxInputRightSource(String source) {
        this.txInputRightSource=source;
             }
    
    public String getRxOutputLeftDestination() {
        return rxOutputLeftDestination;
    }
    
    public void setRxOutputLeftDestination(String source) {
        this.rxOutputLeftDestination=source;
    }
    
    public String getRxOutputRightDestination() {
        return rxOutputRightDestination;
    }
    
    public void setRxOutputRightDestination(String source) {
        this.rxOutputRightDestination=source;
    }
    
    public String getTxOutputLeftDestination() {
        return txOutputLeftDestination;
    }
    
    public void setTxOutputLeftDestination(String source) {
        this.txOutputLeftDestination=source;
    }
    
    public String getTxOutputRightDestination() {
        return txOutputRightDestination;
    }
    
    public void setTxOutputRightDestination(String source) {
        this.txOutputRightDestination=source;
    }
    
    public float getWaterfallLowThreshold() {
        return waterfallLowThreshold;
    }
    
    public void setWaterfallLowThreshhold(float waterfallLowThreshold) {
        this.waterfallLowThreshold=waterfallLowThreshold;
    }
    
    public float getWaterfallHighThreshold() {
        return waterfallHighThreshold;
    }
    
    public void setWaterfallHighThreshhold(float waterfallHighThreshold) {
        this.waterfallHighThreshold=waterfallHighThreshold;
    }
    
    public int getSpectrumLow() {
        return spectrumLow;
    }
    
    public void setSpectrumLow(int spectrumLow) {
        this.spectrumLow=spectrumLow;
    }

    public int getSpectrumHigh() {
        return spectrumHigh;
    }
    
    public void setSpectrumHigh(int spectrumHigh) {
        this.spectrumHigh=spectrumHigh;
    }
    
    public int getSpectrumFrequency() {
        return spectrumFrequency;
    }
    
    public void setSpectrumFrequency(int spectrumFrequency) {
        this.spectrumFrequency=spectrumFrequency;
    }
    
    public int getMeterFrequency() {
        return meterFrequency;
    }
    
    public void setMeterFrequency(int meterFrequency) {
        this.meterFrequency=meterFrequency;
    }
    
    public void setPreamp(Preamps preamp) {
        this.preamp=preamp;
    }
    
    public Preamps getPreamp() {
        return preamp;
    }
    
    public void setAgc(AGCs agc) {
        this.agc=agc;
    }
    
    public AGCs getAgc() {
        return agc;
    }
    
    public void setAgcSlope(int slope) {
        agcSlope=slope;
    }
    
    public int getAgcSlope() {
        return agcSlope;
    }
    
    public void setAgcMaxGain(int gain) {
        agcMaxGain=gain;
    }
    
    public int getAgcMaxGain() {
        return agcMaxGain;
    }
    
    public void setAgcAttack(int attack) {
        agcAttack=attack;
    }
    
    public int getAgcAttack() {
        return agcAttack;
    }
    
    public void setAgcDecay(int decay) {
        agcDecay=decay;
    }
    
    public int getAgcDecay() {
        return agcDecay;
    }
    
    public void setAgcHang(int hang) {
        agcHang=hang;
    }
    
    public int getAgcHang() {
        return agcHang;
    }
    
    public void setAgcFixedGain(int gain) {
        agcFixedGain=gain;
    }
    
    public int getAgcFixedGain() {
        return agcFixedGain;
    }
    
    public void setAgcHangThreshold(int threshold) {
        agcHangThreshold=threshold;
    }
    
    public int getAgcHangThreshold() {
        return agcHangThreshold;
    }

    public int getRxAfGain() {
        return rxAfGain;
    }
    
    public void setRxAfGain(int gain) {
        this.rxAfGain=gain;
    }
    
    public int getRxIfGain() {
        return rxIfGain;
    }
    
    public void setRxIfGain(int gain) {
        this.rxIfGain=gain;
    }
    
    public float getRxPan() {
        return rxPan;
    }
    
    public void setRxPan(float rxPan) {
        this.rxPan=rxPan;
    }
    
    public int getTxMicGain() {
        return txMicGain;
    }
    
    public void setTxMicGain(int gain) {
        this.txMicGain=gain;
    }
    
    public int getTxDriveGain() {
        return txDriveGain;
    }
    
    public void setTxDriveGain(int gain) {
        this.txDriveGain=gain;
    }
    
    public int getCwPitch() {
        return cwPitch;
    }
    
    public void setCwPitch(int pitch) {
        this.cwPitch=pitch;
    }
    
    public boolean isJack() {
        return jack;
    }
    
    public void setJack(boolean state) {
        jack=state;
    }
    
    public boolean isJackStartProcess() {
        return jackStartProcess;
    }
    
    public void setJackStartProcess(boolean state) {
        jackStartProcess=state;
    }
    
    public boolean isJackConnectStartProcess() {
        return jackConnectStartProcess;
    }
    
    public void setJackConnectStartProcess(boolean state) {
        jackConnectStartProcess=state;
    }
    
    public boolean isSdr1000StartProcess() {
        return sdr1000StartProcess;
    }
    
    public void setSdr1000StartProcess(boolean state) {
        sdr1000StartProcess=state;
    }
    
    public boolean isDttspStartProcess() {
        return dttspStartProcess;
    }
    
    public void setDttspStartProcess(boolean state) {
        dttspStartProcess=state;
    }
    
    private void save() {
        bandstackEntry.setAFrequency(aFrequency);
        bandstackEntry.setMode(mode);
        bandstackEntry.setFilter(filter);
    }
    
    public void loadXML() {
        File f=new File(xmlFile);
        if(f.exists()) {
            try {
                Properties properties=new Properties();
                properties.loadFromXML(new FileInputStream(f));
                radio=Radios.valueOf(properties.getProperty("radio","SDR1000"));
                softrockCenterFrequency=Double.parseDouble(properties.getProperty("softrockCenterFrequency","7.056000"));
                transmitEnabled="TRUE".equalsIgnoreCase(properties.getProperty("transmitEnabled","FALSE"));
                usb="TRUE".equalsIgnoreCase(properties.getProperty("usb","TRUE"));
                rfe="TRUE".equalsIgnoreCase(properties.getProperty("rfe","TRUE"));
                pa="TRUE".equalsIgnoreCase(properties.getProperty("pa","TRUE"));
                port=Integer.parseInt(properties.getProperty("port","378"),16);
                soundcard.setSoundcard(Soundcards.valueOf(properties.getProperty("soundcard","UNSUPPORTED")));
                sampleRate=Integer.parseInt(properties.getProperty("sampleRate","96000"));
                bufferSize=Integer.parseInt(properties.getProperty("bufferSize","1024"));
        
                offset=Double.parseDouble(properties.getProperty("offset","-0.011025"));
                step=Double.parseDouble(properties.getProperty("step","-0.011025"));
        
                band=Bands.valueOf(properties.getProperty("band","BAND_4"));
                
                int length=Integer.parseInt(properties.getProperty("bandstack.length","0"));
                Bands tempBand;
                Filters tempFilter;
                Modes tempMode;
                double tempAFrequency;
                bandstacks=new Bandstacks(length);
                for(int i=0;i<length;i++) {
                    int entries=Integer.parseInt(properties.getProperty("bandstack."+i+".entries","0"));
                    bandstacks.set(i,entries);
                    for(int j=0;j<entries;j++) {
                        tempBand=Bands.valueOf(properties.getProperty("bandstack."+i+"."+j+".band"));
                        tempFilter=Filters.valueOf(properties.getProperty("bandstack."+i+"."+j+".filter"));
                        tempMode=Modes.valueOf(properties.getProperty("bandstack."+i+"."+j+".mode"));
                        tempAFrequency=Double.parseDouble(properties.getProperty("bandstack."+i+"."+j+".aFrequency"));
                        bandstacks.setEntry(i,j,new BandstackEntry(tempBand,tempAFrequency,tempMode,tempFilter));
                    }
                    bandstacks.setIndex(i,Integer.parseInt(properties.getProperty("bandstack."+i+".index","0")));
                }

                bandstackEntry=bandstacks.getBandstackEntry(band);
                mode=bandstackEntry.getMode();
                filter=bandstackEntry.getFilter();
                aFrequency=bandstackEntry.getAFrequency();
                
                bFrequency=Double.parseDouble(properties.getProperty("bFrequency","7.056"));
                
                cwPitch=Integer.parseInt(properties.getProperty("cwPitch","600"));
                
                setDisplay(Displays.valueOf(properties.getProperty("display","PANFALL")));
                rxMeter=RxMeters.valueOf(properties.getProperty("rxMeter","SIGNAL_STRENGTH"));
                txMeter=TxMeters.valueOf(properties.getProperty("txMeter","MIC"));
                
                sdr1000Command=properties.getProperty("sdr1000Command","bin/mac/sdr1000");     
                sdr1000FifoPath=properties.getProperty("sdr1000FifoPath","fifos");
                sdr1000HwCommandFifo=properties.getProperty("sdr1000HwCommandFifo","HWcommands");
                sdr1000HwStatusFifo=properties.getProperty("sdr1000HwStatusFifo","HWstatus");
    
                jackCommand=properties.getProperty("jackCommand","bin/mac/jackd");
                jackDriver=properties.getProperty("jackDriver","coreaudio");
                jackDevice=properties.getProperty("jackDevice","coreaudio");
                jackRealtime="TRUE".equalsIgnoreCase(properties.getProperty("jackRealtime","TRUE"));
                jackOptions=new String[Integer.parseInt(properties.getProperty("jackOptions","0"))];
                for(int i=0;i<jackOptions.length;i++) {
                    jackOptions[i]=properties.getProperty("jackOptions."+i,"");
                }
                jackConnectCommand=properties.getProperty("jackConnectCommand","bin/mac/jack_connect");
                
                dttspCommand=properties.getProperty("dttspCommand","bin/mac/sdr-core");
    
                dttspFifoPath=properties.getProperty("dttspFifoPath","fifos");
                dttspRxId=properties.getProperty("dttspRxId","sdr-rx");
                dttspRxCommandFifo=properties.getProperty("dttspRxCommandFifo","RXcommands");
                dttspRxSpectrumFifo=properties.getProperty("dttspRxSpectrumFifo","RXspectrum");
                dttspRxMeterFifo=properties.getProperty("dttspRxMeterFifo","RXmeter");
                dttspTxId=properties.getProperty("dttspTxId","sdr-tx");
                dttspTxCommandFifo=properties.getProperty("dttspTxCommandFifo","TXcommands");
                dttspTxSpectrumFifo=properties.getProperty("dttspTxSpectrumFifo","TXspectrum");
                dttspTxMeterFifo=properties.getProperty("dttspTxMeterFifo","TXmeter");
                
                rxInputLeftSource=properties.getProperty("rxInputLeftSource","EDIROL FA-66 (0431):out3");
                rxInputRightSource=properties.getProperty("rxInputRightSource","EDIROL FA-66 (0431):out4");
                rxOutputLeftDestination=properties.getProperty("rxOutputLeftDestination","EDIROL FA-66 (0431):in1");
                rxOutputRightDestination=properties.getProperty("rxOutputRightDestination","EDIROL FA-66 (0431):in2");
                txInputLeftSource=properties.getProperty("txInputLeftSource","EDIROL FA-66 (0431):out1");
                txInputRightSource=properties.getProperty("txInputRightSource","EDIROL FA-66 (0431):out2");
                txOutputLeftDestination=properties.getProperty("txOutputLeftDestination","EDIROL FA-66 (0431):in3");
                txOutputRightDestination=properties.getProperty("txOutputRightDestination","EDIROL FA-66 (0431):in4");
                
                waterfallLowThreshold=Float.parseFloat(properties.getProperty("waterfallLowThreshold","-160"));
                waterfallHighThreshold=Float.parseFloat(properties.getProperty("waterfallHighThreshold","-80"));
                
                spectrumLow=Integer.parseInt(properties.getProperty("spectrumLow","-170"));
                spectrumHigh=Integer.parseInt(properties.getProperty("spectrumHigh","-70"));
                spectrumFrequency=Integer.parseInt(properties.getProperty("spectrumFrequency","15"));
                meterFrequency=Integer.parseInt(properties.getProperty("meterFrequency","15"));
        
                preamp=Preamps.valueOf(properties.getProperty("preamp","HIGH"));
                agc=AGCs.valueOf(properties.getProperty("agc","MEDIUM"));
                agcSlope=Integer.parseInt(properties.getProperty("agcSlope","0"));
                agcMaxGain=Integer.parseInt(properties.getProperty("agcMaxGain","60"));
                agcAttack=Integer.parseInt(properties.getProperty("agcAttack","2"));
                agcDecay=Integer.parseInt(properties.getProperty("agcDecay","2000"));
                agcHang=Integer.parseInt(properties.getProperty("agcHang","750"));
                agcFixedGain=Integer.parseInt(properties.getProperty("agcFixedGain","20"));
                agcHangThreshold=Integer.parseInt(properties.getProperty("agcHangThreshold","0"));
                rxIfGain=Integer.parseInt(properties.getProperty("rxIfGain","1"));
                rxAfGain=Integer.parseInt(properties.getProperty("rxAfGain","1"));
                rxPan=Float.parseFloat(properties.getProperty("rxPan","0.5"));
                txMicGain=Integer.parseInt(properties.getProperty("txMicGain","1"));
                txDriveGain=Integer.parseInt(properties.getProperty("txDriveGain","1"));
                
                jack="TRUE".equalsIgnoreCase(properties.getProperty("jack","TRUE"));
                jackStartProcess="TRUE".equalsIgnoreCase(properties.getProperty("jackStartProcess","TRUE"));
                jackConnectStartProcess="TRUE".equalsIgnoreCase(properties.getProperty("jackConnectStartProcess","TRUE"));
                sdr1000StartProcess="TRUE".equalsIgnoreCase(properties.getProperty("sdr1000StartProcess","TRUE"));
                dttspStartProcess="TRUE".equalsIgnoreCase(properties.getProperty("dttspStartProcess","TRUE"));
                defaults=false;
                
            } catch (Exception e) {
                System.err.println("State.loadXML: "+e.toString());
                e.printStackTrace();
            }
        }
    }
         
    public void saveXML() {
        save();
        try {
            File f=new File(xmlFile);
            Properties properties=new Properties();
            properties.setProperty("radio",radio.toString());
            properties.setProperty("softrockCenterFrequency",Double.toString(softrockCenterFrequency));
            properties.setProperty("rfe",Boolean.toString(rfe));
            properties.setProperty("pa",Boolean.toString(pa));
            properties.setProperty("usb",Boolean.toString(usb));
            properties.setProperty("port",Integer.toHexString(port));
            properties.setProperty("transmitEnabled",Boolean.toString(transmitEnabled));
            properties.setProperty("soundcard",soundcard.getSoundcard().toString());
            properties.setProperty("sampleRate",Integer.toString(sampleRate));
            properties.setProperty("bufferSize",Integer.toString(bufferSize));
            properties.setProperty("offset",Double.toString(offset));
            properties.setProperty("step",Double.toString(step));
            properties.setProperty("band",band.toString());
            properties.setProperty("cwPitch", Integer.toString(cwPitch));
            
            Bandstack[] bandstack=bandstacks.getBandstacks();
            properties.setProperty("bandstack.length",Integer.toString(bandstack.length));
            for(int i=0;i<bandstack.length;i++) {
                if(bandstack[i]!=null) {
                    BandstackEntry[] bandstackEntries=bandstack[i].getBandstackEntries();
                    properties.setProperty("bandstack."+i+".entries",Integer.toString(bandstackEntries.length));
                    for(int j=0;j<bandstackEntries.length;j++) {
                        properties.setProperty("bandstack."+i+"."+j+".band",bandstackEntries[j].getBand().toString());
                        properties.setProperty("bandstack."+i+"."+j+".filter",bandstackEntries[j].getFilter().toString());
                        properties.setProperty("bandstack."+i+"."+j+".mode",bandstackEntries[j].getMode().toString());
                        properties.setProperty("bandstack."+i+"."+j+".aFrequency",Double.toString(bandstackEntries[j].getAFrequency()));
                    } 
                    properties.setProperty("bandstack."+i+".index",Integer.toString(bandstack[i].getBandstackIndex()));
                }
            }
            
            properties.setProperty("bFrequency",Double.toString(bFrequency));
            properties.setProperty("display",display.toString());
            properties.setProperty("rxMeter",rxMeter.toString());
            properties.setProperty("txMeter",txMeter.toString());
            properties.setProperty("sdr1000Command",sdr1000Command);
            properties.setProperty("sdr1000FifoPath",sdr1000FifoPath);
            properties.setProperty("sdr1000HwCommandFifo",sdr1000HwCommandFifo);
            properties.setProperty("sdr1000HwStatusFifo",sdr1000HwStatusFifo);
                
            properties.setProperty("jackCommand",jackCommand);
            properties.setProperty("jackDriver",jackDriver);
            properties.setProperty("jackDevice",jackDevice);
            properties.setProperty("jackRealtime",Boolean.toString(jackRealtime));
            properties.setProperty("jackOptions",Integer.toString(jackOptions.length));
            for(int i=0;i<jackOptions.length;i++) {
                properties.setProperty("jackOptions."+i,jackOptions[i]);
            }
            properties.setProperty("jackConnectCommand",jackConnectCommand);
            
            properties.setProperty("dttspCommand",dttspCommand);
            properties.setProperty("dttspFifoPath",dttspFifoPath);
            properties.setProperty("dttspRxId",dttspRxId);
            properties.setProperty("dttspRxCommandFifo",dttspRxCommandFifo);
            properties.setProperty("dttspRxSpectrumFifo",dttspRxSpectrumFifo);
            properties.setProperty("dttspRxMeterFifo",dttspRxMeterFifo);
            properties.setProperty("dttspTxId",dttspTxId);
            properties.setProperty("dttspTxCommandFifo",dttspTxCommandFifo);
            properties.setProperty("dttspTxSpectrumFifo",dttspTxSpectrumFifo);
            properties.setProperty("dttspTxMeterFifo",dttspTxMeterFifo);
            
            properties.setProperty("rxInputLeftSource",rxInputLeftSource);
            properties.setProperty("rxInputRightSource",rxInputRightSource);
            properties.setProperty("rxOutputLeftDestination",rxOutputLeftDestination);
            properties.setProperty("rxOutputRightDestination",rxOutputRightDestination);
            properties.setProperty("txInputLeftSource",txInputLeftSource);
            properties.setProperty("txInputRightSource",txInputRightSource);
            properties.setProperty("txOutputLeftDestination",txOutputLeftDestination);
            properties.setProperty("txOutputRightDestination",txOutputRightDestination);
            
            properties.setProperty("waterfallLowThreshold",Float.toString(waterfallLowThreshold));
            properties.setProperty("waterfallHighThreshold",Float.toString(waterfallHighThreshold));
            
            properties.setProperty("spectrumLow",Integer.toString(spectrumLow));;
            properties.setProperty("spectrumHigh",Integer.toString(spectrumHigh));
            properties.setProperty("spectrumFrequency",Integer.toString(spectrumFrequency));
            properties.setProperty("meterFrequency",Integer.toString(meterFrequency));
            
            properties.setProperty("preamp",preamp.toString());
            properties.setProperty("agc",agc.toString());
            properties.setProperty("agcSlope",Integer.toString(agcSlope));
            properties.setProperty("agcMaxGain",Integer.toString(agcMaxGain));
            properties.setProperty("agcAttack",Integer.toString(agcAttack));
            properties.setProperty("agcDecay",Integer.toString(agcDecay));
            properties.setProperty("agcHang",Integer.toString(agcHang));
            properties.setProperty("agcFixedGain",Integer.toString(agcFixedGain));
            properties.setProperty("agcHangThreshold",Integer.toString(agcHangThreshold));
            
            properties.setProperty("rxAfGain",Integer.toString(rxAfGain));
            properties.setProperty("rxIfGain",Integer.toString(rxIfGain));
            properties.setProperty("txMicGain",Integer.toString(txMicGain));
            properties.setProperty("txDriveGain",Integer.toString(txDriveGain));
            
            properties.setProperty("rxPan",Float.toString(rxPan));
            
            properties.setProperty("jack",Boolean.toString(jack));
            properties.setProperty("jackStartProcess",Boolean.toString(jackStartProcess));
            properties.setProperty("jackConnectStartProcess",Boolean.toString(jackConnectStartProcess));
            properties.setProperty("sdr1000StartProcess",Boolean.toString(sdr1000StartProcess));
            properties.setProperty("dttspStartProcess",Boolean.toString(dttspStartProcess));
            
            properties.storeToXML(new FileOutputStream(f),"Temp version until Apple fix their problems");
            
        } catch (Exception e) {
            System.err.println("State.saveXML: "+e.toString());
            e.printStackTrace();
        }
    }
    
    private boolean defaults=true;
    
    private Radios radio;
    private boolean rfe;
    private boolean pa;
    private boolean usb;
    private int port;
    
    private boolean transmitEnabled;
    
    private double softrockCenterFrequency;
    
    private double step;
    private double offset;
    private Soundcard soundcard;
    private int sampleRate;
    private int bufferSize;
    
    private Bands band;
    private Modes mode;
    private Filters filter;
    private Bandstacks bandstacks;
    private BandstackEntry bandstackEntry;
    private double aFrequency;
    private double bFrequency;
    private Displays display;
    private RxMeters rxMeter;
    private TxMeters txMeter;
    private Preamps preamp;
    private AGCs agc;
    private int agcSlope;
    private int agcMaxGain;
    private int agcAttack;
    private int agcDecay;
    private int agcHang;
    private int agcFixedGain;
    private int agcHangThreshold;
    private int rxAfGain;
    private int rxIfGain;
    private float rxPan;
    private int txMicGain;
    private int txDriveGain;
    private int cwPitch;
    
    private String sdr1000Command;
    private String sdr1000FifoPath;
    private String sdr1000HwCommandFifo;
    private String sdr1000HwStatusFifo;
    
    private String dttspCommand;
    private String dttspFifoPath;
    private String dttspRxId;
    private String dttspRxCommandFifo;
    private String dttspRxSpectrumFifo;
    private String dttspRxMeterFifo;
    private String dttspTxId;
    private String dttspTxCommandFifo;
    private String dttspTxSpectrumFifo;
    private String dttspTxMeterFifo;
    
    private String jackCommand;
    private String jackDriver;
    private String jackDevice;
    private boolean jackRealtime;
    private String[] jackOptions;
    
    private String jackConnectCommand;
    private String rxInputLeftSource;
    private String rxInputRightSource;
    private String rxOutputLeftDestination;
    private String rxOutputRightDestination;
    private String txInputLeftSource;
    private String txInputRightSource;
    private String txOutputLeftDestination;
    private String txOutputRightDestination;
    
    private LSBFilters lsbFilters;
    private USBFilters usbFilters;
    private DSBFilters dsbFilters;
    private AMFilters amFilters;
    private FMNFilters fmnFilters;
    private CWLFilters cwlFilters;
    private CWUFilters cwuFilters;
    private DIGUFilters diguFilters;
    private DIGLFilters diglFilters;
    private SPECFilters specFilters;
    private SAMFilters samFilters;
    private DRMFilters drmFilters;
    
    private float waterfallLowThreshold;
    private float waterfallHighThreshold;
    
    private int spectrumHigh;
    private int spectrumLow;
    
    private int spectrumFrequency;
    private int meterFrequency;
    
    private boolean jack;
    
    private boolean jackStartProcess;
    private boolean jackConnectStartProcess;
    private boolean sdr1000StartProcess;
    private boolean dttspStartProcess;
    
    
    private static final String xmlFile="radio.xml";
    
    private static State instance;
    
    public static State getInstance() {
        // the following had coded because of problems with Apple implementation
        /* 
        File f=new File("radio.xml");
        try {
            XMLDecoder decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(f)));
            instance=(State)decoder.readObject();
            decoder.close();
        } catch(Exception e) {
            System.err.println("State.getInstance: Exception: "+e.getMessage());
            instance=new State();
            instance.setDefaults();
        }
        */
        if(instance==null) {
            instance=new State();
            instance.setDefaults();
            instance.loadXML();
        }
        
        return instance;
    }
    
    public static void saveInstance() {
        
        /*
        File f=new File("radio.xml");
        try {
            XMLEncoder encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(f)));
            encoder.writeObject(instance);
            encoder.close();
        } catch(Exception e) {
            System.err.println("State.saveInstance: Exception: "+e.getMessage());
        } 
         */
        instance.saveXML();
    }

    
}
