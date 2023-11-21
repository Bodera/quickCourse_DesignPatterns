class InnerStaticSingleton
{
    private InnerStaticSingleton(){}

    private static class Impl
    {
        private static final InnerStaticSingleton INSTANCE = new InnerStaticSingleton();
    }

    public InnerStaticSingleton getInstance()
    {
        return Impl.INSTANCE;
    }
}