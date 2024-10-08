import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class UserFlyweightDemo
{
    public static void main(String[] args)
    {
        UsersNames user1 = new UsersNames("John Doe");
        UsersNames user2 = new UsersNames("Jane Doe");
        UsersNames user3 = new UsersNames("Jane Smith");
        UsersNames user4 = new UsersNames("John Smith");
        UsersNames user5 = new UsersNames("Jane Smith Doe");
        UsersNames user6 = new UsersNames("John Smith Doe");
        UsersNames user7 = new UsersNames("Janet Smith Doe");

        System.out.println("---------------------------------");

        System.out.println(user1.getFullName());
        System.out.println(user2.getFullName());
        System.out.println(user3.getFullName());
        System.out.println(user4.getFullName());
        System.out.println(user5.getFullName());
        System.out.println(user6.getFullName());
        System.out.println(user7.getFullName());

        System.out.println("---------------------------------");

        System.out.println(user1.getAllNames());
        System.out.println(user7.getAllNames());
    }
}

class UsersNames
{
    private static List<String> namesCache = new ArrayList<>();
    private int[] namesReference;

    public UsersNames(String fullName)
    {
        Function<String, Integer> getOrAdd = (String name) -> {
            int idx = namesCache.indexOf(name);

            if (idx != -1) return idx;
            else {
                namesCache.add(name);
                return namesCache.size() - 1;
            }
        };

        namesReference = Arrays.stream(fullName.split(" "))
            .mapToInt(str -> getOrAdd.apply(str))
            .toArray();
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        for (int index : namesReference) {
            fullName.append(namesCache.get(index)).append(" ");
        }
        return fullName.toString().trim();
    }

    public String getAllNames() {
        return namesCache.toString();
    }
}