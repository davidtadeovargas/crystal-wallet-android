package cy.agorise.crystalwallet.apigenerator;

/**
 * Created by henry on 27/9/2017.
 */

public interface ApiRequestListener {

    public void success(Object answer, int idPetition);
    public void fail(int idPetition);

}
