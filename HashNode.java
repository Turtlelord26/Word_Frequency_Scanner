//A node for the linked list
public class HashNode {
  
  private WordData data;
  
  private HashNode next = null;
  
  //constructor takes a WordData and produces a node with a null next field
  public HashNode(WordData data) {
    this.data = data;
  }
  
  public WordData getData() {
    return data;
  }
  
  public HashNode getNext() {
    return next;
  }
  
  public void setNext(HashNode next) {
    this.next = next;
  }
}