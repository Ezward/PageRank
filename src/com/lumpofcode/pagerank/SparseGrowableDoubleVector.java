package com.lumpofcode.pagerank;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by emurphy on 2/26/15.
 */
public class SparseGrowableDoubleVector
{
    private final int blockSize;
    public final double zero;
    private final List<SparseDoubleVector> blocks;

    public SparseGrowableDoubleVector(final int blockSize)
    {
        this(blockSize, 0.0);
    }

    public SparseGrowableDoubleVector(final int blockSize, final double zero)
    {
        this.blockSize = blockSize;
        this.zero = zero;
        this.blocks = new ArrayList<>(1024);   // this is our initial guess, it will expand as necessary.
        addBlock(); // create the first block.
    }

    public int size()
    {
        return ((blocks.size() - 1) * blockSize) + blocks.get(blocks.size() - 1).size();
    }


    /**
     * Set the value of the element at the given index.
     *
     * @param theVectorIndex
     * @param theValue
     * @throws IndexOutOfBoundsException if there is not room to do
     *         the insert and the space cannot be expanded.
     */
    public void set(final int theVectorIndex, final double theValue)
    {
        final int theBlockIndex = findBlock(theVectorIndex);
        final SparseDoubleVector theBlock = blocks.get(theBlockIndex);

        if(this.zero != theValue)
        {
            if (!theBlock.safeSet(theVectorIndex, theValue))
            {

                //
                // determine if we need to add another block
                // in order to fit the new value.
                //
                final SparseDoubleVector theLastBlock = this.blocks.get(blocks.size() - 1);
                if(theLastBlock.isFull())
                {
                    addBlock();
                }

                //
                // now percolate until we reach our insert block
                //
                for (int i = blocks.size() - 1; i > theBlockIndex; i -= 1)
                {
                    final SparseDoubleVector thePercolateFromBlock = this.blocks.get(i - 1);
                    final SparseDoubleVector thePercolateToBlock = this.blocks.get(i);

                    //
                    // copy up to the percolate to block
                    // then zero the value from the percolate from block.
                    thePercolateToBlock.set(thePercolateFromBlock.upperIndex(), thePercolateFromBlock.get(thePercolateFromBlock.upperIndex()));
                    thePercolateFromBlock.set(thePercolateFromBlock.upperIndex(), this.zero);
                }

                //
                // now we should be able to set into the desired block
                //
                theBlock.set(theVectorIndex, theValue);
            }
        }
        else    // we are zeroing out an element.
        {
            theBlock.set(theVectorIndex, theValue);
            if(!theBlock.isFull())
            {
                //
                // We have a space in our block that needs to be filled in.
                // Fill in the blocks by taking the first element in the subsequent block.
                // Do this for all the rest of the blocks in the array.
                //
                for (int i = theBlockIndex; i < (blocks.size() - 1); i += 1)
                {
                    final SparseDoubleVector theCompactToBlock = blocks.get(i);
                    final SparseDoubleVector theCompactFromBlock = blocks.get(i + 1);

                    theCompactToBlock.set(theCompactFromBlock.lowerIndex(), theCompactFromBlock.get(theCompactFromBlock.lowerIndex()));
                    theCompactFromBlock.set(theCompactFromBlock.lowerIndex(), this.zero);
                }

                if(blocks.size() > 1)
                {
                    //
                    // check to see if the last block is empty.
                    // free it if it is empty
                    //
                    final SparseDoubleVector theLastBlock = blocks.get(blocks.size() - 1);
                    if (theLastBlock.isEmpty())
                    {
                        blocks.remove(blocks.size() - 1);
                    }
                }
            }
        }
    }

    /**
     * Get the element at the given index.
     * If there is no element at the index, return the given default.
     *
     * @param theVectorIndex the index into the vector
     * @param theDefault the value to return if there is no element at the index.
     * @return the value of the element at the index or the default if there
     *         is no element at the given index.
     */
    public double get(final int theVectorIndex, final double theDefault)
    {
        final int theBlockIndex = findBlock(theVectorIndex);
        final SparseDoubleVector theBlock = blocks.get(theBlockIndex);

        return theBlock.get(theVectorIndex, theDefault);
    }

    /**
     * Get the element at the given index.
     * If there is no element at the index, return the given default.
     *
     * @param theVectorIndex the index into the vector
     * @return the value of the element at the index or zero if there
     *         is no element at the given index.
     */
    public double get(final int theVectorIndex)
    {
        return get(theVectorIndex, this.zero);
    }

    /**
     * Find the block that contains the given vector index.
     *
     * @param theVectorIndex
     * @return the block index
     */
    private int findBlock(final int theVectorIndex)
    {
        final int count = blocks.size();
        int i = 0;
        while((i < count) && (blocks.get(i).upperIndex() < theVectorIndex))
        {
            i += 1;
        }
        return (i < count) ? i : (count - 1);  // insert into the last block.
    }

    private SparseDoubleVector addBlock()
    {
        final SparseDoubleVector theBlock = new SparseDoubleVector(this.blockSize, this.zero);
        this.blocks.add(blocks.size(), theBlock);
        return theBlock;
    }

    public IndexIterator iterator()
    {
        return new IndexIterator();
    }

    public final class IndexIterator
    {
        private int indexOfBlock;
        private SparseDoubleVector.IndexIterator indexIterator;
        private int overallIndex;

        private IndexIterator()
        {
            this.indexOfBlock = 0;
            this.indexIterator = blocks.get(0).iterator();
            this.overallIndex = 0;
        }

        public boolean hasNext()
        {
            if(this.indexOfBlock >= blocks.size()) return false;
            if(null == this.indexIterator) return false;
            return this.indexIterator.hasNext();
        }

        public double next()
        {
            if(!hasNext())
            {
                throw new NoSuchElementException();
            }

            //
            // this is the next value
            //
            final int theNextIndex = this.indexIterator.next();

            //
            // if the current block iterator has a next, we are done.
            //
            if(!this.indexIterator.hasNext())
            {
                //
                // start iterating next block if there is one.
                //
                this.indexIterator = null;
                this.indexOfBlock += 1;
                if(this.indexOfBlock < blocks.size())
                {
                    this.indexIterator = blocks.get(this.indexOfBlock).iterator();
                }
            }

            return theNextIndex;

        }
    }
}
