import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, TextField, InputAdornment, IconButton } from '@mui/material'
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import YupPassword from 'yup-password'
import { useState } from 'react'
import Grid from '@mui/material/Unstable_Grid2';
import Paper from '@mui/material/Paper';
import { PictureAsPdf } from '@mui/icons-material';
import Divider from '@mui/material/Divider';
import { styled } from '@mui/material/styles';
import RoundedTextField from '../components/roundedTextField';
import RoundedButton from '../components/roundedButton';
YupPassword(yup); //This is needed for password verification



const validationSchema = yup.object({
    name: yup
        .string()
        .required('Name is required'),
    email: yup
        .string()
        .email('Enter a valid email')
        .required('Email is required'),
    phone: yup
        .string(),
    password: yup.string()
        .required('No password provided.')
        .min(8, 'Password is too short - should be 8 chars minimum.')
        .minLowercase(1, 'password must contain at least 1 lower case letter')
        .minUppercase(1, 'password must contain at least 1 upper case letter')
        .minNumbers(1, 'password must contain at least 1 number')
        .minSymbols(1, 'password must contain at least 1 special character'),
    confirm_password: yup
        .string()
        .required("Please Confirm Password")
        .oneOf([yup.ref('password'), null], 'Passwords must match'),
});

interface PropsType {
    handleSubmit: (values: any) => void;
    googleLogin: (value: any) => void;
}

export default function FormikComponent(props: PropsType) {
    const [showPassword, setShowPassword] = useState(false);

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword)
    };

    const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
    };


    const formik = useFormik({
        initialValues: {
            name: '',
            email: '',
            password: '',
            confirm_password: '',
            phone: '',
        },
        validationSchema: validationSchema,
        onSubmit: props.handleSubmit,

    });

    const onGoogleLogin = props.googleLogin

    return (
        <Grid container style={{ height: "100vh" }} alignItems='center' sx={{ 'backgroundColor': '#9DC6E2' }} >
            <Grid xs={4.5} />
            <Grid xs={3}>
                <Paper elevation={1} sx={{ 'padding-top': 10, 'padding-bottom': 34, 'padding-inline': 34, borderRadius: "15px" }}>
                    <Grid>
                        <h1> Sign Up </h1>
                    </Grid>
                    <form onSubmit={formik.handleSubmit}>
                        <Grid container spacing={2.5}>
                            <Grid xs={12}>
                                <RoundedTextField
                                    InputLabelProps={{ 'shrink': true, 'sx': { 'fontWeight': "bold" } }}
                                    fullWidth
                                    focused
                                    id="name"
                                    name="name"
                                    label="Name"
                                    value={formik.values.name}
                                    onChange={formik.handleChange}
                                    error={formik.touched.name && Boolean(formik.errors.name)}
                                    helperText={formik.touched.name && formik.errors.name}
                                />
                            </Grid>
                            <Grid xs={12}>
                                <RoundedTextField
                                    InputLabelProps={{ 'shrink': true, 'style': { 'fontWeight': "bold" } }}
                                    fullWidth
                                    focused
                                    id="email"
                                    name="email"
                                    label="Email"
                                    value={formik.values.email}
                                    onChange={formik.handleChange}
                                    error={formik.touched.email && Boolean(formik.errors.email)}
                                    helperText={formik.touched.email && formik.errors.email}
                                />
                            </Grid>
                            <Grid xs={12}>
                                <RoundedTextField
                                    InputLabelProps={{ 'shrink': true, 'sx': { 'fontWeight': "bold" } }}
                                    fullWidth
                                    focused
                                    id="phone"
                                    name="phone"
                                    label="Phone"
                                    value={formik.values.phone}
                                    onChange={formik.handleChange}
                                    error={formik.touched.phone && Boolean(formik.errors.phone)}
                                    helperText={formik.touched.phone && formik.errors.phone}
                                />
                            </Grid>
                            <Grid xs={12}>
                                <RoundedTextField
                                    InputLabelProps={{ 'shrink': true, 'sx': { 'fontWeight': "bold" } }}
                                    fullWidth
                                    focused
                                    id="password"
                                    name="password"
                                    label="Password"
                                    type={showPassword ? "" : "password"}
                                    value={formik.values.password}
                                    onChange={formik.handleChange}
                                    error={formik.touched.password && Boolean(formik.errors.password)}
                                    helperText={formik.touched.password && formik.errors.password}
                                    InputProps={{ // <-- This is where the toggle button is added.
                                        endAdornment: (
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label="toggle password visibility"
                                                    onClick={handleClickShowPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                >
                                                    {showPassword ? <Visibility /> : <VisibilityOff />}
                                                </IconButton>
                                            </InputAdornment>
                                        )
                                    }}
                                />
                            </Grid>
                            <Grid xs={12}>
                                <RoundedTextField
                                    InputLabelProps={{ 'shrink': true, 'sx': { 'fontWeight': "bold" } }}
                                    fullWidth
                                    focused
                                    id="confirm_password"
                                    name="confirm_password"
                                    label="Confirm Password"
                                    type={showPassword ? "" : "password"}
                                    value={formik.values.confirm_password}
                                    onChange={formik.handleChange}
                                    error={formik.touched.confirm_password && Boolean(formik.errors.confirm_password)}
                                    helperText={formik.touched.confirm_password && formik.errors.confirm_password}
                                />
                            </Grid>
                            <Grid xs={12}>
                                <RoundedButton variant="contained" fullWidth type="submit" sx={{ 'backgroundColor': '#C89CD7' }}>
                                    Sign up
                                </RoundedButton>

                            </Grid>
                            <Grid xs={12}>
                                <Divider variant="middle" sx={{ 'backgroundColor': 'gray' }} />
                            </Grid>
                            <Grid xs={12}>
                                <RoundedButton variant="contained" fullWidth onClick={onGoogleLogin} sx={{ 'backgroundColor': '#C89CD7' }}>
                                    Sign up with Google
                                </RoundedButton>
                            </Grid>
                            <Grid xs={12}>
                                <p style={{ textAlign: 'center' }}>Have an account? <a href="http://localhost:3000/login" style={{ 'color': "#3366CC" }}> Login</a></p>
                            </Grid>
                        </Grid>

                    </form>
                </Paper>
            </Grid >
        </Grid >
    )
}