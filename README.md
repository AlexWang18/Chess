# *CRUD Command Line Chess Game*
---
### By Alex Wang (alw245@pitt.edu)

## Installation Instructions:
---

## Features:
---
Allows for PvP games using standard chess rules and no rules alike. Comes with castling, pawn promotion, and En Passant
Has robust error detection that prevents the match from crashing.

#### Castling:
1 r k b q ! b - r
2 p p p - p p p p
3 - - - - - - - k
4 - - - - - - - -
5 - - p - - - - -
6 - - - - P - - K
7 P P P P - P P P
8 R K B Q ! - - R
  a b c d e f g h
White to move, enter the square you wish to move from.
e8
Enter the square you wish to move to.
h8
White castled!

#### Promotion:
1 r k b q ! b k r
2 p P p - p p p p
3 - - - - - - - -
4 - - - - - - - -
5 - - - - - - - -
6 - - - - - - - -
7 - P p P P P P P
8 R K B Q ! B K R
  a b c d e f g h
White to move, enter the square you wish to move from.
b2
Enter the square you wish to move to.
a1
You can promote your pawn at b2 Enter your choice between 
A. Knight
B. Bishop
C. Rook
D. Queen
rook
White Pawn, b2 -> a1, captures Black Rook
1 R k b q ! b k r
2 p - p - p p p p
3 - - - - - - - -
4 - - - - - - - -
5 - - - - - - - -
6 - - - - - - - -
7 - P p P P P P P
8 R K B Q ! B K R
  a b c d e f g h
  
#### En Passant:
Black to move, enter the square you wish to move from.
b2
Enter the square you wish to move to.
b4
Black Pawn, b2 -> b4
1 r k b q ! b k r
2 p - p - p p p p
3 - - - - - - - -
4 P p - p - - - -
5 - - - - - - - -
6 - - - - - - - -
7 - P P P P P P P
8 R K B Q ! B K R
  a b c d e f g h
White to move, enter the square you wish to move from.
a4
Enter the square you wish to move to.
b3
You just got enpassanted cuhhhhhhh at b3
White Pawn, a4 -> b3, captures Black Pawn
1 r k b q ! b k r
2 p - p - p p p p
3 - P - - - - - -
4 - - - p - - - -
5 - - - - - - - -
6 - - - - - - - -
7 - P P P P P P P
8 R K B Q ! B K R
  a b c d e f g h
  
  
  
## Usage Example
Welcome to CL Chess!
What style of rules do you want to play?
Classic, Silly, or None
Classic
Okay! There are the standard rules in play! Lowercase letters is black, and uppercase is white. 
Let's begin
1 r - b q ! b k r
2 p p p p - p p p
3 - - k - p - - -
4 - - - - - - - -
5 P - - - - - P -
6 - - - - - P - -
7 - P P P P - - P
8 R K B Q ! B K R
  a b c d e f g h
Black to move, enter the square you wish to move from.
d1
Enter the square you wish to move to.
h5
Black Queen, d1 -> h5
1 r - b - ! b k r
2 p p p p - p p p
3 - - k - p - - -
4 - - - - - - - -
5 P - - - - - P q
6 - - - - - P - -
7 - P P P P - - P
8 R K B Q ! B K R
  a b c d e f g h
White to move, enter the square you wish to move from.
b7
Enter the square you wish to move to.
b5
Move did not follow through, try again..
1 r - b - ! b k r
2 p p p p - p p p
3 - - k - p - - -
4 - - - - - - - -
5 P - - - - - P q
6 - - - - - P - -
7 - P P P P - - P
8 R K B Q ! B K R
  a b c d e f g h
White Pawn, a7 -> a5
Black Knight, b1 -> c3
White Pawn, f7 -> f6
Black Pawn, e2 -> e3
White Pawn, g7 -> g5
Black Queen, d1 -> h5
GG!

Game lasted 82 seconds

## What I've learned:
This was my first personal project spurred on from binging Queen's Gambit. I dabbled a bit in Regex, design patterns, and the general principles of OOP in this project. It has definitely ingrained into me some useful lessons. For one having a plan with your idea is key and drawing out a UML can be especially useful. Another is to only work on a problem one at a time and finish that thoroughly before moving on. I've definitely also learned the importance of having readable and formatted code as was a pain to try to parse through some of my spaghetti code. I could have definitely done a lot more differently with the high level implementation and perhaps added a GUI but I am overall pleased with how it turned out. 

## License:
---
https://www.gnu.org/licenses/gpl-3.0.html
