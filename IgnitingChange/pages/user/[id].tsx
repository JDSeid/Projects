import '../../firebase';
import type { AppProps } from 'next/app'
import Testing from '../../components/test'
import type { NextPage } from 'next'

import { getAuth, signInWithPopup, GoogleAuthProvider } from "firebase/auth";
import { getFirestore, doc, getDoc, DocumentReference } from "firebase/firestore";
import { useState, useEffect } from 'react'

import { useRouter } from 'next/router'
import ErrorPage from 'next/error'

import { User } from "../../types/firebaseTypes"
import Box from '@mui/material/Box';

import UserUpdate from '../../components/userUpdate'


interface PageStatus {
    loading: boolean;
    error: boolean;
}

const UserPage: NextPage = () => {

    const db = getFirestore()
    const router = useRouter();

    const [user, setUser] = useState<User>({ name: "", email: "", phoneNumber: "" });
    const [pageStatus, setPageStatus] = useState<PageStatus>({ loading: false, error: false });

    const auth = getAuth();
    const currUser = auth.currentUser;

    useEffect(() => {

        if (!router.isReady)
            return;

        const id = router.query.id as string;

        getDoc(doc(db, "users", id)).then(value => {
            setUser(value.data() as User);

            setPageStatus({ loading: true, error: value.exists() });
        })

    }, [router.isReady])

    if (pageStatus.loading) {
        if (pageStatus.error) {
            return (
                <Box>
                    Email: {user?.email}
                    <br></br>
                    Name: {user?.name}
                    <br></br>
                    Phone Number: {user?.phoneNumber}
                    <br></br>
                    <UserUpdate></UserUpdate>
                </Box>
            );
        }
        else {
            return (
                <ErrorPage statusCode={404} />
            );
        }
    }
    else {
        return null
    }

}

export default UserPage