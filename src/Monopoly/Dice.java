package Monopoly;

public class Dice {
    public static int roll1() {
        return (int) (Math.random() * 7);
    }

    public static int[] roll2() {
        int r1 = roll1();
        int r2 = roll1();
        return new int[]{r1, r2};
    }

    public static boolean isSnakeEyes(int[] pair) {
        return pair[0] == pair[1];
    }
}
