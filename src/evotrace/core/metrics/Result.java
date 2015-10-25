package evotrace.core.metrics;

import evotrace.document.LinksList;
import evotrace.document.SimilarityMatrix;
import evotrace.document.SingleLink;
import evotrace.document.StringHashSet;
import evotrace.util._;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * Created by niejia on 15/2/23.
 * Result stores all IR exp information
 */
public abstract class Result implements Serializable {
    public SimilarityMatrix matrix;
    protected SimilarityMatrix oracle;
    protected SimilarityMatrix originMatrix;
    protected String resultName;
    protected double cutParameter;
    private String algorithmName;
    private List<Pair<String, String>> algorithmParameters;

    private String log;
    private List<String> correctImprovedTargetsList;

    protected String model;
    protected String cutStrategy;
    protected Map<Double, LinksDistributed> linksDistributedAtRecall = new LinkedHashMap<>();

    public abstract void setCutParameter(double val);

    public abstract void showMetrics();

    public Result() {

    }

    public Result(SimilarityMatrix matrix, SimilarityMatrix oracle) {
        this.matrix = matrix;
        this.oracle = oracle;
        originMatrix = matrix;

        correctImprovedTargetsList = new ArrayList<>();
    }


    public double getCutParameter() {
        return cutParameter;
    }

    public SimilarityMatrix getOracle() {
        return oracle;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCutStrategy(String cutStrategy) {
        this.cutStrategy = cutStrategy;
    }

    public String getModel() {
        return this.model;
    }

    public String getCutStrategy() {
        return this.cutStrategy;
    }

    public SimilarityMatrix getMatrix() {
        return matrix;
    }


    public String showMatrix() {
        StringBuffer sb = new StringBuffer();
        String[] matrixLines = matrix.toString().split("\n");

        int num = 0;
        for (String line : matrixLines) {
            String source = line.split(" ")[0];
            String target = line.split(" ")[1];
//            System.out.println(" source = " + source );
//            System.out.println(" target = " + target );
            sb.append(line);
            if (oracle.isLinkAboveThreshold(source, target)) {

                sb.append(" ");
                sb.append("[correct]");
                num++;

            }
            sb.append("\n");

        }

        System.out.println(sb.toString());
//        System.out.println(num);
        return sb.toString();

//        System.out.println("matrix " + "at " + resultName + " " + cutParameter + " = \n" + matrix);
    }

    public void showOracle() {

        System.out.println("oracle = \n" + oracle);
    }

    public void showPrecisionByQuery() {
        HashMap<String, Double> sourcePrecisions = getPrecisionByQuery();

        System.out.println("Precision by each query at " + resultName + " " + cutParameter + "\n");
        for (String sourceArtifact : sourcePrecisions.keySet()) {
            System.out.println(sourceArtifact + ": " + sourcePrecisions.get(sourceArtifact));
        }
    }

    public Map<SingleLink, Double> getLinksPrecisionLocation() {
        Map<SingleLink, Double> linksPrecision = new LinkedHashMap<>();

        oracle.setThreshold(0.0);
        int correct = 0;
        int currentLink = 0;
        for (SingleLink link : matrix.getLinksAboveThreshold()) {
            currentLink++;
            if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                correct++;
                linksPrecision.put(new SingleLink(link.getSourceArtifactId(), link.getTargetArtifactId(), 1.0), correct / (double) currentLink);
            }
        }

        return linksPrecision;
    }

