package cy.agorise.crystalwallet.network;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import cy.agorise.crystalwallet.apigenerator.GrapheneApiGenerator;
import cy.agorise.crystalwallet.enums.CryptoNet;

/**
 * Created by henry on 6/3/2018.
 */

public abstract class CryptoNetManager {
    /**
     * This map contains the list of the urls to be tested
     */
    private static HashMap<CryptoNet,HashSet<String>> CryptoNetURLs = new  HashMap<>();

    /**
     * This map contains the list of urls been tested and ordered by the fastests
     */
    private static HashMap<CryptoNet,ArrayList<TestedURL>> TestedURLs = new  HashMap<>();

    public static String getURL(CryptoNet crypto){
        return CryptoNetManager.getURL(crypto,0);
    }


    public static String getURL(CryptoNet crypto, int index){
        if(TestedURLs.containsKey(crypto) && TestedURLs.get(crypto).size()>index){
            return TestedURLs.get(crypto).get(index).getUrl();
        }

        if(CryptoNetURLs.containsKey(crypto) && !CryptoNetURLs.get(crypto).isEmpty()){
            return CryptoNetURLs.get(crypto).iterator().next();
        }
        return null;
    }

    public static int getURLSize(CryptoNet crypto){
        if(TestedURLs.containsKey(crypto)){
            return TestedURLs.get(crypto).size();
        }
        return 0;
    }

    public static void addCryptoNetURL(CryptoNet crypto, String url){
        if(!CryptoNetURLs.containsKey(crypto)){
            CryptoNetURLs.put(crypto,new HashSet<String>());
        }

        CryptoNetURLs.get(crypto).add(url);
        CryptoNetVerifier verifier = CryptoNetVerifier.getNetworkVerify(crypto);
        if(verifier != null) {
            verifier.checkURL(url);
        }
    }


    /*
    * Add url with listener on error only for sockets
    * */
    public static void addCryptoNetURL(CryptoNet crypto,
                                       String[] urls,
                                       final Activity activity,
                                       final GrapheneApiGenerator.OnErrorWebSocket onErrorWebSocker,
                                       final boolean showNormalError){
        addCryptoNetURL(crypto,urls,activity,onErrorWebSocker,null,showNormalError);
    }


    /*
     * Add url with listener on error and response for sockets
     * */
    public static void addCryptoNetURL(CryptoNet crypto,
                                       String[] urls,
                                       final Activity activity,
                                       final GrapheneApiGenerator.OnResponsesWebSocket onResponsesWebSocket,
                                       final boolean showNormalError){
        addCryptoNetURL(crypto,urls,activity,null,onResponsesWebSocket,showNormalError);
    }


    /*
    * Utility for above methods
    *
    * */
    public static void addCryptoNetURL(CryptoNet crypto,
                                               String[] urls,
                                               final Activity activity,
                                               final GrapheneApiGenerator.OnErrorWebSocket onErrorWebSocker,
                                               final GrapheneApiGenerator.OnResponsesWebSocket onResponsesWebSocket,
                                               final boolean showNormalError) {

        if (!CryptoNetURLs.containsKey(crypto)) {
            CryptoNetURLs.put(crypto, new HashSet<String>());
        }

        CryptoNetVerifier verifier = CryptoNetVerifier.getNetworkVerify(crypto);
        verifier.getThread().setActivity(activity); //Logical connection to ui
        verifier.getThread().setOnErrorWebSocker(onErrorWebSocker); //Connect response web socket error to ui response
        verifier.getThread().setOnResponsesWebSocket(onResponsesWebSocket); //Connect response and error web socket error to ui response
        verifier.getThread().setShowNormalMessage(showNormalError); //Not show native message error, we handle it

        for (String url : urls) {
            CryptoNetURLs.get(crypto).add(url);
            if (verifier != null) {
                verifier.checkURL(url);
            }
        }

    }



    public static void removeCryptoNetURL(CryptoNet crypto, String url){
        if(CryptoNetURLs.containsKey(crypto)){
            CryptoNetURLs.get(crypto).remove(url);
        }
    }

    public static void verifiedCryptoNetURL(CryptoNet crypto, String url, long time){
        if(CryptoNetURLs.containsKey(crypto) && CryptoNetURLs.get(crypto).contains(url)){
            if(!TestedURLs.containsKey(crypto)){
                TestedURLs.put(crypto,new ArrayList<TestedURL>());
            }
            TestedURL testedUrl = new TestedURL(time,url);
            if(!TestedURLs.get(crypto).contains(testedUrl)){
                TestedURLs.get(crypto).add(testedUrl);
                Collections.sort(TestedURLs.get(crypto));
            }
        }else{
            //TODO add error handler
        }
    }

    public static String getChaindId(CryptoNet crypto){
        CryptoNetVerifier verifier = CryptoNetVerifier.getNetworkVerify(crypto);
        if(verifier != null) {
            return verifier.getChainId();
        }
        return null;
    }

    private static class TestedURL implements Comparable{
        private long time;
        private String url;

        public TestedURL(long time, String url) {
            this.time = time;
            this.url = url;
        }

        public long getTime() {
            return time;
        }

        String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestedURL)) return false;

            TestedURL testedURL = (TestedURL) o;

            return getUrl().equals(testedURL.getUrl());
        }

        @Override
        public int hashCode() {
            return getUrl().hashCode();
        }

        @Override
        public int compareTo(@NonNull Object o) {
            if (this == o) return 0;
            if (!(o instanceof TestedURL)) return 0;

            TestedURL testedURL = (TestedURL) o;
            return (int) (this.getTime() - testedURL.getTime());
        }
    }
}
