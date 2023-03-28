let stompClient = null;
let app = new Vue({
    el: '#app',
    data: {
        isChecked: false,
        total: 0,
        isLoading: false,
        formula_str: "",
    },
    mounted() { // 相當於jquery ready
        // this.loadingEvent(true);
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
            if(this.isStompClientConnected(stompClient)){
                this.disconnectServer();
                this.clickConnect(false);
            }else{
                this.connectServer();
                this.clickConnect(true);
            }
        },
        connectServer() {
            let socket = new SockJS('/stomp-endpoint');
            stompClient = Stomp.over(socket);
            stompClient.heartbeat.outgoing = 2000;  // client will send heartbeats every 20000ms
            stompClient.heartbeat.incoming = 2000;     // client does not want to receive heartbeats from the server
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                app.loadingEvent(false);
                stompClient.subscribe('/topic/calculator', function (resObj) {
                    console.log(resObj);
                    let result = JSON.parse(resObj.body).message;
                    console.log(result);
                    app.showAnswer(result);
                });
                stompClient.subscribe('/topic/random_formula', function (resObj) {
                    console.log(resObj.body);
                    app.formula_str = resObj.body;
                });
            }, (err) => {
                // 斷線重連線
                this.loadingEvent(true);
                console.log(err);
                this.connectServer(); // 重新连接
            });
        },
        disconnectServer() {
            if (this.isStompClientConnected(stompClient)) {
                stompClient.disconnect(function() {
                    console.log("See you next time!");
                });
            }
            this.total = 0;
            console.log("Disconnected");
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
        },
        clickConnect(status) {
            this.isChecked = status;
        },
        loadingEvent(isShow) {
            this.isLoading = isShow;
        },
        getRandomFormula() {
            if (!this.isStompClientConnected(stompClient)){
                alert('請先連線!');
            }else{
                stompClient.send("/app/generate_formula", {});
            }
        },
        paste() {
            this.total = this.formula_str;
        }
    }
});