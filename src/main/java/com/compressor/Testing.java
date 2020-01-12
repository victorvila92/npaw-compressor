package com.compressor;

import me.lemire.integercompression.differential.IntegratedIntCompressor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Testing {

    public static void main(String[] args) throws IOException {

        String parameter = args[0];
        int[] data = getArrayFromFile(args[1]);
        String resultFile = args[2];

        IntegratedIntCompressor iic = new IntegratedIntCompressor();

        int[] result;

        if (parameter.equals("-c")){
            result = iic.compress(data);
        }else {
            result = iic.uncompress(data);
        }

        writeResultToFile(result, resultFile);
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

        //integerList.stream().filter(i -> Collections.frequency(integerList, i) > 1).collect(Collectors.toSet()).forEach(System.out::println);

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

