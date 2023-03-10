
import { styled } from '@mui/system';
import { TextField } from '@mui/material'

const RoundedTextField = styled(TextField)({
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

export default RoundedTextField;

