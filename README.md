# extlang
Automatically exported from code.google.com/p/extlang


# Introduction

This project was an attempt to introduce some syntax extensions features into Intellij IDEA using a toy language's syntax. The todo list was to implement: Interpreting parser. It consumes a .grammar description (as BNF) together with tree transformations descriptions and terminal token representations and parses text files using APIs, provided by Intellij IDEA Find usages Simple code formatter

These goals were accomplished (some partially). The project's language is Kotlin.

## Parser

### Descriptions syntax

The descriptions support only plain BNF form. Use PsiViewer to see if they were parsed correctly. All terminals should be written with CAPITAL LETTERS, while non-terminals should be written lowercase (you can also use an apostrophe ('). the grammar is taken 'as-is' so no left recursion is eliminated etc. An example follows:

```
--nonterminals: e e' s s' t t' f s'' ext id elsepart 
--starter: s' 
--terminals: 
SEMI ";" 
LPAR "(" 
RPAR ")" 
WRITE "write" 
IF "if" 
FI "fi" 
LBRACE "{"
RBRACE "}" 
PLUS "+"
MUL "*"
MINUS "-"
DIV "/" 
WHILE "while" 
DO "do" 
ELSE "else" 
WRITE "write" 
READ "read" 
ASSIGN ":="
REPEAT "repeat" 
UNTIL "until" 
SKIP "skip" 
NOT "!" 
COMPARE "==" 
DONE "done" 
--rules: 
s' ::= s s'' ;
s'' ::= SEMI s' ;
s'' ::= EPSILON ; 
s' ::= LBRACE s' RBRACE ; 
s ::= SKIP ;
s ::= id ASSIGN e ;
s ::= EPSILON ; 
s ::= WHILE e DO s' DONE ;
s ::= IF LPAR e RPAR s elsepart FI; 
s ::= WRITE e ; 
s ::= READ id; 
elsepart ::= ELSE s ; 
elsepart ::= EPSILON ; 
e ::= LPAR e RPAR ; 
e ::= NOT e ; 
e ::= t e' ; 
e' ::= PLUS t e' ; 
e' ::= MINUS t e' ; 
e' ::= COMPARE t e' ; 
e' ::= EPSILON; t ::= f t';
t' ::= MUL f t' ; 
t' ::= DIV f t' ; 
t' ::= EPSILON ; 
f ::= NUM; f ::= id;
id ::= IDENT;
s ::= ext ;
ext ::= REPEAT { s' as $action } UNTIL { e as $condition } -> { < s' ; < $action > < s'' ; < SEMI >< s' ;< s ;< WHILE >< e ;< NOT >< $condition >>< DO >< $action > < DONE >> < s'' >>>> } ;
```

There are following terminal symbols you do not need to enumerate yourself: NUM (any number, can be negative, but no space between number and sign is allowed) IDENT (identifier) EPSILON (empty string)

NB you will always need to alias IDENT as some kind of nonterminal, and whenever you want to write IDENT in your productions, you will write this nonterminal instead. In the example above, we aliased IDENT as id and always used id in production rules. The cause is that an AST node implementation is ELASTNode, and it is only constructed for nodes, not leaves. ELASTNode implements also PsiNamedElement, which is a prerequisite for "Find Usages" and allows to jump to the first entry of identifier with this name

Any non-terminal can get an alias name like this:

s ::= { s' as $aliasname }

Be always aware whether the rules are parsed correctly or not!

When you defined grammar, right click on the contents of a .grammar file and press 'load this as a grammar file', you will be fine. From now on this description will be used to parse the contents of .elang files. You need to make a slight change (like adding a space or whatever) so that the file would be reparsed. The syntax highlighting will be lazily updated as well.

Do not try to format the code when the file is not yet reparsed, as it will bork it.

The idea of syntax extensions is well illustrated here

What's so hard in implementing syntax extensions in IDE?

Well,the problem is that syntax extensions mean the AST produced for the compiler is different from the one produced during parsing. If we somehow want to produce it anyway we need to come up with some hack. We propose to make 'phantom trees' where all nodes are build using Markers (check IntellijIDEA custom language plugin documentation if you don't know what is it). The leaves in a normal trees are built automatically (when you call PsiBuilder's advance() method, the leaf is created with the current token). But you can also create a Marker instance via mark() and then immidiately call its 'done' method with an IElementType's instance you want to inject. That is how we build the extensions nodes: parse them as usual create an additional phantom tree as the current node's last node. Its contents will be built according to the quotation tree provided in the rule.

If we want to delete some tokens that are really in file, the only option we found was to remap them to whitespace-like tokens so the builder will ignore them (and so will the compiler, provided with syntax PSI tree).

## Changes in IDEA

It looks like the default PsiBuilderImpl caches lexemes from lexer and can not lookup further than a part of the file. So we need to change the 'cachelexemes' method in order to fix it.

The other problem can be observed at UpdateHighlighterUtil.java:422 . If we try to highlight zero characters at the last position in file, we will get out of bounds because of this increment. Exactly a situation when the last statement in file is an extension containing an identifier with a reference.

## Find usages

First, we wanted to show not only usages in real code but also in a preprocessed, where syntax extensions are applied. However this turned to not be possible atm. The bad thing happends when IDEA builds the word cache after using WordsScanner. When we invoke "Find Usages" on variable "x" it is firstly dereferenced (if it is not a PsiNamedElement itself), then with its definition as AST node (to which the reference should have pointed) we scan the words looking for a word matching the PsiNamedElement's "getName()" method invokation result.

### What's wrong with it

First of all, we won't get into phantom trees when using any standard-like lexer. We need a special one crawling psi tree (such one is provided as a PoC in PsiLexer.kt)

Even then, the lexemes' length will be 0 inside the phantom tree. When working with WordOccurences, produced by word scanner, we will add words into IDEA's index, taking their hash codes. Hash code of the zero-length word will be zero and such word will not be added into the index. Fail. Check: IdDataConsumer:58

Other than that, find usages works flawlessly.

NB in this project every IDENT must be always the node of one special nonterminal node, used exclusively as a sole parent of an identifier and providing getName() for it (as well as being a target or a producer of references).

## Code formatter

Is trivial (just care that some nodes are phantom ones). We just collect the real leaves and attach it to the root block, so it does not make a problem.

## Other unconveniences

# Contacts

Should you have any questions, you can email me or write a message here.
