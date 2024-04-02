package org.joker;

public class Test {
    private int num1 = 1;
    public static int NUM1 = 100;

    public int func(int a, int b) {
        return add(a, b);
    }

    public Test(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Test() {
    }

    private String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int add(int a, int b) {
        return a + b + num1;
    }

    public int sub(int a, int b) {
        return a - b - NUM1;
    }
}
