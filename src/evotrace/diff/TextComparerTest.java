package evotrace.diff;

import evotrace.preprocess.ArtifactPreprocessor;
import evotrace.util._;
import org.junit.Test;

public class TextComparerTest {

    private final String oldVersion = "data/iTrust/code/v10/ApptBean.java";
    private final String newVersion = "data/iTrust/code/v11/ApptBean.java";
    @Test
    public void testAddedTokenList() throws Exception {
        String oldText = _.readFile(oldVersion);
        String newText = _.readFile(newVersion);
        TextComparer textComparer = new TextComparer(ArtifactPreprocessor.handlePureTextFile(oldText),
                ArtifactPreprocessor.handlePureTextFile(newText));
//        System.out.println(textComparer.addedTokenList());
    }


    @Test
    public void testRemovedTokenList() throws Exception {
        String oldText = _.readFile(oldVersion);
        String newText = _.readFile(newVersion);
        TextComparer textComparer = new TextComparer(ArtifactPreprocessor.handlePureTextFile(oldText),
                ArtifactPreprocessor.handlePureTextFile(newText));
//        System.out.println(textComparer.removedTokenList());
    }


}