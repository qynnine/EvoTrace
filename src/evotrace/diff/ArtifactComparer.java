package evotrace.diff;

/**
 * Created by niejia on 15/10/18.
 */
public interface ArtifactComparer {

    public boolean isEnhanced();

    public boolean isWeaked();

    public boolean isSame();

    public boolean isAmbiguity();
}
