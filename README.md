# SuffixTree-in-java
A naive implementation of suffix tree in Java with Ukkonen's Algorithm.

## How to Use
Compile with 

	make

Run with 

	make run

Clean with 

	make clean

## What Does it Do?
A suffix tree stores all the suffix of a string ```S``` on its nodes. Here is an example of the suffix tree for ```S = banana$```

    * ---$--- * 
    |\----banana$----*
    |            /---$---*
    |\----na----*
    |            \---na$---*
    |             /---$---*
     \------a----*          /---$---*
                  \---na---*
                            \---na$---*

    (* represents a node) 

Note that ```$``` is the termination character, it is necessary so that the final suffix tree will be an "explicit" one (with only leaf nodes corresponding to each suffix, no internal nodes involved). 

With a suffix tree, one can easily determine whether a string ```P``` is a substring of string ```S``` by traversing the suffix tree from the root and follow a path of string ```P```, when the path ends, do a BFS and collect all the leaf nodes that can be reached from this path, they are corresponding to the matches.

Building a suffix tree can be done in O(n), substring searching is also in O(n).
