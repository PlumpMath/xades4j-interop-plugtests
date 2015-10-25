package xades4j.interop.plugtests.verificationonly;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import xades4j.interop.plugtests.crossverification.B_B_Ver;
import xades4j.interop.plugtests.VerificationTest;
import xades4j.interop.plugtests.VerificationTestCase;
import xades4j.verification.XAdESForm;
import xades4j.verification.XAdESVerificationResult;

/**
 *
 * @author luis
 */
@RunWith(Parameterized.class)
public class B_BN_Ver extends VerificationTest {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() throws Exception {
        return folder("XAdES-B-BN.SCUN")
                // No SigningTime
                .test("X-B-BN-1", r -> r
                        .validate(B_BN_Ver::X_B_BN_1))
                // No SigningCertificate
                .test("X-B-BN-2")
                // No DataObjectFormat
                .test("X-B-BN-3", r -> r
                        .validate(B_BN_Ver::X_B_BN_3))
                // Invalid signature value
                .test("X-B-BN-4")
                // Untrusted certificate
                .test("X-B-BN-5")
                // Expired certificate
                .test("X-B-BN-6")
                // Revoked certificate
                .test("X-B-BN-7")
                // Revoked CA certificate
                .test("X-B-BN-8")
                // Signing certificate doesn't match property
                .test("X-B-BN-9")
                .build();
    }

    public B_BN_Ver(VerificationTestCase testCase) {
        super(testCase, XAdESForm.BES);
    }
    
    private static void X_B_BN_1(XAdESVerificationResult res){
        B_B_Ver.ensureBasicProfile(res);
    }
    
    private static void X_B_BN_3(XAdESVerificationResult res){
        B_B_Ver.ensureBasicProfile(res);
    }
}
