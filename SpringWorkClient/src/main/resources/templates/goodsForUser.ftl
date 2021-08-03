<#import "parts/common.ftl" as common>

<@common.page>
    <#if checkValidation == true>
        <form method="get" action="/goods">
            <input type="text" name="name" placeholder="Enter name of good">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
        <#if find_message == "">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Name</th>
                    <th scope="col">Priority</th>
                </tr>
                </thead>
                <tbody>
                <#list goods as good>
                    <tr>
                        <th scope="row">${good.id}</th>
                        <td>${good.name}</td>
                        <td>${good.priority}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="text-warning">${find_message}</p>
        </#if>
    <#else>
        You are not logged in
        <a href="/login">Sign In</a>
    </#if>
</@common.page>