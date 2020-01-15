package com.compressor.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class CompressorOperation {

    public CompressorOperation(){}

    public static int[] getArrayFromFile(String inputFile) throws FileNotFoundException {
        List<Integer> integerList = new ArrayList<>();
        try(Scanner s = new Scanner(new File(inputFile)).useDelimiter(",")){
            while (s.hasNext()){
                integerList.add(Integer.valueOf(s.next().replace("\n","")));
            }
        }
        Stream<Integer> integerStream = integerList.stream();
        return integerStream.mapToInt(x -> x).toArray();
    }

    public static void writeArrayToFile(int[] originalData, String resultFile) throws IOException {
        Files.write(Paths.get(resultFile), Collections.singleton(Arrays.toString(originalData).trim().replace("]","").replace("[","")));
    }

    public static int[] getIntegerArrayFromByteArray(byte[] result){
        IntBuffer intBuf = ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return array;
    }
}
