package me.ryan.temp;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericExampleTest {

    private void eatFruit(Fruit fruit) {
        System.out.println("eat " + fruit.getPulp());
    }

    private void eatFruit(List<? extends Apple> fruits) {
        fruits.forEach(fruit -> System.out.println("eat " + fruit.getPulp()));
    }

    private void collectFruits(List<? super Apple> fruits) {
        // fruits.add(new Fruit()); Covariant A >= B, f(A) >= f(B)
        fruits.add(new Apple());
        fruits.add(new GreenApple());
    }

    @Test
    public void testPolymorphic() {
        eatFruit(new Fruit());
        eatFruit(new Apple());
        eatFruit(new GreenApple());
    }

    @Test
    public void testCovariant() {
        List<GreenApple> greenApples = Lists.newArrayList(new GreenApple());
        List<Apple> apples = Lists.newArrayList(new Apple());
        List<Fruit> fruits = Lists.newArrayList(new Fruit());

        eatFruit(greenApples);
        eatFruit(apples);
        //eatFruit(fruits);
    }

    @Test
    public void testContravariant() {
        List<GreenApple> greenApples = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();
        List<Fruit> fruits = new ArrayList<>();

        collectFruits(fruits);
        collectFruits(apples);
        // collectFruits(greenApples); Contravariant A >= B, f(B) >= f(A)
    }
}