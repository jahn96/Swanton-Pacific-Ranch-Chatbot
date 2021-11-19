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
     * The <code>spr:outside</code> class.
     */
    public static final IRI OUTSIDE = getIRI("outside");

    /**
     * The <code>spr:of</code> class.
     */
    public static final IRI OF = getIRI("of");

    /**
     * The <code>spr:has part</code> class.
     */
    public static final IRI HASPART = getIRI("has part");

    /**
     * The <code>spr:owned and operated in</code> class.
     */
    public static final IRI OWNEDANDOPERATEDIN = getIRI("owned and operated in");

    /**
     * The <code>spr:owned and operated by</code> class.
     */
    public static final IRI OWNEDANDOPERATEDBY = getIRI("owned and operated by");

    /**
     * The <code>spr:owned and operated for</code> class.
     */
    public static final IRI OWNEDANDOPERATEDFOR = getIRI("owned and operated for");

    /**
     * The <code>spr:for</code> class.
     */
    public static final IRI FOR = getIRI("for");

    /**
     * The <code>spr:encompass</code> class.
     */
    public static final IRI ENCOMPASS = getIRI("encompass");


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
