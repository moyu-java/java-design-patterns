# 单例模式（Singleton）

## 单例模式的定义与特点

单例（Singleton）模式的定义：指一个类只有一个实例，且该类能自行创建这个实例的一种模式。

在计算机系统中，还有 Windows 的回收站、操作系统中的文件系统、多线程中的线程池、显卡的驱动程序对象、打印机的后台处理服务、应用程序的日志对象、数据库的连接池、网站的计数器、Web
应用的配置对象、应用程序中的对话框、系统中的缓存等常常被设计成单例。

单例模式在现实生活中的应用也非常广泛，例如公司 CEO、部门经理等都属于单例模型。J2EE 标准中的 ServletContext 和 ServletContextConfig、Spring 框架应用中的
ApplicationContext、数据库中的连接池等也都是单例模式。

单例模式有 3 个特点：

1. 单例类只有一个实例对象；
2. 该单例对象必须由单例类自行创建；
3. 单例类对外提供一个访问该单例的全局访问点。

## 单例模式的优点和缺点

单例模式的优点：

1. 单例模式可以保证内存里只有一个实例，减少了内存的开销。
2. 可以避免对资源的多重占用。
3. 单例模式设置全局访问点，可以优化和共享资源的访问。

单例模式的缺点：

1. 单例模式一般没有接口，扩展困难。如果要扩展，则除了修改原来的代码，没有第二种途径，违背开闭原则。
2. 在并发测试中，单例模式不利于代码调试。在调试过程中，如果单例中的代码没有执行完，也不能模拟生成一个新的对象。
3. 单例模式的功能代码通常写在一个类中，如果功能设计不合理，则很容易违背单一职责原则。

## 单例模式的实现

单例模式可以按不同维度对其进行分类

* 线程安全维度：线程安全的单例模式、线程不安全的单例模式
* 对象创建时机：饿汉式的单例模式、懒汉式的单例模式
    * 饿汉式 - 第一次调用前就已经被实例化了。
    * 懒汉式 - 只有在第一次调用的时候才会被实例化。

### 枚举实现的单例

因为 Java 保证枚举类的每个枚举都是单例，所以我们只需要编写一个只有一个枚举的类即可，而且它也是**线程安全**的。

枚举类也完全可以像其他类那样定义自己的字段、方法，如下方实例代码中的 `name` 参数，`getName()` 及 `setName()` 方法等。

