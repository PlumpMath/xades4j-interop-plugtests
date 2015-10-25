package xades4j.interop.plugtests.crossverification;

import org.junit.Test;
import org.w3c.dom.Document;
import xades4j.interop.plugtests.GenerationTest;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesBesSigningProfile;
import xades4j.production.XadesSigner;
import xades4j.properties.CommitmentTypeProperty;
import xades4j.properties.CounterSignatureProperty;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.SignatureProductionPlaceProperty;
import xades4j.properties.SignerRoleProperty;
import xades4j.properties.SigningTimeProperty;
import xades4j.providers.SignaturePropertiesCollector;
import xades4j.providers.SignaturePropertiesProvider;

/**
 *
 * @author luis
 */
public class B_B_Gen extends GenerationTest {

    public static final String TestCaseFolder = "XAdES-B-B.SCOK";
    public static final String TestCase_X_B_B_1 = "X-B-B-1";
    public static final String TestCase_X_B_B_2 = "X-B-B-2";
    public static final String TestCase_X_B_B_3 = "X-B-B-3";
    public static final String TestCase_X_B_B_4 = "X-B-B-4";
    public static final String TestCase_X_B_B_6 = "X-B-B-6";
    public static final String TestCase_X_B_B_9 = "X-B-B-9";

    protected final SignaturePropertiesProvider timeAndPlaceSigPropsProvider = (SignaturePropertiesCollector spc) -> {
        spc.setSigningTime(new SigningTimeProperty());
        spc.setSignatureProductionPlace(new SignatureProductionPlaceProperty("Sophia Antipolis", null));
    };

    @Test
    public void X_B_B_1() throws Exception {
        Document doc = getNewDocument();

        XadesSigner signer = newBes().newSigner();

        DataObjectDesc obj = getSignedDataObject1AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj), doc);

        outputSignature(doc, TestCaseFolder, TestCase_X_B_B_1);
    }

    @Test
    public void X_B_B_2() throws Exception {
        Document doc = getNewDocument();

        XadesSigner signer = newBes().newSigner();

        DataObjectDesc obj1 = getSignedDataObject1AsEnveloped(doc);
        DataObjectDesc obj2 = getSignedDataObject2AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj1, obj2), doc);

        outputSignature(doc, TestCaseFolder, TestCase_X_B_B_2);
    }

    @Test
    public void X_B_B_3() throws Exception {
        Document doc = getNewDocument();

        XadesSigner signer = newBes()
                .withSignaturePropertiesProvider(timeAndPlaceSigPropsProvider)
                .newSigner();

        DataObjectDesc obj = getSignedDataObject1AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj), doc);

        outputSignature(doc, TestCaseFolder, TestCase_X_B_B_3);
    }

    @Test
    public void X_B_B_4() throws Exception {
        Document doc = getNewDocument();

        XadesSigner signer = newBes()
                .withSignaturePropertiesProvider((SignaturePropertiesCollector spc) -> {
                    spc.setSigningTime(new SigningTimeProperty());
                    spc.setSignatureProductionPlace(new SignatureProductionPlaceProperty("Sophia Antipolis", null));
                    spc.setSignerRole(new SignerRoleProperty("ExecutiveDirector", "ShareHolder"));
                })
                .newSigner();

        DataObjectDesc obj = getSignedDataObject1AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj), doc);

        outputSignature(doc, TestCaseFolder, TestCase_X_B_B_4);
    }

    @Test
    public void X_B_B_6() throws Exception {
        Document doc = getNewDocument();

        XadesSigner signer = newBes()
                .withSignaturePropertiesProvider(timeAndPlaceSigPropsProvider)
                .newSigner();

        DataObjectDesc obj1 = getSignedDataObject1AsEnveloped(doc)
                .withCommitmentType(CommitmentTypeProperty.proofOfOrigin());
        DataObjectDesc obj2 = getSignedDataObject3AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj1, obj2), doc);

        outputSignature(doc, TestCaseFolder, TestCase_X_B_B_6);
    }

    @Test
    public void X_B_B_9() throws Exception {
        Document doc = getNewDocument();

        XadesSigner counterSigner = newBes()
                .withSignaturePropertiesProvider(super.noOpSigPropsProvider)
                .newSigner();

        XadesSigner signer = new XadesBesSigningProfile(super.keyingDataProvider)
                .withSignaturePropertiesProvider((SignaturePropertiesCollector spc) -> {
                    spc.setSigningTime(new SigningTimeProperty());
                    spc.setSignatureProductionPlace(new SignatureProductionPlaceProperty("Sophia Antipolis", null));
                    spc.addCounterSignature(new CounterSignatureProperty(counterSigner));
                })
                .newSigner();

        DataObjectDesc obj = getSignedDataObject1AsEnveloped(doc);
        signer.sign(new SignedDataObjects(obj), doc);

        outputSignature(doc, TestCaseFolder, TestCase_X_B_B_9);
    }
}
