package com.company;

import java.nio.file.Paths;

/**
 * This is the main class of my program.
 *
 * @author Cameron Brien
 */
public class Main {

  public static void main(String[] args) {
      // Creating word suggester
      WordSuggester analyzer = new WordSuggester();
      // Initializing the suggester
      analyzer.buildSuggestionsList(Paths.get("./src/Data/messages.txt"));
      // Getting the user
      analyzer.getUserInput();
  }
}
