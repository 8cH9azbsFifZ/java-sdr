#!/bin/sh
# Start-Up for sdr-shell, dttsp, and usbsoftrock.  Oriented towards a Softrock
# RXTX board.
clear
# http://sdr-shell.googlecode.com/svn/tags/PMSDR-1.0/sdr-shell.sh
set -x
export SDR_DEFRATE=96000 #48000 
#export SDR_DEFRATE=48000 
DEV_CAPTURE=hw:1
DEV_PLAYBACK=hw:0
export SDR_NAME=sdr
#export SDR_NAME=sdr-tx # double entry?

## communication over ports
#export SDR_PARMPORT=19005 # sdr-core eats this
#export SDR_SPECPORT=19006  # sdr-core eats this
#export SDR_METERPORT=19007  # sdr-core eats this

## FIFOs
# if communication over fifos
export SDR_PARMPATH=/dev/shm/SDRcommands
export SDR_METERPATH=/dev/shm/SDRmeter
export SDR_SPECPATH=/dev/shm/SDRspectrum
for f in $SDR_PARMPATH $SDR_METERPATH $SDR_SPECPATH; do test -e $f || mkfifo $f; done;

## Clean up
killall -KILL sdr-core
killall -KILL jackd

## Start jackd
echo "Starting jackd"
/usr/bin/jackd -R -P66 -p128 -t2000 -dalsa -r$SDR_DEFRATE -D -C$DEV_CAPTURE -P$DEV_PLAYBACK -s -p 2048&
# start in (R)ealtime with (P)rio 66, (p)ortmax 128, (t)imeout 2000 alsa with parameters...
JPID="$!"
sleep 5
echo "Started jackd"

## Realtime
#rmmod capability
#rmmod commoncap
#modprobe realcap any=1 allcaps=1

## Start DttSP
echo "Starting DttSP"
sdr-core --spectrum --metering&
#/usr/local/bin/sdr-core --spectrum --metering --client-name=${NAME}_RX --buffsize=${JACK_BUFFER} --ringmult=4 --command-port=19001 --spectrum-port=19002 --meter-port=19003
PIDS="$PIDS $!"
sleep 3
echo "Started DttSP"

# connect jack
echo "Connecting jack"
jack_connect $SDR_NAME:ol alsa_pcm:playback_1 || exit 1
jack_connect $SDR_NAME:or alsa_pcm:playback_2 || exit 1
jack_connect alsa_pcm:capture_1 $SDR_NAME:il || exit 1       
jack_connect alsa_pcm:capture_2 $SDR_NAME:ir || exit 1    
jack_lsp -c || exit 1
PIDS="$PIDS $!"
sleep 1
echo "Connected jack"

echo "The following pids have been used: $PIDS"

# Start SDR shell
echo "Starting SDR shell"
#export SDR_MODE=$PWD/hook-mode
#export SDR_BAND=$PWD/hook-band
./sdr-shell
#./sdr-shell --sample_rate=$SDR_DEFRATE --meter-port=$SDR_METERPORT \
#--spectrum-port=$SDR_SPECPORT --rx-command-port=$SDR_PARMPORT


# kill jack clients before killing jack himself.
# this seems to be a cleaner shutdown than everything at once

kill $PIDS
sleep 2
kill $JPID
wait


killall sdr-core
killall jackd
