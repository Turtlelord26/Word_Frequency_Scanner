//A linked list for the hash table
public class HashLinkedList {
  
  private HashNode head;
  
  //A handy field for methods that search the list
  private int length;
  
  //constructor takes a node but can handle a null input (the difference being the length field value)
  public HashLinkedList(HashNode node) {
    this.head = node;
    if (node == null)
      length = 0;
    else
      length = 1;
  }
  
  public HashNode getHead() {
    return head;
  }
  
  public void setHead(HashNode node) {
    this.head = node;
  }
  
  public void addToFront(HashNode node) {
    node.setNext(getHead());
    setHead(node);
    length++;
  }
  
  public int getLength() {
    return length;
  }
  
  //Shortens code in the hashEntry method of WordFrequencyScanner. 
  //Returns the position of the input word or zero if the word is not in the list
  public int contains(String word) {
    int location = 1;
    while (listRun(location) != null && 
           ! listRun(location).getData().getWord().equals(word) && 
           location <= length) {
      location++;
    }
    if (location > length)
      return 0;
    else
      return location;
  }
  
  //Standard search protocol for linked lists, will return null for overly large input
  public HashNode listRun(int place) {
    HashNode node = getHead();
    for (int i = 1; i < place && i <= getLength(); i++) {
      node = node.getNext();
    }
    return node;
  }
}