
<%@ page import="org.davisononline.footy.match.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>
            <g:message code="org.davisononline.footy.match.views.fixture.assign.title" default="Assign Resources"/>
        </title>
        <script type="text/javascript">
            /**
             * get all fixtures for this new date
             */
            function dateChange() {
                ${remoteFunction(
                    update: 'fixtureList',
                    action:'changeDate',
                    controller: 'resource',
                    params: 'Form.serialize($(\'date_day\').form)',
                    options: [asynchronous:false],
                    after: 'previewAllocations()'
                    )}
            }

            /**
             * enable an array of misc object types to be searched through based on a
             * property of one of the objects.
             * Credit: http://blog.webonweboff.com/2010/05/javascript-search-array-of-objects.html
             * @param a
             * @param fnc
             */
            function ArrayIndexOf(a, fnc) {
                if (!fnc || typeof (fnc) != 'function') {
                    return -1;
                }
                if (!a || !a.length || a.length < 1) return -1;
                for (var i = 0; i < a.length; i++) {
                    if (fnc(a[i])) return i;
                }
                return -1;
            }

            /**
             * pivot the pitches/refs/changing rooms to show a schedule by all
             */
            function previewAllocations() {
                var resDiv = $('previewAllocations')

                // lots of code depends on the number and order of these for now
                var selects = document.getElementsByTagName('select')
                var allPitches = []
                var allRefs = []
                var allChRms = []

                // [012] are the date selects
                for (var i = 3; i < selects.length; i++) {
                    var sel = selects[i]
                    var resourceName = sel[sel.selectedIndex].text
                    var matchFn = function(obj) {return obj.rname == resourceName}
                    var pivot = null

                    if (sel.name.startsWith("pitch")) {
                        pivot = allPitches
                    }

                    if (sel.name.startsWith("ref")) {
                        pivot = allRefs
                    }

                    if (sel.name.startsWith("chrm")) {
                        pivot = allChRms
                    }

                    if (pivot != null) {
                        var pi = ArrayIndexOf(pivot, matchFn);
                        var pivotMember;
                        if (pi > -1) pivotMember = pivot[pi]
                        else {
                            pivotMember = {rname:resourceName, games:[]}
                            pivot.push(pivotMember)
                        }
                        // get game details for this row
                        pushGameToPivot(sel, pivotMember)
                    }
                }

                // render pitch list
                var pitchSummary = render(allPitches, "Pitches", "pitch")
                var refSummary = render(allRefs, "Referees", "ref")
                var chRmSummary = render(allChRms, "Changing Rooms", "chrm")
                resDiv.innerHTML = pitchSummary + refSummary + chRmSummary
            }

            /**
             * push a game description to the array
             */
            function pushGameToPivot(sel, arr) {
                var tr = sel.parentNode.parentNode
                var hh = tr.cells[1].children[0]
                var mm = tr.cells[1].children[1]
                var desc = "<strong>" + hh[hh.selectedIndex].text + ":"
                        + mm[mm.selectedIndex].text + "</strong> "
                        + tr.cells[0].innerHTML
                arr.games.push(desc)
            }

            /**
             * create the block for a preview of a pivot
             */
            function render(obj, title, css) {
                var output = "<div class='pivotPreview " + css + "'><h2>" + title + "</h2>"
                for (var i = 0; i < obj.length; i++) {
                    output += "<h3>" + obj[i].rname + "</h3><ul>"
                    for (var j = 0; j < obj[i].games.length; j++) {
                        output += "<li>" + obj[i].games[j] + "</li>"
                    }
                    output += "</ul>"
                }
                return output + "</div>"
            }

        </script>
    </head>
    <body>
        <div class="list">
            <g:form method="post" class="date">
                <g:set var="now" value="${new Date()}"/>
                <div class="nav">
                    Date to assign resources: <g:datePicker name="date" precision="day" years="${(now.year-1 + 1900)..(now.year+1901)}"/>
                    Key:- || <span class="LeagueGame">League Game</span> || <span class="CupGame">Cup Game</span> || <span class="FriendlyGame">Friendly Game</span> ||
                </div>

                <%-- for fields created by the datePicker --%>
                <script type="text/javascript">
                    $('date_day').onchange = dateChange
                    $('date_month').onchange = dateChange
                    $('date_year').onchange = dateChange
                </script>
            </g:form>

            <div id="fixtureList">

            </div>
        </div>
    </body>
</html>
