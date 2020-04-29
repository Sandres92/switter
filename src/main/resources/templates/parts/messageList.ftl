<#include "security.ftl">
<#import "pager.ftl" as p>

<@p.pager url page />
<div class="row row-cols-1 row-cols-md-3" id="message-list">
    <#list page.content as message>
        <div class="col mb-4">
            <div class="card" data-id="${message.id}">
                <#if message.filename??>
                    <img src="/img/${message.filename}" class="card-img-top" alt="..."/>
                </#if>
                <div class="m-2">
                    <span>${message.text}</span><br/>
                    <i>#${message.tag}</i>
                </div>
                <div class="card-footer text-muted container">
                    <div class="row justify-content-end">
                        <time><#if message.timeOfCreation??>${message.getTimeOfCreation()?string["dd.MM.yyyy HH:mm"]}<#else>no date</#if></time>
                    </div>

                    <div class="row my-2">
                        <a class="col align-self-center"
                           href="/user-messages/${message.author.id}">${message.authorName}</a>
                        <a class="col align-self-center" href="/messages/${message.id}/like">
                            <#if message.meLiked>
                                <i class="fas fa-heart"></i>
                            <#else>
                                <i class="far fa-heart"></i>
                            </#if>
                            ${message.likes}
                        </a>
                        <#if message.author.id == currentUserId>
                            <!--<a class="col btn btn-primary" href="/user-messages/${message.author.id}?message=${message.id}">-->
                            <a class="col btn btn-primary" href="/messages-edit?message=${message.id}">
                                Edit
                            </a>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    <#else>
        No messages
    </#list>
</div>
<@p.pager url page />