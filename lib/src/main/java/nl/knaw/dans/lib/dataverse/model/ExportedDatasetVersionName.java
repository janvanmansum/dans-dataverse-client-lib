package nl.knaw.dans.lib.dataverse.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to parse the names of dataset versions exported to RDA bags and metadata files. This is useful if you use `LocalSubmitToArchiveCommand`
 * [to export the bags to a local disk](https://guides.dataverse.org/en/latest/installation/config.html#local-path-configuration).
 */
public class ExportedDatasetVersionName {

    /*
    private static final String DOI_PATTERN = "(?<doi>[A-Za-z0-9-]+?)";
    private static final String SCHEMA_PATTERN = "(-(?<schema>datacite)\\.)?";
    private static final String DATASET_VERSION_PATTERN = "v(?<major>[0-9]+).(?<minor>[0-9]+)";
    private static final String EXTENSION_PATTERN = "(?<extension>.zip|.xml)";
    private static final Pattern PATTERN = Pattern.compile(DOI_PATTERN + SCHEMA_PATTERN + DATASET_VERSION_PATTERN + EXTENSION_PATTERN);
     */

    /*
     Reverse-engineered from edu.harvard.iq.dataverse.engine.command.impl.LocalSubmitToArchiveCommand, the pattern that the local filename should adhere to is:

     SPACENAME + ('-' + SCHEMA + '.') + 'v' + MAJOR + '.' + MINOR + EXTENSION
     */
    private static final String SPACENAME_PATTERN = "(?<spacename>[A-Za-z0-9-]+?)";
    private static final String SCHEMA_PATTERN = "(-(?<schema>datacite)\\.)?";
    private static final String DATASET_VERSION_PATTERN = "v(?<major>[0-9]+).(?<minor>[0-9]+)";
    private static final String EXTENSION_PATTERN = "(?<extension>.zip|.xml)";
    private static final Pattern PATTERN = Pattern.compile(SPACENAME_PATTERN + SCHEMA_PATTERN + DATASET_VERSION_PATTERN + EXTENSION_PATTERN);

    private final String spaceName;
    private final String schema;
    private final int majorVersion;
    private final int minorVersion;
    private final String extension;

    /**
     * Creates a new object to parse the name of one of the exported files. It works on both the exported ZIP and XML files. Only pass in the base name, not the complete path.
     *
     * @param name the name to parse
     */
    public ExportedDatasetVersionName(String name) {
        Matcher matcher = PATTERN.matcher(name);
        if (!matcher.matches())
            throw new IllegalArgumentException(String.format("Name does not conform to dataset version export naming pattern: %s", name));
        spaceName = matcher.group("spacename");
        schema = matcher.group("schema");
        majorVersion = Integer.parseInt(matcher.group("major"));
        minorVersion = Integer.parseInt(matcher.group("minor"));
        extension = matcher.group("extension");
    }

    /**
     * The part of the filename derived from the global ID (doi, handle)
     *
     * @return the spacename
     */
    public String getSpaceName() {
        return spaceName;
    }

    public String getSchema() {
        return schema;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public String getExtension() {
        return extension;
    }
}
