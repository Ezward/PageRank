package com.lumpofcode.pagerank;

import java.util.List;

/**
 * Created by emurphy on 2/8/15.
 */
public final class PageRankCalculator
{
    public static double[] calculatePageRank(final PageLinks thePageLinks, final int thePageCount, final double beta, final double epsilon, final int theMaxIterations)
    {
        assert(thePageCount > 0);
        assert((beta >= 0.0) && (beta <= 1.0));
        assert((epsilon >= 0.0));

        // initialize storage
        final int dimension = thePageCount;
        final double[][] theMatrix = new double[dimension][dimension];  // [fromPage][toPage]
        double[] thePageRankVector = new double[dimension];
        double[] thePageRankEstimate = new double[dimension];

        initializeMatrixFromPageLinks(theMatrix, thePageLinks, dimension, beta);
        printMatrix(theMatrix);

        // initialize the page rank vector (distribute importance evenly among pages
        initializePageRankVector(thePageRankVector);

        //
        // do a power iteration;
        // 1. calculate a page rank vector estimate
        // 2. calculate the sum of the differences between the page rank vector and new page rank estimate vector
        // 3. If the difference is greather than epsilon, make the new page rank estimate the page rank, repeat starting at 1..
        // 4. we have converged on the page rank vector and we are done.
        //
        int theIterations = 0;
        do
        {
            System.out.println("Iteration " + String.valueOf(theIterations));
            printPageRankVector(thePageRankVector);

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
        }  while((++theIterations < theMaxIterations) && (calculateSumOfDifferences(thePageRankVector, thePageRankEstimate) > epsilon));

        System.out.println("Iteration " + String.valueOf(theIterations));
        printPageRankVector(thePageRankVector);

        // 4. we have converged on the page rank vector and we are done.
        return thePageRankVector;

    }

    /**
     * Calcualte a new estimate for the page rank vector.
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
        final int n = thePageRankVector.length;
        final double theTaxation = (1.0 - beta)/n;
        for(int j = 0; j < n; j += 1)
        {
            //
            // multiply each row by beta and the the page rank vector, then add taxation term
            //
            thePageRankEstimate[j] = theTaxation;
            for(int i = 0; i < n; i += 1)
            {
                //
                // NOTE: it would be a little more efficient to get and hold the column vector
                //       but I've chosen to use a two dimensional reference to the matrix for clarity.
                //
                //
                // NOTE: beta is already applied to the elements of theMatrix, when it is initialized
                //
                thePageRankEstimate[j] += theMatrix[i][j] * thePageRankVector[i];
            }
        }
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
                    theMatrix[theFromPage][theToPage] = theLinkProbability;
                }
            }
            else
            {
                //
                // there are no outlinks from this page, so it is a dead end.
                // we therefore distribute it's importance evenly to all pages
                //
                for(Integer i = 0; i < theDimension; i += 1)
                {
                    theMatrix[theFromPage][i] = theTeleportProbability;
                }
            }
        }
    }

    /**
     * Calcualte the sum of the absolute value of the element differences for
     * two vectors of equal dimension.
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

    public static void printMatrix(final double[][] m)
    {
        final StringBuilder theBuilder = new StringBuilder();

        final int n = m[0].length;

        for(int j = 0; j < n; j += 1)
        {
            theBuilder.append("|");
            theBuilder.append(String.valueOf(m[0][j]));
            for (int i = 1; i < n; i += 1)
            {
                theBuilder.append(", ");
                theBuilder.append(String.valueOf(m[i][j]));
            }
            theBuilder.append("|\n");
        }

        System.out.println(theBuilder.toString());
    }

    public static void printPageRankVector(final double[] v)
    {
        for(int i = 0; i < v.length; i += 1)
        {
            System.out.println(
                    "v[$i] = $probability"
                            .replace("$i", String.valueOf(i))
                            .replace("$probability", String.valueOf(v[i])));
        }
        System.out.println();

    }

}
