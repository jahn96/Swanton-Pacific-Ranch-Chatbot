// Future work
// - use existing namespace and vocabulary (IRI) to better represent ontology

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class main {
    public static void main(String[] args) throws IOException, ParseException {
        String filename = "testStatements.ttl";
        String nameSpace = SPR.NAMESPACE;
        String prefix = SPR.PREFIX;

//        KGModelWrapper kgModelWrapper = KGModelWrapper.createInstance(nameSpace, prefix, filename);
        // Create a model from scratch
        KGModelWrapper kgModelWrapper = KGModelWrapper.createInstance(nameSpace, prefix);

        // Add statements to the model
        // Adding classes
        kgModelWrapper.addStatementToModel("SwantonPacificRanchKnowledge", RDF.TYPE, OWL.CLASS);
        kgModelWrapper.addStatementToModel("Location", RDFS.SUBCLASSOF, SPR.getIRI("SwantonPacificRanchKnowledge"));
        kgModelWrapper.addStatementToModel("Location", RDFS.LABEL, "Locations relevant to Swanton Pacific Ranch");
        kgModelWrapper.addStatementToModel("Location", RDF.TYPE, OWL.CLASS);
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", RDF.TYPE, SPR.getIRI("Location"));
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", SPR.IS, "a 3,200-acre ranch");
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", SPR.getIRI("is"), "a 3,200-acre ranch");
        kgModelWrapper.addStatementToModel("ALBERT_B_SMITH", RDFS.SUBCLASSOF, SPR.getIRI("SwantonPacificRanchKnowledge"));
        kgModelWrapper.addStatementToModel("ALBERT_B_SMITH", RDFS.LABEL, "Any information about Albert B. Smith");
        kgModelWrapper.addStatementToModel("ALBERT_B_SMITH", RDF.TYPE, OWL.CLASS);


        // TODO: Rank the hops to handle more complex questions
        // TODO: calculate the distance between the verb and object to filter mismatch
        String[][] triplets = {{"a_3,200-acre_ranch", "in", "Santa Cruz County"},
                                {"a_3,200-acre_ranch", "outside", "the town"},
                                {"Swanton_Pacific_Ranch", "is", "a 3,200-acre ranch"},
                                {"the_town", "of", "Davenport"},
                                {"Swanton_Pacific_Ranch", "has_part", "Santa Cruz County"},
                                {"Swanton_Pacific_Ranch", "operated_in", "sustainable agriculture"},
                                {"Swanton_Pacific_Ranch", "owned_by", "California Polytechnic State University"},
                                {"Swanton_Pacific_Ranch", "operated_by", "California Polytechnic State University"},
                                {"Swanton_Pacific_Ranch", "operated_for", "research"},
                                {"rangeland", "for", "the College"},
                                {"Swanton_Pacific_Ranch", "encompass", "rangeland"},
                                {"a_significant_part", "of", "the community"}};

        for(String[] triplet: triplets) {
            kgModelWrapper.addStatementToModel(triplet[0], SPR.getIRI(triplet[1]), triplet[2]);
        }
        
        kgModelWrapper.printStatements();
//        kgModelWrapper.writeModel("testStatements.ttl");

        Model model = kgModelWrapper.getKgModel();
        SwantonKnowledgeGraph swantonKg = SwantonKnowledgeGraph.createKnowledgeGraph(model);

//        swantonKg.printStatements();

        //TODO: Factor out this user interaction code to another repo

        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter Question");

        String question = myObj.nextLine();  // Read user input

        // Read template based Semantic Parser
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("src/SemanticParser/data.json"));
        JSONObject jsonObject = (JSONObject)obj;

        // getting pairs
        if (!jsonObject.containsKey(question.strip())) {
            System.out.println("Sorry, I don't have an answer to the question :(");
            return;
        }
        String queryString = jsonObject.get(question.strip()).toString();

        // Add prefix
        queryString = "PREFIX " + prefix + ": <" + nameSpace + "> \n" + queryString;

        // Sample query
//        queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
//        queryString += "SELECT ?name WHERE { \n";
//        queryString += "\tspr:Swanton_Pacific_Ranch spr:owned_by ?name";
//        queryString += "}";

        List<BindingSet> result = swantonKg.queryStatements(queryString);
        for (BindingSet solution: result) {
            System.out.println("?name = " + solution.getValue("name"));
        }

        swantonKg.closeKnowledgeGraph();
    }
}

