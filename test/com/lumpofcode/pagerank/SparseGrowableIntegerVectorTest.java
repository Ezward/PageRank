package com.lumpofcode.pagerank;

import com.lumpofcode.collection.IndexIterator;
import com.lumpofcode.collection.SparseGrowableIntegerVector;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by emurphy on 2/27/15.
 */
public class SparseGrowableIntegerVectorTest
{
    @Test
    public void testSet()
    {
        final SparseGrowableIntegerVector theVector = new SparseGrowableIntegerVector(2);
        assertTrue("The size starts out at zero.", 0 == theVector.size());

        theVector.set(1000, 1001);
        assertTrue(1001 == theVector.get(1000));
        assertTrue("should be size 1.", 1 == theVector.size());

        theVector.set(10, 11);
        assertTrue(11 == theVector.get(10));
        assertTrue("should be size 2.", 2 == theVector.size());

        theVector.set(100, 101);
        assertTrue(101 == theVector.get(100));
        assertTrue("should be size 3.", 3 == theVector.size());

        theVector.set(10000, 10001);
        assertTrue(10001 == theVector.get(10000));
        assertTrue("should be size 4.", 4 == theVector.size());

    }

    @Test
    public void testRemove()
    {
        final SparseGrowableIntegerVector theVector = new SparseGrowableIntegerVector(2);

        theVector.set(1000, 1001);
        theVector.set(10, 11);
        theVector.set(100, 101);
        theVector.set(10000, 10001);

        assertTrue("should be size 4.", 4 == theVector.size());

        theVector.set(100, theVector.zero);
        assertTrue("should be size 3.", 3 == theVector.size());

        theVector.set(10000, theVector.zero);
        assertTrue("should be size 2.", 2 == theVector.size());

        theVector.set(10, theVector.zero);
        assertTrue("should be size 1.", 1 == theVector.size());

        theVector.set(1000, theVector.zero);
        assertTrue("should be size 0.", 0 == theVector.size());

    }

    @Test
    public void testIterator()
    {
        final SparseGrowableIntegerVector theVector = new SparseGrowableIntegerVector(2);

        theVector.set(1000, 1001);
        theVector.set(10, 11);
        theVector.set(100, 101);
        theVector.set(10000, 10001);

        final IndexIterator theIterator  = theVector.iterator();

        assertNotNull("The iterator should not be null.", theIterator);

        assertTrue("There should be a next value", theIterator.hasNext());
        assertTrue("The first index should be 10.", 10 == theIterator.next());

        assertTrue("There should be a next value", theIterator.hasNext());
        assertTrue("The second index should be 100.", 100 == theIterator.next());

        assertTrue("There should be a next value", theIterator.hasNext());
        assertTrue("The third index should be 1000.", 1000 == theIterator.next());

        assertTrue("There should be a next value", theIterator.hasNext());
        assertTrue("The fourth index should be 10000.", 10000 == theIterator.next());

        assertFalse("There should NOT be a next value.", theIterator.hasNext());
    }

}
