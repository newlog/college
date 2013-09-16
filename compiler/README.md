Babel Compiler
===============

This is one of the (many) hardest projects in my university. Objective? Build a complete hand made compiler!

From the bottom-up, here you have a complete compiler that from an invented programming language (Babel) will generate a MIPS32 assembler code.

The programming language is somewhat reduced in a way that, for example, does not support dynamic memory management (and I don't remember if it supports floating point arithmetic), nevertheless you can program any kind of usable software using conditions, loops, recursion, complex mathematical expressions or built-in functions such as reads and writes. 

This compiler was developed through several phases (common phases when compiling code) such as the lexico-syntactic, the semantic and the code generation phase. 

The code generation phase ends up creating a source code file in assembler, so you don't have a binary file (we didn't deal with the linking process or the executable file format). For this reason we used the MIPS32 simulator called [SPIM](http://spimsimulator.sourceforge.net/) (or in my days, PCSPIM). You can download the different versions from [here](http://sourceforge.net/projects/spimsimulator/files/), but, just in case it disappears from the web, within the compiler code I've uploaded a working version of the Spim simulator. 

I should say that all the token ("word") parsing/consuming work is done through [JavaCC](https://javacc.java.net/). You can find basic information in its Wikipedia [entry](http://es.wikipedia.org/wiki/JavaCC) and a (random) tutorial [here](http://www.engr.mun.ca/~theo/JavaCC-Tutorial/javacc-tutorial.pdf).

Finally, if you want to learn about compilers, I recommend you the [Dragon Book](http://en.wikipedia.org/wiki/Compilers:_Principles,_Techniques,_and_Tools) also known as [Compilers: Principles, Techniques, & Tools](http://www.amazon.com/Compilers-Principles-Techniques-Tools-Edition/dp/0321486811) (holy shit! 90 bucks!!)


`NOTE`: I have to say that the cody is really really UGLY (things like several files of 1000 LOC)! Despite this I hope you are can understand how a compiler can be developed. It is not black magic :)

`TODO`: This readme is not finished and an explanation of how to run the compiler and how to execute the generated code with Spim is needed. It also lacks a report on how the compiler works (at a source code level) and which is the programming language syntax.
