
//@author hcadavid

apimock = (function () {

    var mockdata = [];

    mockdata["johnconnor"] = [{author: "johnconnor", "points": [{"x": 150, "y": 120}, {"x": 215, "y": 115}], "name": "house"},
        {author: "johnconnor", "points": [{"x": 340, "y": 240}, {"x": 15, "y": 215}], "name": "gear"}];
    mockdata["maryweyland"] = [{author: "maryweyland", "points": [{"x": 140, "y": 140}, {"x": 115, "y": 115}], "name": "house2"},
        {author: "maryweyland", "points": [{"x": 120, "y": 140}, {"x": 115, "y": 115}], "name": "gear2"}];


    return {
        getBlueprintsByAuthor: function (authname, callback) {
            callback(
                    mockdata[authname]
                    );
        },
        getBlueprintsByNameAndAuthor: function (authname, bpname, callback) {

            callback(
                    mockdata[authname].find(function (e) {
                return e.name === bpname;
            })
                    );
        }
    };

})();

apiClient = (function () {
    
    return{
        getBlueprintsByAuthor: function (authname, callback) {
            if (authname !== "") {
                $.get("/blueprints/" + authname, callback);
            }else if(authname == ""){
                alert("EL AUTOR NO PUEDE ESTAR VACIO");
            }
            ;
        },
        getBlueprintsByNameAndAuthor: function (authname, bpname, callback) {

            $.get("/blueprints/" + authname + "/" + bpname, callback);
        },

        putBlueprint: function (authname, bpname, points, callback) {

            var data = {"author": authname, "points": points, "name": bpname};
            return $.ajax({
                url: "/blueprints/" + authname + "/" + bpname,
                type: 'PUT',
                data: JSON.stringify(data),
                contentType: "application/json"
            }).then(function () {
                $.get("/blueprints/" + authname, callback);
            },
                    function (response) {
                        alert(response.responseText);
                    }
            );
        },

        createBlueprint: function (authname, bpname, points, callback) {
            var data = {"author": authname, "points": points, "name": bpname};
            return $.ajax({
                url: "/blueprints/",
                type: 'POST',
                data: JSON.stringify(data),
                contentType: "application/json"
            }).then(function () {
                $.get("/blueprints/" + authname, callback);
            },
                    function () {
                        alert("Error al actualizar o guardar el nuevo Blueprint");
                    }
            );
        },

        deleteBP: function (authname, bpname, callback) {
            $.ajax({
                url: "/blueprints/" + authname + "/" + bpname,
                type: 'DELETE',
                success:function (){
                    alert("Blueprint borrado");
                }
            }).then(
                    function () {
                        $.get("/blueprints/" + authname, callback);
                    },
                    function () {
                        alert("No se pudo borrar el blueprint");
                    }
            );
            ;
        }

    };
})();

/*
 Example of use:
 var fun=function(list){
 console.info(list);
 }
 
 apimock.getBlueprintsByAuthor("johnconnor",fun);
 apimock.getBlueprintsByNameAndAuthor("johnconnor","house",fun);*/