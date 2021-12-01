# Swanton Pacific Ranch Chatbot - CSC580

This repository contains minimal Knowledge Graph Question Answering system to handle Swanton Pacific Ranch related questions and ParAI model to handle conversational questions. 

Knowledge Graph Database: [Ontotext GraphDB](https://www.ontotext.com/products/graphdb/graphdb-free/)

Currently, we are using a free version of the GraphDB. Therefore, it's not hosted on the server.

## Dependencies
1. [rdf4j](https://rdf4j.org/documentation/) library (3.7.3) to add or delete data from the database.
2. [Maven](https://maven.apache.org/) as a build and dependency management tool.
    - Maven works really well with [Intellij](https://www.jetbrains.com/idea/). So, I recommend to use Intellij for your IDE.
3. [ParlAI](https://parl.ai/docs/tutorial_quick.html#install) should be installed via pip.

## Usage
**To update the data in the knowledge graph with our wrapper classes,**
1. Export graph as a Turtle file.
2. Pass its filename to `KGModelWrapper` as an argument, make changes to the wrapper, and write the model to a Turtle file.
   For example,
```
   KGModelWrapper kgModelWrapper = KGModelWrapper.createInstance(nameSpace, prefix, filename);
   kgModelWrapper.addStatementToModel("Location", RDF.TYPE, OWL.CLASS);
   kgModelWrapper.writeModel("testStatements.ttl");
```
3. Import the output Turtle file to the knowledge graph using its workbench.

**To execute a query in the knowledge graph with our wrapper classes,**
1. Create a Swanton knowledge graph instance with a model from KgModelWrapper.
```
Model model = kgModelWrapper.getKgModel();
SwantonKnowledgeGraph swantonKg = SwantonKnowledgeGraph.createKnowledgeGraph(model);
```
2. Write a query and execute the query.
```
queryString = "PREFIX spr:<http://swantonpacificranch.org/> \n";
queryString += "SELECT ?name WHERE { \n";
queryString += "\tspr:Swanton_Pacific_Ranch spr:owned_by ?name";
queryString += "}";

swantonKg.queryStatements(queryString);
```
3. queryStatements() return list of BindingSet. Each BindingSet should have a value with the name used in the executed query. In the above case, `name`.
```
List<BindingSet> result = swantonKg.queryStatements(queryString);

for (BindingSet solution: result) {
   System.out.println(solution.getValue("name"));
}
```

## TODO
Most of our future work belongs to the Knowledge Graph Question Answering system. 
- [ ] Better design the knowledge graph schema with a class hierarchy such as “Swanton Pacific Ranch is a subclass of location” so that we can see the relationship between nodes with graph visualization. 
- [ ] Add more rules to the KG extraction system to turn the massive amount of data from documents into KG triplets more accurately.
- [ ] Come up with a logical way to handle complex questions that require multiple hops in the knowledge graph. Since it’s hard to automatically generate queries with multi-hops, we might need to use QA pairs as an alternative to handle those questions. 
- [ ] Gather more question - query pairs to either use them as a template or train a neural semantic parser.
- [ ] make custom ParlAI model recognized globally
