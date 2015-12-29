package com.lumpofcode.pagerank;

import com.lumpofcode.collection.*;
import com.lumpofcode.math.VectorHelper;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by emurphy on 2/8/15.
 */
public final class PageRankCalculator
{
    /**
     * Calculate the page rank for a given set of pages with a given link structure.
     *
     * @param theMatrix
     * @param thePageCount
     * @param beta
     * @param epsilon
     * @param theMaxIterations
     * @return
     */
    public static double[] calculateSparsePageRank(final SparseDoubleMatrix theMatrix, final int thePageCount, final double beta, final double epsilon, final int theMaxIterations)
    {
        assert(thePageCount > 0);
        assert((beta >= 0.0) && (beta <= 1.0));
        assert((epsilon >= 0.0));

        final Date theStartTime = new Date();

        // initialize storage
        final int dimension = thePageCount;
        double[] thePageRankVector = new double[dimension];
        double[] thePageRankEstimate = new double[dimension];

        // printSparseMatrix(theMatrix);

        // initialize the page rank vector to our first estimate; assume equal importance to all pages
        // (distribute importance evenly among pages)
        initializePageRankVector(thePageRankVector);

        //
        // do a power iteration;
        // 1. calculate a page rank vector estimate
        // 2. calculate the sum of the differences between the page rank vector and new page rank estimate vector
        // 3. If the difference is greater than epsilon, make the new page rank estimate the page rank, repeat starting at 1..
        // 4. we have converged on the page rank vector and we are done.
        //
        int theIterations = 0;
        do
        {
            System.out.println(
                    "Iteration $i at $t."
                            .replace("$i", String.valueOf(theIterations))
                            .replace("$t", (new Date()).toString()));

            // printPageRankVector(thePageRankVector);

            // 1. calculate a page rank vector estimate
            calculateSparsePageRankVector(theMatrix, thePageRankVector, thePageRankEstimate, beta);


            //
            // the estimate is the new page rank vector,
            // we use the old page rank vector as space for a new estimate.
            //
            final double[] temp = thePageRankVector;
            thePageRankVector = thePageRankEstimate;
            thePageRankEstimate = temp;

            // 2. calculate the sum of the differences between the page rank vector and new page rank estimate vector
            // 3. If the difference is greather than epsilon, make the new page rank estimate the page rank, repeat starting at 1..
        }  while((++theIterations < theMaxIterations) && (calculateSumOfDifferences(thePageRankVector, thePageRankEstimate) > epsilon));

        final Date theEndTime = new Date();

        System.out.println(
                "Complete at $t."
                        .replace("$t", theEndTime.toString()));
        printPageRankVector(thePageRankVector, "sparsepagerank.txt");

        // 4. we have converged on the page rank vector and we are done.
        return thePageRankVector;

    }

    public static double[] calculatePageRank(final PageLinks thePageLinks, final int thePageCount, final double beta, final double epsilon, final int theMaxIterations)
    {
        assert(thePageCount > 0);
        assert((beta >= 0.0) && (beta <= 1.0));
        assert((epsilon >= 0.0));

        final Date theStartTime = new Date();

        // initialize storage
        final int dimension = thePageCount;
        final double[][] theMatrix = new double[dimension][dimension];  // [fromPage][toPage]
        double[] thePageRankVector = new double[dimension];
        double[] thePageRankEstimate = new double[dimension];

        initializeMatrixFromPageLinks(theMatrix, thePageLinks, dimension, beta);
        printMatrix(theMatrix);

        // initialize the page rank vector to our first estimate; assume equal importance to all pages
        // (distribute importance evenly among pages)
        initializePageRankVector(thePageRankVector);

        //
        // do a power iteration;
        // 1. calculate a page rank vector estimate
        // 2. calculate the sum of the differences between the page rank vector and new page rank estimate vector
        // 3. If the difference is greater than epsilon, make the new page rank estimate the page rank, repeat starting at 1..
        // 4. we have converged on the page rank vector and we are done.
        //
        int theIterations = 0;
        do
        {
            System.out.println(
                    "Iteration $i at $t."
                            .replace("$i", String.valueOf(theIterations))
                            .replace("$t", (new Date()).toString()));
            printPageRankVector(thePageRankVector, "pagerank.txt");

            // 1. calculate a page rank vector estimate
            calculatePageRankVector(theMatrix, thePageRankVector, thePageRankEstimate, beta);

            //
            // the estimate is the new page rank vector,
            // we use the old page rank vector as space for a new estimate.
            //
            final double[] temp = thePageRankVector;
            thePageRankVector = thePageRankEstimate;
            thePageRankEstimate = temp;

            // 2. calculate the sum of the differences between the page rank vector and new page rank estimate vector
            // 3. If the difference is greather than epsilon, make the new page rank estimate the page rank, repeat starting at 1..
        }  while((++theIterations < theMaxIterations) && ((theIterations <= 10) ||(calculateSumOfDifferences(thePageRankVector, thePageRankEstimate) > epsilon)));

        final Date theEndTime = new Date();

        System.out.println(
                "Complete at $t."
                        .replace("$t", theEndTime.toString()));

        printPageRankVector(thePageRankVector, "pagerank.txt");

        // 4. we have converged on the page rank vector and we are done.
        return thePageRankVector;

    }


