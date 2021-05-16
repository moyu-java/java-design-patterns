# 单例模式（Singleton）

![](https://i.loli.net/2021/04/26/jKCU4OepwDolxAh.png)

> 定义：指一个类只有一个实例，且该类能自行创建这个实例的一种模式。

## 1. 特点

单例模式有 3 个特点：

1. 单例类只有一个实例对象；
2. 该单例对象必须由单例类自行创建；
3. 单例类对外提供一个访问该单例的全局访问点。

一般来说，系统中只需要有一个实例就能满足系统需要时，那么就可以设计成单例模式。

比如 Windows 的回收站，数据库的连接池，系统中的日志对象等等。（当然，如果你非要设计成多个，我也没有办法是不是）

## 2. 优点和缺点

单例模式的优点：

1. 单例模式可以保证内存里只有一个实例，减少了内存的开销。
2. 可以避免对资源的多重占用。
3. 单例模式设置全局访问点，可以优化和共享资源的访问。

单例模式的缺点：

1. 单例模式一般没有接口，扩展困难。如果要扩展，则除了修改原来的代码，没有第二种途径，违背开闭原则。
2. 在并发测试中，单例模式不利于代码调试。在调试过程中，如果单例中的代码没有执行完，也不能模拟生成一个新的对象。
3. 单例模式的功能代码通常写在一个类中，如果功能设计不合理，则很容易违背单一职责原则。

## 3. 结构

单例模式的 UML 类图如下图所示，非常简单，只有一个类。

![](https://i.loli.net/2021/04/26/jKCU4OepwDolxAh.png)

上述类图很好地体现了单例模式的3个特点。

1. 一个私有实例常量 `INSTANCE`，保证只有一个实例对象；
2. 一个私有的构造器 `private Singleton(){}` 保证外部无法实例化，只能由自身创建；
3. 通过公共的 `getInstance()` 方法提供一个访问该单例的全局访问点。

## 4. 实现

单例模式可以按不同维度对其进行分类：

* 线程安全维度：线程安全的单例模式、线程不安全的单例模式
* 对象创建时机：饿汉式的单例模式、懒汉式的单例模式
    * 饿汉式 - 第一次调用前（或说类被 JVM 加载时）就已经被实例化了。
    * 懒汉式 - 只有在第一次调用的时候才会被实例化。

### 4.1 枚举类单例

因为 Java 保证枚举类的每个枚举都是单例，所以我们只需要编写一个只有一个枚举的类即可，而且它是**线程安全**的。

枚举类也完全可以像其他类那样定义自己的字段、方法，如下方实例代码中的 `name` 参数，`getName()` 及 `setName()` 方法等。

```java
package com.junmoyu.singleton;

/**
 * 枚举的单例实现 - 线程安全
 * 绝对防止多实例化，即使是在面反序列化和反射攻击时
 */
public enum EnumSingleton {
    /**
     * 唯一实例
     */
    INSTANCE;

    /**
     * 如果没有初始化的内容，可删除此方法
     */
    EnumSingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，内存地址为：" + hashCode());
        this.name = "莫语";
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

> `Effective Java` 书中也推荐使用这种单例模式。因为它足够简单，线程安全，且天然可以防止多实例化，即使是在面反序列化和反射攻击时。

如果需要在单例中做初始化操作，可以使用构造方法实现，否则是不需要构造方法的。使用时可以直接用 `EnumSingleton.INSTANCE.getName()` 来调用单例中的方法。

在类中添加一个 `main()` 方法测试一下看看，代码如下：

```java
public enum EnumSingleton {
    INSTANCE;

    // ... 省略其他代码
    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 方法调用测试
        System.out.println("name: " + EnumSingleton.INSTANCE.getName());
        EnumSingleton.INSTANCE.setName("junmoyu.com");
        System.out.println("name: " + EnumSingleton.INSTANCE.getName());

        // 反射测试
        // 枚举天然防止反射攻击
        Class<EnumSingleton> clazz = (Class<EnumSingleton>) Class.forName("com.junmoyu.singleton.EnumSingleton");
        Constructor<EnumSingleton> constructor = clazz.getDeclaredConstructor(null);
        // 这里将直接抛出异常
        EnumSingleton singleton = constructor.newInstance();
    }
}
```

运行后结果如下：

```
com.junmoyu.singleton.EnumSingleton 被实例化，hashCode：460141958
代码启动
name: 莫语
name: junmoyu.com
Exception in thread "main" java.lang.NoSuchMethodException: com.junmoyu.singleton.EnumSingleton.<init>()
	at java.lang.Class.getConstructor0(Class.java:3082)
	at java.lang.Class.getDeclaredConstructor(Class.java:2178)
	at com.junmoyu.singleton.EnumSingleton.main(EnumSingleton.java:51)
```

从结果中可以看出，枚举类的单例模式不是延迟加载的，且可以防止反射创建多个实例。关于反射的话题会在后面的章节专门讲解。

### 4.2 饿汉式单例

饿汉式单例，其创建对象的时机是在第一次调用之前，在类被 JVM 加载时就会被创建。其代码如下：

```java
package com.junmoyu.singleton;

/**
 * 饿汉式单例模式 - 线程安全
 * 该类在程序加载时就已经初始化完成了
 */
public class EagerlySingleton {
    /**
     * 初始化静态实例
     */
    private static final EagerlySingleton INSTANCE = new EagerlySingleton();

    /**
     * 私有构造函数，保证无法从外部进行实例化
     */
    private EagerlySingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 可被用户调用以获取类的实例
     */
    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 5; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + EagerlySingleton.getInstance().hashCode())).start();
        }
    }
}
```

**饿汉式单例**提供了已被实例化的静态实例 `INSTANCE`，所以不存在多个线程创建多个实例的情况，所以它是**线程安全**的。（即使多线程同时调用`getInstance()`，获取也是已经实例化的对象，并没有再重新创建）

这种单例模式的缺点是即使单例没有被使用，对象也会被创建，占用资源（但其实并不会占用太多资源，视具体业务情况而定）。运行`main()`方法测试一下。

```
com.junmoyu.singleton.EagerlySingleton 被实例化，hashCode：460141958
测试代码启动
多线程测试：hashCode：@460141958
多线程测试：hashCode：@460141958
多线程测试：hashCode：@460141958
多线程测试：hashCode：@460141958
多线程测试：hashCode：@460141958
```

可以看到**饿汉式单例**的确是**延迟加载**的，而且**线程安全**。其实线程安全问题比较难以测试，因为此类的确是线程安全的，所以仅做演示，后面会出现线程安全问题的单例方式会着重说明。反射的问题后面也会有专门的章节进行说明。

### 4.3 静态内部类单例

静态内部类实现的单例与上面的饿汉式单例有点相似，这种单例模式也是 **线程安全**的，但它却是延迟加载的，所以对于一些需要延迟加载的单例来说，这种方式是一种非常不错的选择。其代码实现如下所示：

```java
package com.junmoyu.singleton;

