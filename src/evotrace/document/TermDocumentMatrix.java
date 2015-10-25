package evotrace.document;

import java.util.*;

/**
 * Created by niejia on 15/2/23.
 */
public class TermDocumentMatrix {

    private double[][] matrix;
    private List<String> termIndex;
    private List<String> docIndex;
    private Map<String, Integer> termIndexLookup;
    private Map<String, Integer> docIndexLookup;

    public TermDocumentMatrix() {
    }

    public double getValue(int docIndex, int termIndex) {
        return getMatrix()[docIndex][termIndex];
    }

    public void setValue(int docIndex, int termIndex, double value) {
        getMatrix()[docIndex][termIndex] = value;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public List<String> getTermIndex() {
        return termIndex;
    }

    public void setTermIndex(List<String> termIndex) {
        this.termIndex = termIndex;
    }

    public List<String> getDocIndex() {
        return docIndex;
    }

    public void setDocIndex(List<String> docIndex) {
        this.docIndex = docIndex;
    }

    public int NumTerms() {
        return termIndex.size();
    }

    public int NumDocs() {
        return docIndex.size();
    }

    public TermDocumentMatrix(ArtifactsCollection artifacts) {
        termIndex = new ArrayList<>();
        docIndex = new ArrayList<>();
        termIndexLookup = new HashMap<>();
        docIndexLookup = new HashMap<>();

        // create temporary corpus to build matrix with
        Map<String, Map<String, Double>> corpus = new LinkedHashMap<>();
        for (Artifact a : artifacts.values()) {
            // update document maps
            docIndex.add(a.id);
            docIndexLookup.put(a.id, docIndex.size() - 1);
            corpus.put(a.id, new LinkedHashMap<String, Double>());

            for (String term : a.text.split(" ")) {
                // update term maps
                if (term != null && !term.equals(" ")) {
                    if (!termIndexLookup.containsKey(term)) {
                        termIndex.add(term);
                        termIndexLookup.put(term, termIndex.size() - 1);
                    }
                }

                // update document counts
                if (corpus.get(a.id).containsKey(term)) {
                    double count = corpus.get(a.id).get(term);
                    corpus.get(a.id).put(term, count + 1);
                } else {
                    corpus.get(a.id).put(term, Double.valueOf(1));
                }
            }
        }

        matrix = new double[docIndex.size()][];
        for (int i = 0; i < docIndex.size(); i++) {
            matrix[i] = new double[termIndex.size()];
            for (int j = 0; j < termIndex.size(); j++) {
                Double v = corpus.get(docIndex.get(i)).get(termIndex.get(j));
                if (v != null) {
                    matrix[i][j] = v;
                } else {
                    matrix[i][j] = 0.0;
                }
            }
        }

    }

    public double[] getDocument(int index) {
        return matrix[index];
    }

    public double[] getDocument(String artifactID) {
        return getDocument(docIndexLookup.get(artifactID));
    }

    public double getValue(String artifactID, String term) {
        return getValue((int) docIndexLookup.get(artifactID), (int) termIndexLookup.get(term));
    }

    public int getTermIndex(String term) {
        return termIndexLookup.get(term);
    }

    public String getTermName(int index) {
        return termIndex.get(index);
    }

    public int getDocumentIndex(String artifactID) {
        return docIndexLookup.get(artifactID);
    }

    public String getDocumentName(int index) {
        return docIndex.get(index);
    }

    public void setDocument(int index, double[] doc) {
        if (doc.length != matrix[index].length) {
            throw new IllegalArgumentException("The array sizes do not match.");
        }
        matrix[index] = doc;
    }

    public void setDocument(String artifactID, double[] doc) {
        setDocument(docIndexLookup.get(artifactID), doc);
    }

    public void setValue(String artifactID, String term, double value) {
        setValue(docIndexLookup.get(artifactID), termIndexLookup.get(term), value);
    }

    // in TermDocumentMatrix is setMatrix
    public void setNewMatrix(double[][] matrix) {
        if (matrix.length != this.matrix.length) {
            throw new IllegalArgumentException("The matrix has the wrong number of rows.");
        }
        for (int i = 0; i < this.matrix.length; i++) {
            if (matrix[i].length != this.matrix[i].length) {
                throw new IllegalArgumentException("The matrix has the wrong number of columns in row " + i + ".");
            }
        }
        this.matrix = matrix;
    }

    public static List<TermDocumentMatrix> Equalize(TermDocumentMatrix matrix1, TermDocumentMatrix matrix2) {
        // initialize matrices
        List<TermDocumentMatrix> matrices = new ArrayList<>();

        //matrix 1
        matrices.add(new TermDocumentMatrix());
        matrices.get(0).matrix = new double[matrix1.NumDocs()][];
        matrices.get(0).docIndex = new ArrayList<String>(matrix1.docIndex);
        matrices.get(0).docIndexLookup = new HashMap<String, Integer>(matrix1.docIndexLookup);

        //matrix 2
        matrices.add(new TermDocumentMatrix());
        matrices.get(1).matrix = new double[matrix2.NumDocs()][];
        matrices.get(1).docIndex = new ArrayList<String>(matrix2.docIndex);
        matrices.get(1).docIndexLookup = new HashMap<String, Integer>(matrix2.docIndexLookup);

        List<String> termIndex = new ArrayList<String>();
        Map<String, Integer> termIndexLookup = new HashMap<String, Integer>();
        Map<String, Integer> leftovers = new HashMap<String, Integer>(matrix2.termIndexLookup);

        for (String term : matrix1.termIndex) {
            termIndex.add(term);
            termIndexLookup.put(term, termIndex.size() - 1);
            // remove duplicate terms
            if (matrix2.termIndexLookup.containsKey(term)) {
                leftovers.remove(term);
            }
        }

        // add leftovers
        for (String term : leftovers.keySet()) {
            termIndex.add(term);
            termIndexLookup.put(term, termIndex.size() - 1);
        }

        // create new term distributions for each document
        // matrix 1
        matrices.get(0).termIndex = new ArrayList<String>(termIndex);
        matrices.get(0).termIndexLookup = new HashMap<String, Integer>(termIndexLookup);

        for (int i = 0; i < matrices.get(0).NumDocs(); i++) {
            matrices.get(0).matrix[i] = new double[termIndex.size()];
            // fill in original values
            for (int j = 0; j < matrix1.NumTerms(); j++) {
                matrices.get(0).setValue(i, j, matrix1.getValue(i, j));
            }
            // fill in missing terms
            for (int j = matrix1.NumTerms(); j < termIndex.size(); j++) {
                matrices.get(0).setValue(i, j, 0.0);
            }
        }

        // matrix 2
        matrices.get(1).termIndex = new ArrayList<String>(termIndex);
        matrices.get(1).termIndexLookup = new HashMap<String, Integer>(termIndexLookup);
        for (int i = 0; i < matrices.get(1).NumDocs(); i++) {
            matrices.get(1).matrix[i] = new double[termIndex.size()];
            // fill in values
            for (int j = 0; j < termIndex.size(); j++) {
                if (matrix2.containsTerm(termIndex.get(j))) {
                    matrices.get(1).setValue(i, j, matrix2.getValue(matrix2.getDocumentName(i), termIndex.get(j)));
                } else {
                    matrices.get(1).setValue(i, j, 0.0);
                }
            }
        }

        // return
        return matrices;
    }

    public boolean containsTerm(String term) {
        return termIndexLookup.containsKey(term);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < matrix.length; i++) {
            sb.append(docIndex.get(i) + ": ");
            for (int j = 0; j < matrix[0].length; j++) {
                sb.append(termIndex.get(j));
                sb.append("._");
//                sb.append("component._");
                sb.append(matrix[i][j]);
                sb.append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
