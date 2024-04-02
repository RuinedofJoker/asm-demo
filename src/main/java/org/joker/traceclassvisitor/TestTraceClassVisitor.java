package org.joker.traceclassvisitor;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.util.ASMifiable;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.CheckClassAdapter;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;
import org.joker.Test;

import java.io.PrintWriter;

public class TestTraceClassVisitor {
    public static void main(String[] args) throws Exception {
        ClassWriter classWriter = new ClassWriter(0);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(classWriter, new PrintWriter(System.out));
        ClassReader classReader = new ClassReader(Test.class.getName());
        classReader.accept(traceClassVisitor, 0);

    }
}
