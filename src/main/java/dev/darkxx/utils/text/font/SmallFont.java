package dev.darkxx.utils.text.font;

import java.util.HashMap;
import java.util.Map;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/text/font/SmallFont.class */
public class SmallFont {
    private static final Map<Character, Character> smallFontMap = new HashMap();

    static {
        smallFontMap.put('a', (char) 7424);
        smallFontMap.put('b', (char) 665);
        smallFontMap.put('c', (char) 7428);
        smallFontMap.put('d', (char) 7429);
        smallFontMap.put('e', (char) 7431);
        smallFontMap.put('f', (char) 42800);
        smallFontMap.put('g', (char) 610);
        smallFontMap.put('h', (char) 668);
        smallFontMap.put('i', (char) 618);
        smallFontMap.put('j', (char) 7434);
        smallFontMap.put('k', (char) 7435);
        smallFontMap.put('l', (char) 671);
        smallFontMap.put('m', (char) 7437);
        smallFontMap.put('n', (char) 628);
        smallFontMap.put('o', (char) 7439);
        smallFontMap.put('p', (char) 7448);
        smallFontMap.put('q', 'q');
        smallFontMap.put('r', (char) 640);
        smallFontMap.put('s', (char) 42801);
        smallFontMap.put('t', (char) 7451);
        smallFontMap.put('u', (char) 7452);
        smallFontMap.put('v', (char) 7456);
        smallFontMap.put('w', (char) 7457);
        smallFontMap.put('x', 'x');
        smallFontMap.put('y', (char) 655);
        smallFontMap.put('z', (char) 7458);
        smallFontMap.put('{', '{');
        smallFontMap.put('|', '|');
        smallFontMap.put('}', '}');
        smallFontMap.put('~', (char) 732);
    }

    public static String convert(String text) {
        String text2 = text.toLowerCase();
        StringBuilder result = new StringBuilder();
        for (char character : text2.toCharArray()) {
            if (smallFontMap.containsKey(Character.valueOf(character))) {
                result.append(smallFontMap.get(Character.valueOf(character)));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }
}
