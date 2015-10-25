package evotrace.exp.itrust.preprocess;

import evotrace.diff.ComparerType;
import evotrace.diff.VersionComparer;
import evotrace.util.AppConfigure;

/**
 * Created by niejia on 15/10/20.
 */
public class iTrustReqVersionCompare {

    public static void main(String[] args) {
        VersionComparer requirementVersionComparer = new VersionComparer(AppConfigure.iTrust_requirementDiffFile,
                "v11", "v10", AppConfigure.iTrust_newVersionRequirementPath, AppConfigure.iTrust_oldVersionRequirementPath,"txt", ComparerType.TextComparer);

        System.out.println(" requirementVersionComparer = " + requirementVersionComparer );
    }
}
