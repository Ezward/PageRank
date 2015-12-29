package com.lumpofcode.pagerank;

import com.lumpofcode.collection.IntegerArray;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by emurphy on 2/28/15.
 */
public class IntegerArrayTest
{
    @Test
    public void testConstructor()
    {
        IntegerArray theArray = new IntegerArray(3);

        //
        // array always starts out empty
        //
        assertTrue("Should be size 0", 0 == theArray.size());

        //
        // it's ok to start out zero size
        //

        theArray = new IntegerArray(0);
        assertTrue("Should be size 0", 0 == theArray.size());
    }

    @Test
    public void testSet()
    {
        final IntegerArray theArray = new IntegerArray(0, 16);

        for(int i = 0; i < 100; i += 1)
        {
            theArray.set(i, i);
            assertTrue("Size should be " + String.valueOf(i + 1), (i + 1) == theArray.size());
        }

        for(int i = 0; i < 100; i += 1)
        {
            assertTrue("Value should be " + String.valueOf(i), i == theArray.get(i));
        }
    }

    @Test
    public void testInsert()
    {
        IntegerArray theArray = new IntegerArray(0, 16);

        //
        // insert at front
        //
        for(int i = 0; i < 100; i += 1)
        {
            theArray.insertAt(0, i);
            assertTrue("Size should be " + String.valueOf(i + 1), (i + 1) == theArray.size());
        }

        for(int i = 0; i < 100; i += 1)
        {
            assertTrue("Value should be " + String.valueOf(99 - i), (99 - i) == theArray.get(i));
        }

        //
        // insert at end
        //
        theArray = new IntegerArray(0, 16);
        for(int i = 0; i < 100; i += 1)
        {
            theArray.insertAt(i, i);
            assertTrue("Size should be " + String.valueOf(i + 1), (i + 1) == theArray.size());
        }

        for(int i = 0; i < 100; i += 1)
        {
            assertTrue("Value should be " + String.valueOf(i), i == theArray.get(i));
        }

    }

    @Test
    public void testRemove()
    {
        IntegerArray theArray = new IntegerArray(0, 16);

        for(int i = 0; i < 100; i += 1)
        {
            theArray.set(i, i);
            assertTrue("Size should be " + String.valueOf(i + 1), (i + 1) == theArray.size());
        }

        for(int i = 0; i < 100; i += 1)
        {
            assertTrue("Value should be " + String.valueOf(i), i == theArray.removeAt(0));
            assertTrue("Size should be " + String.valueOf(99 - i), (99 - i) == theArray.size());
        }

        //
        // remove at the end
        //
        theArray = new IntegerArray(0, 16);
        for(int i = 0; i < 100; i += 1)
        {
            theArray.set(i, i);
            assertTrue("Size should be " + String.valueOf(i + 1), (i + 1) == theArray.size());
        }

        for(int i = 99; i >= 0; i -= 1)
        {
            assertTrue("Value should be " + String.valueOf(i), i == theArray.removeAt(i));
            assertTrue("Size should be " + String.valueOf(i), i == theArray.size());
        }

    }

    @Test
    public void testPushPeekPop()
    {
        final IntegerArray theArray = new IntegerArray(0, 16);

        for(int i = 0; i < 100; i += 1)
        {
            theArray.push(i);
            assertTrue("Size should be " + String.valueOf(i + 1), (i + 1) == theArray.size());
        }

        for(int i = 0; i < 100; i += 1)
        {
            assertTrue("Value should be " + String.valueOf(99 - i), (99 - i) == theArray.peek());
            assertTrue("Value should be " + String.valueOf(99 - i), (99 - i) == theArray.pop());
            assertTrue("Size should be " + String.valueOf(99 - i), (99 - i) == theArray.size());
        }
    }


}
