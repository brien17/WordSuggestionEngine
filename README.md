## Word Suggestion Engine
This is a project that I did for my Intro to Data Engineering class. The goal of this project was to demonstrate concepts learned in the class by building a simple word recommendation engine. The process of my program is described below.<br>

### Process
* Determine the confidence and support of each word pair (bigram)
* Ask user for word (as if they were typing it)
* Build a List of possible "next words" at least 3 words in length
* If the support of a word pair is >65% suggest that as one of the possible next word to be typed
    * Similar to how your phone gives you a list of possible next words when typing
* If no words with support >65% suggest the three most common English connector words: the, this, of
* If less than 3 words found - pad the list with words from the most common connectors: the, this, of
* Print out the list with each word on its own line

## Getting started
The easiest way to use this program is to run it at my repl available here: https://repl.it/@cbrien17/WordSuggestionEngine <br>
<br>
Alternatively, you can run it using the Java Runtime Environment (JRE) by following these steps: <br>
1. First you will need to have the JRE installed.<br> The JRE is available here: https://java.com<br>
2. Click the "Clone or download" button on this repository.
3. Click the "Download ZIP" button.
4. Extract the downloaded folder.
5. In the extracted folder, run the file named "WordSuggestionEngine.jar".

The program should open up in a new window.
