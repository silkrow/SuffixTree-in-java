import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the input string: ");
        String inputString = scanner.nextLine();
        byte[] inputBytes = inputString.getBytes();

        System.out.print("Enter the pattern: ");
        String pattern = scanner.nextLine();
        byte[] patternBytes = pattern.getBytes();

        scanner.close();

        SuffixTree suffixTree = new SuffixTree(inputBytes);

        // Find matches for the pattern
        List<Integer> matches = suffixTree.match(patternBytes); 
        if (matches.size() == 0) {
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

