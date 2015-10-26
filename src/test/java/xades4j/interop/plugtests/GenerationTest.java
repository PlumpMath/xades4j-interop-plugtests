package xades4j.interop.plugtests;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyStoreException;
import java.util.Base64;
import org.w3c.dom.Document;
import xades4j.algorithms.Algorithm;
import xades4j.algorithms.ExclusiveCanonicalXMLWithoutComments;
import xades4j.production.EnvelopedXmlObject;
import xades4j.production.XadesBesSigningProfile;
import xades4j.production.XadesCSigningProfile;
import xades4j.production.XadesFormatExtenderProfile;
import xades4j.production.XadesSigningProfile;
import xades4j.production.XadesTSigningProfile;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.DataObjectFormatProperty;
import xades4j.providers.AlgorithmsProviderEx;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.SignaturePropertiesCollector;
import xades4j.providers.SignaturePropertiesProvider;
import xades4j.providers.impl.AuthenticatedTimeStampTokenProvider;
import xades4j.providers.impl.DefaultAlgorithmsProviderEx;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.providers.impl.TSAHttpAuthenticationData;
import xades4j.providers.impl.ValidationDataFromCertValidationProvider;

/**
 *
 * @author luis
 */
public class GenerationTest extends TestBase {

    class AlgsProvider extends DefaultAlgorithmsProviderEx {

        @Override
        public Algorithm getCanonicalizationAlgorithmForTimeStampProperties() {
            return new ExclusiveCanonicalXMLWithoutComments();
        }

        @Override
        public Algorithm getCanonicalizationAlgorithmForSignature() {
            return new ExclusiveCanonicalXMLWithoutComments();
        }
    }

    private final KeyingDataProvider keyingDataProvider;
    protected final SignaturePropertiesProvider noOpSigPropsProvider;
    private final AlgorithmsProviderEx algsProvider;
    private final TSAHttpAuthenticationData tsaAuthData;

    protected GenerationTest() {

        try {
            DirectPasswordProvider passProvider = new DirectPasswordProvider("TODO");
            keyingDataProvider = new FileSystemKeyStoreKeyingDataProvider(
                    "pkcs12",
                    "TODO",
                    FirstCertificateSelector.instance,
                    passProvider,
                    passProvider,
                    false);
        } catch (KeyStoreException ex) {
            throw new NullPointerException("KeyingDataProvider init failed:" + ex.getMessage());
        }

        noOpSigPropsProvider = (SignaturePropertiesCollector spc) -> {
        };

        algsProvider = new AlgsProvider();
        
        tsaAuthData = new TSAHttpAuthenticationData(
                "http://xadessrv.plugtests.net/protected/tsp/TspRequest",
                "TODO",
                "TODO");
    }

    protected XadesSigningProfile newBes() {
        return new XadesBesSigningProfile(keyingDataProvider)
                .withAlgorithmsProviderEx(algsProvider);
    }

    protected XadesSigningProfile newT() {
        return new XadesTSigningProfile(keyingDataProvider)
                .withAlgorithmsProviderEx(algsProvider)
                .withTimeStampTokenProvider(AuthenticatedTimeStampTokenProvider.class)
                .withBinding(TSAHttpAuthenticationData.class, tsaAuthData);
    }

    protected XadesSigningProfile newC() {
        return new XadesCSigningProfile(keyingDataProvider, new ValidationDataFromCertValidationProvider(VerificationTest.scokCertificateValidator))
                .withAlgorithmsProviderEx(algsProvider)
                .withTimeStampTokenProvider(AuthenticatedTimeStampTokenProvider.class)
                .withBinding(TSAHttpAuthenticationData.class, tsaAuthData);
    }
    
    protected XadesFormatExtenderProfile newExtender(){
        return new XadesFormatExtenderProfile()
                .withAlgorithmsProviderEx(algsProvider)
                .withTimeStampTokenProvider(AuthenticatedTimeStampTokenProvider.class)
                .withBinding(TSAHttpAuthenticationData.class, tsaAuthData);
    }

    protected void outputSignature(Document doc, String testCaseFolder, String testCaseName) throws Exception {
        outputDocument(doc, testCaseFolder, "Signature-" + testCaseName + ".xml");
    }

    protected static DataObjectDesc getSignedDataObject1AsEnveloped(Document doc) throws IOException {
        DataObjectDesc obj = getSignedDataObject1AsEnvelopedNoProps(doc);
        return obj.withDataObjectFormat(new DataObjectFormatProperty("text/plain", "http://www.ietf.org/rfc/rfc2279.txt")
                .withIdentifier("http://uri.etsi.org/01903/v1.3.2#")
                .withDescription("Technical Specification")
                .withDocumentationUri("http://www.w3.org/TR/XAdES/")
                .withDocumentationUri("file:///test.test/schemas/xades.xsd"));
    }

    protected static DataObjectDesc getSignedDataObject1AsEnvelopedNoProps(Document doc) throws IOException {
        byte[] bytes = Files.readAllBytes(dataFilesBasePath.resolve("../../data/ts_101903v010302p.txt"));
        String base64String = Base64.getEncoder().encodeToString(bytes);
        return new EnvelopedXmlObject(doc.createTextNode(base64String));
    }
    
    protected static DataObjectDesc getSignedDataObject2AsEnveloped(Document doc) throws IOException {
        byte[] bytes = Files.readAllBytes(dataFilesBasePath.resolve("../../data/wordDoc.doc"));
        String base64String = Base64.getEncoder().encodeToString(bytes);
        DataObjectDesc obj = new EnvelopedXmlObject(doc.createTextNode(base64String));
        return obj.withDataObjectFormat(new DataObjectFormatProperty("application/msword")
                .withIdentifier("http://xades-portal.etsi.org/2012XAdESBpPlugtest/fooWordDocument#")
                .withDescription("Foo Word Document for Plugtest"));
    }

    protected static DataObjectDesc getSignedDataObject3AsEnveloped(Document doc) throws IOException {
        byte[] bytes = Files.readAllBytes(dataFilesBasePath.resolve("../../data/ts_101903v010402p.txt"));
        String base64String = Base64.getEncoder().encodeToString(bytes);
        DataObjectDesc obj = new EnvelopedXmlObject(doc.createTextNode(base64String));
        return obj.withDataObjectFormat(new DataObjectFormatProperty("text/plain", "http://www.ietf.org/rfc/rfc2279.txt")
                .withIdentifier("http://uri.etsi.org/01903/v1.4.2#")
                .withDescription("Technical Specification")
                .withDocumentationUri("http://www.w3.org/TR/XAdES v1.4.2/")
                .withDocumentationUri("file:///test.test/schemas/xades.xsd"));
    }
}
