import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import KnowledgeGraph.SwantonKnowledgeGraph;

import Model.SPR;
import Model.KGModelWrapper;


public class main {
    public static void main(String[] args) throws IOException, ParseException {
        String filename = "testStatements.ttl";
        String nameSpace = SPR.NAMESPACE;
        String prefix = SPR.PREFIX;

        // Wraps a model based on data in the filename
//        KGModelWrapper kgModelWrapper = KGModelWrapper.createInstance(nameSpace, prefix, filename);
        // Wraps a model from scratch
        KGModelWrapper kgModelWrapper = KGModelWrapper.createInstance(nameSpace, prefix);

        // Add statements to the model
        // TODO: Design knowledge graph class structure and organize data with hierarchy
        kgModelWrapper.addStatementToModel("SwantonPacificRanchKnowledge", RDF.TYPE, OWL.CLASS);
        kgModelWrapper.addStatementToModel("Location", RDFS.SUBCLASSOF, SPR.getIRI("SwantonPacificRanchKnowledge"));
        kgModelWrapper.addStatementToModel("Location", RDFS.LABEL, "Locations relevant to Swanton Pacific Ranch");
        kgModelWrapper.addStatementToModel("Location", RDF.TYPE, OWL.CLASS);
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", RDF.TYPE, SPR.getIRI("Location"));
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", SPR.IS, SPR.getIRI("a_3200-acre_ranch"));
        kgModelWrapper.addStatementToModel("Swanton_Pacific_Ranch", SPR.getIRI("is"), SPR.getIRI("a_3200-acre_ranch"));
        kgModelWrapper.addStatementToModel("ALBERT_B_SMITH", RDFS.SUBCLASSOF, SPR.getIRI("SwantonPacificRanchKnowledge"));
        kgModelWrapper.addStatementToModel("ALBERT_B_SMITH", RDFS.LABEL, "Any information about Albert B. Smith");
        kgModelWrapper.addStatementToModel("ALBERT_B_SMITH", RDF.TYPE, OWL.CLASS);


        // TODO: Rank the hops to handle more complex questions
        // TODO: we should replace code below with triplets in triplets.txt that are automatically extracted from Extracting_triplets_for_Knowledge_graph.ipynb once we finish adding all rules and the output triplets are in a good shape.
        String[][] triplets = {{"a_3200-acre_ranch", "in", "Santa Cruz County"},
                                {"a_3200-acre_ranch", "outside", "the town"},
                                {"Swanton_Pacific_Ranch", "is", "a 3200-acre ranch"},
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

        // If you want to write data in the model to ttl file, uncomment the line below.
//        kgModelWrapper.writeModel("testStatements.ttl");

        // Based on a model, create a Swanton Knowledge Graph instance to execute query.
        // We created this Swanton Knowledge Graph class because the current GraphDB doesn't allow remote access to the database. You might not need the code below with the direct access to the knowledge graph.
        Model model = kgModelWrapper.getKgModel();
        SwantonKnowledgeGraph swantonKg = SwantonKnowledgeGraph.createKnowledgeGraph(model);

        // Below is the simple text interface of Knowledge Graph Question Answering System
        // 1. Given the input from the user
        // 2. Get its corresponding SPARQL query from semantic parser
        // 3. Execute the query in the knowledge graph and get data
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter Question");

        String question = myObj.nextLine().strip();  // Read user input

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
        String variable = queryString.split(" ")[1].substring(1);

        // Add prefix
        queryString = "PREFIX " + prefix + ": <" + nameSpace + "> \n" + queryString;

        List<BindingSet> result = swantonKg.queryStatements(queryString);

        for (BindingSet solution: result) {
            System.out.println(solution.getValue(variable));
        }

        swantonKg.closeKnowledgeGraph();
    }
}

