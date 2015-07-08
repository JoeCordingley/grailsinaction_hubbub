package com.grailsinaction


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class QueryIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "Simple property comparison"(){
        when: "Users are selected by a simple password match"
        def users = User.where{
            password == "testing"
        }.list(sort: "loginId")

        then: "The users with that paasword are returned"
        users*.loginId == ["frankie"]
    }

    void "Multiple criteria"(){
        when: "A user is selected by loginId or password"
        def users = User.where{
            loginId == "frankie" || password == "crikey"
        }.list(sort:"loginId")
        then: "The matching loginIds are returned"
        users*.loginId == ["dillon", "frankie", "sara"]
    }

    void "Query on association"() {
        when: "The following collection is queried"
        def users = User.where{
            following.loginId == "sara"
        }.list(sort:"loginId")

        then:"A list of the followers of the given user is returned"
        users*.loginId == ["phil"]
    }

    void "Query against a range value"(){
        given: "The current date & time"
        def now = new Date()

        when: "The 'dateCreated' property is queried"
        def users = User.where{
            dateCreated in (now - 1)..now
        }.list(sort: "loginId", order: "desc")

        then: "The users created within the specified date range are returned"
        users*.loginId == ["phil", "peter", "glen", "frankie", "chuck_norris", "admin"]

    }

    void "Retrieve a single instance"(){
        when: "A specific user is queried with get()"
        def user = User.where{
            loginId == "phil"
        }.get()

        then: "A single instance is returned"
        user.password == "thomas"
    }

    void "Retrieve five users"(){
        when:
        def users = User.list(/*sort: 'loginId', order: "asc",*/ //for some reason breaks it
                        max: 5, fetch: [posts: 'eager'])

        then:
        users.size() == 5
    }

    void "get Users between dates again"(){
        given: "The current date & time"
        def now = new Date()

        when: "The 'dateCreated' property is queried"
        def users = User.withCriteria{
            between("dateCreated", now -1, now)
            order("loginId", "desc")
        }

        then: "The users created within the specified date range are returned"
        users*.loginId == ["phil", "peter", "glen", "frankie", "chuck_norris", "admin"]

    }

    void "see what happens"(){
        when:
        def tagList = Post.withCriteria{
            createAlias "tags", "t"

            user{eq "loginId", "phil"}

            projections{
                groupProperty "t.name"
                count "t.id"
            }
            order("t.name", "asc")
        }
        then:
        tagList == [["bbq", 1], ["grails", 2], ["groovy", 3], ["ramblings", 1], ["second", 1]]


    }

}
