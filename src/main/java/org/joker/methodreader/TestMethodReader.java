package org.joker.methodreader;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.commons.AnalyzerAdapter;
import org.joker.MyClassLoader;

import java.lang.reflect.Method;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class TestMethodReader {
    public static void main(String[] args) throws Exception {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(
                V1_8,
                ACC_PUBLIC,
                "org/joker/MyClass",
                null,
                "java/lang/Object",
                new String[] {}
        );

        classWriter.visitField(
                ACC_PRIVATE,
                "name",
                "Ljava/lang/String;",
                null,
                null
        ).visitEnd();

        MethodVisitor construct = classWriter.visitMethod(
                ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );
        construct.visitVarInsn(ALOAD, 0);
        construct.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        construct.visitInsn(RETURN);
        construct.visitMaxs(1, 1);
        construct.visitEnd();
        classWriter.visitEnd();

        MethodVisitor getterName = classWriter.visitMethod(
                ACC_PUBLIC,
                "getName",
                "()Ljava/lang/String;",
                null,
                null
        );
        getterName.visitCode();
        getterName.visitVarInsn(ALOAD, 0); // this 入栈
        getterName.visitFieldInsn(GETFIELD, "org/joker/MyClass", "name", "Ljava/lang/String;"); //
        getterName.visitInsn(ARETURN);
        getterName.visitMaxs(1, 1);
        getterName.visitEnd();
        classWriter.visitEnd();

        MethodVisitor setterName = classWriter.visitMethod(
                ACC_PUBLIC,
                "setName",
                "(Ljava/lang/String;)V",
                null,
                null
        );
        setterName.visitCode();
        setterName.visitVarInsn(ALOAD, 0); // this 入栈
        setterName.visitVarInsn(ALOAD, 1); // 形参 name 入栈
        setterName.visitFieldInsn(PUTFIELD, "org/joker/MyClass", "name", "Ljava/lang/String;"); // this.name = name
        setterName.visitInsn(RETURN);
        setterName.visitMaxs(1, 1);
        setterName.visitEnd();
        classWriter.visitEnd();

        Class myClass = new MyClassLoader().defineClass("org.joker.MyClass", classWriter.toByteArray());
        Object instance = myClass.newInstance();
        Method setName = myClass.getMethod("setName", String.class);
        setName.invoke(instance, "xiaoming");

        Method getName = myClass.getMethod("getName");
        System.out.println(getName.invoke(instance));
    }
}
