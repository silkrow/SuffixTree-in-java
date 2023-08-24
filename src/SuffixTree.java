/* Suffix Tree implementation based on Ukkonen's Algorithm
 */
//package edu.illinois.iti.orc;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/* UkkSuffixTree is constructed from a string s.
 * An array of nodes is stored in the UkkSuffixTree class, node[0] is the root of the suffix tree.
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
        int v, pos;

        State(int v, int pos) {
            this.v = v;
            this.pos = pos;
        }
    }
    
    static State ptr;

    UkkSuffixTree (String s) {
        this.s = s;
        this.n = s.length();
        t[0] = new Node(); // Initialize the root node
        // for (int i = 1; i < MAXN; ++i)
        //     t[i] = new Node(); // Initialize other nodes
        ptr = new State(0, 0); // Initialize ptr within the constructor
        buildTree(s, n);
    }


    static State go(State st, int l, int r, String s) {
        while (l < r) {
            if (st.pos == t[st.v].len()) {
                st = new State(t[st.v].get(s.charAt(l)), 0);
                if (st.v == -1)
                    return st;
            } else {
                if (s.charAt(t[st.v].l + st.pos) != s.charAt(l))
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
        if (t[v].par == -1)
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
            t[leaf] = new Node(pos, n, mid);
            t[mid].next.put(s.charAt(pos), leaf);

            ptr.v = getLink(mid);
            ptr.pos = t[ptr.v].len();
            if (mid == 0)
                break;
        }
    }

    static void buildTree(String s, int n) {
        sz = 1;
        for (int i = 0; i < n; ++i)
            treeExtend(i, s);
    }

    public int[] match(String pattern) {
        List<Integer> matches = new ArrayList<>();

        State currentState = new State(0, 0);

        for (int i = 0; i < pattern.length(); i++) {
            currentState = go(currentState, currentState.pos, currentState.pos + 1, pattern);
            if (currentState.v == -1) {
                return new int[0]; // No matches found, return an empty array
            }
            if (currentState.pos == t[currentState.v].len()) {
                matches.add(i - t[currentState.v].len() + 1);
            }
        }

        int[] result = new int[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            result[i] = matches.get(i);
        }
        return result;
    }

    static void printTree() {
        for (int i = 0; i < sz; i++) {
            System.out.println(t[i].l + " " + t[i].r + " " + t[i].par + " " + t[i].link);
        }
    }
}