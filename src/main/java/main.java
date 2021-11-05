// Future work
// - use existing namespace and vocabulary (IRI) to better represent ontology

import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import java.io.IOException;


public class main {
    public static void main(String[] args) throws IOException {
        String filename = "graphStatements.ttl";
        String nameSpace = SPR.NAMESPACE;
        String prefix = SPR.PREFIX;

        KGModelWrapper kgModelWrapper = KGModelWrapper.createInstance(filename, nameSpace, prefix);

        kgModelWrapper.addStatementToModel("Location", RDFS.SUBCLASSOF, prefix + ":" + "SwantonPacificRanchKnowledge");
        kgModelWrapper.addStatementToModel("Location", RDFS.LABEL, "Locations relevant to Swanton Pacific Ranch");
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", RDF.TYPE, prefix+ ":" + "Location");
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", SPR.IS, "a 3,200-acre ranch");

        kgModelWrapper.printStatements();
        kgModelWrapper.writeModel("graphStatements.ttl");
    }
}

