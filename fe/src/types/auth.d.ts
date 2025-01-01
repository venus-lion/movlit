type LoginBody = {
    email: string;
    password: string;
};

type RegisterBody = {
    email: string;
    password: string;
    nickname: string;
    repeatPassword: string;
    tasteMoodId?: string;
};

type UserInfoType = {
    email: string;
    exp: number;
    iat: number;
    id: string;
    iss: string;
};
