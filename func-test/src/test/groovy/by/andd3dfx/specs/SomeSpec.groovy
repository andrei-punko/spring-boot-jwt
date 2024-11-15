package by.andd3dfx.specs

import spock.lang.Specification

import static by.andd3dfx.configs.Configuration.authRestClient
import static by.andd3dfx.configs.Configuration.client
import static by.andd3dfx.configs.Configuration.resourceRestClient
import static by.andd3dfx.configs.Configuration.secret

class SomeSpec extends Specification {

    def 'Generate access token'() {
        when: 'generate access token'
        def authResponse = generateToken()

        then: 'server returns 200 code (ok)'
        assert authResponse.status == 200
        and: 'access_token present'
        assert authResponse.responseData.access_token != null
        and: 'refresh_token present'
        assert authResponse.responseData.refresh_token != null
        and: 'token_type is bearer'
        assert authResponse.responseData.token_type == 'bearer'
    }

    Object generateToken() {
        authRestClient.auth.basic client, secret
        return authRestClient.post(
                path: '/oauth/token',
                body: [
                        grant_type: 'password',
                        username  : 'john.doe',
                        password  : 'jwtpass'
                ],
                requestContentType: 'application/x-www-form-urlencoded'
        )
    }

    def 'Generate access token & check it'() {
        when: 'generate access token'
        def authResponse = generateToken()
        String access_token = authResponse.responseData.access_token
        and: 'check generated token'
        def checkTokenResponse = checkToken(access_token)

        then: 'server returns 200 code (ok)'
        assert checkTokenResponse.status == 200
        and: 'right aud present'
        assert checkTokenResponse.responseData.aud == ['testjwtresourceid']
        and: 'right user_name present'
        assert checkTokenResponse.responseData.user_name == 'john.doe'
        and: 'right scope present'
        assert checkTokenResponse.responseData.scope == ['read,write,foo']
        and: 'right authorities present'
        assert checkTokenResponse.responseData.authorities == ["STANDARD_USER"]
    }

    Object checkToken(String accessToken) {
        authRestClient.auth.basic client, secret
        return authRestClient.post(
                path: '/oauth/check_token',
                query: [
                        token: accessToken,
                ],
        )
    }

    def 'Generate token & refresh it'() {
        when: 'generate access token'
        def authResponse = generateToken()
        String access_token = authResponse.responseData.access_token
        String refresh_token = authResponse.responseData.refresh_token
        and: 'check generated token'
        def refreshTokenResponse = refreshToken(refresh_token)

        then: 'server returns 200 code (ok)'
        assert refreshTokenResponse.status == 200
        and: 'access_token present'
        assert refreshTokenResponse.responseData.access_token != null
        and: 'access_token changed'
        assert refreshTokenResponse.responseData.access_token != access_token
        and: 'refresh_token present'
        assert refreshTokenResponse.responseData.refresh_token != null
        and: 'refresh_token changed'
        assert refreshTokenResponse.responseData.refresh_token != refresh_token
        and: 'token_type is bearer'
        assert refreshTokenResponse.responseData.token_type == 'bearer'
    }

    Object refreshToken(String refreshToken) {
        authRestClient.auth.basic client, secret
        return authRestClient.post(
                path: '/oauth/token',
                body: [
                        grant_type   : 'refresh_token',
                        refresh_token: refreshToken,
                ],
                requestContentType: 'application/x-www-form-urlencoded'
        )
    }

    def 'Get access to resource using token'() {
        when: 'generate token'
        def authResponse = generateToken()
        String access_token = authResponse.responseData.access_token

        and: 'get access to resource using token'
        resourceRestClient.headers['Authorization'] = 'Bearer ' + access_token
        def getResponse = resourceRestClient.get(
                path: '/foos/1',
        )

        then: 'server returns 200 code (ok)'
        assert getResponse.status == 200
    }
}
