package evotrace.exp.itrust.preprocess;

import evotrace.preprocess.BatchingPreprocess;
import evotrace.preprocess.SourceTargetUnion;
import evotrace.util.AppConfigure;

/**
 * Created by niejia on 15/10/19.
 */
public class iTrustNewVersionDataPreprocess {

    public static void main(String[] args) {
        SourceTargetUnion union = new SourceTargetUnion(AppConfigure.iTrust_newVersionRequirementPath,
                AppConfigure.iTrust_newVersionCodePath, AppConfigure.iTrust_newVersionRTMInDB, AppConfigure.iTrust_newVersionCleanedRequirementPath,
                AppConfigure.iTrust_newVersionCleanedCodePath, "rtm_v11");

        BatchingPreprocess preprocess = new BatchingPreprocess(AppConfigure.iTrust_newVersionCleanedRequirementPath,
                AppConfigure.iTrust_newVersionCleanedCodePath);
        preprocess.doProcess();
    }
}
