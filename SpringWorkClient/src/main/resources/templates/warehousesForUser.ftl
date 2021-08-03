<#import "parts/common.ftl" as common>

<@common.page>
    <#if checkValidation == true>
        <form method="get" action="/warehouse/${num}">
            <input type="text" name="name" placeholder="Enter name of good">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
        <#if find_message == "">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Good ID</th>
                    <th scope="col">Good Name</th>
                    <th scope="col">Good Count</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <#list warehouses as warehouse>
                    <tr>
                        <th scope="row">${warehouse.id}</th>
                        <td>${warehouse.good.id}</td>
                        <td>${warehouse.good.name}</td>
                        <td>${warehouse.good_count}</td>
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