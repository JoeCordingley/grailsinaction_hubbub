package com.grailsinaction


class PostController {

    static defaultAction = "home"
    def postService

    def home() {
        if (!params.id){
            params.id = "chuck_norris"
        }
        redirect(action: 'timeline', params: params)
    }

    def timeline(String id) {
        def user = User.findByLoginId(params.id)
        if (!user){
            response.sendError(404)
        } else {
            [user: user]
        }
    }

    def addPost(String id, String content) {

//        def user = User.findByLoginId(params.id)
//        def myParams = params
//        if (user) {
//            def post = new Post(content: params.content)
//            user.addToPosts(post)
//            if (user.save()){
//                flash.message = "Successfully created Post"
//            } else {
//                flash.message = "Invalid or empty post"
//            }
//        } else {
//            flash.message = "Invalid User Id"
//        }
//        redirect(action: "timeline", id: params.id)

        try {
            def newPost = postService.createPost(id, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect(action: 'timeline', id: id)
    }
}
