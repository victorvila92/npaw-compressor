package com.compressor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Testing {

    private static final int CHUNK_SIZE = 16384;
    
    public static void main(String[] args) throws IOException {

        String parameter = args[0];
        int[] data = getArrayFromFile(args[1]);
        String resultFile = args[2];

        Arrays.parallelSort(data);

/*        IntegratedIntegerCODEC regularCodec = new IntegratedBinaryPacking();
        IntegratedVariableByte integratedVariableByte = new IntegratedVariableByte();
        IntegratedIntegerCODEC lastCodec = new IntegratedComposition(regularCodec, integratedVariableByte);

        if (parameter.equals("-c")){
            int[] compressed = new int[data.length];

            IntWrapper inputOffset = new IntWrapper(0);
            IntWrapper outputOffset = new IntWrapper(0);
            for (int k = 0; k < data.length / CHUNK_SIZE; ++k){
                regularCodec.compress(data, inputOffset, CHUNK_SIZE, compressed, outputOffset);
            }
            lastCodec.compress(data, inputOffset, data.length % CHUNK_SIZE, compressed, outputOffset);
            compressed = Arrays.copyOf(compressed, outputOffset.intValue());
            writeResultToFile(compressed, resultFile);

        }else {
            int[] recovered = new int[CHUNK_SIZE];

            IntWrapper compOff = new IntWrapper(0);
            IntWrapper recOffset;

            while (compOff.get() < data.length) {
                recOffset = new IntWrapper(0);
                regularCodec.uncompress(data, compOff, data.length - compOff.get(), recovered, recOffset);

                if (recOffset.get() < CHUNK_SIZE) {
                    integratedVariableByte.uncompress(data, compOff, data.length - compOff.get(), recovered, recOffset);
                }
            }
            writeResultToFile(recovered, resultFile);
        }*/
        System.out.println("END");
    }

    private static void writeResultToFile(int[] result, String resultFilename) throws IOException {
        String resultString = Arrays.toString(Arrays.stream(result).mapToObj(String::valueOf).toArray(String[]::new)).replace("[","").replace("]","").replace(" ","");
        Files.write(Paths.get(resultFilename), resultString.getBytes());
    }

    private static int[] getArrayFromFile(String inputFile) {

        List<Integer> integerList = new ArrayList<>();

        try(Scanner s = new Scanner(new File(inputFile)).useDelimiter(",")){
            while (s.hasNext()){
                integerList.add(Integer.valueOf(s.next()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Stream<Integer> integerStream = integerList.stream();
        return integerStream.mapToInt(x -> x).toArray();
    }

    private synchronized  Set<Integer> findDuplicatesFromList(List<Integer> integerList){
        Set<Integer> tempSet = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();

        integerList.forEach(number -> {
            if (!tempSet.add(number)) duplicates.add(number);
        });
        return duplicates;
    }
}

