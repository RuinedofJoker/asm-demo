package org.joker.writer;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import org.joker.Test;
import org.joker.reader.ClassPrinter;

public class TestWriter2 {
    public static void main(String[] args) throws Exception {
        ClassReader classReader = new ClassReader(Test.class.getName());
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classWriter, 0);

        ClassReader classReader2 = new ClassReader(classWriter.toByteArray());
        classReader2.accept(new ClassPrinter(), 0);
    }
}
