<#import "parts/common.ftl" as common>

<@common.page>
    <#if checkValidation == true>
        <div>
            <form method="post" action="/goods/add">
                <input type="text" name="name" placeholder="Enter name of good"/>
                <input type="number" step=0.01 name="priority" placeholder="Priority">
                <button type="submit" class="btn btn-primary">Add</button>
            </form>
        </div>
        <p class="text-danger">${add_message!" "}</p>
        <form method="get" action="/goods">
            <input type="text" name="name" placeholder="Enter name of good">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
        <p class="text-danger">${delete_message!" "}</p>
        <#if find_message == "">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Name</th>
                    <th scope="col">Priority</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <#list goods as good>
                    <tr>
                        <th scope="row">${good.id}</th>
                        <td>${good.name}</td>
                        <td>${good.priority}</td>
                        <td>
                            <form method="post" action="/goods/delete/${good.id}">
                                <button type="submit" class="btn btn-primary">DELETE</button>
                            </form>
                        </td>
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