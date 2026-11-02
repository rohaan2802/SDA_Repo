# Sprints, GoF Creational Patterns, and `atomic` / `volatile`

Revision notes for SDA: what a sprint is, the five Gang of Four creational patterns in Java, and the difference between C++ `std::atomic`, C++ `volatile` and Java `volatile`.

## 1. Sprints in software development

A **sprint** is a short, fixed-length work period in Agile (usually 1–2 weeks, at most 4) in which the team plans, builds, tests and finishes a set of tasks, then shows a usable increment.

**Sprint flow (Scrum)**

1. **Sprint Planning** – choose the Sprint Goal and the work that can realistically be done.
2. **Development** – build, test and integrate continuously.
3. **Daily Scrum** – 15-minute daily sync to check progress and adjust the plan.
4. **Sprint Review** – demo the increment to stakeholders and gather feedback.
5. **Sprint Retrospective** – improve the team's process for the next sprint.

**Why sprints:** faster feedback, lower risk (small batches instead of one big release), better predictability (velocity, capacity), continuous improvement.

| Term | Meaning |
|---|---|
| Product Backlog | All possible work: features, fixes, enhancements |
| Sprint Backlog | Items chosen for this sprint plus the plan to deliver them |
| Increment | Working output at the end of the sprint |
| Definition of Done | Quality checklist an item must meet to count as complete |

## 2. GoF creational patterns (Java)

Creational patterns answer *"how do we create objects cleanly?"* Instead of `new` everywhere, they control object creation so code is easier to change, test and extend.

### 2.1 Singleton

Only **one object** of the class exists, with one global way to reach it (config, logger, cache).

```java
public enum AppConfig {              // simplest, thread-safe, serialization-safe
    INSTANCE;
    private String mode = "DEV";
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
}
// AppConfig.INSTANCE.setMode("PROD");
```

Lazy, thread-safe version (double-checked locking):

```java
public class Logger {
    private static volatile Logger instance;
    private Logger() { }                         // stops "new Logger()"
    public static Logger getInstance() {
        if (instance == null) {                  // 1st check
            synchronized (Logger.class) {
                if (instance == null) {          // 2nd check
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
    public void log(String msg) { System.out.println("LOG: " + msg); }
}
```

`volatile` here stops another thread from seeing a half-constructed object.

### 2.2 Factory Method

Call a method that **decides which class to create**; the caller depends only on the interface.

```java
interface Notification { void send(String message); }
class EmailNotification implements Notification {
    public void send(String m) { System.out.println("Email: " + m); }
}
class SmsNotification implements Notification {
    public void send(String m) { System.out.println("SMS: " + m); }
}
class NotificationFactory {
    public static Notification create(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms"   -> new SmsNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
// NotificationFactory.create("email").send("Your OTP is 1234");
```

### 2.3 Abstract Factory

A "factory of factories" that creates **families of related objects** that must match (Windows vs Mac widgets).

```java
interface Button { void paint(); }
interface Checkbox { void paint(); }
class WindowsButton implements Button { public void paint() { System.out.println("Windows Button"); } }
class WindowsCheckbox implements Checkbox { public void paint() { System.out.println("Windows Checkbox"); } }
class MacButton implements Button { public void paint() { System.out.println("Mac Button"); } }
class MacCheckbox implements Checkbox { public void paint() { System.out.println("Mac Checkbox"); } }

interface UIFactory { Button createButton(); Checkbox createCheckbox(); }
class WindowsUIFactory implements UIFactory {
    public Button createButton() { return new WindowsButton(); }
    public Checkbox createCheckbox() { return new WindowsCheckbox(); }
}
class MacUIFactory implements UIFactory {
    public Button createButton() { return new MacButton(); }
    public Checkbox createCheckbox() { return new MacCheckbox(); }
}
// UIFactory f = new WindowsUIFactory();  f.createButton().paint();
```

Switching one factory switches the whole family.

### 2.4 Builder

Build a complex object **step by step**, useful when there are many optional fields and constructors like `new User(a, b, c, d, e…)` become unreadable.

```java
class User {
    private final String name, email;
    private final int age;
    private User(Builder b) { name = b.name; email = b.email; age = b.age; }
    static class Builder {
        private final String name;
        private String email = "";
        private int age;
        Builder(String name) { this.name = name; }
        Builder email(String e) { email = e; return this; }
        Builder age(int a) { age = a; return this; }
        User build() { return new User(this); }
    }
}
// User u = new User.Builder("Ali").email("ali@mail.com").age(21).build();
```

### 2.5 Prototype

Create a new object by **cloning** an existing one – good when creation is expensive or many similar objects are needed.

```java
class Document implements Cloneable {
    String title, content;
    Document(String t, String c) { title = t; content = c; }
    @Override public Document clone() {
        try { return (Document) super.clone(); }          // shallow copy
        catch (CloneNotSupportedException e) { throw new RuntimeException(e); }
    }
}
// Document copy = template.clone(); copy.title = "Invoice #1001";
```

`clone()` is shallow by default; nested objects need a deep copy.

## 3. `std::atomic` (C++) and `volatile` (C++ and Java)

### C++ `std::atomic` – safe shared variables for threads

An atomic operation happens as **one indivisible step**, so two threads cannot mix their updates.

```cpp
#include <atomic>
#include <thread>
#include <iostream>
std::atomic<int> counter{0};
void work() { for (int i = 0; i < 100000; i++) counter++; }
int main() {
    std::thread t1(work), t2(work);
    t1.join(); t2.join();
    std::cout << counter << "\n";   // always 200000; a plain int could lose updates
}
```

### C++ `volatile` – "don't optimise this memory away"

The compiler must really read/write the variable every time, because the memory can change outside the program's normal flow:

- memory-mapped hardware registers (embedded systems),
- variables changed by an interrupt handler,
- sometimes signal handlers.

```cpp
volatile int* statusReg = (int*)0x40000000;
int readStatus() { return *statusReg; }   // re-read on every call
```

C++ `volatile` is **not** for threads – it gives no atomicity and no thread visibility. It also does not pull new values from a database, file, keyboard or network, and a process ID does not need it; those are read through API calls. For threads use `std::atomic<T>` or a mutex:

```cpp
std::atomic<bool> stop{false};             // not: volatile bool stop
```

### Java `volatile` – visibility between threads

Every read/write goes to main memory, so other threads always see the latest value, and reordering around the variable is limited.

```java
class StopExample {
    private static volatile boolean stop = false;
    public static void main(String[] args) throws Exception {
        Thread worker = new Thread(() -> {
            while (!stop) { /* work */ }
            System.out.println("Stopped!");
        });
        worker.start();
        Thread.sleep(1000);
        stop = true;                        // worker sees the change
    }
}
```

Java `volatile` does **not** make `count++` safe (it is read-modify-write); use `AtomicInteger`, `synchronized` or a lock.

### Summary

| | Purpose | Thread-safe for `++`? |
|---|---|---|
| C++ `std::atomic` | Safe shared reads/writes between threads | Yes |
| C++ `volatile` | Stop compiler optimisation (hardware, interrupts) | No |
| Java `volatile` | Threads see the latest value | No |
