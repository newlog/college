Estel Music Player
=================


Estel Music Player is a client-server application that lets you share and play the music from your computer to your mobile phone.

Estel Music Player comes with three applications. 

The first one is the server. It's programmed in Java and it lets you choose a folder in your computer full of songs you want to listen in your mobile. Looking at its source code you can find interesting things such as:

+ Graphical programming using Swing.
+ How to run an application from the task bar.
+ Socket communication to send binary data.
+ Custom protocol communication

On the other hand you have the client applications. They are natively programmed for Android and IOS. Right now they must be pretty outdated so I don't know if they will work in modern devices (you can try and tell me!). In the client source code you can find the following interesting things.

In general:

+ How to play music and how to handle random sequences of songs (with an optimized low song repetition)
+ How to handle binary data from the network.

Platform specific:

+ An interesting (not official) library to handle asynchronous socket communication in IOS.
+ How to play music in IOS without it being played from the music library (a requirement in the days I programmed this - dunno if it still works like this)
+ How to program a threaded socket communication to separate the protocol communication from the binary data in Java (Android - it wasn't needed in IOS because of the asynchronous socket library I found).


`NOTE`: This software is **FULL OF BUGS** so use it at your own risk. The code is published so you understand how client-server communication works between a computer and both Android and IOS. This is completely a toy software.

`NOTE`: Despite the code is educational, this doesn't mean that it is in fact good code. It might be a perfect shit of code (sincerely, I don't remember how I programmed it), but, at least, if it doesn't teach how to program things it will teach you how not to program them.

`TODO`: This readme is not finished. A way to install and run the software should be explained, as well as which devices are supported (given the IOS policy, it might not even work in nowadays Iphone devices...)