    /**
     * Calculate a new estimate for the page rank vector.
     *
     * @param theMatrix the link matrix[theFromPage][theToPage]
     * @param thePageRankVector the current page rank vector
     * @param thePageRankEstimate on completion, a new estimate for the page rank vector
     * @param beta the probability (0 <= beta <= 1) that a random walker will take a link (rather than teleport)
     */
    protected static void calculatePageRankVector(final double[][] theMatrix, final double[] thePageRankVector, final double[] thePageRankEstimate, final double beta)
    {
        assert((beta >= 0.0) && (beta <= 1.0));

        //
        // v' = B*M*v + (1 - B)e/n
        //
        // where
        // v' = the new estimate for the page rank vector
        // B = beta; the probability that a random walker will choose and out-link (rather than teleport)
        // M = the link matrix
        // v = the page rank vector
        // e = the unity vector (vector of ones)
        // n = the dimension (the number of pages)
        //
        // NOTE: we have already pre-applied Beta to each matrix element at initialization, so we don't apply it below.
        //
        final int n = thePageRankVector.length;
        final double theTaxation = (1.0 - beta)/n;
        for(int i = 0; i < n; i += 1)   // choose the row in the matrix (m) and the estimate vector (v')
        {
            //
            // multiply each row by the page rank vector and sum them, then add taxation term
            //
            thePageRankEstimate[i] = theTaxation;
            for(int j = 0; j < n; j += 1)   // choose the column in the matrix (m) and row in the page rank vector (v)
            {
                //
                // NOTE: beta is already applied to the elements of theMatrix, when it is initialized
                //
                thePageRankEstimate[i] += theMatrix[i][j] * thePageRankVector[i];
            }
        }
    }


