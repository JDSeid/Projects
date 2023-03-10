import { Button } from '@mui/material'
import type { NextPage } from 'next'
import Head from 'next/head'
import Image from 'next/image'
import styles from '../styles/Home.module.css'
import { getDatabase, ref, onValue } from "firebase/database";
import { collection, doc, setDoc, getDoc, getDocs, DocumentData, QueryDocumentSnapshot } from "firebase/firestore";
import { db } from "../firebase";
import { getAuth, signInWithPopup, GoogleAuthProvider, onAuthStateChanged } from "firebase/auth";
import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';


interface Props {
  Users: User[]
  isAdmin: boolean
}


const adminHome: NextPage = (props: Props) => {
  if (props.isAdmin == false) {
    return (
      <div> 404 not found </div>
    )
  }
  return (
    <div>
      <div>
        <text> Hello world </text>
      </div>

      <TableContainer component={Paper}>
        <Table sx={{ maxWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell>Name </TableCell>
              <TableCell align="right">Email</TableCell>
              <TableCell align="right">PhoneNumber</TableCell>
              <TableCell align="right">Role</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {(props.Users).map((user) => (
              <TableRow
                key={user.name}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {user.name}
                </TableCell>
                <TableCell align="right">{user.email}</TableCell>
                <TableCell align="right">{user.phoneNumber}</TableCell>
                <TableCell align="right">{user.role}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  )
}

export default adminHome




interface User {
  email: string;
  name: string;
  phoneNumber: string;
  role: string
}

export async function getServerSideProps() {
  let isAdmin: Boolean = false;
  const auth = getAuth();
  onAuthStateChanged(auth, (user) => {
    if (user) {
      if (user.email == "cathy@ictc.org") {
        isAdmin = true;
      }
      // User is signed in, see docs for a list of available properties
      // https://firebase.google.com/docs/reference/js/firebase.User
      const uid = user.uid;
      // ...
    } else {
      return {
        props: {
          checkAdmin: isAdmin
        }
      }
    }
  });


  const colRef = collection(db, "users");
  const docsnap = await getDocs(colRef);
  let Data: User[] = [];

  docsnap.forEach(element => {
    let elemData = element.data();
    let currUser: User = {
      name: elemData.name, email: elemData.email,
      phoneNumber: elemData.phoneNumber, role: "Volunteer"
    };
    Data.push(currUser);
  });


  return {

    props: {
      Users: Data,
      checkAdmin: isAdmin
    },
  }
}
