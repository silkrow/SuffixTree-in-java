# SuffixTree-in-java
A naive implementation of suffix tree in Java.

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
    |\----banana$----
    |            /---$---*
    |\----na----|
    |            \---na$---*
    |         /---$---*
     \------a|       /---$---*
              \---na|
                     \---na$---*
