// Suffix Tree implementation based on Ukkonen's Algorithm

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/* SuffixTree is constructed from a string s.
 * An array of nodes is stored in the SuffixTree class, node[0] is the root of the suffix tree.
 * build_tree() is the function to construct the suffix tree. 
 */
class SuffixTree {
    static final int MAXN = 100000; // Maximum number of nodes
    static String s;
    static int n;

    /* Each node stores the information about the edge between it and its parent.
     * (l, r) - left and right boundaries of the substring [l, r - 1]
     * par - the index of the parent node in the node array
     * link - the suffix link (pointing to the index of a node)
     * next - the list of edges going out from this node
    */
    static class Node {
        int l, r, par, link;
        HashMap<Character, Integer> next;

        Node() {
            this(0, 0, -1);
        }

        Node(int l, int r, int par) {
            this.l = l;
            this.r = r;
            this.par = par;
            this.link = -1;
            this.next = new HashMap<>();
        }

        int len() {
            return r - l;
        }

        int get(char c) {
            return next.getOrDefault(c, -1);
        }
    }

    static Node[] t = new Node[MAXN];
    static int sz;

    static class State {
        int v, pos; // v - index of the current node being processed during tree traversal
                    // pos - the position within that node's substring where the traversal is
        State(int v, int pos) {
            this.v = v;
            this.pos = pos;
        }
    }
    
    static State ptr;

    SuffixTree (String s) {
        this.s = s;
        this.n = s.length();
        t[0] = new Node(); // Initialize the root node
        ptr = new State(0, 0); // Initialize ptr within the constructor
        buildTree(s, n);
    }


    static State go(State st, int l, int r, String str) {
        while (l < r) {
            if (st.pos == t[st.v].len()) {
                st = new State(t[st.v].get(str.charAt(l)), 0);
                if (st.v == -1)
                    return st;
            } else {
                if (s.charAt(t[st.v].l + st.pos) != str.charAt(l))
                    return new State(-1, -1);
                if (r - l < t[st.v].len() - st.pos)
                    return new State(st.v, st.pos + r - l);
                l += t[st.v].len() - st.pos;
                st.pos = t[st.v].len();
            }
        }
        return st;
    }

    static int split(State st) {
        if (st.pos == t[st.v].len())
            return st.v;
        if (st.pos == 0)
            return t[st.v].par;
        Node v = t[st.v];
        int id = sz++;
        t[id] = new Node(v.l, v.l + st.pos, v.par);
        t[v.par].next.put(s.charAt(v.l), id);
        t[id].next.put(s.charAt(v.l + st.pos), st.v);
        t[st.v].par = id;
        t[st.v].l += st.pos;
        return id;
    }

    static int getLink(int v) {
        if (t[v].link != -1)
            return t[v].link;
        if (t[v].par == -1) // special case for root node
            return 0;
        int to = getLink(t[v].par);
        return t[v].link = split(go(new State(to, t[to].len()), t[v].l + (t[v].par == 0? 1 : 0), t[v].r, s));
    }

    static void treeExtend(int pos, String s) {
        for (;;) {
            State nptr = go(ptr, pos, pos + 1, s);
            if (nptr.v != -1) {
                ptr = nptr;
                return;
            }

            int mid = split(ptr);
            int leaf = sz++;
            t[leaf] = new Node(pos, n, mid); // new node, r = n
            t[mid].next.put(s.charAt(pos), leaf); // add the child to parent's next 

            ptr.v = getLink(mid);
            ptr.pos = t[ptr.v].len();
            if (mid == 0)
                break;
        }
    }

    static void buildTree(String str, int n) {
        sz = 1;
        for (int i = 0; i < n; ++i)
            treeExtend(i, str);
    }

    public int[] match(String pattern) {
        State sptr = new State(0, 0);
        int i = 0;

        while (i < pattern.length()) {
            if (!t[sptr.v].next.containsKey(pattern.charAt(i))) {
                return new int[0];
            }

            sptr.v = t[sptr.v].next.get(pattern.charAt(i)); // Goto next node

            int len = t[sptr.v].len()>(pattern.length() - i)?(pattern.length() - i):t[sptr.v].len();

            for (int j = 0; j < len; j++) {
                sptr.pos = j;
                if (s.charAt(j + t[sptr.v].l) != pattern.charAt(i)){
                    return new int[0];
                }
                i++;
            }
        }

        int count = pattern.length() - sptr.pos + t[sptr.v].len() - 1; // Num of characters on the path

        // BFS starting from node t[sptr.v] to find all the leaves.

        int[] result = new int[2];
        result[0] = sptr.v;
        result[1] = sptr.pos;
        return result;
    }

    static void printTree() {
        for (int i = 0; i < sz; i++) {
            System.out.println("Node" + i + " " + t[i].l + " " + t[i].r + " " + t[i].par + " " + t[i].link);
        }
    }

}
