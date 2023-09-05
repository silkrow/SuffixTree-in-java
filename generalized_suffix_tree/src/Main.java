import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the input string: ");
        String inputString = scanner.nextLine();

        System.out.print("Enter the pattern: ");
        String pattern = scanner.nextLine();

        scanner.close();

        SuffixTree suffixTree = new SuffixTree(inputString);

        // Find matches for the pattern
		List<Integer> matches = suffixTree.match(pattern);
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