```java
/**
 * 枚举的单例实现 - 线程安全
 * 绝对防止多实例化，即使是在面反序列化和反射攻击时
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public enum EnumSingleton {

    /**
     * 唯一实例
     */
    INSTANCE;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

> `Effective Java` 书中也推荐使用这种单例模式。

我们可以在 `Application` 类中进行测试，代码如下：

```java
public class Application {

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);
        // 多线程测试
        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> printObject(EagerlySingleton.getInstance())).start();
        }
    }
    
    private static void printObject(Object obj) {
        System.out.println("对象内存地址：" + obj.getClass().getSimpleName() + "@" + obj.hashCode());
    }
}
```

### 饿汉式单例模式

饿汉式单例，其创建对象的时机是在第一次调用之前，在类被 JVM 加载时就会被创建。

此类提供了已被实例化的静态实例 `INSTANCE`，所以不存在多个线程创建多个实例的情况。所以它是**线程安全**的

这种单例模式的缺点是即使单例没有被使用，对象也会被创建，占用资源。但其实并不会占用太多资源。

```java
/**
 * 饿汉式单例模式 - 线程安全
 * 该类在程序加载时就已经初始化完成了
 *
 * @author moyu.jun
 * @date 2021/4/18
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
        System.out.println("EagerlySingleton 被实例化");
    }

    /**
     * 可被用户调用以获取类的实例
     *
     * @return 单例实例
     */
    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);
        // 多线程测试
        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(EagerlySingleton.getInstance().toString())).start();
        }
    }
}
```

### 静态内部类实现的单例

静态内部类实现的单例与上面的饿汉式单例有点相似，这种单例模式也是 **线程安全**的，但它却是延迟加载的，其代码实现如下所示：

```java
/**
 * 通过静态内部类实现的懒加载单例模式 - 线程安全
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class StaticInnerClassLazyLoadedSingleton {

    /**
     * 私有构造方法
     */
    private StaticInnerClassLazyLoadedSingleton() {
        System.out.println("StaticInnerClassLazyLoadedSingleton 被实例化");
    }

    /**
     * 获取单例实例
     *
     * @return 单例实例
     */
    public static StaticInnerClassLazyLoadedSingleton getInstance() {
        return HelperHolder.INSTANCE;
    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    /**
     * 使用静态内部类来实现延迟加载
     */
    private static class HelperHolder {
        private static final StaticInnerClassLazyLoadedSingleton INSTANCE = new StaticInnerClassLazyLoadedSingleton();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);

        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(StaticInnerClassLazyLoadedSingleton.getInstance().toString())).start();
        }
    }
}
```

运行 `main()` 方法，可以看到日志打印中 `代码启动` 先于 `StaticInnerClassLazyLoadedSingleton 被实例化` 打印，可以说明它的确是**延迟加载**的。

并且在多线程测试中可以发现其实例化地址都是一样的。可以说明它也的确是**线程安全**的。

### 懒汉式单例

懒汉式的特点是`延迟加载`，即对象会在第一次调用时才会被实例化，避免资源消耗。

如下方代码所示，这是最简单的一种写法，但却是 **线程不安全** 的。

```java
/**
 * 懒汉式单例模式 - 线程不安全
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class LazyLoadedSingleton {

    /**
     * 这种写法也是线程不安全的，可能因指令重排导致实例被多次实例化
     */
    private static LazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造函数，保证无法从外部进行实例化
     */
    private LazyLoadedSingleton() {
        System.out.println("LazyLoadedSingleton 被实例化");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程不安全的实例化
     *
     * @return 单例实例
     */
    public static LazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LazyLoadedSingleton();
        }
        return INSTANCE;
    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);

        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(LazyLoadedSingleton.getInstance().toString())).start();
        }
    }
}
```

在私有构造器中添加 `Thread.sleep(100);` 延长实例化的时间，以便更好地测试多线程实例化时的问题。通过运行 `main()` 方法查看日志可以发现，对象被多次实例化，且对象内存地址也尽都不同。

那么想要让它**线程安全**的话，可以在 `getInstance()` 方法上加锁 `synchronized`。

```java
public class LazyLoadedSingleton {
    // ...

