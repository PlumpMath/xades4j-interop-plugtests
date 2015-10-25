package xades4j.interop.plugtests;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author luis
 */
public class TestBase {
    
    protected static final Path basePath = Paths.get("../xades-plugtests-2015");
    protected static final Path testCasesBasePath = basePath.resolve("plugtest_latest");

    protected static final Path dataFilesBasePath = TestBase.basePath.resolve("nop").resolve("nop");
    protected static final String dataFilesBaseUri = dataFilesBasePath.toUri().toString() + "/";
    
    protected final DocumentBuilder db;

    protected TestBase() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new NullPointerException("DocumentBuilder init failed:" + ex.getMessage());
        }
    }

    protected Document getNewDocument() throws Exception {
        return db.newDocument();
    }

    protected void outputDocument(Document doc, String testCaseFolder, String fileName) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        File outDir = testCasesBasePath.resolve(testCaseFolder).resolve("X4J").toFile();
        try (FileOutputStream out = new FileOutputStream(new File(outDir, fileName))) {
            tf.newTransformer().transform(new DOMSource(doc), new StreamResult(out));
        }
    }
}
