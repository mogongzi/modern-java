package me.ryan.temp;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericExample {

    public static void main(String[] args) {
        // Arrays are covariant in Java
        Number[] numbers = new Number[3];
        numbers[0] = 2;
        numbers[1] = 1.2F;
        numbers[2] = 2.02D;
        Arrays.stream(numbers).forEach(System.out::println);

        List<Number> source = Lists.newArrayList(2, 1.4f, 2.0D);
        List<Number> target = new ArrayList<>();
        copy(source, target);
        target.forEach(System.out::println);
    }

    public static void copy(List<? extends Number> source, List<? super Number> target) {
        for(Number number : source) {
            target.add(number);
        }
    }
}

class Fruit {
    public String getPulp() {
        return "general pulp";
    }
}

class Apple extends Fruit {
    @Override
    public String getPulp() {
        return "apple pulp";
    }
}

class GreenApple extends Apple {
    @Override
    public String getPulp() {
        return "unripe apple pulp";
    }
}