/**
 * 静态内部类 - 线程安全，延迟加载
 * 写法简单，且可延迟加载，较推荐此种实现
 */
public class StaticInnerClassSingleton {
    /**
     * 使用静态内部类来实现延迟加载
     */
    private static class HelperHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }

    /**
     * 私有构造方法
     */
    private StaticInnerClassSingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 获取单例实例
     */
    public static StaticInnerClassSingleton getInstance() {
        return HelperHolder.INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 5; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + StaticInnerClassSingleton.getInstance().hashCode())).start();
        }
    }
}
```

与饿汉式单例不同的是，静态内部类单例是延迟加载的，对于一些占用资源多且使用频率不高的单例来说是个非常不错的实现，而且它也是线程安全的，如果你的业务需要线程安全且延迟加载的单例模式，那么静态内部类是个非常不错的选择。同样运行 `main()`
进行测试，结果如下：

```
测试代码启动
com.junmoyu.singleton.StaticInnerClassSingleton 被实例化，hashCode：1251394951
多线程测试：hashCode：@1251394951
多线程测试：hashCode：@1251394951
多线程测试：hashCode：@1251394951
多线程测试：hashCode：@1251394951
多线程测试：hashCode：@1251394951
```

通过日志可以明显发现，当主线程 `main()`启动之后，且睡眠等待一分钟，在调用了 `StaticInnerClassSingleton.getInstance().hashCode()`
方法时该类才被实例化，且在多线程中，仅被实例化了一次。

### 4.4 懒汉式单例 - 线程不安全

懒汉式的特点就是**延迟加载**，即对象会在第一次调用时才会被实例化，避免资源消耗。

如下方代码所示，这是最简单的一种写法，但这是 **线程不安全** 的。

```java
package com.junmoyu.singleton;