    protected static void calculateSparsePageRankVector(final SparseDoubleMatrix theMatrix, final double[] thePageRankVector, final double[] thePageRankEstimate, final double beta)
    {
        assert((beta >= 0.0) && (beta <= 1.0));

        //
        // v' = B*M*v + (1 - B)e/n
        //
        // where
        // v' = the new estimate for the page rank vector
        // B = beta; the probability that a random walker will choose and out-link (rather than teleport)
        // M = the link matrix
        // v = the page rank vector
        // e = the unity vector (vector of ones)
        // n = the dimension (the number of pages)
        //
        // NOTE: we have already pre-applied Beta to each matrix element at initialization, so we don't apply it below.
        //
        final int n = thePageRankVector.length;
        final double theTaxation = (1.0 - beta)/n;

        //
        // preinitialize each vector element with the teleport taxation
        // this also clears the previous values so we can reuse the
        // vector and not have to allocate a new one.
        //
        for(int i = 0; i < n; i += 1)
        {
            thePageRankEstimate[i] = theTaxation;
        }


        // choose the row in the matrix (m) and the estimate vector (v')
        //
        // iterate the columns for the given row
        //
        final IntegerIterator theColumnIterator = theMatrix.columnIterator();
        while(theColumnIterator.hasNext())
        {
            final int j = theColumnIterator.next();
            final IntegerIterator theRowIterator = theMatrix.rowIterator(j);
            while(theRowIterator.hasNext())
            {
                final int i = theRowIterator.next();
                final double theValue = theMatrix.get(i, j, 0.0);

                thePageRankEstimate[i] += theValue * thePageRankVector[j];
            }
        }

        //
        // calcualate the sum of the elements of thePageRankVectorEstimate;
        // - it may be less than 1 because of dead-ends that leaked page rank.
        // - Fix this so that the magnitude is one by adding
        //   (1 - S(r)) / n to each element of r, where S(r) is the sum
        //   of the elements of r.
        //
        // NOTE : this will also take care of the teleport taxation.
        //
        final double theVectorSum = VectorHelper.vectorSumOfElements(thePageRankEstimate);
        if(theVectorSum < 1.0)
        {
            final double theLeakage = (1 - theVectorSum) / n;
            for (int i = 0; i < n; i += 1)
            {
                thePageRankEstimate[i] += theLeakage;
            }
        }

        //
        // if there has been leakage, then scale the vector so that
        // it's elements add to one.
        //
//        final double theVectorSum = VectorHelper.vectorSumOfElements(thePageRankEstimate);
//        if(theVectorSum < 1.0)
//        {
//            final double theLeakage = (1 / theVectorSum);
//            System.out.println("theLeakage = $l".replace("$l", String.valueOf(theLeakage)));
//            for (int j = 0; j < n; j += 1)
//            {
//                thePageRankEstimate[j] /= theVectorSum; // scale to make everything add to one.
//            }
//        }

    }

    /**
     * Initialize the page rank vector with equal importance for each page;
     *
     * @param thePageRankVector
     */
    protected static void initializePageRankVector(final double[] thePageRankVector)
    {
        final int theDimension = thePageRankVector.length;
        final double theProbability = 1.0 / theDimension;
        for(int i = 0; i < theDimension; i += 1)
        {
            thePageRankVector[i] = theProbability;
        }
    }

    /**
     * Use the sparse link map to initialize the matrix.
     *
     * @param theMatrix
     * @param thePageLinks
     * @param theDimension
     * @param beta
     */
    protected static void initializeMatrixFromPageLinks(final double[][] theMatrix, final PageLinks thePageLinks, final int theDimension, final double beta)
    {
        final double theTeleportProbability = 1.0 / theDimension;   // needed for pages with no out-links

        for(Integer theFromPage = 0; theFromPage < theDimension; theFromPage += 1)
        {
            if((theFromPage < 0) || (theFromPage >= theDimension)) throw new IllegalStateException("thePageLinks contain a from-page index that is out of bounds.");

            final List<Integer> theToPages = thePageLinks.get(theFromPage);
            if((null != theToPages)  && !theToPages.isEmpty())
            {
                //
                // NOTE: we apply beta to the matrix element here, so we should NOT apply it
                //       during the power iteration!
                //
                final double theLinkProbability = (1.0 / theToPages.size()) * beta;
                for(Integer theToPage : theToPages)
                {
                    if((theToPage < 0) || (theToPage >= theDimension)) throw new IllegalStateException("thePageLinks contain a to-page index that is out of bounds.");
                    theMatrix[theToPage][theFromPage] = theLinkProbability;
                }
            }
            else
            {
                //
                // there are no outlinks from this page, so it is a dead end.
                // we therefore distribute it's importance evenly to all pages
                //
                for(Integer theToPage = 0; theToPage < theDimension; theToPage += 1)
                {
                    theMatrix[theToPage][theFromPage] = theTeleportProbability;
                }
            }
        }
    }

