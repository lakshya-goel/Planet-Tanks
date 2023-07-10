package com.mygdx.gamemech;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;

class MyTest {
    final TankStars game;

    public MyTest(TankStars game) {
        this.game = game;
    }

    @Test
    public void test1() {
        GameMech t = new GameMech(game);
        float t1 = t.moveLeft(52);
        assertEquals((int)t1,(int)t.dat);
    }

    @Test
    public void test2() {
        GameMech t = new GameMech(game);
        float t2 = t.moveLeft(50);
        assertEquals((int)t2,49);
    }
    @Test
    public void test3() {
        GameMech t = new GameMech(game);
        float t1 = t.moveRight(50);
        assertEquals((int)t1,(int)t.dat);
    }
    @Test
    public void test4() {
        GameMech t = new GameMech(game);
        float t1 = t.moveRight(52);
        assertEquals((int)t1,(int)53);
    }
}

public class TestRunner {
    public static void main(String[] args) {
        Result result= JUnitCore.runClasses(MyTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}