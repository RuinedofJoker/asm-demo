package org.joker.tree;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.tree.*;
import org.joker.MyClassLoader;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class TestTreeMethod {
    public static void main(String[] args) {
        ClassNode classNode = new ClassNode();
        // ... 省略其他的定义了

        MethodNode methodNode = new MethodNode(ACC_PUBLIC, "setF", "(I)V", null, null);
        InsnList il = methodNode.instructions;
        il.add(new VarInsnNode(ILOAD, 1));
        LabelNode label = new LabelNode();
        il.add(new JumpInsnNode(IFLT, label));
        il.add(new VarInsnNode(ALOAD, 0));
        il.add(new VarInsnNode(ILOAD, 1));
        il.add(new FieldInsnNode(PUTFIELD, "pkg/Bean", "f", "I"));
        LabelNode end = new LabelNode();
        il.add(new JumpInsnNode(GOTO, end));
        il.add(label);
        il.add(new FrameNode(F_SAME, 0, null, 0, null));
        il.add(new TypeInsnNode(NEW, "java/lang/IllegalArgumentException"));
        il.add(new InsnNode(DUP));
        il.add(new MethodInsnNode(INVOKESPECIAL,
                "java/lang/IllegalArgumentException", "<init>", "()V"));
        il.add(new InsnNode(ATHROW));
        il.add(end);
        il.add(new FrameNode(F_SAME, 0, null, 0, null));
        il.add(new InsnNode(RETURN));
        methodNode.maxStack = 2;
        methodNode.maxLocals = 2;

        classNode.methods.add(methodNode);

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);

        Class aClass = new MyClassLoader().defineClass("pkg.Bean", classWriter.toByteArray());
    }
}
