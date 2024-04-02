# ASM4

## asm注意事项

由于字节码中是没有package和import这种语句的，所有asm中需要使用到一个类的时候必须使用它的内部名，内部名规则是类的全限定名将其中的`.`改成`/`，如 **String**`的内部名为 **java/lang/String**。



### Java 基本类型的描述符

内部名只能用于类或接口类型。所有其他 Java 类型，比如字段类型，在已编译类中都是用类型描述符表示的



| Java 类型       | 类型描述符               |
| --------------- | ------------------------ |
| **boolean**     | **Z**                    |
| **char**        | **C**                    |
| **byte**        | **B**                    |
| **short**       | **S**                    |
| **int**         | **I**                    |
| **float**       | **F**                    |
| **long**        | **J**                    |
| **double**      | **D**                    |
| **Object**      | **Ljava/lang/Object;**   |
| **int[]**       | **[I**                   |
| **Object[] []** | **[[Ljava/lang/Object;** |

基元类型的描述符是单个字符:**Z** 表示 **boolean**，**C** 表示 **char**，**B** 表示 **byte**，**S** 表示 **short**， **I** 表示 **int**，**F** 表示 **float**，**J** 表示 **long**，**D** 表示 **double**。一个类类型的描述符是这个类的 内部名，前面加上字符 **L**，后面跟有一个分号。例如，**String** 的类型描述符为 **Ljava/lang/String;**。而一个数组类型的􏰂述符是一个方括号后面跟有该数组元素类型的描述符。



### 方法描述符

​			

| 源文件中的方法声明           | 方法描述符                  |
| ---------------------------- | --------------------------- |
| **void m(int i, float f)**   | **(IF)V**                   |
| **int m(Object o)**          | **(Ljava/lang/Object;)I**   |
| **int[] m(int i, String s)** | **(ILjava/lang/String;)[I** |
| **Object m(int[] i)**        | **([I)Ljava/lang/Object;**  |

Object类及其子类后面要加一个`;`



## asm重要的类

### 读取类

要读取类需要使用到的组件有**ClassReader**以及给这个Reader的一个ClassVisitor的访问者实现类回调。



```java
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
        return null;
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
        return null;
    }

    public void visitEnd() {
        System.out.println("--visitEnd--");
    }
}
```



```java
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
```



Visiter节点一共有以下几个：

| 方法名称        | 描述                       | 节点              |
| :-------------- | :------------------------- | :---------------- |
| visit           | 访问类信息                 | ClassVisitor      |
| visitField      | 访问类成员变量或类变量信息 | FieldVisitor      |
| visitMethod     | 访问类中的方法             | MethodVisitor     |
| visitAnnotation | 访问类的注释信息           | AnnotationVisitor |

#### MethodVisitor

```java
abstract class MethodVisitor { // public accessors ommited MethodVisitor(int api);
MethodVisitor(int api, MethodVisitor mv);
AnnotationVisitor visitAnnotationDefault();
AnnotationVisitor visitAnnotation(String desc, boolean visible); AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible);
void visitAttribute(Attribute attr);
void visitCode();
void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack);
void visitInsn(int opcode);
void visitIntInsn(int opcode, int operand);
void visitVarInsn(int opcode, int var);
void visitTypeInsn(int opcode, String desc);
void visitFieldInsn(int opc, String owner, String name, String desc);
void visitMethodInsn(int opc, String owner, String name, String desc); void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs);
void visitJumpInsn(int opcode, Label label);
void visitLabel(Label label);
void visitLdcInsn(Object cst);
void visitIincInsn(int var, int increment);
void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels); void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels); void visitMultiANewArrayInsn(String desc, int dims);
void visitTryCatchBlock(Label start, Label end, Label handler,
String type);
void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index);
void visitLineNumber(int line, Label start);
void visitMaxs(int maxStack, int maxLocals);
void visitEnd();
}
```



较重要方法解释：

| MethodVisitor常用方法名 | 描述                               |
| ----------------------- | ---------------------------------- |
| visitCode               | ASM开始扫描方法                    |
| visitMaxs               | 用以确定类方法在执行时候的堆栈大小 |
| visitEnd                | 表示方法输出完毕                   |



### 生成类

要使用asm生成类需要使用到 ClassWriter 使用它的visit方法编写类实现，最后使用toByteArray方法转成一个byte数组，最后用一个自定义的类加载器来加载这个类（主要是要重写里面的findClass方法，中间调用defineClass(String name, byte)方法来加载）

```java
	public static void main(String[] args) {
        ClassWriter cw = new ClassWriter(0); // 参数为 0 / ClassWriter.COMPUTE_MAXS / ClassWriter.COMPUTE_FRAMES 中的一个，代表是否需要在MethodVisitor中是否需要手动通过visitMaxs方法指定最大操作数栈大小和最大局部变量表大小
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
        // 这里使用一个 ClassLoader 来加载这个类
        Class aClass = new MyClassLoader().defineClass("org.joker.Comparable", comparableBT);
        System.out.println(aClass);
    }
