package xades4j.interop.plugtests.crossverification;

import static org.junit.Assert.*;
import xades4j.interop.plugtests.VerificationTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import xades4j.interop.plugtests.VerificationTest;
import xades4j.properties.SignatureTimeStampProperty;
import xades4j.verification.XAdESForm;
import xades4j.verification.XAdESVerificationResult;

/**
 *
 * @author luis
 */
@RunWith(Parameterized.class)
public class B_T_Ver extends VerificationTest {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() throws Exception {
        return folder(B_T_Gen.TestCaseFolder)
                .test(B_T_Gen.TestCase_X_B_T_1, r -> r
                        .validate(B_T_Ver::X_B_T_1))
                .test(B_T_Gen.TestCase_X_B_T_2, r -> r
                        .validate(B_T_Ver::X_B_T_2))
                .build();
    }

    public B_T_Ver(VerificationTestCase testCase) {
        super(testCase, XAdESForm.T);
    }
    
    private static void X_B_T_1(XAdESVerificationResult res){
        B_B_Ver.ensureBasicProfile(res);
    }
    
    private static void X_B_T_2(XAdESVerificationResult res){
        B_B_Ver.ensureBasicProfile(res);
        assertEquals("Expected 2 SignatureTimeStamp", 2, res.getPropertiesFilter().getOfType(SignatureTimeStampProperty.class).size());
    }
}
