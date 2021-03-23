//This solution should reverse an integer 123 -> 321

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        System.out.println(reverse(0));
    }

    public static int reverse(int x) {
        double minRange = Math.pow(-2, 31);
        double maxRange = Math.pow(2, 31) - 1;
        System.out.println(minRange + " " + maxRange);
        Scanner keyboardInput = new Scanner(System.in);
        x = keyboardInput.nextInt();
        double y = 0;
        while (x != 0) {
            y += x % 10;
            x /= 10;
            if (x != 0) {
                y *= 10;
            }
            if (y <= minRange || y >= maxRange) {
                return 0;
            }
        }
        return (int) y;
    }

}
