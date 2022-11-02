package org.example;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger threeSymbol = new AtomicInteger(0);
    public static AtomicInteger fourSymbol = new AtomicInteger(0);
    public static AtomicInteger fiveSymbol = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        String oneSymbolPattern = "([a-zA-Z])\\1*";

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (String text : texts) {
                if (text.matches(oneSymbolPattern)) {
                    int length = text.length();
                    if (length == 3) {
                        threeSymbol.incrementAndGet();
                    } else if (length == 4) {
                        fourSymbol.incrementAndGet();
                    } else {
                        fiveSymbol.incrementAndGet();
                    }
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (String text : texts) {
                if (!text.matches(oneSymbolPattern) && text.equals(new StringBuilder(text).reverse().toString())) {
                    int length = text.length();
                    if (length == 3) {
                        threeSymbol.incrementAndGet();
                    } else if (length == 4) {
                        fourSymbol.incrementAndGet();
                    } else {
                        fiveSymbol.incrementAndGet();
                    }
                }
            }
        });

        Thread thread3 = new Thread(() -> {
            for (String text : texts){
                if (!text.matches(oneSymbolPattern) && !text.equals(new StringBuilder(text).reverse().toString())) {
                    char[] chars = text.toCharArray();
                    Arrays.sort(chars);
                    if (text.equals(new String(chars))){
                        int length = text.length();
                        if (length == 3) {
                            threeSymbol.incrementAndGet();
                        } else if (length == 4) {
                            fourSymbol.incrementAndGet();
                        } else {
                            fiveSymbol.incrementAndGet();
                        }
                    }
                }

            }
        });


        thread1.start();
        thread1.join();
        thread2.start();
        thread2.join();
        thread3.start();
        thread3.join();

        System.out.println("Красивых слов с длиной 3: " + threeSymbol.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + fourSymbol.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + fiveSymbol.get() + " шт");

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}