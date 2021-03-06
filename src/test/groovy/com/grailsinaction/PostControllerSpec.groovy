package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PostController)
@Mock([User,Post])
class PostControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Get a user's timeline given their id"() {
        given: "A user with posts in the db"
        User chuck = new User(loginId: "chuck_norris", password: "password")
        chuck.addToPosts(new Post(content: "A first post"))
        chuck.addToPosts(new Post(content: "A second post"))
        chuck.save(failOnError: true)

        and: "A loginId parameter"
        params.id = chuck.loginId

        when: "the timeline is invoked"
        def model = controller.timeline()

        then: "the user is in the returned model"
        model.user.loginId == "chuck_norris"
        model.user.posts.size() == 2
    }

    def "Check that non-existent users are handled with an error"() {
        given: "The id of a non-existent user"
        params.id = "this-user-id-does-not-exist"

        when: "the timeline is invoked"
        controller.timeline()

        then: "a 404 is sent to the browser"
        response.status == 404
    }
    /*def "Adding a valid new post to the timeline"(){
        given: "A  user with posts in the db"
        User chuck = new User(
                loginId: "chuck_norris",
                password: "password"
        ).save(failOnError: true)

        and: "A login parameter"
        params.id = chuck.loginId

        and: "Some content for the post"
        params.content = "Chuck Norris can usnit test " +
                "entire applications with a single assert"

        when: "addPost is invoked"
        def model = controller.addPost()

        then: "our flash message and redirect confirms the success"
        flash.message == "Successfully created Post"
        response.redirectedUrl == "/post/timeline/${chuck.loginId}"
        Post.countByUser(chuck) ==1

    }*/
    def "Adding a valid new post to the timeline"(){
        given: "a mock post service"
        def mockPostService = Mock(PostService)
        1 * mockPostService.createPost(_,_) >> new Post(content: "Mock Post")
        controller.postService = mockPostService

        when: "controller is invoked"
        def result = controller.addPost( "joe_cool", "Posting up a storm")

        then: "redirected to timeline, flas message tells us all is well"
        flash.message ==~ /Added new post: Mock.*/
        response.redirectedUrl == '/post/timeline/joe_cool'
    }
    def "Adding an empty post to the timeline"(){
        given: "A  user with posts in the db"
        User chuck = new User(
                loginId: "chuck_norris",
                password: "password"
        ).save(failOnError: true)

        and: "A login parameter"
        params.id = chuck.loginId

        and: "An empty post"
        params.content = ""

        when: "addPost is invoked"
        def model = controller.addPost()

        then: "our flash message and redirect confirms the failure"
        flash.message == "Invalid or empty post"
        response.redirectedUrl == "/post/timeline/${chuck.loginId}"
        Post.countByUser(chuck) ==0


    }
    @Unroll
    def "Testing id of #suppliedId redirects to #expectedUrl"(){

        given:
        params.id = suppliedId

        when: "Controller is invoked"
        controller.home()

        then:
        response.redirectedUrl == expectedUrl

        where:
        suppliedId | expectedUrl
        'joe_cool' | '/post/timeline/joe_cool'
        null | '/post/timeline/chuck_norris'
    }
}
