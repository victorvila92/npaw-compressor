package com.compressor;

import com.compressor.exceptions.CommandLineArgumentException;
import com.compressor.exceptions.CompressionException;
import com.compressor.exceptions.DecompressionException;
import org.xerial.snappy.Snappy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.compressor.operations.CompressorOperation.*;

public class Compressor {

    private static final Integer ARGUMENT_SIZE = 3;
    private static final Logger LOGGER = Logger.getLogger(Compressor.class.getName());

    public static void main(String[] args) throws Exception {

        if (argumentsAreWrong(args)){
            throw new CommandLineArgumentException("CommandLineArgument ERROR. Please follow the command: 'java -jar compressor.jar -[c/d] initialFile resultFile'. Does the initial file exist?");
        }

        String initialFile = args[1];
        String resultFile = args[2];
        byte[] result;

        if(shouldCompress(args[0])) {
            int[] data = getArrayFromFile(initialFile);
            try {
                result = Snappy.compress(data);
                Files.write(Paths.get(resultFile), result);
                LOGGER.log(Level.INFO, "Successfully compressed!");
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, ex.toString(), ex);
                throw new CompressionException("Error while compressing.", ex);
            }
        }else {
            try {
                result = Snappy.uncompress(Files.readAllBytes(Paths.get(initialFile)));
                int[] originalData = getIntegerArrayFromByteArray(result);
                writeArrayToFile(originalData, resultFile);
                LOGGER.log(Level.INFO, "Successfully decompressed!");
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, ex.toString(), ex);
                throw new DecompressionException("Error while compressing.", ex);
            }
        }
    }

    private static boolean argumentsAreWrong(String[] args) {
        return args.length != ARGUMENT_SIZE || (!args[0].equals("-c") && !args[0].equals("-d") || !new File(args[1]).exists());
    }

    private static boolean shouldCompress(String parameter){
        return parameter.equals("-c");
    }
}
