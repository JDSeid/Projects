import type { NextPage } from 'next'

// Importing from custom component
import Testing from '../components/test'

// IMPORTING FOR FIREBASE FUNCTIONS EXAMPLE -- We am not using most of these functions currently
import { getAuth, signInWithPopup, GoogleAuthProvider } from "firebase/auth";
import { collection, doc, setDoc, getDoc } from "firebase/firestore";
import { db } from "../firebase";

// Importing MUI component
import { Button } from '@mui/material'


const Home: NextPage = () => {
  // stuff before return
  // hello

  // Functions that use the "await" keyword must be async
  // This function looks into the users/user1 document in the db and prints that document (as key value pairs)
  const usePromiseWithAwait = async () => {
    const docRef = doc(db, "users", "user1");               // Reference to document in DB
    const document = await getDoc(docRef);                  // Pull information based on reference
    console.log("THIS WILL PRINT FIRST", document.data());  // Print data in document
    console.log("THIS WILL PRINT SECOND");
  }

  // This function looks into the users/user1 document in the db and prints that document (as key value pairs)
  const usePromiseWithThen = () => {
    const docRef = doc(db, "users", "user1");
    getDoc(docRef).then((value) => {
      console.log("THIS WILL PRINT SECOND", value.data());
    })
    console.log("THIS WILL PRINT FIRST")
  }

  // You can see console.logs if you right click in chrome, press inspect, and then click console
  return (
    <div>
      {/* <Testing text="" text2="test"></Testing> */}
      {/* When Click Button, run usePromiseWithAwait */}
      <Button onClick={usePromiseWithAwait}>USE PROMISE WITH AWAIT</Button>

      {/* When Click Button, run usePromiseWithThen */}
      <Button onClick={usePromiseWithThen}>USE PROMISE WITH THEN</Button>
    </div>
  )
}

export default Home
