import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

//A class that computes the number of occurences of each word in an input file and prints this data to an output file
public class WordFrequencyScanner {
  
  private static HashLinkedList[] hashTable;
  
  //Field used to determine when to rehash into a larger table
  private static int hashedNodes;
  
  //main method for ease in running, all it really does is call calculateWordFrequencies
  public static void main(String[] args) {
    if (args.length == 2) {
      calculateWordFrequencies(args[0], args[1]);
    }
    else {
      System.out.print("Argument Error: Program takes two arguments, input file name and output file name.");
    }
  }
  
  //Central method of this class. Initializes the fields and calls many helper methods.
  public static void calculateWordFrequencies(String inputFile, String outputFile) {
    hashTable = new HashLinkedList[32];
    hashedNodes = 0;
    //First method call generates a string representing the contents of the input file
    String inputContent = readFileAsString(inputFile).toLowerCase();
    //Second method call generates an array of individual words from the file contents
    String[] inputWords = inputContent.split("[^0-9a-zA-Z]");
    //Third method call hashes the entries in the String array
    hashWords(inputWords);
    //Fourth method call prints the hashed data to an output file
    writeDataToFile(outputFile);
  }
  
  //First method call generates a string representing the contents of the input file
  //This method largely copied from author's work for P2
  public static String readFileAsString(String filename) {
    
    try {
      FileReader fr = new FileReader(filename);
      StringBuilder sb = new StringBuilder();
      //negative one is already used, for when the reader runs out of file to read, 
      //but it needs to be initialized to something and zero is a valid cahracter.
      int character = -2;
      while (character != -1) {
      
        character = fr.read();
        //if statement blocks the -1 that happens at the end of the file
        if (character >= 0)
          sb.append((char) character);
      }
      return sb.toString();
    }
    //can trigger on the FileReader constructor
    catch (FileNotFoundException e) {
      System.out.print("File not found or unreadable.");
      return "";
    }
    //can trigger on the read() method
    catch (IOException e) {
      System.out.println("I/O Error during file read");
      return "";
    }
  }
  
  //Third method call hashes the entries in the String array
  private static void hashWords(String[] inputWords) {
    int length = inputWords.length;
    //loop goes through the array of Strings, and calls hashEntry on each one that is not an empty String
    for (int i = 0; i < length; i++) {
      if (! inputWords[i].equals(""))
        hashEntry(hashTable, inputWords[i]);
    }
  }
  
  //Helper method to a helper method, but prevents some rather ugly nested loop code
  //method hashes an input word into an input table.
  private static void hashEntry(HashLinkedList[] table, String word) {
    int hash = (word.hashCode() % table.length + table.length) % table.length;
    //if nothing is in the spot of hashTable that the word hashes to, create a HashLinkedList for that spot
    if (table[hash] == null) {
      table[hash] = new HashLinkedList(new HashNode(new WordData(word)));
    }
    //if there is a list already in place and it contains word, increment the frequency thereof
    else if (table[hash].contains(word) != 0) {
      table[hash].listRun(table[hash].contains(word)).getData().increment();
    }
    // if there is a list in place that does not contain word, add a HashNode to the list
    else {
      table[hash].addToFront(new HashNode(new WordData(word)));
    }
    hashedNodes++;
    //rehash conditional. This was originally 2 * table.length, 
    // but the result was a table with length > 16000. That seemed unnecessarly large
    if (hashedNodes >= 5 * table.length)
        rehash(table);
  }
  
  //Helper method to a helper method to a helper method, but it prevents some truly ugly recursive code
  //method rehashes the entries of the input table into a table twice as large
  private static void rehash(HashLinkedList[] hashTable) {
    //necessary to reset this field, since the hashEntry calls below will add to it as normal
    hashedNodes = 0;
    HashLinkedList[] expo = new HashLinkedList[2 * hashTable.length];
    //First level of loop runs through the linked lists in the hash table
    for (int i = 0; i < hashTable.length; i++) {
      if (hashTable[i] != null) {
        //so long as there is a list, the second level of loop runs through the entries of that list
        for (int j = 1; j <= hashTable[i].getLength() && 
             ! hashTable[i].listRun(j).getData().getWord().equals(""); j++) {
          WordData data = hashTable[i].listRun(j).getData();
          int freq = data.getFrequency();
          //third level of loop rehashes the word determined by the first two levels of loop a number of times
          //equal to its frequency
          for (int k = 1; k <= freq; k++) {
            //so long as rehash is calling hashEntry, it is impossible for hashEntry to call rehash,
            //as hashedNodes will only increase to its original total, while table size has doubled.
            hashEntry(expo, data.getWord());
          }
        }
      }
    }
    WordFrequencyScanner.hashTable = expo;
  }
  
  //Fourth method call prints the hashed data to an output file
  private static void writeDataToFile(String outputFile) {
    try {
      int totalNodes = 0;
      int totalWords = 0;
      FileWriter fw = new FileWriter(new File(outputFile));
      //first level of loop runs through the hashTable
      for (int i = 0; i < hashTable.length; i++) {
        if (hashTable[i] != null) {
          //seocnd level of loop runs through a list in the hashTable, extracts words and frequencies, and writes
          for (int j = 1; j <= hashTable[i].getLength(); j++) {
            WordData data = hashTable[i].listRun(j).getData();
            int freq = data.getFrequency();
            String word = data.getWord();
            fw.write("(" + word + " - " + ((Integer) freq).toString() + ")\n");
            totalNodes++;
            totalWords += freq;
          }
        }
      }
      fw.close();
      //Console printouts of average length of collision lists and total word counts
      System.out.println("Average length of collision lists: " + 
                         ((Double) ((totalNodes + 0.0) / hashTable.length)).toString());
      System.out.println("Word count: " + ((Integer) totalWords).toString());
    }
    //may occur on calls of write() in FileWriter
    catch (IOException e) {
      System.out.println("I/O Error during file write");
    }
  }
}