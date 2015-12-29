package com.lumpofcode.pagerank;

public class Main {

    public static void main(String[] args)
    {
        //
        // initialize the page links
        //

        // all nodes link to nodes other than themselves
//        thePageLinks
//                .addLink(0, 1)  // A -> B
//                .addLink(0, 2)  // A -> B
//                .addLink(1, 0)  // A -> C
//                .addLink(1, 2)  // A -> C
//                .addLink(2, 0)  // B -> C
//                .addLink(2, 1); // C -> C
//        final double beta = 0.85;

        // week 1, problem 1
//        thePageLinks
//                .addLink(0, 1)  // A -> B
//                .addLink(0, 2)  // A -> C
//                .addLink(1, 2)  // B -> C
//                .addLink(2, 2); // C -> C
//        final double beta = 0.85;

        // week 1, problem 2
//        thePageLinks
//                .addLink(0, 1)  // A -> B
//                .addLink(0, 2)  // A -> C
//                .addLink(1, 2)  // B -> C
//                .addLink(2, 0); // C -> A
//        final double beta = 0.85;

        // week 1, problem 3
        // NOTE: hack initial eigen vector to all ones!!!
//        thePageLinks
//                .addLink(0, 1)  // A -> B
//                .addLink(0, 2)  // A -> C
//                .addLink(1, 2)  // B -> C
//                .addLink(2, 0); // C -> A
//        final double beta = 1.0;    // no taxation

//        final PageLinks thePageLinks = new PageLinks();
//        thePageLinks.addLink(0, 1).addLink(1, 2).addLink(2,3);
//        final double[] thePageRankVector = PageRankCalculator.calculatePageRank(thePageLinks, thePageLinks.getMax() + 1, 1, 0.000000000001, 100);

        //
        // Week7B1
        //
//        final PageLinks thePageLinks = new PageLinks();
//        thePageLinks.addLink(0, 1);
//        thePageLinks.addLink(0, 2);
//        thePageLinks.addLink(1, 0);
//        thePageLinks.addLink(2, 3);
//        thePageLinks.addLink(3, 2);
//
//        final double theTeleportBias[] = {0.2, 0.1, 0.0, 0.0};
//
//        PageRankCalculator.calculateBiasedPageRank(thePageLinks, theTeleportBias.length, theTeleportBias, 0.0000001, 100);


        // Final
//        0 -> 0, 1, 2
//        1 -> 2


//        final PageLinks thePageLinks = new PageLinks();
//        thePageLinks.addLink(0, 0);
//        thePageLinks.addLink(0, 1);
//        thePageLinks.addLink(0, 2);
//        thePageLinks.addLink(1, 2);
//
//        final double theTeleportBias[] = {0.1, 0.1, 0.0};
//
//        PageRankCalculator.calculateBiasedPageRank(thePageLinks, theTeleportBias.length, theTeleportBias, 0.0000001, 100);

        final PageLinks thePageLinks = new PageLinks();
        thePageLinks.addLink(0, 1);
        thePageLinks.addLink(1, 2);
        thePageLinks.addLink(2, 3);

        final double theTeleportBias[] = {0.0, 0.0, 0.0, 1.0};

        PageRankCalculator.calculateBiasedPageRank(thePageLinks, theTeleportBias.length, theTeleportBias, 0.0000001, 100);

//        SparseDoubleMatrix theMatrix = null;
//        final double beta = 0.8;
//
//        //
//        // we put this part in a try block partially so that when we are done intializing the matrix
//        // from the page links, the page links can be reclaimed by garbage collection.
//        //
//        try
//        {
//            final IntegerPageLinks thePageLinks = new IntegerPageLinks();
//            PageLinkHelper.readPageLinks(thePageLinks, "web-Google-sorted.txt", 0, '\t', null);
//
//            theMatrix = new SparseDoubleMatrix(thePageLinks.getMax() + 1, 1024, 12);  // [fromPage][toPage]
//
//
//            //
//            // show the page links
//            //
//            // PageLinkHelper.dumpPageLinks(thePageLinks);
//            // PageLinkHelper.dumpPageLinks(thePageLinks);
//
//            PageRankCalculator.initializeSparseMatrixFromPageLinks(theMatrix, thePageLinks, thePageLinks.getMax() + 1, beta);
//        }
//        catch(Exception e)
//        {
//            System.out.print(e.toString());
//        }
//
//
//        //
//        // NOTE: we cannot simply use the size of the page links map as our dimension,
//        //       since that only include pages with out links.  Our link map may include
//        //       dead end pages that do not have out links.
//        //
//
////        final double[] thePageRankVector = PageRankCalculator.calculatePageRank(thePageLinks, thePageLinks.getMax() + 1, beta, 0.000001, 100);
//        final double[] thePageRankVector = PageRankCalculator.calculateSparsePageRank(theMatrix, theMatrix.rowCount, beta, 0.0000001, 100);
    }

}
