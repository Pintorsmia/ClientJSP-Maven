
package calcul;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Distance", targetNamespace = "http://calcul/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Distance {


    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.Double
     */
    @WebMethod(operationName = "Distance")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "Distance", targetNamespace = "http://calcul/", className = "calcul.Distance_Type")
    @ResponseWrapper(localName = "DistanceResponse", targetNamespace = "http://calcul/", className = "calcul.DistanceResponse")
    @Action(input = "http://calcul/Distance/DistanceRequest", output = "http://calcul/Distance/DistanceResponse")
    public Double distance(
        @WebParam(name = "arg0", targetNamespace = "")
        Double arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        Double arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        Double arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        Double arg3);

}
