package org.joker.transform;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Type;
import org.joker.MyClassLoader;
import org.joker.reader.ClassPrinter;

import java.io.IOException;
import java.lang.reflect.Method;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM4;
import static jdk.internal.org.objectweb.asm.Opcodes.V1_8;

public class TestTransformMain {

    public static void main(String[] args) throws Exception {
        ClassReader classReader = new ClassReader("org.joker.transform.TestTransform");
        ClassWriter classWriter = new ClassWriter(classReader, 0);

        classReader.accept(new ClassPrinter(), 0);

        byte[] bytes = classWriter.toByteArray();
        //Class aClass = new MyClassLoader().defineClass("org.joker.transform.TestTransform", bytes);
        //System.out.println(aClass);

        Type type = Type.getType(String.class);
        System.out.println(type.getClassName());
        System.out.println(type.getInternalName());

        Method trim = String.class.getMethod("trim");
        System.out.println(Type.getType(trim).getInternalName());
    }
}
