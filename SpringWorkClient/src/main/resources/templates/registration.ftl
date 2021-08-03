<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as login>

<@common.page>
    Registration
    <@login.login "/registration" "Registration"/>
    <p class="text-danger">${message!" "}</p>
    <a href="/login">Sign In</a>
</@common.page>