/**
 * 懒汉式 - 线程不安全
 * 非常不推荐使用
 */
public class ThreadUnsafeLazyLoadedSingleton {

    private static ThreadUnsafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadUnsafeLazyLoadedSingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 可被用户调用以获取类的实例 - 线程不安全
     */
    public static ThreadUnsafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadUnsafeLazyLoadedSingleton();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 延迟加载测试
        System.out.println("测试代码启动");
        Thread.sleep(1000);

        // 多线程测试
        for (int i = 0; i < 5; i++) {
            new Thread(() -> System.out.println("多线程测试：hashCode：" + "@" + ThreadUnsafeLazyLoadedSingleton.getInstance().hashCode())).start();
        }
    }
}
```

直接运行 `main()` 测试一下，结果如下：

```
测试代码启动
com.junmoyu.singleton.ThreadUnsafeLazyLoadedSingleton 被实例化，hashCode：847507483
多线程测试：hashCode：@847507483
com.junmoyu.singleton.ThreadUnsafeLazyLoadedSingleton 被实例化，hashCode：319699154
多线程测试：hashCode：@319699154
com.junmoyu.singleton.ThreadUnsafeLazyLoadedSingleton 被实例化，hashCode：758108352
多线程测试：hashCode：@758108352
多线程测试：hashCode：@847507483
多线程测试：hashCode：@847507483
```

从结果可以明显的看到，该类虽然是延迟加载的，但是在多线程中，被实例化多次，这是线程不安全，非常不推荐使用！

### 4.5 懒汉式单例 - 线程安全

既然上一种方式是线程不安全的，那么基于上面的写法，做一些修改，让它线程安全不就可以嘛。比较简单的一种做法是在`getInstance()`方法上加锁，添加 `synchronized` 关键字即可。代码如下：

```java
package com.junmoyu.singleton;

/**
 * 懒汉式 - 线程安全，延迟加载
 * 但因为 getInstance() 方法加锁，导致多线程下性能较差，不推荐使用
 */
