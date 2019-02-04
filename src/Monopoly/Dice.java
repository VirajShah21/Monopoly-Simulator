package Monopoly;

class Dice {
    static int roll1() {
        return (int) (Math.random() * 6) + 1;
    }

    static int[] roll2() {
        int r1 = roll1();
        int r2 = roll1();
        return new int[]{r1, r2};
    }
}
