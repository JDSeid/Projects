// import { Button, TextField } from '@mui/material'
import type { NextPage } from 'next'
import FormikComponent from '../components/FormikComponent'
import { db } from "../firebase"
import { doc, setDoc, getFirestore } from "firebase/firestore";
import Grid from '@mui/material/Grid';
import {
    signInWithPopup,
    GoogleAuthProvider,
    getAuth,
    createUserWithEmailAndPassword
} from "firebase/auth";

const provider = new GoogleAuthProvider();
const auth = getAuth();



function createUser(email: string, password: string, name: string, phoneNumber: string) {
    createUserWithEmailAndPassword(auth, email, password)
        .then(async (userCredential) => {
            // Signed in 
            const user = userCredential.user;

            // add user to dtabase
            await setDoc(doc(db, "users", user.uid), {
                email: user.email,
                name: name,
                phoneNumber: phoneNumber,
            });
        })
        .catch((error) => {
            console.log(error)
            alert(error.message)
        });
}


//Sign up with google
function googleLogin() {
    signInWithPopup(auth, provider)
        .then(async (result) => {
            // This gives you a Google Access Token. You can use it to access the Google API.
            const credential = GoogleAuthProvider.credentialFromResult(result);
            if (credential) {
                const token = credential.accessToken;
            }
            // The signed-in user info.
            const user = result.user;
            // Loop through list users in firebase, check if user is already in it
            await setDoc(doc(db, "users", user.uid), {
                email: user.email,
                name: user.displayName,
                phoneNumber: user.phoneNumber,
            });
        }).catch((error) => {
            // Handle Errors here.
            const errorCode = error.code;
            const errorMessage = error.message;
            // The email of the user's account used.
            const email = error.customData.email;
            // The AuthCredential type that was used.
            const credential = GoogleAuthProvider.credentialFromError(error);
        });
}


const SignUp: NextPage = () => {

    async function submit(values: any) {
        console.log(values)
        createUser(values.email, values.password, values.name, values.phone)
    }

    return (


        <div>
            {/* <button
                onClick={googleLogin}>
                Sign in with Google
            </button> */}
            <FormikComponent handleSubmit={submit} googleLogin={googleLogin} />
        </div>
    )
}

export default SignUp

