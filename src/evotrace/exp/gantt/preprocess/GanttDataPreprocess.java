package evotrace.exp.gantt.preprocess;

import evotrace.preprocess.BatchingPreprocess;
import evotrace.preprocess.SourceTargetUnion;
import evotrace.util.AppConfigure;

/**
 * Created by niejia on 15/10/24.
 */
public class GanttDataPreprocess {

    public static void main(String[] args) {
        SourceTargetUnion union = new SourceTargetUnion(AppConfigure.Gantt_RequirementPath,
                AppConfigure.Gantt_CodePath, AppConfigure.Gantt_RTMInDB, AppConfigure.Gantt_CleanedRequirementPath,
                AppConfigure.Gantt_CleanedCodePath, "rtm");

        BatchingPreprocess preprocess = new BatchingPreprocess(AppConfigure.Gantt_CleanedRequirementPath,
                AppConfigure.Gantt_CleanedCodePath);
        preprocess.doProcess();
    }
}
