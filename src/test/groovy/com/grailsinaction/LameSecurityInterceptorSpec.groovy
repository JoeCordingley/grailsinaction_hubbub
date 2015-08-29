package com.grailsinaction


import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(LameSecurityInterceptor)
class LameSecurityInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    @Unroll
    void "Test lameSecurity interceptor matching #action"() {
        when:"A request matches the interceptor"
            withRequest(controller:"post", action: action)

        then:"The interceptor does match"
            interceptor.doesMatch() == expected

        where:
        action | expected
        "addPost" | true
        "deletePost" | true
        "fakeAction" | false
    }

//    @Unroll
//    def "Test ImpersonateId" (){
//        given: "impersonateId is populated"
//        def params = [impersonateId: name]
//        when:
//        withRequest(controller: "post", action: "addPost")
//
//        then: "Oh I don't know"
//        (interceptor.getModel().session.user != null) == expected
//
//        where:
//        name | expected
//        "chuck_norris" | true
//        "nobody" | false
//    }

}
