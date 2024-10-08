package org.example;

import java.io.*;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanAlgo {

    private static HashMap<Character, String> hashMap = new HashMap<>(); // key = character, value = binary pathway

    public static String encode(String text) {
        if(text.length() == 0 || text == null){
            return "Invalid string provided.";
        }

        HuffmanTree tree = new HuffmanTree();
        tree.buildTree(tree.buildFrequencyTable(text));

        buildPath(tree.getRoot(), "");

        String compressedString = "";
        for(int i = 0; i < text.length(); i++){ // find the binary pathway for each character in the string using the hash map
            compressedString += hashMap.get(text.charAt(i)); // adding the pathway string for each character which is stored in the hashmap (the character is the key for the tree pathway which is the value)
        }

        System.out.println("The space saved is " + calculateSpaceSaved(text.length(), compressedString.length()));

        return compressedString;
    }


    // string represents the binary pathway
    public static void buildPath(HuffmanTree.Node current, String string){
        if(current == null || string == null){
            return;
        }
        if(current.getLeftChild() != null){
            buildPath(current.getLeftChild(), string + "0");
        }
        hashMap.put(current.getVal(), string);
        if(current.getRightChild() != null){
            buildPath(current.getRightChild(), string + "1");
        }
        hashMap.put(current.getVal(), string);
    }

    public static String calculateSpaceSaved(int uncompressedText, int compressedTextPathway){
        double uncompressedSpace = uncompressedText * 8;
        double compressedSpace = compressedTextPathway;
        double percentageSaved =  1.0 - compressedSpace/uncompressedSpace;
        return percentageSaved * 100 + "%";
    }



    public static String decode(HuffmanTree tree, String text) {
        if(tree == null || text == null){
            return "Invalid tree or text provided.";
        }
        for(int i = 0; i < text.length(); i++){
            if(!text.substring(i, i+1).equals("1") && !text.substring(i, i+1).equals("0")){
                return "Invalid text provided. There are not only binary values present.";
            }
        }
        HuffmanTree.Node current = null;
        if(tree.getRoot() != null){
            current = tree.getRoot();
        }
        else{
            return "Tree with null root provided.";
        }
        String result = "";
        for(int i = 0; i < text.length(); i++){
            if(text.substring(i, i+1).equals("1")){
                if(current.getRightChild() != null){
                    current = current.getRightChild();
                }
                else{
                    return "Invalid tree provided"; // confirm with mr. u that the tree provided will always be compatible with the word provided
                }
            }
            else if(text.substring(i, i+1).equals("0")){
                if(current.getLeftChild() != null){
                    current = current.getLeftChild();
                }
                else{
                    return "Invalid tree provided";
                }
            }
            if(current.getVal() != null){ // not null means there is a character there
                result += current.getVal();
                current = tree.getRoot(); // reset current back to the root to go through the tree again
            }
        }
        return result;
    }


    // --------------------------- PART 2 --------------------------- //
    public static File saveCompressedText(String text) throws IOException { // can i add an ioexception for the filewriter?
        HuffmanTree tree = new HuffmanTree();
        String compressedText = encode(text);
        // so i have to make a priority queue but then how do i get it to the compressed text to add to the file? do i use the encode method?


        File desktopFolder = new File(System.getProperty("user.home"), "Desktop"); // replacement only works for folders not files
        // it knows desktop is a folder because the first parameter is gethome
        // if file doesn't exist, it creates a new one or replaces if existing
        File compressed = new File(desktopFolder, "Compressed Text.txt"); // use slash if want to access a folder within desktop --> indicates a folder within a folder, will crete a new one if it doesn't exist
        // it knows the child is NOT a folder bc didn't use slashes in the name

        //creating the instance of file
        //File path = new File("/Users/username/Desktop");
        try{
            FileWriter wr = new FileWriter(compressed); // this actually MAKES the file
            wr.write(compressedText); // add the text that you want into the file you just created
            // wr.flush(); -- don't need this
            wr.close();
            // how do i dynamically do it?
        }
        catch(IOException exception){
            System.out.println("catch");
        }
        //File file = new File("/Users/username/Desktop"); // idk if this is how it works
        return compressed;
    }

    public static File saveHuffmanTree(HuffmanTree tree) throws IOException {
        File desktopFolder = new File(System.getProperty("user.home"), "Desktop"); // replacement only works for folders not files
        // toString of desktopFolder will print the location of desktop (/users/allwarda/...)
        System.out.println(desktopFolder.toString());
        FileOutputStream fileOutput = new FileOutputStream(desktopFolder.toString() + "/Huffman Tree.huffman", true); // don't put a colon anywhere (mac doesnt need it and windows will add it for you)
        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(tree);
        return new File(desktopFolder+"Huffman Tree.huff");
    }

    public static boolean loadHuffmanData() throws IOException{
        File desktopFolder = new File(System.getProperty("user.home"), "Desktop");
        //FileOutputStream tree = new FileOutputStream();
        File compressed = new File(desktopFolder, "Compressed Text.txt");
        File huffmanFile = new File(desktopFolder, "Huffman Tree.huffman");

        // think of try, catch as an if-else logic
        HuffmanTree tree = new HuffmanTree(); // must initialize OUTSIDE of try ctach

        BufferedReader br = new BufferedReader(new FileReader(compressed));

        // Declaring a string variable
        String text = "";
        // Condition holds true till
        // there is character in a string
        while ((text = br.readLine()) != null) {
            System.out.println(text);
        }


        try {
            // Reading the object from a file

            /*
            FileInputStream file = new FileInputStream(compressed);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            text = (String) in.readObject();
            System.out.println("text file found");


            in.close();
            file.close();

             */

            // -------- HUFFMAN FILE ------- //
            FileInputStream fileHuffman = new FileInputStream(huffmanFile);
            ObjectInputStream inHuffman = new ObjectInputStream(fileHuffman);
            tree = (HuffmanTree) inHuffman.readObject();
            System.out.println("huffman file found");

            inHuffman.close();
            fileHuffman.close();


            System.out.println("Object has been deserialized\n"
                    + "Data after Deserialization.");

            // System.out.println("z = " + object1.z);
        }

        catch (IOException ex) {
            System.out.println("One of the files could not be found."); // would get an IOException if the file could not be found
            // built-in error handling
            return false;
        }
        catch (NullPointerException ex) {
            System.out.println("Null pointer exception.");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException" +
                    " is caught");
        }

        if(text != null && text.equals("")){
            System.out.println("The file is empty, but it was still found.");
        }
        if(tree != null && tree.getRoot() != null){
            System.out.println("The tree is not empty!");
        }

        System.out.println(); // breakpoints stop the code before the line runs
        // breakpoints provide a high level view of objects at a time (shows all the instance variables and connected nodes within a tree, for example)
        return true;
    }




    public class NodeComparator implements Comparator<HuffmanTree.Node> {
        @Override // always use @override with implements -- way of getting past with subclassing where you can only have 1 subclass
        public int compare(HuffmanTree.Node o1, HuffmanTree.Node o2) {
            return o1.getFrequency().compareTo(o2.getFrequency());
        }
        // the implemented class methods are always empty
        // if you implement an interface, you are required to use the methods in the interface
        // must use a comparator if you use a priority queue

    }








}
