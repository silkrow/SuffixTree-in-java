import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the input string: ");
        String inputString = scanner.nextLine();
        byte[] inputBytes = inputString.getBytes(); // Convert input string to byte array

        System.out.print("Enter the pattern: ");
        String pattern = scanner.nextLine();
        byte[] patternBytes = pattern.getBytes(); // Convert pattern string to byte array

        scanner.close();

        SuffixTree suffixTree = new SuffixTree(inputBytes); // Use inputBytes instead of inputString

        // Find matches for the pattern
        int[] matches = suffixTree.match(patternBytes); // Use patternBytes instead of pattern
        if (matches.length == 0) {
            System.out.println("No matches found.");
        } else {
            System.out.println("Pattern found at indices:");
            for (int match : matches) {
                System.out.println(match);
            }
        }

        // Print the constructed tree
        // suffixTree.printTree();
    }
}

