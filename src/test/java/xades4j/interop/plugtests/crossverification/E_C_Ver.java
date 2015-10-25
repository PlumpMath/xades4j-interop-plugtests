package xades4j.interop.plugtests.crossverification;

import xades4j.interop.plugtests.VerificationTestCase;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import xades4j.interop.plugtests.VerificationTest;
import xades4j.verification.XAdESForm;

/**
 *
 * @author luis
 */
@RunWith(Parameterized.class)
public class E_C_Ver extends VerificationTest {

    @Parameterized.Parameters(name = "{0}")
    public static Iterable<Object[]> data() throws Exception {
        return folder(E_C_Gen.TestCaseFolder)
                .test(E_C_Gen.TestCase_X_C_1)
                .build();
    }

    public E_C_Ver(VerificationTestCase testCase) {
        super(testCase, XAdESForm.C);
    }
}
