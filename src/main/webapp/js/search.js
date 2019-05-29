function search() {
    var index = document.getElementById("selection").options.selectedIndex;
    var value = document.getElementById("selection").options[index].value;
    if (value === "term") {
        loadByTerms()
    } else {
        loadByFormula()
    }
}
function loadByTerms() {
    document.getElementById("btn-search").innerHTML = "Loading";
    $.ajax({
        url: "/MathSearch/search",
        type: 'GET',
        data: {
            math : $('#math-input').val(),
            type: "term"
        },
        success: function(data){
            document.getElementById("btn-search").innerHTML = "Search";
            var content = document.getElementById("content");

            while (content.firstChild) {
                content.removeChild(content.firstChild);
            }

            var table = document.createElement("div");
            table.id = "table";
            content.appendChild(table);

            var count = 1;
            for(var i = 0; i < data.length; ++i) {
                var listOfMaths = data[i];
                for (var j = 0; j < listOfMaths.length; j++){
                    var math = listOfMaths[j];

                    var item = document.createElement("div");
                    item.className = "item";

                    var link = document.createElement("a");
                    link.target = "_blank";
                    link.className = "title";
                    link.href = math.link + "#" + math.topic.replace(/\s/g, '_');
                    if (math.title === math.topic) {
                        link.innerHTML = math.title;
                    } else {
                        link.innerHTML = math.title + ". " + math.topic;
                    }
                    var formula = document.createElement("div");
                    formula.className = "formula";

                    formula.innerHTML = "$$" + math.name + "$$";
                    var termDiv = document.createElement("div");
                    termDiv.className = "term";

                    item.appendChild(link);
                    item.appendChild(formula);
                    item.appendChild(termDiv);
                    table.appendChild(item);

                    math.terms.forEach(function(term) {
                        var p = document.createElement('p');
                        p.innerHTML = term.text;
                        termDiv.appendChild(p);
                    });
                    count++;
                }
            }
            MathJax.Hub.Queue(["Typeset",MathJax.Hub]);
        },
        error: function(xhr, ajaxOptions, thrownError) {
            document.getElementById("btn-search").innerHTML = "Search";
            alert(xhr.status + ": " + thrownError.message);
        }
    });
}

function loadByFormula() {
    document.getElementById("btn-search").innerHTML = "Loading";
    $.ajax({
        url: "/MathSearch/search",
        type: 'GET',
        data: {
            math : $('#math-input').val(),
            type: "formula"
        },
        success: function(data){
            document.getElementById("btn-search").innerHTML = "Search";
            var content = document.getElementById("content");

            while (content.firstChild) {
                content.removeChild(content.firstChild);
            }
            var table = document.createElement("table");
            table.id = "table-formula";
            content.appendChild(table);

            for(var i = 0; i < data.length; ++i) {
                var listOfMaths = data[i];
                for (var j = 0; j < listOfMaths.length; j++){
                    var math = listOfMaths[j];

                    var item = table.insertRow();

                    var link = document.createElement("a");
                    link.target = "_blank";
                    link.className = "title";
                    link.href = math.link + "#" + math.topic.replace(/\s/g, '_');
                    if (math.title === math.topic) {
                        link.innerHTML = math.title;
                    } else {
                        link.innerHTML = math.title + ". " + math.topic;
                    }
                    var formula = document.createElement("div");
                    formula.className = "formula-inline";
                    formula.innerHTML = "$$" + math.name + "$$";
                }
            }
            MathJax.Hub.Queue(["Typeset",MathJax.Hub]);
        },
        error: function(xhr, ajaxOptions, thrownError) {
            document.getElementById("btn-search").innerHTML = "Search";
            alert(xhr.status + ": " + thrownError.message);
        }
    });
}