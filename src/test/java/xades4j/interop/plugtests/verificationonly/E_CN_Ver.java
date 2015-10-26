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
public class E_CN_Ver extends VerificationTest {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() throws Exception {
        return folder("XAdES-E-CN.SCUN")
                // CompleteCertificateRefs does not reference the complete cert paht
                .test("X-CN-1")
                .test("X-CN-2")
                .build();
    }

    public E_CN_Ver(VerificationTestCase testCase) {
        super(testCase, XAdESForm.C);
    }
}