    public static void initializeSparseMatrixFromPageLinks(final SparseDoubleMatrix theMatrix, final IntegerPageLinks thePageLinks, final int theDimension, final double beta)
    {
        final double theTeleportProbability = 1.0 / theDimension;   // needed for pages with no out-links

        final IntegerIterator theIterator = thePageLinks.indices();

        //for(int theFromPage = 0; theFromPage < theDimension; theFromPage += 1)
        while(theIterator.hasNext())
        {
            final int theFromPage = theIterator.next();
            if((theFromPage < 0) || (theFromPage >= theDimension)) throw new IllegalStateException("thePageLinks contain a from-page index that is out of bounds.");

            final IntegerArray theToPages = thePageLinks.get(theFromPage);
            if((null != theToPages)  && !theToPages.isEmpty())
            {
                //
                // NOTE: we apply beta to the matrix element here, so we should NOT apply it
                //       during the power iteration!
                //
                final double theLinkProbability = (1.0 / theToPages.size()) * beta;
                for(int i = 0; i < theToPages.size(); i += 1)
                {
                    final int theToPage = theToPages.get(i);
                    if((theToPage < 0) || (theToPage >= theDimension))
                    {
                        throw new IllegalStateException("thePageLinks contain a to-page index that is out of bounds.");
                    }
                    theMatrix.set(theToPage, theFromPage,  theLinkProbability);
                }
            }
            else
            {
                //
                // there are no outlinks from this page, so it is a dead end.
                // we therefore distribute it's importance evenly to all pages
                //
                // TODO: we should not have to actually set this information if we can instead
                //       detect during the page rank calculation that the theFromPage has no outlinks,
                //       then we can 'synthesize' what is essentially a constant value, 1.0/theDimension.
                //       rather than have to look it up.
                //       That will help keep our matrix very sparse.
                //
//                for(int i = 0; i < theDimension; i += 1)
//                {
//                    theMatrix.set(i, theFromPage, theTeleportProbability);
//                }

                //
                // we will let this stay zero, then we will fix the page-rank leakage at the
                // end of each iteration by adding (1 - ||r||) / n to each element in r.
                //
            }
        }
    }

    /**
     * Calculate the sum of the absolute value of the element differences for
     * two vectors of equal dimension.
     *
     * @param v1 a vector with 1 or more elements
     * @param v2 a vector with the same number of elements as v1
     * @return the sum of the absolute value of the element differences.
     */
    protected static double calculateSumOfDifferences(final double[] v1, final double[] v2)
    {
        assert(v1.length > 0);
        assert(v1.length == v2.length);

        double theDifference = 0.0;
        final int n = v1.length;
        for(int i = 0; i < n; i += 1)
        {
            theDifference += Math.abs(v1[i] - v2[i]);
        }

        return theDifference;
    }

    /**
     * Print the matrix to system out.
     *
     * @param m
     */
    private static void printMatrix(final double[][] m)
    {
        final StringBuilder theBuilder = new StringBuilder();

        final int n = m[0].length;

        for(int i = 0; i < n; i += 1)
        {
            theBuilder.append("|");
            theBuilder.append(String.valueOf(m[i][0]));
            for (int j = 1; j < n; j += 1)
            {
                theBuilder.append(", ");
                theBuilder.append(String.valueOf(m[i][j]));
            }
            theBuilder.append("|\n");
        }

        System.out.println(theBuilder.toString());
    }

    private static void printSparseMatrix(final SparseDoubleMatrix m)
    {
        final StringBuilder theBuilder = new StringBuilder();

        for(int i = 0; i < m.rowCount; i += 1)
        {
            theBuilder.append("|");
            theBuilder.append(String.valueOf(m.get(i, 0, 0.0)));
            for (int j = 1; j < m.columnCount; j += 1)
            {
                theBuilder.append(", ");
                theBuilder.append(String.valueOf(m.get(i, j, 0.0)));
            }
            theBuilder.append("|\n");
        }

        System.out.println(theBuilder.toString());
    }

