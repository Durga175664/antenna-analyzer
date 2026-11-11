package com.antenna.model; 

  

/** One VNA measurement sample: frequency in GHz and S11 in dB. */ 

public record DataPoint(double frequencyGHz, double s11Db) { } 