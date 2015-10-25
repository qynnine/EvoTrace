package evotrace.core.dataset;

import evotrace.document.ArtifactsCollection;
import evotrace.document.SimilarityMatrix;
import evotrace.io.ArtifactsReader;
import evotrace.io.RTMReader;

/**
 * Created by niejia on 15/2/23.
 */
public class TextDataset {

    private ArtifactsCollection sourceCollection;
    private ArtifactsCollection targetCollection;
    private SimilarityMatrix rtm;

    public TextDataset(String sourceDirPath, String targetDirPath, String rtmPath) {
        this.setSourceCollection(ArtifactsReader.getCollections(sourceDirPath, ".txt"));
        this.setTargetCollection(ArtifactsReader.getCollections(targetDirPath, ".java"));
        this.setRtm(RTMReader.createSimilarityMatrix(rtmPath));
    }

    public ArtifactsCollection getSourceCollection() {
        return sourceCollection;
    }

    public void setSourceCollection(ArtifactsCollection sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public ArtifactsCollection getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(ArtifactsCollection targetCollection) {
        this.targetCollection = targetCollection;
    }

    public SimilarityMatrix getRtm() {
        return rtm;
    }

    public void setRtm(SimilarityMatrix rtm) {
        this.rtm = rtm;
    }
}
