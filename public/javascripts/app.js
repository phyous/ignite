function onload(){
    var dataType = "index"
    function loadData(type, map, heatmap) {
        $.getJSON('heatmap/' + type + '.json',function(testData) {
            google.maps.event.addListenerOnce(map, "idle", function(){
                heatmap.setDataSet(testData);
            });
        });
    }
    var myLatlng = new google.maps.LatLng(48.3333, 16.35);
    var options = {
        zoom: 2,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        disableDefaultUI: false,
        scrollwheel: true,
        draggable: true,
        navigationControl: true,
        mapTypeControl: false,
        scaleControl: true,
        disableDoubleClickZoom: false
    };
    var map = new google.maps.Map(document.getElementById("heatmapArea"), options);

    var heatmap = new HeatmapOverlay(map, {"radius":10, "visible":true, "opacity":60});

    // Toggle button
    document.getElementById("tog").onclick = function() {
        heatmap.toggle();
    };

    document.getElementById("reg").onclick = function() {
        dataType = "index"
        loadData(dataType, map, heatmap)
    }

    document.getElementById("norm").onclick = function() {
        dataType = "normalized"
        loadData(dataType, map, heatmap)
    }
    loadData(dataType, map, heatmap)
    // TODO: Autorelaod data and fix rendering.
    // Right now if we enable this code, newly rendered heatmap will be incorrectly placed when panning map.
//        loadData($dataType, map, heatmap)
//        var frameRefreshInterval = setInterval(function() {
//            console.log("CALLED!")
//            loadData($dataType, map, heatmap)
//        }, 2000);
};