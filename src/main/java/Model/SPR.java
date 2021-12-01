package Model;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class SPR {
    /**
     * The full namespace: "http://swantonpacificranch.org/".
     */
    public static final String NAMESPACE = "http://swantonpacificranch.org/";

    /**
     * The prefix usually used for this vocabulary: 'spr'.
     */
    public static final String PREFIX = "spr";

    /**
     * The <code>spr:is</code> property.
     */
    public static final IRI IS = getIRI("is");

    /**
     * The <code>spr:in</code> class.
     */
    public static final IRI IN = getIRI("in");

    /**
     * Creates a new {@link IRI} with this vocabulary's namespace for the given local name.
     *
     * @param localName a local name of an IRI, e.g. 'creatorOf', 'name', 'Artist', etc.
     * @return an IRI using the http://example.org/ namespace and the given local name.
     */
    public static IRI getIRI(String localName) {
        return SimpleValueFactory.getInstance().createIRI(NAMESPACE, localName);
    }
}
