class Monostate 
{
    public static void main(String[] args) {
        ChiefExecutiveOfficer ceo = new ChiefExecutiveOfficer("Evellyn", 31);
        ChiefExecutiveOfficer ceo_2 = new ChiefExecutiveOfficer();
        
        System.out.println(ceo);
        System.out.println(ceo_2);
    }
}

class ChiefExecutiveOfficer
{
    private static String name;
    private static int age;

    public ChiefExecutiveOfficer() {}

    public ChiefExecutiveOfficer(String name, int age) {
        ChiefExecutiveOfficer.name = name;
        ChiefExecutiveOfficer.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        ChiefExecutiveOfficer.name = name;
    }

    public void setAge(int age) {
        ChiefExecutiveOfficer.age = age;
    }

    @Override
    public String toString() {
        return "ChiefExecutiveOfficer [name=" + name + ", age=" + age + "]";
    }
}