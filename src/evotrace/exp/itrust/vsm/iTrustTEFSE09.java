package evotrace.exp.itrust.vsm;

import evotrace.core.algo.TrivialBalanced;
import evotrace.core.algo.TrivialOptimistic;
import evotrace.core.algo.TrivialPessimistic;
import evotrace.core.algo.Weighted;
import evotrace.core.dataset.TextDataset;
import evotrace.core.ir.IR;
import evotrace.core.ir.IRModelConst;
import evotrace.core.metrics.Result;
import evotrace.util.AppConfigure;
import evotrace.visual.VisualCurve;

/**
 * Created by niejia on 15/10/22.
 */
public class iTrustTEFSE09 {

    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.iTrust_newVersionCleanedRequirementPath,
                AppConfigure.iTrust_newVersionCleanedCodePath, AppConfigure.iTrust_newVersionRTM);

        Result result_ir_tp = IR.compute(textDataset, IRModelConst.VSM, new TrivialPessimistic());
        Result result_ir_to = IR.compute(textDataset, IRModelConst.VSM, new TrivialOptimistic());
        Result result_ir_tb = IR.compute(textDataset, IRModelConst.VSM, new TrivialBalanced());
        Result result_ir_w = IR.compute(textDataset, IRModelConst.VSM, new Weighted());


        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir_tp);
        curve.addLine(result_ir_to);
        curve.addLine(result_ir_tb);
        curve.addLine(result_ir_w);
        curve.showChart();
    }
}
