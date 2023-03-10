import { Button } from '@mui/material'
import type { NextPage } from 'next'
import Head from 'next/head'
import Image from 'next/image'
import Testing from '../components/test'
import styles from '../styles/Home.module.css'
// IMPORTING FOR FIREBASE EXAMPLE
import { getAuth, signInWithPopup, GoogleAuthProvider } from "firebase/auth";


const TestPage: NextPage = () => {
    const x = 10;
    return (
        <div>
            Example
        </div>
    )
}

export default TestPage
