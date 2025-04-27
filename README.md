# secretive-company
This is a playground for practicing relational DB 
modelling with many relations.

Here's the setup:

You work at a very secretive company which is organised into
__Departments__, __Projects__ and __Teams__ (like a hierarchy). Let's call them
___Company Parts___.

Each department project and team can specify which other department,
project or team is ___NOT___ allowed to know about it.

If project __Y__ says that department __X__ is blocked, then all the entities
under department __X__ are also blocked by project __Y__. Similarly for any
other level of the hierarchy.

Similarly, if department __A__ has some blocks, then __A__'s projects and
teams also inherit those blocks: they block
the same __Company Parts__.