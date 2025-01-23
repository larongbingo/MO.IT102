import java.util.HashMap;

public class Week6HashMap {
    public static void main(String[] args) {
        System.out.println(firstNonRepeatingCharacter("ASDD"));
        System.out.println(firstNonRepeatingCharacter("ASDQWE"));
    }

    // Not necessary to use hashmap if you only require to get the first non-unique character
    public static Character firstNonRepeatingCharacter(String input) {
        var hashMap = new HashMap<Character, Integer>();

        for(var c : input.toCharArray()) {
            if (!hashMap.containsKey(c)) {
                hashMap.put(c, 1);
            } else {
                return c;
            }
        }

        return null;
    }
}
