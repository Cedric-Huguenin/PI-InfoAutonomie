var graph_data = [];

var graph = new Morris.Line({
    // ID of the element in which to draw the chart.
    element: 'myfirstchart',
    // Chart data records -- each entry in this array corresponds to a point on
    // the chart.
    data: graph_data,
    // The name of the data record attribute that contains x-values.
    xkey: 'time',
    // A list of names of data record attributes that contain y-values.
    ykeys: ['81.77', '9.138', '153.111'],
    ymin: 'auto', ymax: 'auto',
    hideHover: true,
    // Labels for the ykeys -- will be displayed when you hover over the
    // chart.
    labels: ['81.77', '9.138', '153.111']
});

$(function(){

    // get websocket class, firefox has a different way to get it
    var WS = window['MozWebSocket'] ? window['MozWebSocket'] : WebSocket;

    // open pewpew with websocket
    var socket = new WS('ws://iotlab.telecomnancy.eu/liveStream/0');

    var writeMessages = function(event){
        console.log(event.data);
        var obj = JSON.parse(event.data);
        var ykey = obj.mote;
        var dict = {};
        dict[ykey] = obj.light1;
        dict['time'] = obj.timestamp;
        graph_data.push(dict);


        graph.setData(graph_data);
    }

    socket.onmessage = writeMessages;

});