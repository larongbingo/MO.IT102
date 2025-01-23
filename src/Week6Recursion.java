import java.util.HashMap;

public class Week6Recursion {
    public static void main(String[] args) {
        System.out.println(CachedRecursiveFibonacci.recursiveFibonacci(20));
    }
}

class CachedRecursiveFibonacci {
    private static HashMap<Integer, Integer> results = new HashMap<>() {{
        put(1,1);
        put(2,1);
    }};

    public static int recursiveFibonacci(int n) {
        if (n <= 0) {
            return 0;
        }

        if (results.containsKey(n)) {
            return results.get(n);
        }

        var result = recursiveFibonacci(n - 1) + recursiveFibonacci(n - 2);

        // Cache to skip running previously computed fib number
        results.put(n, result);

        return result;
    }
}
