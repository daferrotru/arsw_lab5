var app = (function () {
    var author;
    var bps = [];
    var canvasSelected = false;
    var existingBP = false;
    var first;
    var last;
    var authorname;
    var bpName;
    var currentBP = [];
    var promise;


    var str = function (tag) {
        return '"' + tag + '"';
    };


    //Helper function to get correct page offset for the Pointer coords
    function getOffset(obj) {
        var offsetLeft = 0;
        var offsetTop = 0;
        do {
            if (!isNaN(obj.offsetLeft)) {
                offsetLeft += obj.offsetLeft;
            }
            if (!isNaN(obj.offsetTop)) {
                offsetTop += obj.offsetTop;
            }
        } while (obj = obj.offsetParent);
        return {left: offsetLeft, top: offsetTop};
    }


    var callback = function (lista) {
        canvasSelected = false;
        var ans;
        ans = lista.map(function (obj1) {
            var obj;
            var numpoints = obj1.points.length;
            obj = {name: obj1.name, points: numpoints};
            return obj;
        });
        $("#tablaBP tbody").empty();
        ans.map(function (bp) {
            $(document).ready(function () {
                var markup = "<tr><td>" + bp.name + "</td><td>" + bp.points + "</td><td><button type='button' onclick='app.drawBp(" + str(document.getElementById('author').value) + ',' + str(bp.name) + ")'>Draw</button></td></tr>";
                $("#tablaBP tbody").append(markup);
            });
        }
        );
        document.getElementById("totalPoints").innerHTML = "Total User Points: " + ans.reduce(function (total, points) {
            return total + points.points;
        }, 0);
    };

    var callbackBP = function (lista) {

        currentBP = lista;
        document.getElementById("currentBP").innerHTML = "Current Blueprint: " + lista.name;
        var canvas = document.getElementById("CanvasBP");
        var c = canvas.getContext("2d");
        c.beginPath();
        c.clearRect(0, 0, canvas.width, canvas.height);
        for (var i = 0; i < lista.points.length - 1; i++) {
            first = lista.points[i];
            last = lista.points[i + 1];
            c.moveTo(first.x, first.y);
            c.lineTo(last.x, last.y);
        }
        c.stroke();
    };

    var drawPoint = function (e) {
        var canvas = document.getElementById("CanvasBP");
        var c = canvas.getContext("2d");

        var offset = getOffset(canvas);

        currentBP.points.push({x: e.pageX - offset.left, y: e.pageY - offset.top});
        callbackBP(currentBP);

    };




    var isSelected = function () {
        return canvasSelected;
    };

    return{
        setPoints: function (author_) {
            author = author_;
            apiClient.getBlueprintsByAuthor(author, callback);
        },
        drawBp: function (authname, bpname) {

            canvasSelected = true;
            authorname = authname;
            bpName = bpname;
            apiClient.getBlueprintsByNameAndAuthor(authname, bpname, callbackBP);
        },
        init: function () {

            console.info('initialized');
            var canvas = document.getElementById("CanvasBP");

            //if PointerEvent is suppported by the browser:

            if (window.PointerEvent) {
                canvas.addEventListener("pointerdown", function (event) {
                    if (isSelected()) {
                        drawPoint(event);
                    }
                });
            } else {
                canvas.addEventListener("mousedown", function (event) {
                    if (isSelected()) {
                        drawPoint(event);
                    }
                });
            }
        },

        update_save: function () {
            if (canvasSelected && !existingBP) {
                promise = apiClient.putBlueprint(authorname, bpName, currentBP.points, callback);
                callbackBP(currentBP);
            } else if (existingBP) {
                promise = apiClient.createBlueprint(currentBP.author, currentBP.name, currentBP.points, callback);
                alert("New Blueprint Saved!");
                existingBP = false;
            } else {
                alert("Create or select a Blueprint");
            }


        },

        createNewBlueprint: function (bpName) {
            if (bpName !== "" && author !== "") {
                canvasSelected = true;
                existingBP = true;
                currentBP = {"author": author, "points": [], "name": bpName};
                callbackBP(currentBP);
            } else {
                alert("Set a name for this new Blueprint");
            }

        },

        deleteBP: function () {
            
            if (canvasSelected === true) {
                apiClient.deleteBP(currentBP.author, currentBP.name, callback);
                var canvas = document.getElementById("CanvasBP");
                var c = canvas.getContext("2d");
                document.getElementById("currentBP").innerHTML = "Current Blueprint: ";
                c.clearRect(0, 0, canvas.width, canvas.height);

            }

        }


    };
})();