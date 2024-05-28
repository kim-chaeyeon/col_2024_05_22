var vm = new Vue({
    el: '#app',
    data: {
        roomId: '',
        room: {},
        sender: '',
        message: '',
        messages: [],
        ws: null, // WebSocket 객체를 data 속성에 추가
        reconnectAttempts: 0
    },
    created() {
        this.roomId = localStorage.getItem('wschat.roomId');
        this.sender = localStorage.getItem('wschat.sender');
        if (!this.roomId || !this.sender) {
            console.error("Valid room ID or sender not found in localStorage. Found:", this.roomId, this.sender);
            return;
        }
        this.initializeWebSocket(); // WebSocket 초기화를 별도의 메소드로 분리
        this.findRoom();
    },
    methods: {
        initializeWebSocket() {
            var sock = new SockJS("/ws-stomp");
            this.ws = Stomp.over(sock);
            this.connect();
        },
        connect() {
            this.ws.connect({}, (frame) => {
                this.ws.subscribe(`/sub/chat/room/${this.roomId}`, (message) => {
                    var recv = JSON.parse(message.body);
                    this.recvMessage(recv);
                });
                this.ws.send(`/pub/chat/message/${this.roomId}`, {}, JSON.stringify({ type: 'ENTER', roomId: this.roomId, sender: this.sender }));
            }, (error) => {
                if (this.reconnectAttempts++ < 5) {
                    setTimeout(this.connect, 10000); // 재연결 로직
                    console.log("Attempt to reconnect #" + this.reconnectAttempts);
                } else {
                    console.error("Failed to reconnect after 5 attempts.");
                }
            });
        },
        findRoom() {
            axios.get('/chat/room/' + this.roomId).then(response => {
                this.room = response.data;
                document.querySelector('.room-name h2').innerText = this.room.name;
            }).catch(error => {
                console.error('Error fetching room details:', error);
            });
        },
        sendMessage() {
            if (!this.message.trim()) return;
            this.ws.send(`/pub/chat/message/${this.roomId}`, {}, JSON.stringify({
                type: 'TALK',
                roomId: this.roomId,
                sender: this.sender,
                message: this.message.trim()
            }));
            this.message = '';
        },
        recvMessage(recv) {
            this.messages.push({
                type: recv.type,
                sender: recv.type === 'ENTER' ? '[알림]' : recv.sender,
                message: recv.message
            });
            this.$nextTick(() => {
                var container = this.$el.querySelector('.wrap');
                container.scrollTop = container.scrollHeight;
            });
        }
    }
});
