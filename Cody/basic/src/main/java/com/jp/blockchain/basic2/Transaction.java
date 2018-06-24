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

/**
 *
 *
 *
 * @author dimit.chadha
 * @since 1.0
 * @version 1.0
 */
public class Transaction implements Tx
{

    private String hash;
    private String value;

    public String hash()
    {
        return hash;
    }

    public Transaction(String value)
    {
        this.hash = SHA256.generateHash(value);
        this.setValue(value);
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {

        // new value need to recalc hash
        this.hash = SHA256.generateHash(value);
        this.value = value;
    }

    public String toString()
    {
        return this.hash + " : " + this.getValue();
    }

}
