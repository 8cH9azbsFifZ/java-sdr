/*
 * Radio.java
 *
 * Created on September 9, 2007, 5:56 PM
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

import java.awt.Color;
import java.awt.Font;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

/**
 *
 *  Radio
 */
public class Radio extends javax.swing.JFrame
        implements BandListener,FilterListener,ModeListener,VFOListener,
                   DisplayListener,MeterListener,DSPListener,TransmitListener,
                   PreampListener,AGCListener,RxGainListener,TxGainListener {
    
    /** Creates new form Radio */
    public Radio() {
        super("Java GUI for DttSP: SDR-1000");
        initComponents();
    }
    
    public static void main(String[] args) {
        Radio radio=new Radio();
        radio.init();
        radio.setVisible(true);
    }
    
    public void init() {
       
        // get the saved state (sets defaults if no radio.xml)
        state=State.getInstance();
        
        // if this is the first time (no radio.xml)
        // give the user a chance to configure the system
        if(state.isDefaults()) {
            configure();
            if(configureDialog.isQuit()) {
                this.setVisible(false);
                System.exit(0);
           }
        }
        
        this.setTitle("Java GUI for DttSP: "+state.getRadio().toString());
        
        startupDialog=new StartupDialog(this,false);
        startupDialog.setTitle("Java GUI for DttSP: Startup");
        startupDialog.setRadio(state.getRadio());
        java.awt.Dimension d=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        startupDialog.setLocation(((int)d.getWidth()/2)-(startupDialog.getWidth()/2),((int)d.getHeight()/2)-(startupDialog.getHeight()/2));
        startupDialog.setVisible(true); 
        
        boolean errors=false;
        do {
            if(errors) {
                stopThreads();
                stopProcesses();
                configure();
                if(configureDialog.isQuit()) {
                    this.setVisible(false);
                    System.exit(0);
                }
            }   
            errors=startProcesses();    
        } while (errors);
        
        //could have changed in the config dialog
        this.setTitle("Java GUI for DttSP: "+state.getRadio().toString());
        
        createCommandFifos();

        addListeners();
        
        // setup DttSP        
        rxDttSP.sendCommand("setTRX "+RX);
        rxDttSP.sendCommand("setRunState 2");
        if(state.isTransmitEnabled()) {
            txDttSP.sendCommand("setTRX "+TX);
            txDttSP.sendCommand("setRunState 1");
        }
        
        // start in standby mode
        poweredOn=false;
        if(state.getRadio()==Radios.SDR1000) {
            sdr1000.sendCommand("StandBy");
        }
        
        // setup GUI components
        setup();

        // turn it on
        powerOn();
        
        // always start in receive
        receive(RunModes.PLAY);
    }
    
    public void jackCompleted(int status) {
        synchronized(jackSemaphore) {
            jackSemaphore.notify();
        }
    }
    
    public void initOzyCompleted(int status) {
        synchronized(ozySemaphore) {
            ozySemaphore.notify();
        }
    }
    
    // BandListener
    public void bandSelected(Bands band) {
        if(band!=Bands.BAND_13) { 
            // not switching between XVtr and HF
            // save current in bandstack entry
            BandstackEntry bandstackEntry=state.getBandstackEntry(); 
            bandstackEntry.setMode(state.getMode());
            bandstackEntry.setFilter(state.getFilter());
            bandstackEntry.setAFrequency(state.getAFrequency());
            mode.unselect(state.getMode());
            filter.unselect(state.getFilter());
            this.band.unselect(state.getBand());
            if(state.getBand()!=band) {
                state.setBand(band);
                bandstackEntry=state.getBandstacks().getBandstackEntry(band);
                state.setBandstackEntry(bandstackEntry);
            } else {
                bandstackEntry=state.getBandstacks().getNextBandstackEntry(band);
                state.setBandstackEntry(bandstackEntry);
            }
            this.band.select(state.getBand());
            state.setAFrequency(bandstackEntry.getAFrequency());
            state.setMode(bandstackEntry.getMode());
            state.setFilter(bandstackEntry.getFilter());
            currentFilterValues=getFilterValues(state.getMode());
            filter.setTitles(currentFilterValues.getTitles());

            vfo.setAFrequency(state.getAFrequency());
            vfo.setAText(frequencyInfo.getFrequencyInfo(state.getAFrequency()));
            mode.select(state.getMode());
            
            filter.select(state.getFilter());
            filter.set(currentFilterValues.getLow(state.getFilter()),currentFilterValues.getHigh(state.getFilter()));
        } else {

        }
        setAFrequency(state.getAFrequency());
        setFilter(currentFilterValues.getLow(state.getFilter()),currentFilterValues.getHigh(state.getFilter()));
        setMode(state.getMode());
    }
    
    // FilterListener
    public void filterSelected(Filters filter) {
        this.filter.unselect(state.getFilter());
        state.setFilter(filter);
        this.filter.select(filter);
        this.filter.set(currentFilterValues.getLow(state.getFilter()),currentFilterValues.getHigh(state.getFilter()));
        setFilter(currentFilterValues.getLow(filter),currentFilterValues.getHigh(filter));
    }
    
    public void setFilterValues(int low,int high) {
        if(state.getFilter()!=Filters.FILTER_11 && state.getFilter()!=Filters.FILTER_12) {
            this.filter.unselect(state.getFilter());
            state.setFilter(Filters.FILTER_11);
            this.filter.select(Filters.FILTER_11);
        }
        this.filter.set(low,high);
        
        currentFilterValues.setVarValues(state.getFilter(),low,high);
        setFilter(low,high);
    }
    
    // ModeListener
    public void modeSelected(Modes mode) {
        
        System.err.println("Radio.modeSelected: "+mode);
        this.mode.unselect(state.getMode());
        state.setMode(mode);
        this.mode.select(mode);
        
        // setup filters for this mode
        this.currentFilterValues=getFilterValues(mode);
        filter.setTitles(currentFilterValues.getTitles());
        filter.unselect(state.getFilter());
        state.setFilter(currentFilterValues.getDefaultFilter());
        Filters filter=state.getFilter();
        this.filter.select(filter);
        this.filter.set(currentFilterValues.getLow(state.getFilter()),currentFilterValues.getHigh(state.getFilter()));
        
        setMode(mode);
        setFilter(currentFilterValues.getLow(filter),currentFilterValues.getHigh(filter));
        
    }
    

    // VFOListener
    public void setAFrequency(double frequency) {
        int softrockOffset=0;
        if(state.getRadio()==Radios.Softrock) {
            double min=state.getSoftrockCenterFrequency()-((state.getSampleRate()/2)/1000000.0);
            double max=state.getSoftrockCenterFrequency()+((state.getSampleRate()/2)/1000000.0);
            if(frequency<min) {
                frequency=min;
            } else if(frequency>max) {
                frequency=max;
            }
            softrockOffset=(int)((state.getSoftrockCenterFrequency()-frequency)*1000000.0);
        }
        
        state.setAFrequency(frequency);
        vfo.setAText(frequencyInfo.getFrequencyInfo(frequency));
        if(poweredOn) {
            if(state.getRadio()==Radios.SDR1000) {
                if(mox) {
                    sdr1000.sendCommand("SetFreq "+frequencyFormat.format(frequency));
                } else {
                    sdr1000.sendCommand("SetFreq "+frequencyFormat.format(frequency+state.getOffset()));
                }
            } else if(state.getRadio()==Radios.Softrock) {
                rxDttSP.sendCommand("setOsc "+softrockOffset+" "+RX);
            }
        }
        display.setFrequency(frequency);
    }
    
    public void setBFrequency(double frequency) {
        bFrequency=frequency;
        vfo.setBText(frequencyInfo.getFrequencyInfo(bFrequency));
        state.setBFrequency(frequency);
    }
    
    // DisplayListener
    public void displaySelected(Displays display) {
        this.display.unselect(state.getDisplay());
        state.setDisplay(display);
        this.display.select(display);
        
        if(spectrumThread!=null) {
            spectrumThread.terminate();
            spectrumThread=null;
        }
        
        if(spectrumUpdateThread!=null) {
            spectrumUpdateThread.terminate();
            spectrumUpdateThread=null;
        }
        
        switch(display) {
            case NONE:
                break;
            case SPECTRUM:
                if(mox||tune) {
                    txDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_FILT+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspTxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),txDttSP,"reqSpectrum");
                    spectrumUpdateThread.start();
                } else {
                    rxDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_FILT+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspRxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),rxDttSP,"reqSpectrum");
                    spectrumUpdateThread.start();
                }
                break;
            case PANADAPTER:
            case WATERFALL:
            case PANFALL:
                if(mox||tune) {
                    txDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_PRE_FILT+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspTxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),txDttSP,"reqSpectrum");
                    spectrumUpdateThread.start();
                } else {
                    rxDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_PRE_FILT+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspRxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),rxDttSP,"reqSpectrum");
                    spectrumUpdateThread.start();
                }
                break;
            case SCOPE:
                if(mox||tune) {
                    txDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_AGC+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspTxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),txDttSP,"reqScope");
                    spectrumUpdateThread.start();
                } else {
                    rxDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_AGC+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspRxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),rxDttSP,"reqScope");
                    spectrumUpdateThread.start();
                }
                break;
            case PHASE:
                if(mox||tune) {
                    txDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_AGC+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspTxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),txDttSP,"reqPhase");
                    spectrumUpdateThread.start();
                } else {
                    rxDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_AGC+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspRxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),rxDttSP,"reqPhase");
                    spectrumUpdateThread.start();
                }
                break;
            case PHASE2:
                if(mox) {
                    txDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_AGC+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspTxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),txDttSP,"reqPhase");
                    spectrumUpdateThread.start();
                } else {
                    rxDttSP.sendCommand("setSpectrumType "+SpectrumThread.SPEC_POST_AGC+" "+SpectrumThread.SPEC_PWR);
                    spectrumThread=new SpectrumThread(new java.io.File(state.getDttspFifoPath(),state.getDttspRxSpectrumFifo()),this.display);
                    spectrumThread.start();
                    spectrumUpdateThread=new SpectrumUpdateThread(state.getSpectrumFrequency(),rxDttSP,"reqPhase");
                    spectrumUpdateThread.start();
                }
                break;
        }
                    
    }
    
    public void rxMeterSelected(RxMeters rxMeter) {
        meter.selectRxMeter(rxMeter);
        if(meterThread!=null) {
            meterThread.terminate();
            meterThread=null;
        }
        
        if(meterUpdateThread!=null) {
            meterUpdateThread.terminate();
            meterUpdateThread=null;
        }
        switch(rxMeter) {
            case SIGNAL_STRENGTH:
            case SIGNAL_AVERAGE:
            case ADC_L:
            case ADC_R:
                //rxDttSP.sendCommand("setMeterType 3");
                meterThread=new MeterThread(new java.io.File(state.getDttspFifoPath(),state.getDttspRxMeterFifo()),this.meter,Meter.RX_METER);
                meterThread.start();
                meterUpdateThread=new MeterUpdateThread(state.getMeterFrequency(),rxDttSP,"reqRXMeter",Meter.RX_METER);
                meterUpdateThread.start();
                break;
            case OFF:
                break;
        }
        
    }
    
    public void txMeterSelected(TxMeters txMeter) {
        meter.selectTxMeter(txMeter);
        state.setTxMeter(txMeter);
        if(state.isTransmitEnabled()) {
            if(meterThread!=null) {
                meterThread.terminate();
                meterThread=null;
            }

            if(meterUpdateThread!=null) {
                meterUpdateThread.terminate();
                meterUpdateThread=null;
            }
            switch(txMeter) {
                case FORWARD_POWER:
                case REVERSE_POWER:
                case MIC:
                case EQ:
                case LEVELER:
                case LVL_G:
                case COMP:
                case CPDR:
                case ALC:
                case ALC_G:
                case SWR:
                    meterThread=new MeterThread(new java.io.File(state.getDttspFifoPath(),state.getDttspTxMeterFifo()),this.meter,Meter.TX_METER);
                    meterThread.start();
                    meterUpdateThread=new MeterUpdateThread(state.getMeterFrequency(),txDttSP,"reqTXMeter",Meter.TX_METER);
                    meterUpdateThread.start();
                    break;
                case OFF:
                    break;
            }
        }
    }
    
    public void preampSelected(Preamps preamp) {
        this.preamp.unselected(state.getPreamp());
        state.setPreamp(preamp);
        display.setPreampOffset(preampOffset.getPreampOffset(preamp));
        this.preamp.selected(state.getPreamp());
        switch(preamp) {
            case OFF:
                if(state.getRadio()==Radios.SDR1000) {
                    if(state.isRfe()) {
                        sdr1000.sendCommand("SetATTOn 1");
                        sdr1000.sendCommand("SetINAOn 1");
                    } else {

                    }
                }
                break;
            case LOW:
                if(state.getRadio()==Radios.SDR1000) {
                    if(state.isRfe()) {
                        sdr1000.sendCommand("SetATTOn 0");
                        sdr1000.sendCommand("SetINAOn 1");
                    } else {

                    }
                }
                break;
            case MEDIUM:
                if(state.getRadio()==Radios.SDR1000) {
                    if(state.isRfe()) {
                        sdr1000.sendCommand("SetATTOn 1");
                        sdr1000.sendCommand("SetINAOn 0");
                    } else {
                        sdr1000.sendCommand("SetINAOn 0");
                    }
                }
                break;
            case HIGH:
                if(state.getRadio()==Radios.SDR1000) {
                    if(state.isRfe()) {
                        sdr1000.sendCommand("SetATTOn 0");
                        sdr1000.sendCommand("SetINAOn 0");
                    } else {
                        sdr1000.sendCommand("SetINAOn 1");
                    }
                }
                break;
        }
    }
    
    // AGCListener
    
    public void agcSelected(AGCs agc) {
        this.agc.unselected(state.getAgc());
        state.setAgc(agc);
        this.agc.selected(state.getAgc());
        switch(agc) {
            case OFF:
                rxDttSP.sendCommand("setRXAGC 0");
                break;
            case LONG:
                rxDttSP.sendCommand("setRXAGC 1");
                break;
            case SLOW:
                rxDttSP.sendCommand("setRXAGC 2");
                break;
            case MEDIUM:
                rxDttSP.sendCommand("setRXAGC 3");
                break;
            case FAST:
                rxDttSP.sendCommand("setRXAGC 4");
                break;
        }
    }
    
    public void configureAGC() {
        AGCDialog dialog=new AGCDialog(this,true);
        dialog.setup(this);
        dialog.setAgcSlope(state.getAgcSlope());
        dialog.setAgcMaxGain(state.getAgcMaxGain());
        dialog.setAgcAttack(state.getAgcAttack());
        dialog.setAgcDecay(state.getAgcDecay());
        dialog.setAgcHang(state.getAgcHang());
        dialog.setAgcHangThreshold(state.getAgcHangThreshold());
        dialog.setAgcFixedGain(state.getAgcFixedGain());
        dialog.setVisible(true);
        state.saveInstance();
    }
    
    public void setAgcSlope(int slope) {
        rxDttSP.sendCommand("setRXAGCSlope +"+slope);
        state.setAgcSlope(slope);
    }
    
    public void setAgcMaxGain(int gain) {
        rxDttSP.sendCommand("setRXAGCLimit +"+gain);
        state.setAgcMaxGain(gain);
    }
    
    public void setAgcAttack(int attack) {
        rxDttSP.sendCommand("setRXAGCAttack +"+attack);
        state.setAgcAttack(attack);
    }
    
    public void setAgcDecay(int decay) {
        rxDttSP.sendCommand("setRXAGCDecay +"+decay);
        state.setAgcDecay(decay);
    }
    
    public void setAgcHang(int hang) {
        rxDttSP.sendCommand("setRXAGCHang +"+hang);
        state.setAgcHang(hang);
    }
    
    public void setAgcFixedGain(int gain) {
        rxDttSP.sendCommand("setRXAGCFix +"+gain);
        state.setAgcFixedGain(gain);
    }
    
    public void setAgcHangThreshold(int threshold) {
        rxDttSP.sendCommand("setRXAGCHangThreshold +"+threshold);
        state.setAgcHangThreshold(threshold);
    }
    
    
    public void stepSelected(double step) {
        state.setStep(step);
    }
    
    public void incrementFrequency(int increment) {
        setAFrequency(state.getAFrequency()+((double)increment*state.getStep()));
        vfo.setAFrequency(state.getAFrequency());
    }
    
    public void dragFrequency(double increment) {
        switch(state.getSampleRate()) {
            case 48000:
                setAFrequency(state.getAFrequency()+increment);
                break;
            case 96000:
                setAFrequency(state.getAFrequency()+increment);
                break;
            case 192000:
                setAFrequency(state.getAFrequency()+increment);
                break;
        }
        vfo.setAFrequency(state.getAFrequency());
    }
 
    public void setFrequency(double frequency) {
        setAFrequency(frequency);
        vfo.setAFrequency(frequency);
    }
    
    public void configureDisplay() {
        DisplayDialog dialog=new DisplayDialog(this,true);
        dialog.setDisplayListener(this);
        dialog.setSpectrumLow(state.getSpectrumLow());
        dialog.setSpectrumHigh(state.getSpectrumHigh());
        dialog.setWaterfallLow((int)state.getWaterfallLowThreshold());
        dialog.setWaterfallHigh((int)state.getWaterfallHighThreshold());
        dialog.setVisible(true);
        state.saveInstance();
    }
    
    public void setSpectrumLow(int low) {
        state.setSpectrumLow(low);
        display.setSpectrumLow(low);
    }
    
    public void setSpectrumHigh(int high) {
        state.setSpectrumHigh(high);
        display.setSpectrumHigh(high);
    }
    
    public void setWaterfallLow(int low) {
        state.setWaterfallLowThreshhold((float)low);
        display.setWaterfallLowThreshold((float)low);
    }
    
    public void setWaterfallHigh(int high) {
        state.setWaterfallHighThreshhold((float)high);
        display.setWaterfallHighThreshold((float)high);
    }
    
    // DSPListener
    public void nrSelected() {
        nr=!nr;
        dsp.selectNR(nr);
        rxDttSP.sendCommand("setNR "+(nr?1:0));
    }
    
    public void anfSelected() {
        anf=!anf;
        dsp.selectANF(anf);
        rxDttSP.sendCommand("setANF "+(anf?1:0));
    }
    
    public void nbSelected() {
        nb=!nb;
        dsp.selectNB(nb);
        rxDttSP.sendCommand("setNB "+(nb?1:0));
    }
    public void nb2Selected() {
        nb2=!nb2;
        dsp.selectNB2(nb2);
        rxDttSP.sendCommand("setSDROM "+(nb2?1:0));
    }
    public void srSelected() {
        sr=!sr;
        dsp.selectSR(sr);
    }
    public void binSelected() {
        bin=!bin;
        dsp.selectBIN(bin);
        rxDttSP.sendCommand("setBIN "+(bin?1:0));
    }
    
    public void rxAfGainChanged(int gain) {
        state.setRxAfGain(gain);
        rxDttSP.sendCommand("setGain "+RX+" "+OUT+" "+calculateGain(gain));
    }
    
    public void rxIfGainChanged(int gain) {
        state.setRxIfGain(gain);
        rxDttSP.sendCommand("setGain "+RX+" "+IN+" "+calculateGain(gain));
    }
 
    public void rxPanChanged(float pan) {
        state.setRxPan(pan);
        rxDttSP.sendCommand("setRXPan "+Float.toString(1.0F-pan));
    }
    
    public void txMicGainChanged(int gain) {
        state.setTxMicGain(gain);
        if(state.isTransmitEnabled()) {
            txDttSP.sendCommand("setGain "+TX+" "+IN+" "+calculateGain(gain));
        }
    }
    
    public void txDriveGainChanged(int gain) {
        state.setTxDriveGain(gain);
        if(state.isTransmitEnabled()) {
            txDttSP.sendCommand("setGain "+TX+" "+OUT+" "+calculateTXGain(gain));
        }
    }
    
    private double calculateTXGain(int gain) {
        double logLow=Math.log10(1);
        double logHigh=Math.log10(100);
        double range=logHigh-logLow;
        double ratio=range/(100-1);
        double pos=gain*ratio;
        double adjustedPos=logLow+pos;
        return Math.pow(10.0,adjustedPos);
    }
    
    private double calculateRXGain(int gain) {
        return 30.0*(double)gain/100.0;    
    }
    
    private double calculateGain(int gain) {
        return (double)gain;
    }
    
    //private double calculateTxGain(int watts) {
    //    double target_dbm = 10*(double)Math.log10((double)watts*1000.0);
    //    target_dbm -= 49.0; //GainByBand(CurrentBand);
    //    double target_volts = Math.sqrt(Math.pow(10, target_dbm*0.1)*0.05); // E = Sqrt(P * R)
    //    return target_volts/2.23; //audio_volts1;
    //}
    
    // TransmitListener
    public void moxSelected() {
        if(state.isTransmitEnabled()) {
            mox=!mox;
            transmit.setMox(mox);
            if(mox) {
                transmit(RunModes.PLAY);
            } else {
                receive(RunModes.PLAY);
            }
            transmit.enableTune(!mox);
        }
    }
    
    public void tuneSelected() {
        int tone=600;
        int filterLow=500;
        int filterHigh=700;
        if(state.isTransmitEnabled()) {
            tune=!tune;
            transmit.setTune(tune);
            if(tune) {
                switch(state.getMode()) {
                    case LSB:
                    case CWL:
                    case DIGL:
                        tone=-state.getCwPitch();
                        filterLow=tone-100;
                        filterHigh=tone+100;
                        break;
                    case USB:
                    case CWU:
                    case DIGU:
                        tone=state.getCwPitch();
                        filterLow=tone-100;
                        filterHigh=tone+100;
                        break;
                    case DSB:
                        tone=state.getCwPitch();
                        filterLow=tone-100;
                        filterHigh=tone+100;
                        break;
                    case AM:
                    case SAM:
                    case FMN:
                        tone=state.getCwPitch();
                        filterLow=tone-100;
                        filterHigh=tone+100;
                        break;
                }
                setFilter(filterLow,filterHigh);
                txDttSP.sendCommand("SetTestTone "+tone+" -50");
                txDttSP.sendCommand("SetTEST 0");
                transmit(RunModes.TEST);
            } else {
                setMode(state.getMode());
                setFilter(currentFilterValues.getLow(state.getFilter()),currentFilterValues.getHigh(state.getFilter()));
                receive(RunModes.PLAY);
            }
            transmit.enableMox(!tune);
        }
    }
    
    //-------------------------------------------------------------------------
    
    private void powerOn() {
        poweredOn=true;
        if(state.getRadio()==Radios.SDR1000) {
            sdr1000.sendCommand("PowerOn");
            receive(RunModes.PLAY);
            /*
            sdr1000.sendCommand("UpdateHW 0");
            sdr1000.sendCommand("SetPA_TR 0");
            preampSelected(state.getPreamp());
            sdr1000.sendCommand("UpdateHW 1");
            sdr1000.sendCommand("SetFreq "+frequencyFormat.format(state.getAFrequency()+state.getOffset()));
            rxDttSP.sendCommand("setOsc "+(int)(state.getOffset()*1000000.0)+" "+RX);
            hwCommandFifo.write("PowerOn");
            hwCommandFifo.write("SetPA_TR 0");
            double offset=state.getOffset();
            hwCommandFifo.write("SetFreq "+frequencyFormat.format(state.getAFrequency()+offset));
            rxCommandFifo.write("setOsc "+(int)(offset*1000000.0)+" "+RX);      
            if(state.isTransmitEnabled()) {
                txDttSP.sendCommand("setOsc "+0+" "+TX);
            }
            */
        } else if(state.getRadio()==Radios.Softrock) {
            rxDttSP.sendCommand("setOsc 0 "+RX);  // default to center frequency
            if(state.isTransmitEnabled()) {
                txDttSP.sendCommand("setOsc 0 "+TX);
            }
        }
        setFilter(currentFilterValues.getLow(state.getFilter()),currentFilterValues.getHigh(state.getFilter()));
        setMode(state.getMode());
        powerButton.setForeground(java.awt.Color.YELLOW);
    }
    
    private void transmit(RunModes mode) {
        System.err.println("Radio.transmit: "+mode.toString());
        rxDttSP.sendCommand("setRunState "+RunModes.PASS.ordinal());
        txDttSP.sendCommand("setRunState "+mode.ordinal());
        txDttSP.sendCommand("setTRX "+TX);
        if(state.getRadio()==Radios.SDR1000) {
            sdr1000.sendCommand("UpdateHW 0");
            sdr1000.sendCommand("SetMute 1");
            if(state.isRfe()) {
                sdr1000.sendCommand("SetINAOn 1");
                sdr1000.sendCommand("SetATTOn 0");
                sdr1000.sendCommand("SetRFE_TR 1");   
                if(state.isPa()) {
                    sdr1000.sendCommand("SetPA_TR 1");
                    sdr1000.sendCommand("SetPA_Bias 1");
                }
            } else {
                sdr1000.sendCommand("SetINAOn 0");
            }
            sdr1000.sendCommand("SetTRX_TR 1");
            sdr1000.sendCommand("UpdateHW 1");
            sdr1000.sendCommand("SetFreq " + frequencyFormat.format(state.getAFrequency()));
            txDttSP.sendCommand("setOsc "+0+" "+TX);
            display.setOffset(0);
        }
        txMeterSelected(state.getTxMeter());
        
        displaySelected(state.getDisplay());
        display.setOffset(0);
        
        // need to monitor the power
        power=new Power(new java.io.File(state.getSdr1000FifoPath(),state.getSdr1000HwStatusFifo()));
        //powerThread.start();
        powerUpdateThread=new PowerUpdateThread(state.getMeterFrequency(),sdr1000,this.meter,power);
        powerUpdateThread.start();
        
        System.err.println("Radio.receive: exit");
    }
    
    private void receive(RunModes mode) {
        System.err.println("Radio.receive: "+mode.toString());
        
        if(power!=null) {
            power.terminate();
            power=null;
        }

        if(powerUpdateThread!=null) {
            powerUpdateThread.terminate();
            powerUpdateThread=null;
        }
        
        if(state.isTransmitEnabled()) {
            txDttSP.sendCommand("setRunState "+RunModes.PASS.ordinal());
        }
        rxDttSP.sendCommand("setRunState "+mode.ordinal());
        if(state.getRadio()==Radios.SDR1000) {
            sdr1000.sendCommand("UpdateHW 0");
            if(state.isRfe()) {
                sdr1000.sendCommand("SetRFE_TR 0");
                if(state.isPa()) {
                    sdr1000.sendCommand("SetPA_Bias 0");
                    sdr1000.sendCommand("SetPA_TR 0");
                }
            }
            sdr1000.sendCommand("SetTRX_TR 0");
            sdr1000.sendCommand("SetMute 0");
        }
        preampSelected(state.getPreamp());
        
        if(state.getRadio()==Radios.SDR1000) {
            sdr1000.sendCommand("UpdateHW 1");
            sdr1000.sendCommand("SetFreq "+frequencyFormat.format(state.getAFrequency()+state.getOffset()));
            rxDttSP.sendCommand("setOsc "+(int)(state.getOffset()*1000000.0)+" "+RX);
        }
                    
        rxMeterSelected(state.getRxMeter());
        displaySelected(state.getDisplay());
        display.setOffset(state.getOffset());
        System.err.println("Radio.receive: exit");
    }
    
    private void configure() {
        
        configureDialog=new ConfigureDialog(this,true);
        
        // center it
        java.awt.Dimension d=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        configureDialog.setLocation(((int)d.getWidth()/2)-(configureDialog.getWidth()/2),((int)d.getHeight()/2)-(configureDialog.getHeight()/2));
           
        configureDialog.initValues();
        configureDialog.setVisible(true);
        
        // used the resulting radio type to set the title of the radio
        this.setTitle("Java GUI for DttSP: "+state.getRadio().toString());

    }

    private void setMode(Modes mode) {
        int m=0;
        switch(mode) {
            case LSB:
                m=0;
                break;
            case USB:
                m=1;
                break;
            case DSB:
                m=2;
                break;
            case CWL:
                m=3;
                break;
            case CWU:
                m=4;
                break;
            case FMN:
                m=5;
                break;
            case AM:
                m=6;
                break;
            case DIGU:
                m=7;
                break;
            case SPEC:
                m=8;
                break;
            case DIGL:
                m=9;
                break;
            case SAM:
                m=10;
                break;
        }
        if(poweredOn) {
            rxDttSP.sendCommand("setMode "+m+" "+RX);
            if(state.isTransmitEnabled()) {
                txDttSP.sendCommand("setMode "+m+" "+TX);
            }
        }
    }
    
    private void setFilter(int low,int high) {
        display.setFilter(low,high);
        if(poweredOn) {
            rxDttSP.sendCommand("setFilter "+low+" "+high+" "+RX);
            if(state.isTransmitEnabled()) {
                txDttSP.sendCommand("setFilter "+low+" "+high+" "+TX);
            }
        }
    }
    
    private FilterValues getFilterValues(Modes mode) {
        System.err.println("Radio.getFilterValues: "+mode);
        FilterValues filterValues=null;
        switch(mode) {
            case LSB:
                filterValues=state.getLsbFilters();
                break;
            case USB:
                filterValues=state.getUsbFilters();
                break;
            case DSB:
                filterValues=state.getDsbFilters();
                break;
            case AM:
                filterValues=state.getAmFilters();
                break;
            case FMN:
                filterValues=state.getFmnFilters();
                break;
            case CWL:
                filterValues=state.getCwlFilters();
                break;
            case CWU:
                filterValues=state.getCwuFilters();
                break;
            case DIGU:
                filterValues=state.getDiguFilters();
                break;
            case DIGL:
                filterValues=state.getDiglFilters();
                break;
            case SPEC:
                filterValues=state.getSpecFilters();
                break;
            case SAM:
                filterValues=state.getSamFilters();
                break;
            case DRM:
                filterValues=state.getDrmFilters();
                break;
        }
        return filterValues;
    }
    
    private boolean startOzy(){
        boolean errors =false;
        
          // Ozy and Janus init 
               startupDialog.setStatus("Starting OzyJanus ...");   
               initozy = new InitOzy("InitOzy",this);
               initozy.start();
               synchronized(ozySemaphore) {
                    try {
                        ozySemaphore.wait();
                    } catch (Exception e) {
                    }
                }
               if(initozy.getResult()!=0) {
                   errors=true;
               }
               
           // starting jack server    
                startupDialog.setStatus("Starting jack ...");
                jack=new Jack("jack",state.isJackStartProcess());
                jack.setCommand(state.getJackCommand());
                jack.setRealtime(state.getJackRealtime());
                jack.setDriver(state.getJackDriver());
                jack.setOptions(state.getJackOptions());
                jack.setSampleRate(state.getSampleRate());
                jack.setBufferSize(state.getBufferSize());
                jack.start();
                                  
                System.err.println("Sleeping ...");
                try {
                    Thread.sleep(3000);
                } catch(Exception e) {
                }
                
           // Janus connection 
               startupDialog.setStatus("Connecting OzyJanus ...");   
               jackozy=new JackOzy("connectOzy");
               jackozy.setSampleRate(state.getSampleRate());
               jackozy.setBufferSize(state.getBufferSize());
               jackozy.start();
                           
               System.err.println("Sleeping ...");
               try {
                    Thread.sleep(3000);
               } catch(Exception e) {
               }
               if(jackozy.getResult()!=0) {
                   errors=true;
               }
               return errors;
    }
    
    private boolean startProcesses() {
        boolean errors=false;        
        
        try {
            
            if(state.getSoundcard().getSoundcard().name().equalsIgnoreCase("OZY_JANUS")){
                errors=startOzy();   
                if (errors){
                   throw new StartupException("Error: ozy init failed...");
                }
            }    
            
            if(state.getRadio()==Radios.SDR1000) {
                startupDialog.setStatus("Starting SDR1000 hardware interface ...");

                sdr1000=new SDR1000("sdr1000",state.isSdr1000StartProcess());
                sdr1000.setCommand(state.getSdr1000Command());
                sdr1000.setFifoPath(state.getDttspFifoPath());
                sdr1000.setCommandPath(state.getSdr1000HwCommandFifo());
                sdr1000.setStatusPath(state.getSdr1000HwStatusFifo());
                sdr1000.setRfe(state.isRfe());
                sdr1000.setPa(state.isPa());
                sdr1000.setUsb(state.isUsb());
                sdr1000.setPort(state.getPort());
                sdr1000.start();
                System.err.println("Sleeping ...");
                try {
                    Thread.sleep(3000);
                } catch(Exception e) {
                }
            }
            
            if(state.isJack() & !(state.getSoundcard().getSoundcard().name().equalsIgnoreCase("OZY_JANUS"))){        
                startupDialog.setStatus("Starting jack ...");
                jack=new Jack("jack",state.isJackStartProcess());
                jack.setCommand(state.getJackCommand());
                jack.setRealtime(state.getJackRealtime());
                jack.setDriver(state.getJackDriver());
                jack.setOptions(state.getJackOptions());
                jack.setSampleRate(state.getSampleRate());
                jack.setBufferSize(state.getBufferSize());
                jack.start();
                                  
                System.err.println("Sleeping ...");
                try {
                    Thread.sleep(3000);
                } catch(Exception e) {
                }         
            
            }
                        
            startupDialog.setStatus("Starting RX DttSP ("+state.getDttspRxId()+") ...");
            rxDttSP=new DttSP(state.getDttspRxId(),state.isDttspStartProcess());
            rxDttSP.setCommand(state.getDttspCommand());
            rxDttSP.setSampleRate(state.getSampleRate());
            rxDttSP.setFifoPath(state.getDttspFifoPath());
            rxDttSP.setCommandPath(state.getDttspRxCommandFifo());
            rxDttSP.setMeterPath(state.getDttspRxMeterFifo());
            rxDttSP.setSpectrumPath(state.getDttspRxSpectrumFifo());
            rxDttSP.setBufferSize(state.getBufferSize());
            rxDttSP.setJack(state.isJack());
            rxDttSP.start();

            System.err.println("Sleeping ...");
            try {
                Thread.sleep(2000);
            } catch(Exception e) {
            }

            if(state.isTransmitEnabled()) {
                startupDialog.setStatus("Starting TX DttSP ("+state.getDttspTxId()+") ...");
                txDttSP=new DttSP(state.getDttspTxId(),state.isDttspStartProcess());
                txDttSP.setCommand(state.getDttspCommand());
                txDttSP.setSampleRate(state.getSampleRate());
                txDttSP.setFifoPath(state.getDttspFifoPath());
                txDttSP.setCommandPath(state.getDttspTxCommandFifo());
                txDttSP.setMeterPath(state.getDttspTxMeterFifo());
                txDttSP.setSpectrumPath(state.getDttspTxSpectrumFifo());
                txDttSP.setBufferSize(state.getBufferSize());
                txDttSP.setJack(state.isJack());
                txDttSP.start();       

                System.err.println("Sleeping ...");
                try {
                    Thread.sleep(2000);
                } catch(Exception e) {
                }
                System.err.println("Awake");
            }
                     
            if(state.isJack()) {
                   
                // make rx connections
                startupDialog.setStatus("Making Jack connection ("+state.getDttspRxId()+":il) ...");
                JackConnect jackConnect=new JackConnect(state.getDttspRxId()+":il",this,state.isJackConnectStartProcess());
                jackConnect.setCommand(state.getJackConnectCommand());
                jackConnect.setSource(state.getJackDevice()+":"+state.getRxInputLeftSource());
                jackConnect.setDestination(state.getDttspRxId()+":il");
                jackConnect.start();
                if(state.isJackConnectStartProcess()) {
                    synchronized(jackSemaphore) {
                        try {
                            jackSemaphore.wait();
                        } catch (Exception e) {
                        }
                    }
                    if(jackConnect.getResult()!=0) {
                        throw new StartupException("Error: jack connect failed");
                    }
                }


                startupDialog.setStatus("Making Jack connection ("+state.getDttspRxId()+":ir) ...");
                jackConnect=new JackConnect(state.getDttspRxId()+":ir",this,state.isJackConnectStartProcess());
                jackConnect.setCommand(state.getJackConnectCommand());
                jackConnect.setSource(state.getJackDevice()+":"+state.getRxInputRightSource());
                jackConnect.setDestination(state.getDttspRxId()+":ir");
                jackConnect.start();
                if(state.isJackConnectStartProcess()) {
                    synchronized(jackSemaphore) {
                        try {
                            jackSemaphore.wait();
                        } catch (Exception e) {
                        }
                    }
                    if(jackConnect.getResult()!=0) {
                        throw new StartupException("Error: jack connect failed");
                    }
                }

                startupDialog.setStatus("Making Jack connection ("+state.getDttspRxId()+":ol) ...");
                jackConnect=new JackConnect(state.getDttspRxId()+":ol",this,state.isJackConnectStartProcess());
                jackConnect.setCommand(state.getJackConnectCommand());
                jackConnect.setSource(state.getDttspRxId()+":ol");
                jackConnect.setDestination(state.getJackDevice()+":"+state.getRxOutputLeftDestination());
                jackConnect.start();
                if(state.isJackConnectStartProcess()) {
                    synchronized(jackSemaphore) {
                        try {
                            jackSemaphore.wait();
                        } catch (Exception e) {
                        }
                    }

                    if(jackConnect.getResult()!=0) {
                        throw new StartupException("Error: jack connect failed");
                    }
                }

                startupDialog.setStatus("Making Jack connection ("+state.getDttspRxId()+":or) ...");
                jackConnect=new JackConnect(state.getDttspRxId()+":or",this,state.isJackConnectStartProcess());
                jackConnect.setCommand(state.getJackConnectCommand());
                jackConnect.setSource(state.getDttspRxId()+":or");
                jackConnect.setDestination(state.getJackDevice()+":"+state.getRxOutputRightDestination());
                jackConnect.start();
                if(state.isJackConnectStartProcess()) {
                    synchronized(jackSemaphore) {
                        try {
                            jackSemaphore.wait();
                        } catch (Exception e) {
                        }
                    }

                    if(jackConnect.getResult()!=0) {
                        throw new StartupException("Error: jack connect failed");
                    }
                }

                if(state.isTransmitEnabled()) {
                    // make tx connections
                    startupDialog.setStatus("Making Jack connection ("+state.getDttspTxId()+":il) ...");
                    jackConnect=new JackConnect(state.getDttspTxId()+":il",this,state.isJackConnectStartProcess());
                    jackConnect.setCommand(state.getJackConnectCommand());
                    jackConnect.setSource(state.getJackDevice()+":"+state.getTxInputLeftSource());
                    jackConnect.setDestination(state.getDttspTxId()+":il");
                    jackConnect.start();
                    if(state.isJackConnectStartProcess()) {
                        synchronized(jackSemaphore) {
                            try {
                                jackSemaphore.wait();
                            } catch (Exception e) {
                            }
                        }
                    }


                    startupDialog.setStatus("Making Jack connection ("+state.getDttspTxId()+":ir) ...");
                    jackConnect=new JackConnect(state.getDttspTxId()+":ir",this,state.isJackConnectStartProcess());
                    jackConnect.setCommand(state.getJackConnectCommand());
                    jackConnect.setSource(state.getJackDevice()+":"+state.getTxInputRightSource());
                    jackConnect.setDestination(state.getDttspTxId()+":ir");
                    jackConnect.start();
                    if(state.isJackConnectStartProcess()) {
                        synchronized(jackSemaphore) {
                            try {
                                jackSemaphore.wait();
                            } catch (Exception e) {
                            }
                        }
                    }

                    startupDialog.setStatus("Making Jack connection ("+state.getDttspTxId()+":ol) ...");
                    jackConnect=new JackConnect(state.getDttspTxId()+":ol",this,state.isJackConnectStartProcess());
                    jackConnect.setCommand(state.getJackConnectCommand());
                    jackConnect.setSource(state.getDttspTxId()+":ol");
                    jackConnect.setDestination(state.getJackDevice()+":"+state.getTxOutputLeftDestination());
                    jackConnect.start();
                    if(state.isJackConnectStartProcess()) {
                        synchronized(jackSemaphore) {
                            try {
                                jackSemaphore.wait();
                            } catch (Exception e) {
                            }
                        }
                    }


                    startupDialog.setStatus("Making Jack connection ("+state.getDttspTxId()+":or) ...");
                    jackConnect=new JackConnect(state.getDttspTxId()+":or",this,state.isJackConnectStartProcess());
                    jackConnect.setCommand(state.getJackConnectCommand());
                    jackConnect.setSource(state.getDttspTxId()+":or");
                    jackConnect.setDestination(state.getJackDevice()+":"+state.getTxOutputRightDestination());
                    jackConnect.start();
                    if(state.isJackConnectStartProcess()) {
                        synchronized(jackSemaphore) {
                            try {
                                jackSemaphore.wait();
                            } catch (Exception e) {
                            }
                        }
                    }

                }
                
            }
            
        startupDialog.setVisible(false);
            
        }catch (StartupException e) {
            startupDialog.setStatus(e.getMessage());
            errors=true;

        }
        
        return errors;   
    }
         
    private void createCommandFifos() {
        //rxCommandFifo=new CommandFifo(new File(state.getDttspFifoPath(),state.getDttspRxCommandFifo()));
        //if(state.isTransmitEnabled()) {
        //    txCommandFifo=new CommandFifo(new File(state.getDttspFifoPath(),state.getDttspTxCommandFifo()));
        //}
        //if(state.getRadio()==Radios.SDR1000) {
        //    hwCommandFifo=new CommandFifo(new File(state.getSdr1000FifoPath(),state.getSdr1000HwCommandFifo()));
        //}
    }

    private void addListeners() {
        band.addListener(this);
        mode.addListener(this);
        filter.addListener(this);
        vfo.addListener(this);
        display.addListener(this);
        meter.addListener(this);
        dsp.addListener(this);
        transmit.addListener(this);
        preamp.addListener(this);
        agc.addListener(this);
        rxGain.addListener(this);
        txGain.addListener(this);
    }
   
    private void setup() {
        display.setRadio(state.getRadio());
        display.setSoftrockCenterFrequency(state.getSoftrockCenterFrequency());
        display.setSampleRate(state.getSampleRate());
        display.setDisplayCalibrationOffset(state.getSoundcard().getDisplayCalibrationOffset());
        display.setOffset(state.getOffset());
        display.select(state.getDisplay());
        display.setSpectrumLow(state.getSpectrumLow());
        display.setSpectrumHigh(state.getSpectrumHigh());
        display.setWaterfallLowThreshold(state.getWaterfallLowThreshold());
        display.setWaterfallHighThreshold(state.getWaterfallHighThreshold());
        display.setPreampOffset(preampOffset.getPreampOffset(state.getPreamp()));

        meter.setMeterCalibrationOffset(state.getSoundcard().getMeterCalibrationOffset());
        meter.setPreampOffset(preampOffset.getPreampOffset(state.getPreamp()));
        meter.selectRxMeter(state.getRxMeter());
        meter.selectTxMeter(state.getTxMeter());
        
        preamp.selected(state.getPreamp());
        agc.selected(state.getAgc());
        
        band.select(state.getBand());
        mode.select(state.getMode());
        Filters filter =state.getFilter();
        currentFilterValues=getFilterValues(state.getMode());
        this.filter.setTitles(currentFilterValues.getTitles());
        this.filter.select(filter);
        this.filter.set(currentFilterValues.getLow(filter),currentFilterValues.getHigh(filter));
        unselectedColor=powerButton.getBackground();
        
        double aFrequency=state.getAFrequency();
        vfo.setAFrequency(aFrequency);
        vfo.setAText(frequencyInfo.getFrequencyInfo(aFrequency));
        vfo.setATransmit(false);
        
        bFrequency=state.getBFrequency();
        vfo.setBFrequency(bFrequency);
        vfo.setBText(frequencyInfo.getFrequencyInfo(bFrequency));
        vfo.setBTransmit(false);
        
        vfo.setStep(state.getStep());
        display.setFrequency(aFrequency);
        displaySelected(state.getDisplay());
        rxMeterSelected(state.getRxMeter());
        
        filterSizeCalibrationOffset.setFilterSize(state.getBufferSize());
        meter.setFilterSizeOffset(filterSizeCalibrationOffset.getFilterSizeOffset());
        display.setFilterSizeOffset(filterSizeCalibrationOffset.getFilterSizeOffset());
        
        rxGain.setAfGain(state.getRxAfGain());
        rxGain.setIfGain(state.getRxIfGain());
        
        rxAfGainChanged(state.getRxAfGain());
        rxIfGainChanged(state.getRxIfGain());
        
        txGain.setMicGain(state.getTxMicGain());
        txGain.setDriveGain(state.getTxDriveGain());
        
        txMicGainChanged(state.getTxMicGain());
        txDriveGainChanged(state.getTxDriveGain());
        
        
        if(state.getRadio()==Radios.Softrock) {
            band.setEnabled(false);
        } else {
            band.setEnabled(true);
        }
        
        transmit.enableMox(state.isTransmitEnabled());
        transmit.enableTune(state.isTransmitEnabled());
   }

    private void stopProcesses() {

        if(rxDttSP!=null) {
            rxDttSP.terminate();
            rxDttSP=null;
        }
        if(txDttSP!=null) {
            txDttSP.terminate();
            txDttSP=null;
        }
        if(jack!=null) {
            jack.terminate();
            jack=null;
        }
        
        if(sdr1000!=null) {
            sdr1000.terminate();
            sdr1000=null;
        } 
                    
        if(jackozy!=null) {
            jackozy.terminate();
            jackozy=null;
        }       
    }
    
    private void stopThreads() {

        if(meterThread!=null) {
            meterThread.terminate();
            meterThread=null;
        }
        
        if(spectrumThread!=null) {
            spectrumThread.terminate();
            spectrumThread=null;
        }
        
        if(power!=null) {
            power.terminate();
            power=null;
        }
        
        if(meterUpdateThread!=null) {
            meterUpdateThread.terminate();
            meterUpdateThread=null;
        }
    
        if(spectrumUpdateThread!=null) {
            spectrumUpdateThread.terminate();
            spectrumUpdateThread=null;
        }
        
        if(powerUpdateThread!=null) {
            powerUpdateThread.terminate();
            powerUpdateThread=null;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        powerButton = new javax.swing.JButton();
        vfo = new radio.VFO();
        display = new radio.Display();
        band = new radio.Band();
        mode = new radio.Mode();
        filter = new radio.Filter();
        dsp = new radio.DSP();
        transmit = new radio.Transmit();
        meter = new radio.Meter();
        configureButton = new javax.swing.JButton();
        preamp = new radio.Preamp();
        agc = new radio.AGC();
        rxGain = new radio.RxGain();
        txGain = new radio.TxGain();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        powerButton.setBackground(new java.awt.Color(153, 153, 153));
        powerButton.setText("Power");
        powerButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        powerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                powerButtonActionPerformed(evt);
            }
        });
        getContentPane().add(powerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 90, 60));
        getContentPane().add(vfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 900, 100));
        getContentPane().add(display, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 1010, 330));
        getContentPane().add(band, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 100, 170, 140));
        getContentPane().add(mode, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 240, 170, 120));
        getContentPane().add(filter, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 360, 170, 250));
        getContentPane().add(dsp, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 430, 130, 130));
        getContentPane().add(transmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 90, 80));
        getContentPane().add(meter, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 0, -1, -1));

        configureButton.setBackground(new java.awt.Color(153, 153, 153));
        configureButton.setText("Configure");
        configureButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        configureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configureButtonActionPerformed(evt);
            }
        });
        getContentPane().add(configureButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 90, 40));
        getContentPane().add(preamp, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 430, 120, 130));
        getContentPane().add(agc, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 430, 100, 190));
        getContentPane().add(rxGain, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 430, 240, 70));
        getContentPane().add(txGain, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 500, 240, 70));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void configureButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configureButtonActionPerformed
        configure();
        if(configureDialog!=null) {
            if(configureDialog.isUpdated()) {
                // save this new config
                State.saveInstance();
                
                // for now - stop and restart processes and threads
                poweredOn=false;
                if(state.getRadio()==Radios.SDR1000) {
                    sdr1000.sendCommand("StandBy");
                }
                stopThreads();
                stopProcesses();
                
                // now restart the processes
                startupDialog.setVisible(true);
                startProcesses();
                setup();
                powerOn();
                startupDialog.setVisible(false);
            } else if(configureDialog.isQuit()) {
                poweredOn=false;
                if(state.getRadio()==Radios.SDR1000) {
                    sdr1000.sendCommand("StandBy");
                }
                stopThreads();
                stopProcesses();
                this.setVisible(false);
                System.exit(0);
            }
        }
    }//GEN-LAST:event_configureButtonActionPerformed


    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        System.err.println("Radio.formWindowClosing");
        if(state.getRadio()==Radios.SDR1000) {
            sdr1000.sendCommand("StandBy");
        }
        State.saveInstance();
        stopThreads();
        stopProcesses();
        poweredOn=false;
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void powerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_powerButtonActionPerformed

        if(poweredOn) {
            sdr1000.sendCommand("StandBy");
            poweredOn=false;
        } else {  
            sdr1000.sendCommand("PowerOn");
            poweredOn=true;
        }
            
        powerButton.setForeground(poweredOn?java.awt.Color.YELLOW:java.awt.Color.BLACK);
            
    }//GEN-LAST:event_powerButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private radio.AGC agc;
    private radio.Band band;
    private javax.swing.JButton configureButton;
    private radio.Display display;
    private radio.DSP dsp;
    private radio.Filter filter;
    private radio.Meter meter;
    private radio.Mode mode;
    private javax.swing.JButton powerButton;
    private radio.Preamp preamp;
    private radio.RxGain rxGain;
    private radio.Transmit transmit;
    private radio.TxGain txGain;
    private radio.VFO vfo;
    // End of variables declaration//GEN-END:variables
    
    private SDR1000 sdr1000;
    private Jack jack;
    private DttSP rxDttSP;
    private DttSP txDttSP;
    private InitOzy initozy;
    private JackOzy jackozy;
    
    private Object jackSemaphore=new Object();
    private Object ozySemaphore=new Object();
 
    private State state;
      
    //private CommandFifo rxCommandFifo;
    //private CommandFifo txCommandFifo;
    //private CommandFifo hwCommandFifo;
    
    private int RX=0;
    private int TX=1;
    
    private int IN=0;
    private int OUT=1;
    
    private java.text.DecimalFormat frequencyFormat=new java.text.DecimalFormat("####.000000",new java.text.DecimalFormatSymbols(java.util.Locale.ENGLISH));
    
    private boolean poweredOn;
    
    private FilterValues currentFilterValues;

    private double bFrequency;
    
    private PreampOffset preampOffset=new PreampOffset();
    private FilterSizeCalibrationOffset filterSizeCalibrationOffset=new FilterSizeCalibrationOffset();
    
    public boolean nr=false;
    public boolean anf=false;
    public boolean nb=false;
    public boolean nb2=false;
    public boolean sr=false;
    public boolean bin=false;
    
    public boolean mox=false;
    public boolean tune=false;
    
    private FrequencyInfo frequencyInfo=new FrequencyInfo();
    
    private SpectrumThread spectrumThread;
    private SpectrumUpdateThread spectrumUpdateThread;
    private MeterThread meterThread;
    private MeterUpdateThread meterUpdateThread;
    private Power power;
    private PowerUpdateThread powerUpdateThread;
    
    public static Color selectedColor=Color.YELLOW;
    public static Color unselectedColor;
    public static Color receiveColor=Color.GREEN;
    public static Color transmitColor=Color.RED;
    public static Color disabledColor=Color.LIGHT_GRAY;

    private StartupDialog startupDialog;
    private ConfigureDialog configureDialog;
    
}