public class ThreadSafeLazyLoadedSingleton {
    private static ThreadSafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadSafeLazyLoadedSingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 可被用户调用以获取类的实例 - 线程安全
     * 使用 synchronized 加锁以实现线程安全
     */
    public static synchronized ThreadSafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadSafeLazyLoadedSingleton();
        }
        return INSTANCE;
    }
}
```

通过加锁，每次只有一个线程允许访问 `getInstance()`，实例化对象，当实例化完成后，这个线程才会解锁，其他线程就没办法创建实例了，也就实现了线程安全。

但是这种方式有一个很大的缺点，就是每次使用时都会因为**锁**而非常消耗性能，因为每次调用 `getInstance()` 都只有一个线程可以访问，其他线程只能干等着。所以这种方式也是不推荐的。测试代码大家可自己运行看看结果。

### 4.6 懒汉式单例 - 双重校验锁

到这里，我们可以看到以上两种懒汉式单例都有非常明显的缺陷，那么怎么解决呢？

基于上一种方式的代码，既然在方法上加锁会影响性能，那么我们把锁加在方法里面，加锁之前先判断一下是否已经实例化了是不是就可以了。

```java
public class ThreadSafeLazyLoadedSingleton {
    public static ThreadSafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            // 1 号位置
            synchronized (ThreadSafeLazyLoadedSingleton.class) {
                // 2 号位置
                INSTANCE = new ThreadSafeLazyLoadedSingleton();
            }
        }
        return INSTANCE;
    }
}
```

如上方代码所示，这样就不会出现每次调用 `getInstance()`都只能有一个线程访问导致性能问题了是不是。

但是仔细想一想上面是不是在多线程的环境下还是会有问题呢？比如此时有线程A和线程B两个线程，两个线程同时访问 `getInstance()`方法，同时到达 **1 号位置**，此时他们会争抢锁，因为只能有一个线程进入下面的代码块。

假设线程A此时抢到了锁，线程B未抢到锁，在1号位置等待。当线程A在 **2 号位置** 创建完实例，返回之后解锁，此时对象已经被实例化了。解锁之后，线程B就可以获取锁了，获取锁之后，线程B也可以再创建一个实例。

针对上面这种情况，还需要再进行优化。

```java
public class ThreadSafeLazyLoadedSingleton {
    public static ThreadSafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            // 1 号位置
            synchronized (ThreadSafeLazyLoadedSingleton.class) {
                // 2 号位置
                if (INSTANCE == null) {
                    INSTANCE = new ThreadSafeLazyLoadedSingleton();
                }
            }
        }
        return INSTANCE;
    }
}
```

在 **2 号位置** 同样加上非空判断，这样即使出现上述的情况，当实例化之后，其他线程获取锁，进入到 **2 号位置**，也无法再创建实例了。这种方式就是懒汉式单例的最终解决方案：**双重校验锁**。

至于为什么要双重校验，答案就在上面的那个例子里面了。**双重校验锁** 单例模式完整的代码如下：

```java
package com.junmoyu.singleton;

/**
 * 懒汉式最终解决方案 - 线程安全，延迟加载
 * 也叫双重校验锁
 */
