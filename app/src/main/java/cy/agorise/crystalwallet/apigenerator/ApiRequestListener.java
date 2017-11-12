package cy.agorise.crystalwallet.apigenerator;

/**
 * This listener and apirquest answer
 *
 * Created by henry on 27/9/2017.
 */

public interface ApiRequestListener {

    /**
     * Call when the function returns successfully
     * @param answer The answer, this object depends on the kind of request is made to the api
     * @param idPetition the id of the ApiRequest petition
     */
    public void success(Object answer, int idPetition);

    /**
     * Call when the function fails
     * @param idPetition the id of the ApiRequest petition
     */
    public void fail(int idPetition);

}
