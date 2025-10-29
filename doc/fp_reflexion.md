# FP Reflexion

While exploring Functional Programming (FP) for the first time, I encountered many obstacles and differences compared to
Procedural or Object-Oriented Programming (OOP). The first problem I encountered, was how to organize Code without using
classes. I solved this by reading some documentation. There are components called: "Singleton Objects". I used them
similarly to modules in other languages, so I could split my code into multiple files, which made the code nicely
organized.

In order to create Tokens in my Lexer I needed some kind of data structure to be able to represent these
Tokens. Naturally I couldn't use classes, so I did some research. I found out that In
Scala, there is a structure called `final case class` Which is a immutable data structure. I was able to use that for my
Tokens. In other cases I used Enums which also proved quite useful for defining types.

The next problem I encountered was making decisions in code. If you have some code that needs to behave differently
based on a condition you just use an if-statement. In FP the alternative is pattern matching, since you want to handle
every possible case. At first, it's quite weird to force yourself, not to think in control structures. But I adapted and
used pattern matching using Scala's `match` to achieve it.

Another problem I faced was looping. In OOP you have structures like `for` and `while`. In
FP you don't. Instead, you use functions like `map`, `filter` and `foldLeft`. This was not enough though as I had places
where these functions wouldn't suffice. So another way to replace these standard loops is recursion. Recursion is a
greate pattern to use when writing an interpreter as I found out. That is because the Recursive Descent Parser is full
of recursion (I guess it's literally in the name). But loops were far from the biggest problem I had with FP.

The biggest issue I encountered overall was immutability. When I was writing the Lexer, I noticed that I needed the
index of the current iteration of the loop. I couldn't use a traditional mutable counter because that's not immutable.
In that situation I had luck because Scala provides a `zipWithIndex` function that does the job for you and provides the
index, but I wasn't able to solve all problems as easy as that. The biggest realisation when working with immutability
is that you have to work more with parameters. My Parser for
example is all about parsing a list of Tokens. So how do you take or even add elements from an immutable list? Well it's
parameters. As the function you expect a list of Tokens. You change it make a copy and put it as an argument into
another function. Keep doing that, and suddenly we achieved changing a list that is immutable. It is very simple on
paper but mind-blowing when you're coming from OOP.