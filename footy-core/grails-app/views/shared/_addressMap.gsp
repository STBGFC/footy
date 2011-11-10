
        <g:if test="${address?.postCode}">
        <g:if test="${!size}"><g:set var="size" value="150"/></g:if>
        <g:if test="${!zoom}"><g:set var="zoom" value="14"/></g:if>
        <g:if test="${!scale}"><g:set var="scale" value="1"/></g:if>
        <img 
            class="addressmap" 
            src="http://maps.google.com/maps/api/staticmap?center=${address?.encodeAsURL()}&markers=color:green|size:mid|label:${address?.name[0]}|${address?.encodeAsURL()}&size=${size}x${size}&sensor=false&zoom=${zoom}&scale=${scale}"
            title="Address Location"
            alt="Map" />
        </g:if>
