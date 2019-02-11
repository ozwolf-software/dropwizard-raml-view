# DropWizard RAML API Resource Bundle

[![Status](https://img.shields.io/badge/status-EOL-red.svg?style=flat-square)](https://img.shields.io/badge/status-EOL-red.svg)
[![Travis](https://img.shields.io/travis/ozwolf-software/dropwizard-raml-view.svg?style=flat-square)](https://travis-ci.org/ozwolf-software/dropwizard-raml-view)
[![Maven Central](https://img.shields.io/maven-central/v/net.ozwolf/dropwizard-raml-view.svg?style=flat-square)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.ozwolf%22%20AND%20a%3A%22dropwizard-raml-view%22)
[![Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](LICENSE)

This bundle allows a RAML specification to be attached to the resources of the service, exposing a human-readable, HTML representation of the RAML specification on the `/api` resource.

## EOL Notice

This library has reached it's conclusion and it's concepts have been migrated into the new [DropWizard RAML](https://github.com/ozwolf-software/dropwizard-raml) project.  Specifically, to migrate from this view to the new view, refer to the [DropWizard RAML API Docs](https://github.com/ozwolf-software/dropwizard-raml/tree/master/dropwizard-raml-apidocs) library.

## Disclaimer

This bundle supports both RAML 1.0 and RAML 0.8 but not all features of the specification language have been implemented in the view.
 
If you wish to include a missing feature, either raise an issue and I will address it when time permits or fork the project and make a pull request.

As this bundle now uses the newer `raml-parser-2` library, Mulesoft have implemented some stricter controls over the specification structure, even for RAML 0.8.  This means some older specification files may need to be updated to meet requirements.

## Maven Central

```xml
<dependency>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-view</artifactId>
    <version>${current.version}</version>
</dependency>
```

## Compatibility

The following versions are available via [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22dropwizard-raml-view%22):

+ DropWizard 1.2.+ (with RAML 0.8 and 1.0 support) - [Version 1.2.1.0](http://search.maven.org/#artifactdetails%7Cnet.ozwolf%7Cdropwizard-raml-view%7C1.2.1.0%7Cjar)
+ DropWizard 1.1.+ (with RAML 0.8 and 1.0 support) - [Version 1.1.1.0](http://search.maven.org/#artifactdetails%7Cnet.ozwolf%7Cdropwizard-raml-view%7C1.1.1.0%7Cjar)
+ DropWizard 1.0.+ (with RAML 0.8 and 1.0 support) - [Version 1.0.2.0](http://search.maven.org/#artifactdetails%7Cnet.ozwolf%7Cdropwizard-raml-view%7C1.0.2.0%7Cjar)
+ DropWizard 0.9.2 (with RAML 0.8 support) - [Version 0.9.2.1](http://search.maven.org/#artifactdetails%7Cnet.ozwolf%7Cdropwizard-raml-view%7C0.9.2.1%7Cjar)

While versions have not been deployed to Maven Central, older DropWizard versions with RAML 0.8 compatibility tags are available, though they are EOL without support.  To use these, clone the repository, switch to the branch in question and use a `mvn clean install` to install it locally or create a package that can be uploaded to your own repository.

+ DropWizard 0.8
+ DropWizard 0.7

## Endpoints

The following endpoints will be made available on your service:

+ `/api` - The API specification as a HTML viewable endpoint.
+ ~~`/api/raw`~~ - The API specification as it's raw YAML format.  **Note:** At present, this resource is unavailable due to the `RamlEmitter` functionality of the original RAML Parser library not being implemented in the newer library.  Please refer to this [issue](https://github.com/raml-org/raml-java-parser/issues/159) for further information. 

## Example Usage

To add this bundle to your service and expose a RAML specification under the `/api` resource.  The bundle will differentiate between version 0.8 and version 1.0 RAML specifications automatically.

```java
bootstrap.addBundle(RamlView.bundle("apispecs/apispecs.raml"));
```

## Functionality Caveat

This project currently provides functionality that meet my usage needs for RAML specifications.  It is by no means a comprehensive HTML representation of the RAML specification.  This means particular components that other people need _will_ be missing.

## RAML Specification

+ [RAML 1.0 Documentation](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md)
+ [RAML 0.8 Documentation](https://github.com/raml-org/raml-spec/blob/master/versions/raml-08/raml-08.md)

## Contributions

Contributions to this project are welcome.

## Dependencies

This project uses the following dependency projects:

+ [RAML Java Parser 2](https://github.com/raml-org/raml-java-parser)
+ [DropWizard](https://github.com/dropwizard/dropwizard)
+ [Commons Language 3](https://github.com/apache/commons-lang)
+ [Commons Codec](https://github.com/apache/commons-codec)
+ [Markdown4j](https://github.com/jdcasey/markdown4j)
