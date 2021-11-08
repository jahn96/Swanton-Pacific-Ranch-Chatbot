import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.*;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.util.List;

public class SwantonKnowledgeGraph {
    private final Repository db;
    private static SwantonKnowledgeGraph instance = null;

    public static SwantonKnowledgeGraph createKnowledgeGraph(Model model) {
        if (instance == null) {
            instance = new SwantonKnowledgeGraph(model);
        }
        return instance;
    }

    private SwantonKnowledgeGraph(Model model) {
        db = new SailRepository(new MemoryStore());
        addModelToDB(model);
    }

    private void addModelToDB(Model model) {
        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
              // add the model
              conn.add(model);

              // let's check that our data is actually in the database
              try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)) {
                    for (Statement st: result) {
                          System.out.println("db contains: " + st);
                    }
              }
        }
    }

    public void printStatements() {
        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            try (RepositoryResult<Statement> result = conn.getStatements(null, null, null)){
                for (Statement st: result) {
                    System.out.println("db contains: " + st);
                }
            }
        }
    }

    public List<BindingSet> queryStatements(String queryString) {
        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            TupleQuery query = conn.prepareTupleQuery(queryString);
            return QueryResults.asList(query.evaluate());
        }
    }

    public void constructStatements(String constString) {
        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            GraphQuery query = conn.prepareGraphQuery(constString);

            try (GraphQueryResult result = query.evaluate()) {
                // just iterate over all solutions in the result
                for (Statement st: result) {
                    System.out.println("db contains: " + st);
                }
            }
        }
    }

    public void closeKnowledgeGraph() {
        db.shutDown();
    }
}
