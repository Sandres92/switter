<#import "parts/common.ftl" as c>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter?if_exists}"
                       placeholder="search by tag"/>
                <button type="submit" class="btn btn-primary ml-2">search</button>
            </form>
        </div>
    </div>
    <#include "parts/messageCreate.ftl"/>

    <#include "parts/messageList.ftl"/>

</@c.page>