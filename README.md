# amf-bug-securedby

This repository contains a simple example which demonstrates a bug in the AMF parser which produces an invalid OAS3 file
when converting a RAML file which is referencing a security scheme in a library via securedBy.  Run the test 'RamlToOasConversionTest'
and it will fail.  The test converts a RAML 1.0 API file to OAS3.0 and then validates the generated OAS3 API file.  The OAS3 file 
will be written to the directory 'src/test/resources/demo-api/'.  The OAS3 file will be invalid:

```
Profile: OAS 3.0
Conforms: false
Number of results: 1

Level: Violation

- Constraint: http://a.ml/vocabularies/amf/parser#SecurityScheme-type-minCount
  Message: Type is mandatory in a Security Scheme Object
  Severity: Violation
  Target: file:///C:/Users/Tom/IdeaProjects/test-amf-bug-securedby/src/test/resources/demo-api/api.json#/web-api/endpoint/%2Faccounts/supportedOperation/get/security/requirement_1/schemes/lib.basic/scheme/fragment
  Property: http://a.ml/vocabularies/security#type
  Range: 
  Location: 
```

The generated output OAS3 file is missing the following securitySchemes component.

```json
"securitySchemes": {
    "basic": {
    "type": "http",
    "description": "This API supports Basic Authentication.\n",
    "scheme": "basic"
    }
}
```

The test RamlValidationTest shows that the RAML file is valid.