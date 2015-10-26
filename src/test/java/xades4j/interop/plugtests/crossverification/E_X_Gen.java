package xades4j.interop.plugtests.crossverification;

import org.junit.Test;
import org.w3c.dom.Document;
import xades4j.interop.plugtests.GenerationTest;
import xades4j.interop.plugtests.VerificationTest;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesSignatureFormatExtender;
import xades4j.production.XadesSigner;
import xades4j.properties.DataObjectDesc;
import xades4j.verification.XAdESForm;
import xades4j.verification.XadesVerifier;

/**
 *
 * @author luis
 */
public class E_X_Gen extends GenerationTest {

    public static final String TestCaseFolder = "XAdES-E-X.SCOK";
    public static final String TestCase_X_X_1 = "X-X-1";

    @Test
    public void X_X_1() throws Exception {
        Document doc = getNewDocument();

        XadesSigner signer = newC().newSigner();
        XadesVerifier verifier = VerificationTest.newVerifier();
        XadesSignatureFormatExtender extender = newExtender().getFormatExtender();
        
        DataObjectDesc obj = getSignedDataObject1AsEnvelopedNoProps(doc);
        signer.sign(new SignedDataObjects(obj), doc);
        verifier.verify(doc.getDocumentElement(), null, extender, XAdESForm.X);
        
        outputSignature(doc, TestCaseFolder, TestCase_X_X_1); 
    }
}
