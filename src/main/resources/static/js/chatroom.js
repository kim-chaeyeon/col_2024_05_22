var sock = new SockJS("/ws-stomp");
var ws = Stomp.over(sock);
var reconnect = 0;
var vm = new Vue({
    el: '#app',
    data: {
        roomId: '',
        room: {},
        sender: '',
        message: '',
        messages: []
    },
    created() {
        this.roomId = localStorage.getItem('wschat.roomId');
        this.sender = localStorage.getItem('wschat.sender');
        this.findRoom();
    },
    methods: {
        findRoom: function() {
            axios.get('/chat/room/' + this.roomId).then(response => {
                this.room = response.data;
                // 채팅방 이름을 업데이트하는 부분 추가
                document.querySelector('.room-name h2').innerText = this.room.name;
            });
        },
        sendMessage: function() {
            ws.send(`/pub/chat/message/${this.roomId}`, {}, JSON.stringify({ type: 'TALK', roomId: this.roomId, sender: this.sender, message: this.message }));
            this.message = '';
        },
        recvMessage: function(recv) {
            this.messages.push({
                "type": recv.type,
                "sender": recv.type == 'ENTER' ? '[알림]' : recv.sender,
                "message": recv.message
            });
            this.$nextTick(() => {
                var container = this.$el.querySelector('.wrap');
                container.scrollTop = container.scrollHeight;
            });
        }
    }
});

function connect() {
    ws.connect({}, function (frame) {
        ws.subscribe(`/sub/chat/room/${vm.$data.roomId}`, function (message) {
            var recv = JSON.parse(message.body);
            vm.recvMessage(recv);
        });
        ws.send(`/pub/chat/message/${vm.$data.roomId}`, {}, JSON.stringify({ type: 'ENTER', roomId: vm.$data.roomId, sender: vm.$data.sender }));
    }, function (error) {
        if (reconnect++ <= 5) {
            setTimeout(function () {
                console.log("connection reconnect");
                sock = new SockJS("/ws-stomp");
                ws = Stomp.over(sock);
                connect();
            }, 10 * 1000);
        }
    });
}

connect();
