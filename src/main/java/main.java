// Future work
// - use existing namespace and vocabulary (IRI) to better represent ontology

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;

import java.io.IOException;
import java.util.List;


public class main {
    public static void main(String[] args) throws IOException {
        String filename = "graphStatements.ttl";
        String nameSpace = SPR.NAMESPACE;
        String prefix = SPR.PREFIX;

        KGModelWrapper kgModelWrapper = KGModelWrapper.createInstance(nameSpace, prefix, filename);

        kgModelWrapper.addStatementToModel("Location", RDFS.SUBCLASSOF, prefix + ":" + "SwantonPacificRanchKnowledge");
        kgModelWrapper.addStatementToModel("Location", RDFS.LABEL, "Locations relevant to Swanton Pacific Ranch");
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", RDF.TYPE, prefix+ ":" + "Location");
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", SPR.IS, "a 3,200-acre ranch");

        kgModelWrapper.printStatements();
        //        kgModelWrapper.writeModel("graphStatements.ttl");

        Model model = kgModelWrapper.getKgModel();
        SwantonKnowledgeGraph swantonKg = SwantonKnowledgeGraph.createKnowledgeGraph(model);

//        swantonKg.printStatements();

        // Sample query
        String queryString = "PREFIX " + prefix + ": <" + nameSpace + "> \n";
//        queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
        queryString += "SELECT ?s ?p WHERE { \n";
        queryString += "\t?s ?p \"3,200 acres\"";
        queryString += "}";

        List<BindingSet> result = swantonKg.queryStatements(queryString);
        for (BindingSet solution: result) {
            System.out.println("?s = " + solution.getValue("s"));
            System.out.println("?p = " + solution.getValue("p"));
        }

        swantonKg.closeKnowledgeGraph();
    }
}

