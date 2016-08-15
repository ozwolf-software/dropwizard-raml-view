# DropWizard RAML API Resource Bundle

[![Build Status](https://travis-ci.org/ozwolf-software/dropwizard-raml-view.svg?branch=master)](https://travis-ci.org/ozwolf-software/dropwizard-raml-view)

This bundle allows a RAML specification to be attached to the resources of the service, exposing a human-readable, HTML representation of the RAML specification on the `/api` resource.

**Note:** This library currently only supports RAML 0.8.

## Maven Central

```xml
<dependency>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-view</artifactId>
    <version>1.0.0.1</version>
</dependency>
```

## Compatibility

The following versions are available via [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22dropwizard-raml-view%22):

+ DropWizard 1.0.0 - [Version 1.0.0.1](http://search.maven.org/#artifactdetails%7Cnet.ozwolf%7Cdropwizard-raml-view%7C1.0.0.1%7Cjar)
+ DropWizard 0.9.2 - [Version 0.9.2.1](http://search.maven.org/#artifactdetails%7Cnet.ozwolf%7Cdropwizard-raml-view%7C0.9.2.1%7Cjar)

While versions have not been deployed to Maven Central, RAML 0.8 compatibility tags are availble, though they are EOL without support.  To use these, clone the repository, switch to the branch in question and use a `mvn clean install` to install it locally or create a package that can be uploaded to your own repository.

+ DropWizard 0.8
+ DropWizard 0.7


## Endpoints

The following endpoints will be made available on your service:

+ `/api` - The API specification as a HTML viewable endpoint.
+ `/api/raw` - The raw RAML specification as re-emitted from the RAML JSON Parser.

## Example Usage

To add this bundle to your service and expose a RAML specification under the `/api` resource.

```java
bootstrap.addBundle(RamlView.bundle("apispecs/apispecs.raml"));
```

## Functionality Caveat

This project currently provides functionality that meet my usage needs for RAML specifications.  It is by no means a comprehensive HTML representation of the RAML specification.  This means particular components that other people need _will_ be missing.

## RAML Specification

The RAML specification documentation can be found [`here`](https://github.com/raml-org/raml-spec)

## Contributions

Contributions to this project are welcome.

## Dependencies

This project uses the following dependency projects:

+ [RAML Java Parser](https://github.com/raml-org/raml-java-parser)
+ [DropWizard](https://github.com/dropwizard/dropwizard)
+ [Commons Language 3](https://github.com/apache/commons-lang)
+ [Commons Codec](https://github.com/apache/commons-codec)
+ [Markdown4j](https://github.com/jdcasey/markdown4j)
