package com.lumpofcode.pagerank;

/**
 * Created by emurphy on 2/25/15.
 */
public interface SparseVector<K>
{
    void set(int theIndex, K theValue);

    K get(int theIndex, K theDefault);

    K get(int theIndex);
}
