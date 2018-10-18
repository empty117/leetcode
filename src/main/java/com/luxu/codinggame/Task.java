package com.luxu.codinggame;

/**
 * @author xulu
 * @date 10/11/2018
 */
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Bring data on patient samples from the diagnosis machine to the laboratory with enough molecules to produce medicine!
 **/
class Task {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int projectCount = in.nextInt();
        for (int i = 0; i < projectCount; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            int c = in.nextInt();
            int d = in.nextInt();
            int e = in.nextInt();
        }

        // game loop
        while (true) {
            for (int i = 0; i < 2; i++) {
                String target = in.next();
                int eta = in.nextInt();
                int score = in.nextInt();
                int storageA = in.nextInt();
                int storageB = in.nextInt();
                int storageC = in.nextInt();
                int storageD = in.nextInt();
                int storageE = in.nextInt();
                int expertiseA = in.nextInt();
                int expertiseB = in.nextInt();
                int expertiseC = in.nextInt();
                int expertiseD = in.nextInt();
                int expertiseE = in.nextInt();
            }
            int availableA = in.nextInt();
            int availableB = in.nextInt();
            int availableC = in.nextInt();
            int availableD = in.nextInt();
            int availableE = in.nextInt();
            int sampleCount = in.nextInt();
            for (int i = 0; i < sampleCount; i++) {
                int sampleId = in.nextInt();
                int carriedBy = in.nextInt();
                int rank = in.nextInt();
                String expertiseGain = in.next();
                int health = in.nextInt();
                int costA = in.nextInt();
                int costB = in.nextInt();
                int costC = in.nextInt();
                int costD = in.nextInt();
                int costE = in.nextInt();
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("GOTO DIAGNOSIS");
        }
    }
}
