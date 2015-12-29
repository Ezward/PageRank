package com.lumpofcode.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by emurphy on 2/26/15.
 */
public class SparseGrowableVector<K extends Object> implements SparseVector<K>
{
    private final int blockSize;
    public final K zero;
    private final List<SparseArrayVector<K>> blocks;

    public SparseGrowableVector(final int blockSize)
    {
        this(blockSize, null);
    }

    public SparseGrowableVector(final int blockSize, final K zero)
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
    public void set(final int theVectorIndex, final K theValue)
    {
        //
        // figure out which block we should be in
        //
        int theBlockIndex = findBlock(theVectorIndex);
        SparseArrayVector<K> theBlock = blocks.get(theBlockIndex);

        if((this.zero == theValue) || ((null != this.zero) && this.zero.equals(theValue)))
        {
            //
            // setting zero may create an empty element in the block.
            // in this case, we want to compact to remove the empty element
            //
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
                    final SparseArrayVector<K> theCompactToBlock = blocks.get(i);
                    final SparseArrayVector<K> theCompactFromBlock = blocks.get(i + 1);

                    theCompactToBlock.set(theCompactFromBlock.lowerIndex(), theCompactFromBlock.get(theCompactFromBlock.lowerIndex()));
                    theCompactFromBlock.set(theCompactFromBlock.lowerIndex(), this.zero);
                }

                if(blocks.size() > 1)
                {
                    //
                    // check to see if the last block is empty.
                    // free it if it is empty
                    //
                    final SparseArrayVector<K> theLastBlock = blocks.get(blocks.size() - 1);
                    if (theLastBlock.isEmpty())
                    {
                        blocks.remove(blocks.size() - 1);
                    }
                }
            }
        }
        else    //(this.zero != theValue)
        {
            if (!theBlock.safeSet(theVectorIndex, theValue))
            {
                // TODO: the issue is that we could not fit it (16) because it was off the end of the first block, which was full.
                //       this was NOT an issue of being unable to insert into the block, so we simply need to add a block
                //       and then insert into the new block.
                //       the way the code is now, it backs up to the first block and insert into their (overwriting 15 with 16)
                //

                //
                // determine if we need to add another block
                // in order to fit the new value.
                //
                final SparseArrayVector<K> theLastBlock = this.blocks.get(blocks.size() - 1);
                if(theLastBlock.isFull())
                {
                    addBlock();

                    //
                    // re-calculate which block we should be in
                    // because we might need to go into the new block.
                    //
                    theBlockIndex = findBlock(theVectorIndex);
                    theBlock = blocks.get(theBlockIndex);
                }

                //
                // now percolate until we reach our insert block
                //
                for (int i = blocks.size() - 1; i > theBlockIndex; i -= 1)
                {
                    final SparseArrayVector<K> thePercolateFromBlock = this.blocks.get(i - 1);
                    final SparseArrayVector<K> thePercolateToBlock = this.blocks.get(i);

                    //
                    // copy up to the percolate to block
                    // then zero the value from the percolate from block.
                    thePercolateToBlock.set(thePercolateFromBlock.upperIndex(), thePercolateFromBlock.get(thePercolateFromBlock.upperIndex()));
                    thePercolateFromBlock.set(thePercolateFromBlock.upperIndex(), this.zero);
                }

                //
                // now we should be able to set into the desired block
                // TODO: actually, we should not be inserting into the original block, because our data
                //       should not have been in it; it could not fit and so should be on the subsequent block
                //       so this line accidentally overwrites the last value in theBlock, when it
                //       should be writing to the first value in the next block.
                //
                theBlock.set(theVectorIndex, theValue);
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
    public K get(final int theVectorIndex, final K theDefault)
    {
        final int theBlockIndex = findBlock(theVectorIndex);
        final SparseArrayVector<K> theBlock = blocks.get(theBlockIndex);

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
    public K get(final int theVectorIndex)
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

        //
        // use a binary search to find the block index.
        //
        int theLeft = 0;
        int theRight = count;

        while (theLeft < theRight)
        {
            final int i = (theLeft + theRight) / 2;
            final SparseArrayVector<K> theBlock = blocks.get(i);
            if (theBlock.isEmpty() || (theVectorIndex < theBlock.lowerIndex()))
            {
                theRight = i; // search left
            }
            else if (theVectorIndex > theBlock.upperIndex())
            {
                theLeft = i + 1; // search right
            }
            else
            {
                return i; // this is the block
            }
        }

        // less than first blocks lower index
        // or
        // greater than the last block's upper index
        return (theLeft < count) ? theLeft : (count - 1);

//        int i = 0;
//        while((i < count) && (blocks.get(i).upperIndex() < theVectorIndex))
//        {
//            i += 1;
//        }
//        return (i < count) ? i : (count - 1);  // insert into the last block.
    }

    private SparseArrayVector<K> addBlock()
    {
        final SparseArrayVector<K> theBlock = new SparseArrayVector<K>(this.blockSize, this.zero);
        this.blocks.add(blocks.size(), theBlock);
        return theBlock;
    }

    // TODO: test this iterator against the indices iterator to show values come out in same order
    public Iterator<K> values()
    {
        return new ValueIteratorImpl();
    }

    public IntegerIterator indices()
    {
        return new IndexIteratorImpl();
    }

    private final class IndexIteratorImpl implements IntegerIterator
    {
        private int indexOfBlock;
        private IntegerIterator indexIterator;
        private int overallIndex;

        private IndexIteratorImpl()
        {
            this.indexOfBlock = 0;
            this.indexIterator = blocks.get(0).indices();
            this.overallIndex = 0;
        }

        public boolean hasNext()
        {
            if(this.indexOfBlock >= blocks.size()) return false;
            if(null == this.indexIterator) return false;
            return this.indexIterator.hasNext();
        }

        public int next()
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
                    this.indexIterator = blocks.get(this.indexOfBlock).indices();
                }
            }

            return theNextIndex;

        }
    }

    private final class ValueIteratorImpl implements Iterator<K>
    {
        private int indexOfBlock;
        private IntegerIterator indexIterator;
        private int overallIndex;

        private ValueIteratorImpl()
        {
            this.indexOfBlock = 0;
            this.indexIterator = blocks.get(0).indices();
            this.overallIndex = 0;
        }

        public boolean hasNext()
        {
            if(this.indexOfBlock >= blocks.size()) return false;
            if(null == this.indexIterator) return false;
            return this.indexIterator.hasNext();
        }

        public K next()
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
                    this.indexIterator = blocks.get(this.indexOfBlock).indices();
                }
            }

            return blocks.get(this.indexOfBlock).get(theNextIndex);
        }
    }

}
