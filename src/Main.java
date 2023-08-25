public class Main {
    public static void main(String[] args) {
        String inputString = "your_input_string_here";
        SuffixTree suffixTree = new SuffixTree(inputString);

        // Example usage: Find matches for a pattern
        String pattern = "you";
        int[] matches = suffixTree.match(pattern);
        for (int match : matches) {
            System.out.println("Pattern found at index: " + match);
        }

        // Example usage: Print the constructed tree
        suffixTree.printTree();
    }
}

