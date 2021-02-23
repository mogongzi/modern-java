package me.ryan.temp;

public class GenericExample {

    public static void main(String[] args) {
        // Arrays are covariant in Java
        Number[] numbers = new Number[3];
        numbers[0] = 2;
        numbers[1] = 1.2F;
        numbers[2] = 2.02D;
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