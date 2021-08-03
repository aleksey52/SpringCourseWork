<#import "parts/common.ftl" as common>

<@common.page>
    <#if checkValidation == true>
        <div>
            <form method="post" action="/sales/addSale">
                <input type="text" name="nameGood" placeholder="Enter name of good"/>
                <input type="number" name="good_count" placeholder="Enter quantity of good">
                <input type="text" name="create_date" placeholder="Enter date"/>
                <button type="submit" class="btn btn-primary">Add</button>
            </form>
        </div>
        <p class="text-danger">${add_message!" "}</p>
        <form method="get" action="/sales">
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
                    <th scope="col">Create Date</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <#list sales as sale>
                    <tr>
                        <th scope="row">${sale.id}</th>
                        <td>${sale.good.id}</td>
                        <td>${sale.good.name}</td>
                        <td>${sale.good_count}</td>
                        <td>${sale.create_date}</td>
                        <td>
                            <form method="post" action="/sales/delete/${sale.id}">
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