    /**
     * 线程安全的实例化，但每次获取实例都会加锁，严重影响并发性能
     *
     * @return 单例实例
     */
    public static synchronized LazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LazyLoadedSingleton();
        }
        return INSTANCE;
    }
    // ...
}
```

修改之后再次运行 `main()` 测试，查看内存地址是否全都一样，可以发现此时多线程获取的对象内存地址都是一样的。

但是他的缺点也很明显，每次调用 `getInstance()` 方法时都会加锁，严重影响了性能，所以为此又衍生出一种写法叫做 **双重校验锁**。代码如下所示。

```java
/**
 * 懒汉式单例模式 - 线程安全
 * 这种实现也被叫做双重校验锁（Double Check Locking）
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class ThreadSafeLazyLoadedSingleton {

    /**
     * 加入 volatile 保证线程可见性，防止指令重排导致实例被多次实例化
     * 否则线程不安全
     */
    private volatile static ThreadSafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadSafeLazyLoadedSingleton() {
        System.out.println("ThreadSafeLazyLoadedSingleton 被实例化");
    }

    /**
     * 线程安全的实例化，使用双重检查，避免每次获取实例时都加锁
     * 但这种模式依然是有隐患的，INSTANCE 常量必须添加 volatile 关键字才能避免指令重排，保持线程可见性
     * 而 volatile 在 JDK 1.5 之后才支持
     *
     * @return 单例实例
     */
    public static ThreadSafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (ThreadSafeLazyLoadedSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadSafeLazyLoadedSingleton();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("代码启动");
        Thread.sleep(1000);

        for (int i = 0; i < SingletonConstants.THREADS_NUMBER; i++) {
            new Thread(() -> System.out.println(ThreadSafeLazyLoadedSingleton.getInstance().toString())).start();
        }
    }
}
```

在 `getInstance()` 方法中，不在方法上加锁，而是先判断对象是否被实例化，如果还没有被实例化的话，则加锁，加锁后再判断一次是否被实例化，如果没有的话才会创建实例化。

所以在多线程的情况下，只有第一次调用时会加锁，所以并不会影响性能。

但是在这种情况下需要注意的是 `INSTANCE` 前添加了一个 `volatile` 关键字，原因则是 **双重校验锁** 可能会因为指令重排而出现问题。

### volatile 关键字解析

首先来看一下 **双重校验锁** 会出现什么问题。我们先要了解对象的创建过程(new 关键字)，它简单的分为三个阶段:

1.分配对象内存空间。 2.初始化对象。 3.设置对象指向内存空间。

但是实际上第二步和第三步的顺序是可以互换的，在 JVM 的优化中存在一种指令重排序的机制，可以加快 JVM 的运行速度。

那么现在我们来做个实验，运行 `ThreadSafeLazyLoadedSingleton.main()` 方法后，在 `target` 目录下找到它的 `ThreadSafeLazyLoadedSingleton.class`
文件，然后使用 `javap -c ThreadSafeLazyLoadedSingleton.class > DCL.txt` 来生成字节码文件。

打开文件可以在 `public static com.junmoyu.singleton.ThreadSafeLazyLoadedSingleton getInstance();` 下方看到 `getInstance()`
方法整个字节码执行过程。

```
      17: new           #9                  // class com/junmoyu/singleton/ThreadSafeLazyLoadedSingleton
      20: dup
      21: invokespecial #10                 // Method "<init>":()V
      24: putstatic     #5                  // Field INSTANCE:Lcom/junmoyu/singleton/ThreadSafeLazyLoadedSingleton;
```

上面四个步骤是节选了 `new ThreadSafeLazyLoadedSingleton()` 的执行过程。

* 17 : new 指令在 java 堆上为 ThreadSafeLazyLoadedSingleton 对象分配内存空间，并将地址压入操作栈顶
* 20 : dup 指令为复制操作栈顶值，并将其压入栈顶，这时操作栈上有连续相同的两个对象地址
* 21 : 调用实例的构造函数，实例化对象，这一步会弹出一个之前入栈的对象地址
* 24 : 将对象地址赋值给常量 `INSTANCE`

由上可看到创建一个对象并非原子操作，而是分成了多个步骤，如果 JVM 重排序后，21 在 24 之后，此时分配完了对象的内存空间，且把内存地址复制给了常量 `INSTANCE`，那么此时 `INSTANCE != null`。

如果此时有另外一个线程调用 `getInstance()` 就会直接返回 `INSTANCE` 常量，然而对象其实还没有实例化完成，返回的将是一个空的对象。执行过程如下：

| 执行步骤 | 线程1                             | 线程2                        |
| -------- | --------------------------------- | ---------------------------- |
| step1    | 分配对象内存空间                  |                              |
| step2    | 将对象内存地址赋值给常量 INSTANCE  |                              |
| step3    |                                   | 判断对象是否为 null          |
| step4    |                                   | 对象不为 null, 返回 INSTANCE |
| step5    |                                   | 访问 INSTANCE 对象           |
| step6    | 初始化对象                        |                              |

如果出现上表所示的情况，那么线程2将获取到一个空的对象，访问对象的参数或方法都将出现异常。所以需要加上 **volatile** 关键字。**volatile** 关键字有两个作用：

1.保证对象的可见性。 2.防止指令重排序。

在 `Spring` 源码中也可以看到类似的例子，如 `org.springframework.beans.factory.xml.DefaultNamespaceHandlerResolver`
类中的 `private volatile Map<String, Object> handlerMappings;` 参数与 `private Map<String, Object> getHandlerMappings()`
方法就是使用 **双重校验锁** 的方式编写的。感兴趣的可以自行查看。

> 对象的创建可能发生指令的重排序，使用 volatile 可以禁止指令的重排序，保证多线程环境下的线程安全。

那么至此，相信你对 **双重校验锁** 的单例为什么要使用 volatile 关键字的原因已经了解清楚了。

但是你可能还有一个疑惑，为什么之前在 `public static synchronized LazyLoadedSingleton getInstance()` 方法上加锁时并未提到需要加 volatile 关键字呢。

其实原因也很简单，因为这里锁的是方法，即使出现了指令重排，其他的线程在调用 `getInstance()` 时也无法获取实例，因为方法被加锁了，自然也不会出现问题，只是方法加锁性能损耗较大而已。

那么单例还有其他问题嘛？那当然是有的...

### 反射会导致单例失效嘛？

至于反射会不会导致单例失效，我们不妨测试一下。在各个单例类的 `main()` 方法中添加如下代码：

```java
public class EagerlySingleton {
    // ...

