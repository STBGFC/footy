<%@ page import="grails.plugin.searchable.internal.lucene.LuceneUtils; grails.plugin.searchable.internal.util.StringQueryUtils; org.springframework.util.ClassUtils" %>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Admin Search Results</title>
    <script type="text/javascript">
        var focusField = 'q2'
    </script>
  </head>
  <body>
        <g:form url='[controller: "search", action: "index"]' id="searchableForm" name="searchableForm" method="get">
            <g:textField name="q" id="q2" value="${params.q}" size="50"/><g:submitButton name="search" value="search" />
        </g:form>

        <div class="list">
            <g:set var="haveQuery" value="${params.q?.trim()}" />
            <g:set var="haveResults" value="${searchResult?.results}" />
            <div id="searchExplanation">
                <g:if test="${haveQuery && !haveResults && !parseException}">
                <p>Nothing matched your query - <strong>${params.q}</strong></p>
                </g:if>

                <g:if test="${searchResult?.suggestedQuery}">
                <p>
                    Did you mean <g:link controller="search" action="index" params="[q: searchResult.suggestedQuery]">${StringQueryUtils.highlightTermDiffs(params.q.trim(), searchResult.suggestedQuery)}</g:link>?
                </p>
                </g:if>

                <g:if test="${parseException}">
                <p>Your query - <strong>${params.q}</strong> - was not understood.</p>
                <p>Suggestions:</p>
                <ul>
                    <li>Fix the query syntax if you were trying to perform an advanced search</li>
                    <g:if test="${LuceneUtils.queryHasSpecialCharacters(params.q)}">
                    <li>Remove special characters like <strong><code>( " - [ ]</code></strong>, before searching, eg, <em><strong>${LuceneUtils.cleanQuery(params.q)}</strong></em>
                        <br/><g:link controller="search" action="index" params="[q: LuceneUtils.cleanQuery(params.q)]">Search again with special characters removed</g:link>
                    </li>
                    <li>Escape special characters like <strong><code>( " - [ ]</code></strong> with <strong>\</strong>, eg, <em><strong>${LuceneUtils.escapeQuery(params.q)}</strong></em>
                        <br/><g:link controller="search" action="index" params="[q: LuceneUtils.escapeQuery(params.q)]">Search again with special characters escaped</g:link>
                    </li>
                    </g:if>
                </ul>
                </g:if>
            </div>

            <g:if test="${haveResults}">
            <div id="searchResults">
                <g:if test="${haveQuery}">
                <p>
                    Showing <strong>${searchResult.offset + 1}</strong> - <strong>${searchResult.results.size() + searchResult.offset}</strong> of <strong>${searchResult.total}</strong>
                    results for <strong>${params.q}</strong>
                </p>
                </g:if>
                <g:each var="result" in="${searchResult.results}" status="index">
                <div class="searchResult">
                    <g:set var="className" value="${ClassUtils.getShortName(result.getClass())}" />
                    <g:set var="link" value="${createLink(controller: className[0].toLowerCase() + className[1..-1], action: 'edit', id: result.id)}" />
                    <g:set var="desc" value="${result.toString()}" />
                    <g:if test="${desc.size() > 120}"><g:set var="desc" value="${desc[0..120] + '...'}" /></g:if>
                    <div class="name">${className}: <a href="${link}">${desc.encodeAsHTML()}</a></div>
                    <div class="displayLink">${link}</div>
                </div>
                </g:each>
            </div>

            <div class="paginateButtons">
                <g:if test="${haveResults}">
                <g:set var="totalPages" value="${Math.ceil(searchResult.total / searchResult.max)}" />
                <g:if test="${totalPages == 1}"><span class="currentStep">Page 1 of 1</span></g:if>
                <g:else><g:paginate controller="search" action="index" params="[q: params.q]" total="${searchResult.total}" /></g:else>
                </g:if>
            </div>
            </g:if>
        </div>
    </body>
</html>
