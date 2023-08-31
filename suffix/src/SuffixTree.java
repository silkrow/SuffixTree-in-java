import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;

class SuffixTree {
    static final int MAXN = 100000;
    static byte[] s; // Change String to byte[]
    static int n;
    static final byte TER = '$'; // Change char to byte

    static class Node {
        int l, r, par, link, p_len;
        HashMap<Byte, Integer> next; // Change HashMap<Character, Integer> to HashMap<Byte, Integer>

        Node() {
            this(0, 0, -1);
        }

        Node(int l, int r, int par) {
            this.l = l;
            this.r = r;
            this.par = par;
            this.link = -1;
            this.next = new HashMap<>();
            this.p_len = 0;
        }

        int len() {
            return r - l;
        }

        int get(byte b) { // Change char to byte
            return next.getOrDefault(b, -1);
        }
    }

    static Node[] node = new Node[MAXN];
    static int sz;

    static class State {
        int v, pos;

        State(int v, int pos) {
            this.v = v;
            this.pos = pos;
        }
    }

    static State ptr;

    SuffixTree(byte[] byteArray) { // Change String to byte[]
        this.s = byteArray;
        this.n = this.s.length;
        node[0] = new Node();
        ptr = new State(0, 0);
        buildTree(this.s, n);
    }

    static State go(State st, int l, int r, byte[] byteArray) { // Change String to byte[]
        while (l < r) {
            if (st.pos == node[st.v].len()) {
                st = new State(node[st.v].get(byteArray[l]), 0); // Change char to byte
                if (st.v == -1)
                    return st;
            } else {
                if (s[node[st.v].l + st.pos] != byteArray[l]) { // Change char to byte
                    return new State(-1, -1);
                }
                if (r - l < node[st.v].len() - st.pos) {
                    return new State(st.v, st.pos + r - l);
                }
                l += node[st.v].len() - st.pos;
                st.pos = node[st.v].len();
            }
        }
        return st;
    }

    static int split(State st) {
        if (st.pos == node[st.v].len()) {
            return st.v;
        }
        if (st.pos == 0) {
            return node[st.v].par;
        }
        Node v = node[st.v];
        int id = sz++;
        node[id] = new Node(v.l, v.l + st.pos, v.par);
        node[v.par].next.put(s[v.l], id);
        node[id].next.put(s[v.l + st.pos], st.v);
        node[st.v].par = id;
        node[st.v].l += st.pos;

        node[id].p_len = node[node[id].par].p_len + node[id].len();
        node[st.v].p_len = node[node[st.v].par].p_len + node[st.v].len();
        return id;
    }

    static int getLink(int v) {
        if (node[v].link != -1) {
            return node[v].link;
        }
        if (node[v].par == -1) {
            return 0;
        }
        int to = getLink(node[v].par);
        return node[v].link = split(go(new State(to, node[to].len()), node[v].l + (node[v].par == 0 ? 1 : 0), node[v].r, s));
    }

    static void treeExtend(int pos, byte[] byteArray) { // Change String to byte[]
        for (;;) {
            State nptr = go(ptr, pos, pos + 1, byteArray); // Change String to byte[]
            if (nptr.v != -1) {
                ptr = nptr;
                return;
            }

            int mid = split(ptr);
            int leaf = sz++;
            node[leaf] = new Node(pos, n, mid);
            node[mid].next.put(s[pos], leaf);
            node[leaf].p_len = node[mid].p_len + node[leaf].len();

            ptr.v = getLink(mid);
            ptr.pos = node[ptr.v].len();
            if (mid == 0) {
                break;
            }
        }
    }

    static void buildTree(byte[] byteArray, int n) { // Change String to byte[]
        sz = 1;
        for (int i = 0; i < n; ++i) {
            treeExtend(i, byteArray);
        }
    }

    public int[] match(byte[] patternArray) { // Change String to byte[]
        int sptr = 0;
        int i = 0;
        List<Integer> matches = new ArrayList<>();

        while (i < patternArray.length) { // Change String to byte[]
            if (!node[sptr].next.containsKey(patternArray[i])) { // Change char to byte
                return new int[0];
            }

            sptr = node[sptr].next.get(patternArray[i]); // Change char to byte

            int len = Math.min(node[sptr].len(), patternArray.length - i); // Change Math.min to Math.min

            for (int j = 0; j < len; j++) {
                if (s[node[sptr].l + j] != patternArray[i + j]) {
                    return new int[0];
                }
            }
            i += len;
        }

        Queue<Integer> bfs = new LinkedList<>();
        bfs.offer(sptr);

        while (!bfs.isEmpty()) {
            sptr = bfs.poll();
            if (node[sptr].next.size() == 0) {
                matches.add(s.length - node[sptr].p_len);
            } else {
                for (Map.Entry<Byte, Integer> entry : node[sptr].next.entrySet()) {
                    int child = entry.getValue();
                    bfs.offer(child);
                }
            }
        }

        int[] result = new int[matches.size()];
        for (int k = 0; k < matches.size(); k++) {
            result[k] = matches.get(k);
        }
        return result;
    }

    static void printTree() {
        for (int i = 0; i < sz; i++) {
            System.out.println("Node " + i + " " + node[i].l + " " + node[i].r + " " + node[i].par + " " + node[i].link + " " + node[i].p_len);
        }
    }
}
