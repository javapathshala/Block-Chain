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
package com.jp.blockchain.basic2;

import java.util.List;

/**
 *
 *
 *
 * @author dimit.chadha
 * @since 1.0
 * @version 1.0
 */
public class RunChain
{

    public static void main(String[] args)
    {
        SimpleBlockchain<Transaction> chain1 = new SimpleBlockchain<Transaction>();

        chain1.add(new Transaction("A")).add(new Transaction("B")).add(new Transaction("C"));

        SimpleBlockchain<Transaction> chain2 = chain1.Clone();

        chain1.add(new Transaction("D"));

        System.out.println(String.format("Chain 1 Hash: %s", chain1.getHead().getHash()));
        System.out.println(String.format("Chain 2 Hash: %s", chain2.getHead().getHash()));
        System.out.println(
                String.format("Chains Are In Sync: %s", chain1.getHead().getHash().equals(chain2.getHead().getHash())));

        chain2.add(new Transaction("D"));

        System.out.println(String.format("Chain 1 Hash: %s", chain1.getHead().getHash()));
        System.out.println(String.format("Chain 2 Hash: %s", chain2.getHead().getHash()));
        System.out.println(
                String.format("Chains Are In Sync: %s", chain1.getHead().getHash().equals(chain2.getHead().getHash())));

        System.out.println(chain1.blockChainHash().equals(chain2.blockChainHash()));

        System.out.println("Current Chain Head Transactions: ");
        for (Block block : chain1.chain)
        {
            for (Object tx : block.getTransactions())
            {
                System.out.println("\t" + tx);
            }
        }

        // Block Merkle root should equal root hash in Merkle Tree computed from
        // block transactions
        Block headBlock = chain1.getHead();
        List<Transaction> merkleTree = headBlock.merkleTree();
        System.out.println(headBlock.getMerkleRoot().equals(merkleTree.get(merkleTree.size() - 1)));

        // Validate block chain
        System.out.println(chain1.validate());
        System.out.println(String.format("Chain is Valid: %s", chain1.validate()));
    }
}
