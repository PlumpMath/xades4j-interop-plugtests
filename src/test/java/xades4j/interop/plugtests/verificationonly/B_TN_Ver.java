package xades4j.interop.plugtests.verificationonly;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import xades4j.interop.plugtests.VerificationTest;
import xades4j.interop.plugtests.VerificationTestCase;
import xades4j.verification.XAdESForm;

/**
 *
 * @author luis
 */
@RunWith(Parameterized.class)
public class B_TN_Ver extends VerificationTest {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() throws Exception {
        return folder("XAdES-B-TN.SCUN")
                // Signing certificate revoke on singature TS time
                .test("X-B-TN-1")
                .build();
    }

    public B_TN_Ver(VerificationTestCase testCase) {
        super(testCase, XAdESForm.T);
    }
}
