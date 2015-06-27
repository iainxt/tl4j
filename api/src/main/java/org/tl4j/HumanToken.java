package org.tl4j;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Iain
 * Date: 23/12/14
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class HumanToken {

    private static ArrayList<Character> niceCharacters = new ArrayList<Character>();
    private static Random random = new Random();
    private static int LENGTH = 6;// 6 should be enough, 1BIL combinations
    static
    {
        buildNiceCharacters();
    }
    /**
     * Generate a random human transferable string using 0-9 A-Z without easily confused characters 0,O,1,I.
     *
     * These tokens will not be unique, but should be unique enough with 1 billion combinations.
     *
     * @return
     */
    public static String next()
    {
        return next(LENGTH);
    }

    public static String next(int length)
    {
        final int numberOfCharacters=niceCharacters.size();
        final int maxNumber = (int)Math.pow(numberOfCharacters, length);
        int number = random.nextInt(maxNumber);
        String encoded = "";
        for (int i = 0;i < length; i++)
        {
            int r = number % numberOfCharacters;
            number = (number - r) / numberOfCharacters;
            encoded = niceCharacters.get(r) + encoded;
        }
        return encoded;
    }
    private static void buildNiceCharacters()
    {
        // Nice characters are all once you can easily tell which one it is.
        // It is easy to confuse:  0O and 1I
        // In addition, case would be ambiguous for users to say over the phone, so sticking to upper.
        //ABCDEFGHIJKLMNOPQRSTUVWXYZ
        //0123456789
        // 2-9 skipping 0,1
        for (int i = 2; i <= 9; i++)
        {
            niceCharacters.add((""+i).charAt(0));
        }
        // A-Z skipping O,I
        int A = String.valueOf('A').codePointAt(0);
        for (int i = 0; i < 26; i++)
        {
            char c = Character.toChars(i+A)[0];
            if (c == 'O' || c == 'I') continue;
            niceCharacters.add(c);
        }
    }
}
