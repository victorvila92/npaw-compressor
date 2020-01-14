package com.compressor;

import com.google.common.base.Joiner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompressorEngine {

    private static final Integer TIMES = 800;
    private static HashMap<Integer, Integer> repeatedSet = new HashMap<>();
    private static HashMap<Integer, Integer> consSet = new HashMap<>();
    private static Set<Integer> dataSet = new HashSet<>();

    public static void main(String[] args) {
        String initialFile = args[1];
        String resultFile = args[2];

        if(shouldCompress(args[0])){
            int[] data = getArrayFromFile(initialFile);
            Arrays.parallelSort(data);
            compress(data, resultFile);
        }else {
            List<Integer> data = getListFromFile(initialFile);
            decompress(data, resultFile);
        }
        System.out.println("END");
    }

    static void decompress(List<Integer> datasetList, String resultFile){

        List<Integer> integerList = new ArrayList<>();
        int previous = -1;
        for (int number : datasetList) {
            if(number <= TIMES && number >= 0){
                if(number == 0){
                    integerList.add(previous);
                    integerList.add(previous);
                }else {
                    integerList.add(previous + number);
                }
            }else {
                integerList.add(number);
            }
            previous = number;
        }
        try {
            writeDescompressedResultToFile(integerList, resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("OK");
    }

    static void compress(int[] data, String resultFile){
        dataSet = Arrays.stream(data).boxed().collect(Collectors.toSet());

        List<Callable> tasks = new ArrayList<>(Collections.singletonList(getRepeatedNumbersTask(data)));
        for (int i = 1; i <= TIMES; i++){
            tasks.add(getConsecutiveNumbersTask(data, i));
        }

        try {
            writeCompressedResultToFile(dataSet, repeatedSet, consSet, resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean shouldCompress(String parameter){
        return parameter.equals("-c");
    }

    static Callable getRepeatedNumbersTask(int[] datasetArray) {
        int previous = -1;
        for (int number : datasetArray) {
            if (previous == -1){
                previous = number;
            }else if(previous == number) {
                repeatedSet.put(number, 0);
            } else {
                previous = number;
            }
        }
        return () -> repeatedSet;
    }

    static Callable getConsecutiveNumbersTask(int[] datasetArray, int consecutiveNumber) {

        int previous = -1;
        Set<Integer> tempSet = new HashSet<>();

        for (Integer number : datasetArray){
            if(previous + consecutiveNumber == number) {
                consSet.putIfAbsent(previous, consecutiveNumber);
                tempSet.add(previous);
                tempSet.add(number);
            }
            previous = number;
        }
        dataSet.removeAll(tempSet);
        return () -> consSet;
    }

    private static void writeDescompressedResultToFile(List<Integer> dataSet, String resultFilename) throws IOException {

    }

    private static void writeCompressedResultToFile(Set<Integer> dataSet, HashMap<Integer, Integer> repeatedSet, HashMap<Integer, Integer> consSet, String resultFilename) throws IOException {
        StringBuilder sb = new StringBuilder();
        String dataSetString = dataSet.stream().map(String::valueOf).collect(Collectors.joining(","));
        String repeatedString = Joiner.on(",").withKeyValueSeparator(",").join(repeatedSet);
        String consString = Joiner.on(",").withKeyValueSeparator(",").join(consSet);
        sb.append(dataSetString).append(",").append(repeatedString).append(",").append(consString);
        Files.write(Paths.get(resultFilename), Collections.singleton(sb.toString()));
    }

    private static List<Integer> getListFromFile(String inputFile) {

        List<Integer> integerList = new ArrayList<>();

        try(Scanner s = new Scanner(new File(inputFile)).useDelimiter(",")){
            while (s.hasNext()){
                integerList.add(Integer.valueOf(s.next().replace("\n","")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return integerList;
    }

    private static int[] getArrayFromFile(String inputFile) {

        List<Integer> integerList = new ArrayList<>();

        try(Scanner s = new Scanner(new File(inputFile)).useDelimiter(",")){
            while (s.hasNext()){
                integerList.add(Integer.valueOf(s.next().replace("\n","")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Stream<Integer> integerStream = integerList.stream();
        return integerStream.mapToInt(x -> x).toArray();
    }
}

