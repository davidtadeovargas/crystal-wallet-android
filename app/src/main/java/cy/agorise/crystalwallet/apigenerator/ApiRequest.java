package cy.agorise.crystalwallet.apigenerator;

/**
 * Created by henry on 27/9/2017.
 */

public class ApiRequest {

    int id;

    ApiRequestListener listener;

    public ApiRequest(int id, ApiRequestListener listener) {
        this.id = id;
        this.listener = listener;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ApiRequestListener getListener() {
        return listener;
    }

    public void setListener(ApiRequestListener listener) {
        this.listener = listener;
    }
}
