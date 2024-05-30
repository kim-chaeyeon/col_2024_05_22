const vm = new Vue({
    el: '#app',
    data() {
        return {
            room_name: '',
            chatrooms: []
        };
    },
    created() {
        this.findAllRoom();
    },
    methods: {
        findAllRoom() {
            axios.get('/chat/api/rooms').then(response => {
                this.chatrooms = response.data;
            }).catch(error => {
                console.error('Failed to load chat rooms:', error);
                alert("Failed to load chat rooms.");
            });
        },
        createRoom() {
            if (!this.room_name) {
                alert("방 제목을 입력해 주십시오.");
                return;
            }
            const params = new URLSearchParams();
            params.append("name", this.room_name);
            axios.post('/chat/room', params).then(response => {
                alert(`${response.data.name} 방이 성공적으로 생성되었습니다.`);
                this.room_name = '';
                this.findAllRoom();
            }).catch(error => {
                console.error('채팅방을 생성하는 데 실패했습니다:', error);
                alert("채팅방을 생성하는 데 실패했습니다.");
            });
        },
        enterRoom(roomId) {
            const sender = prompt('대화명을 입력해 주세요.');
            if (sender) {
                localStorage.setItem('wschat.sender', sender);
                localStorage.setItem('wschat.roomId', roomId.toString());  // UUID를 문자열로 명시적 변환
                window.location.href = `/chat/room/enter/${roomId}`;
            }
        }
    }
});
