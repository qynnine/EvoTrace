package evotrace.diff;

import evotrace.util.AppConfigure;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niejia on 15/10/8.
 */
public class VersionComparer {

    private DiffParser diffParser;
    private Map<String, ArtifactComparer> artifactComparerList;
    private List<String> changedArtifactNames;
    private List<String> addedArtifactNames;
    private List<String> removedArtifactNames;
    private List<String> unchangedArtifactNames;

    private List<String> sameArtifactNames;
    private List<String> enhancedArtifactNames;
    private List<String> weakedArtifactNames;
    private List<String> ambiguityArtifactNames;

    private ComparerType comparerType;

    private int sameArtifactCount;
    private int enhancedArtifactCount;
    private int weakedArtifactCount;
    private int ambiguityArtifactCount;

    public VersionComparer(String diffFilePath, String newVersionName, String oldVersionName, String newVersionPath, String oldVersionPath, String extendName, ComparerType comparerType) {
        diffParser = new DiffParser(diffFilePath, newVersionName, oldVersionName, extendName);
        artifactComparerList = new HashMap<>();

        this.changedArtifactNames = getArtifactNames(diffParser.getChangedFileNames());
        this.addedArtifactNames = getArtifactNames(diffParser.getAddedFileNames());
        this.removedArtifactNames = getArtifactNames(diffParser.getRemovedFileNames());
        this.unchangedArtifactNames = parseUnchangedArtifactNames(diffParser, oldVersionPath, extendName);

        this.sameArtifactNames = new ArrayList<>();
        this.enhancedArtifactNames = new ArrayList<>();
        this.weakedArtifactNames = new ArrayList<>();
        this.ambiguityArtifactNames = new ArrayList<>();

//        System.out.println("DiffParser = " + diffParser);

        for (String file : diffParser.getChangedFileNames()) {
            ArtifactComparer comparer = null;
            String id = file.split("\\.")[0];
            if (comparerType.equals(ComparerType.TextComparer)) {
                comparer = new TextComparer(newVersionPath+file, oldVersionPath+file);
            }

            artifactComparerList.put(id, comparer);
        }

        for (String file : unchangedArtifactNames) {
            ArtifactComparer comparer = null;
            String id = file.split("\\.")[0];
            if (comparerType.equals(ComparerType.TextComparer)) {
                comparer = new TextComparer(newVersionPath + file + ".java", oldVersionPath + file + ".java");
            }

            artifactComparerList.put(id, comparer);
        }

        for (String artifact : artifactComparerList.keySet()) {
            ArtifactComparer comparer = artifactComparerList.get(artifact);
            if (comparer.isSame()) {
                sameArtifactCount++;
                sameArtifactNames.add(artifact);
            } else if (comparer.isEnhanced()) {
                enhancedArtifactCount++;
                enhancedArtifactNames.add(artifact);
            } else if (comparer.isWeaked()) {
                weakedArtifactCount++;
                weakedArtifactNames.add(artifact);
            } else if (comparer.isAmbiguity()) {
                ambiguityArtifactCount++;
                ambiguityArtifactNames.add(artifact);
            }
        }
    }

    private List<String> parseUnchangedArtifactNames(DiffParser diffParser, String oldVersionPath, String extendName) {
        List<String> unChangednames = new ArrayList<>();

        File folder = new File(oldVersionPath);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();
                if (fileName.endsWith(extendName)) {
                    if (!diffParser.getAddedFileNames().contains(fileName) && !diffParser.getRemovedFileNames().contains(fileName)
                            && !diffParser.getChangedFileNames().contains(fileName)) {
                        unChangednames.add(fileName.split("\\.")[0]);
                    }
                }
            }
        }
        return unChangednames;
    }

    private List<String> getArtifactNames(List<String> files) {
        List<String> names = new ArrayList<>();
        for (String name : files) {
            names.add(name.split("\\.")[0]);
        }
        return names;
    }

    public ArtifactComparer getComparerForArtifact(String artifact) {
        return artifactComparerList.get(artifact);
    }

    public List<String> getUnchangedArtifactNames() {
        return unchangedArtifactNames;
    }

    public List<String> getChangedArtifactNames() {
        return changedArtifactNames;
    }

    public List<String> getAddedArtifactNames() {
        return addedArtifactNames;
    }

    public List<String> getRemovedArtifactNames() {
        return removedArtifactNames;
    }

    public int addedFilesCount() {
        return addedArtifactNames.size();
    }

    public int removedFilesCount() {
        return removedArtifactNames.size();
    }

    public int changedFilesCount() {
        return changedArtifactNames.size();
    }

    public int unChangedFilesCount() {
        return unchangedArtifactNames.size();
    }

    public List<String> getSameArtifactNames() {
        return sameArtifactNames;
    }

    public List<String> getEnhancedArtifactNames() {
        return enhancedArtifactNames;
    }

    public List<String> getWeakedArtifactNames() {
        return weakedArtifactNames;
    }

    public List<String> getAmbiguityArtifactNames() {
        return ambiguityArtifactNames;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------- division ----------------");
        sb.append("\n");

        sb.append("Added files" + "(" + addedFilesCount() + "):");
        sb.append("\n");
        for (String f : getAddedArtifactNames()) {
            sb.append(f);
            sb.append("\n");
        }
        sb.append("\n");

        sb.append("Removed files" + "(" + removedFilesCount() + "):");
        sb.append("\n");
        for (String f : getRemovedArtifactNames()) {
            sb.append(f);
            sb.append("\n");
        }
        sb.append("\n");

        sb.append("Changed files" + "(" + changedFilesCount() + "):");
        sb.append("\n");
        for (String f : getChangedArtifactNames()) {
            sb.append(f);
            sb.append("\n");
        }

        sb.append("Unchanged files" + "(" + unChangedFilesCount() + "):");
        sb.append("\n");
        for (String f : getUnchangedArtifactNames()) {
            sb.append(f);
            sb.append("\n");
        }


        sb.append("sameArtifactCount: " + sameArtifactCount);
        sb.append("enhancedArtifactCount: " + enhancedArtifactCount);
        sb.append("weakedArtifactCount: " + weakedArtifactCount);
        sb.append("ambiguityArtifactCount: " + ambiguityArtifactCount);
        return sb.toString();
    }



    public static void main(String[] args) {
        VersionComparer requirementVersionComparer = new VersionComparer(AppConfigure.iTrust_requirementDiffFile,
                "v11", "v10", AppConfigure.iTrust_newVersionRequirementPath, AppConfigure.iTrust_oldVersionRequirementPath,"txt",ComparerType.TextComparer);
//
//        VersionComparer codeVersionComparer = new VersionComparer(Configuration.iTrust_codeDiffFile, "java", "v11", "10"
//                , Configuration.iTrust_newVersionCodePath, Configuration.iTrust_oldVersionCodePath);
    }
}
