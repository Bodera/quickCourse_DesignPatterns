# Your First Singleton

We're now going to get ourselves acquainted with the _Singleton_ pattern, to do that we're going to build a fairly basic singleton to begin with. In fact we're going to build a class which enforces the rule that should only ever be one instance of that class.

```java
class BasicSingleton
{

}
```

Ok so how do you prevent people from making additional copies of that class? First of all you can add a private constructor so that no one could be able to create that class.

```java
class BasicSingleton
{
    private BasicSingleton()
    {

    }
}

class Demo
{
    public static void main(String[] args)
    {
        new BasicSingleton();
    }
}
```

And that's the output this program produces:

```bash
> error: BasicSingleton() has private access in BasicSingleton
```

Okay, but if it can't be created by the constructor method how can it actually be exposed to the client? Well you can make just a single instance of it and then expose that instance.

```java
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

}
```

And that's pretty much it. Let's increase this class a bit to better comprehension.

```java
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
```

Let's go back to our main method and check it out after modifying `BasicSingleton`.

```java
class Demo
{
    public static void main(String[] args)
    {
        BasicSingleton carrier = BasicSingleton.getInstance();

        System.out.println(carrier.getSecret());

        carrier.setSecret(carrier.getSecret() * 10);
        System.out.println(carrier.getSecret());
    }
}
```

And that's the output this program produces:

```bash
100
1000
```

There are of course a lot of issues with our singleton as we're going to take a look soon at the subsequent lessons but this is the simplest approach that in fact works. The idea here to implement a _Singleton_ here is to make a private constructor so nobody can instantiate it, and then providing a static final instance of that object, the one and only one instance that we're ever going to expose.
