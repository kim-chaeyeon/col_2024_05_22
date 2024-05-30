const vm = new Vue({
    el: '#app',
    data() {
        return {
            room_name: '',
            chatrooms: [],
            isAuthenticated: false,
            currentUser: '' // 현재 로그인한 사용자 이름
        };
    },
    created() {
        this.checkAuthentication();
    },
    methods: {
        checkAuthentication() {
            axios.get('/auth/check')
                .then(response => {
                    this.isAuthenticated = response.data.isAuthenticated;
                    if (this.isAuthenticated) {
                        this.currentUser = response.data.username;
                        this.findAllRoom();
                    } else {
                        window.location.href = "/member/login";
                    }
                })
                .catch(error => {
                    console.error('Failed to check authentication:', error);
                    window.location.href = "/member/login";
                });
        },
        findAllRoom() {
            axios.get('/chat/api/rooms').then(response => {
                this.chatrooms = response.data;
            }).catch(error => {
                console.error('Failed to load chat rooms:', error);
                alert("Failed to load chat rooms.");
            });
        },
        getCsrfToken() {
            const tokenElement = document.querySelector('meta[name="_csrf"]');
            return tokenElement ? tokenElement.getAttribute('content') : '';
        },
        createRoom() {
            if (!this.room_name) {
                alert("방 제목을 입력해 주십시오.");
                return;
            }
            const params = new URLSearchParams();
            params.append("name", this.room_name);

            axios.post('/chat/room', params, {
                headers: {
                    'X-CSRF-TOKEN': this.getCsrfToken()
                }
            }).then(response => {
                alert(`${response.data.name} 방이 성공적으로 생성되었습니다.`);
                this.room_name = '';
                this.findAllRoom();
            }).catch(error => {
                console.error('채팅방을 생성하는 데 실패했습니다:', error);
                alert("채팅방을 생성하는 데 실패했습니다.");
            });
        },
        enterRoom(roomId) {
            localStorage.setItem('wschat.sender', this.currentUser);
            localStorage.setItem('wschat.roomId', roomId.toString());  // UUID를 문자열로 명시적 변환
            window.location.href = `/chat/room/enter/${roomId}`;
        }
    }
});
