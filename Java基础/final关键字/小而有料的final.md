---
title: 小而有料的final
categories: Java内功第一重
tags: [Java,final]
date: 2018-02-08 20:37:15
---

final这块肉虽然小,但不可不知，因为不仅面试可能会问，实际中也常使用。
就问你常量声明时你为什么要加final？它有哪些作用？你思考过吗？


<!-- more -->

final通常指被修饰的东西不能被改变的，不能被改变的原因旺往往出于设计和效率的考虑。

先看final能修饰什么？
- 修饰变量
- 修饰方法
- 修饰类

不同的修饰有什么不同的作用呢？下面慢慢道来：

## final常量

final修饰的常量必须在被使用前进行初始化，初始化的方式无非两种：定义时赋值或构造函数内赋值

final修饰常量基本类型和引用类型有些不同，我分开讲。

### 基本数据类型

```java
public class Test {

    private final int A;
    private final int B = 3;
    private final int C = new Random().nextInt();

    public Test() {
        this.A = 5;
    }
    public Test(int a) {
        this.A = a;
    }
}

```
- 为什么常量B要用final修饰？

    因为这种确定值(定义便立即赋值)的final常量，编译器会在编译时将该常量值带入到任何可能用到它的计算式中，这会减轻运行时的一些负担。
 比如 int d = 3 * B; 那么编译后相当于 int d = 9;

- final修饰的常量值是在编译期间被确定的吗？

    注意上面的变量C，我们并不能以为final修饰的就能在编译时知道它的值，C的值只有在运行时被初始化时才会显现。

### 引用类型

引用类型不能改变指的是引用被初始化指向一个对象后，就再也**无法改为指向其他对象**，但其**指向的对象本身是可以被修改的**。

```java
public class B {

    //final常量d指向了对象D
    private final D d =new D();

    //这个方法改变了对象D的内容
    public D change(){
        this.d.setS("change");
        return d;
    }

    //注意这个方法,将常量d的引用改变了
    public D refnewD(){
        this.d = new D();
        return d;
    }
    
    class D {
        private String s= "init";

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }
    }
}
```
上面代码是无法编译的，因为this.d=new D()会提示`Cannot assign a value to final variable 'd'`,但是你却可以调用change()方法来改变d对象的s变量值。

## final方法

- 为什么使用final修饰方法

1. 设计：把方法锁定，防止继承类修改它的含义。
2. 效率：早期编译器会将final方法所有的调用转为内嵌调用，随着JVM的提高，这种方式已被丢弃。

- final和private

当一个方法被private修饰，这会隐式的指定为final，这也会使得子类无法覆盖此方法，可以对private方法增加final修饰，但对该方法并不能增加任何意义。

## final类

String类是final应用的典型例子，可去查看[面试别再问我String了](../String类详解/面试别再问我String了.md)。

- 为什么要把类指定为final？

出于安全考虑或其他原因，你不希望该类有任何变动或被继承。

> 一旦类被final修饰，即代表final类中的所有成员变量和方法都会隐式的final.


## final参数
在方法的参数列表中将参数指定为final，即代表该参数引用所指向的对象是无法改变的；
若为基本类型，则表示值可以读取，但无法修改。修饰情况和final常量类似。也可结合[这题不会！别说你懂值传递与引用传递](../Java值传递/这题不会！别说你懂值传递与引用传递.md)看。

## final相关

### static 和 final

1. static强调的是该数据只存在一份，且是属于类的，不是属于对象。
2. final强调该数据不可变，且是属于对象的。

```java
public class C {

    private final double A = 3.14D;

    private static double b = 3.14D;

    public static void main(String[] args) {
        new C();
        new C();
        new C();
    }
}
```
上面代码运行后，A会存在3个，但b只有一个。

### final，finally和finalize

- finally 

    是在异常处理时配合```try-catch```执行清理操作，需要清理的资源包括：打开的文件或网络连接等，它会把内存之外的资源恢复到他们的初始状态。无论try中是否有异常出现，finally里的操作都会被执行。

- finalize

    这是Object基类的一个方法，垃圾收集器在将对象清除出内存之前调用的清理资源方法，且此方法只会被系统调用一次，其实finalize能做的工作，try-finally能做的更好，《深入理解Java虚拟机》中建议大家忘掉这个方法的存在。

## 参考书籍
- Java编程思想
- 深入理解Java虚拟机