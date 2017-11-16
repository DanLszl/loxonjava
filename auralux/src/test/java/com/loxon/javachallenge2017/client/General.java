package com.loxon.javachallenge2017.client;

import java.util.ArrayList;
import java.util.List;

public class General {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();

        int sum = list.stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);
    }
}
