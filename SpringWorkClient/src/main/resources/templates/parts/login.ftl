<#macro login action button_name>
    <form action="${action}" method="post">
        <div><label> User Name : <input type="text" name="username"/> </label></div>
        <div><label> Password: <input type="password" name="password"/> </label></div>
        <div><input type="submit" value="${button_name}" class="btn btn-primary"/></div>
    </form>
</#macro>
