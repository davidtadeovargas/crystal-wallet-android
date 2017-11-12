package cy.agorise.crystalwallet.apigenerator;

/**
 * This is a request to be passed to an api generator, when an answer is expected.
 *
 *
 * Created by henry on 27/9/2017.
 */

public class ApiRequest {
    /**
     * The id of this api request
     */
    int id;

    /**
     * The listener of this apirequest, to be passed the answer
     */
    ApiRequestListener listener;

    /**
     * Basic constructor
     * @param id The id of this request
     * @param listener The listener for this request
     */
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
