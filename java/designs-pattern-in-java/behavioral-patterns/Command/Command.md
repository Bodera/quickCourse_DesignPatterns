# Command

## Simple Commodore

The command design pattern is typically illustrated with one example which is of a bank account that you modify. Here we're going to do exactly that.

```java
class BankAccount 
{
    private int balance;
    private int overdraftLimit = -500; // how much you can borrow

    public void deposit(int amount)
    {
        balance += amount;
        System.out.println("Deposited " + amount + ", balance is now " + balance);
    }

    public void withdraw(int amount) 
    {
        if (balance - amount >= overdraftLimit) {
            balance -= amount;
            System.out.println("Withdrew " + amount + ", balance is now " + balance);
        }
    }

    @Override
    public String toString() 
    {
        return "BankAccount{" +
                "balance=" + balance +
                '}';
    }
}
```

Now we can try manipulating that bank account. We've made methods where you can perform modifications to it directly, and that's just fine, but what we really want to do is perform some sort of *audit*. We want to have a record of every single deposit and withdrawal that happens, and we don't really want to modify the bank account with some sort of logging interface being injected in order to do this. In other words, we want to process the whole thing differently, and for that we are going to have the command design pattern.

Specifically, we're going to build a class called `BankAccountCommand` which is going to encapsulate the idea of performing some operation on a bank account. There aren't too many operations that you can do, is just deposit and withdrawal basically. What we're also going to implement is an interface called `Command` that holds a single method called `execute` that is needed to actually perform the command to something or other, not limited to a bank account, hopefully you'll notice it's a very abstract kind of thing.

We have to specify what kind of bank account this command operates on, and what kind of action we want to perform on the bank account, and the amount that we want to either deposit or withdraw. 

```java
interface Command
{
    void execute();
}

class BankAccountCommand 
{
    public enum Action 
    {
        DEPOSIT, WITHDRAW
    }

    private BankAccount account;
    private Action action;
    private int amount;

    public BankAccountCommand(BankAccount account, Action action, int amount) 
    {
        this.account = account;
        this.action = action;   
        this.amount = amount;
    }
}
```

The next step is to implement the command method. First things first, we need to figure out what kind of action should be performed on the method is called.

```java
class BankAccountCommand implements Command 
{
    //...

    @Override
    public void execute() 
    {
        switch (action) {
            case DEPOSIT:
                account.deposit(amount);
                break;
            case WITHDRAW:
                account.withdraw(amount);
                break;
            default:
                break;
        }
    }
}
```

After all of this set up, we can now perform operations on the bank account using this new functionality that we've just built. In addition to just making a single command for the bank account, or a single process, what we're going to do is make a list of them.

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        BankAccount account = new BankAccount();
        System.out.println("Initial balance: " + account);

        List<BankAccountCommand> commands = new ArrayList<>();
        BankAccountCommand deposit100 = new BankAccountCommand(account, BankAccountCommand.Action.DEPOSIT, 100);
        BankAccountCommand withdraw25 = new BankAccountCommand(account, BankAccountCommand.Action.WITHDRAW, 25);
        BankAccountCommand withdraw600 = new BankAccountCommand(account, BankAccountCommand.Action.WITHDRAW, 600);
        commands.add(deposit100);
        commands.add(withdraw25);
        commands.add(withdraw600);

        for (BankAccountCommand command : commands) 
        {
            command.execute();
        }
    }
}
```

Output:

```txt
Initial balance: BankAccount{balance=0}
Deposited 100, balance is now 100
Withdrew 25, balance is now 75
```

We're starting out with an account with a balance of zero, then we deposit 100 to it, proceed to withdraw 25, and finally we try to withdraw 600 exceeding the overdraft limit, so the balance of the account doesn't change. The output is what you would expect.

This is a very simple illustration of how you can make bank account commands, and you can actually get them to process themselves because essentially what happens in the `execute` method is telling the command to apply itself to the appropriate bank account and perform the appropriate action.

## Undo operations

In case you're wondering why we used an interface for command, well one of the reasons is not only do I want to apply the commands, but I might also want to undo the operation inside the commands. So a very simple implementation would be something like adding an `undo` method to the `Command` interface, and then we can implement that method in the `BankAccountCommand` class.

And let's suppose that we agreed that the deposit and withdrawal operations are symmetric, so to undo a deposit you have to do a withdrawal and vice versa, this is fundamentally incorrect but let's stick with it for the sake of the demo.

```java
interface Command 
{
    void execute();
    void undo();
}

