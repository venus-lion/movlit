import React, {useState} from 'react';
import axiosInstance from '../axiosInstance';
import {Link, useNavigate} from 'react-router-dom';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css'; // CSS 임포트

const MemberRegister = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [nickname, setNickname] = useState('');
    const [dob, setDob] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (password !== repeatPassword) {
            alert('패스워드가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await axiosInstance.post('/members/register', {
                email,
                password,
                repeatPassword,
                nickname,
                dob: dob ? dob.toISOString().slice(0, 10) : null, // dob 변환,
            });

            console.log('Registration successful:', response.data);
            alert('회원 가입이 완료되었습니다.');

            navigate('/member/login');
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
            <div className="container" style={{marginTop: '30px'}}>
                <div className="row">
                    <div className="col-3"></div>
                    <div className="col-6">
                        <div className="card">
                            <div className="card-body">
                                <div className="card-title">
                                    <h3>
                                        <strong>회원 가입</strong>
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
                                                <label className="col-form-label">
                                                    패스워드 확인
                                                </label>
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
                                                <label className="col-form-label">닉네임</label>
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
                                                <DatePicker
                                                    selected={dob}
                                                    onChange={(date) => setDob(date)}
                                                    dateFormat="yyyy-MM-dd" // 원하는 날짜 형식 지정
                                                    className="form-control" // 부트스트랩 클래스 적용
                                                    isClearable // 선택 해제 버튼 추가
                                                    showYearDropdown // 연도 선택 드롭다운 표시
                                                    scrollableYearDropdown // 연도 스크롤 가능
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

                                <p className="mt-3">
                                    <span className="me-3">이미 계정이 있으신가요? </span>
                                    <Link to="/member/login">로그인</Link>
                                </p>

                                {/* 소셜 로그인 버튼 */}
                                <div className="mt-3 mb-3">
                                <div className="social-login-header">
                                        <span>소셜 계정으로 가입</span>
                                    </div>
                                    <div className="social-login-buttons">
                                        <a
                                            href="/oauth2/authorization/google"
                                            className="social-login-button"
                                        >
                                            <img
                                                src="/images/google-logo.png"
                                                alt="Google"
                                                className="social-login-icon"
                                            />
                                        </a>
                                        <a
                                            href="/oauth2/authorization/naver"
                                            className="social-login-button"
                                        >
                                            <img
                                                src="/images/naver-logo.jpg"
                                                alt="Naver"
                                                className="social-login-icon"
                                            />
                                        </a>
                                        <a
                                            href="/oauth2/authorization/kakao"
                                            className="social-login-button"
                                        >
                                            <img
                                                src="/images/kakao-logo.png"
                                                alt="Kakao"
                                                className="social-login-icon"
                                            />
                                        </a>
                                    </div>
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