public class DoubleCheckLockingSingleton {
    /**
     * 加入 volatile 保证线程可见性，防止指令重排导致实例被多次实例化
     * 否则线程不安全
     */
    private volatile static DoubleCheckLockingSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private DoubleCheckLockingSingleton() {
        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    /**
     * 线程安全的实例获取，使用双重检查，避免每次获取实例时都加锁
     * 但这种模式依然是有隐患的，INSTANCE 常量必须添加 volatile 关键字才能避免指令重排，保持线程可见性
     * 而 volatile 在 JDK 1.5 之后才支持
     */
    public static DoubleCheckLockingSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (DoubleCheckLockingSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheckLockingSingleton();
                }
            }
        }
        return INSTANCE;
    }
}
```

测试方法与之前一样，在此就不演示了。

要注意的事，这种实现方式，`INSTANCE` 常量是必须要加上 `volatile` 关键字的，不然还是不能保证完全的线程安全，不加 `volatile`  可能会因为 JVM 指令重排而出现问题。具体原因在下一个章节详细说明。

## 5. 进阶

### 5.1 volatile 关键字解析

首先来看一下 **双重校验锁** 会出现什么问题。我们先要了解对象的创建过程(new 关键字)，它简单的分为三个阶段:

1.分配对象内存空间。 2.初始化对象。 3.设置对象指向内存空间。

但是实际上第二步和第三步的顺序是可以互换的，在 JVM 的优化中存在一种指令重排序的机制，可以加快 JVM 的运行速度。

那么现在我们来做个实验，运行 `DoubleCheckLockingSingleton.main()` 方法后，在 `target` 目录下找到它的 `DoubleCheckLockingSingleton.class`
文件，然后使用 `javap -c DoubleCheckLockingSingleton.class > DCL.txt` 来生成字节码文件。

打开文件可以在 `public static com.junmoyu.singleton.DoubleCheckLockingSingleton getInstance();` 下方看到 `getInstance()`
方法整个字节码执行过程。

```
17: new           #9      // class com/junmoyu/singleton/ThreadSafeLazyLoadedSingleton
20: dup
21: invokespecial #10     // Method "<init>":()V
24: putstatic     #5      // Field INSTANCE:Lcom/junmoyu/singleton/ThreadSafeLazyLoadedSingleton;
```

上面四个步骤是节选了 `new DoubleCheckLockingSingleton()` 的执行过程。

* 17 : new 指令在 java 堆上为 ThreadSafeLazyLoadedSingleton 对象分配内存空间，并将地址压入操作栈顶
* 20 : dup 指令为复制操作栈顶值，并将其压入栈顶，这时操作栈上有连续相同的两个对象地址
* 21 : 调用实例的构造函数，实例化对象，这一步会弹出一个之前入栈的对象地址
* 24 : 将对象地址赋值给常量 `INSTANCE`

由上可看到创建一个对象并非原子操作，而是分成了多个步骤，如果 JVM 重排序后，21 在 24 之后，此时分配完了对象的内存空间，且把内存地址复制给了常量 `INSTANCE`，那么此时 `INSTANCE != null`。

如果此时有另外一个线程调用 `getInstance()` 就会直接返回 `INSTANCE` 常量，然而对象其实还没有实例化完成，返回的将是一个空的对象。执行过程如下：

| 执行步骤 | 线程1                             | 线程2                        |
| -------- | --------------------------------- | ---------------------------- |
| step1    | 分配对象内存空间                  |                              |
| step2    | 将对象内存地址赋值给常量 INSTANCE |                              |
| step3    |                                   | 判断对象是否为 null          |
| step4    |                                   | 对象不为 null, 返回 INSTANCE |
| step5    |                                   | 访问 INSTANCE 对象           |
| step6    | 初始化对象                        |                              |

如果出现上表所示的情况，那么线程2将获取到一个空的对象，访问对象的参数或方法都将出现异常。所以需要加上 **volatile** 关键字。**volatile** 关键字有两个作用：

1. 保证对象的可见性。
2. 防止指令重排序。

> 对象的创建可能发生指令的重排序，使用 volatile 可以禁止指令的重排序，保证多线程环境下的线程安全。

那么至此，相信你对 **双重校验锁** 的单例为什么要使用 volatile 关键字的原因已经了解清楚了。

但是你可能还有一个疑惑，为什么之前在 `public static synchronized LazyLoadedSingleton getInstance()` 方法上加锁时并未提到需要加 volatile 关键字呢。

其实原因也很简单，因为这里锁的是方法，即使出现了指令重排，其他的线程在调用 `getInstance()` 时也无法获取实例，因为方法被加锁了，自然也不会出现问题，只是方法加锁性能损耗较大而已。

在 `Spring` 源码中也可以看到类似的例子，如 `org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver`
类中的 `private volatile Map<String, Object> handlerMappings;` 参数与 `private Map<String, Object> getHandlerMappings()`
方法就是使用 **双重校验锁** 的方式编写的。感兴趣的可以自行查看。

### 5.2 反射会导致单例失效嘛？

至于反射会不会导致单例失效，我们不妨测试一下。在各个单例类的 `main()` 方法中添加如下代码：

```java
public class EagerlySingleton {
    // ...

