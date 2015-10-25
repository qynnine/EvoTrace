package evotrace.diff;

import evotrace.preprocess.ArtifactPreprocessor;
import evotrace.util._;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by niejia on 15/10/7.
 */
public class TextComparer implements ArtifactComparer {

    private List<String> oldTokenList;
    private List<String> newTokenList;

    private List<String> addedTokenList;
    private List<String> removedTokenList;

//    private double rateOfChangeForTrace;
//    private double rateOfChangeForNotrace;

    public TextComparer(String newVersion, String oldVersion) {

        String oldText = ArtifactPreprocessor.handlePureTextFile(_.readFile(oldVersion));
        String newText = ArtifactPreprocessor.handlePureTextFile(_.readFile(newVersion));

        String[] oldTokens = oldText.split(" ");
        oldTokenList = Arrays.asList(oldTokens);

        String[] newTokens = newText.split(" ");
        newTokenList = Arrays.asList(newTokens);

        calculateAddedTokens();
        calculateRemovedTokens();

        calculateRateOfChangeForTrace();
        calculateRateOfChangeForNoTrace();
    }

    private void calculateRateOfChangeForNoTrace() {
//        double v =
    }

    private void calculateRateOfChangeForTrace() {

    }


    private void calculateAddedTokens() {
        List<String> oldTokenListDeepCopy = new ArrayList<>(oldTokenList);
        List<String> newTokenListDeepCopy = new ArrayList<>(newTokenList);

        for (String token : oldTokenListDeepCopy) {
            newTokenListDeepCopy.remove(token);
        }

        addedTokenList = new ArrayList<>(newTokenListDeepCopy);
    }

    private void calculateRemovedTokens() {
        List<String> oldTokenListDeepCopy = new ArrayList<>(oldTokenList);
        List<String> newTokenListDeepCopy = new ArrayList<>(newTokenList);

        for (String token : newTokenListDeepCopy) {
            oldTokenListDeepCopy.remove(token);
        }

        removedTokenList = new ArrayList<>(oldTokenListDeepCopy);
    }


    public List<String> addedTokenList() {
        return addedTokenList;
    }

    public List<String> removedTokenList() {
        return removedTokenList;
    }

    public int oldVersionLength() {
        return oldTokenList.size();
    }

    public int newVersionLength() {
        return newTokenList.size();
    }

    // |A_new - A_old|
    public int addedTokenCount() {
        return addedTokenList.size();
    }

    // |A_old - A_new|
    public int removedTokenCount() {
        return removedTokenList.size();
    }

//    public double getRateOfChangeForTrace() {
//        return rateOfChangeForTrace;
//    }
//
//    public double getRateOfChangeForNotrace() {
//        return rateOfChangeForNotrace;
//    }

    public boolean isEnhanced() {
        return addedTokenCount() > 0 && removedTokenCount() == 0;
    }

    public boolean isWeaked() {
        return addedTokenCount() == 0 && removedTokenCount() > 0;
    }

    public boolean isSame() {
        return addedTokenCount() == 0 && removedTokenCount() == 0;
    }

    public boolean isAmbiguity() {
        return addedTokenCount() > 0 && removedTokenCount() > 0;
    }
}
