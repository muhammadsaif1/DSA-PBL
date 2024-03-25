package PBL2;
public class LinkedStack {

    public static class Node{
         Object data;
         Node next;
         Node(Object data,Node next){
         this.data=data;
         this.next=next;}
     }
 
     private Node top;
     private int size;
     public void push(Object data){
         top = new Node(data,top);
         ++size;
     }
 
     public int size(){return size;}
 
     public boolean isEmpty(){
         return size()==0;
     }
 
     public Object peek(){
         if (isEmpty()){
             throw new IllegalStateException("Stack is empty");
         }
         return top.data;
     }
 
     public Object pop(){
         if (isEmpty()){
             throw new IllegalStateException("Stack is empty");
         }
         Object val = top.data;
         top = top.next;
         --size;
         return val;
     }
     public void display(){
        Node temp = top;
        while (temp != null){
            System.out.print(temp.data+" ");
            temp = temp.next;
        }
        System.out.println();
    }
    }