package org.example;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTree implements Serializable {
    private Node root;

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public PriorityQueue<Node> buildFrequencyTable(String text){
        if(text == null || text.length() == 0){
            return null;
        }
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new NodeComparator());
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(int i = 0; i < text.length(); i++){
            if(hashMap.containsKey(text.substring(i, i + 1))){
                hashMap.replace(text.substring(i, i + 1), hashMap.get(text.substring(i, i + 1)) + 1);
            }
            else{
                hashMap.put(text.substring(i, i + 1), 1);
            }
        }

        for (Map.Entry<String,Integer> mapElement : hashMap.entrySet()) {
            String character = mapElement.getKey();
            int frequency = (mapElement.getValue());
            Node node = new Node(frequency, character.charAt(0));
            priorityQueue.add(node);
        }
        return priorityQueue;
    }

    public HuffmanTree buildTree(PriorityQueue<Node> freqTable){
        if(freqTable == null){
            return this;
        }
        while(freqTable.size() > 1){
            Node node1 = freqTable.remove();
            System.out.println("Node A removed from PQ: Char " + node1.getVal() + " with frequency of " + node1.getFrequency());
            Node node2 = freqTable.remove();
            System.out.println("Node B removed from PQ: Char " + node2.getVal() + " with frequency of " + node2.getFrequency());
            Node newNode = new Node(node1, node2);
            System.out.println("New mini tree is built: parent node has frequency of " + newNode.getFrequency() + ". Left Child is: Char " + newNode.getLeftChild().getVal() + " : " + newNode.getLeftChild().getFrequency() + " and Right Child is: Char " + newNode.getRightChild().getVal() + " : " + newNode.getRightChild().getFrequency());
            freqTable.add(newNode);
            System.out.println();
            System.out.println("PQ has " + freqTable.size() + " nodes remaining until tree is fully built");
            System.out.println();
        }
        if(freqTable.size() == 1){
            root = freqTable.peek();
        }
        return this; // returns huffman tree object with has a root --> the root gives you access to the tree of binary
    }



    public class Node implements Serializable{
       private Integer frequency;
       private Character val;
       private Node leftChild;
       private Node rightChild;

        public void setFrequency(Integer frequency) {
            this.frequency = frequency;
        }

        public Character getVal() {
            return val;
        }

        public void setVal(Character val) {
            this.val = val;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChild() {
            return rightChild;
        }

        public void setRightChild(Node rightChild) {
            this.rightChild = rightChild;
        }

        public Node(Node leftChild, Node rightChild) { // Serializable will be passed down to nodes by default but if you don't want it to be, you can specify don't take the inheritance down...SOMETIMES?
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            frequency = leftChild.getFrequency() + rightChild.getFrequency();
        }

        public Node(Integer frequency, Character val) {
            this.frequency = frequency;
            this.val = val;
        }

        public Integer getFrequency() {
            return frequency;
        }
    }

    public class NodeComparator implements Comparator<Node> {
        @Override // always use @override with implements -- way of getting past with subclassing where you can only have 1 subclass
        public int compare(Node o1, Node o2) {
            return o1.getFrequency().compareTo(o2.getFrequency());
        }
        // the implemented class methods are always empty
        // if you implement an interface, you are required to use the methods in the interface
        // must use a comparator if you use a priority queue

    }
}

