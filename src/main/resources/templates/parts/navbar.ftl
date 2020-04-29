<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">switter</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item <#if activeHome??>active</#if>">
                <a class="nav-link" href="/">home </a>
            </li>
            <#if user??>
                <li class="nav-item <#if activeNews??>active</#if>">
                    <a class="nav-link" href="/main">news</a>
                </li>
                <li class="nav-item <#if activeMyMessages??>active</#if>">
                    <a class="nav-link" href="/user-messages/${currentUserId}">my messages</a>
                </li>
            </#if>
            <#if isAdmin>
                <li class="nav-item <#if activeUsers??>active</#if>">
                    <a class="nav-link" href="/user">users</a>
                </li>
            </#if>
            <#if user??>
                <li class="nav-item <#if activeProfile??>active</#if>">
                    <a class="nav-link" href="/user/profile">profile</a>
                </li>
            </#if>
        </ul>

        <div class="navbar-text mr-3"><#if user??>${name}<#else>please, login</#if></div>
        <@l.logout/>
    </div>
</nav>