    public HashMap<String, Double> getPrecisionByQuery() {
        HashMap<String, Double> sourcePrecisions = new LinkedHashMap<>();
        oracle.setThreshold(0.0);
        for (String sourceArtifact : oracle.sourceArtifactsIds()) {
            LinksList links = matrix.getLinksAboveThresholdForSourceArtifact(sourceArtifact);
            Collections.sort(links, Collections.reverseOrder());
            int correct = 0;
            for (SingleLink link : links) {
                if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                    correct++;
                }
            }
            sourcePrecisions.put(sourceArtifact, correct / (double) links.size());
        }
        return sourcePrecisions;
    }

    public void showRecallByQuery() {
        HashMap<String, Double> sourceRecall = getRecallByQuery();
        System.out.println("Recall by each query at " + resultName + " " + cutParameter + "\n");
        for (String sourceArtifact : sourceRecall.keySet()) {
            System.out.println(sourceArtifact + ": " + sourceRecall.get(sourceArtifact));
        }
    }

    public HashMap<String, Double> getRecallByQuery() {
        HashMap<String, Double> sourceRecall = new LinkedHashMap<>();
        oracle.setThreshold(0.0);

        for (String sourceArtifact : oracle.sourceArtifactsIds()) {
            LinksList links = matrix.getLinksAboveThresholdForSourceArtifact(sourceArtifact);
            int correct = 0;
            for (SingleLink link : links) {
                if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                    correct++;
                }
            }
            sourceRecall.put(sourceArtifact, correct / (double) oracle.getCountOfLinksAboveThresholdForSourceArtifact(sourceArtifact));
        }
        return sourceRecall;
    }

    public HashMap<String, Double> getAveragePrecisionByQuery() {
        HashMap<String, Double> sourceAveragePrecision = new LinkedHashMap<>();

        for (String sourceID : oracle.sourceArtifactsIds()) {
            double sumOfPrecisions = 0.0;
            int currentLink = 0;
            int correctSoFar = 0;
            LinksList links = matrix.getLinksAboveThresholdForSourceArtifact(sourceID);
            Collections.sort(links, Collections.reverseOrder());
            for (SingleLink link : links) {
                currentLink++;
                if (oracle.isLinkAboveThreshold(sourceID, link.getTargetArtifactId())) {
                    correctSoFar++;
                    sumOfPrecisions += correctSoFar / (double) currentLink;
                }
            }
            sourceAveragePrecision.put(sourceID, sumOfPrecisions / oracle.getCountOfLinksAboveThresholdForSourceArtifact(sourceID));
        }
        return sourceAveragePrecision;
    }

    public double getMeanAveragePrecisionByQuery() {
        HashMap<String, Double> sourceAveragePrecision = getAveragePrecisionByQuery();
        double sum = 0.0;
        for (String sourceArtifact : sourceAveragePrecision.keySet()) {
//            System.out.println(" sourceAveragePrecision.get(sourceArtifact) = " + sourceAveragePrecision.get(sourceArtifact) );
            sum += sourceAveragePrecision.get(sourceArtifact);
        }
        return (sum / sourceAveragePrecision.size());
    }

    public void showAveragePrecisionByQuery() {
        HashMap<String, Double> sourceAveragePrecision = getAveragePrecisionByQuery();
        System.out.println("AveragePrecision by each query at " + resultName + " " + cutParameter + "\n");
        for (String sourceArtifact : sourceAveragePrecision.keySet()) {
            System.out.println(sourceArtifact + ": " + sourceAveragePrecision.get(sourceArtifact));
        }
    }

    public void showMeanAveragePrecisionByQuery() {
        double meanAveragePrecision = getMeanAveragePrecisionByQuery();
//        System.out.println("MeanAverage at " + resultName + " " + cutParameter);
        System.out.println(model + " " + cutStrategy + " meanAveragePrecision = " + meanAveragePrecision);
    }

    public void showPrecisionByRanklist() {
        double precision = getPrecisionByRanklist();
        String metric = "Precision";
        System.out.println(metric + " = " + precision + " at " + resultName + " " + cutParameter);
    }

    public void showRecallByRanklist() {
        double recall = getRecallByRanklist();
        String metric = "Recall";
        System.out.println(metric + " = " + recall + " at " + resultName + " " + cutParameter);
    }

    public void showAveragePrecisionByRanklist() {
        double averagePrecision = getAveragePrecisionByRanklist();
        String metric = "AveragePrecision";
        System.out.println(metric + " = " + averagePrecision + " at " + resultName + " " + cutParameter);
    }

    public double getPrecisionByRanklist() {
        oracle.setThreshold(0.0);
        int correct = 0;

        for (SingleLink link : matrix.getLinksAboveThreshold()) {
            if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                correct++;
            }
        }
        double precision = correct / (double) matrix.getLinksAboveThreshold().size();

        return precision;
    }

    public double getRecallByRanklist() {
        oracle.setThreshold(0.0);
        int correct = 0;
        for (SingleLink link : matrix.getLinksAboveThreshold()) {
            if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                correct++;
            }
        }
        double recall = correct / (double) oracle.count();
        return recall;

    }

    public double getAveragePrecisionByRanklist() {
        double sumOfPrecisions = 0.0;
        int currentLink = 0;
        int correctSoFar = 0;
        LinksList links = matrix.getLinksAboveThreshold();
        Collections.sort(links, Collections.reverseOrder());
        for (SingleLink link : links) {
            currentLink++;
            if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                correctSoFar++;
                sumOfPrecisions += correctSoFar / (double) currentLink;
            }
        }
        double averagePrecision = sumOfPrecisions / oracle.allLinks().size();
        return averagePrecision;
    }

    public PrecisionRecallCurve getPrecisionRecallCurve() {
        PrecisionRecallCurve precisionRecallCurve = new PrecisionRecallCurve();
        precisionRecallCurve.setName(resultName);
        precisionRecallCurve.setCutParameter(cutParameter);

        oracle.setThreshold(0.0);
        int correct = 0;

        int TP = 0;
        int TN = 0;
        int FP = 0;
        int FN = 0;

        LinksList allLinks = matrix.allLinks();
        Collections.sort(allLinks, Collections.reverseOrder());

//        System.out.println(" allLinks = " + allLinks );
        LinksList links = matrix.getLinksAboveThreshold();
        Collections.sort(links, Collections.reverseOrder());

        int linkNumber = 0;
        for (SingleLink link : allLinks) {
//            System.out.println(" link = " + link );
            String source = link.getSourceArtifactId();
            String target = link.getTargetArtifactId();
            if (matrix.isLinkAboveThreshold(source, target)) {

                if (oracle.isLinkAboveThreshold(source, target)) {
                    correct++;
                    TP++;
                } else {
                    FP++;
//                    System.out.println("Fasle Positive: " + source + " " + target + " "
//                            + matrix.getScoreForLink(source, target));
                }
                linkNumber++;
                double precision = correct / (double) linkNumber;
                precisionRecallCurve.put(String.format("%03d_Precision", linkNumber), precision);
                double recall = correct / (double) oracle.count();
                precisionRecallCurve.put(String.format("%03d_Recall", linkNumber), recall);
                LinksDistributed linksDistributed = new LinksDistributed(TP, FP, TN, FN);
                linksDistributedAtRecall.put(recall, linksDistributed);

            } else {
                if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                    FN++;
//                    System.out.println("Fasle Negative: " + source + " " + target + " "
//                            + matrix.getScoreForLink(source, target));
                }
                TN++;
            }
        }
        return precisionRecallCurve;
    }

    public void highlightOracleInMatrix() {
        oracle.setThreshold(0.0);
        LinksList links = matrix.allLinks();
        int i = 0;
        for (SingleLink link : links) {
            if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                System.out.println(link.getSourceArtifactId() + " " + link.getTargetArtifactId() + " " + matrix.getScoreForLink(link.getSourceArtifactId(), link.getTargetArtifactId()));
                i++;
            }
        }
    }

    public void showLinksDistributed(double recall) {
        getPrecisionRecallCurve();
        LinksList allLinks = matrix.allLinks();
        System.out.println(" allLinks = " + allLinks.size());
        System.out.println(" oracle = " + oracle.allLinks().size());

        double max = 0.0;
        for (double key : linksDistributedAtRecall.keySet()) {
            if (key < recall && key > max) {
                max = key;
            }
        }

        System.out.println("at " + max + "\n" + linksDistributedAtRecall.get(max));
    }

    public void showLinksDistributed() {
        LinksList allLinks = matrix.allLinks();

        int TP = 0;
        int TN = 0;
        int FP = 0;
        int FN = 0;
        for (SingleLink link : allLinks) {
            if (matrix.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                    TP++;
                } else {
                    FP++;
                }
            } else {
                if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                    FN++;
                }
                TN++;
            }
        }

        System.out.println(" allLinks = " + allLinks.size());
        System.out.println(" oracle = " + oracle.allLinks().size());
        System.out.println(" TP = " + TP);
        System.out.println(" TN = " + TN);
        System.out.println(" FP = " + FP);
        System.out.println(" FN = " + FN);
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public List<Pair<String,String>> getAlgorithmParameters() {
        return algorithmParameters;
    }

    public void setAlgorithmParameters(List<Pair<String,String>> algorithmParameters) {
        this.algorithmParameters = algorithmParameters;
    }

    private String getLinksDistributed(double recall) {
        getPrecisionRecallCurve();

        double max = 0.0;
        for (double key : linksDistributedAtRecall.keySet()) {
            if (key < recall && key > max) {
                max = key;
            }
        }
        return "Retrieved links distributed at recll " + max + "\n" + linksDistributedAtRecall.get(max);
    }

    private String getLinksDistributed() {
        LinksList allLinks = matrix.allLinks();

        int TP = 0;
        int TN = 0;
        int FP = 0;
        int FN = 0;
        for (SingleLink link : allLinks) {
            if (matrix.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                    TP++;
                } else {
                    FP++;
                }
            } else {
                if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                    FN++;
                }
                TN++;
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append("all Retrieved Links = " + allLinks.size());
        sb.append("\n");
        sb.append("oracle = " + oracle.allLinks().size());
        sb.append("\n");
        sb.append("Retrieved links distributed at recall " + TP * 1.0 / oracle.allLinks().size());
        sb.append("\n");
        sb.append("TP = " + TP);
        sb.append("\n");
        sb.append("TN = " + TN);
        sb.append("\n");
        sb.append("FP = " + FP);
        sb.append("\n");
        sb.append("FN = " + FN);
        sb.append("\n\n");
        return sb.toString();
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void store(String path) {
        StringBuffer sb = new StringBuffer();
        sb.append(getAlgorithmName());
        sb.append("\n");
        sb.append("Parameters" + "\n");
        for (Pair<String, String> pair : getAlgorithmParameters()) {
            sb.append(pair.getKey());
            sb.append(" ");
            sb.append(pair.getValue());
            sb.append("\n");
        }
        sb.append("\n\n");
        sb.append("MeanAveragePrecisionByQuery ");
        sb.append(getMeanAveragePrecisionByQuery());
        sb.append("\n");
        sb.append("AveragePrecisionByRanklist ");
        sb.append(getAveragePrecisionByRanklist());
        sb.append("\n");
        sb.append("\n");

        sb.append(getLog());
        sb.append("\n");

        sb.append(getLinksDistributed());
        for (double r = 0.1; r <= 0.9; r = r + 0.2) {
            sb.append(getLinksDistributed(r));
            sb.append("\n\n");
        }


        sb.append(showMatrix());
        _.writeFile(sb.toString(), path + "/" + getAlgorithmName() + ".txt");
    }

    public List<String> getCorrectImprovedTargetsList() {
        return correctImprovedTargetsList;
    }

    public void setCorrectImprovedTargetsList(List<String> correctImprovedTargetsList) {
        this.correctImprovedTargetsList = correctImprovedTargetsList;
    }

    public LinksList computeFinalRanks() {

        StringHashSet sourceSet = matrix.sourceArtifactsIds();
        StringHashSet targetSet = matrix.targetArtifactsIds();


        SimilarityMatrix scoresSim = new SimilarityMatrix();

        for (String source : sourceSet) {

            double totalScorePerLink = matrix.targetArtifactsIds().size();

            Map<String, Double> scoresMap = matrix.getLinksForSourceId(source);
            Map<String, Double> sortedScores = new TreeMap<>();

            LinksList perLinklist = matrix.getLinksAboveThresholdForSourceArtifact(source);
            for (SingleLink sl : perLinklist) {
                scoresSim.addLink(sl.getSourceArtifactId(), sl.getTargetArtifactId(), totalScorePerLink);
                totalScorePerLink--;
            }
        }

        LinksList result = new LinksList();
//        Map<String,Double> result = new TreeMap<>();

//        for (String source : scoresSim.sourceArtifactsIds()) {
            for (String target : scoresSim.targetArtifactsIds()) {

                result.add(new SingleLink("1", target, 0.0));
//                if (result.getScore())
//                if (result.get(target) == null) {
//                    result.put(target, scoresSim.getScoreForLink(source, target));
//                } else {
//                    result.put(target, result.get(target) + scoresSim.getScoreForLink(source, target));
//                }
            }
//        }
//
        for (String source : sourceSet) {
            for (String target : targetSet) {
                result.updateLink("1", target, result.getScore("1", target) + scoresSim.getScoreForLink(source, target));
            }
        }


        Collections.sort(result, Collections.reverseOrder());

        System.out.println(" scoresSim = " + scoresSim );
        System.out.println(result);
        return null;
    }
}
