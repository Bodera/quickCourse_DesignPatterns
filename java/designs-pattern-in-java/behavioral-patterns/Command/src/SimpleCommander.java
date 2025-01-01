import java.util.ArrayList;
import java.util.List;

public class SimpleCommander 
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

        for (Command command : commands.reversed()) 
        {
            command.undo();
        }
    }
}

interface Command 
{
    void execute();
    void undo();
}

class BankAccountCommand implements Command 
{
    public enum Action 
    {
        DEPOSIT, WITHDRAW
    }

    private BankAccount account;
    private Action action;
    private int amount;
    private boolean succeeded;

    public BankAccountCommand(BankAccount account, Action action, int amount) 
    {
        this.account = account;
        this.action = action;   
        this.amount = amount;
    }

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
    private int balance;
    private int overdraftLimit = -500; // how much you can borrow

    public void deposit(int amount)
    {
        balance += amount;
        System.out.println("Deposited " + amount + ", balance is now " + balance);
    }

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

    @Override
    public String toString() 
    {
        return "BankAccount{" +
                "balance=" + balance +
                '}';
    }
}
