# Java - Thread

## Runnable
- java.lang > interface Runnable
- 도큐먼트: https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html#java.lang.Runnable

- All Knwon Subinterfacies:
    - RunnableFuture<V>, RunnableScheduledFuture<V>

- All Known Implementing Classes:
    - AsyncBoxView.ChildState, ForkJoinWorkerThread, FutureTask, RenderableImageProducer, SwingWorker, Thread, TimerTask

- Functional Interface:
    - Runnable은 함수형 인터페이스이므로 람다 표현식 또는 메소드의 참조에 대한 대상이 될 수 있다.

```java
@FunctionalInterface
public interface Runnable
```
- Runnable는 인스턴스가 스레드에 의해 실행되는 어떠한 클래스던지 구현할 수 있는 인터페이스이다. 클래스는 *run*이라고 불리는 인자가 없는 메소드를 정의해야 한다.
- 이 인터페이스는 객체에 대해 그것들이 활성화되어 실행될 때 이용될 공통적인 프로토콜을 제공하기 위해 디자인되었다. 예를 들어, Thread 클래스는 Runnable을 구현한다. 활성화(active) 된다는 것은 단순히 스레드가 시작되었고 아직 중지되지 않았다는 것을 말한다.
- 또한, Runnable은 Thread의 하위클래스가 아니더라도 클래스가 활성화 된다는 의미를 제공한다. Runnable을 구현하는 클래스는 Thread의 하위클래스가 아니더라도 Thread 인스턴스를 인스턴스화 하고 자기 자신을 타겟으로 넣어 통과시킨다. 대부분의 경우, Runnable 인터페이스는 다른 Thread의 메소드들 대신 run() 메소드만 오버라이드 하여 사용한다. 이렇게 하는 것은 개발자가 기본 기능을 수정하거나 향상하려는 의도가 없는 이상 클래스가 서브클래스화 되지 않아야 하기 때문에 중요한 포인트이다.
- since: JDK1.0




## Thread
- java.lang > Class Thread
- 도큐먼트: https://docs.oracle.com/javase/8/docs/api/java/lang/Thread.html


```java
public class Thread
extends Object
implements Runnable
```


- *스레드 thread*라는 것은 프로그램의 실행 스레드를 말한다. JVM은 애플리케이션이 여러 개의 스레드들이 동시적 실행되는 것을 허용한다.

- 모든 스레드에는 우선순위가 있다. 우선순위가 높은 스레드는 순위가 더 낮은 스레드보다 먼저 실행된다. 각 스레드는 deamon으로 간주된다. 어떤 스레드 안에서 코드가 실행되면 새로운 Thread 객체를 생성한다. 새 스레드는 자신을 생성한 스레드와 동일한 우선순위를 갖도록 초기화되며, 생성자가 deamon일 때는 마찬가지로 daemon이다.

- JVM이 시작될 때, 대부분의 경우 (일반적으로 지정된 클래스 내에서 main이라는 이름의 메소드를 호출하는)하나의 논-데몬 싱글 스레드로 되어 있다. JVM은 다음의 상황 중 하나가 발생할 때까지 지속된다:
    - Runtime 클래스의 exit 메소드가 호출되고 security manager가 exit 오퍼레이션이 자리를 차지하도록 허용했을 때
    - daemon이 아닌 모든 스레드가 죽었을 때, 또는 호출한 run 메소드로부터 반환되거나, 실행중인 메소드를 상회하는 exception이 발생했을 때

- 실행 스레드를 생성하는 방법에는 두 가지가 있다.
- 하나는 클래스가 Thread의 서브클래스가 되도록 명시하는 것이다. 이 경우 서브클래스는 Thread 클래스로부터 run 메소드를 오버라이드 해야 한다. 그런 뒤에 서브클래스의 인스턴스가 할당되고 시작될 수 있다. 예를 들어, 주어진 값보다 더 큰 소수를 계산하는 스레드는 다음과 같이 작성될 수 있다:
    ```java
         class PrimeThread extends Thread {
         long minPrime;
         PrimeThread(long minPrime) {
             this.minPrime = minPrime;
         }

         public void run() {
             // compute primes larger than minPrime
              . . .
         }
     }
    ```
- 다음 코드는 스레드를 생성한 뒤에 실행된다:
    ```java
    PrimeThread p = new PrimeThread(143);
    p.start();
    ```
