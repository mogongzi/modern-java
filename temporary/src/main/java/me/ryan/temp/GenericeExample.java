package me.ryan.temp;

public class GenericeExample {

    public static void main(String[] args) {

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