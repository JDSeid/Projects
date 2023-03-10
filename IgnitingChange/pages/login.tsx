import '../firebase';
import { useState } from 'react';
import { Button, TextField, InputAdornment, Link, IconButton, Typography } from '@mui/material'
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';
import {
    getAuth,
    signInWithPopup,
    GoogleAuthProvider,
    signInWithEmailAndPassword,
} from "firebase/auth";
import React from 'react';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { useRouter } from 'next/router';
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import { styled } from '@mui/system';
import RoundedTextField from '../components/roundedTextField';
import RoundedButton from '../components/roundedButton';

const auth = getAuth();
const provider = new GoogleAuthProvider();

// require users to fill fields 
const validationSchema = yup.object({
    email: yup
        .string()
        .email('enter a valid email')
        .required('email is required'),
    password: yup
        .string()
        .required('password is required'),
});

// login with google popup 
async function googleLogin() {
    try {
        await signInWithPopup(auth, provider);
    } catch (err) {
        console.error(err);
    }
}

// style changes 
const headerStyle = { font: 'Inter', fontStyle: 'normal', fontWeight: 700, fontSize: 29, lineHeight: '40px' }


// regular login 
const Login = () => {
    const [errorMessage, setErrorMessage] = useState(false);

    const handleClose = (event?: React.SyntheticEvent | Event, reason?: string) => {
        if (reason === 'clickaway') {
            return;
        }
        setErrorMessage(false);
    };

    const router = useRouter();
    const formik = useFormik({
        initialValues: {
            email: '',
            password: '',
        },
        validationSchema: validationSchema,
        onSubmit: values => {
            signInWithEmailAndPassword(auth, values.email, values.password).then(() => {
                router.push('/');
            }).catch((err) => {
                setErrorMessage(true);
            })
        },
    });

    // takes use to sign up page 
    function SignUp() {
        return (
            <Link
                sx={{ paddingTop: 3, paddingLeft: 9, color: '#555555', textDecoration: 'none', font: 'Inter' }}
                component="button"
                onClick={() => router.push('/signup')}
            >
                Don't have an account? Sign up
            </Link>
        );
    }

    // takes user to change password page 
    function ChangePassword() {
        return (
            <Link
                sx={{ paddingTop: 1, paddingLeft: 12, color: '#555555', textDecoration: 'none', font: 'Inter' }}
                component="button"
                onClick={() => router.push('/changepassword')}
            >
                Forgot your password?
            </Link>
        );
    }

    // make password visible 
    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword(!showPassword);

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            sx={{ backgroundColor: '#9DC6E2', minHeight: "100vh", minWidth: "100vw" }}
        >
            <Paper elevation={5} sx={{ width: 400, margin: 10, padding: "34px 41px", borderRadius: "15px" }}>
                <form onSubmit={formik.handleSubmit}>
                    <Grid container spacing={2.5}>
                        <Grid xs={12} item>
                            <Typography sx={headerStyle}>Login</Typography>
                        </Grid>

                        <Grid xs={12} item>
                            <RoundedTextField
                                id="email"
                                name="email"
                                label="Email"
                                InputLabelProps={{ required: false }}
                                value={formik.values.email}
                                onChange={formik.handleChange}
                                error={formik.touched.email && Boolean(formik.errors.email)}
                                helperText={formik.touched.email && formik.errors.email}
                                fullWidth
                                focused
                            />
                        </Grid>
                        <Grid xs={12} item>
                            <RoundedTextField
                                id="password"
                                name="password"
                                label="Password"
                                type={showPassword ? "text" : "password"}
                                InputLabelProps={{ required: false }}
                                value={formik.values.password}
                                onChange={formik.handleChange}
                                error={formik.touched.password && Boolean(formik.errors.password)}
                                helperText={formik.touched.password && formik.errors.password}
                                fullWidth
                                focused
                                InputProps={{
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={handleClickShowPassword}
                                            >
                                                {showPassword ? <Visibility /> : <VisibilityOff />}
                                            </IconButton>
                                        </InputAdornment>
                                    )
                                }}
                            />
                        </Grid>
                        <Grid xs={12} item>
                            <RoundedButton
                                variant="contained"
                                fullWidth
                                type="submit"
                            >
                                Login
                            </RoundedButton>
                        </Grid>
                        <Grid xs={12} item>
                            <RoundedButton
                                variant="contained"
                                fullWidth
                                onClick={googleLogin}
                            >
                                Google Login
                            </RoundedButton>
                        </Grid>
                        <Grid>
                            <Grid xs={12} item>
                                <SignUp />
                            </Grid>
                            <Grid xs={12} item>
                                <ChangePassword />
                            </Grid>
                        </Grid>
                    </Grid>
                </form>
            </Paper>
            <Snackbar
                anchorOrigin={{
                    horizontal: 'center',
                    vertical: 'top',
                }}
                open={errorMessage}
                onClose={handleClose}
            >
                <Alert sx={{ width: 300 }} onClose={handleClose} severity="error">
                    Wrong email/password entered
                </Alert>
            </Snackbar>
        </Box>
    );

};

export default Login