    @Override
    public String toString() {
        // 打印类名，以及类的内存地址
        return getClass().getName() + "@" + hashCode();
    }

    public static void main(String[] args) throws Exception {

        // 反射测试
        // 通过反射的方式直接调用私有构造器（通过在构造器里抛出异常可以解决此漏洞）
        Class<EagerlySingleton> clazz = (Class<EagerlySingleton>) Class.forName("com.junmoyu.singleton.EagerlySingleton");
        Constructor<EagerlySingleton> constructor = clazz.getDeclaredConstructor(null);
        // 跳过权限检查
        EagerlySingleton eagerlySingleton1 = constructor.newInstance();
        EagerlySingleton eagerlySingleton2 = constructor.newInstance();

        System.out.println(eagerlySingleton1.toString());
        System.out.println(eagerlySingleton2.toString());
    }
}
```

运行后查看日志可以发现，两个对象的内存地址是不一样的。除了枚举实现的单例模式外，其他拥有私有构造器的实现方式均可通过反射来创建多个实例。要解决的话也很简单。代码如下：

饿汉式单例模式的实现如下：

```java
/**
 * 饿汉式单例模式 - 线程安全
 * 该类在程序加载时就已经初始化完成了
 *
 * @author moyu.jun
 * @date 2021/4/18
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
        System.out.println("EagerlySingleton 被实例化");
        // 防止通过反射进行实例化
        if (INSTANCE != null) {
            throw new IllegalStateException("Already initialized.");
        }
    }

    /**
     * 可被用户调用以获取类的实例
     *
     * @return 单例实例
     */
    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }
}
```

懒汉式的单例模式实现如下：

```java
/**
 * 懒汉式单例模式 - 线程安全
 * 这种实现也被叫做双重校验锁（Double Check Locking）
 *
 * @author moyu.jun
 * @date 2021/4/18
 */
public class ThreadSafeLazyLoadedSingleton {

    /**
     * 加入 volatile 保证线程可见性，防止指令重排导致实例被多次实例化
     * 否则线程不安全
     */
    private volatile static ThreadSafeLazyLoadedSingleton INSTANCE = null;

    /**
     * 私有构造方法
     */
    private ThreadSafeLazyLoadedSingleton() {
        System.out.println("ThreadSafeLazyLoadedSingleton 被实例化");
        // 防止通过反射进行实例化
        if (INSTANCE == null) {
            INSTANCE = this;
        } else {
            throw new IllegalStateException("Already initialized.");
        }
    }

