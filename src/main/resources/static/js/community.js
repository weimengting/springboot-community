/**
 * 对问题进行回复
 */
function post() {
    var questionId = $("#question_id").val();
    var commentContent = $("#comment_content").val();
    comment2Target(questionId, 1, commentContent);
}


/**
 *对评论进行回复
 */
function comment2comment(e) {
    var targetId = e.getAttribute("data-id");
    var content = $("#input-" + targetId).val();
    comment2Target(targetId, 2, content)
}

function comment2Target(targetId, type, content) {
    if (!content){
        alert("回复内容不能为空");
        return;
    }

    // post方法向服务器请求增加数据,url表示转到什么路径下($.表示这是一个jquery方法)
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) { //response表示来自于服务器的响应
            if (response.code === 200){
                window.location.reload();
            }
            else if (response.code === 2003){
                var isAccepted = confirm(response.message);
                if (isAccepted){
                    window.open("https://gitee.com/oauth/authorize?client_id=7b4e69941f8c29011804b4598175546115d10ea42ceb649fe83ce0eda78a400b&redirect_uri=http://localhost:8080/callback&response_type=code");
                    window.localStorage.setItem("closable", true);
                }
            }
            else {
                alert(response.message); //将信息在浏览器上方挂出来
            }
        },
        dataType: "json"
    });
}

/**
 * 展开二级评论
 */
function collapseComments(e) { //e是前端的this，作用于整个span标签栏(data是父级对应的id)
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    //自定义一个用来标记页面状态的变量
    var collapse = e.getAttribute("data-collapse");
    if (collapse){
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }
    else {
        //使用getJson向服务器请求获取对应路径的资源
        var commentBody = $("#comment-" + id);  //表示的是前端对应的块级带有相应id的元素<div>

        //如果加载过就不再进行重复追加
        if (commentBody.children().length != 1){
            //将二级评论展开
            comments.addClass("in"); //添加名称为in的class
            //修改当前二级评论的页面状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");  //不用在前端的class里面添加该属性，因为函数可以直接调用css文件的格式
        }
        else {
            $.getJSON( "/comment/" + id, function( data ) {
                $.each( data.data.reverse(), function( index, comment ) { //function（data）是从服务器请求到的数据，之后对于data中的data属性进行遍历

                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl,
                        "style": "width: 38px; height: 38px"
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));


                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);


                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    commentBody.prepend(commentElement);
                });
                //将二级评论展开
                comments.addClass("in"); //添加名称为in的class
                //修改当前二级评论的页面状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");  //不用在前端的class里面添加该属性，因为函数可以直接调用css文件的格式
            });
        }
    }
}


/**
 * 设置标签的自动填入（后面有.val()表示原id本来的属性值，如果没有则表示该id对应的整个元素）
 */
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    console.log(value);
    var previous = $("#tag").val();  //查看现有的标签是否有将要添加的标签,以及是否为空
    if (previous.indexOf(value) === -1){  //如果不存在则追加
        if (previous){
            $("#tag").val(previous + ',' + value);
        }
        else {
            $("#tag").val(value);
        }
    }
}

function showTags() {
    $("#show-tag").show();
}