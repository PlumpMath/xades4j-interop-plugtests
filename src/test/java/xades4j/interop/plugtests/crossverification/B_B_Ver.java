package xades4j.interop.plugtests.crossverification;

import xades4j.interop.plugtests.VerificationTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;
import xades4j.interop.plugtests.VerificationTest;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.DataObjectFormatProperty;
import xades4j.properties.SignatureProductionPlaceProperty;
import xades4j.properties.SignerRoleProperty;
import xades4j.properties.SigningTimeProperty;
import xades4j.verification.XAdESForm;
import xades4j.verification.XAdESVerificationResult;

/**
 *
 * @author luis
 */
@RunWith(Parameterized.class)
public class B_B_Ver extends VerificationTest {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() throws Exception {
        return folder(B_B_Gen.TestCaseFolder)
                .test(B_B_Gen.TestCase_X_B_B_1, r -> r
                    .validate(B_B_Ver::X_B_B_1))
                .test(B_B_Gen.TestCase_X_B_B_2, r -> r
                    .validate(B_B_Ver::X_B_B_2))
                .test(B_B_Gen.TestCase_X_B_B_3, r -> r
                    .validate(B_B_Ver::X_B_B_3))
                .test(B_B_Gen.TestCase_X_B_B_4, r -> r
                        .skipOnPropUnmarshalError(SignerRoleProperty.PROP_NAME)
                        .validate(B_B_Ver::X_B_B_4))
                .test(B_B_Gen.TestCase_X_B_B_6)
                .test(B_B_Gen.TestCase_X_B_B_9)
                .build();
    }

    public B_B_Ver(VerificationTestCase testCase) {
        super(testCase, XAdESForm.BES, XAdESForm.EPES);
    }
    
    public static void ensureBasicProfile(XAdESVerificationResult res){
        assertEquals("No SigningTime", 1, res.getPropertiesFilter().getOfType(SigningTimeProperty.class).size());
        assertTrue("No signed data objects", res.getSignedDataObjects().size() > 0);
        for (DataObjectDesc obj : res.getSignedDataObjects()) {
            assertTrue("No DataObjectFormat", obj.getSignedDataObjProps()
                    .stream()
                    .anyMatch(p -> p.getClass() == DataObjectFormatProperty.class));
        }
    }
    
    private static void X_B_B_1(XAdESVerificationResult res){
        ensureBasicProfile(res);
    }
    
    private static void X_B_B_2(XAdESVerificationResult res){
        ensureBasicProfile(res);
        assertTrue("Expected 2 signed data objects", res.getSignedDataObjects().size() >= 2);
    }
    
    private static void X_B_B_3(XAdESVerificationResult res){
        ensureBasicProfile(res);
        assertEquals("No SignatureProductionPlace", 1, res.getPropertiesFilter().getOfType(SignatureProductionPlaceProperty.class).size());
    }
    
    private static void X_B_B_4(XAdESVerificationResult res){
        ensureBasicProfile(res);
        assertEquals("No SignerRoleProperty", 1, res.getPropertiesFilter().getOfType(SignerRoleProperty.class).size());
    }
}
