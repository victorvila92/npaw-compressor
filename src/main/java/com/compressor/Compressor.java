package com.compressor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Compressor {

    public static List<Integer> compress(String uncompressed) {
        // Build the dictionary.
        int dictSize = 1000000;
        Map<String,Integer> dictionary = new HashMap<>();
        for (int i = 0; i < 1000000; i++)
            dictionary.put("" + (char)i, i);

        String w = "";
        List<Integer> result = new ArrayList<>();
        for (char c : uncompressed.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc))
                w = wc;
            else {
                result.add(dictionary.get(w));
                // Add wc to the dictionary.
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }

        // Output the code for w.
        if (!w.equals(""))
            result.add(dictionary.get(w));
        return result;
    }

    /** Decompress a list of output ks to a string. */
    public static String decompress(List<Integer> compressed) {
        // Build the dictionary.
        int dictSize = 1000000;
        Map<Integer,String> dictionary = new HashMap<>();
        for (int i = 0; i < 1000000; i++)
            dictionary.put(i, "" + (char)i);

        String w = "" + (char)(int)compressed.remove(0);
        StringBuilder result = new StringBuilder(w);
        for (int k : compressed) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Bad compressed k: " + k);

            result.append(entry);

            // Add w+entry[0] to the dictionary.
            dictionary.put(dictSize++, w + entry.charAt(0));

            w = entry;
        }
        return result.toString();
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

    private static List<Integer> getIntegerArrayFromFile(String inputFile) {

        List<Integer> integerList = new ArrayList<>();

        try(Scanner s = new Scanner(new File(inputFile)).useDelimiter(",")){
            while (s.hasNext()){
                integerList.add(Integer.valueOf(s.next()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return integerList;
    }

    private static String getDataFromFile(String inputFile) {

        StringBuilder sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(inputFile))) {
            stream.forEach(sb::append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static void writeResultToFile(List<Integer> result, String resultFilename) throws IOException {
        Stream<Integer> integerStream = result.stream();
        writeResultToFile(integerStream.mapToInt(x -> x).toArray(), resultFilename);
    }

    private static void writeResultToFile(int[] result, String resultFilename) throws IOException {
        String resultString = Arrays.toString(Arrays.stream(result).mapToObj(String::valueOf).toArray(String[]::new)).replace("[","").replace("]","").replace(" ","");
        Files.write(Paths.get(resultFilename), resultString.getBytes());
    }

    public static void main(String[] args) throws IOException {

        String parameter = args[0];
        //List<Integer> data = getIntegerArrayFromFile(args[1]);
        String resultFile = args[2];

        String data = getDataFromFile(args[1]);

        if (parameter.equals("-c")){
            List<Integer> compressed = compress(data);
            writeResultToFile(compressed, resultFile);
            System.out.println("COMPRESSED");
        }else {
            //String decompressed = decompress(data);
            //writeResultToFile(decompressed, resultFile);
            //System.out.println("DE-COMPRESSED");
        }
    }
}
