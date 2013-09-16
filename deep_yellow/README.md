Deep Yellow
============

Deep Yellow is an artificial ([badass](http://www.urbandictionary.com/define.php?term=badass)) chess player. 

In the `extra` folder you will also find a complete chess game framework so you can connect two artificial intelligences to beat their asses automatically or you can play against your artificial friend. I don't have the source code of this framework (only the dot class files) because it was given to me, but at least you will be able to enjoy it (and if you are curious enough you can always reverse engineer it ;)

One of the most amazing things of this project is that you can read all the design choices (**in english!**) we made in order to develop an efficient chess player. Why we choose that programming language, the piece and board representation, the piece movement, the search algorithm or the heuristics.

In this code you will find many interesting things such as:

+ How to design software with performance in mind.
+ Bitwise operations to represent the board and piece movement.
+ A chess improved version of the [`negamax`](http://en.wikipedia.org/wiki/Negamax) algorithm.
+ The different heuristics we used to try to make this machine a little intelligent.


Some performance information
----------------------------

This information can be found in the documentation file from the repository, but given that it is interesting I put it here so you don't have to download all the PDF file.

This performance measures were done even before the prune algorithm and the several heuristics were implemented so a better performance is expected.

      Device     |  Search Depth  |  Time  
 ---             |:--------------:|:-----:
    Macbook Pro  |       5        |  10''
    Macbook Pro  |       8        |    1'  
    Macbook Pro  |       9        |   13'  
  AlienWare M11x |       5        |    1'  
  AlienWare M11x |       8        |   10'  
  AlienWare M11x |       9        | 4'30'' 

+ Macbook Pro: Intel Core 2 Duo 2.4 GHz, RAM 4GB DDR3, x64
+ AlienWare M11x: Intel Core i7 CPU 1.20GHz, RAM 4GB DDR3, x64


Name clarification
------------------

Deep Yellow's name comes from his big brother [Deep Blue](http://en.wikipedia.org/wiki/Deep_Blue_(chess_computer)). Despite being a happy family, Deep Yellow is not a really gifted mind as his brother. For this reason, the [opposite](http://en.wikipedia.org/wiki/Complementary_colors) color in the RGB model was used to name it :)

TODO
----

+ If someone is interested I'll write how to configure all the platform so you can enjoy a good match.

+ By the way, it is worth noting that our friend Yellow Deep is a little dumb in many situations. Be kind with him, not everyone is as smart as you. Think about the (not-so-far) moment in which machines will outsmart you... 
