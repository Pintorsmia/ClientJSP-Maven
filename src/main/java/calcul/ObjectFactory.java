
package calcul;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the calcul package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DistanceResponse_QNAME = new QName("http://calcul/", "DistanceResponse");
    private final static QName _Distance_QNAME = new QName("http://calcul/", "Distance");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: calcul
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DistanceResponse }
     * 
     */
    public DistanceResponse createDistanceResponse() {
        return new DistanceResponse();
    }

    /**
     * Create an instance of {@link Distance_Type }
     * 
     */
    public Distance_Type createDistance_Type() {
        return new Distance_Type();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DistanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calcul/", name = "DistanceResponse")
    public JAXBElement<DistanceResponse> createDistanceResponse(DistanceResponse value) {
        return new JAXBElement<DistanceResponse>(_DistanceResponse_QNAME, DistanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Distance_Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://calcul/", name = "Distance")
    public JAXBElement<Distance_Type> createDistance(Distance_Type value) {
        return new JAXBElement<Distance_Type>(_Distance_QNAME, Distance_Type.class, null, value);
    }

}
