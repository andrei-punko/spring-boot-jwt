package by.andd3dfx.configs

import groovyx.net.http.RESTClient

class Configuration {
    private static final String host = "localhost"
    private static final String authServiceUrl = "http://$host:9090"
    public static final RESTClient authRestClient = new RESTClient(authServiceUrl)
    public static final String client = 'testjwtclientid'
    public static final String secret = 'XY7kmzoNzl100'

    private static final String resourceServiceUrl = "http://$host:9091"
    public static final RESTClient resourceRestClient = new RESTClient(resourceServiceUrl)
}
