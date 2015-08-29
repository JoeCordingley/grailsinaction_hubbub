package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.web.mapping.UrlMappings
import spock.lang.*

/**
 * Created by joe on 18/07/15.
 */
@TestFor(UrlMappings)
@Mock(PostController)
class UrlMappingsSpec extends Specification {

    def "Ensure basic mapping operations for user permalink" () {

        expect:
        assertForwardUrlMapping(url, controller: expectCtrl, action: expectAction) {
            id = expectId
        }

        where:
        url                      | expectCtrl | expectAction | expectId
        '/users/glen'            | 'post'     | 'timeline'   | 'glen'
        '/timeline/chuck_norris' | 'post'     | 'timeline'   | 'chuck_norris'
    }
}
