package evotrace.exp.itrust.preprocess;

import evotrace.preprocess.BatchingPreprocess;
import evotrace.preprocess.SourceTargetUnion;
import evotrace.util.AppConfigure;

/**
 * Created by niejia on 15/10/19.
 */
public class iTrustOldVersionDataPreprocess {

    public static void main(String[] args) {
        SourceTargetUnion union = new SourceTargetUnion(AppConfigure.iTrust_oldVersionRequirementPath,
                AppConfigure.iTrust_oldVersionCodePath, AppConfigure.iTrust_oldVersionRTMInDB, AppConfigure.iTrust_oldVersionCleanedRequirementPath,
                AppConfigure.iTrust_oldVersionCleanedCodePath, "rtm_v10");

        BatchingPreprocess preprocess = new BatchingPreprocess(AppConfigure.iTrust_oldVersionCleanedRequirementPath,
                AppConfigure.iTrust_oldVersionCleanedCodePath);
        preprocess.doProcess();
    }
}
