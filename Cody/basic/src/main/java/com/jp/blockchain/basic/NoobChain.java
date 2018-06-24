/*
 * Copyright (c) Java Pathshala
 * Wisdom Being Shared
 * All rights reserved.
 *
 * No parts of this source code can be reproduced without written consent from
 * Java Pathshala
 * JavaPathshala.com
 *
 */
package com.jp.blockchain.basic;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;

/**
 *
 *
 *
 * @author dimit.chadha
 * @since 1.0
 * @version 1.0
 */
public class NoobChain
{

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    private static Block genesisBlock, secondBlock, thirdBlock;
    public static int difficulty = 5;

    /**
     * longest valid chain is accepted by the network.
     *
     * @param args
     */
    public static void main(String[] args)
    {
//        createBlocks();
        createBlockChain();
        printBlockChain();

        //mineBlocks();
        System.out.println("\nBlockchain is Valid: " + isChainValid());
        printBlockChain();

    }

    /**
     * Create 3 Blocks
     */
    private static void createBlocks()
    {
        genesisBlock = new Block("Hi im the first block", "0");
//        System.out.println("Hash for block 1 : " + genesisBlock.hash);
//        System.out.println("Previous Hash for block 1 : " + genesisBlock.previousHash);
//        System.out.println("");

        secondBlock = new Block("Yo im the second block", genesisBlock.hash);
//        System.out.println("Hash for block 2 : " + secondBlock.hash);
//        System.out.println("Previous Hash for block 2 : " + secondBlock.previousHash);
//        System.out.println("");

        thirdBlock = new Block("Hey im the third block", secondBlock.hash);
//        System.out.println("Hash for block 3 : " + thirdBlock.hash);
//        System.out.println("Previous Hash for block 3 : " + thirdBlock.previousHash);
//        System.out.println("");
    }

    private static void createBlockChain()
    {
        //add our blocks to the blockchain ArrayList:
//        blockchain.add(genesisBlock);
        blockchain.add(new Block("Hi im the first block", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Yo im the second block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to Mine block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Yo im the third block", blockchain.get(blockchain.size() - 1).hash));
        System.out.println("Trying to Mine block 3... ");
        blockchain.get(2).mineBlock(difficulty);

//        blockchain.add(secondBlock);
//        blockchain.add(thirdBlock);
    }

    /**
     * Print Block Chain in JSON Format
     */
    private static void printBlockChain()
    {
        System.out.println("Block Chain in JSON Format ");
        System.out.println("");
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }

    private static void mineBlocks()
    {
        mineBlock(0);
        mineBlock(1);
        mineBlock(2);
    }

    private static void mineBlock(int blockNumber)
    {
        System.out.println("Trying to Mine block ... " + (blockNumber + 1));
        blockchain.get(blockNumber).mineBlock(difficulty);
        System.out.println("");
    }

    /**
     * Checking Integrity of block chain created with 3 blocks
     * <p>
     * Any change to the block chain’s blocks will cause this method to return false.
     * <p>
     * What’s to stop someone tampering with data in an old block then creating a whole new longer
     * block chain and presenting that to the network --> PROOF OF WORK
     * The hash cash proof of work system means it takes considerable time and computational power to create new blocks.
     * Hence the attacker would need more computational power than the rest of the peers combined.
     *
     * @return
     */
    public static Boolean isChainValid()
    {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++)
        {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.calculateHash()))
            {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash))
            {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget))
            {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

}
