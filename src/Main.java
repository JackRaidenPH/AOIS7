import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    static GLFlagPair compareByMSB(boolean[] first, boolean[] second) {
        for (int bit = Math.min(first.length, second.length) - 1; bit >= 0; bit--) {
            if (first[bit] ^ second[bit]) {
                if (first[bit])
                    return new GLFlagPair(true, false);
                else
                    return new GLFlagPair(false, true);
            }
        }
        return new GLFlagPair(false, false);
    }

    static boolean[][] randomArray(int count, int entrySize) {
        boolean[][] result = new boolean[count][];
        for (int entryIndex = 0; entryIndex < count; entryIndex++) {
            boolean[] entry = new boolean[entrySize];
            for (int bit = 0; bit < entrySize; bit++) {
                entry[bit] = new Random(System.nanoTime()).nextBoolean()
                        ^ new Random(System.nanoTime()).nextBoolean()
                        & new Random(System.nanoTime()).nextBoolean()
                        ^ new Random(System.nanoTime()).nextBoolean();
            }
            result[entryIndex] = entry;
        }
        return result;
    }

    static String stringifyArray(boolean[][] array) {
        StringBuilder builder = new StringBuilder();
        for (boolean[] entry : array) {
            StringBuilder entryBuilder = new StringBuilder();
            for (boolean bit : entry) {
                entryBuilder.append(bit ? "1 " : "0 ");
            }
            builder.append(entryBuilder.reverse()).append(";\n");
        }
        return builder.toString();
    }

    static boolean[][] searchInRange(boolean[][] array, boolean[] lower, boolean[] upper) {
        List<boolean[]> result = new ArrayList<>();
        for (boolean[] entry : array) {
            if (!compareByMSB(entry, upper).g() && !compareByMSB(entry, lower).l())
                result.add(entry);
        }
        return result.toArray(boolean[][]::new);
    }

    static boolean[][] searchByMask(boolean[][] array, String mask) {
        List<boolean[]> result = new ArrayList<>();
        for (boolean[] entry : array) {
            boolean accept = true;
            for (int bit = 0; bit < entry.length; bit++) {
                char charBit = mask.charAt(mask.length() - 1 - bit);
                if (!(
                        (charBit == '1' && entry[bit])
                                || (charBit == '0' && !entry[bit])
                                || (charBit == 'X')
                )) {
                    accept = false;
                    break;
                }
            }
            if (accept) {
                result.add(entry);
            }
        }
        return result.toArray(boolean[][]::new);
    }

    public static void main(String[] args) {
        boolean[][] arr = randomArray(10, 5);
        System.out.println(stringifyArray(arr));
        boolean[] three = new boolean[]{true, true, false, false, false};
        boolean[] seven = new boolean[]{true, true, true, false, false};
        System.out.println(stringifyArray(searchInRange(arr, three, seven)));
        System.out.println(stringifyArray(searchByMask(arr,"1X001")));
    }
}

record GLFlagPair(boolean g, boolean l) {
}