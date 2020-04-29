<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="container my-3">
        <a class="btn btn-outline-primary" href="<#if prevPage??>${prevPage}<#else>main</#if>">
            <i class="fas fa-chevron-left"></i>
        </a>
    </div>

    <form method="post" enctype="multipart/form-data">
        <div class="container">
            <div class="col">
                <div class="row justify-content-md-center">
                    <div class="card my-3" data-id="${message.id}" style="max-width: 540px;">
                        <#if message.filename??>
                            <img src="/img/${message.filename}" class="card-img-top" alt="..."/>
                        </#if>
                    </div>

                    <div class="form-group  ml-5 my-3">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <div class="form-group">
                            <input type="text"
                                   class="form-control ${(textError??)?string('is-invalid', '')}"
                                   value="<#if message??>${message.text}</#if>"
                                   name="text"
                                   placeholder="input message"/>
                            <#if textError??>
                                <div class="invalid-feedback">
                                    ${textError}
                                </div>
                            </#if>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control"
                                   value="<#if message??>${message.tag}</#if>"
                                   name="tag"
                                   placeholder="tag"/>
                            <#if tagError??>
                                <div class="invalid-feedback">
                                    ${tagError}
                                </div>
                            </#if>
                        </div>
                        <div class="form-group">
                            <div class="custom-file">
                                <input type="file" name="file" id="customFile"
                                       value="<#if message??><#if message.filename??>${message.filename}</#if></#if>"/>
                                <label class="custom-file-label" for="customFile">choose file</label>
                            </div>
                        </div>
                    </div>
                </div>

                <#if message.author.id == currentUserId>
                    <div class="d-flex align-items-end flex-column bd-highlight mb-3">

                        <div class="form-group my-3 mt-auto p-2 bd-highlight">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <input type="hidden" name="id" value="<#if message??>${message.id}</#if>"/>
                            <input type="hidden" name="prevPage"
                                   value="<#if prevPage??>${prevPage}<#else>main</#if>"/>
                            <button type="submit" class="btn btn-primary">save</button>
                        </div>

                    </div>
                </#if>
            </div>
        </div>
    </form>

</@c.page>