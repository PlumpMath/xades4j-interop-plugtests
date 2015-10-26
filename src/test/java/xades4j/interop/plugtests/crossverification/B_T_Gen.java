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
public class B_T_Gen extends GenerationTest {

    public static final String TestCaseFolder = "XAdES-B-T.SCOK";
    public static final String TestCase_X_B_T_1 = "X-B-T-1";
    public static final String TestCase_X_B_T_2 = "X-B-T-2";

    @Test
    public void X_B_T_1() throws Exception {
        Document doc = getNewDocument();

        XadesSigner signer = newT().newSigner();

        DataObjectDesc obj = getSignedDataObject1AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj), doc);

        outputSignature(doc, TestCaseFolder, TestCase_X_B_T_1);
    }
}
