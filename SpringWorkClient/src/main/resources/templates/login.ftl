<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as login>

<@common.page>
    Login page
    <@login.login "/login" "Sign In"/>
    <p class="text-danger">${message!" "}</p>
    <a href="/registration">Registration</a>
</@common.page>