    public static void main(String[] args) throws Exception {
        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此问题）
        Class<EagerlySingleton> clazz = (Class<EagerlySingleton>) Class.forName("com.junmoyu.singleton.EagerlySingleton");
        Constructor<EagerlySingleton> constructor = clazz.getDeclaredConstructor(null);

        EagerlySingleton singleton1 = constructor.newInstance();
        EagerlySingleton singleton2 = constructor.newInstance();

        System.out.println("反射测试：singleton1 hashCode：" + "@" + singleton1.hashCode());
        System.out.println("反射测试：singleton2 hashCode：" + "@" + singleton2.hashCode());
    }
}
```

运行后查看日志可以发现，两个对象的 hashCode 是不一样的。除了枚举实现的单例模式外，其他拥有私有构造器的实现方式均可通过反射来创建多个实例。

要解决的话也很简单。饿汉式单例模式的实现如下：

```java
/**
 * 饿汉式单例模式 - 线程安全
 */
public class EagerlySingleton {
    private static final EagerlySingleton INSTANCE = new EagerlySingleton();

    private EagerlySingleton() {
        // 防止通过反射进行实例化从而破坏单例
        // 最好放在开头，如不需要删除即可
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }
        System.out.println(getClass().getCanonicalName() + " 被实例化，hashCode：" + hashCode());
    }

    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }
}
```

其他方式实现的单例和饿汉式单例一样，在此就不一一说明了。再次运行 `main()` 方法测试反射。可以打印日志如下：

```
...
Caused by: java.lang.IllegalStateException: Already initialized.
	at com.junmoyu.singleton.StaticInnerClassSingleton.<init>(StaticInnerClassSingleton.java:27)
	... 5 more
```

可以看到想要通过反射实例化的时候，直接抛出了异常，并没有进行实例化的操作。

### 5.3 反序列化问题

除了反射以外，使用反序列化也同样会破坏单例。

还是以 `EagerlySingleton` 类来测试，先让其实现 `Serializable` 接口，然后在 `main()` 方法里面添加以下代码：

```java
package com.junmoyu.singleton.serializable;

public class EagerlySingleton implements Serializable {
    private static final EagerlySingleton INSTANCE = new EagerlySingleton();

    private EagerlySingleton() {
    }

    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("tempFile"));
        EagerlySingleton osInstance = EagerlySingleton.getInstance();
        System.out.println("反序列化测试：osInstance hashCode：" + "@" + osInstance.hashCode());
        os.writeObject(osInstance);

        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
        EagerlySingleton isInstance = (EagerlySingleton) is.readObject();
        // 查看 hashCode 是否相同
        System.out.println("反序列化测试：isInstance hashCode：" + "@" + isInstance.hashCode());
    }
}
```

运行 `main()`方法测试一下，查看日志结果如下：

```
com.junmoyu.singleton.serializable.DoubleCheckLockingSingleton 被实例化，hashCode：460141958
反序列化测试：osInstance hashCode：@460141958
反序列化测试：isInstance hashCode：@81628611
```

可以看到序列化对象和反序列化之后的对象 hashCode 并不相同。

想要解决反序列化的问题，只需要添加一个 `readResolve()` 方法即可。代码如下：

```java
public class EagerlySingleton implements Serializable {
    // ...

