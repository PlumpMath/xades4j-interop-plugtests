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
public class E_C_Gen extends GenerationTest {

    public static final String TestCaseFolder = "XAdES-E-C.SCOK";
    public static final String TestCase_X_C_1 = "X-C-1";
    
    @Test
    public void X_C_1() throws Exception {
        Document doc = getNewDocument();
        
        XadesSigner signer = newC().newSigner();
        
        DataObjectDesc obj = getSignedDataObject1AsEnvelopedNoProps(doc);
        signer.sign(new SignedDataObjects(obj), doc);
        
        outputSignature(doc, TestCaseFolder, TestCase_X_C_1); 
    }
}
