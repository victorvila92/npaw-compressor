package com.compressor;

import com.google.common.base.Joiner;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Testing {

    private static final Integer TIMES = 800;
    private static HashMap<Integer, Integer> repeatedSet = new HashMap<>();
    private static HashMap<Integer, Integer> consSet = new HashMap<>();
    private static Set<Integer> dataSet = new HashSet<>();

    public static void main(String[] args) {
        String parameter = args[0];
        String resultFile = args[2];

        int[] data = getArrayFromFile(args[1]);

        Arrays.parallelSort(data);
        dataSet = Arrays.stream(data).boxed().collect(Collectors.toSet());

        List<Callable> tasks = new ArrayList<>(Collections.singletonList(getRepeatedNumbersTask(data)));
        for (int i = 1; i <= TIMES; i++){
            tasks.add(getConsecutiveNumbersTask(data, i));
        }

        try {
            writeResultToFile(dataSet, repeatedSet, consSet, resultFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("END");
    }

    static Callable getRepeatedNumbersTask(int[] datasetArray) {
        int previous = 0;
        for (int number : datasetArray) {
            if (previous == 0){
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

        int previous = 0;
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

    private static void writeResultToFile(Set<Integer> dataSet, HashMap<Integer, Integer> repeatedSet, HashMap<Integer, Integer> consSet, String resultFilename) throws IOException {
        StringBuilder sb = new StringBuilder();
        String dataSetString = dataSet.stream().map(String::valueOf).collect(Collectors.joining(","));
        String repeatedString = Joiner.on(",").withKeyValueSeparator(",").join(repeatedSet);
        String consString = Joiner.on(",").withKeyValueSeparator(",").join(consSet);
        sb.append(dataSetString).append(repeatedString).append(consString);
        Files.write(Paths.get(resultFilename), Collections.singleton(sb.toString()));
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
}

