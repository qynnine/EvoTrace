package evotrace.io;

import evotrace.document.SimilarityMatrix;
import evotrace.util.AppConfigure;
import org.junit.Test;

public class RTMReaderTest {

    @Test
    public void testCreateSimilarityMatrix() throws Exception {
        SimilarityMatrix rtm = RTMReader.createSimilarityMatrix(AppConfigure.iTrust_newVersionRTM);
        System.out.println(" rtm = " + rtm);
        System.out.println(rtm.getScoreForLink("UC38", "DrugInteractionDAO"));
    }
}