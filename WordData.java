//A class to allow easy storage of the frequency of a given word
public class WordData {
  
  private String word;
  
  private int freq;
  
  //constructor takes a String for the word field and initializes the frequency field to 1
  public WordData(String word) {
    this.word = word;
    freq = 1;
  }
  
  public String getWord() {
    return word;
  }
  
  public int getFrequency() {
    return freq;
  }
  
  //frequency will never need to change in any way other than in increments of +1
  public void increment() {
    freq++;
  }
}