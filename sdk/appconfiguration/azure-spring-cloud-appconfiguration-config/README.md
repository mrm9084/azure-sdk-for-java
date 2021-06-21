# Azure Spring cloud appconfiguration config client library for Java

Azure Spring Cloud App Configuration Config is a Spring library for loading configurations and feature flags From Azure App Configuration stores. With this library, configurations are loaded from Azure App Configuration by Spring Profile, or *(no label)* if no profile is set, into Spring PropertySource, so configuration stored in Azure App Configuration can be easily used and conveniently accessed like other externalized configuration properties, e.g. properties in files.

[Package (Maven)][package] | [API reference documentation][refdocs] | [Product documentation][docs] | [Samples][sample]

## Key concepts

Azure App Configuration provides configuration for the client application.

From a developer's perspective, Azure App Configuration APIs accept and return lists of configuration key value pairs.

Azure App Configuration supports multiple special content types for configurations.

* application/vnd.microsoft.appconfig.ff+json;charset=utf-8
* application/vnd.microsoft.appconfig.keyvaultref+json;charset=utf-8
* application/json, or anything with the [main type application and contains the subtype json][json-content-type].

All other content types are treated as Key Value pairs that need no extra processing.

### application/vnd.microsoft.appconfig.ff+json;charset=utf-8

Content Type for Feature Flags. To use Feature Flags either `azure-spring-cloud-feature-management` or `azure-spring-cloud-feature-management-web` needs to be used to access Feature Flag values, otherwise they are stored in json in the propery source.

## Getting started
## Key concepts
## Examples
## Troubleshooting
## Next steps
## Contributing

<!-- Links -->
[package]: https://mvnrepository.com/artifact/com.azure.spring/azure-spring-cloud-appconfiguration-config
[refdocs]: https://azure.github.io/azure-sdk-for-java/appconfiguration.html
[docs]: https://docs.microsoft.com/azure/azure-app-configuration/quickstart-java-spring-app
[sample]: https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring/azure-spring-boot-samples/azure-appconfiguration-sample
[json-content-type]: https://docs.microsoft.com/azure/azure-app-configuration/howto-leverage-json-content-type#valid-json-content-type