import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandmadeStatePattern {

    private static Map<PhoneState, List<Map.Entry<PhoneTrigger, PhoneState>>>
        machineRules = new HashMap<>();

    static
    {
        machineRules.put(PhoneState.OFF_HOOK, List.of(
            Map.entry(PhoneTrigger.DIAL, PhoneState.CONNECTING),
            Map.entry(PhoneTrigger.FINISH, PhoneState.ON_HOOK)
        ));
    
        machineRules.put(PhoneState.CONNECTING, List.of(
            Map.entry(PhoneTrigger.HANG_UP, PhoneState.OFF_HOOK),
            Map.entry(PhoneTrigger.ANSWER, PhoneState.CONNECTED)
        ));
    
            machineRules.put(PhoneState.CONNECTED, List.of(
            Map.entry(PhoneTrigger.LEFT_MSG, PhoneState.OFF_HOOK),
            Map.entry(PhoneTrigger.HANG_UP, PhoneState.OFF_HOOK),
            Map.entry(PhoneTrigger.ON_HOLD, PhoneState.ON_HOLD)
        ));
    
            machineRules.put(PhoneState.ON_HOLD, List.of(
            Map.entry(PhoneTrigger.OFF_HOLD, PhoneState.CONNECTED),
            Map.entry(PhoneTrigger.HANG_UP, PhoneState.OFF_HOOK)
        ));
    }

    private static PhoneState currentState = PhoneState.OFF_HOOK;
    private static PhoneState exitState = PhoneState.ON_HOOK;

    public static void main(String[] args)
    {
        BufferedReader consoleReader = new BufferedReader(
            new InputStreamReader(System.in)
        );

        while (true)
        {
            System.out.println("The phone is currently in " + currentState + " state.");
            System.out.println("What do you want to do?");

            for (int i = 0; i < machineRules.get(currentState).size(); i++)
            {
                PhoneTrigger trigger = machineRules.get(currentState).get(i).getKey();
                System.out.println("" + i + ": " + trigger);
            }

            boolean parseOk;
            int choice = 0;
            do
            {
                try {
                    System.out.println("Inform your choice: ");
                    choice = Integer.parseInt(consoleReader.readLine());
                    parseOk = choice >= 0 && choice < machineRules.get(currentState).size();
                } catch (IOException e) {
                    parseOk = false;
                } catch (NumberFormatException e) {
                    parseOk = false;
                }
            } while (!parseOk);

            currentState = machineRules.get(currentState).get(choice).getValue();

            if (currentState == exitState) break;
        }

        System.out.println("The phone is now in " + currentState + " state.");
    }
}

enum PhoneState
{
    OFF_HOOK,
    ON_HOOK,
    CONNECTING,
    CONNECTED,
    ON_HOLD
}

enum PhoneTrigger
{
    DIAL,
    ANSWER,
    HANG_UP,
    ON_HOLD,
    OFF_HOLD,
    LEFT_MSG,
    FINISH
}