```

注意ClassWriter 继承了ClassVisitor，所以它也可以通过 ClassReader 的 accept 方法访问。此时 ClassWriter 调用toByteArray得到的类就是 ClassReader 构造方法传入的类所对应的字节码byte数组。

```java
		ClassReader classReader = new ClassReader(Test.class.getName());
        ClassWriter classWriter = new ClassWriter(0);
        classReader.accept(classWriter, 0);

        ClassReader classReader2 = new ClassReader(classWriter.toByteArray());
        classReader2.accept(new ClassPrinter(), 0);
```





### 转换类

真正使用过程中还可以结合上面的 ClassWriter 和 ClassReader 来做到读取一个类并修改里面的内容。

我们可以在自定义的 ClassVisitor 里面封装一个 ClassWriter ，在执行对应的visit方法时来给这个 ClassWriter 添加或修改东西。

在 ClassVisitor 中要移除一个属性/方法/注解只需要在访问到该属性/方法/注解的visitXxx方法那返回null就行了，同理要改变则是重写对应的visitXxx方法并返回合适的Visitor对象。

在ASM4使用手册里面提供的案例里面很喜欢用一个操作，用一个自定义的 ClassVisitor 来装饰一到多个 ClassVisitor ，在该 ClassVisitor 的构造方法中传入你要装饰的 ClassVisitor ，然后你使用就使用该新定义的 ClassVisitor 。这个你只需要在构造中调用 ClassVisitor 的含 ClassVisitor 的构造方法（这种只能装饰一个 ClassVisitor ）。



### Type

type可以用来表示类和方法，使用以下方式获得type对象

```java
        // 获取类的type
		Type type = Type.getType(String.class);
        System.out.println(type.getClassName());
        System.out.println(type.getInternalName());

		// 获取方法的type是根据反射的Method对象获取
		Method trim = String.class.getMethod("trim");
        System.out.println(Type.getType(trim).getInternalName());
```



### TraceClassVisitor

TraceClassVisitor可以接收一个IO的PrintWriter对象，将生成的类输出到PrintWriter中。

```java
		ClassWriter classWriter = new ClassWriter(0);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(classWriter, new PrintWriter(System.out));
        ClassReader classReader = new ClassReader(Test.class.getName());
        classReader.accept(traceClassVisitor, 0);
