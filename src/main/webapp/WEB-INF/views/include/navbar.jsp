<%--
  Created by IntelliJ IDEA.
  User: tgdsl
  Date: 2016/12/15
  Time: 22:18
  To change this template use File | Settings | File Templates.
上面的蓝条
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="header-bar">
    <div class="container">
        <a href="/home" class="brand">
            <i class="fa fa-coffee"></i>
        </a>
        <ul class="unstyled inline pull-right">
            <c:choose>
                <c:when test="${not empty sessionScope.curr_user}">
                    <li>
                        <a href="#">
                            <img src="http://ohwnpkfcx.bkt.clouddn.com/${sessionScope.curr_user.avatar}?imageView2/1/w/20/h/20" class="img-circle" alt="">
                        </a>
                    </li>
                    <li>
                        <a href=""><i class="fa fa-plue"></i></a>
                    </li><li>
                        <a href="#"><i class="fa fa-bell"></i></a>
                    </li><li>
                        <a href="setting.html"><i class="fa fa-plue"></i></a>
                    </li><li>
                        <a href="#"><i class="fa fa-plue"></i></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li><a href="/login"><i class="fa fa-sign-in" ></i></a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</div>