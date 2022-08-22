import React from "react";
import { Route, Routes } from "react-router-dom";
import LandingPage from "./path/landingPage";

function Paths() {
  return (
    <Routes>
      <Route path="/" exact element={<LandingPage />} />
    </Routes>
  );
}

export default Paths;
