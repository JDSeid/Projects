// Import the functions you need from the SDKs you need
import { initializeApp, getApps, getApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getStorage } from "firebase/storage";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyBYIf8OPIeqwqmdELEJJny1ZgBJFHa0h1M",
    authDomain: "ignitingchange-1e259.firebaseapp.com",
    projectId: "ignitingchange-1e259",
    storageBucket: "ignitingchange-1e259.appspot.com",
    messagingSenderId: "816509659289",
    appId: "1:816509659289:web:56971d5da5e267a3d8f008",
    measurementId: "G-T9ZT26VN9S"
};

// Initialize Firebase
const app = getApps().length === 0 ? initializeApp(firebaseConfig) : getApp()
export default app;

export const db = getFirestore(app);
export const storage = getStorage(app);
