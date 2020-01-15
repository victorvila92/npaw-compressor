package com.compressor.exceptions;

public class CompressionException extends Exception{
    public CompressionException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}