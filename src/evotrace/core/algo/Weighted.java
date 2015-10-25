package evotrace.core.algo;

import evotrace.core.dataset.TextDataset;
import evotrace.diff.ComparerType;
import evotrace.diff.TextComparer;
import evotrace.diff.VersionComparer;
import evotrace.document.LinksList;
import evotrace.document.SimilarityMatrix;
import evotrace.document.SingleLink;
import evotrace.document.StringHashSet;
import evotrace.io.RTMReader;
import evotrace.util.AppConfigure;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by niejia on 15/10/22.
 */
public class Weighted implements CSTI {
    @Override
    public SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset) {
        SimilarityMatrix matrix_tw = new SimilarityMatrix();
        SimilarityMatrix rtm_oldversion = RTMReader.createSimilarityMatrix(AppConfigure.iTrust_newVersionRTMInText);

        StringHashSet reqInOldVersion = rtm_oldversion.sourceArtifactsIds();
        StringHashSet codeInOldVersion = rtm_oldversion.targetArtifactsIds();

        VersionComparer codeVersionComparer = new VersionComparer(AppConfigure.iTrust_codeDiffFile, "v11", "10"
                , AppConfigure.iTrust_newVersionCodePath, AppConfigure.iTrust_oldVersionCodePath, "java", ComparerType.TextComparer);
        List<String> unchangedCode = codeVersionComparer.getUnchangedArtifactNames();
        VersionComparer requirementVersionComparer = new VersionComparer(AppConfigure.iTrust_requirementDiffFile,
                "v11", "v10", AppConfigure.iTrust_newVersionRequirementPath, AppConfigure.iTrust_oldVersionRequirementPath,"txt", ComparerType.TextComparer);

        int count = 0;
        for (String source : matrix.sourceArtifactsIds()) {
            LinksList linksList = new LinksList();
            Map<String, Double> links = matrix.getLinksForSourceId(source);

            for (String target : links.keySet()) {
                if (reqInOldVersion.contains(source) && codeInOldVersion.contains(target)) {
                    double score = matrix.getScoreForLink(source, target);

                    TextComparer reqComparer = (TextComparer) requirementVersionComparer.getComparerForArtifact(source);
                    TextComparer codeComparer = (TextComparer) codeVersionComparer.getComparerForArtifact(target);

                    // fb = 1
                    if (rtm_oldversion.isLinkAboveThreshold(source, target)) {

                        int removedTokenCountForReq = reqComparer.removedTokenCount();
                        int removedTokenCountForCode = codeComparer.removedTokenCount();

                        double alpha = 1.0 - 1.0 * (removedTokenCountForReq + removedTokenCountForCode) / (reqComparer.oldVersionLength() + codeComparer.oldVersionLength());
                        double score_enh = alpha * 1.0 + (1.0 - alpha) * score;
                        System.out.println("source: " + source + " target: " + target + " score: " + score + " alpha: " + alpha + " score_enh: " + score_enh);
                        linksList.add(new SingleLink(source, target, score_enh));

                    }
                    // fb = 0;
                    else {
                        int addedTokenCountForReq = reqComparer.addedTokenCount();
                        int addedTokenCountForCode = codeComparer.addedTokenCount();

                        double alpha = 1.0 - 1.0 * (addedTokenCountForReq + addedTokenCountForCode) / (reqComparer.newVersionLength() + codeComparer.newVersionLength());
                        double score_enh = alpha * 0.0 + (1.0 - alpha) * score;
                        System.out.println("source: " + source + " target: " + target + " score: " + score + " alpha: " + alpha + " score_enh: " + score_enh);
                        linksList.add(new SingleLink(source, target, score_enh));
                    }
                } else {
                    double score = matrix.getScoreForLink(source, target);
                    linksList.add(new SingleLink(source, target, score));
                }
            }

            Collections.sort(linksList, Collections.reverseOrder());

            for (SingleLink link : linksList) {
                matrix_tw.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            }
        }

        System.out.println(" count = " + count );
        return matrix_tw;
    }

    @Override
    public String getAlgorithmName() {
        return "Weighted";
    }

    @Override
    public List<Pair<String, String>> getAlgorithmParameters() {
        return null;
    }

    @Override
    public String getDetails() {
        return null;
    }

    @Override
    public List<String> getCorrectImprovedTargetsList() {
        List parameters = new ArrayList();
        Pair<String, String> p = new Pair<>("None", "");
        parameters.add(p);
        return parameters;
    }
}