    /**
     * 如果有序列化需求，需要添加此方法以防止反序列化时重新创建新实例
     * 如无序列化需求可不加，同时去除 implements Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }
}
```

再次运行 `main()`方法测试一下，查看日志结果如下：

```
com.junmoyu.singleton.serializable.EagerlySingleton 被实例化，hashCode：460141958
反序列化测试：osInstance hashCode：@460141958
反序列化测试：isInstance hashCode：@460141958
```

可以发现两个对象的 hashCode 已经是一致的了。

> 枚举类的单例，是天然可以绝对防止多实例化的，反射及反序列化都无效。

至于为什么加了 `readResolve()`就可以防止反序列化重新创建实例，就要深入源码解析了。这里就不详细叙述了，简单说一下。

反序列化的对象获取是通过方法 ObjectInputStream#readObject()，进入源码，可以看到 `Object obj = readObject0(false);` 这行代码最终返回的对象。继续进入源码，在
ObjectInputStream#readObject0()方法中可以看到一个 switch 选择器，找到下面这块重点代码：

```java
public class ObjectInputStream extends InputStream implements ObjectInput, ObjectStreamConstants {
    // ...
    private Object readObject0(boolean unshared) throws IOException {
        // ...
        try {
            switch (tc) {
                // ...
                case TC_ENUM:
                    // 单例中为什么枚举最安全，感兴趣的同学可以看一下这里的实现
                    return checkResolve(readEnum(unshared));
                case TC_OBJECT:
                    // 这里的 readOrdinaryObject 就是读取对象的方法了
                    return checkResolve(readOrdinaryObject(unshared));
            }
        } finally {
        }
    }
}    
```

进入 `readOrdinaryObject()` 方法，最终的答案都在这里了。

![](https://i.loli.net/2021/04/28/rH91hqgmEQFfbz7.png)

这个方法里面，最重要的两块代码，我圈出来了。第一块其实就是我们在没有加入 `readResolve()`
方法时，它通过反射创建了一个新的实例，在第二块代码的判断里 `desc.hasReadResolveMethod() == false` 将不会执行 if 里面的语句。它返回就是之前创建的新实例了。

if 中的代码其实就是调用 `readResolve()` 方法，然后将获取到的对象替换掉第一块代码里面创建的新实例，而`readResolve()` 方法不正是返回了单例的实例嘛。所以如果加了这个方法，就会执行 if
里面的代码，用单例的实例去替换掉反射创建的实例。

所以现在你知道为什么加入 `readResolve()` 方法就可以防止反序列化了吧。

### 5.4 你以为这就结束了？

虽然不想再啰嗦了（对不住了！），但是还有一个**重点**！

如果有两个类加载器（class loader）的存在，那是两个类加载器可能各自创建自己的单例模式。

因为每个类加载器都定义了一个命名空间，如果有两个或以上的类加载器，不同的类加载器可能会加载同一个类，那么从整个程序来看，同一个类就被加载多次了。也就是会有多个单例的实例并存。

所以，如果你的程序有多个类加载器又同时使用了单例模式，那么就要小心了。有一个解决方法就是**你可以自行指定类加载器，并指定同一个类加载器。**

## 6. 拓展

另外其实还有一种稍微特殊一点的 "单例" 模式，可以称之为 **线程单例**，那就是使用 ThreadLocal 使每一个线程拥有自己的单例。

比如 mybatis 3.5.x 版本中的 `org.apache.ibatis.executor.ErrorContext` 类，就是使用了此种方式实现的，感兴趣的可以自行研究，在此不再赘述。简单贴下代码感受下：

```java
package org.apache.ibatis.executor;

public class ErrorContext {
    // 这里使用了函数式接口 Supplier<T>，更优雅的初始化 ThreadLocal
    private static final ThreadLocal<ErrorContext> LOCAL = ThreadLocal.withInitial(ErrorContext::new);

    private ErrorContext() {
    }

    public static ErrorContext instance() {
        return LOCAL.get();
    }
}
```

## 7. 总结

至此，我们讨论了六种单例模式的实现方式。

1. 枚举实现 - 线程安全
2. 饿汉式单例 - 线程安全
3. 静态内部类实现 - 线程安全、延迟加载
4. 普通懒汉式 - 线程不安全、延迟加载
5. 方法加锁懒汉式 - 线程安全、延迟加载，但性能差
6. 双重校验锁懒汉式 - 线程安全、延迟加载

且除了枚举实现的单例外，其他均有反射及序列化会破坏单例的情况。那么综合来看的话，枚举实现的单例是最优的方案，也是 `Effective Java` 书中推荐的方案。然而在 Java
及一些框架的源码中使用枚举单例的例子很少，不知道是为什么，可能是我看的源码还不够多吧。

因为枚举实现的单例模式其实也属于饿汉式，所以如果在实例化时需要执行耗时操作的话，则不建议使用。

那么除此之外较好的单例实现还有**静态内部类**的实现，以及**双重校验锁**的实现，可以根据自己的业务需要灵活选择。
