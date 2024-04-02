package org.joker.reader;

import jdk.internal.org.objectweb.asm.ClassReader;

import java.io.IOException;

public class TestReader {
    public static void main(String[] args) throws Exception {
        ClassPrinter cp = new ClassPrinter();

        ClassReader cr = new ClassReader(
                "java.lang.Runnable" // 你需要访问的类
        );
        // ClassReader cr = new ClassReader(Runnable.class.getClassLoader().getResourceAsStream("java.lang.Runnable"));
        // 还可以通过 byte[] 来获取 ClassReader

        // 访问类
        cr.accept(
                cp, // 你的 ClassVisitor 访问者实现
                0 //
        );
    }
}
