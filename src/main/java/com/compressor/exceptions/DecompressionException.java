package com.compressor.exceptions;

public class DecompressionException extends Exception{
    public DecompressionException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}