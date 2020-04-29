<#import "parts/common.ftl" as c>

<@c.page>
    <#if profileChange??>
        <div class="alert alert-success" role="alert">
            ${profileChange}
        </div>
    </#if>

    <h5>${username}</h5>
    ${message?if_exists}
    <form method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> password: </label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="password"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> email: </label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" placeholder="some@some.com" value="${email!''}"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary" type="submit">save</button>
    </form>
</@c.page>