import amf.apicontract.client.platform.AMFBaseUnitClient;
import amf.apicontract.client.platform.RAMLConfiguration;
import amf.core.client.platform.model.document.BaseUnit;
import amf.core.client.platform.validation.AMFValidationReport;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

/**
 * A test to show that the RAML API file is valid.
 */
@Slf4j
public class RamlValidationTest {

    @Test
    public void ramlIsValid() throws ExecutionException, InterruptedException {
        Path pathToRamlFile = given_a_raml_1_api_referencing_a_security_scheme_in_library();

        then_the_raml_file_is_valid( pathToRamlFile );
    }

    private void then_the_raml_file_is_valid(Path pathToRamlFile) throws ExecutionException, InterruptedException {
        final AMFBaseUnitClient ramlClient = RAMLConfiguration.RAML10().baseUnitClient();
        final BaseUnit model = ramlClient.parse( pathToRamlFile.toUri().toString() ).get().baseUnit();
        AMFValidationReport report = ramlClient.validate( model ).get();
        log.info("Validation report: {}", report );
        Assertions.assertTrue( report.conforms(), "The RAML file should be valid");
    }

    private Path given_a_raml_1_api_referencing_a_security_scheme_in_library() {
        Path pathToRamlFile = Paths.get( "src/test/resources/demo-api/amf-bug-securedby.raml");
        return pathToRamlFile;
    }
}
