---
title: Java8 接口改变：静态方法与默认方法
categories: Java基础
tags: [Java]
date: 2019-12-07 09:00

---



Java8接口更改包括接口中的静态方法和默认方法。在Java8之前，接口中只能有方法声明，但是从Java 8开始，我们可以在接口中使用默认方法和静态方法。

<!-- more -->

## Java 8 接口

设计接口一直是一项艰巨的工作，因为如果我们想在接口中添加其他方法，就需要在所有实现类中进行更改。随着接口的老化，实现它的类的数量可能会增长到无法扩展接口的程度。这就是为什么在设计应用程序时，大多数框架提供一个基本实现类，然后我们扩展它并重写适用于我们的应用程序的方法。

让我们看看默认接口方法和静态接口方法，以及它们在Java8接口更改中引入的原因。



## Java 接口默认方法

为了在java接口中创建默认方法，我们需要在方法签名中使用“default”关键字。例如：

```java
package com.journaldev.java8.defaultmethod;

public interface Interface1 {

	void method1(String str);
	
	default void log(String str){
		System.out.println("I1 logging::"+str);
	}
}
```

注意，log（String str）是Interface1中的默认方法。现在，当一个类将实现Interface1时，不必为接口的默认方法提供实现。这个特性将帮助我们用额外的方法扩展接口，我们只需要提供一个默认的实现。

假设我们有另一个具有以下方法的接口：

```java
package com.journaldev.java8.defaultmethod;

public interface Interface2 {

	void method2();
	
	default void log(String str){
		System.out.println("I2 logging::"+str);
	}
}
```

我们知道Java不允许我们继承多个类，因为它会导致“菱形问题”，编译器无法决定使用哪个超类方法。使用默认方法时，接口也会出现菱形问题。因为如果一个类同时实现了Interface1和Interface2并且没有实现公共的默认方法，编译器就不能决定选择哪个方法。

扩展多个接口是Java不可或缺的一部分，您可以在核心Java类以及大多数企业应用程序和框架中找到它。因此，为了确保这个问题不会出现在接口中，必须为接口的常见默认方法提供实现。因此，如果一个类同时实现上述两个接口，则它必须为log（）方法提供实现，否则编译器将抛出编译时错误。

实现Interface1和Interface2的一个简单类是：

```java
package com.journaldev.java8.defaultmethod;

public class MyClass implements Interface1, Interface2 {

	@Override
	public void method2() {
	}

	@Override
	public void method1(String str) {
	}

	@Override
	public void log(String str){
		System.out.println("MyClass logging::"+str);
		Interface1.print("abc");
	}
}

```

关于java接口默认方法的要点：

1. Java接口默认方法将帮助我们扩展接口，而不必担心破坏实现类。

2. Java接口默认方法弥补了接口和抽象类之间的差异。

3. Java 8接口默认方法将帮助我们避免使用工具类，例如所有Collections类方法都可以在接口本身中提供。

4. Java接口默认方法将帮助我们删除基本实现类，我们可以提供默认实现，实现类可以选择重写哪个。

5. 在接口中引入默认方法的一个主要原因是为了增强Java 8中的Collections API以支持lambda表达式。

6. 如果层次结构中的任何类具有具有相同签名的方法，则默认方法将变得不相关。默认方法不能重写java.lang.Object中的方法。推理非常简单，因为Object是所有java类的基类。因此，即使我们在接口中将Object类方法定义为默认方法，它也将是无用的，因为Object类方法将始终被使用。这就是为什么要避免混淆，我们不能有覆盖Object类方法的默认方法。

7. Java接口默认方法也称为Defender方法或虚拟扩展方法。

   

## Java 接口静态方法

Java接口静态方法与默认方法类似，只是我们不能在实现类中重写它们。这个特性有助于我们避免实现类中的糟糕实现带来的不希望的结果。让我们用一个简单的例子来研究这个问题。

```java
package com.journaldev.java8.staticmethod;

public interface MyData {

	default void print(String str) {
		if (!isNull(str))
			System.out.println("MyData Print::" + str);
	}

	static boolean isNull(String str) {
		System.out.println("Interface Null Check");

		return str == null ? true : "".equals(str) ? true : false;
	}
}
```

现在让我们看看一个实现类，它的isNull（）方法的实现很差。

```java
package com.journaldev.java8.staticmethod;

public class MyDataImpl implements MyData {

	public boolean isNull(String str) {
		System.out.println("Impl Null Check");
		return str == null ? true : false;
	}
	
	public static void main(String args[]){
		MyDataImpl obj = new MyDataImpl();
		obj.print("");
		obj.isNull("abc");
	}
}
```

注意，isNull（String str）是一个简单的类方法，它没有重写接口方法。例如，如果我们将@Override注释添加到isNull（）方法中，将导致编译器错误。
现在，当我们运行应用程序时，会得到以下输出。

```sql
Interface Null Check
Impl Null Check
```

如果我们将接口方法从static设置为default，我们将得到以下输出。

```sql
Impl Null Check
MyData Print::
Impl Null Check
```

Java接口静态方法仅对接口方法可见，如果我们从MyDataImpl类中移除isNull（）方法，我们将无法将其用于MyDataImpl对象。但是和其他静态方法一样，我们可以使用类名来使用接口静态方法。例如，有效语句将是：

```java
boolean result = MyData.isNull("abc");
```

java接口静态方法要点：

1. Java接口静态方法是接口的一部分，不能用于实现类对象。
2. Java接口静态方法适合于提供实用方法，例如空检查、集合排序等。
3. Java接口静态方法通过不允许实现类重写它们来帮助我们提供安全性。
4. 我们不能为Object类方法定义接口静态方法，我们将得到编译器错误为“这个静态方法不能从Object中隐藏实例方法”。这是因为在java中不允许这样做，因为Object是所有类的基类，我们不能有一个类级静态方法和另一个具有相同签名的实例方法。
5. 我们可以使用java接口静态方法来移除诸如集合之类的实用工具类，并将其所有静态方法移动到相应的接口，这样就很容易找到和使用。



## Java函数式接口

在结束本文之前，我想简单介绍一下功能接口。只有一个抽象方法的接口称为函数式接口。

引入了一个新的注释@functionainterface来将接口标记为Functional接口。@functionainterface注释是一种避免在功能接口中意外添加抽象方法的工具。这是可选的，但使用它是很好的实践。

Java 8的功能性接口是人们期待已久且备受关注的特性，因为它使我们能够使用lambda表达式来实例化它们。添加了一个新的包java.util.function和一堆函数接口，为lambda表达式和方法引用提供目标类型。我们将在以后的文章中研究函数接口和lambda表达式。



> 翻译自： https://www.journaldev.com/2752/java-8-interface-changes-static-method-default-method#comments