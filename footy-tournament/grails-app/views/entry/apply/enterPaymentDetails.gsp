<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><g:message code="enter.payment.heading" default="Enter Payment" /></title>
    </head>

    <body>
        <div class="body">
        <h1><g:message code="enter.payment.heading" default="Enter Payment" /></h1>

        <div class="dialog">
        <p>
            You have submitted <strong>${entries.size()}</strong> team${entries.size()==1 ? '':'s'} at a total 
            of <strong>&pound;${entries.size() * 28.00}</strong>
        </p>
        <ul>
        <g:each in="${entries}"><li>${it.club.name} ${it.ageGroup} ${it.teamName}</li></g:each>
        </ul>
        <table>
        <tbody>
            <tr class="prop">
            <td  class="name">
                <img src="${resource(dir:'images', file:'paypal_logo.gif')}" alt="PayPal"/>
            </td>
            <td  class="value image">
                <g:form controller="paypal" action="uploadCart">
                <input type="image" 
                    src="https://www.paypal.com/en_US/i/btn/btn_buynow_LG.gif"
                    alt="PayPal - The safer, easier way to pay online!"/>
                <img alt="" border=0" src="https://www.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1"/>
                </g:form>
            </td>
            </tr>
        </tbody>
        </table>
        <p>
            You will receive email confirmation as soon as payment clears (which is
            normally immediate in the case of PayPal)
        </p>
        </div>
        </div>
    </body>
</html>

