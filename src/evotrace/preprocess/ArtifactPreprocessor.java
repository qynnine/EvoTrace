package evotrace.preprocess;

/**
 * Created by niejia on 15/2/22.
 */
public class ArtifactPreprocessor {

    private static final String stopwordsPath = "data/stopwords/stop-words_english_1_en.txt";

    public static String handlePureTextFile(String str) {
        str = CleanUp.chararctorClean(str);
        str = CleanUp.lengthFilter(str, 3);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, stopwordsPath);
        return str;
    }

    public static String handleJavaFile(String str) {
        str = CleanUp.chararctorClean(str);
        str = CamelCase.split(str);
        str = CleanUp.lengthFilter(str, 3);
        str = CleanUp.tolowerCase(str);
        str = Snowball.stemming(str);
        str = Stopwords.remover(str, stopwordsPath);
        return str;
    }
}