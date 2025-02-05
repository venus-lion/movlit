import React, {useState} from 'react';
import axiosInstance from '../axiosInstance';
import {useNavigate, useOutletContext} from 'react-router-dom';
import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const MemberLogin = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const {updateLoginStatus} = useOutletContext();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axiosInstance.post('/members/login', {
                email,
                password,
            });
            const {refreshToken} = response.data;

            console.log('refreshToken = ', refreshToken);

            document.cookie = `refreshToken=${refreshToken}; Secure; HttpOnly; Path=/; Max-Age=1209600`;
            // Secure; <- Https 후 꼭 추가해주기

            updateLoginStatus(true);
            console.log('로그인 요청 성공');
            toast.success('로그인에 성공했습니다!');
            console.log('알림 표시 후');
            navigate('/');
        } catch (error) {
            setError('로그인 정보가 올바르지 않습니다.');
            console.error('Login error:', error);
            toast.error('로그인 정보가 올바르지 않습니다.');
        }
    };

    return (
        <div className="bg-light">
            <div className="container" style={{marginTop: '30px'}}>
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
                                            <td colSpan="2" className="center-buttons">
                                                <button
                                                    className="btn btn-primary"
                                                    type="submit"
                                                    style={{marginRight: '5px'}}
                                                >
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
                                    <span className="me-3">계정이 없으신가요? </span>
                                    <a href="/member/register">회원 가입</a>
                                </p>

                                {/* 소셜 로그인 버튼 */}
                                <div className="mt-3 mb-3">
                                    <div className="social-login-header">
                                        <span>소셜 계정으로 가입</span>
                                    </div>
                                    <div className="social-login-buttons">
                                        {/*<a*/}
                                        {/*    href="/oauth2/authorization/google"*/}
                                        {/*    className="social-login-button"*/}
                                        {/*>*/}
                                        {/*    <img*/}
                                        {/*        src="/images/google-logo.png"*/}
                                        {/*        alt="Google"*/}
                                        {/*        className="social-login-icon"*/}
                                        {/*    />*/}
                                        {/*</a>*/}
                                        <a href="/oauth2/authorization/naver" className="social-login-button">
                                            <img
                                                src="/images/naver-logo.jpg"
                                                alt="Naver"
                                                className="social-login-icon"
                                            />
                                        </a>
                                        {/*<a*/}
                                        {/*    href="/oauth2/authorization/kakao"*/}
                                        {/*    className="social-login-button"*/}
                                        {/*>*/}
                                        {/*    <img*/}
                                        {/*        src="/images/kakao-logo.png"*/}
                                        {/*        alt="Kakao"*/}
                                        {/*        className="social-login-icon"*/}
                                        {/*    />*/}
                                        {/*</a>*/}
                                    </div>
                                </div>
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