    /**
     * Print the given page rank vector to system out.
     *
     * @param v
     */
    private static void printPageRankVector(final double[] v, final String theOutFilePath)
    {
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter(theOutFilePath, "UTF-8");

            for (int i = 0; i < v.length; i += 1)
            {
                writer.println(
                        "v[$i] = $probability"
                                .replace("$i", String.valueOf(i))
                                .replace("$probability", String.valueOf(v[i])));
            }
            writer.println();
            writer.flush();
        }
        catch (Exception e)
        {
            System.out.println("Failure outputing the page rank vector.");
            if(null != writer)
            {
                writer.close();
            }
        }

    }

    /**
     * Print the given page rank vector to system out.
     *
     * @param v
     */
    private static void dumpPageRankVector(final double[] v)
    {
        try
        {
            double theSum = 0.0;
            for (int i = 0; i < v.length; i += 1)
            {
                theSum += v[i];
                System.out.println(
                        "v[$i] = $probability"
                                .replace("$i", String.valueOf(i))
                                .replace("$probability", String.valueOf(v[i])));
            }
            System.out.println("sum == $sum".replace("$sum", String.valueOf(theSum)));
        }
        catch (Exception e)
        {
            System.out.println("Failure outputing the page rank vector.");
        }

    }

    public static double[] calculateBiasedPageRank(final PageLinks thePageLinks, final int thePageCount, final double[] theTeleportBiases, final double epsilon, final int theMaxIterations)
    {
        assert(thePageCount > 0);
        assert((null != theTeleportBiases) && (theTeleportBiases.length == thePageCount));
        assert((epsilon >= 0.0));

        //
        // calculate beta from bias
        // 1.0 == beta + sum(biases)
        //
        double beta = 1.0;
        for(double theBias : theTeleportBiases)
        {
            beta -= theBias;
        }

        if(beta < 0.0) throw new IllegalStateException("thePageBiases are greater than one.");

        final Date theStartTime = new Date();

        // initialize storage
        final int dimension = thePageCount;
        final double[][] theMatrix = new double[dimension][dimension];  // [fromPage][toPage]
        double[] thePageRankVector = new double[dimension];
        double[] thePageRankEstimate = new double[dimension];

        initializeBiasedMatrixFromPageLinks(theMatrix, thePageLinks, dimension, theTeleportBiases);
        printMatrix(theMatrix);

        // initialize the page rank vector to our first estimate; assume equal importance to all pages
        // (distribute importance evenly among pages)
        initializePageRankVector(thePageRankVector);

        //
        // do a power iteration;
        // 1. calculate a page rank vector estimate
        // 2. calculate the sum of the differences between the page rank vector and new page rank estimate vector
        // 3. If the difference is greater than epsilon, make the new page rank estimate the page rank, repeat starting at 1..
        // 4. we have converged on the page rank vector and we are done.
        //
        int theIterations = 0;
        do
        {
            System.out.println(
                    "Iteration $i at $t."
                            .replace("$i", String.valueOf(theIterations))
                            .replace("$t", (new Date()).toString()));
            dumpPageRankVector(thePageRankVector);

            // 1. calculate a page rank vector estimate
            calculateBiasedPageRankVector(theMatrix, thePageRankVector, thePageRankEstimate, theTeleportBiases);

            //
            // the estimate is the new page rank vector,
            // we use the old page rank vector as space for a new estimate.
            //
            final double[] temp = thePageRankVector;
            thePageRankVector = thePageRankEstimate;
            thePageRankEstimate = temp;

            // 2. calculate the sum of the differences between the page rank vector and new page rank estimate vector
            // 3. If the difference is greather than epsilon, make the new page rank estimate the page rank, repeat starting at 1..
        }  while((++theIterations < theMaxIterations) && ((theIterations <= 20) || (calculateSumOfDifferences(thePageRankVector, thePageRankEstimate) > epsilon)));

        final Date theEndTime = new Date();

        System.out.println(
                "Complete at $t."
                        .replace("$t", theEndTime.toString()));

        dumpPageRankVector(thePageRankVector);

        // 4. we have converged on the page rank vector and we are done.
        return thePageRankVector;

    }

    /**
     * Calcualte a new estimate for the page rank vector.
     *
     * @param theMatrix the link matrix[theFromPage][theToPage]
     * @param thePageRankVector the current page rank vector
     * @param thePageRankEstimate on completion, a new estimate for the page rank vector
     * @param theTeleportBiases the probability (0 <= beta <= 1) that a random walker will take a link (rather than teleport)
     */
    protected static void calculateBiasedPageRankVector(final double[][] theMatrix, final double[] thePageRankVector, final double[] thePageRankEstimate, final double[] theTeleportBiases)
    {

        //
        // v' = B*M*v + es
        //
        // where
        // v' = the new estimate for the page rank vector
        // B = beta; the probability that a random walker will choose and out-link (rather than teleport)
        // M = the link matrix
        // v = the page rank vector
        // es = the teleport bias vector; the teleport probability for each node; whose sum of elements == (1 - beta)
        // n = the dimension (the number of pages)
        //
        // NOTE: we have already pre-applied Beta to each matrix element at initialization, so we don't apply it below.
        //

        final int n = thePageRankVector.length;
        for(int i = 0; i < n; i += 1)   // choose the row in the matrix (m) and the estimate vector (v')
        {
            //
            // start with the teleport bias and add page rank
            //
            thePageRankEstimate[i] = theTeleportBiases[i];
            for(int j = 0; j < n; j += 1)   // choose the column in the matrix (m) and row in the page rank vector (v)
            {
                //
                // NOTE: beta is already applied to the elements of theMatrix, when it is initialized
                //
                thePageRankEstimate[i] += theMatrix[i][j] * thePageRankVector[j];
            }
        }

        //
        // calculate the leakage using the sum of the elements of thePageRankVectorEstimate;
        // - it may be less than 1 because of dead-ends that leaked page rank.
        // - Fix this so that the magnitude is one by adding
        //   (1 - S(r)) / n to each element of r, where S(r) is the sum
        //   of the elements of r.
        //
        // NOTE : this will also take care of the teleport taxation.
        //
//        final double theVectorSum = VectorHelper.vectorSumOfElements(thePageRankEstimate);
//        if(theVectorSum < 1.0)
//        {
//            final double theLeakage = (1.0 - theVectorSum) / n;
//            System.out.println("theLeakage = $l".replace("$l", String.valueOf(theLeakage)));
//            for (int j = 0; j < n; j += 1)
//            {
//                thePageRankEstimate[j] += theLeakage;
//            }
//        }

        //
        // if there has been leakage, then scale the vector so that
        // it's elements add to one.
        //
        final double theVectorSum = VectorHelper.vectorSumOfElements(thePageRankEstimate);
        //if(theVectorSum < 1.0)
        {
            final double theLeakage = (1 / theVectorSum);
            System.out.println("theLeakage = $l".replace("$l", String.valueOf(theLeakage)));
            for (int j = 0; j < n; j += 1)
            {
                thePageRankEstimate[j] /= theVectorSum; // scale to make everything add to one.
            }
        }


    }

    protected static void initializeBiasedMatrixFromPageLinks(final double[][] theMatrix, final PageLinks thePageLinks, final int theDimension, double[] theTeleportBiases)
    {
        //
        // calculate beta from bias
        // 1.0 == beta + sum(biases)
        //
        double beta = 1.0;
        for (double theBias : theTeleportBiases)
        {
            beta -= theBias;
        }
        if (beta < 0.0) throw new IllegalStateException("thePageBiases are greater than one.");

        for (Integer theFromPage = 0; theFromPage < theDimension; theFromPage += 1)
        {
            if ((theFromPage < 0) || (theFromPage >= theDimension))
                throw new IllegalStateException("thePageLinks contain a from-page index that is out of bounds.");

            final List<Integer> theToPages = thePageLinks.get(theFromPage);
            if ((null != theToPages) && !theToPages.isEmpty())
            {
                //
                // NOTE: we apply beta to the matrix element here, so we should NOT apply it
                //       during the power iteration!
                //
                final double theLinkProbability = (1.0 / theToPages.size()) * beta;
                for (Integer theToPage : theToPages)
                {
                    if ((theToPage < 0) || (theToPage >= theDimension))
                        throw new IllegalStateException("thePageLinks contain a to-page index that is out of bounds.");
                    theMatrix[theToPage][theFromPage] = theLinkProbability;
                }
            }
            else
            {
                //
                // there are no outlinks from this page, so it is a dead end.
                // we therefore distribute it's importance evenly to all pages
                //
                for (Integer theToPage = 0; theToPage < theDimension; theToPage += 1)
                {
                    //
                    // distribute the link probability based on
                    // the percentage of teleport bias
                    //
                    // theMatrix[theToPage][theFromPage] = (theTeleportBiases[theToPage] / (1 - beta));
                    theMatrix[theToPage][theFromPage] = theTeleportBiases[theToPage];
                    //theMatrix[i][j] = 1.0 / theDimension;
                }
            }
        }
    }
}
