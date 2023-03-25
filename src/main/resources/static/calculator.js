let stompClient = null;
let app = new Vue({
    el: '#app',
    data: {
        isChecked: false,
        total: 0
    },
    mounted() { // 相當於jquery ready
        console.log('mounted!');
    },
    methods: {
        isStompClientConnected(stompOvj) {
            console.log(stompOvj);
            return !(stompOvj === null || stompOvj.connected === false);
        },
        showAnswer: function(answer) {
            if(isNaN(answer)){
                alert(answer);
            }else{
                this.total = answer;
            }
        },
        toggleSwitch() {
            console.log("connect:", this.isChecked);
            if(this.isChecked){
                let socket = new SockJS('http://127.0.0.1:8080/stomp-endpoint');
                stompClient = Stomp.over(socket);
                stompClient.heartbeat.outgoing = 2000;  // client will send heartbeats every 20000ms
                stompClient.heartbeat.incoming = 2000;     // client does not want to receive heartbeats from the server
                stompClient.reconnect_delay = 5000;     // Add the following if you need automatic reconnect (delay is in milli seconds)
                stompClient.connect({}, function (frame) {
                    console.log('Connected: ' + frame);
                    stompClient.subscribe('/topic/calculator', function (greeting) {
                        console.log(greeting);
                        let result = JSON.parse(greeting.body).message;
                        console.log(result);
                        app.showAnswer(result);
                    });
                });
            }else{
                if (this.isStompClientConnected(stompClient)) {
                    stompClient.disconnect(function() {
                        console.log("See you next time!");
                    });
                }
                this.total = 0;
                console.log("Disconnected");
            }
            // 可以在這裡加上更多的處理邏輯
        },
        key(num) {
            return this.total===0 ? this.total += num : this.total += '' + num;
        },
        clear() {
            return this.total = 0;
        },
        equal() {
            let equal = this.total;
            if (!this.isStompClientConnected(stompClient)){
                alert('請先連線!');
            }else{
                // return this.total = eval(equal);   //js 計算結果
                stompClient.send("/app/tool", {}, JSON.stringify({'inputString': equal}));
            }
        }
    }
});