    /**
     * 线程安全的实例化，使用双重检查，避免每次获取实例时都加锁
     * 但这种模式依然是有隐患的，INSTANCE 常量必须添加 volatile 关键字才能避免指令重排，保持线程可见性
     * 而 volatile 在 JDK 1.5 之后才支持
     *
     * @return 单例实例
     */
    public static ThreadSafeLazyLoadedSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (ThreadSafeLazyLoadedSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadSafeLazyLoadedSingleton();
                }
            }
        }
        return INSTANCE;
    }
}
```

### 反序列化问题

除了反射以外，使用序列化与反序列化也同样会破坏单例。

还是以 `EagerlySingleton` 类来测试，先让其实现 `Serializable` 接口，然后在 `main()` 方法里面添加以下代码：

```java
public class EagerlySingleton implements Serializable {

    /**
     * 初始化静态实例
     */
    private static final EagerlySingleton INSTANCE = new EagerlySingleton();

    /**
     * 私有构造函数，保证无法从外部进行实例化
     */
    private EagerlySingleton() {
    }

    /**
     * 可被用户调用以获取类的实例
     *
     * @return 单例实例
     */
    public static EagerlySingleton getInstance() {
        return INSTANCE;
    }

    /**
     * 添加此方法以防止反序列化破坏单例模式
     *
     * @return 单例实例
     */
//    private Object readResolve() {
//        return INSTANCE;
//    }
    public static void main(String[] args) throws Exception {

        // 反序列化测试
        // 将对象写入文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempFile"));
        EagerlySingleton instance = EagerlySingleton.getInstance();
        System.out.println("反序列化对象1：" + instance.toString());
        oos.writeObject(instance);
        // 从文件中读取对象
        File file = new File("tempFile");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        EagerlySingleton newInstance = (EagerlySingleton) ois.readObject();
        // 判断是否是同一个对象
        System.out.println("反序列化对象2：" + newInstance.toString());
    }
}
```

运行后可以通过日志发现两个对象的内存地址并不一致，要解决这个问题，只要添加一个方法即可。如上方代码中被注释的 `readResolve()` 方法。

取消注释后，再次运行，查看日志可以发现两个实例的内存地址是一样的。

我们在用相同的方法测试一下**枚举**实现的单例模式，可以发现，**枚举**并不存在序列化的问题。

## 总结

至此，我们讨论了六种单例模式的实现方式。

1. 枚举实现 - 线程安全
2. 饿汉式单例 - 线程安全
3. 静态内部类实现 - 线程安全
4. 普通懒汉式 - 线程不安全
5. 方法加锁懒汉式 - 线程安全，但性能差
6. 双重校验锁懒汉式 - 线程安全

且除了枚举实现的单例外，其他均有反射及序列化会破坏单例的情况。那么综合来看的话，枚举实现的单例是最优的方案，也是 `Effective Java` 书中推荐的方案。然而在 Java
及一些框架的源码中使用枚举单例的例子很少，不知道是为什么，可能是我看的源码还不够多吧。

因为枚举实现的单例模式其实也属于饿汉式，所以如果在实例化时需要执行耗时操作的话，则不建议使用。

那么除此之外较好的单例实现还有**静态内部类**的实现，以及**双重校验锁**的实现，可以根据自己的业务需要灵活选择。

## 拓展

另外其实还有一种稍微特殊一点的 "单例" 模式，可以称之为 **线程单例**，及在双重校验锁的单例模式中，不使用 **volatile** 关键字，而是使用 ThreadLocal 使每一个线程拥有自己的单例。

比如 mybatis 3.5.6 版本中的 `org.apache.ibatis.executor.ErrorContext` 类，就是使用了此种方式实现的，感兴趣的可以自行研究，在此不再赘述。