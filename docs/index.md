MANUAL
======

Library with classes and functions for interacting with the Dataverse API.

DESCRIPTION
-----------
Dataverse is an open source web application to share, preserve, cite, explore, and analyze research data. 
See: <https://dataverse.org/about>{:target=_blank}. It has several APIs that enable programmatic access. 
See: <https://guides.dataverse.org/en/latest/api/index.html>{:target=_blank}. This library facilitates accessing this 
API from Java code.

INSTALLATION
------------

To use this library in a Maven-based project:

1. Include in your `pom.xml` a declaration for the DANS maven repository:

        <repositories>
            <!-- possibly other repository declarations here ... -->
            <repository>
                <id>DANS</id>
                <releases>
                    <enabled>true</enabled>
                </releases>
                <url>https://maven.dans.knaw.nl/releases/</url>
            </repository>
        </repositories>

2. Include a dependency on this library. 

        <dependency>
            <groupId>nl.knaw.dans.lib</groupId>
            <artifactId>dans-dataverse-client-lib</artifactId>
            <version>{version}</version> <!-- <=== FILL LIBRARY VERSION TO USE HERE -->
        </dependency>
