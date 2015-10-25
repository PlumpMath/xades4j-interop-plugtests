package xades4j.interop.plugtests.crossverification;

import org.junit.Test;
import org.w3c.dom.Document;
import xades4j.interop.plugtests.GenerationTest;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesSigner;
import xades4j.properties.DataObjectDesc;

/**
 *
 * @author luis
 */
public class E_BES_Gen extends GenerationTest {

    public static final String TestCaseFolder = "XAdES-E-BES.SCOK";
    public static final String TestCase_X_BES_3 = "X-BES-3";
    
    @Test
    public void X_BES_3() throws Exception {
        Document doc = getNewDocument();
        
        XadesSigner signer = newBes()
                .withSignaturePropertiesProvider(super.noOpSigPropsProvider)
                .newSigner();
        
        DataObjectDesc obj = getSignedDataObject1AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj), doc);
        
        outputSignature(doc, TestCaseFolder, TestCase_X_BES_3);
    }
}
