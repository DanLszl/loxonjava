package com.loxon.javachallenge2017.pack.utility;

import com.sun.java.swing.plaf.windows.WindowsTreeUI;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Util {
    public static Map<Integer, Double> normalizeValues(Map<Integer, Double> values) {
        Double min;
        Double max;

        min = values.entrySet().stream()
                .mapToDouble(entry -> entry.getValue())
                .min()
                .getAsDouble();

        max = values.entrySet().stream()
                .mapToDouble(entry -> entry.getValue())
                .max()
                .getAsDouble();

        return values.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> entry.getKey(),
                                entry -> (entry.getValue() - min) / (max-min)
                        )
                );
    }

    public static Map<Integer, Double> normalizeRadiuses(Map<Integer, Double> values) {
        Double max = values.entrySet().stream()
                .mapToDouble(entry -> entry.getValue())
                .max()
                .getAsDouble();

        return values.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue() / max
                ));
    }
}
