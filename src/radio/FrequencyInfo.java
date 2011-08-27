/*
 * FrequencyInfo.java
 *
 * Created on September 1, 2007, 10:42 AM
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

public class FrequencyInfo {
    
    /** Creates a new instance of FrequencyInfo */
    public FrequencyInfo() {
    }
    
    public String getFrequencyInfo(double frequency) {
        String result="Out of band";

        int freq=(int)((frequency*1000000.0)+0.5);
        double f=(double)freq/1000000.0;
        Info info;
        for(int i=0;i<frequencyInfo.length;i++) {
            info=frequencyInfo[i];
            //System.err.println("F="+f+" min="+info.getMinFrequency()+" max="+info.getMaxFrequency());
            if(f>=info.getMinFrequency() && f<=info.getMaxFrequency()) {
                return info.getInfo();
            }
        }
        
        return result;
    }
    
    public Bands getBand(double frequency) {
        Bands band=Bands.BAND_15;
        HamBand hamBand;
        for(int i=0;i<hamBands.length;i++) {
            hamBand=hamBands[i];
            if(hamBand.isInBand(frequency)) {
                band=hamBand.getBand();
            }
        }    
        return band;
    }
    
    Info[] frequencyInfo= {
        new Info(1.800000, 1.809999, "160M CW/Digital Modes", true),
        new Info(1.810000, 1.810000, "160M CW QRP", true),
        new Info(1.810001, 1.842999, "160M CW", true),
        new Info(1.843000, 1.909999, "160M SSB/SSTV/Wide Band", true),
        new Info(1.910000, 1.910000, "160M SSB QRP", true),
        new Info(1.910001, 1.994999, "160M SSB/SSTV/Wide Band", true),
        new Info(1.995000, 1.999999, "160M Experimental", true),

        new Info(3.500000, 3.524999, "80M Extra CW", true),
        new Info(3.525000, 3.579999, "80M CW", true),
        new Info(3.580000, 3.589999, "80M RTTY", true),
        new Info(3.590000, 3.590000, "80M RTTY DX", true),
        new Info(3.590001, 3.599999, "80M RTTY", true),
        new Info(3.600000, 3.699999, "75M Extra SSB", true),
        new Info(3.700000, 3.789999, "75M Ext/Adv SSB", true),
        new Info(3.790000, 3.799999, "75M Ext/Adv DX Window", true),
        new Info(3.800000, 3.844999, "75M SSB", true),
        new Info(3.845000, 3.845000, "75M SSTV", true),
        new Info(3.845001, 3.884999, "75M SSB", true),
        new Info(3.885000, 3.885000, "75M AM Calling Frequency", true),
        new Info(3.885001, 3.999999, "75M SSB", true),

        new Info(5.330500, 5.330500, "60M Channel 1", true),
        new Info(5.346500, 5.346500, "60M Channel 2", true),
        new Info(5.366500, 5.366500, "60M Channel 3", true),
        new Info(5.371500, 5.371500, "60M Channel 4", true),
        new Info(5.403500, 5.403500, "60M Channel 5", true),

        new Info(7.000000, 7.024999, "40M Extra CW", true),
        new Info(7.025000, 7.039999, "40M CW", true),
        new Info(7.040000, 7.040000, "40M RTTY DX", true),
        new Info(7.040001, 7.099999, "40M RTTY", true),
        new Info(7.100000, 7.124999, "40M CW", true),
        new Info(7.125000, 7.170999, "40M Ext/Adv SSB", true),
        new Info(7.171000, 7.171000, "40M SSTV", true),
        new Info(7.171001, 7.174999, "40M Ext/Adv SSB", true),
        new Info(7.175000, 7.289999, "40M SSB", true),
        new Info(7.290000, 7.290000, "40M AM Calling Frequency", true),
        new Info(7.290001, 7.299999, "40M SSB", true),

        new Info(10.100000, 10.129999, "30M CW", true),
        new Info(10.130000, 10.139999, "30M RTTY", true),
        new Info(10.140000, 10.149999, "30M Packet", true),

        new Info(14.000000, 14.024999, "20M Extra CW", true),
        new Info(14.025000, 14.069999, "20M CW", true),
        new Info(14.070000, 14.094999, "20M RTTY", true),
        new Info(14.095000, 14.099499, "20M Packet", true),
        new Info(14.099500, 14.099999, "20M CW", true),
        new Info(14.100000, 14.100000, "20M NCDXF Beacons", true),
        new Info(14.100001, 14.100499, "20M CW", true),
        new Info(14.100500, 14.111999, "20M Packet", true),
        new Info(14.112000, 14.149999, "20M CW", true),
        new Info(14.150000, 14.174999, "20M Extra SSB", true),
        new Info(14.175000, 14.224999, "20M Ext/Adv SSB", true),
        new Info(14.225000, 14.229999, "20M SSB", true),
        new Info(14.230000, 14.230000, "20M SSTV", true),
        new Info(14.230000, 14.285999, "20M SSB", true),
        new Info(14.286000, 14.286000, "20M AM Calling Frequency", true),
        new Info(14.286001, 14.349999, "20M SSB", true),

        new Info(18.068000, 18.099999, "17M CW", true),
        new Info(18.100000, 18.104999, "17M RTTY", true),
        new Info(18.105000, 18.109999, "17M Packet", true),
        new Info(18.110000, 18.110000, "17M NCDXF Beacons",              true),
        new Info(18.110001, 18.167999, "17M SSB",                        true),

        new Info(21.000000, 21.024999, "15M Extra CW",                   true),
        new Info(21.025000, 21.069999, "15M CW",                         true),
        new Info(21.070000, 21.099999, "15M RTTY",                       true),
        new Info(21.100000, 21.109999, "15M Packet",                     true),
        new Info(21.110000, 21.149999, "15M CW",                         true),
        new Info(21.150000, 21.150000, "15M NCDXF Beacons",              true),
        new Info(21.150001, 21.199999, "15M CW",                         true),
        new Info(21.200000, 21.224999, "15M Extra SSB",                  true),
        new Info(21.225000, 21.274999, "15M Ext/Adv SSB",                true),
        new Info(21.275000, 21.339999, "15M SSB",                        true),
        new Info(21.340000, 21.340000, "15M SSTV",                       true),
        new Info(21.340001, 21.449999, "15M SSB",                        true),

        new Info(24.890000, 24.919999, "12M CW",                                 true),
        new Info(24.920000, 24.924999, "12M RTTY",                               true),
        new Info(24.925000, 24.929999, "12M Packet",                             true),
        new Info(24.930000, 24.930000, "12M NCDXF Beacons",              true),
        new Info(24.930001, 24.989999, "12M SSB Wideband",               true),

        new Info(28.000000, 28.069999, "10M CW",                                 true),
        new Info(28.070000, 28.149999, "10M RTTY",                               true),
        new Info(28.150000, 28.199999, "10M CW",                                 true),
        new Info(28.200000, 28.200000, "10M NCDXF Beacons",              true),
        new Info(28.200001, 28.299999, "10M Beacons",                    true),
        new Info(28.300000, 28.679999, "10M SSB",                                true),
        new Info(28.680000, 28.680000, "10M SSTV",                               true),
        new Info(28.680001, 28.999999, "10M SSB",                                true),
        new Info(29.000000, 29.199999, "10M AM",                                 true),
        new Info(29.200000, 29.299999, "10M SSB",                                true),
        new Info(29.300000, 29.509999, "10M Satellite Downlinks",       true),
        new Info(29.510000, 29.519999, "10M Deadband",                   true),
        new Info(29.520000, 29.589999, "10M Repeater Inputs",           true),
        new Info(29.590000, 29.599999, "10M Deadband",                   true),
        new Info(29.600000, 29.600000, "10M FM Simplex",                 true),
        new Info(29.600001, 29.609999, "10M Deadband",                   true),
        new Info(29.610000, 29.699999, "10M Repeater Outputs",          true),

        new Info(50.000000, 50.059999, "6M CW",                                  true),
        new Info(50.060000, 50.079999, "6M Beacon Sub-Band",             true),
        new Info(50.080000, 50.099999, "6M CW",                                  true),
        new Info(50.100000, 50.124999, "6M DX Window",                   true),
        new Info(50.125000, 50.125000, "6M Calling Frequency",          true),
        new Info(50.125001, 50.299999, "6M SSB",                                 true),
        new Info(50.300000, 50.599999, "6M All Modes",                   true),
        new Info(50.600000, 50.619999, "6M Non Voice",                   true),
        new Info(50.620000, 50.620000, "6M Digital Packet Calling",     true),
        new Info(50.620001, 50.799999, "6M Non Voice",                   true),
        new Info(50.800000, 50.999999, "6M RC",                                  true),
        new Info(51.000000, 51.099999, "6M Pacific DX Window",          true),
        new Info(51.100000, 51.119999, "6M Deadband",                    true),
        new Info(51.120000, 51.179999, "6M Digital Repeater Inputs",    true),
        new Info(51.180000, 51.479999, "6M Repeater Inputs",             true),
        new Info(51.480000, 51.619999, "6M Deadband",                    true),
        new Info(51.620000, 51.679999, "6M Digital Repeater Outputs",   true),
        new Info(51.680000, 51.979999, "6M Repeater Outputs",           true),
        new Info(51.980000, 51.999999, "6M Deadband",                    true),
        new Info(52.000000, 52.019999, "6M Repeater Inputs",             true),
        new Info(52.020000, 52.020000, "6M FM Simplex",                  true),
        new Info(52.020001, 52.039999, "6M Repeater Inputs",             true),
        new Info(52.040000, 52.040000, "6M FM Simplex",                  true),
        new Info(52.040001, 52.479999, "6M Repeater Inputs",             true),
        new Info(52.480000, 52.499999, "6M Deadband",                    true),
        new Info(52.500000, 52.524999, "6M Repeater Outputs",           true),
        new Info(52.525000, 52.525000, "6M Primary FM Simplex",         true),
        new Info(52.525001, 52.539999, "6M Deadband",                    true),
        new Info(52.540000, 52.540000, "6M Secondary FM Simplex",       true),
        new Info(52.540001, 52.979999, "6M Repeater Outputs",           true),
        new Info(52.980000, 52.999999, "6M Deadbands",                   true),
        new Info(53.000000, 53.000000, "6M Remote Base FM Spx",          true),
        new Info(53.000001, 53.019999, "6M Repeater Inputs",             true),
        new Info(53.020000, 53.020000, "6M FM Simplex",                  true),
        new Info(53.020001, 53.479999, "6M Repeater Inputs",             true),
        new Info(53.480000, 53.499999, "6M Deadband",                    true),
        new Info(53.500000, 53.519999, "6M Repeater Outputs",            true),
        new Info(53.520000, 53.520000, "6M FM Simplex",                  true),
        new Info(53.520001, 53.899999, "6M Repeater Outputs",            true),
        new Info(53.900000, 53.900000, "6M FM Simplex",                  true),
        new Info(53.900010, 53.979999, "6M Repeater Outputs",            true),
        new Info(53.980000, 53.999999, "6M Deadband",                    true),

        new Info(144.000000, 144.099999, "2M CW",                        true),
        new Info(144.100000, 144.199999, "2M CW/SSB",                    true),
        new Info(144.200000, 144.200000, "2M Calling",                   true),
        new Info(144.200001, 144.274999, "2M CW/SSB",                    true),
        new Info(144.275000, 144.299999, "2M Beacon Sub-Band",           true),
        new Info(144.300000, 144.499999, "2M Satellite",                 true),
        new Info(144.500000, 144.599999, "2M Linear Translator Inputs",  true),
        new Info(144.600000, 144.899999, "2M FM Repeater",               true),
        new Info(144.900000, 145.199999, "2M FM Simplex",                true),
        new Info(145.200000, 145.499999, "2M FM Repeater",               true),
        new Info(145.500000, 145.799999, "2M FM Simplex",                true),
        new Info(145.800000, 145.999999, "2M Satellite",                 true),
        new Info(146.000000, 146.399999, "2M FM Repeater",               true),
        new Info(146.400000, 146.609999, "2M FM Simplex",                true),
        new Info(146.610000, 147.389999, "2M FM Repeater",               true),
        new Info(147.390000, 147.599999, "2M FM Simplex",                true),
        new Info(147.600000, 147.999999, "2M FM Repeater",               true)
    };
    
    HamBand[] hamBands= {
        new HamBand(1.8,2.0,Bands.BAND_1),
        new HamBand(3.5,4.0,Bands.BAND_2),
        new HamBand(5.3305,5.4035,Bands.BAND_3),
        new HamBand(7.0,7.3,Bands.BAND_4),
        new HamBand(10.1,10.15,Bands.BAND_5),
        new HamBand(14.0,14.35,Bands.BAND_6),
        new HamBand(18.068,18.168,Bands.BAND_7),
        new HamBand(21.0,21.45,Bands.BAND_8),
        new HamBand(24.89,24.99,Bands.BAND_9),
        new HamBand(28.0,29.7,Bands.BAND_10),
        new HamBand(50.0,54.0,Bands.BAND_11),
        new HamBand(144.0,148.0,Bands.BAND_11)
    };
    
    class Info {
        
        Info(double minFrequency,double maxFrequency,String info,boolean transmit) {
            this.minFrequency=minFrequency;
            this.maxFrequency=maxFrequency;
            this.info=info;
            this.transmit=transmit;
        }
        
        public void setMinFrequency(double minFrequency) {
            this.minFrequency=minFrequency;
        }
        
        public void setMaxFrequency(double maxFrequency) {
            this.maxFrequency=maxFrequency;
        }
        
        public void setInfo(String info) {
            this.info=info;
        }
        
        public double getMinFrequency() {
            return minFrequency;
        }
        
        public double getMaxFrequency() {
            return maxFrequency;
        }
        
        public String getInfo() {
            return info;
        }
        
        public void setTransmit(boolean transmit) {
            this.transmit=transmit;
        }
        
        public boolean getTransmit() {
            return transmit;
        }
        
        private double minFrequency;
        private double maxFrequency;
        private String info;
        private boolean transmit;
    }
    
    class HamBand {
        
        HamBand(double low,double high,Bands band) {
            this.low=low;
            this.high=high;
            this.band=band;
        }
        
        public boolean isInBand(double frequency) {
            return frequency>=low && frequency<=high;
        }
        
        public Bands getBand() {
            return band;
        }
        
        private double low;
        private double high;
        private Bands band;
    }
}

