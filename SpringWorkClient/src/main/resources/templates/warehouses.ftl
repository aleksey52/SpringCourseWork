<#import "parts/common.ftl" as common>

<@common.page>
    <#if checkValidation == true>
        <div>
            <form method="post" action="/warehouse/add/${num}">
                <input type="text" name="nameGood" placeholder="Enter name of good"/>
                <input type="number" name="good_count" placeholder="Enter quantity of good">
                <button type="submit" class="btn btn-primary">Add</button>
            </form>
        </div>
        <p class="text-danger">${add_message!" "}</p>
        <form method="get" action="/warehouse/${num}">
            <input type="text" name="name" placeholder="Enter name of good">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
        <p class="text-danger">${delete_message!" "}</p>
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
                        <td>
                            <form method="post" action="/warehouse/delete/${num}/${warehouse.id}">
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