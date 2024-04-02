package org.joker.transform;

public class TestTransform {
    public TestTransform(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public TestTransform() {
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
        return a + b;
    }
}
