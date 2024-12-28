import React, {useState} from 'react';
import axios from 'axios';
import {useNavigate, useOutletContext} from 'react-router-dom';

const MemberLogin = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    // App 컴포넌트에서 Outlet을 통해 전달받은 updateLoginStatus를 사용
    const {updateLoginStatus} = useOutletContext();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('/api/members/login', {email, password});
            const {accessToken, refreshToken} = response.data;

            // 토큰 저장
            sessionStorage.setItem('accessToken', accessToken);
            document.cookie = `refreshToken=${refreshToken}; Secure; HttpOnly; Path=/; Max-Age=1209600`; // 2주

            // 로그인 성공 후 updateLoginStatus를 true로 업데이트
            updateLoginStatus(true);

            // 로그인 성공 후 페이지 리디렉션
            navigate('/member/list');
        } catch (error) {
            setError('로그인 정보가 올바르지 않습니다.');
            console.error('Login error:', error);
        }
    };

    return (
        <div className="bg-light">
            {/* ... 상단 네비게이션 ... */}
            <div className="container" style={{marginTop: '270px'}}>
                <div className="row">
                    <div className="col-4"></div>
                    <div className="col-4">
                        <div className="card">
                            <div className="card-body">
                                <div className="card-title">
                                    <h3>
                                        <strong>로그인</strong>
                                    </h3>
                                </div>
                                <hr/>
                                <form onSubmit={handleSubmit}>
                                    <table className="table table-borderless">
                                        <tbody>
                                        <tr>
                                            <td style={{width: '45%'}}>
                                                <label className="col-form-label">이메일</label>
                                            </td>
                                            <td style={{width: '55%'}}>
                                                <input
                                                    type="text"
                                                    name="email"
                                                    className="form-control"
                                                    value={email}
                                                    onChange={(e) => setEmail(e.target.value)}
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label className="col-form-label">비밀번호</label>
                                            </td>
                                            <td>
                                                <input
                                                    type="password"
                                                    name="password"
                                                    className="form-control"
                                                    value={password}
                                                    onChange={(e) => setPassword(e.target.value)}
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colSpan="2">
                                                <button className="btn btn-primary" type="submit">
                                                    확인
                                                </button>
                                                <button className="btn btn-secondary" type="reset">
                                                    취소
                                                </button>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>

                                {error && <p style={{color: 'red'}}>{error}</p>}

                                <p className="mt-3">
                                    <span className="me-3">사용자 계정이 없으신가요?</span>
                                    <a href="/member/register">사용자 가입</a>
                                </p>

                                {/* ... 소셜 로그인 ... */}
                            </div>
                        </div>
                    </div>
                    <div className="col-4"></div>
                </div>
            </div>
        </div>
    );
};

export default MemberLogin;