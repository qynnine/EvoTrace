package evotrace.diff;

import evotrace.util.AppConfigure;
import org.junit.Test;

public class DiffParserTest {

    @org.junit.Test
    public void testFetchJavaFileNames() throws Exception {
        DiffParser diffParser = new DiffParser(AppConfigure.iTrust_codeDiffFile, "v11", "10", "java");
        diffParser.fetchFileName("Only in v11/java/: AddOfficeVisitAction.java");
        diffParser.fetchFileName("diff -bur v10/java/AddUAPAction.java v11/java/AddUAPAction.java");
    }

    @Test
    public void testDirectoryComparerForRequirement() throws Exception {
        DiffParser requirementDiffParser = new DiffParser(AppConfigure.iTrust_requirementDiffFile, "v11", "v10", "txt");
        System.out.println(requirementDiffParser);
    }

    @Test
    public void testDirectoryComparerForCode() {
        DiffParser codeDiffParser = new DiffParser(AppConfigure.iTrust_codeDiffFile, "v11", "v10", "java");
        System.out.println(codeDiffParser);
    }
}
