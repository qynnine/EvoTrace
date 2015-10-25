package evotrace.core.algo;

import evotrace.core.dataset.TextDataset;
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
public class TrivialOptimistic implements CSTI {


    @Override
    public SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset) {

        SimilarityMatrix matrix_to = new SimilarityMatrix();
        SimilarityMatrix rtm_oldversion = RTMReader.createSimilarityMatrix(AppConfigure.iTrust_newVersionRTMInText);

        StringHashSet reqInOldVersion = rtm_oldversion.sourceArtifactsIds();
        StringHashSet codeInOldVersion = rtm_oldversion.targetArtifactsIds();

        int count = 0;
        for (String source : matrix.sourceArtifactsIds()) {
            LinksList linksList = new LinksList();
            Map<String, Double> links = matrix.getLinksForSourceId(source);

            for (String target : links.keySet()) {
                if (reqInOldVersion.contains(source) && codeInOldVersion.contains(target)) {
                    if (rtm_oldversion.isLinkAboveThreshold(source, target)) {
                        count++;
                        linksList.add(new SingleLink(source, target, 1.0));
                    } else {
                        linksList.add(new SingleLink(source, target, 0.0));
                    }
                } else {
                    double score = matrix.getScoreForLink(source, target);
                    linksList.add(new SingleLink(source, target, score));
                }
            }

            Collections.sort(linksList, Collections.reverseOrder());

            for (SingleLink link : linksList) {
                matrix_to.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            }
        }

        System.out.println(" count = " + count );
        return matrix_to;
    }

    @Override
    public String getAlgorithmName() {
        return "Trivial Optimistic";
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
