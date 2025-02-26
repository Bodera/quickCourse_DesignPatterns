# Memento

When we looked at the _Command_ pattern we saw that you can record a sequence of commands, apply to a particular object, and then play them back to sort of roll back their states.

This isn't the only way that you can do this because imagine a typical bank account, the bank account has just one piece of state called a **balance**, and you might want to simply roll back the balance to a particular state by essentially giving the user a kind of token and that token happens to be the _Memento_ design pattern.

We're going to implement a simple bank account which allows you to deposit and withdraw money, and then you can roll back the account to a particular state.

```java
class BankAccount 
{
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                '}';
    }
}
```

The idea essentially is that you can have each operation, such as `deposit()` for example, return a snapshot of the internal state of the bank account so that this snapshot can be used to restore the bank account to a previous state. Such like:

```java
class BankAccountToken 
{
    public int balance;

    public BankAccountToken(int balance) {
        this.balance = balance;
    }
}
```

So a memento is a snapshot of the internal state of the bank account, our bank account has just a single field that you might want to restore that's balance, so the memento is going to store precisely that.

The idea is that instead of having your operations defined as `void` you make them return a memento. This allows you to roll back subsequently the deposit operation back to what it was before. So after the operation has succeeded we returned a new memento specifying the balance we actually had.

```java
class BankAccount
{
    // ...

    public BankAccountToken deposit(int amount) {
        balance += amount;
        return new BankAccountToken(balance);
    }
}
```

Before we start using this API we need to have a method for actually restoring the memento.

```java
class BankAccount 
{
    // ...

    public void restore(BankAccountToken token) {
        balance = token.balance;
    }
}
```

And now we can use the memento to roll back the account to a particular state.

```java
public class Demo
{
    public static void main(String[] args) {
        BankAccount account = new BankAccount(100);
        BankAccountToken t1 = account.deposit(50);
        BankAccountToken t2 = account.deposit(25);
        System.out.println(account);

        account.restore(t1);
        System.out.println(account);

        account.restore(t2);
        System.out.println(account);
    }
}
```

Output:

```txt
BankAccount{balance=175}
BankAccount{balance=150}
BankAccount{balance=175}
```

This is a simple example of how you implement the memento pattern. There's only one limitation, we're not getting a memento for the constructor of `BankAccount`. How we solve that is up to you, for example you could disallow a constructor which sets the balance so would require the users to set the initial balance explicitly. Unfortunately the constructor can't really return a memento by itself, a constructor returns a reference to the new initialized object.

Typically, the memento should be a read-only structure because otherwise you can restore the system to a state that the system was never in, which seems a bit unfair and can be used maliciously, it's best to keep the memento as an immutable kind of construct.

Another limitation to memento is the amount of memory that you're going to occupy. Imagine that you have a huge structure with lots of variables, and you're returning a memento on every turn. Of course that if those mementos are not saved anywhere they're going to be garbage collected, but if someone decides to store every single memento then it's going to be a problem in terms of the amount of memory that's required to actually keep every single piece of information. That's precisely the case where you might want to use the command design pattern instead, it just lists the set of changes not the set of states. And changes are typically much smaller than the overall number of states especially if you have lots of states.

## Using memento for interoperability

One of the things that you can use the memento for is interoperability between different languages. How?

Suppose that you have some C++ or C library, if they expose ordinary top levels functions like:

```cpp
int f(int x);
```

This is something that's reasonably easy to call from a place like Java or other programming language by setting up a kind of native interface connection and that's good.

The problem with interoperability with C and C++ is when you have **classes**. Let's say that on the C++ side you have a class called `Foo` and inside that you have some `void` function.

```cpp
class Foo
{
    void bar(int z);
}
```

The problem is that you cannot just go ahead and instantiate the `Foo` class inside Java or whatever the other language is, it simply does not work. So, what does the memento pattern have to do with this?

One of the things you can do is, you can leave all of these objects back in the sort of native or unmanaged side of constructs. So imagine that somewhere on the unmanaged side of things you have a unit store, somewhere on memory where when somebody needs a `Foo` they actually allocate the appropriate memory and put that reference of `Foo` there. On the Java side, or whatever language, what you can do to operate on this `Foo` object is to interact with top level functions exposed on the C/C++ side.

For example a function like:

```cpp
Memento createFoo(int x, int y);
```

The memento returned by the `createFoo()` function could be for example an integer which refers to the location of `Foo` inside some cache or other kind of storage unit. On the Java side you end up getting this integer and so through this integer, which is actually a memento, you can now operate on a `Foo` object.

If you have some method that takes an instance of `Foo` as argument, you can elaborate on some overload which accepts a memento and just pass the memento as an argument.

That's the point of using a memento for interoperability. You can use a memento to pass around a pointer to a class or a class instance, and then you can use the memento to call methods on the class instance. You know that you can't pass objects across boundaries only primitives, so what about passing a primitive which happens to be a memento or a token to the underlying object and then creating some APIs for actually exposing that object through the memento allowing operations on it.
