package me.ryan.lucene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Vsm {

    public static double calCosSim(Map<String, Double> v1, Map<String, Double> v2) {
        double scalar = 0.0, norm1 = 0.0, norm2 = 0.0, similarity = 0.0;

        var v1Keys = v1.keySet();
        var v2Keys = v2.keySet();
        var both = new HashSet<>(v1Keys);

        both.retainAll(v2Keys);
        System.out.println(both);

        for (var word : both) {
            scalar += v1.get(word) * v2.get(word);
        }

        for (var word : v1.keySet()) {
            norm1 += Math.pow(v1.get(word), 2);
        }

        for (var word : v2.keySet()) {
            norm2 += Math.pow(v2.get(word), 2);
        }

        similarity = scalar / Math.sqrt(norm1 * norm2);

        System.out.println("scalar: " + scalar);
        System.out.println("norm1: " + norm1);
        System.out.println("norm2: " + norm2);
        return similarity;
    }

    public static void main(String[] args) {
        Map<String, Double> m1= new HashMap<>();
        m1.put("Hello", 1.0);
        m1.put("css", 2.0);
        m1.put("Lucene", 3.0);

        Map<String, Double> m2 = new HashMap<>();
        m2.put("Hello", 1.0);
        m2.put("Word", 2.0);
        m2.put("Hadoop", 3.0);
        m2.put("java", 4.0);
        m2.put("html", 1.0);
        m2.put("css", 2.0);
        System.out.println("similarity: " + calCosSim(m1, m2));
    }
}
