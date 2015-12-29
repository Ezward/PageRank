package com.lumpofcode.pagerank;

import com.lumpofcode.collection.SparseDoubleMatrix;
import com.lumpofcode.util.StringUtils;

import java.io.*;

/**
 * Created by emurphy on 2/18/15.
 */
public class SparseMatrixHelper
{
    public static void readPageLinks(final SparseDoubleMatrix theMatrix, final File theFile, final int theSkipLines, final char theFieldDelimiter, final Character theTextFieldQuote)
    {
        System.out.println("Start importing file " + theFile.getAbsolutePath());

        try
        {
            int theLineCount = 0;
            final BufferedReader theReader = new BufferedReader(new FileReader(theFile));
            try
            {
                String theLine;
                while (null != (theLine = theReader.readLine()))
                {
                    // skip the beginning lines
                    if(theLineCount >= theSkipLines)
                    {
                        theLine = theLine.trim();
                        if (!theLine.isEmpty())
                        {
                            // parse the line and add it to the links
                            //parsePageLine(theMatrix, theLine, '\t', null);
                        }
                    }
                    theLineCount += 1;
                }

                System.out.println("Successfully imported " + String.valueOf(theLineCount) + " lines from " + theFile.getPath());
            }
            catch (Exception ex)
            {
                System.out.println("Failed to import file " + theFile.getPath() + " after " + String.valueOf(theLineCount) + "lines.");
            }
            finally
            {
                theReader.close();
                System.out.println("Finished importing file " + theFile.getAbsolutePath());
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found: " + theFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            System.out.println("Failure closing file reader.");
        }
    }


    public static void parsePageLine(final PageLinks thePageLinks, final String theLine, final char theFieldDelimiter, final Character theTextFieldQuote)
    {
        //
        // break the delimited line up into fields
        // we are expecting all the fields in the school and courses tables
        //
        if ((null != theLine) && !theLine.isEmpty())
        {
            System.out.println("Importing line:" + theLine);

            //
            // skip comments
            //
            if('#' == theLine.charAt(0))
            {
                return;
            }

            //
            // if the line is not empty, then the number of
            // tabs in the line must match the expeced field count
            //
            final String[] theFields = theLine.split(String.valueOf(theFieldDelimiter));
            if (2 == theFields.length)
            {
                //
                // remove whitespace and quotes
                //
                if (null != theTextFieldQuote)
                {
                    for (int i = 0; i < theFields.length; i += 1)
                    {
                        theFields[i] = StringUtils.trim(theFields[i].trim(), theTextFieldQuote);
                    }
                }
                else
                {
                    // remove whitespace only
                    for (int i = 0; i < theFields.length; i += 1)
                    {
                        theFields[i] = theFields[i].trim();
                    }
                }

                final Integer theFromPage = Integer.valueOf(theFields[0]);
                final Integer theToPage = Integer.valueOf(theFields[1]);

                thePageLinks.addLink(theFromPage, theToPage);

            }
            else
            {
                System.out.println("Skipping line with wrong number of fields (" + String.valueOf(theFields.length) + ") should be 2.");
            }
        }
        else
        {
            System.out.println("Skipping empty line.");
        }
    }


/*
    protected static void initializeSparseMatrixFromPageLinkFile(final HashMatrix<Double> theMatrix, final File theFile, final int theDimension, final double beta)
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

                    if((theToPage < 0) || (theToPage >= theDimension))
                    {
                        throw new IllegalStateException("thePageLinks contain a to-page index that is out of bounds.");
                    }
                    theMatrix.set(theFromPage, theToPage,  theLinkProbability);
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
                    theMatrix.set(theFromPage, i, theTeleportProbability);
                }
            }
        }
    }

*/

}
