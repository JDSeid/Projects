import { Button } from '@mui/material'
import { styled } from '@mui/system';

const RoundedButton = styled(Button)({
    "&:hover": {
        backgroundColor: "#C89CD7"
    },
    "&.MuiButton-root": {
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
    },
});

export default RoundedButton; 
