Examples
========

This project contains sample code  that shows how to use the library. The examples
are part of the same GitHub repository and can be found in the [examples sub-module](https://github.com/DANS-KNAW/dans-dataverse-client-lib/tree/master/examples/){:target=_blank:}.

# Running the examples

Copy the supplied `dataverse.properties.template` to `dataverse.properties` 
and edit the properties to match your setup. 
For example; when running the `dev_dataversenl` VM, set the `baseUrl=http://ddvnl.dans.knaw.nl:8080`. 
Note the `http` and `8080` to circumvent any SSL and self signed certificate problems. 
Also make sure you have the correct API key and unblock secret. 

The simples example just to check that you can connect is running the `DataverseView.java`, 
because it does not need any input and simply returns the `view` of the root verse. 

In the following example program arguments the `$DOI` is a placeholder for a DOI of the Dataset that is being manipulated; lie `doi:10.5072/FK2/ABCD`. 

## Examples that mutate the dataset metadata

These potentially need a system metadata key to work. 
If you don't provide the right key you get an error message: 
"Updating system metadata in block citation requires a valid key". 

### DatasetUpdateMetadataFromJsonLd

Updates the metadata of a dataset using the JSON-LD format. 

To change the title

    $DOI http://purl.org/dc/terms/title "Some new title"

When you have set up Dataverse to protect a metadata block with a secret key you can provide ths also. 
For the `citation` metadata block (where the `title` is) and the key `mysecretkey`:

    $DOI http://purl.org/dc/terms/title "Some new title" citation mysecretkey

You can also update the license (not part of a metadata block) with the following arguments:
 
    $DOI http://schema.org/license  http://creativecommons.org/licenses/by-nc/4.0

or back to default 

    $DOI http://creativecommons.org/publicdomain/zero/1.0

For the license change no system metadata key is needed. 


### DatasetUpdateMetadata

This sets all values in code, the only thing you need to provide is a DOI.
When you have set up Dataverse to protect the citation metadata block with a secret key you need provide this also.

    $DOI citation mysecretkey


### DatasetEditMetadata

Allows to change the `title` of the Dataset. Add the secret key if the citation block was protected. 

    $DOI "My Edited Dataset" mysecretkey


### DataverseCreateDataset

Creates a new Dataset with metadata set in code. Add the secret key if the citation block was protected.

    mysecretkey

### DataverseImportDataset

Imports a Dataset with metadata set in code. The DOI is automatically generated in the test code. 
Add the secret key if the citation block was protected.

    mysecretkey
