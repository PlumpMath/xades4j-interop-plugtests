package xades4j.interop.plugtests;

import java.security.cert.X509Certificate;
import java.util.List;
import xades4j.providers.impl.KeyStoreKeyingDataProvider;

/**
 *
 * @author Luís
 */
public class FirstCertificateSelector implements KeyStoreKeyingDataProvider.SigningCertSelector {
    
    public static KeyStoreKeyingDataProvider.SigningCertSelector instance = new FirstCertificateSelector();
    
    @Override
    public X509Certificate selectCertificate(List<X509Certificate> availableCertificates) {
        return availableCertificates.get(0);
    }
}