- 스레드를 생성하는 또 다른 방법은 Runnable 인터페이스 구현을 명시하는 것이다. 이 경우 클래스는 run 메소드를 구현한다. 그런 뒤에 클래스의 인스턴스가 할당된 뒤, Thread가 생성될 때 인자로 넘어가고 시작될 수 있다. 위에서 봤던 케이스과 같은 예제를 보도록 하자:
    ```java
    class PrimeRun implements Runnable {
         long minPrime;
         PrimeRun(long minPrime) {
             this.minPrime = minPrime;
         }

         public void run() {
             // compute primes larger than minPrime
              . . .
         }
     }
    ```
- 다음 코드는 스레드를 생성한 뒤에 실행된다:
    ```java
    PrimeRun p = new PrimeRun(143);
    new Thread(p).start();
    ```

- 식별을 위해 각 스레드는 이름이 주어진다. 한 개 이상의 스레드에 같은 이름이 주어질 수 있다. 특별한 이름 없이 스레드가 생성되면, 새 이름이 부여된다.
- 따로 명시하지 않는 한, 이 클래스에서 생성자 또는 메소드에 null 인자를 보내면 NullPointerException이 발생된다.
- Since: JDK1.0





## Callable
- java.util.concurrent > interface Callable<V>
- type parameters: V - 호출된 메소드의 반환값
- Functional Interface:
    함수형 인터페이스로서, 람다 표현식 또는 메소드를 참조될 수 있다.

```java
@FunctionalInterface
public interface Callable<V>
```
- 어떤 반환값을 갖는 작업은 exception을 던질 수 있다. 구현자는 인자가 없는 call 메소드를 정의한다. Callable 인터페이스는 인스턴스가 잠재적으로 다른 스레드에 의해 실행될 수 있는 가능성이 있다는 데에서 Runnable과 유사하다. 하지만 Runnable은 값을 반환하거나 명시된(checked) exception을 던질 수 없다는 점에서 다르다.

- 구현자 클래스는 다른 일반 형태를 Callable 클래스 형태로 변환하는 기능 메소드를 포함한다.
- Since: 1.5




# Executor
- java.util.concurrent > Interface Executor

```java
public interface Executor
```

- 제출된 Runnable task를 실행하는 객체. 이 인터페이스는 스레드 사용, 스케줄링 등의 세부정보를 포함하여 각 task가 실행되는 매커니즘 방식에서 task 제출을 분리하는 방법을 제공한다. 하나의 Executor는 명시적으로 스레드를 생성하는 대신 일반적으로 작동한다. 예를 들어, 각 task 세트에 새로운 Thread(new (RunnableTask()).start())를 발생시키는 것 보다는 다음과 같이 쓸 수 있다:
    ```java
    Executor executor = anExecutor;
    executor.execute(new RunnableTask1());
    executor.execute(new RunnableTask2());
    ...
    ```

- 하지만, Executor 인터페이스는 해당 실행이 비동기적으로 실행되도록 엄격하게 요구하지는 않는다. 가장 간단한 케이스로는 executor가 제출된 task를 즉시 호출자의 스레드에서 실행시키는 것이다:
```java
class DirectExecutor implements Executor {
   public void execute(Runnable r) {
     r.run();
   }
 }
```

- 좀 더 세부적으로는, task들은 호출자의 스레드 말고 다른 어떤 스레드에서 실행된다. 아래의 executor는 각 task에 새로운 스레드를 부여한다.

```java
 class ThreadPerTaskExecutor implements Executor {
   public void execute(Runnable r) {
     new Thread(r).start();
   }
 }
```

- 많은 Executor의 구현체들은 task를 어떻게, 언제로 스케줄링 할 것인지 일종의 제한을 설정한다. 아래의 executor는 제출된 task들을 두 번째 executor로 직렬화하여 넘기는 복합적인 executor를 설명한다.

```java
 class SerialExecutor implements Executor {
   final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
   final Executor executor;
   Runnable active;

   SerialExecutor(Executor executor) {
     this.executor = executor;
   }

   public synchronized void execute(final Runnable r) {
     tasks.offer(new Runnable() {
       public void run() {
         try {
           r.run();
         } finally {
           scheduleNext();
         }
       }
     });
     if (active == null) {
       scheduleNext();
     }
   }

   protected synchronized void scheduleNext() {
     if ((active = tasks.poll()) != null) {
       executor.execute(active);
     }
   }
 }
```

- 이 패키지의 Executor 구현체는 ExecutorService를 구현한다. ExecutorService는 보다 더 확장된 인터페이스이다. ThreadPoolExecutor 클래스는 확장 가능한 스레드풀 구현체를 제공한다. Executors 클래스는 이 Executors를 위한 편리한 팩토리 메서드들을 제공한다.

- 메모리 일관성 효과: Runnable 객체를 Executor에 제출하기 전에 앞서 스레드의 작업은 Executor가 시작되기 전, 아마도 다른 스레드에서 발생한다.

- Since: 1.5
