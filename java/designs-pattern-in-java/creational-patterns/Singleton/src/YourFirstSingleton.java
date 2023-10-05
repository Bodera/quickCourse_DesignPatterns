class BasicSingleton
{
    private BasicSingleton()
    {
    }

    private static final BasicSingleton INSTANCE = new BasicSingleton();

    public static BasicSingleton getInstance()
    {
        return INSTANCE;
    }

    private int secret = 100;

    public int getSecret()
    {
        return secret;
    }

    public void setSecret(int secret)
    {
        this.secret = secret;
    }
}

class YourFirstSingleton
{
    public static void main(String[] args)
    {
        BasicSingleton carrier = BasicSingleton.getInstance();

        System.out.println(carrier.getSecret());

        carrier.setSecret(carrier.getSecret() * 10);
        System.out.println(carrier.getSecret());
    }
}