```

并且在调用 TraceClassVisitor 的visit 方法时会同步调用 构造传过来的 ClassVisitor 的该visit方法。

这个就相当于io流的那个装饰器模式，这个类提供了一层将visit结果输出到PrintWriter的装饰功能。



### CheckClassAdapter

CheckClassAdapter跟上面的TraceClassVisitor是一样的思想，一个装饰器来装饰一个 ClassVisitor （一般是ClassWriter），这个装饰器的功能就是在visit添加 属性/方法/注解 定义的同时验证这些定义是否正确。



### ASMifier

ASMifier可以通过jvm参数调用来打印源代码（反编译）

```java
java -classpath asm.jar:asm-util.jar \
org.objectweb.asm.util.ASMifier \
java.lang.Runnable
```



## 方法

java方法使用栈实现，每个栈帧里面有局部变量表和操作数栈（我默认看到这里的人会jvm，不提太多）。

我们每个表达式语句的计算就是靠操作数栈来实现，假设有个语句 int c = a + b;

这里会先有一个c变量声明在局部变量表里面，然后将 a 入操作数栈，再将 b 入操作数栈，然后将栈顶的 a 和 b 相加出栈赋值给 c（就相当于后缀表达式/逆波兰式计算）。long和double类型需要占局部变量表和操作数栈的两个槽位，其他基本类型是一个槽位，这也是为什么这两个类型的很多操作都不是原子的。

#### 部分常用字节码指令：

下面指令中的x一般用于表示某个类型描述符

###### 数据读取与写入指令：VarInsn

**ILOAD**, **LLOAD**, **FLOAD**, **DLOAD** 和 **ALOAD** 指令读取一个局部变量并将它的值压到操作数栈中。如 `ILOAD i` 是将局部变量表中索引 i 的内容入操作数栈顶。

**ILOAD** 用于加载一个 **boolean**、**byte**、 **char**、**short** 或 **int** 局部变量。

**LLOAD**、**FLOAD** 和 **DLOAD** 分别用于加载 **long**、**float** 或 **double** 值。其中 LLOAD 和 DLOAD 占两个槽的大小。

**ALOAD** 用于加载任意非基元值。

与之对应，**ISTORE**、**LSTORE**、**FSTORE**、**DSTORE** 和 **ASTORE** 指令从操作数栈中弹出一个值，并将它存储在由其索引 i 指定的局部变量中。 



###### 数组指令：

在上面的数据指令的类型后面加一个 `A` 。

**xALOAD**指令弹出一个索引和一个数组，并压入此索 引处数组元素的值。

**xASTORE** 指令弹出一个值、一个索引和一个数组，并将这个值存 储在该数组的这一索引处。

这里的 *x* 可以是 **I**、**L**、**F**、**D** 或 **A**，还可以是 **B**、**C** 或 **S**。 



###### 操作数栈指令：

**POP**弹出栈顶部的值。

**DUP**压入顶部栈值的一个副本。

**SWAP** 交互操作数栈顶两个元素。

**BIPUSH** x 将整形 x 入栈。



###### 常量指令：

下面的几个指令是将常量入操作数栈。

**ACONST**_**NULL**压入**null。**

**ICONST**_**0**压入 **int** 值 0。

**FCONST**_**0** 压入 **0f。**

**DCONST_0** 压入 **0d。**

**BIPUSH** *b* 压入字节值 *b。*

**SIPUSH** *s* 压入 **short** 值 *s*。

**LDC** *cst* 压入任意 **int**、**float**、**long**、**double**、**String** 或 **class**1 常量 **cst**。



###### 算术指令：Insn

**xADD**、**xSUB**、**xMUL**、**xDIV** 和 **xREM** 对应于**+**、**-**、*****、**/**和**%**运算。

这些指令没有参数，它们是将操作数栈顶元素出栈运算后再将结果入栈，其中 **x** 为 **I**、 **L**、**F** 或 **D** 之一。



###### 类型转换：

这些指令将操作数栈顶元素弹出并转换后再入栈。

**I2F**, **F2D**, **L2D** 等将数值由一种数值类型转换为另一种 类型。

**CHECKCAST** *t* 将一个引用值转换为类型 *t*。 



###### 对象指令：

些指令用于创建对象、锁定它们、检测它们的类型。

**NEW** type 将一个 *type* 类型的新对象压入栈中(其中 *type* 是一个内部名)



###### 字段指令：

**GETFIELD** *owner name desc* 从操作数栈中弹出一个对象引用，并将该对象的 *name* 属性中的值入栈。owner 是该属性所在类的内部名。

**PUTFIELD** *owner name desc* 弹出一个值和一个对象引用（连续从操作数栈中弹出两个值，栈顶第一个元素为右值，第二个元素为左值的对象引用，如this.name = name 就是以下字节码：

ALOAD 0

ALOAD name的局部变量表索引

PUTFIELD 你的类的内部名 name Ljava/lang/String;

），并将这个值存储在它的 *name* 字段中。

上面这两种情况下，该对象都必须是 *owner* 类型，它的字段必须为 *desc* 类型。

**GETSTATIC** 和 **PUTSTATIC** 是类似指令，但用于静态字段。 



###### 方法指令：

**INVOKEVIRTUAL** *owner name desc* 调用在 类 *owner* 中定义的 *name* 方法，其方法描述符为 *desc*。

**INVOKESTATIC** 用于静态方法。

**INVOKESPECIAL** 用于私有方法和构造器。

**INVOKEINTERFACE** 用于接口中定义的方法。

**INVOKEDYNAMIC** 用于新动态方法调用机制。



###### 跳转指令：

**IFEQ** *label* 从栈中弹出一个 **int** 值，如果这个值为 0，则跳转到由这个 *label* 指定的指令处(否则，正常执行下一条指令)。

还有许多其他跳转指令，比如 **IFNE** 或 **IFGE**。

**TABLESWITCH** 和**LOOKUPSWITCH** 对应于 **switch** 语句。



###### 返回指令：Insn

**xRETURN**和**RETURN**指令用于终止一个方法的执行，并将其结果返回给调用者（结果是操作数栈顶元素）。

**RETURN** 用于返回 **void** 的方法，**xRETURN** 用于其他方法。



#### 如何使用

局部变量表说明：

如果方法是成员方法，局部变量表下标0为this，参数依次就是1,2...

如果方法是静态方法，局部变量表下标0为第一个参数，也就是参数依次就是0,1,2...



这里直接上例子，生成一个类，类里面一共一个 private String name属性 和 一个无参构造，两个 name 的 getter 和 setter。

```java
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
```



如果要用跳转指令要配合Label对象使用，直接 new 一个 Label 对象，在 `visitJumpInsn `里面使用，后面在需要跳转到的地方前面加上 `mv.visitLabel(刚刚new的Label对象);`



```java
	/*
		下面的代码大概是该asm生成的字节码内容
		public void setF(int f) {
			if (f < 0) {
				throw new IllegalArgumentException();
			} else {
				this.f = f;
			}
		}
	*/
	mv.visitCode();
    mv.visitVarInsn(ILOAD, 1);
    Label label = new Label();
	// 定义并使用label
    mv.visitJumpInsn(IFLT, label);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ILOAD, 1);
    mv.visitFieldInsn(PUTFIELD, "pkg/Bean", "f", "I");
    Label end = new Label();
    mv.visitJumpInsn(GOTO, end);
	// 标记 label 的跳转点
    mv.visitLabel(label);
    mv.visitFrame(F_SAME, 0, null, 0, null);
    mv.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "()V");
    mv.visitInsn(ATHROW);
    mv.visitLabel(end);
    mv.visitFrame(F_SAME, 0, null, 0, null);
    mv.visitInsn(RETURN);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
