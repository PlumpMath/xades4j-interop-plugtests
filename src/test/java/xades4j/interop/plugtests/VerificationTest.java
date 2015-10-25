package xades4j.interop.plugtests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.xml.security.utils.Constants;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import xades4j.XAdES4jException;
import xades4j.providers.CertificateValidationProvider;
import xades4j.providers.impl.PKIXCertificateValidationProvider;
import xades4j.utils.FileSystemDirectoryCertStore;
import xades4j.verification.InvalidSignatureException;
import xades4j.verification.SignatureSpecificVerificationOptions;
import xades4j.verification.XAdESForm;
import xades4j.verification.XAdESVerificationResult;
import xades4j.verification.XadesVerificationProfile;
import xades4j.verification.XadesVerifier;
import xades4j.xml.unmarshalling.PropertyUnmarshalException;

/**
 *
 * @author luis
 */
public class VerificationTest extends TestBase {

    public static final CertificateValidationProvider scokCertificateValidator;

    static {
        try {
            File cryptoDir = TestBase.basePath.resolve("CryptographicMaterial").toFile();

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(new FileInputStream(new File(cryptoDir, "RootCAOK.cer")));

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(null);
            ks.setEntry("root", new KeyStore.TrustedCertificateEntry(cert), null);

            FileSystemDirectoryCertStore certStore = new FileSystemDirectoryCertStore(cryptoDir.getAbsolutePath());
            scokCertificateValidator = new PKIXCertificateValidationProvider(
                    ks,
                    true,
                    certStore.getStore());
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | CRLException | NoSuchProviderException ex) {
            throw new NullPointerException("CertificateValidationProvider init failed:" + ex.getMessage());
        }
    }

    protected static VerificationTestCasesBuilder folder(String testCasesFolder) {
        return new VerificationTestCasesBuilder(TestBase.testCasesBasePath.resolve(testCasesFolder));
    }

    private final VerificationTestCase testCase;
    private final List<XAdESForm> expectedForms;
    private final Map<String, VerificationTestCaseRules> rulesPerTestCase;

    protected VerificationTest(VerificationTestCase testCase, XAdESForm... expectedForms) {
        this.testCase = testCase;
        this.expectedForms = Arrays.asList(expectedForms);
        this.rulesPerTestCase = new HashMap<>();
    }

    @Test
    public void test() throws Exception {

        VerificationTestCaseRules rules = this.testCase.rules;
        XadesVerifier verifier = new XadesVerificationProfile(scokCertificateValidator)
                .acceptUnknownProperties(true) // TS validation data, mostly
                .newVerifier();
        try {
            XAdESVerificationResult res = verifier.verify(
                    getSignatureElement(),
                    new SignatureSpecificVerificationOptions().useBaseUri(dataFilesBaseUri));

            assertTrue("Unexpected form", this.expectedForms.contains(res.getSignatureForm()));
            rules.extraValidations
                    .stream()
                    .forEach(v -> v.accept(res));

        } catch (PropertyUnmarshalException ex) {
            assumeFalse(rules.propsToSkipTestOnUnmarshalError.contains(ex.getPropertyName()));
            assumeFalse("Unsupported".equals(ex.getPropertyName()));
            System.err.println(ex.getPropertyName());
            throw ex;
        } catch (AssertionError ex) {
            System.err.println(ex.getMessage());
            ouputVerificationReport(false);
            fail(ex.getMessage());
        } catch (InvalidSignatureException ex) {
            ouputVerificationReport(false);
            fail(ex.getMessage());
        } catch (XAdES4jException ex) {
            System.err.println(ex.getCause());
            throw ex;
        }

        ouputVerificationReport(true);
    }

    private Element getSignatureElement() throws Exception {
        Document doc = db.parse(this.testCase.sigFile);
        NodeList nodes = doc.getElementsByTagNameNS(Constants.SignatureSpecNS, Constants._TAG_SIGNATURE);
        return (Element) nodes.item(0);
    }

    private void ouputVerificationReport(boolean valid) throws Exception {
        Document report = getNewDocument();
        report.appendChild(report.createElement(valid ? "VALID" : "INVALID"));
        String fileName = String.format("Verification_of_%s_%s", this.testCase.company, this.testCase.sigFile.getName());
        outputDocument(report, this.testCase.testCasesFolder, fileName);
    }
}
