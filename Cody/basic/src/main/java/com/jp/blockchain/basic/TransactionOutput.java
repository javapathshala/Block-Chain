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

import java.security.PublicKey;

/**
 * Transaction outputs will show the final amount sent to each party from the transaction.
 * These, when referenced as inputs in new transactions, act as proof that you have coins to send.
 *
 *
 * @author dimit.chadha
 * @since 1.0
 * @version 1.0
 */
public class TransactionOutput
{

    public String id;
    public PublicKey reciepient; //also known as the new owner of these coins.
    public float value; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId)
    {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient) + Float.toString(value) + parentTransactionId);
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey)
    {
        return (publicKey == reciepient);
    }
}
