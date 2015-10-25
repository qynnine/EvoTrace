package evotrace.exp.itrust.preprocess;

import evotrace.diff.ComparerType;
import evotrace.diff.VersionComparer;
import evotrace.util.AppConfigure;

/**
 * Created by niejia on 15/10/20.
 */
public class iTrustCodeVersionCompare {

    public static void main(String[] args) {
        VersionComparer codeVersionComparer = new VersionComparer(AppConfigure.iTrust_codeDiffFile, "v11", "10"
                , AppConfigure.iTrust_newVersionCodePath, AppConfigure.iTrust_oldVersionCodePath, "java", ComparerType.TextComparer);


        System.out.println(codeVersionComparer.getUnchangedArtifactNames());
    }
}
