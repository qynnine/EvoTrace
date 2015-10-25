package evotrace.exp.gantt.vsm;

import evotrace.core.algo.None_CSTI;
import evotrace.core.dataset.TextDataset;
import evotrace.core.ir.IR;
import evotrace.core.ir.IRModelConst;
import evotrace.core.metrics.Result;
import evotrace.util.AppConfigure;
import evotrace.visual.VisualCurve;

/**
 * Created by niejia on 15/10/24.
 */
public class Gantt_SimpleVSM {

    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.Gantt_CleanedRequirementPath,
                AppConfigure.Gantt_CleanedCodePath, AppConfigure.Gantt_RTM);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI());
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.showChart();
    }
}
