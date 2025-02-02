import {useRef} from 'react';

const useSSE = () => {
    const eventSourceRef = useRef(null);

    const start = () => {
        if (eventSourceRef.current) {
            console.log("이미 SSE 연결이 활성화됨");
            return;
        }

        const accessToken = sessionStorage.getItem("accessToken");
        if (!accessToken) {
            console.log("토큰 없음, SSE 연결 안 함");
            return;
        }

        console.log("SSE 시작, AccessToken:", accessToken);
        const eventSource = new EventSource(`http://localhost:8080/notification/subscribe/groupChat`, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        eventSourceRef.current = eventSource;

        eventSource.onmessage = (event) => {
            console.log("📩 새 알림:", event.data);
            alert(`📢 알림: ${event.data}`);
        };

        eventSource.onerror = () => {
            console.log("SSE 연결 종료. 재연결 필요");
            eventSource.close();
            eventSourceRef.current = null;
        };
    };

    const stop = () => {
        if (eventSourceRef.current) {
            console.log("SSE 연결 해제");
            eventSourceRef.current.close();
            eventSourceRef.current = null;
        }
    };

    return {start, stop};
};

export default useSSE;