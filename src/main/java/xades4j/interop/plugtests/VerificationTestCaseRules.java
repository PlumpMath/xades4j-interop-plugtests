package xades4j.interop.plugtests;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;
import xades4j.verification.XAdESVerificationResult;

/**
 *
 * @author luis
 */
public class VerificationTestCaseRules {
    public final Set<String> propsToSkipTestOnUnmarshalError;
    public final Collection<Consumer<XAdESVerificationResult>> extraValidations;

    public VerificationTestCaseRules() {
        this.propsToSkipTestOnUnmarshalError = new HashSet<>(2);
        this.extraValidations = new LinkedList<>();
    }
    
    public VerificationTestCaseRules skipOnPropUnmarshalError(String propName){
        this.propsToSkipTestOnUnmarshalError.add(propName);
        return this;
    }
    
    public VerificationTestCaseRules validate(Consumer<XAdESVerificationResult> validation){
        this.extraValidations.add(validation);
        return this;
    }
}
