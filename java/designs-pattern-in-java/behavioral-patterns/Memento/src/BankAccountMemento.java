class BankAccountMemento {
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

class BankAccount 
{
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public BankAccountToken deposit(int amount) {
        balance += amount;
        return new BankAccountToken(balance);
    }

    public BankAccountToken withdraw(int amount) {
        balance -= amount;
        return new BankAccountToken(balance);
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

    public void restore(BankAccountToken token) {
        balance = token.getBalance();
    }
}

class BankAccountToken 
{
    private final int balance;

    public BankAccountToken(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }
}
