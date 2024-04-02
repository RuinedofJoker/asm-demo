package org.joker.writer;

import jdk.internal.org.objectweb.asm.ClassWriter;
import org.joker.MyClassLoader;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class TestWriter {
    public static void main(String[] args) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(
                V1_8, // java版本
                ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, // public abstract interface
                "org/joker/Comparable", // 你的类的内部名
                null, // 泛型
                "java/lang/Object", // 继承的类的内部名
                new String[] {} // 实现的接口(继承的接口)
        );

        // 定义一个变量 public static final int LESS = -1;
        cw.visitField(
                ACC_PUBLIC + ACC_STATIC + ACC_FINAL, // public static final
                "LESS", // 变量名
                "I", // int
                null, // 泛型
                new Integer(-1) // 给变量赋值，这里如果不是一个 static final 的常量这个值必须为 null
        ).visitEnd();

        // 定义一个抽象方法 public abstract int compareTo(Object o);
        cw.visitMethod(
                ACC_PUBLIC + ACC_ABSTRACT, // public abstract
                "compareTo", // 方法名
                "(Ljava/lang/Object;)I", // 方法描述符
                null, // 泛型
                null // 抛出的异常，一个String数组
        ).visitEnd();

        byte[] comparableBT = cw.toByteArray();
        // 这里可以使用一个 ClassLoader 来加载这个类
        Class aClass = new MyClassLoader().defineClass("org.joker.Comparable", comparableBT);
        System.out.println(aClass);
    }
}
