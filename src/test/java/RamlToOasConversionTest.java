import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.AMFDocumentResult;
import amf.apicontract.client.platform.APIConfiguration;
import amf.apicontract.client.platform.OASConfiguration;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.validation.AMFValidationReport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

/**
 * A test to show that the conversion from RAML to OAS produces
 * an invalid OAS file.
 */
@Slf4j
public class RamlToOasConversionTest {
    private String OAS3_OUTPUT_FILE_NAME = "api.json";

    @Test
    public void ramlToOasConversionShouldProduceValidOasFile() throws ExecutionException, InterruptedException, IOException {
        Path pathToRamlFile = given_a_raml_1_api_referencing_a_security_scheme_in_library();

        Path pathToOasFile = when_the_raml_api_is_converted_to_oas3( pathToRamlFile );

        then_the_oas_api_file_is_valid( pathToOasFile );
    }

    private void then_the_oas_api_file_is_valid(Path pathToOasFile) throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient oasClient = OASConfiguration.OAS30().baseUnitClient();
        final BaseUnit model = oasClient.parse( pathToOasFile.toUri().toString() ).get().baseUnit();
        AMFValidationReport report = oasClient.validate( model ).get();
        log.info("Validation report: {}", report );
        Assertions.assertTrue( report.conforms(), "The OAS file should be valid");
    }

    private Path when_the_raml_api_is_converted_to_oas3(Path pathToRamlFile) throws ExecutionException, InterruptedException, IOException {
        final AMFBaseUnitClient inputFileClient = APIConfiguration.API().baseUnitClient();
        AMFDocumentResult parseResult = inputFileClient.parseDocument( pathToRamlFile.toUri().toString() ).get();
        AMFBaseUnitClient oas30Client = OASConfiguration.OAS30().baseUnitClient();
        final BaseUnit transformedOas3Model = oas30Client.transform( parseResult.baseUnit() ).baseUnit();
        final String oasJsonOutputString = oas30Client.render( transformedOas3Model ).trim();

        Path pathToOas3File = Paths.get("src/test/resources/demo-api/" + OAS3_OUTPUT_FILE_NAME   );
        Files.write( pathToOas3File, oasJsonOutputString.getBytes(StandardCharsets.UTF_8) );

        return pathToOas3File;
    }

    private Path given_a_raml_1_api_referencing_a_security_scheme_in_library() {
        Path pathToRamlFile = Paths.get( "src/test/resources/demo-api/amf-bug-securedby.raml");
        return pathToRamlFile;
    }
}
