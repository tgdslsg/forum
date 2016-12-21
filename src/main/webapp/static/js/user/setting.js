$(function () {
    $("#basicBtn").click(function () {
        $("#basicForm").submit();
    });
    $("#basicForm").validate({
        errorElement: 'span',
        errorClass: 'text-error',
        rules: {
            email: {
                required: true,
                email: true,
                remote: '/validate/email?type=1'
            }
        },
        messages: {
            email: {
                required: "邮件必填",
                email: "邮件格式错误",
                remote: "电子邮件已被占用"
            }
        },
        submitHandler: function (form) {
            $.ajax({
                url: "/setting?action=profile",//  / 后跟的是servlet路径 ？后面跟的是参数
                type: "post",
                data: $(form).serialize(),
                beforeSend: function () {
                    $("#basicBtn").text("保存中...").attr("disable", "disable");
                },
                success: function (data) {
                    if (data.state = "success") {
                        alert("修改成功......")
                    }
                },
                error: function () {
                    alert("服务器错误")
                },
                complete: function () {
                    $("#basicBtn").text("保存").attr("disable")
                }
            });
        }
    });

    //密码修改
    $("#passwordBtn").click(function () {
        $("#passwordForm").submit();
    });
    $("#passwordForm").validate({
        errorElement:"span" ,
        errorClass: "text-error",

        rules: {
            oldpassword: {
                required: true,
                rangelength: [3, 16]

            },
            newpassword: {
                required: true,
                rangelength: [3, 16]
            },
            repassword: {
                required: true,
                rangelength: [3, 16],
                equalTo: "#newpassword"
            },
        },
        messages: {
            oldpassword: {
                required: "请输入原始密码",
                rangelength: "密码长度3-18个字符"
            },
            newpassword: {
                required: "请输入新密码",
                rangelength: "密码长度3-18个字符"
            },
            repassword: {
                required: "请输入确认密码",
                rangelength: "密码长度3-18个字符",
                equalTo: "两次密码不一样"
            }
        },
        submitHandler: function (form) {
            $.ajax({
                url: "/setting?action=password",
                type: "post",
                data: $(form).serialize(),
                beforeSend: function () {
                    $("#passwordBtn").text("保存中...").attr("disabled", "disabled");
                },
                success: function (data) {
                    if (data.state =="success") {
                        alert("密码修改成功，请重新登录");
                        window.location.href = "/login";
                    } else {
                        alert(data.message+"djsdsf");
                    }
                },
                error: function () {
                    alert("服务器错误");
                },
                complete: function () {
                    $("#passwordBtn").text("保存").removeAttr("disabled");
                }
            });
        }
    });
})