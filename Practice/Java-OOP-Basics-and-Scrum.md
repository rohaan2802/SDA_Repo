# Java OOP Basics, Use Cases and Scrum

Short revision notes for SDA: interfaces, packages and abstract classes in Java, the main OOP ideas compared with C++, what a use case is, and Scrum.

## 1. Interface, package and abstract class

| Concept | In one line | Think of it as |
|---|---|---|
| **Interface** | Only method declarations; an implementing class must write the bodies | A contract |
| **Package** | A group of related classes and interfaces | A folder |
| **Abstract class** | Cannot be instantiated; may have abstract *and* normal methods | A half-finished template |

```java
interface Animal {
    void sound();                 // no body – the "rule"
}
class Dog implements Animal {
    public void sound() { System.out.println("Bark"); }
}
```

```java
package mypack;

public class MyClass {
    public void display() { System.out.println("Hello from package!"); }
}
```

```java
abstract class Shape {
    abstract void draw();         // must be implemented by a child
    void info() { System.out.println("This is a shape."); }
}
class Circle extends Shape {
    void draw() { System.out.println("Drawing Circle"); }
}
```

### Calling a normal method of an abstract class

An abstract class has no objects of its own, so create a **child** object and call the method through it:

```java
public class Main {
    public static void main(String[] args) {
        Shape s = new Circle();   // parent reference, child object
        s.draw();                 // child's method   -> Drawing Circle
        s.info();                 // inherited method -> This is a shape.
    }
}
```

## 2. Package vs C/C++ header file

| | C/C++ header (`#include`) | Java package (`import`) |
|---|---|---|
| Purpose | Reuse code from another file | Reuse code from another package |
| Contains | Mostly declarations; bodies live in `.c`/`.cpp` files | Compiled `.class` files with full implementations |
| How it works | The preprocessor pastes the text into your file | The compiler is only told where to find the class |

```java
package animals;
public class Dog { public void bark() { System.out.println("Woof!"); } }
```
```java
import animals.Dog;   // similar in spirit to #include
public class Main {
    public static void main(String[] args) { new Dog().bark(); }
}
```

So a package can be imported like a header, but it is a whole toolbox of ready classes, not just a list of declarations. A large program is split into many packages.

## 3. Advantages of OOP

1. **Reusability** – write a class once and reuse it (a `Car` class for many cars).
2. **Modularity** – code is split into small independent classes that are easy to debug.
3. **Abstraction** – show only what is needed (`driveCar()` without knowing the engine).
4. **Encapsulation** – private data reached only through methods.
5. **Inheritance** – build new classes from existing ones.
6. **Polymorphism** – one method name, behaviour depends on the object (`draw()` for `Circle`, `Square`).
7. **Maintainability** and 8. **Scalability** – well-structured code is easier to change and grow.

## 4. Encapsulation

Data hiding plus controlled access through getters and setters:

```java
class Student {
    private String name;                       // hidden from outside
    public void setName(String n) { name = n; }
    public String getName() { return name; }
}
```

## 5. Polymorphism in Java and C++

"One thing, many forms."

```java
class Calculator {                             // compile time: overloading
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }
}
class Animal { void sound() { System.out.println("Animal makes sound"); } }
class Dog extends Animal { void sound() { System.out.println("Dog barks"); } }
// Animal a = new Dog(); a.sound();  -> Dog barks   (run time: overriding)
```

```cpp
class Complex {                                // C++ also has operator overloading
public:
    int real, imag;
    Complex(int r = 0, int i = 0) : real(r), imag(i) {}
    Complex operator+(const Complex& c) { return Complex(real + c.real, imag + c.imag); }
};
class Animal { public: virtual void sound() { std::cout << "Animal sound\n"; } };
class Dog : public Animal { public: void sound() override { std::cout << "Dog barks\n"; } };
// Animal* a = new Dog(); a->sound();  -> Dog barks
```

| | Java | C++ |
|---|---|---|
| Compile-time | Method overloading | Function and operator overloading |
| Run-time | Method overriding | `virtual` functions |
| Marker | `@Override` (optional) | `virtual` / `override` |
| Operator overloading | Not allowed | Allowed |

With one parent and several children (e.g. `Dog`, `Cat`, `Cow` extending `Animal`), a parent reference can hold any child, and the call runs the child's own version at run time.

## 6. Use case

A **use case** describes how a user interacts with the system to reach one goal – a short story of use.

*ATM – Withdraw Cash:* insert card → enter PIN → choose Withdraw → enter amount → receive money.

- **Feature** = what the system can do ("Add to Cart").
- **Use case** = the step-by-step way a user uses that feature to reach a goal (customer picks a product, clicks *Add to Cart*, the system stores it for checkout).
- A use case is not progress tracking and not just a feature list.

## 7. Scrum

Scrum is an Agile framework for building complex software in short iterations with continuous feedback.

- **Key features:** iterative and incremental (Sprints of 2–4 weeks), self-organising team, regular customer feedback, transparency.
- **Roles:** Product Owner (goals, priorities, Product Backlog), Scrum Master (follows the rules, removes obstacles), Development Team (cross-functional, delivers the work).
- **Artifacts:** Product Backlog, Sprint Backlog, Increment.
- **Events:** Sprint Planning, Daily Scrum (15-minute stand-up), Sprint Review, Sprint Retrospective.
- **Advantages:** faster delivery, adapts to changing requirements, better teamwork, continuous feedback.

> **Exam definition:** Scrum is an Agile framework in which a self-organising team delivers working software in short time-boxed Sprints, guided by a prioritised Product Backlog and regular inspection and adaptation.
