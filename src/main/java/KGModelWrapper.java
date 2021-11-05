/**
 * Some codes are from https://rdf4j.org/documentation/tutorials/getting-started/
 */

//import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.*;

/**
 * A wrapper class for a knowledge graph model
 */
public class KGModelWrapper {

    private static KGModelWrapper instance = null;
    private Model kgModel;
    private String nameSpace_;
    private String prefix_;

    /**
     * A construction method for Knowledge Graph Model
     * @param nameSpace namespace of an IRI
     * @param prefix prefix of an IRI
     * @param filename if filename is given, it will use the model. Otherwise, create a new one
     */
    public KGModelWrapper(String nameSpace, String prefix, String... filename) {
        try {
            if (filename.length != 0){
                kgModel = readFile(filename[0]);
            }
            else{
                kgModel = new TreeModel();
            }

            nameSpace_ = nameSpace;
            prefix_ = prefix;
        }
        catch (IOException e) {
            System.err.println("Error in reading the given file");
            e.printStackTrace();
        }
    }

    public static KGModelWrapper createInstance(String nameSpace, String prefix, String filename) {
        if(instance == null) {
            instance = new KGModelWrapper(nameSpace, prefix, filename);
        }
        return instance;
    }

    /**
     * Read a Turtle file and save it into a model
     * @param filename: name of a Turtle file that contains graph data
     * @return model that contains the data in the given file
     * @throws IOException
     */
    private Model readFile(String filename) throws IOException {
        // read the given file as an InputStream.
        InputStream input = new FileInputStream(filename);

        // Rio also accepts a java.io.Reader as input for the parser.
        return Rio.parse(input, "", RDFFormat.TURTLE);
    }

    /**
     *
     * @param subject
     * @param relation
     * @param object can be string literal or IRI object
     * @param <T>
     * @return
     */
    public <T>  void addStatementToModel (String subject, IRI relation, T object, String... language) {
        ModelBuilder modelBuilder = new ModelBuilder(kgModel);

        String lan = language.length > 0 ? language[0] : "en";
        if (object instanceof String) {
            modelBuilder
                    .setNamespace(prefix_, nameSpace_)
                    .add(prefix_ + ":" + subject, relation, Values.literal((String) object, lan));
        }
        else if (object instanceof IRI){
            modelBuilder
                    .setNamespace(prefix_, nameSpace_)
                    .add(prefix_ + ":" + subject, relation, object);
        }

        kgModel = modelBuilder.build();
    }

    public <T> void addStatementToModel (String subject, String relation, T object, String... language) {
        ModelBuilder modelBuilder = new ModelBuilder(kgModel);

        String lan = language.length > 0 ? language[0] : "en";
        if (object instanceof String) {
            modelBuilder
                    .setNamespace(prefix_, nameSpace_)
                    .add(prefix_ + ":" + subject, relation, Values.literal((String) object, lan));
        }
        else if (object instanceof IRI){
            modelBuilder
                    .setNamespace(prefix_, nameSpace_)
                    .add(prefix_ + ":" + subject, relation, object);
        }
        kgModel = modelBuilder.build();
    }

    public void addBlankNodeToModel (String subject, IRI relation, IRI[][] relationObjectPairs ) {
        BNode blankNode = Values.bnode();
        ModelBuilder modelBuilder = new ModelBuilder(kgModel);

        modelBuilder
                .setNamespace(prefix_, nameSpace_)
                .subject(prefix_ + ":" + subject)
                .add(relation, blankNode);

        for (IRI[] pair : relationObjectPairs) {
            modelBuilder.add(pair[0], pair[1]);
        }

        kgModel = modelBuilder.build();
    }

    public void removeStatement(Resource subject, IRI relation, Value object) {
        kgModel.remove(subject, relation, object);
    }

    /**
     * Write a model to a file in the Turtle syntax format
     * @param fileName name of a file to write
     * @throws IOException
     */
    public void writeModel(String fileName) throws IOException {
        FileWriter myWriter = new FileWriter(fileName);
        Rio.write(kgModel, myWriter, RDFFormat.TURTLE);
    }

    /**
     * Print statements in a model
     */
    public void printStatements() {
        for (Statement st : kgModel) {
            System.out.println(st);
        }
    }
}
