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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Holds our public key and private keys:
 * <p>
 * For our ‘noobcoin’ the public key will act as our address. It’s OK to share this public key with others to receive payment.
 * Our private key is used to sign our transactions,so that nobody can spend our noobcoins other than the owner
 * of the private key. Users will have to keep their private key Secret ! We also send our public key along
 * with the transaction and it can be used to verify that our signature
 * is valid and data has not been tampered with.
 * <p>
 * The private key is used to sign the data we don’t want to be tampered with.
 * The public key is used to verify the signature.
 *
 * @author dimit.chadha
 * @since 1.0
 * @version 1.0
 */
public class Wallet
{

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public Wallet()
    {
        generateKeyPair();
    }

    /**
     * Elliptic-curve cryptography to Generate our KeyPairs.
     */
    public void generateKeyPair()
    {
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    //returns balance and stores the UTXO's owned by this wallet in this.UTXOs
    public float getBalance()
    {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : NoobChainTx.UTXOs.entrySet())
        {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey))
            { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id, UTXO); //add it to our list of unspent transactions.
                total += UTXO.value;
            }
        }
        return total;
    }
    //Generates and returns a new transaction from this wallet.

    public Transaction sendFunds(PublicKey _recipient, float value)
    {
        if (getBalance() < value)
        { //gather balance and check funds.
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        //create array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet())
        {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value)
            {
                break;
            }
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs)
        {
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

}
