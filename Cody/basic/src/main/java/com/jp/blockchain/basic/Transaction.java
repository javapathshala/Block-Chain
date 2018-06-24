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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 * Each transaction will carry a certain amount of data:
 * The public key(address) of the sender of funds.
 * The public key(address) of the receiver of funds.
 * The value/amount of funds to be transferred.
 * Inputs, which are references to previous transactions that prove the sender has funds to send.
 * Outputs, which shows the amount relevant addresses received in the transaction.
 * ( These outputs are referenced as inputs in new transactions )
 * A cryptographic signature, that proves the owner of the address is the one sending this transaction and that
 * the data hasn’t been changed.( for example: preventing a third party from changing the amount sent )
 * <p>
 * <p>
 * The private key is used to sign the data and the public key can be used to verify its integrity.
 *
 * @author dimit.chadha
 * @since 1.0
 * @version 1.0
 */
public class Transaction
{

    public String transactionId; // this is also the hash of the transaction.
    public PublicKey sender; // senders address/public key.
    public PublicKey reciepient; // Recipients address/public key.
    public float value;
    public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // a rough count of how many transactions have been generated.

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs)
    {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // This Calculates the transaction hash (which will be used as its Id)
    private String calulateHash()
    {
        sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender)
                + StringUtil.getStringFromKey(reciepient)
                + Float.toString(value) + sequence
        );
    }

    //Signs all the data we dont wish to be tampered with.
    public void generateSignature(PrivateKey privateKey)
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    //Verifies the data we signed hasnt been tampered with
    public boolean verifiySignature()
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
//Returns true if new transaction could be created.

    public boolean processTransaction()
    {
        if (verifiySignature() == false)
        {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //gather transaction inputs (Make sure they are unspent):
        for (TransactionInput i : inputs)
        {
            i.UTXO = NoobChainTx.UTXOs.get(i.transactionOutputId);
        }

        //check if transaction is valid:
        if (getInputsValue() < NoobChainTx.minimumTransaction)
        {
            System.out.println("#Transaction Inputs to small: " + getInputsValue());
            return false;
        }

        //generate transaction outputs:
        float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
        transactionId = calulateHash();
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); //send value to recipient
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //send the left over 'change' back to sender

        //add outputs to Unspent list
        for (TransactionOutput o : outputs)
        {
            NoobChainTx.UTXOs.put(o.id, o);
        }

        //remove transaction inputs from UTXO lists as spent:
        for (TransactionInput i : inputs)
        {
            if (i.UTXO == null)
            {
                continue; //if Transaction can't be found skip it
            }
            NoobChainTx.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputsValue()
    {
        float total = 0;
        for (TransactionInput i : inputs)
        {
            if (i.UTXO == null)
            {
                continue; //if Transaction can't be found skip it
            }
            total += i.UTXO.value;
        }
        return total;
    }

//returns sum of outputs:
    public float getOutputsValue()
    {
        float total = 0;
        for (TransactionOutput o : outputs)
        {
            total += o.value;
        }
        return total;
    }

}
