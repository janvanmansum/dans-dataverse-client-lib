Development
===========
This page contains information for developers about how to contribute to this project.

General
-------

* When extending the library follow the established patterns, to keep it easy to understand for any
  new user.

JavaDoc
-------
Since this is a library, the JavaDocs should be relatively extensive, although there is no need to go
overboard with this. At a minimum:

* The JavaDocs must be generated successfully. As of today this is a standard part of the build; the build
  will fail if doc generation fails.
* Every API endpoint method needs JavaDocs that documents the parameters and exceptions and has
  a deep link to the Dataverse docs for the end-point that is called. This must be a link to target
  "_blank". See existing code for examples.
* If an example program for the end-point method is available (which _should_ be the case) also add
  a deep link to (the latest commit of) the example code.
* [Run the documentation site locally](https://dans-knaw.github.io/dans-datastation-architecture/dev/#documentation-with-mkdocs){:target=_blank}
  to check how it renders.

Examples
--------
Each API endpoint method should have an example program. The program should be easy to read and usable to
try out the end-point. It usually takes a couple of command line parameters so that the user can point it
to a specific dataset and try several things without modifying the code. However, these are code examples,
and as such as intended to be modified for ad hoc testing. Do not add too many parameters or complex logic
to them.

    !!! alert Connecting to Dataverse on a vagrant box



Lombok
------
Use the [Lombok](https://projectlombok.org/){:target=_blank} annotations to:

* add a logger to classes that may contain logic. Do not add a logger to model classes;
* automatically generate getters, setters, toString, equals/hashCode and constructors where appropriate.
