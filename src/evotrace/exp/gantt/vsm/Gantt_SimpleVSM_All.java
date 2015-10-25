package evotrace.exp.gantt.vsm;

import evotrace.core.algo.None_CSTI;
import evotrace.core.dataset.TextDataset;
import evotrace.core.ir.IR;
import evotrace.core.ir.IRModelConst;
import evotrace.core.metrics.Result;
import evotrace.util.AppConfigure;
import evotrace.visual.VisualCurve;

/**
 * Created by niejia on 15/10/25.
 */
public class Gantt_SimpleVSM_All {

    public static void main(String[] args) {

        TextDataset textDataset = new TextDataset(AppConfigure.Gantt_CleanedRequirementPath,
                AppConfigure.Gantt_CleanedCodePath, AppConfigure.Gantt_RTM);

        TextDataset textDatasetAll = new TextDataset(AppConfigure.Gantt_CleanedRequirementPath,
                AppConfigure.Gantt_CleanedCodeAllPath, AppConfigure.Gantt_RTM);

        TextDataset textDatasetPlus = new TextDataset(AppConfigure.Gantt_CleanedRequirementPath,
                AppConfigure.Gantt_CleanedCodePlusStrContentPath, AppConfigure.Gantt_RTM);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI());
        Result result_ir_plus = IR.compute(textDatasetPlus, IRModelConst.VSM, new None_CSTI());
        Result result_ir_all = IR.compute(textDatasetAll, IRModelConst.VSM, new None_CSTI());

        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();
        result_ir_all.showAveragePrecisionByRanklist();
        result_ir_all.showMeanAveragePrecisionByQuery();
        result_ir_plus.showAveragePrecisionByRanklist();
        result_ir_plus.showMeanAveragePrecisionByQuery();


        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.addLine(result_ir_all);
        curve.addLine(result_ir_plus);
        curve.showChart();
    }
}
