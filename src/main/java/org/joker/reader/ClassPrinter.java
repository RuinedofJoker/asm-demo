package org.joker.reader;

import jdk.internal.org.objectweb.asm.*;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM4;

/**
 * 一个访问者模式实现的类访问者的回调类，将这个传给 ClassReader 可以做到 访问类
 */
public class ClassPrinter extends ClassVisitor {
    public ClassPrinter() {
        super(ASM4);
    }

    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        System.out.println("--visit--");
        StringBuilder sb = new StringBuilder();
        if (interfaces != null) {
            sb.append("{");
            for (String interfaceItem : interfaces) {
                sb.append(interfaceItem).append(" ");
            }
            sb.append("}");
        }
        System.out.println("version: " + version + " access: " + access + " name: " + name + " signature: " + signature + " superName: " + superName + " interfaces: " + sb);
    }

    public void visitSource(String source, String debug) {
        System.out.println("--visitSource--");
        System.out.println("source: " + source + " debug: " + debug);
    }

    public void visitOuterClass(String owner, String name, String desc) {
        System.out.println("--visitOuterClass--");
        System.out.println("owner: " + owner + " name: " + name + " desc: " + desc);
    }

    public AnnotationVisitor visitAnnotation(String desc,
                                             boolean visible) {
        System.out.println("--visitAnnotation--");
        System.out.println("desc: " + desc + " visible: " + visible);
        return null;
    }

    public void visitAttribute(Attribute attr) {
        System.out.println("--visitAttribute--");
        System.out.println("attr: " + attr);
    }

    public void visitInnerClass(String name, String outerName,
                                String innerName, int access) {
        System.out.println("--visitInnerClass--");
        System.out.println("name: " + name + " outerName: " + outerName + " innerName: " + innerName + " access: " + access);
    }

    public FieldVisitor visitField(int access, String name, String
            desc,
                                   String signature, Object value) {
        System.out.println("--visitField--");
        System.out.println("access: " + access + " name: " + name + " desc: " + desc + " signature: " + signature + " value: " + value);
        return new FieldVisitor(0) {
            @Override
            public AnnotationVisitor visitAnnotation(String s, boolean b) {
                System.out.println("--visitAnnotation--");
                System.out.println(s + " " + b);
                return super.visitAnnotation(s, b);
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String s, boolean b) {
                System.out.println("--visitTypeAnnotation--");
                return super.visitTypeAnnotation(i, typePath, s, b);
            }

            @Override
            public void visitAttribute(Attribute attribute) {
                System.out.println("--visitAttribute--");
                super.visitAttribute(attribute);
            }

            @Override
            public void visitEnd() {
                System.out.println("--visitEnd--");
                super.visitEnd();
            }
        };
    }

    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        System.out.println("--visitMethod--");
        StringBuilder sb = new StringBuilder();
        if (exceptions != null) {
            sb.append("{");
            for (String exception : exceptions) {
                sb.append(exception).append(" ");
            }
            sb.append("}");
        }
        System.out.println("access: " + access + " name: " + name + " desc: " + desc + " signature: " + signature + " exceptions: " + sb);
        return new MethodVisitor(0) {
            @Override
            public void visitCode() {
                System.out.println("--visitCode--");
                super.visitCode();
            }

            @Override
            public void visitMaxs(int i, int i1) {
                System.out.println("--visitMaxs--");
                super.visitMaxs(i, i1);
            }

            @Override
            public void visitEnd() {
                System.out.println("--visitEnd--");
                super.visitEnd();
            }
        };
    }

    public void visitEnd() {
        System.out.println("--visitEnd--");
    }
}
