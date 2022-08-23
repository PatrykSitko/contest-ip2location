import React from "react";
import { Route, Routes } from "react-router-dom";
import Menu from "../components/menu";
import LandingPage from "./path/landingPage";
import LoginPage from "./path/loginPage";
import RegisterPage from "./path/registerPage";

function Paths({ user = { loggedIN: false } }) {
  return (
    <>
      {user.loggedIN && <Menu />}
      <Routes>
        {(user.loggedIN && (
          <>
            <Route path="/" exact element={<LandingPage />} />
          </>
        )) || (
          <>
            {["/", "/login"].map((path, key) => (
              <Route {...{ path, key }} exact element={<LoginPage />} />
            ))}
            <Route path="/register" exact element={<RegisterPage />} />
          </>
        )}
      </Routes>
    </>
  );
}

export default Paths;
