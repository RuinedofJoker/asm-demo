package org.joker.tree;

import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import java.util.List;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class TestClassNode {
    public static void main(String[] args) {
        ClassNode classNode = new ClassNode();
        classNode.version = V1_8;
        classNode.access = ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE;
        classNode.name = "org/joker/Comparable";
        classNode.superName = "java/lang/Object";
        classNode.interfaces.add("java/lang/Runnable");
        classNode.fields.add(new FieldNode(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                "LESS", "I", null, new Integer(-1)));
        classNode.fields.add(new FieldNode(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                "EQUAL", "I", null, new Integer(0)));
        classNode.fields.add(new FieldNode(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                "GREATER", "I", null, new Integer(1)));
        classNode.methods.add(new MethodNode(ACC_PUBLIC + ACC_ABSTRACT,
                "compareTo", "(Ljava/lang/Object;)I", null, null));

        List<FieldNode> fields = classNode.fields;
        for (int i = 0; i < fields.size(); i++) {
            // 如果要删除属性这里可以找到你要删除的属性的下标
        }
        // 然后把它删了
        //classNode.accept();
    }
}
