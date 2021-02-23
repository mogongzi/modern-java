package me.ryan.temp;

import com.google.common.collect.Lists;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class GenericeExampleTest extends TestCase {

    private void eatFruit(Fruit fruit) {
        System.out.println("eat " + fruit.getPulp());
    }

    private void eatFruit(List<? extends Apple> fruits) {
        fruits.forEach(fruit -> System.out.println("eat " + fruit.getPulp()));
    }

    private void collectFruits(List<? super Apple> fruits) {
        //fruits.add(new Fruit());
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
        List<GreenApple> greenApples = Lists.newArrayList();
        List<Apple> apples = Lists.newArrayList();
        List<Fruit> fruits = Lists.newArrayList();

        collectFruits(fruits);
        collectFruits(apples);
        collectFruits(greenApples);
    }
}