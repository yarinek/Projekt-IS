export interface loginDTO {
    username: string;
    password: string;
}

export interface registerDTO {
    username: string;
    password: string;
    matchingPassword: string;
    email: string;
}