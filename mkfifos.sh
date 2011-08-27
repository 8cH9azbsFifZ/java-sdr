#!/bin/bash
hwcmdpath="fifos/HWcommands"
hwstatuspath="fifos/HWstatus"

if [ ! -p $hwcmdpath ]; then
  mkfifo $hwcmdpath
fi
if [ ! -p $hwstatuspath ]; then
  mkfifo $hwstatuspath
fi

rxcmdpath="fifos/RXcommands"
rxmtrpath="fifos/RXmeter"
rxspecpath="fifos/RXspectrum"

if [ ! -p $rxcmdpath ]; then
  mkfifo $rxcmdpath
fi
if [ ! -p $rxmtrpath ]; then
  mkfifo $rxmtrpath
fi
if [ ! -p $rxspecpath ]; then
  mkfifo $rxspecpath
fi

txcmdpath="fifos/TXcommands"
txmtrpath="fifos/TXmeter"
txspecpath="fifos/TXspectrum"

if [ ! -p $txcmdpath ]; then
  mkfifo $txcmdpath
fi
if [ ! -p $txmtrpath ]; then
  mkfifo $txmtrpath
fi
if [ ! -p $txspecpath ]; then
  mkfifo $txspecpath
fi
