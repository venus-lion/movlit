import React, { useState, useEffect } from 'react';
import axiosInstance from '../axiosInstance';
import { useNavigate } from 'react-router-dom';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import Select from 'react-select';

const MemberUpdate = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [nickname, setNickname] = useState('');
    const [dob, setDob] = useState(null);
    const [genres, setGenres] = useState([]);
    const [selectedGenres, setSelectedGenres] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axiosInstance.get('/members/myPage');
                const userData = response.data;
                setEmail(userData.email);
                setNickname(userData.nickname);
                setDob(userData.dob ? new Date(userData.dob) : null);
                setSelectedGenres(
                    userData.genreList.map((genre) => ({
                        value: genre.genreId,
                        label: genre.genreName,
                    }))
                );
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };

        const fetchGenres = async () => {
            try {
                const response = await axiosInstance.get('/genreList');
                const genreOptions = response.data.map((genre) => ({
                    value: genre.genreId,
                    label: genre.genreName,
                }));
                setGenres(genreOptions);
            } catch (error) {
                console.error('Error fetching genres:', error);
            }
        };

        fetchUserData();
        fetchGenres();
    }, []);

    const handleGenreChange = (selectedOptions) => {
        setSelectedGenres(selectedOptions);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (password !== repeatPassword) {
            alert('패스워드가 일치하지 않습니다.');
            return;
        }

        if (selectedGenres.length < 3) {
            alert('최소 3개의 장르를 선택해야 합니다.');
            return;
        }

        try {
            const response = await axiosInstance.put('/members/update', {
                email,
                password,
                repeatPassword,
                nickname,
                dob: dob ? dob.toISOString().slice(0, 10) : null,
                genreIds: selectedGenres.map((genre) => genre.value),
            });

            console.log('Update successful:', response.data);
            alert('회원 정보가 수정되었습니다.');
            navigate('/mypage');
        } catch (error) {
            console.error('Update error:', error);
            if (error.response) {
                alert(error.response.data.message);
            } else {
                alert('요청 중 오류가 발생했습니다.');
            }
        }
    };

    return (
        // MemberRegister와 유사한 폼 구조를 사용합니다.
        <div className="bg-light">
            <div className="container" style={{ marginTop: '30px' }}>
                <div className="row">
                    <div className="col-3"></div>
                    <div className="col-6">
                        <div className="card">
                            <div className="card-body">
                                <div className="card-title">
                                    <h3>
                                        <strong>회원 수정</strong>
                                    </h3>
                                </div>
                                <hr />
                                <form onSubmit={handleSubmit}>
                                    <table className="table table-borderless">
                                        <tbody>
                                        <tr>
                                            <td style={{ width: '45%' }}>
                                                <label className="col-form-label">이메일</label>
                                            </td>
                                            <td style={{ width: '55%' }}>
                                                <input
                                                    type="text"
                                                    name="email"
                                                    className="form-control"
                                                    value={email}
                                                    onChange={(e) => setEmail(e.target.value)}
                                                    disabled // 이메일은 변경 불가능하도록 설정
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
                                                    dateFormat="yyyy-MM-dd"
                                                    className="form-control"
                                                    isClearable
                                                    showYearDropdown
                                                    scrollableYearDropdown
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label className="col-form-label">장르</label>
                                            </td>
                                            <td>
                                                <Select
                                                    isMulti
                                                    options={genres}
                                                    value={selectedGenres}
                                                    onChange={handleGenreChange}
                                                    placeholder="장르 선택 (최소 3개)"
                                                    styles={{
                                                        control: (provided) => ({
                                                            ...provided,
                                                            marginBottom: '15px',
                                                        }),
                                                        menu: (provided) => ({
                                                            ...provided,
                                                            zIndex: 9999,
                                                        }),
                                                    }}
                                                />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colSpan="2" className="center-buttons">
                                                <button
                                                    className="btn btn-primary"
                                                    type="submit"
                                                    style={{ marginRight: '5px' }}
                                                >
                                                    수정
                                                </button>
                                                <button className="btn btn-secondary" type="button" onClick={() => navigate('/mypage')}>
                                                    취소
                                                </button>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div className="col-3"></div>
                </div>
            </div>
        </div>
    );
};

export default MemberUpdate;