```



## AST API

### ClassNode

asm的ast 树形api核心类是ClassNode

并且 ClassNode 同样继承自 ClassVisitor，并且它还有一个accept方法，也就是它拥有 ClassReader 和 ClassVisitor的功能。

**ClassNode**

```java
public class ClassNode... {
    public int version;
	public int access;
	public String name;
    public String signature;
    public String superName;
    public List<String> interfaces;
    public String sourceFile;
    public String sourceDebug;
    public String outerClass;
    public String outerMethod;
    public String outerMethodDesc;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<Attribute> attrs;
    public List<InnerClassNode> innerClasses;
    public List<FieldNode> fields;
    public List<MethodNode> methods;
}
```

其中**name** 是一个内部名字，**signature** 是一个类签名。



**FieldNode**

```java
public class FieldNode ... {
      public int access;
      public String name;
      public String desc;
      public String signature;
      public Object value;
      public FieldNode(int access, String name, String desc,
           String signature, Object value) {
        ...
}
... }
```



**MethodNode**

```java
 public class MethodNode ... {
      public int access;
      public String name;
      public String desc;
      public String signature;
      public List<String> exceptions;
      ...
      public MethodNode(int access, String name, String desc,
      String signature, String[] exceptions)
      {
... }
}
```



### 使用

用树 API 生成类的过程就是:创建一个 **ClassNode** 对象，并初始化它的字段。



```java
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
		// 然后把它删了，方法同理
```



### 树形API操作方法

使用的是 MethodNode 对象，跟上面的Core API模式差不多，就是用 xxxNode对象来做，如：

```java
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
```

