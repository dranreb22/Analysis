package Affinity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Ex {
    public static void main(String[] args) throws IOException {
        File sampleFile = new File("./src/sampleFile.csv");
        BufferedReader reader;

        if (!sampleFile.exists()) {
            System.out.println("File does not exist");
            ;
            System.exit(1);
        }

        reader = new BufferedReader((new FileReader(sampleFile)));
        String line = null;
        int totalTransactions = 0;

        String[] features = reader.readLine().split(",");
        int numberOfFeatures = features.length;

        List<int[]> sampleResults = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            sampleResults.add(Arrays.stream(line.split(","))
                    .mapToInt(Integer::parseInt).toArray());
            totalTransactions++;
        }

        reader.close();

        Map<String, Integer> fullResults = new HashMap<>();
        Map<HashSet<String>, Integer> validResults = new HashMap<>();

        for (int[] sample : sampleResults) {
            for (int premise = 0; premise < numberOfFeatures; premise++) {
                if (sample[premise] == 1) {
                    fullResults.put(features[premise], fullResults.getOrDefault(features[premise], 0) + 1);
                }
                for (int conclusion = 0; conclusion < numberOfFeatures; conclusion++) {
                    if (conclusion == premise) {
                        continue;
                    }

                    if (sample[conclusion] == 1) {
                        validResults.put(new HashSet<String>(Arrays.asList(features[premise], features[conclusion])),
                                validResults.getOrDefault(new HashSet<String>(Arrays.asList(features[premise], features[conclusion])), 0) + 1);
                    }
                }
            }
        }
        for (HashSet<String> featureSet : validResults.keySet()) {
            List<String> featureList = featureSet.stream().collect(Collectors.toList());

            double confidence = (double) fullResults.get(featureList.get(0)) / validResults.get(featureSet);
            double support = (double) validResults.get(featureSet) / totalTransactions;

            System.out.printf("We show a confidence of %f that a person who"
                            + " bought %s will also buy %s%n and a support of %f that"
                            + " a person will buy these items together at all.%n",
                    confidence, featureList.get(0), featureList.get(1), support);
        }
    }
}