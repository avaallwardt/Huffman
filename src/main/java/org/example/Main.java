package org.example;

import java.io.IOException;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) throws IOException {

        HuffmanTree tree = new HuffmanTree();

        tree.buildTree(tree.buildFrequencyTable("hello world"));

        HuffmanAlgo.saveCompressedText("hello world");
        HuffmanAlgo.saveHuffmanTree(tree);

        HuffmanAlgo.loadHuffmanData();

    }
}