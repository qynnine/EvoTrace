package evotrace.exp.itrust.js;

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
public class SimpleJS_OldVersionPlusStrContent {


    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.iTrust_oldVersionCleanedRequirementPath,
                AppConfigure.iTrust_oldVersionCleanedCodePath, AppConfigure.iTrust_oldVersionRTM);

        TextDataset textDatasetPlus = new TextDataset(AppConfigure.iTrust_oldVersionCleanedRequirementPath,
                AppConfigure.iTrust_oldVersionCleanedCodePlusStrContentPath, AppConfigure.iTrust_oldVersionRTM);

        Result result_ir = IR.compute(textDataset, IRModelConst.JSD, new None_CSTI());
        Result result_ir_plus = IR.compute(textDatasetPlus, IRModelConst.JSD, new None_CSTI());

        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();
        result_ir_plus.showAveragePrecisionByRanklist();
        result_ir_plus.showMeanAveragePrecisionByQuery();


        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.addLine(result_ir_plus);
        curve.showChart();
    }
}
