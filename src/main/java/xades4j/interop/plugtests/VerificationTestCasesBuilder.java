package xades4j.interop.plugtests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author luis
 */
public class VerificationTestCasesBuilder {

    private final Path testCasesFolder;
    private final Map<String, Function<VerificationTestCaseRules, VerificationTestCaseRules>> testCasesNames;

    public VerificationTestCasesBuilder(Path testCasesFolder) {
        this.testCasesFolder = testCasesFolder;
        this.testCasesNames = new HashMap<>();
    }

    public VerificationTestCasesBuilder test(String testCaseName) {
        return this.test(testCaseName, r -> r);
    }

    public VerificationTestCasesBuilder test(String testCaseName, Function<VerificationTestCaseRules, VerificationTestCaseRules> rules) {
        this.testCasesNames.put(testCaseName, rules);
        return this;
    }

    public Iterable<Object[]> build() throws Exception {
        Object[][] testCases = Files.list(this.testCasesFolder)
                .filter(f -> !f.endsWith("TestDefinitions"))
                .flatMap(f -> this.testCasesNames
                        .entrySet()
                        .stream()
                        .map(test -> createTestCase(f, test.getKey(), test.getValue())))
                .filter(tc -> tc.sigFile.exists())
                .sorted(Comparator.comparing((VerificationTestCase t) -> t.name).thenComparing(tc -> tc.company))
                .map(tc -> new Object[]{tc})
                .toArray(Object[][]::new);

        return Arrays.asList(testCases);
    }

    private VerificationTestCase createTestCase(Path f, String name, Function<VerificationTestCaseRules, VerificationTestCaseRules> rules) {
        return new VerificationTestCase(
                this.testCasesFolder.getFileName().toString(),
                name,
                f.getFileName().toString(),
                f.resolve("Signature-" + name + ".xml").toFile(),
                rules.apply(new VerificationTestCaseRules()));
    }
}