class BankAccountCommand implements Command 
{
    //...

    @Override
    public void undo() 
    {
        switch (action) {
            case DEPOSIT:
                account.withdraw(amount);
                break;
            case WITHDRAW:
                account.deposit(amount);
                break;
            default:
                break;
        }
    }
}
```

At first sight that implementation might seem like a good idea but if we go back to our demo and add the undo functionality, we will see that the output is not what we expected.

```java
public class Demo 
{
    public static void main(String[] args) 
    {
        BankAccount account = new BankAccount();
        System.out.println("Initial balance: " + account);

        List<BankAccountCommand> commands = new ArrayList<>();
        BankAccountCommand deposit100 = new BankAccountCommand(account, BankAccountCommand.Action.DEPOSIT, 100);
        BankAccountCommand withdraw25 = new BankAccountCommand(account, BankAccountCommand.Action.WITHDRAW, 25);
        BankAccountCommand withdraw600 = new BankAccountCommand(account, BankAccountCommand.Action.WITHDRAW, 600);
        commands.add(deposit100);
        commands.add(withdraw25);
        commands.add(withdraw600);

        for (Command command : commands) 
        {
            command.execute();
        }

        Collections.reverse(commands);
        for (Command command : commands) 
        {
            command.undo();
        }
    }
}
```

Output:

```txt
Initial balance: BankAccount{balance=0}
Deposited 100, balance is now 100
Withdrew 25, balance is now 75
Deposited 600, balance is now 675
Deposited 25, balance is now 700
Withdrew 100, balance is now 600
```

We never had withdrawn 600, but still our command considers it in the undo operation resulting on an incorrect final balance of 600 instead of 0.

What we can do about this? First we have to understand the problem which in this case is that we never know whether a command actually succeeded, and if it didn't succeed there is no point in undoing it. So we're going to modify the command structure in order to actually indicate whether a bank account command succeeded. Let's add a boolean field called `success` to the `BankAccountCommand` class and modify our API a little.

```java
class BankAccountCommand implements Command 
{
    //...
    private boolean succeeded;

    @Override
    public void execute() 
    {
        switch (action) {
            case DEPOSIT:
                account.deposit(amount);
                break;
            case WITHDRAW:
                succeeded = account.withdraw(amount);
                break;
            default:
                break;
        }
    }

    @Override
    public void undo() 
    {
        if (!succeeded) return;
        switch (action) {
            case DEPOSIT:
                account.withdraw(amount);
                break;
            case WITHDRAW:
                account.deposit(amount);
                break;
            default:
                break;
        }
    }
}

class BankAccount 
{
    //...

    public boolean withdraw(int amount) 
    {
        if (balance - amount >= overdraftLimit) 
        {
            balance -= amount;
            System.out.println("Withdrew " + amount + ", balance is now " + balance);

            return true;
        }
        return false;
    }
}
```

Now our output looks like this:

```txt
Initial balance: BankAccount{balance=0}
Deposited 100, balance is now 100
Withdrew 25, balance is now 75
Deposited 25, balance is now 100
```

Hold on, shouldn't the balance be 0? It seems like the operation of deposit does never succeed, we should always update our flag `succeeded` to true when the action is equals `DEPOSIT`. Let's fix that.

```java
class BankAccountCommand implements Command 
{
    //...
    @Override
    public void execute() 
    {
        switch (action) {
            case DEPOSIT:
                account.deposit(amount);
                succeeded = true;
                break;
            case WITHDRAW:
                succeeded = account.withdraw(amount);
                break;
            default:
                break;
        }
    }
}
```

Now our output looks like this:

```txt
Initial balance: BankAccount{balance=0}
Deposited 100, balance is now 100
Withdrew 25, balance is now 75
Deposited 25, balance is now 100
Withdrew 100, balance is now 0
```

This little demo illustrates how you can implement undo and redo operations using the command design pattern.
