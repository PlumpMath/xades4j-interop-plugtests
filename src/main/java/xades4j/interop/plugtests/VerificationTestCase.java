package xades4j.interop.plugtests;

import java.io.File;

/**
 *
 * @author luis
 */
public class VerificationTestCase {

    public final String testCasesFolder;
    public final String name;
    public final String company;
    public final File sigFile;
    public final VerificationTestCaseRules rules;

    public VerificationTestCase(String testCasesFolder, String name, String company, File sigFile, VerificationTestCaseRules rules) {
        this.testCasesFolder = testCasesFolder;
        this.name = name;
        this.company = company;
        this.sigFile = sigFile;
        this.rules = rules;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", name, company);
    }
}
