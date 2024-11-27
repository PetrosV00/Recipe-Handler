package org.example;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Usage: java -jar example-jar.jar <file>");
        }

        if (args.length == 1 && !args[0].equals("-list")) {

            IngredientList list = new IngredientList(10);
            CookwareList cookwareList = new CookwareList(10);

            String filepath = args[0];

            File file = new File(filepath);


            // Check if the file exists
            if (!file.exists()) {
                System.out.println("File does not exist: " + file.getAbsolutePath());
                return;
            }

            // Set up a BufferedReader to read the file
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedReader br2 = new BufferedReader(new FileReader(file));


            // Simplified regex pattern to match ingredients and quantities
            String pattern1 = "@([α-ωΑ-Ωά-ώΆ-Ώ\\s]{1,})+\\{([\\d]{1,})?+\\%?+([α-ωΑ-Ωά-ώΆ-Ώa-zA-z]{1,})?+\\}|\\@([α-ωΑ-Ωά-ώΆ-Ώ]{1,})";
            String pattern2 = "#([α-ωΑ-Ωά-ώΆ-Ώ\\s]{1,})+\\{+\\}|\\#([α-ωΑ-Ωά-ώΆ-Ώ]{1,})+\\{?+\\}?";
            String pattern3 = "~\\{+([\\d]{1,})+\\%+([a-zA-Z]{1,})+\\}";


            // Compile the regex pattern

            //Ingredient pattern
            Pattern p1 = Pattern.compile(pattern1);
            //Cookware pattern
            Pattern p2 = Pattern.compile(pattern2);
            //Time pattern
            Pattern p3 = Pattern.compile(pattern3);


            // Read the entire file content
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            // Print the content of the file for debugging
            //System.out.println("File content:\n" + content.toString());

            // Create a Matcher to find all matches in the content
            Matcher m1 = p1.matcher(content.toString());
            Matcher m2 = p2.matcher(content.toString());
            Matcher m3 = p3.matcher(content.toString());


            int i = 0;

            // Filling The list with the matching items
            while (m1.find()) {

                // Print out the ingredient and quantity
                if (m1.group(1) != null) {
                    Ingredient curr = new Ingredient();
                    curr.setName(m1.group(1));
                    curr.setQuantity(m1.group(2));
                    curr.setUnit(m1.group(3));

                    if (list.nameExists(curr.getName())) {
                        int j = list.getPosition(curr.getName());
                        int quan = Integer.parseInt(curr.getQuantity()) + Integer.parseInt(list.getIngredient(j).getQuantity());
                        String quantity = Integer.toString(quan);
                        list.getIngredient(j).setQuantity(quantity);
                    } else {
                        list.addIngredient(curr, i);
                        i++;
                    }
                }

                if (m1.group(4) != null) {
                    Ingredient curr = new Ingredient();
                    curr.setName(m1.group(4));

                    if (!list.nameExists(curr.getName())) {
                        list.addIngredient(curr, i);
                        i++;
                    }
                }

            }

            int j = 0;

            while (m2.find()) {
                if (m2.group(1) != null) {
                    Cookware curr = new Cookware();
                    curr.setName(m2.group(1));

                    if (cookwareList.exists(curr.getName())) {
                        continue;
                    } else {
                        cookwareList.add(curr, j);
                        j++;
                    }
                }

                if (m2.group(2) != null) {
                    Cookware curr = new Cookware();
                    curr.setName(m2.group(2));

                    if (cookwareList.exists(curr.getName())) {
                        continue;
                    } else {
                        cookwareList.add(curr, j);
                        j++;
                    }
                }
            }

            Time timeCurr = new Time();

            while (m3.find()) {
                if (m3.group(1) != null) {
                    if (timeCurr == null) {
                        timeCurr.setTime(Integer.parseInt(m3.group(1)));
                        timeCurr.setUnit(m3.group(2));
                    } else {
                        timeCurr.setTime(timeCurr.getTime() + Integer.parseInt(m3.group(1)));
                        timeCurr.setUnit(m3.group(2));
                    }
                }
            }


            ///////////////////////////////////////////////////////////////////////////////////////////
            // printing

            list.printList();
            cookwareList.printList();

            System.out.println("\nΣυνολική Ώρα:\n" + timeCurr.getTime() + " " + timeCurr.getUnit());

            System.out.println("\nΒήματα:\n");
            int k = 1;
            while (br2.read() != -1) {
                System.out.println(k + ". " + br2.readLine() + "\n");
                k++;
            }


            // Close the BufferedReader
            br.close();
            br2.close();

        } else if (args[0].equals("-list") && args.length >= 3) {

            IngredientList list = new IngredientList(100);

            for (String filepath : args) {
                File file = new File(filepath);

                if (!file.exists()) {
                    System.out.println("File does not exist: " + file.getAbsolutePath());
                    continue;
                }

                BufferedReader br = new BufferedReader(new FileReader(file));

                StringBuilder content = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }

                String pattern1 = "@([α-ωΑ-Ωά-ώΆ-Ώ\\s]{1,})+\\{([\\d]{1,})?+\\%?+([α-ωΑ-Ωά-ώΆ-Ώa-zA-z]{1,})?+\\}|\\@([α-ωΑ-Ωά-ώΆ-Ώ]{1,})";
                Pattern p1 = Pattern.compile(pattern1);
                Matcher m1 = p1.matcher(content.toString());

                int i = 0;

                // Filling The list with the matching items
                while (m1.find()) {

                    // Print out the ingredient and quantity
                    if (m1.group(1) != null) {
                        Ingredient curr = new Ingredient();
                        curr.setName(m1.group(1));
                        curr.setQuantity(m1.group(2));
                        curr.setUnit(m1.group(3));

                        if (list.nameExists(curr.getName())) {
                            int j = list.getPosition(curr.getName());
                            int quan = Integer.parseInt(curr.getQuantity()) + Integer.parseInt(list.getIngredient(j).getQuantity());
                            String quantity = Integer.toString(quan);
                            list.getIngredient(j).setQuantity(quantity);
                        } else {
                            list.addIngredient(curr, i);
                            i++;
                        }
                    }

                    if (m1.group(4) != null) {
                        Ingredient curr = new Ingredient();
                        curr.setName(m1.group(4));

                        if (!list.nameExists(curr.getName())) {
                            list.addIngredient(curr, i);
                            i++;
                        }
                    }

                }

            }

            list.printList();
        }

    }
}