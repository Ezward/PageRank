package com.lumpofcode.pagerank;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by emurphy on 2/26/15.
 */
public class SparseGrowableVectorTest
{
    @Test
    public void testSet()
    {
        final SparseGrowableVector<Double> theVector = new SparseGrowableVector<>(2, Double.valueOf(0.0));
        assertTrue("The size starts out at zero.", 0 == theVector.size());

        theVector.set(1000, 1000.0);
        assertTrue(1000.0 == theVector.get(1000));
        assertTrue("should be size 1.", 1 == theVector.size());

        theVector.set(10, 10.0);
        assertTrue(10.0 == theVector.get(10));
        assertTrue("should be size 2.", 2 == theVector.size());

        theVector.set(100, 100.0);
        assertTrue(100.0 == theVector.get(100));
        assertTrue("should be size 3.", 3 == theVector.size());

        theVector.set(10000, 10000.0);
        assertTrue(10000.0 == theVector.get(10000));
        assertTrue("should be size 4.", 4 == theVector.size());

    }

    @Test
    public void testRemove()
    {
        final SparseGrowableVector<Double> theVector = new SparseGrowableVector<>(2, Double.valueOf(0.0));

        theVector.set(1000, 1000.0);
        theVector.set(10, 10.0);
        theVector.set(100, 100.0);
        theVector.set(10000, 10000.0);

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
        final SparseGrowableVector<Double> theVector = new SparseGrowableVector<>(2, Double.valueOf(0.0));

        theVector.set(1000, 1000.0);
        theVector.set(10, 10.0);
        theVector.set(100, 100.0);
        theVector.set(10000, 10000.0);

        final SparseGrowableVector<Double>.IndexIterator theIterator  = theVector.iterator();

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