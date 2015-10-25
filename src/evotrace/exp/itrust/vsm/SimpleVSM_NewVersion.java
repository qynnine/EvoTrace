package evotrace.exp.itrust.vsm;

import evotrace.core.algo.None_CSTI;
import evotrace.core.dataset.TextDataset;
import evotrace.core.ir.IR;
import evotrace.core.ir.IRModelConst;
import evotrace.core.metrics.Result;
import evotrace.util.AppConfigure;
import evotrace.visual.VisualCurve;

/**
 * Created by niejia on 15/10/19.
 */
public class SimpleVSM_NewVersion {

    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.iTrust_newVersionCleanedRequirementPath,
                AppConfigure.iTrust_newVersionCleanedCodePath, AppConfigure.iTrust_newVersionRTM);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI());
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.showChart();
    }
}
