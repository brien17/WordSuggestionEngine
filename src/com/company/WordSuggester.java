package com.company;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** */
class WordSuggester {

  // Field to hold the BiGrams
  private Map<Set<String>, Integer> bgrams;

  // Field to hold each unique word in the document
  private Set<String> uniqueWords;

  // Field to hold each word and a list of their suggestions
  private Map<String, List<String>> suggestions = new HashMap<>();

  /**
   * This method takes in the path to a file and calls the getBigrams method to make the BiGrams,
   * get the number of BiGrams, and populate the uniqueWords Set. It then calls the buildSuggestion
   * method with every unique word in the file to generate suggestions for all of the words and
   * populate the suggestions HashMap.
   *
   * @param path The path to the file
   */
  void buildSuggestionsList(Path path) {
    // Create the BiGrams and get all of the words in the file
    getBigrams(path);

    // Build the suggestions lists for every word
    uniqueWords.forEach(this::buildSuggestion);
  }

  /**
   * This method takes in a path to a file and generates BiGrams, counts the number of BiGrams, and
   * makes a Set of all of the unique words from the file.
   *
   * @param path The path to the file
   */
  private void getBigrams(Path path) {
    // Create the Map for the BiGrams
    bgrams = new HashMap<>();

    try {
      // Load a file into a single list of every word
      Stream<String> lines = Files.lines(path).filter(line -> !line.isBlank());

      // Getting a list of all of the words
      List<String> words =
          lines
              .map(String::toLowerCase) // Converts to lower case
              .map(
                  line ->
                      line.split(
                          "\\s+")) // Splits on white space creating a stream of string arrays
              .flatMap(Arrays::stream) // Flattens the stream to a stream of strings
              .collect(Collectors.toList()); // Collects to a list

      // Getting all of the words without duplicates
      uniqueWords = new HashSet<>(words);

      // Creating the BiGrams
      for (int i = 1; i < words.size(); i++) {
        bgrams.merge(
            new LinkedHashSet<>(Arrays.asList(words.get(i - 1), words.get(i))), 1, Integer::sum);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * This method takes in a word and then creates a suggestion list of words that might come after
   * that word. It then adds the word and the suggestion list to the suggestions HashMap.
   *
   * @param word The word to generate suggestions for
   */
  private void buildSuggestion(String word) {
    // Keeps track of the words and their counts for words that come after the entered word
    Map<String, Integer> nextWords = new HashMap<>();

    // List of suggested next words
    final List<String> suggestionsList = new ArrayList<>(Arrays.asList("the", "this", "of"));

    // Getting all of the BiGrams that have the entered word as their first word and adding the
    // second word and its count to the nextWords Map
    bgrams.entrySet().stream()
        .filter( // Filtering for when the first word is the entered word
            e -> e.getKey().toArray()[0].equals(word))
        .forEach( // Storing in Map
            e -> nextWords.put((String) e.getKey().toArray()[1], e.getValue()));

    // Keep track of total of times any word comes after the entered word
    final int totalNextWords = nextWords.values().stream().mapToInt(i -> i).sum();

    // Determining the likelihood of each word and adding to list if > 0.65
    nextWords.entrySet().stream()
        .filter((e) -> (double) e.getValue() / totalNextWords > 0.65) // Filtering words
        .forEach(e -> suggestionsList.add(0, e.getKey())); // Adding words to front of suggestions

    // Adding the word and its first 3 suggestions to the map
    suggestions.put(word, suggestionsList.subList(0, 3));
  }

  /**
   * This method asks for user input on the console and generates word suggestions based on the user
   * input until the user enters the command to quit.
   */
  void getUserInput() {
    // Making a list to display suggestions
    final List<String> suggestionsList = new ArrayList<>();

    while (true) {
      // Setting the suggestionList to the default suggestions
      suggestionsList.clear();
      suggestionsList.addAll(Arrays.asList("the", "this", "of"));

      // Giving information to the user
      System.out.println("Please enter a word and I will generate suggestions for your next word");
      System.out.println("Enter e to quit");

      // Creating a scanner and getting input
      Scanner in = new Scanner(System.in);
      String input = in.nextLine().toLowerCase();

      // Exiting if input is e
      if (input.equals("e")) {
        break;
      } else {
        // Gets the correct suggestions for the word entered by the user
        suggestions.entrySet().stream() // Getting all of the suggestions
            .filter( // Filtering out any that don't match the user input
                k -> k.getKey().equals(input))
            .forEach( // Clear the suggestions and replaces it with the suggestions for the word
                e -> {
                  suggestionsList.clear();
                  suggestionsList.addAll(e.getValue());
                });

        // Giving suggestions to user
        suggestionsList.forEach(e -> System.out.println("Your next word might be " + e));
      }
    }
  }
}
