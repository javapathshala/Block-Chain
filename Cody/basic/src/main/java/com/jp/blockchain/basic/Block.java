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

import java.util.ArrayList;
import java.util.Date;

/**
 *
 *
 *
 * @author dimit.chadha
 * @since 1.0
 * @version 1.0
 */
public class Block
{

    public String hash;
    public String previousHash;
    private String data; //our data will be a simple message.
    private final long timeStamp; //as number of milliseconds since 1/1/1970.
    private int nonce; //miners to do proof-of-work by trying different variable values in the block until its hash starts with a certain number of 0�s.
    public ArrayList<Transaction> transactions = new ArrayList(); //our data will be a simple message.

    public String merkleRoot;

    //Block Constructor.
    public Block(String data, String previousHash)
    {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }

    //Block Constructor.
    public Block(String previousHash)
    {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
    }
    //Calculate new hash based on blocks contents

    public String calculateHash()
    {
        String calculatedhash = StringUtil.applySha256(
                previousHash
                + Long.toString(timeStamp)
                + Integer.toString(nonce)
                + merkleRoot
        );
        return calculatedhash;
    }

    //Increases nonce value until hash target is reached.
    public void mineBlock(int difficulty)
    {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
        while (!hash.substring(0, difficulty).equals(target))
        {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction)
    {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null)
        {
            return false;
        }
        if ((!"0".equals(previousHash)))
        {
            if ((transaction.processTransaction() != true))
            {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}
