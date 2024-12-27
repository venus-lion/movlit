import React, {useState} from 'react';
import axios from 'axios';

const MemberRegister = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [nickname, setNickname] = useState('');
    const [dob, setDob] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (password !== repeatPassword) {
            alert('패스워드가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await axios.post('/api/member/register', {
                email,
                password,
                repeatPassword,
                nickname,
                dob,
            });

            console.log('Registration successful:', response.data);
            alert(response.data.message);

            window.location.href = '/member/login';
        } catch (error) {
            console.error('Registration error:', error);
            if (error.response) {
                alert(error.response.data.message);
            } else {
                alert('요청 중 오류가 발생했습니다.');
            }
        }
    };

    return (
        <div className="bg-light">
            {/* 상단 네비게이션 (필요에 따라 추가) */}
            <nav className="navbar navbar-expand-sm bg-dark navbar-dark fixed-top">
                <div className="container-fluid">
                    <img src="/img/ck-logo.png" alt="Logo" height="60"/>
                    <div className="p-2 bg-dard justify-content-center">
                        <img src="https://picsum.photos/1500/180" alt="Banner" width="100%"/>
                    </div>
                </div>
            </nav>

            <div className="container" style={{marginTop: '250px'}}>
                <div className="row">
                    <div className="col-3"></div>
                    <div className="col-6">
                        <div className="card">
                            <div className="card-body">
                                <div className="card-title">
                                    <h3>
                                        <strong>사용자 가입</strong>
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
                                                <label className="col-form-label">패스워드</label>
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
                                            <td>
                                                <label className="col-form-label">패스워드 확인</label>
                                            </td>
                                            <td>
                                                <input
                                                    type="password"
                                                    name="repeatPassword"
                                                    className="form-control"
                                                    value={repeatPassword}
                                                    onChange={(e) => setRepeatPassword(e.target.value)}
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label className="col-form-label">사용자 이름</label>
                                            </td>
                                            <td>
                                                <input
                                                    type="text"
                                                    name="nickname"
                                                    className="form-control"
                                                    value={nickname}
                                                    onChange={(e) => setNickname(e.target.value)}
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label className="col-form-label">생년월일</label>
                                            </td>
                                            <td>
                                                <input
                                                    type="text"
                                                    name="dob"
                                                    className="form-control"
                                                    value={dob}
                                                    onChange={(e) => setDob(e.target.value)}
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

                                <p className="mt-3">
                                    <span className="me-3">이미 사용자 계정이 있으신가요?</span>
                                    <a href="/member/login">로그인</a>
                                </p>

                                {/* 소셜 로그인 버튼 */}
                                <div className="mt-3 mb-3">
                                    <span className="me-3">소셜 계정으로 가입</span>
                                    <span>
                    <a href="/oauth2/authorization/google">
                      <img src="/img/google-logo.png" alt="Google" height="32"/>
                    </a>
                    <a href="/oauth2/authorization/github">
                      <img src="/img/github-logo.png" alt="GitHub" height="32"/>
                    </a>
                    <a href="/oauth2/authorization/naver">
                      <img src="/img/naver-logo.jpg" alt="Naver" height="32"/>
                    </a>
                    <a href="/oauth2/authorization/kakao">
                      <img src="/img/kakao-logo.png" alt="Kakao" height="32"/>
                    </a>
                    <a href="/oauth2/authorization/facebook">
                      <img src="/img/facebook-logo.png" alt="Facebook" height="32"/>
                    </a>
                  </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-3"></div>
                </div>
            </div>
        </div>
    );
};

export default MemberRegister;