package ru.otus.project.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static Integer nextInt(Integer bound) {
        return RANDOM.nextInt(bound);
    }

    public static List<Integer> getManyRandomFrom(Integer maxValue) {
        int countIndexes = nextInt(maxValue / 2) + 1;
        return RANDOM.ints(countIndexes, 1, maxValue + 1).distinct().boxed().toList();
    }

    public static <T> List<T> getManyRandomFrom(List<T> list) {
        List<T> listForShuffle = new ArrayList<>(list);
        Collections.shuffle(listForShuffle);
        int countIndexes = nextInt((list.size() + 1) / 2) + 1;
        return listForShuffle.subList(0, countIndexes);
    }

    public static <T> T getOneRandomFrom(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}
