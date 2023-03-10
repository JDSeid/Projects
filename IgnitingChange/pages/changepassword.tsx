import React from "react";
import { Button, TextField, Typography } from '@mui/material'
import '../firebase';
import { useFormik } from 'formik';
import * as yup from 'yup';
import {
    getAuth,
    sendPasswordResetEmail,
} from "firebase/auth";
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import Grid from '@mui/material/Grid';
import { styled } from '@mui/system';

const validationSchema = yup.object({
    email: yup
        .string()
        .email('enter a valid email')
        .required('email is required'),
});

const auth = getAuth();

const headerStyle = { font: 'Inter', fontStyle: 'normal', fontWeight: 700, fontSize: 29, lineHeight: '40px' }
const buttonStyle = {
    "&:hover": {
        backgroundColor: "#C89CD7"
    },
    font: 'Inter',
    fontStyle: 'normal',
    fontWeight: 700,
    fontSize: 15,
    lineHeight: '24px',
    backgroundColor: '#C89CD7',
    borderRadius: '15px',
    textTransform: 'none',
    padding: "10px",
    variant: "contained"
}
const StyledTextField = styled(TextField)({
    "& .MuiOutlinedInput-notchedOutline": {
        borderRadius: "15px"
    },
    "& .MuiOutlinedInput-root": {
        height: "45px",
        font: "Inter",
        fontSize: '15px',
        fontWeight: 400,
        "&.Mui-focused fieldset": {
            borderColor: 'black'
        }
    },
    "& label.Mui-focused": {
        color: '#555555',
        font: "Inter",
        fontSize: '16px',
        fontWeight: 700,
        labelAsterisk: { color: 'white' }
    },
});

const ChangePassword = () => {
    // const router = useRouter();
    const formik = useFormik({
        initialValues: {
            email: '',
        },
        validationSchema: validationSchema,
        onSubmit: (values) => {
            sendPasswordResetEmail(auth, values.email).then(() => {
                alert("Email sent!")
            }).catch((err: any) => {
                console.log(err);
                alert("User not found");
            });
        },
    });

    return (
        <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            sx={{ backgroundColor: '#9DC6E2', minHeight: "100vh", minWidth: "100vw" }}
        >
            <Paper elevation={5} sx={{ width: 400, height: 270, margin: 10, padding: "34px 41px", borderRadius: "15px" }}>
                <form onSubmit={formik.handleSubmit}>
                    <Grid container spacing={3}>
                        <Grid xs={12} item>
                            <Typography sx={headerStyle}>Reset Password</Typography>
                        </Grid>

                        <Grid xs={12} item>
                            <StyledTextField
                                id="email"
                                name="email"
                                label="Email"
                                type="email"
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
                            <Button
                                variant="contained"
                                sx={buttonStyle}
                                type="submit"
                                fullWidth
                            >
                                Send
                            </Button>
                        </Grid>

                    </Grid>
                </form>
            </Paper>
        </Box>
    );

};

export default ChangePassword