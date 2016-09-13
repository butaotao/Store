(function() {
    var app = {
        // Application Constructor
        initialize: function() {
            this.bindEvents();
        },
        // Bind Event Listeners
        //
        // Bind any events that are required on startup. Common events are:
        // 'load', 'deviceready', 'offline', and 'online'.
        bindEvents: function() {
            document.addEventListener('deviceready', this.onDeviceReady, false);
        },
        // deviceready Event Handler
        //
        // The scope of 'this' is the event. In order to call the 'receivedEvent'
        // function, we must explicitly call 'app.receivedEvent(...);'
        onDeviceReady: function() {
            // app.receivedEvent('deviceready');
            // alert('cordova is ready')
        },
        // Update DOM on a Received Event
        receivedEvent: function(id) {
            // var parentElement = document.getElementById(id);
            // var listeningElement = parentElement.querySelector('.listening');
            // var receivedElement = parentElement.querySelector('.received');

            // listeningElement.setAttribute('style', 'display:none;');
            // receivedElement.setAttribute('style', 'display:block;');
            // console.log('Received Event: ' + id);
        }

    };

    app.initialize();


    function cordova_call(config, callback) {

        if (!config) {
            return alert('缺少参数');
        }

        if (!config.name) {
            return alert('缺少调用方法名');
        }

        var name = config.name;

        var json = JSON.stringify(config.params || {});



        // 有回调
        function thisCallback(rps) {
            if (callback) {
                callback(JSON.parse(rps));
            }
        };

        cordova.exec(
            thisCallback,
            null,
            "BridgePlugin",
            name, [json]);

    }

    window.$cordova = {
        call: cordova_call
    };

})()
