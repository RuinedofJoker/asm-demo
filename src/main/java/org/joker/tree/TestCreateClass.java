package org.joker.tree;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.tree.*;
import org.joker.MyClassLoader;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class TestCreateClass {
    public static void main(String[] args) throws Exception {
        ClassNode classNode = new ClassNode();
        classNode.name = "org/joker/TestCreateMyClass";
        classNode.superName = "java/lang/Object";
        classNode.access = ACC_PUBLIC;
        classNode.version = V1_8;

        MethodVisitor constructor = classNode.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(ALOAD, 0);
        constructor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(RETURN);
        constructor.visitMaxs(2, 2);
        constructor.visitEnd();
        classNode.visitEnd();

        MethodNode methodNode = new MethodNode(
                ACC_PUBLIC + ACC_STATIC,
                "add",
                "(II)I",
                null,
                null
        );
        InsnList instructions = methodNode.instructions;
        instructions.add(new VarInsnNode(ILOAD, 0));
        instructions.add(new VarInsnNode(ILOAD, 1));
        instructions.add(new InsnNode(IADD));
        instructions.add(new VarInsnNode(ISTORE, 2));
        instructions.add(new VarInsnNode(ILOAD, 2));
        instructions.add(new InsnNode(IRETURN));
        methodNode.maxStack = 3;
        methodNode.maxLocals = 3;
        methodNode.accept(classNode);

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        Class aClass = new MyClassLoader().defineClass("org/joker/TestCreateMyClass".replace("/", "."), classWriter.toByteArray());

        Object instance = aClass.newInstance();
        System.out.println(aClass.getMethod("add", int.class, int.class).invoke(null, 1, 2));
    }
}
